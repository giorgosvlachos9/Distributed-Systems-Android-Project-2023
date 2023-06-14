package com.example.distsystemsproj2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Total_results extends AppCompatActivity {
    private String username, user_total_res;
    private double user_total_time, server_total_time, user_total_distance, server_total_distance, user_total_up_ele, server_total_up_ele;
    private int total_files, total_user_gpxs;

    private TextView user_totalsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_results);

        username = getIntent().getStringExtra("Username");
        Totals user_totals = (Totals) getIntent().getSerializableExtra("User_totals");
        Totals server_totals = (Totals) getIntent().getSerializableExtra("Server_totals");

        user_total_res = user_totals.getTotals_string();
        user_total_time = user_totals.getTotal_time();
        user_total_distance = user_totals.getTotal_dist();
        user_total_up_ele = user_totals.getTotal_upele();
        total_user_gpxs = user_totals.getNumber_of_files();

        server_total_time = server_totals.getTotal_time();
        server_total_distance = server_totals.getTotal_dist();
        server_total_up_ele = server_totals.getTotal_upele();
        total_files = server_totals.getNumber_of_files();



        user_totalsTV = (TextView) findViewById(R.id.idTVTotalResults);
        user_totalsTV.setText("Username: "+username+"\n"+user_total_res);
    }
}