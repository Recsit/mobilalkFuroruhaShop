package com.example.furdoruhawebshop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationHandler {
    private static final String CHANNEL_ID = "swimsuit_notification_channel";

    private NotificationManager manager;
    private Context context;

    public NotificationHandler(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);


    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"swimsuit notification",NotificationManager.IMPORTANCE_DEFAULT);
    }
}
