package com.example.distsystemsproj2023;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.Manifest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTV, fileSelTV, ttt;
    private Button choose_fileBTN, uploadBTN;


    private static final int FILE_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private ActivityResultLauncher<Intent> fileChooserLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTV = (TextView) findViewById(R.id.idTVWelcome);
        fileSelTV = (TextView) findViewById(R.id.idTVFileSelected);
        choose_fileBTN = (Button) findViewById(R.id.idBSelectFile);
        uploadBTN = (Button) findViewById(R.id.idBUpload);

        ttt = (TextView) findViewById(R.id.textView2);

        choose_fileBTN.setOnClickListener(v -> {
            if (checkPermission()) {
                openFileChooser();
            } else {
                requestPermission();
            }
        });

            fileChooserLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleFileSelectionResult);

    }


    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fileChooserLauncher.launch(intent);
    }

    private void handleFileSelectionResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri fileUri = result.getData().getData();

            //String temp = fileUri.getPath();
            //String filePath = temp.substring(temp.indexOf("Download/")+9);

            fileSelTV.setText(fileUri.getPath());
            //Toast.makeText(this, "Selected file: " + fileSelTV, Toast.LENGTH_SHORT).show();

            try {
                printRes(fileUri.getPath());
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }


    private void printRes(String path) throws IOException {
        System.out.println("A");
        FileReader txt = new FileReader(path);
        BufferedReader txt_handler = new BufferedReader(txt);

        String online;
        online = txt_handler.readLine();

        while ((online = txt_handler.readLine()) != null) {
            Log.d("Tag", "-------------------");
            Log.i("Tag", online);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}