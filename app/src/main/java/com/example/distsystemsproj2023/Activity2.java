package com.example.distsystemsproj2023;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Activity2 extends AppCompatActivity {

    private Button gpx_resultsBTN, total_resBTN;
    private String username, gpx_file_res, total_res;
    private int total_gpxs, total_user_files;
    private AsyncTaskResult client_info;
    private static final String CHANNEL_ID = "my_noti_channel_id";
    private static final String CHANNEL_NAME = "Noti_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        gpx_resultsBTN = (Button) findViewById(R.id.idBTNGpxResults);
        total_resBTN = (Button) findViewById(R.id.idBTNTotalResults);

        // Getting the results from Master
        client_info = (AsyncTaskResult) getIntent().getSerializableExtra("Client_info");
        username = client_info.getUsername();
        gpx_file_res = client_info.getGpx_file_res();
        total_res = client_info.getTotal_res();
        total_user_files = client_info.getTotal_user_files();
        total_gpxs = client_info.getTotal_gpxs();

        showNotification("Got File Results!");



        gpx_resultsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_intent1 = new Intent(Activity2.this, GPX_results.class);
                new_intent1.putExtra("Username", username);
                new_intent1.putExtra("GPX_res", gpx_file_res);

                startActivity(new_intent1);
            }
        });

        total_resBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_intent2 = new Intent(Activity2.this, GPX_results.class);
                new_intent2.putExtra("Username", username);
                new_intent2.putExtra("Total_res", total_res);
                new_intent2.putExtra("Total_user_gpxs", total_gpxs);
                new_intent2.putExtra("Total_user_files", total_user_files);

                startActivity(new_intent2);
            }
        });

    }

    private void showNotification(String msg){
        NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My Notification Channel");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            notiManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Ready")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notiManager.notify(0, notiBuilder.build());

    }


    /*private void showNotification(String msg) {
        // Create a notification manager
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My Notification Channel");
            channel.enableLights(true);
            channel.setLightColor(Color.CYAN);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, Activity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create the notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setStyle(new NotificationCompat.BigPictureStyle())
                        .setContentTitle("My Notification")
                        .setContentText(msg)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVibrate(new long[]{100, 1000, 200, 340})
                        .setAutoCancel(false)
                        .setTicker("Notification")
                        .setFullScreenIntent(pendingIntent, true);

        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat nmc = NotificationManagerCompat.from(getApplicationContext());
        // Show the notification
        nmc.notify(1, builder.build());
    }*/

}