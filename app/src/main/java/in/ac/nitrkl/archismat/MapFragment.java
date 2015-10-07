package in.ac.nitrkl.archismat;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by avay on 12/9/15.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback{

    public static final String LOCATION_NAME = "locationName";
    public static final String LOCATION_LONG = "longitude";
    public static final String LOCATION_LAT = "latitude";

    public MapFragment() {}


    public static MapFragment getMapFragment( Bundle data) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(data);
        return mapFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Bundle data = getArguments();

        LatLng location = new LatLng( data.getDouble(LOCATION_LAT), data.getDouble(LOCATION_LONG));

        String mapType = PreferenceManager
                    .getDefaultSharedPreferences(getActivity())
                    .getString(getString(R.string.SETTING_MAP_TYPE_KEY), "1");

        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions()
                .position( location )
                .title( "NIT Rourkela" )
                .snippet( data.getString(LOCATION_NAME) ) );
        googleMap.setMapType( Integer.parseInt( mapType ) );
        CameraPosition cameraPosition = CameraPosition.builder().target(location).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }
}
