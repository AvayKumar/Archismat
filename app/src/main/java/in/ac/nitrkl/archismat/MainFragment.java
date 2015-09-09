package in.ac.nitrkl.archismat;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

import in.ac.nitrkl.archismat.util.ArchismatCursorAdapter;
import in.ac.nitrkl.archismat.data.ArchismatContract;
import in.ac.nitrkl.archismat.data.ArchismatDBHealper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 315;
    private static final String LOG_TAG = "MainFragment";
    private ArchismatCursorAdapter mAdapter;
    private String mStartDate;

    public static final String LONG = "longitude";
    public static final String LAT = "latitude";
    public static final String LOCATION = "location";


    public static int deviceWidth;

    public MainFragment() {
        setHasOptionsMenu(true);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Bundle arg = getArguments();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( metrics );
        deviceWidth = metrics.widthPixels;
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( metrics );
        deviceWidth = metrics.widthPixels;
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.lvFeed);
        mAdapter = new ArchismatCursorAdapter(getActivity(), null, 0);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor = mAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(i)) {

                    int type = cursor.getInt(ArchismatDBHealper.ARCH_UPDATE_TYPE);

                    switch (type) {
                        case 0:
                            AlertFragment alertFragment = AlertFragment.getAlertFragment( cursor.getString( ArchismatDBHealper.ARCH_DESCRIPTION ) );

                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.pop_in, R.anim.pop_out)
                                    .replace(R.id.main_container, alertFragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        case 1:
                            double latitude = cursor.getDouble(ArchismatDBHealper.ARCH_LAT);
                            double longitude = cursor.getDouble(ArchismatDBHealper.ARCH_LONG);
                            String locatoin = cursor.getString(ArchismatDBHealper.ARCH_LOCATION);

                            Bundle data = new Bundle();
                            data.putDouble(LONG, longitude);
                            data.putDouble(LAT, latitude);
                            data.putString(LOCATION, locatoin);

                            Intent mapIntent = new Intent(getActivity(), MapActivity.class);
                            mapIntent.putExtras(data);
                            startActivity(mapIntent);
                            break;
                        case 2:

                            ShareImageFragment shareImage = ShareImageFragment.getShareFragment(
                                    cursor.getString( ArchismatDBHealper.ARCH_PICK_URI ),
                                    cursor.getString( ArchismatDBHealper.ARCH_DESCRIPTION )
                            );

                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.pop_in, R.anim.pop_out)
                                    .replace(R.id.main_container, shareImage)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }

                }
            }
        });

        registerForContextMenu(listView);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd000000");
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Cursor data = mAdapter.getCursor();

        switch ( item.getItemId() ) {
            case R.id.action_delete:
                if( data.moveToPosition( info.position ) ) {
                    Uri idUri = ArchismatContract.buildArchismatUri( data.getLong(ArchismatDBHealper.ARCH_ID) );
                    if( getActivity().getContentResolver().delete( idUri, null, null) > 0) {
                        Toast.makeText(getActivity(), getString(R.string.delete_success), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.delete_fail), Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
