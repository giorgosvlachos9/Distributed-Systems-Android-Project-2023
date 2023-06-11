package com.example.distsystemsproj2023;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTV, fileSelTV;
    private Button choose_fileBTN, uploadBTN;

    private String file_contents;


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

            System.out.println("Gia na ddoyme");

            fileSelTV.setText("File Successfully selected");

            this.file_contents = createFileString(fileUri);
            System.out.println(file_contents);


            uploadBTN.setVisibility(View.VISIBLE);
            uploadBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, Activity2.class);
                    startActivity(intent);
                }
            });

            Toast.makeText(this, "Selected file: " + fileUri.getPath(), Toast.LENGTH_LONG).show();



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

    private String createFileString(Uri f_Uri){
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(f_Uri);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            br.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException thrown");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException thrown");
            e.printStackTrace();
        }


        return sb.toString();

    }

    private String generateRandomString(int l){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(l);

        for (int i = 0; i < l; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();

    }


}