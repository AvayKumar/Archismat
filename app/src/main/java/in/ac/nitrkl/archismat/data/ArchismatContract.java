package in.ac.nitrkl.archismat.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by avay on 19/8/15.
 */
public class ArchismatContract implements BaseColumns {

    public static final String CONTENT_AUTHORITY = "in.ac.nitrkl.archismat";

    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String CONTENT_TYPE =   "vnd.android.cursor.dir/" + CONTENT_AUTHORITY;

    public static final String TABLE_NAME = "event_feed";

    public static final String DESCRIPTION = "description";

    public static final String UPDATE_TYPE = "update_type";

    public static final String RECEIVE_TIME = "notification_received";

    public static final String EVENT_NAME = "event_name";

    public static final String LOCATION_NAME = "location_name";

    public static final String LOCATION_LAT = "latitude";

    public static final String LOCATION_LONG = "longitude";

    public static final String FEATURED_PICK = "featured_pick";

    public static Uri buildArchismatUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static long getArchismatId(Uri uri) {
        return ContentUris.parseId(uri);
    }

    public static final String DATE_TIME = "datetime";

    public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String READABLE_DATE_FORMAT = "h:m a, dd MMM yyyy";

    public static String getDbDateString( Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
        return format.format( date );
    }

    public static Date getDateFromDb(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getReadableDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(READABLE_DATE_FORMAT);
        return dateFormat.format(date);
    }

}
