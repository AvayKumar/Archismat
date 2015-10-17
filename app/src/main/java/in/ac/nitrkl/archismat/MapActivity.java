package in.ac.nitrkl.archismat;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String LOCATION_NAME = "locationName";
    public static final String LOCATION_LONG = "longitude";
    public static final String LOCATION_LAT = "latitude";
    private GoogleMap mMap;
    private Bundle mDSata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent dataIntent = getIntent();
        mDSata = dataIntent.getExtras();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        LatLng location = new LatLng( mDSata.getDouble(LOCATION_LAT), mDSata.getDouble(LOCATION_LONG));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rourkela, 13));
        String mapType = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString(getString(R.string.SETTING_MAP_TYPE_KEY), "1");
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("NIT Rourkela")
                .snippet(mDSata.getString(LOCATION_NAME)));
        mMap.setMapType( Integer.parseInt( mapType ) );
        CameraPosition cameraPosition = CameraPosition.builder().target(location).zoom(14).build();
        mMap.animateCamera( CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setUpMapIfNeeded();
    }
}
