package in.ac.nitrkl.archismat;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ApplicationTestCase;

import in.ac.nitrkl.archismat.data.ArchismatContract;
import in.ac.nitrkl.archismat.data.ArchismatDBHealper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    public void testCreateDB() throws Throwable{
        mContext.deleteDatabase(ArchismatDBHealper.DATABASE_NAME);
        SQLiteDatabase db = new ArchismatDBHealper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    private ContentValues getTableContentValues(){

        ContentValues values = new ContentValues();
        //values.put(ArchismatContract.UPDATE_TITLE, "Test title");
        values.put(ArchismatContract.DESCRIPTION, "I didn't wanted to but i did!!!");
        values.put(ArchismatContract.UPDATE_TYPE, 0);
        values.put(ArchismatContract.RECEIVE_TIME, "2015205");
        values.put(ArchismatContract.LOCATION_NAME, "Rourela");
        values.put(ArchismatContract.LOCATION_LONG, 53.625);
        values.put(ArchismatContract.LOCATION_LAT, 56.665);
        values.put(ArchismatContract.FEATURED_PICK, "hot_axxx.jpg");

        return values;
    }

    public void testInsertReadDb() {

        SQLiteDatabase db = new ArchismatDBHealper(mContext).getWritableDatabase();

        ContentValues values = getTableContentValues();

        long id = db.insert(ArchismatContract.TABLE_NAME, null, values);

        assertTrue( id != -1);

        Cursor cursor = db.query(
                ArchismatContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue( cursor.moveToFirst() );
    }

    /* Testing content provider */

    public void testDeleteAll() {

        mContext.getContentResolver().delete(ArchismatContract.CONTENT_URI, null, null);

        Cursor cursor = mContext.getContentResolver().query(ArchismatContract.CONTENT_URI, null, null, null, null);
        assertEquals(cursor.getCount(), 0);
        cursor.close();

    }

    public void testInsertReadProvider() {

        ContentValues values = getTableContentValues();
        Uri uri = mContext.getContentResolver().insert(ArchismatContract.CONTENT_URI, values);
        long id = ArchismatContract.getArchismatId(uri);
        assertTrue(id != -1);

        Cursor cursor = mContext.getContentResolver().query(ArchismatContract.CONTENT_URI, null,null, null, null);
        assertTrue(cursor.getCount() > 0);

    }

}