package in.ac.nitrkl.archismat.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by avay on 19/8/15.
 */
public class ArchismatDBHealper extends SQLiteOpenHelper {


    public static final int ARCH_ID = 0;
    public static final int ARCH_DESCRIPTION = 1;
    public static final int ARCH_UPDATE_TYPE = 2;
    public static final int ARCH_RECEIVE_TIME = 3;
    public static final int ARCH_EVENT_NAME = 4;
    public static final int ARCH_LOCATION = 5;
    public static final int ARCH_LONG = 6;
    public static final int ARCH_LAT = 7;
    public static final int ARCH_PICK_URI = 8;



    public static final String DATABASE_NAME = "archimsat.db";
    public static final int DATABASE_VERSION = 1;

    public ArchismatDBHealper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ArchismatDBHealper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_TABLE =
                "CREATE TABLE " + ArchismatContract.TABLE_NAME + " (" +
                        ArchismatContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ArchismatContract.DESCRIPTION + " TEXT, " +
                        ArchismatContract.UPDATE_TYPE + " INTEGER, " +
                        ArchismatContract.RECEIVE_TIME + " TEXT, " +
                        ArchismatContract.EVENT_NAME + " TEXT, " +
                        ArchismatContract.LOCATION_NAME + " TEXT, " +
                        ArchismatContract.LOCATION_LONG + " REAL, " +
                        ArchismatContract.LOCATION_LAT + " REAL, " +
                        ArchismatContract.FEATURED_PICK + " TEXT);";

            sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + ArchismatContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
