package com.example.distsystemsproj2023;

import java.io.Serializable;

public class Totals implements Serializable {
    private String totals_string;
    private double total_time, total_dist, total_upele;
    private int number_of_files;

    public Totals(String s, double t, double d, double e, int f){
        this.totals_string = s;
        this.total_time = t;
        this.total_dist = d;
        this.total_upele = e;
        this.number_of_files = f;
    }

    public String getTotals_string() {
        return totals_string;
    }

    public double getTotal_time() {
        return total_time;
    }

    public double getTotal_dist() {
        return total_dist;
    }

    public double getTotal_upele() {
        return total_upele;
    }

    public int getNumber_of_files() {
        return number_of_files;
    }
}
