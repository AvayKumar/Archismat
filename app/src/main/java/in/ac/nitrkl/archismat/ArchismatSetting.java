package in.ac.nitrkl.archismat;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by avay on 8/9/15.
 */
public class ArchismatSetting extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.archismat_setting);
    }

}
