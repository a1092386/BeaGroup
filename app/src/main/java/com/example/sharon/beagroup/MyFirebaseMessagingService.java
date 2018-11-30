package com.example.sharon.beagroup;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "測試Service";
    Context context;
    String token;
    public MyFirebaseMessagingService(Context ctx){
        context = ctx;
    }
    public MyFirebaseMessagingService(){
    }

    public String getToken(Context context){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        Log.w(TAG, "取得token  "+token);
                        SaveSharedPreference.setToken(context, token);
                    }
                });

        return token;
    }



    @Override
    public void onNewToken(String mToken) {
            super.onNewToken(mToken);
            Log.e("TOKEN",mToken);
            SaveSharedPreference.setToken(context, mToken);
    }

    //當APP在foreground時自動呼叫
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData().get("message"));
        Log.d(TAG, "Notification Click Action: " + remoteMessage.getNotification().getClickAction());

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();
        String flag = remoteMessage.getData().get("flag");
        String dataMessage = remoteMessage.getData().get("message");
        String dataFrom = remoteMessage.getData().get("from_user_id");
        String groupID = remoteMessage.getData().get("from_group_id");

        String channelId = "Default";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_noti_icon2)
                        .setColor(Color.parseColor("#55DDE0"))
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true);
        if(flag.equals("2")){
            click_action = ".FindFriends";
        }

        //Intent resultIntent = new Intent(this, NotificationActivity.class);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("message", dataMessage);
        resultIntent.putExtra("from_user_id", dataFrom);
        resultIntent.putExtra("from_group_id", groupID);




        // Because clicking the notification opens a new ("special") activity, there's no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = (int)System.currentTimeMillis();
        // Gets an instance of the NotificationManager service

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        // Builds the notification and issues it.
        manager.notify(mNotificationId, mBuilder.build());
    }


}