package in.ac.nitrkl.archismat.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import in.ac.nitrkl.archismat.ArchismatPreferences;
import in.ac.nitrkl.archismat.R;

/**
 * Created by avay on 8/9/15.
 */
public class Util {

    public static final String BASE_URL = "http://archismat.in/";

    public static Bitmap scaleImage(Context context, String fileName) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( fileName, options );

        int imageWidth = options.outWidth;

        int deviceWidth = PreferenceManager.getDefaultSharedPreferences(context).getInt(ArchismatPreferences.DEVICE_WIDTH, 0);

        int scaleFactor = Math.max( 1, imageWidth/deviceWidth);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;


        return BitmapFactory.decodeFile(fileName, options);
    }

    public static Bitmap scaleDefaultImage(Context context) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), R.drawable.missing_image, options);

        int imageWidth = options.outWidth;
        int deviceWidth = PreferenceManager.getDefaultSharedPreferences(context).getInt(ArchismatPreferences.DEVICE_WIDTH, 0);
        int scaleFactor = Math.max( 1, imageWidth/deviceWidth);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        return BitmapFactory.decodeResource(context.getResources(), R.drawable.missing_image, options);
    }

}
