package in.ac.nitrkl.archismat.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Created by avay on 19/8/15.
 */
public class ArchismatProvider extends ContentProvider {

    private ArchismatDBHealper mDbHealper;

    @Override
    public boolean onCreate() {
        mDbHealper = new ArchismatDBHealper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgument, String sortOrder) {


        Cursor cursor = mDbHealper.getReadableDatabase().query(
                ArchismatContract.TABLE_NAME,
                projection,
                selection,
                selectionArgument,
                null,
                null,
                sortOrder);

        Log.d("CONTENT_QUERY", cursor.getCount()+"");
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return ArchismatContract.CONTENT_TYPE;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = mDbHealper.getWritableDatabase().insert(ArchismatContract.TABLE_NAME, null, contentValues);
        return ArchismatContract.buildArchismatUri( id );
    }

    @Override
    public int delete(Uri uri, String sel, String[] sArgs) {

        String selection = ArchismatContract._ID + " = ? ";
        long id = ArchismatContract.getArchismatId(uri);

        return mDbHealper.getWritableDatabase().delete(ArchismatContract.TABLE_NAME, selection, new String[]{ Long.toString(id) });
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

}
