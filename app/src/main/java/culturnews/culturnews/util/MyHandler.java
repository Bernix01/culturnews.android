package culturnews.culturnews.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.microsoft.windowsazure.notifications.NotificationsHandler;

import culturnews.culturnews.MainActivity;
import culturnews.culturnews.R;

/**
 * Created by Guillermo on 5/31/2015.
 */
public class MyHandler extends NotificationsHandler {
    public static final int NOTIFICATION_ID = 1;
    static public MainActivity mainActivity;
    NotificationCompat.Builder builder;
    Context ctx;
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String nhMessage = bundle.getString("message");

        sendNotification(nhMessage);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setVibrate(new long[]{500, 500, 500, 500, 500})
                        .setLights(Color.YELLOW, 1000, 1500)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("CulturNews")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
