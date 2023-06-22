package com.example.distsystemsproj2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Activity2 extends AppCompatActivity {

    private Button gpx_resultsBTN, total_resBTN;
    private String username, gpx_file_res, user_total_res, server_total_res;
    private Totals user_totals, server_totals;
    private double user_total_time, server_total_time, user_total_distance, server_total_distance, user_total_up_ele, server_total_up_ele;
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
        user_total_res = client_info.getUser_Total_res();
        server_total_res = client_info.getServer_total_res();
        total_user_files = client_info.getTotal_user_files();
        total_gpxs = client_info.getTotal_gpxs();
        // Parses the results of the string file
        stringParser(user_total_res, false);
        stringParser(server_total_res, true);

        showNotification("Got File Results!");

        // minutes  kms meters meters/minute



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

                Intent new_intent2 = new Intent(Activity2.this, Total_results.class);
                user_totals = new Totals(user_total_res, user_total_time, user_total_distance, user_total_up_ele, total_gpxs);
                server_totals = new Totals(server_total_res, server_total_time, server_total_distance, server_total_up_ele, total_user_files);
                new_intent2.putExtra("Username", username);
                // ----------------------------------------------------------
                new_intent2.putExtra("User_totals", user_totals);
                //----------------------------------------------------------
                new_intent2.putExtra("Server_totals", server_totals);

                startActivity(new_intent2);

            }
        });

    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();

        Intent new_int = new Intent();
        new_int.putExtra("Username", username);
        // ----------------------------------------------------------
        new_int.putExtra("User_totals", user_totals);
        //----------------------------------------------------------
        new_int.putExtra("Server_totals", server_totals);

        setResult(RESULT_OK, new_int);
        finish();
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

    // If server_checker id true then we initialize the total values of the server
    // else we initialize the total values of the user
    private void stringParser(String tot_res, boolean server_checker){
        BufferedReader reader = new BufferedReader(new StringReader(tot_res));
        try {
            String line = reader.readLine();
            if (line == null) return ;

            if (server_checker){
                String temp = "Total Time (in Minutes) : ";
                server_total_time = Double.parseDouble(line.substring(line.indexOf(temp)+temp.length()+1));
                line = reader.readLine();
                temp = "Total Distance (in Kilometers) :";
                server_total_distance = Double.parseDouble(line.substring(line.indexOf(temp)+temp.length()+1));
                line = reader.readLine();
                temp = "Total Elevation (in Meters) : ";
                server_total_up_ele = Double.parseDouble(line.substring(line.indexOf(temp)+temp.length()+1));
            }else{
                String temp = "Total Time (in Minutes) : ";
                user_total_time = Double.parseDouble(line.substring(line.indexOf(temp)+temp.length()+1));
                line = reader.readLine();
                temp = "Total Distance (in Kilometers) :";
                user_total_distance = Double.parseDouble(line.substring(line.indexOf(temp)+temp.length()+1));
                line = reader.readLine();
                temp = "Total Elevation (in Meters) : ";
                user_total_up_ele = Double.parseDouble(line.substring(line.indexOf(temp)+temp.length()+1));

            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}