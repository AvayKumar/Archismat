package in.ac.nitrkl.archismat;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.nitrkl.archismat.util.Util;

/**
 * Created by avay on 4/9/15.
 */
public class ShareImageFragment extends Fragment {

    private static final String IMAGE_URI = "imageUri";
    private static final String IMAGE_DESC = "imageDescription";

    private ImageView mImageView;
    private TextView mTextView;

    public ShareImageFragment() {
        super();
    }

    public static ShareImageFragment getShareFragment(String imageUri, String desc) {

        ShareImageFragment shareImageFragment = new ShareImageFragment();

        Bundle data = new Bundle();
        data.putString(IMAGE_URI, imageUri);
        data.putString(IMAGE_DESC, desc);

        shareImageFragment.setArguments( data );
        return shareImageFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.image_share_fragment, container, false);
        mImageView = (ImageView) root.findViewById(R.id.ivShareImage);
        mTextView = (TextView) root.findViewById(R.id.tvShareText);

        Bundle data = getArguments();

        Uri uri = Uri.parse(data.getString(IMAGE_URI));
        String description = data.getString(IMAGE_DESC);
        String imageLocation = uri.getPath();
        Bitmap imageBitmap = Util.scaleImage(MainFragment.deviceWidth, imageLocation);
        mImageView.setImageBitmap( imageBitmap );
        mTextView.setText( description );

        return root;
    }


}
