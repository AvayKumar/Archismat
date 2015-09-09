package in.ac.nitrkl.archismat.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import in.ac.nitrkl.archismat.MainActivity;
import in.ac.nitrkl.archismat.R;

/**
 * Created by avay on 8/9/15.
 */
public class Notification {

    private static final int NOTIFICATION_ID = 315;

    public static void sendAlertNotification(Context context, String title, String content) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle( title )
                .setContentText( content )
                .setSound( defaultSoundUri )
                .setPriority(android.app.Notification.PRIORITY_HIGH )
                .setAutoCancel(true)
                .setContentIntent( pendingIntent );

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build() );

    }

    public static void sendPictureNotification(Context context, String title, String content, Bitmap bitmap) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build() );

    }

    public static void sendEventNotification (Context context, String title, String location) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle( title )
                .setContentText( location )
                .setSound( defaultSoundUri )
                .setAutoCancel(true)
                .setContentIntent( pendingIntent );

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build() );


    }

}
