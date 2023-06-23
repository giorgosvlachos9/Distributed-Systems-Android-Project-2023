package com.example.distsystemsproj2023;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.Manifest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.*;
import java.net.Socket;



public class MainActivity extends AppCompatActivity {

    private TextView welcomeTV, fileSelTV, totalResHelper;
    private Button choose_fileBTN, uploadBTN, goToTotalsBTN;

    private boolean check_totals = false;
    private String username;
    private Totals user_totals, server_totals;


    private static final int FILE_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ActivityResultLauncher<Intent> fileChooserLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTV = (TextView) findViewById(R.id.idTVWelcome);
        fileSelTV = (TextView) findViewById(R.id.idTVFileSelected);
        choose_fileBTN = (Button) findViewById(R.id.idBSelectFile);
        uploadBTN = (Button) findViewById(R.id.idBUpload);
        goToTotalsBTN = (Button) findViewById(R.id.idBTNGoToTotals);


        choose_fileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                    openFileChooser();
                } else {
                    requestPermission();
                }
            }
        });



        fileChooserLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleFileSelectionResult);

        goToTotalsBTN.setOnClickListener(v -> {
            if (!check_totals){
                Toast.makeText(MainActivity.this, "Have to upload file first", Toast.LENGTH_LONG).show();
            }else{
                Intent intent_tot = new Intent(MainActivity.this, Total_results.class);
                intent_tot.putExtra("Username", username);
                intent_tot.putExtra("User_totals", user_totals);
                intent_tot.putExtra("Server_totals", server_totals);

                startActivity(intent_tot);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            resetActivity();

            username = data.getStringExtra("Username");
            user_totals = (Totals) data.getSerializableExtra("User_totals");
            server_totals = (Totals) data.getSerializableExtra("Server_totals");
            check_totals = true;

        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fileChooserLauncher.launch(intent);
    }

    private void handleFileSelectionResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri fileUri = result.getData().getData();

            fileSelTV.setText("File Successfully selected");

            String file_contents = createFileString(fileUri);



            uploadBTN.setVisibility(View.VISIBLE);
            uploadBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AsyncTask<String,Void,AsyncTaskResult> myAsync =
                            new AsyncTask<String, Void, AsyncTaskResult>() {
                                @Override
                                protected void onPreExecute() {
                                    Toast.makeText(MainActivity.this, "Sending file", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                protected AsyncTaskResult doInBackground(String... strings) {
                                    try{

                                        Socket s = new Socket("192.168.56.1", 4320);
                                        /* Create the streams to send and receive data from server */
                                        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                                        ObjectInputStream in = new ObjectInputStream(s.getInputStream());

                                        String file_cont = strings[0];
                                        out.writeUTF("client");
                                        out.flush();
                                        out.writeUTF(file_cont);
                                        out.flush();
                                        // Initialization of the info to return
                                        String username, gpx_results, user_total_results, server_total_results;
                                        int total_user_files, total_gpxs;

                                        while(true) {

                                            username = in.readUTF();
                                            gpx_results = in.readUTF();
                                            user_total_results = in.readUTF();
                                            server_total_results = in.readUTF();
                                            total_user_files = in.readInt();
                                            total_gpxs = in.readInt();

                                            break;

                                        }

                                        AsyncTaskResult atr = new AsyncTaskResult(username, gpx_results, user_total_results,
                                                server_total_results, total_user_files, total_gpxs);

                                        return atr;

                                    }catch (Exception e){
                                        return new AsyncTaskResult();
                                    }

                                }

                                @Override
                                protected void onPostExecute(AsyncTaskResult as_task) {
                                    super.onPostExecute(as_task);

                                    Intent intent = new Intent(MainActivity.this, Activity2.class);
                                    intent.putExtra("Client_info", as_task);
                                    startActivityForResult(intent,1);


                                    //showNotification("File results are waiting for you!");

                                }
                            };
                    myAsync.execute(file_contents);

                }
            });

            Toast.makeText(this, "Selected file: " + fileUri.getPath(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                openFileChooser();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
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

    private void resetActivity(){
        fileSelTV.setText("No File Selected");
        uploadBTN.setVisibility(View.GONE);
    }



}