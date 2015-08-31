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

/**
 * Created by avay on 25/8/15.
 */
public class ArchismatGcmListnerService extends GcmListenerService {

    private static final String LOG_TAG = "GcmListner";
    public static final String TYPE = "type";
    private String mCurrentFilePath;

    @Override
    public void onMessageReceived(String from, Bundle bundle) {

        String data = bundle.getString("data");

        Log.i(LOG_TAG, "From: " + from );
        Log.i(LOG_TAG, "Message: " + data);

        sendNotification(data);

        try {
            insertIntoDataBase(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void insertIntoDataBase(String data) throws JSONException {

        JSONObject dataObject = new JSONObject(data);

        int type = dataObject.getInt("type");

        String receiveTime = ArchismatContract.getDbDateString( new Date() );

        switch ( type ) {
            case 0:
                String message = dataObject.getString("message");
                getContentResolver().insert( ArchismatContract.CONTENT_URI,
                        getAlertValues(receiveTime, message) );
                Log.d("Alert message", message);
                break;
            case 1:
                String desc = dataObject.getString("desc");
                String location = dataObject.getString("location");
                double longitude = dataObject.getDouble("long");
                double latitude = dataObject.getDouble("lat");
                getContentResolver().insert( ArchismatContract.CONTENT_URI,
                        getEventValues(receiveTime, desc, location, longitude, latitude) );
                break;
            case 2:
                String url = dataObject.getString("url");
                String imageDesc = dataObject.getString("desc");
                getContentResolver().insert(ArchismatContract.CONTENT_URI,
                        getPictureValues(receiveTime, imageDesc, url));
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

    private ContentValues getEventValues(String date, String desc, String location, double longitude, double latitude) {

        ContentValues values = new ContentValues();

        values.put(ArchismatContract.UPDATE_TYPE, 1);
        values.put(ArchismatContract.DESCRIPTION, desc);
        values.put(ArchismatContract.RECEIVE_TIME, date);
        values.put(ArchismatContract.LOCATION_NAME, location);
        values.put(ArchismatContract.LOCATION_LONG, longitude);
        values.put(ArchismatContract.LOCATION_LAT, latitude);

        return values;
    }

    private ContentValues getPictureValues(String date, String desc, String imageUrl) {

        // Image download snippet
        Uri imageUri = Uri.parse( imageUrl );
        String mFileName = imageUri.getLastPathSegment();

        HttpURLConnection connection = null;
        Uri diskImageUri = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            URL url = new URL( imageUri.toString() );
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();

            inputStream = connection.getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream( inputStream );

            File imageFilePath = createImageFile(mFileName);

            Log.d("FILE_NAME", mCurrentFilePath);

            fileOutputStream = new FileOutputStream( imageFilePath );
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            galleryAddPic();

            if( isExternalStorageReadable() ) {
                File albumDir = createAlbum();
                File imageDir = new File(albumDir, mFileName);
                diskImageUri = Uri.fromFile( imageDir );
            }


        } catch (IOException e) {
            e.printStackTrace();
            if( connection != null ) {
                connection.disconnect();
            }

            if( inputStream != null ) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if( fileOutputStream != null ) {
                try {
                    fileOutputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }

        if( connection != null ) {
            connection.disconnect();
        }

        if( inputStream != null ) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ContentValues values = new ContentValues();

        values.put(ArchismatContract.UPDATE_TYPE, 2);
        values.put(ArchismatContract.DESCRIPTION, desc);
        values.put(ArchismatContract.RECEIVE_TIME, date);
        values.put(ArchismatContract.FEATURED_PICK, diskImageUri.toString());

        return values;
    }

    private void  galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File( mCurrentFilePath );
        Uri fileUri = Uri.fromFile( file );
        mediaScanIntent.setData(fileUri);
        this.sendBroadcast( mediaScanIntent );
    }

    private File createImageFile(String fileName) throws IOException {

        File imageFile = null;

        if( isExternalStorageWritable() ) {
            File fileDirectory = createAlbum();
            imageFile = new File(fileDirectory, fileName);
        } else {
            Log.e("WRITABLE", "External storage not writable");
        }

        if( imageFile != null ) {
            mCurrentFilePath = imageFile.getAbsolutePath();
        }

        return imageFile;
    }

    private File createAlbum() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Archismat");

        if( !file.mkdir() ) {
            Log.e("MKDIR", "Failed to create file OR Directory exists");
        }
        return file;
    }

    private boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

}
