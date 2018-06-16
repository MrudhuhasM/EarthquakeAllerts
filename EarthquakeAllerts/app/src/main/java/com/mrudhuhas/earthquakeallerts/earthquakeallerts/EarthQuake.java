package com.mrudhuhas.earthquakeallerts.earthquakeallerts;

public class EarthQuake {
    String place;
    String date;
    String time;
    double magnitude;
    String url;

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    String depth;

    public EarthQuake(String place, String date, String time, double magnitude, String url,String depth) {
        this.place = place;
        this.date = date;
        this.time = time;
        this.magnitude = magnitude;
        this.url = url;
        this.depth = depth;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
