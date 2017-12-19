package shop.plea.and.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Random;

import shop.plea.and.R;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.ui.activity.IntroActivity;

/**
 * Created by kwon on 2017-12-04.
 */

public class MyFcmListenerService extends FirebaseMessagingService{

    private String msg, title;
    private PushData pushData;

    private class PushData
    {
        String url = "";

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.log(Logger.LogState.E, "receive gcm message = " + remoteMessage.getData());


        String data = remoteMessage.getData().get("data");

        Logger.log(Logger.LogState.E, "receive gcm data = " + Utils.getStringByObject(data));
        if(!TextUtils.isEmpty(data))
        {
            msg = remoteMessage.getData().get("message");
            title = remoteMessage.getData().get("title");

            pushData = new Gson().fromJson(data, PushData.class);



            showNotification();


        }
    }

    private void showNotification()
    {
        try{

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;

            // notification pending setting
            Intent notificationIntent = new Intent(this, IntroActivity.class);

            notificationIntent.putExtra("key_1","value");
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification =  buildNotification(pendingIntent).build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;

            notificationManager.notify(m, notification);

        } catch (Exception ex)
        {
            Logger.log(Logger.LogState.E, "showNotification = " + ex.getMessage());

        }
    }

    protected NotificationCompat.Builder buildNotification(PendingIntent pIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.setSummaryText(getString(R.string.app_name));
            style.setBigContentTitle(getString(R.string.app_name));
            style.bigText(msg);

            builder.setStyle(style);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(getString(R.string.app_name));
            builder.setContentText(title);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        builder.setContentIntent(pIntent);

        return builder;
    }

    /*
    private RemoteViews getComplexNotificationView() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews notificationView = new RemoteViews(
                getPackageName(),
                R.layout.remote_view
        );

        notificationView.setImageViewResource(
                R.id.imagenotileft,
                R.drawable.ic_launcher);

        notificationView.setTextViewText(R.id.remote_title, getString(R.string.app_name));
        notificationView.setTextViewText(R.id.remote_text, msg);

        return notificationView;
    }
    */
}
