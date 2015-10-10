package in.ac.nitrkl.archismat.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import in.ac.nitrkl.archismat.R;

/**
 * Created by avay on 8/9/15.
 */
public class Util {

    public static final String BASE_URL = "http://archismat.in/";
    public static final String IMAGE_UPLOAD_URL = BASE_URL + "uploads/files/";

    public static Bitmap scaleImage(int deviceWidth, String fileName) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( fileName, options );

        int imageWidth = options.outWidth;

        int scaleFactor = Math.max( 1, imageWidth/deviceWidth);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;


        return BitmapFactory.decodeFile(fileName, options);
    }

    public static Bitmap scaleDefaultImage(Context context, int deviceWidth) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), R.drawable.missing_image, options);

        int imageWidth = options.outWidth;

        int scaleFactor = Math.max( 1, imageWidth/deviceWidth);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        return BitmapFactory.decodeResource(context.getResources(), R.drawable.missing_image, options);
    }

}
