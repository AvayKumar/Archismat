package in.ac.nitrkl.archismat;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import in.ac.nitrkl.archismat.util.Notification;


public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String LOG_TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressDialog progressDialog;
    private String locationName;
    private double longitude, latitude;

    public static boolean isInForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Notification.notificationCount = 0;

        if( savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, new MainFragment()).commit();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if( preferences != null && !preferences.contains(ArchismatPreferences.DEVICE_WIDTH) ) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics( metrics );
            preferences.edit().putInt(ArchismatPreferences.DEVICE_WIDTH, metrics.widthPixels).apply();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

                boolean sentToken = preferences.getBoolean(ArchismatPreferences.SENT_TOKEN_TO_SERVER, false);

                if( sentToken ) {
                    progressDialog.hide();
                    Log.d(LOG_TAG, "Token received successfully");

                } else {
                    progressDialog.hide();
                    Log.d(LOG_TAG, "Error occurred while retrieve Token");

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( MainActivity.this );
                    dialogBuilder.setTitle( R.string.token_error_title)
                            .setMessage( R.string.token_error_message  )
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    dialogBuilder.create().show();

                }

            }
        };

        validateConnection();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isInForeground = true;
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(ArchismatPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        isInForeground = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( progressDialog != null )
            progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent setting = new Intent(MainActivity.this, ArchismatSetting.class);
            startActivity(setting);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean checkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if( networkInfo != null && networkInfo.isConnected() ) {
            return true;
        } else {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( this );
            dialogBuilder.setTitle( R.string.alert_dialog_title)
                    .setMessage(R.string.alert_dialog_content  )
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            validateConnection();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            dialogBuilder.create().show();

            return false;
        }

    }

    /*
    Check if GooglePlay service is available on the device
    * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void validateConnection() {
        if (checkPlayServices() && checkConnection() ) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle( ProgressDialog.STYLE_SPINNER );
            progressDialog.setMessage( getResources().getString(R.string.progress_dialog_message) );

            progressDialog.show();

            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

        }
    }

    @Override
    public void onClickAlert(String message) {

        AlertFragment alertFragment = AlertFragment.getAlertFragment( message );

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.pop_in, R.anim.pop_out)
                .replace(R.id.main_container, alertFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onClickEvent(String location, double longitude, double latitude) {

        Bundle data = new Bundle();

        data.putString(MapActivity.LOCATION_NAME, location);
        data.putDouble(MapActivity.LOCATION_LONG, longitude);
        data.putDouble(MapActivity.LOCATION_LAT, latitude);

        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.putExtras( data );
        startActivity(mapIntent);

    }

    @Override
    public void onClickPick(String uri, String desc) {

        ShareImageFragment shareImage = ShareImageFragment.getShareFragment(uri, desc);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.pop_in, R.anim.pop_out)
                .replace(R.id.main_container, shareImage)
                .addToBackStack(null)
                .commit();
    }

}
