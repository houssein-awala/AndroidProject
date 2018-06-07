package com.master1.newsapplication.androidproject;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d("", "Message data payload: " + remoteMessage.getData());

            //if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
             //   scheduleJob();
         //   } else {
                // Handle message within 10 seconds
              //  handleNow();
           // }
            System.out.println(remoteMessage.getData().get("title"));
            startActivity(new Intent(this,MainActivity.class));

            System.out.println(remoteMessage.getMessageType());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.d("", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            System.out.println(remoteMessage.getNotification());
        }
    }
}
