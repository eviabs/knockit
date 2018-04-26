package com.knockit.android.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonIOException;
import com.knockit.android.App;
import com.knockit.android.R;
import com.knockit.android.activities.MainActivity;
import com.knockit.android.net.KnockitMessage;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    public static MainActivity activity;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("PVL", "State: " + App.isActivityVisible());

        KnockitMessage knockitMessage = KnockitMessage.getMessage(remoteMessageToJSON(remoteMessage));
        knockitMessage.setTime(remoteMessage.getSentTime());
        FirebaseHelper.addOutsideOfApp(FirebaseDatabase.getInstance().getReference(), knockitMessage);

        if (!App.isActivityVisible()) {
            sendNotification(knockitMessage);
        } else {
            if (activity != null) {
                activity.showFullScreenAlert(knockitMessage);
            }

        }
    }

    private String remoteMessageToJSON(RemoteMessage remoteMessage) {
        try {
            return (new JSONObject(remoteMessage.getData())).toString();
        } catch (JsonIOException e) {
            return null;
        }
    }

    private void sendNotification(KnockitMessage knockitMessage){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(KnockitMessage.MESSAGE_TYPE, knockitMessage.getMessageType());
        i.putExtra(KnockitMessage.MESSAGE_TIME, knockitMessage.getTime());
        PendingIntent pi = PendingIntent.getActivity(this,
                                                    0,
                                                    i,
                                                    PendingIntent.FLAG_ONE_SHOT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("tick tick")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("type: " + knockitMessage.getMessageType())
                .setContentText("time: " + knockitMessage.getTime())
//                .setContentInfo("Info")
                .setSound(sound)
                .setAutoCancel(true)
                .setContentIntent(pi);

        notificationManager.notify(1, notificationBuilder.build());

    }
}
