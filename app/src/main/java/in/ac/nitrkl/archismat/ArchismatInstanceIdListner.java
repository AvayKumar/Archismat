package in.ac.nitrkl.archismat;

import android.content.Intent;
import android.preference.PreferenceManager;

import com.google.android.gms.iid.InstanceIDListenerService;


/**
 * Created by avay on 25/8/15.
 */
public class ArchismatInstanceIdListner extends InstanceIDListenerService{

    @Override
    public void onTokenRefresh() {

        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(ArchismatPreferences.TOKEN_REFRESHED, true).apply();
        Intent refreshToken = new Intent(this, RegistrationIntentService.class);
        startActivity(refreshToken);

    }
}
