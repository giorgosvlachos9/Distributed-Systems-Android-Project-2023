package com.example.distsystemsproj2023;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class AsyncTaskResult implements Serializable {
    private String username, gpx_file_res, total_res, error;
    private int total_gpxs, total_user_files;


    public AsyncTaskResult(){ this.error = "Error"; }

    public AsyncTaskResult(String s1, String s2, String s3, int n1, int n2){
        this.username = s1;
        this.gpx_file_res = s2;
        this.total_res = s3;
        this.total_gpxs = n1;
        this.total_user_files = n2;
    }

    public String getUsername() { return username; }

    public String getError() { return error; }

    public int getTotal_gpxs() { return total_gpxs; }

    public int getTotal_user_files() { return total_user_files; }

    public String getGpx_file_res() {
        return gpx_file_res;
    }

    public String getTotal_res() {
        return total_res;
    }
}
