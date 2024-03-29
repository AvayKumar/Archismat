package in.ac.nitrkl.archismat.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import in.ac.nitrkl.archismat.R;
import in.ac.nitrkl.archismat.data.ArchismatContract;
import in.ac.nitrkl.archismat.data.ArchismatDBHealper;

/**
 * Created by avay on 27/8/15.
 */
public class ArchismatCursorAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 3;
    private static final int VIEW_TYPE_ALERT = 0;
    private static final int VIEW_TYPE_EVENT = 1;
    private static final int VIEW_TYPE_PICTURE = 2;


    public ArchismatCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int itemType = cursor.getInt(ArchismatDBHealper.ARCH_UPDATE_TYPE);
        View listItem;

        switch ( itemType ) {
            case VIEW_TYPE_ALERT:
                listItem = LayoutInflater.from(context).inflate(R.layout.alert_list_item, parent, false);
                ViewHolderAlert holderAlert = new ViewHolderAlert(listItem);
                listItem.setTag(holderAlert);
                break;

            case VIEW_TYPE_EVENT:
                listItem = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
                ViewHolderEvent holderEvent = new ViewHolderEvent(listItem);
                listItem.setTag(holderEvent);
                break;

            case VIEW_TYPE_PICTURE:
                listItem = LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false);
                ViewHolderImage holderImage = new ViewHolderImage(listItem);
                listItem.setTag(holderImage);
                break;

            default:
                throw new UnsupportedOperationException();
        }

        return listItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        int itemType = cursor.getInt(ArchismatDBHealper.ARCH_UPDATE_TYPE);

        Date dbDate = ArchismatContract.getDateFromDb( cursor.getString(ArchismatDBHealper.ARCH_RECEIVE_TIME) );
        String readAbleDate =   ArchismatContract.getReadableDate(dbDate);

        switch ( itemType ) {
            case VIEW_TYPE_ALERT:
                ViewHolderAlert holderAlert = (ViewHolderAlert) view.getTag();
                holderAlert.receiveTime.setText( readAbleDate );
                holderAlert.message.setText( cursor.getString( ArchismatDBHealper.ARCH_DESCRIPTION ) );
                break;

            case VIEW_TYPE_EVENT:
                ViewHolderEvent holderEvent = (ViewHolderEvent) view.getTag();
                holderEvent.location.setText( cursor.getString( ArchismatDBHealper.ARCH_LOCATION ) );
                holderEvent.receiveTime.setText( readAbleDate );
                holderEvent.description.setText( cursor.getString( ArchismatDBHealper.ARCH_DESCRIPTION ) );
                holderEvent.eventName.setText( cursor.getString( ArchismatDBHealper.ARCH_EVENT_NAME ) );
                break;

            case VIEW_TYPE_PICTURE:
                ViewHolderImage holderImage = (ViewHolderImage) view.getTag();
                holderImage.receiveTime.setText( readAbleDate );
                holderImage.description.setText(cursor.getString(ArchismatDBHealper.ARCH_DESCRIPTION));
                final Uri uri = Uri.parse(cursor.getString(ArchismatDBHealper.ARCH_PICK_URI));
                String imageLocation = uri.getPath();
                Bitmap imageBitmap = Util.scaleImage(context, imageLocation);
                if( imageBitmap == null) {
                    imageBitmap = Util.scaleDefaultImage(context);
                }
                holderImage.imageUpdate.setImageBitmap( imageBitmap );
                break;

            default:
                throw new UnsupportedOperationException();
        }

    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return cursor.getInt(ArchismatDBHealper.ARCH_UPDATE_TYPE);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    public static class ViewHolderAlert {
        public final TextView receiveTime;
        public final TextView message;

        public ViewHolderAlert(View view) {
            receiveTime = (TextView) view.findViewById(R.id.tvAlertTime);
            message = (TextView) view.findViewById(R.id.tvAlertMessage);
        }
    }

    public static class ViewHolderImage {
        public final TextView receiveTime;
        public final ImageView imageUpdate;
        public final TextView description;

        public ViewHolderImage(View view) {
            receiveTime = (TextView) view.findViewById(R.id.tvImageTime);
            imageUpdate = (ImageView) view.findViewById(R.id.ivImage);
            description = (TextView) view.findViewById(R.id.tvImageDescription);
        }
    }

    public static class ViewHolderEvent {
        public final TextView receiveTime;
        public final TextView description;
        public final TextView location;
        public final TextView eventName;

        public ViewHolderEvent(View view) {
            receiveTime = (TextView) view.findViewById(R.id.tvEventTime);
            description = (TextView) view.findViewById(R.id.tvEventDescription);
            location = (TextView) view.findViewById(R.id.tvEventLocation);
            eventName = (TextView) view.findViewById(R.id.tvEventName);
        }
    }



}
