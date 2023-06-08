package com.example.distsystemsproj2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    private TextView welcomeTV, fileSelTV;
    private Button choose_fileBTN, uploadBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTV = (TextView) findViewById(R.id.idTVWelcome);
        fileSelTV = (TextView) findViewById(R.id.idTVFileSelected);
        choose_fileBTN = (Button) findViewById(R.id.idBSelectFile);
        uploadBTN = (Button) findViewById(R.id.idBUpload);

        choose_fileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    System.out.println("PermNotGranted");
                }
                else{
                    System.out.println("Granted");
                }
            }
        });
    }
}