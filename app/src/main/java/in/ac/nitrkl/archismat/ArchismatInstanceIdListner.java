package in.ac.nitrkl.archismat;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


/**
 * Created by avay on 25/8/15.
 */
public class ArchismatInstanceIdListner extends InstanceIDListenerService{

    @Override
    public void onTokenRefresh() {
        Intent refreshToken = new Intent(this, RegistrationIntentService.class);
        startActivity(refreshToken);
    }
}
