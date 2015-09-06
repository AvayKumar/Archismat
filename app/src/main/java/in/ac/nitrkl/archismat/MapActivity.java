package in.ac.nitrkl.archismat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by avay on 5/9/15.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double longitude, latitude;
    private String snippet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        longitude = data.getDouble(MainFragment.LONG);
        latitude = data.getDouble(MainFragment.LAT);
        snippet = data.getString(MainFragment.SNIPPET);

    }

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

        LatLng location = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions()
                .position( location )
                .title( "NIT Rourkela" )
                .snippet( snippet ));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraPosition cameraPosition = CameraPosition.builder().target(location).zoom(17).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setUpMapIfNeeded();
    }
}
