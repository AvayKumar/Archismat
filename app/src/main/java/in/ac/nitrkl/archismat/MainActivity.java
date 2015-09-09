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
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String LOG_TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.main_container, new MainFragment()).commit();

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

        if( checkConnection() ) {

            if (checkPlayServices()) {

                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setProgressStyle( ProgressDialog.STYLE_SPINNER );
                progressDialog.setMessage( getResources().getString(R.string.progress_dialog_message) );

                progressDialog.show();

                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }

        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( this );
            dialogBuilder.setTitle( R.string.alert_dialog_title)
                         .setMessage( R.string.alert_dialog_content  )
                         .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                             }
                         });
            dialogBuilder.create().show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(ArchismatPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent setting = new Intent(MainActivity.this, ArchismatSetting.class);
            startActivity(setting);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private boolean checkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return ( networkInfo != null && networkInfo.isConnected() );

    }

}
