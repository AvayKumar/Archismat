package in.ac.nitrkl.archismat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.ac.nitrkl.archismat.data.ArchismatContract;

/**
 * Created by avay on 4/9/15.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Uri>{

    Context context;
    String mDesc, mDataTime;

    public DownloadImageTask(Context context, String desc, String dateTime) {
        super();
        this.context = context;
        this.mDesc = desc;
        this.mDataTime = dateTime;
    }

    private static final String LOG_TAG = "DownloadImageTask";
    private String mCurrentFilePath;

    @Override
    protected Uri doInBackground(String... downloadUrl) {

        String imageUrl = downloadUrl[0];

        Uri imageUri = Uri.parse( imageUrl );
        String mFileName = imageUri.getLastPathSegment();

        Log.d(LOG_TAG, mFileName);
        Log.d(LOG_TAG, imageUrl);

        HttpURLConnection connection = null;
        Uri diskImageUri = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {

            URL url = new URL( imageUri.toString() );
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            inputStream = connection.getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);

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

        return diskImageUri;
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);

        ContentValues values = new ContentValues();

        values.put(ArchismatContract.UPDATE_TYPE, 2);
        values.put(ArchismatContract.DESCRIPTION, mDesc);
        values.put(ArchismatContract.RECEIVE_TIME, mDataTime);
        values.put(ArchismatContract.FEATURED_PICK, uri.toString());

        context.getContentResolver().insert(ArchismatContract.CONTENT_URI, values);

    }

    private void  galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File( mCurrentFilePath );
        Uri fileUri = Uri.fromFile( file );
        mediaScanIntent.setData(fileUri);
        context.sendBroadcast(mediaScanIntent);
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
