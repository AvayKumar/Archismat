package in.ac.nitrkl.archismat;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.ac.nitrkl.archismat.custom.ArchismatCursorAdapter;
import in.ac.nitrkl.archismat.data.ArchismatContract;
import in.ac.nitrkl.archismat.data.ArchismatDBHealper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID = 315;
    private static final String LOG_TAG = "MainFragment";
    private ArchismatCursorAdapter mAdapter;
    private String mStartDate;

    public MainFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Bundle arg = getArguments();
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =inflater.inflate(R.layout.fragment_main, null);
        ListView listView = (ListView) rootView.findViewById(R.id.lvFeed);
        mAdapter = new ArchismatCursorAdapter(getActivity(), null, 0);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "SHORT CLICK " + i, Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd0000");
        String sd = format.format( new Date() );

        String selection = ArchismatContract.RECEIVE_TIME + " >= ? ";
        String[] selectionArgs = new String[]{sd};
        String sortOrder = ArchismatContract.RECEIVE_TIME + " DESC";

        return new CursorLoader(getActivity(), ArchismatContract.CONTENT_URI,  null, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if( data.getCount() > 0 ) {
            mAdapter.swapCursor(data);
        } else {
            Log.d(LOG_TAG, "CURSOR ZERO");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private ContentValues getAlertValues(String date){
        ContentValues values = new ContentValues();
        values.put(ArchismatContract.DESCRIPTION, "I didn't wanted to but i did!!!");
        values.put(ArchismatContract.UPDATE_TYPE, 0);
        values.put(ArchismatContract.RECEIVE_TIME, date);
        return values;
    }

    private ContentValues getEventValues(String date) {

        ContentValues values = new ContentValues();
        values.put(ArchismatContract.DESCRIPTION, "Event will be held in BBA at 9:30");
        values.put(ArchismatContract.UPDATE_TYPE, 1);
        values.put(ArchismatContract.RECEIVE_TIME, date);
        values.put(ArchismatContract.LOCATION_NAME, "Rourela");
        values.put(ArchismatContract.LOCATION_LONG, 53.625);
        values.put(ArchismatContract.LOCATION_LAT, 56.665);

        return values;
    }

    private ContentValues getImageValues(String date){

        ContentValues values = new ContentValues();
        values.put(ArchismatContract.UPDATE_TITLE, "Test title");
        values.put(ArchismatContract.DESCRIPTION, "Simply beautifull !!!");
        values.put(ArchismatContract.UPDATE_TYPE, 2);
        values.put(ArchismatContract.RECEIVE_TIME, date);
        values.put(ArchismatContract.FEATURED_PICK, "hot_axxx.jpg");

        return values;
    }

}
