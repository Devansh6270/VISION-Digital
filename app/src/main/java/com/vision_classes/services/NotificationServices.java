package com.vision_classes.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.vision_classes.R;
import com.vision_classes.activities.SplashScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationServices extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.e("Some data", "" + remoteMessage.getData());
//        Log.e("Notification",remoteMessage.getNotification().getTitle());

//        String title = remoteMessage.getData().getTitle();
//        String message = remoteMessage.getNotification().getBody();
//
//        Log.e("Title",title);
//        Log.e("message",message);
        String clickAction = remoteMessage.getNotification().getClickAction();
        sendNotification("Hello", "message", clickAction);
    }

    private void sendNotification(String title, String message, String clickAction) {


        Intent intent;
        try {
            intent = new Intent(clickAction);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } catch (Exception e) {
            intent = new Intent(this, SplashScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
