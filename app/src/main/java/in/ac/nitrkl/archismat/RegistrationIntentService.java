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

                sendRegistrationToServer(token);
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

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }


}
