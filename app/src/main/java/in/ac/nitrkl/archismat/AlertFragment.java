package in.ac.nitrkl.archismat;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.nitrkl.archismat.util.Util;

/**
 * Created by avay on 8/9/15.
 */
public class AlertFragment extends Fragment{

    private static final String ALERT_MESSAGE = "alert_message";

    private TextView mTextView;

    public AlertFragment() {
    }

    public static AlertFragment getAlertFragment(String message) {

        AlertFragment alertFragment = new AlertFragment();
        Log.d("ALERT_FRAGMENT", message);
        Bundle data = new Bundle();
        data.putString(ALERT_MESSAGE, message);

        alertFragment.setArguments(data);
        return alertFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.alert_fragment, container, false);
        mTextView = (TextView) root.findViewById(R.id.alert_message);

        Bundle data = getArguments();
        mTextView.setText( data.getString(ALERT_MESSAGE));

        return root;
    }

}
