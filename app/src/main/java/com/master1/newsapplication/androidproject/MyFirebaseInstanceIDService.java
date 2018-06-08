package com.master1.newsapplication.androidproject;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    public void  sendRegistrationToServer(final String token)
    {
        System.out.println("new token "+token);
        final SharedPreferences preferences= getSharedPreferences("token",MODE_PRIVATE);
        final String old=preferences.getString("token","");
        new Thread() {
            String adress = "http://news-project.000webhostapp.com/editToken.php?newToken="
                    +token
                    +" oldToken="+old;

            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(adress);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    preferences.edit().putString("token",token).commit();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
