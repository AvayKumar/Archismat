package in.ac.nitrkl.archismat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by avay on 8/9/15.
 */
public class Util {

    public static Bitmap scaleImage(int ivWidth, String fileName) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( fileName, options );

        int imageWidth = options.outWidth;

        int scaleFactor = Math.max( 1, imageWidth/ivWidth);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;


        return BitmapFactory.decodeFile(fileName, options);
    }

}
