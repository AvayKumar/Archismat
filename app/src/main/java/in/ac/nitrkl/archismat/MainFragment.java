package in.ac.nitrkl.archismat;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import in.ac.nitrkl.archismat.data.ArchismatContract;
import in.ac.nitrkl.archismat.data.ArchismatDBHealper;
import in.ac.nitrkl.archismat.util.ArchismatCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 315;
    private static final String LOG_TAG = "MainFragment";
    private ArchismatCursorAdapter mAdapter;
    private int mQueryLimit = 10;
    private int preLast;

    public MainFragment() {
        setHasOptionsMenu(true);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
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
                            ((Callback) getActivity()).onClickAlert(cursor.getString(ArchismatDBHealper.ARCH_DESCRIPTION));
                            break;
                        case 1:
                            ((Callback) getActivity()).onClickEvent(
                                    cursor.getString(ArchismatDBHealper.ARCH_LOCATION),
                                    cursor.getDouble(ArchismatDBHealper.ARCH_LONG),
                                    cursor.getDouble(ArchismatDBHealper.ARCH_LAT)
                            );
                            break;
                        case 2:
                            ((Callback) getActivity()).onClickPick(
                                    cursor.getString(ArchismatDBHealper.ARCH_PICK_URI),
                                    cursor.getString(ArchismatDBHealper.ARCH_DESCRIPTION)
                            );
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

                final int lastItem = firstVisibleItem + visibleItemCount;
                if( lastItem == totalItemCount && preLast!=lastItem ) {
                    //to avoid multiple calls for last item
                    preLast = lastItem;
                    mQueryLimit += 10;
                    getLoaderManager().restartLoader(LOADER_ID, null, MainFragment.this);
                }

            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = ArchismatContract.RECEIVE_TIME + " DESC LIMIT " + mQueryLimit;
        return new CursorLoader(getActivity(), ArchismatContract.CONTENT_URI,  null, null, null, sortOrder);
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

    public interface Callback {
        void onClickAlert(String message );
        void onClickEvent(String location, double longitude, double latitude);
        void onClickPick(String uri, String desc);
    }

}
