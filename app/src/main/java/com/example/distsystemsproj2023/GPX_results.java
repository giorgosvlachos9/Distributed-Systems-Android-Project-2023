package com.example.distsystemsproj2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GPX_results extends AppCompatActivity {
    private TextView gpx_resTV;
    private String username, gpx_results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpx_results);

        username = getIntent().getStringExtra("Username");
        gpx_results = getIntent().getStringExtra("GPX_res");

        gpx_resTV = (TextView) findViewById(R.id.idTVGpxRes);

        gpx_resTV.setText("Username: "+username+"\n"+gpx_results);

    }
}