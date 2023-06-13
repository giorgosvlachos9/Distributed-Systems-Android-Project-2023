package com.example.distsystemsproj2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Total_results extends AppCompatActivity {
    private String username, total_res;
    private int total_user_files, total_gpxs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_results);

        username = getIntent().getStringExtra("Username");
        total_res = getIntent().getStringExtra("Total_res");
        total_gpxs = getIntent().getIntExtra("Total_user_gpxs", 0);
        total_user_files = getIntent().getIntExtra("Total_user_files", 0);
    }
}