package com.example.distsystemsproj2023;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Total_results extends AppCompatActivity {
    private String username, user_total_res;
    private Totals user_totals, server_totals;
    private double user_total_time, server_total_time, user_total_distance, server_total_distance, user_total_up_ele, server_total_up_ele;
    private int total_files, total_user_gpxs;

    private TextView user_totalsTV, total_time_compTV, total_dist_compTV, total_upele_compTV;

    private BarChart time_bc, dist_bc, up_ele_bc;
    private BarData time_bar_data, dist_bar_data, up_ele_bar_data;
    private BarDataSet time_bar_dataSet, dist_bar_dataSet, up_ele_bar_dataSet;
    private ArrayList<BarEntry> time_barEntries, dist_barEntries, up_ele_barEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_results);

        initVariables();

        user_totalsTV = (TextView) findViewById(R.id.idTVTotalResults);
        total_time_compTV = (TextView) findViewById(R.id.idTVTotalTimeComp);
        total_dist_compTV = (TextView) findViewById(R.id.idTVTotalDistComp);
        total_upele_compTV = (TextView) findViewById(R.id.idTVTotalUpEleComp);

        time_bc = (BarChart) findViewById(R.id.idBCTotalTimeComp);
        dist_bc = (BarChart) findViewById(R.id.idBCTotalDistComp);
        up_ele_bc = (BarChart) findViewById(R.id.idBCTotalUpEleComp);

        user_totalsTV.setText("Username: "+username+"\n"+user_total_res);

        getAllEntries();

        // For the Time Bar Chart
        time_bar_dataSet = new BarDataSet(time_barEntries, "Total Time Set");
        time_bar_data = new BarData(time_bar_dataSet);

        time_bc.setData(time_bar_data);

        time_bar_dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        time_bar_dataSet.setValueTextColor(Color.BLACK);
        time_bar_dataSet.setValueTextSize(16f);

        // For the Dist Bar Chart
        dist_bar_dataSet = new BarDataSet(dist_barEntries, "Total Dist Set");
        dist_bar_data = new BarData(dist_bar_dataSet);

        dist_bc.setData(dist_bar_data);

        dist_bar_dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dist_bar_dataSet.setValueTextColor(Color.BLACK);
        dist_bar_dataSet.setValueTextSize(16f);

        // For the UpEle Bar Chart
        up_ele_bar_dataSet = new BarDataSet(up_ele_barEntries, "Total Up Elevation Set");
        up_ele_bar_data = new BarData(up_ele_bar_dataSet);

        up_ele_bc.setData(up_ele_bar_data);

        up_ele_bar_dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        up_ele_bar_dataSet.setValueTextColor(Color.BLACK);
        up_ele_bar_dataSet.setValueTextSize(16f);



    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        setResult(RESULT_OK);
        finish();
    }

    private void initVariables(){
        username = getIntent().getStringExtra("Username");
        user_totals = (Totals) getIntent().getSerializableExtra("User_totals");
        server_totals = (Totals) getIntent().getSerializableExtra("Server_totals");

        user_total_res = user_totals.getTotals_string();
        user_total_time = user_totals.getTotal_time();
        user_total_distance = user_totals.getTotal_dist();
        user_total_up_ele = user_totals.getTotal_upele();
        total_user_gpxs = user_totals.getNumber_of_files();


        server_total_time = server_totals.getTotal_time();
        server_total_distance = server_totals.getTotal_dist();
        server_total_up_ele = server_totals.getTotal_upele();
        total_files = server_totals.getNumber_of_files();
    }

    private void getAllEntries(){
        getTimeBarChartEntries();
        getDistBarChartEntries();
        getUpEleBarChartEntries();
    }

    private void getTimeBarChartEntries(){
        time_barEntries = new ArrayList<>();
        time_barEntries.add(new BarEntry(1f, (float) user_total_time));
        time_barEntries.add(new BarEntry(2f, (float)(server_total_time/total_files)));


    }

    private void getDistBarChartEntries(){
        dist_barEntries = new ArrayList<>();
        dist_barEntries.add(new BarEntry(1f, (float) user_total_distance));
        dist_barEntries.add(new BarEntry(2f, (float)(server_total_distance/total_files)));
    }

    private void getUpEleBarChartEntries(){
        up_ele_barEntries = new ArrayList<>();
        up_ele_barEntries.add(new BarEntry(1f, (float) user_total_up_ele));
        up_ele_barEntries.add(new BarEntry(2f, (float)(server_total_up_ele/total_files)));
    }

}