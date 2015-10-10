package in.ac.nitrkl.archismat;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.ac.nitrkl.archismat.util.Util;

/**
 * Created by avay on 25/8/15.
 */
public class RegistrationIntentService extends IntentService {

    private static final String LOG_TAG = "RegistrationService";
    private static final String[] TOPICS = {"archismat"};

    public RegistrationIntentService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            synchronized (LOG_TAG) {

                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                boolean tokenRefreshed = preferences.getBoolean(ArchismatPreferences.TOKEN_REFRESHED, false);

                sendRegistrationToServer(token, tokenRefreshed);
                subscribeTopics(token);

                preferences.edit().putBoolean(ArchismatPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.toString());

            preferences.edit().putBoolean(ArchismatPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }

        Intent registrationComplete = new Intent(ArchismatPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void sendRegistrationToServer(String token, boolean tokenRefreshed) {

        //Send registration to Application server

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        int previousTokenId = sharedPreferences.getInt(ArchismatPreferences.LAST_TOKEN_ID, -1);
        String urlString = Util.BASE_URL + "app/register-device.php?token=" + token + "&refreshed=" + tokenRefreshed + "&update_id=" + previousTokenId;

        Log.d(LOG_TAG, urlString);

        try {

            URL registrationUrl = new URL( urlString );
            HttpURLConnection conn = (HttpURLConnection) registrationUrl.openConnection();
            conn.setRequestMethod( "GET" );
            conn.setConnectTimeout(10000);
            conn.setDoInput( true );
            conn.connect();

            InputStream is = conn.getInputStream();

            int readChar;
            StringBuffer response = new StringBuffer();

            while( (readChar = is.read()) != -1  ) {
                response.append( (char) readChar );
            }

            Log.d(LOG_TAG, response.toString() );

            int  tokenId = Integer.parseInt( response.toString() );

            if( tokenId != -1 && !tokenRefreshed ) {
                sharedPreferences.edit().putInt(ArchismatPreferences.LAST_TOKEN_ID, tokenId).apply();
            }

            if( tokenRefreshed && tokenId != -1 ) {
                sharedPreferences.edit().putBoolean(ArchismatPreferences.TOKEN_REFRESHED, false).apply();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }


}
