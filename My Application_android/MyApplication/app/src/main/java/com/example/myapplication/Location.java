package com.example.myapplication;

public class Location {
    private double dbLati, dblongi;
    private String strChecked;

    public void setDbLati(double dbLati) {
        this.dbLati = dbLati;
    }

    public void setDblongi(double dblongi) {
        this.dblongi = dblongi;
    }

    public void setStrChecked(String strChecked) {
        this.strChecked = strChecked;
    }

    public double getDbLati() {
        return dbLati;
    }

    public double getDblongi() {
        return dblongi;
    }

    public String getStrChecked() {
        return strChecked;
    }
}
