package in.ac.nitrkl.archismat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import in.ac.nitrkl.archismat.data.ArchismatContract;
import in.ac.nitrkl.archismat.util.Notification;

/**
 * Created by avay on 25/8/15.
 */
public class ArchismatGcmListnerService extends GcmListenerService {

    private static final String LOG_TAG = "GcmListner";

    @Override
    public void onMessageReceived(String from, Bundle bundle) {

        String data = bundle.getString("message");

        Log.i(LOG_TAG, "From: " + from);
        Log.i(LOG_TAG, "Message: " + data);

        try {
            insertIntoDataBase(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoDataBase(String data) throws JSONException {

        JSONObject dataObject = new JSONObject(data);

        int type = dataObject.getInt("type");

        String receiveTime = ArchismatContract.getDbDateString(new Date());

        boolean notification = PreferenceManager.getDefaultSharedPreferences(this)
                                .getBoolean(getString(R.string.SETTING_NOTIFICATION_KEY), true);

        switch ( type ) {
            case 0:
                String message = dataObject.getString("message");
                getContentResolver().insert( ArchismatContract.CONTENT_URI,
                        getAlertValues(receiveTime, message) );

                if( notification && !MainActivity.isInForeground) {
                    Notification.sendAlertNotification(
                            getApplication(),
                            getResources().getString(R.string.alert_notification_title),
                            message
                    );
                }
                Log.d("Alert message", message);
                break;
            case 1:
                String desc = dataObject.getString("desc");
                String name = dataObject.getString("name");
                String location = dataObject.getString("location");
                double longitude = dataObject.getDouble("long");
                double latitude = dataObject.getDouble("lat");
                getContentResolver().insert( ArchismatContract.CONTENT_URI,
                        getEventValues(receiveTime, desc, name, location, longitude, latitude) );
                if( notification && !MainActivity.isInForeground) {
                    Notification.sendEventNotification(
                            getApplication(),
                            getResources().getString(R.string.event_notification_title),
                            name +  " (" + location + ")"
                    );
                }
                break;
            case 2:
                String url = dataObject.getString("url");
                String imageDesc = dataObject.getString("desc");
                new DownloadImageTask(getApplication(), imageDesc, receiveTime, false).execute( url );
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private ContentValues getAlertValues(String date, String message){

        ContentValues values = new ContentValues();

        values.put(ArchismatContract.UPDATE_TYPE, 0);
        values.put(ArchismatContract.DESCRIPTION, message);
        values.put(ArchismatContract.RECEIVE_TIME, date);
        return values;
    }

    private ContentValues getEventValues(String date, String desc, String name, String location, double longitude, double latitude) {

        ContentValues values = new ContentValues();

        values.put(ArchismatContract.UPDATE_TYPE, 1);
        values.put(ArchismatContract.DESCRIPTION, desc);
        values.put(ArchismatContract.EVENT_NAME, name);
        values.put(ArchismatContract.RECEIVE_TIME, date);
        values.put(ArchismatContract.LOCATION_NAME, location);
        values.put(ArchismatContract.LOCATION_LONG, longitude);
        values.put(ArchismatContract.LOCATION_LAT, latitude);

        return values;
    }




}
