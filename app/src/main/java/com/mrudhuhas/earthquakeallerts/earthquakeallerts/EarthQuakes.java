package com.mrudhuhas.earthquakeallerts.earthquakeallerts;

public class EarthQuakes {
    String place;
    double magnitude;
    String date;
    String time;
    String url;

    public EarthQuakes(String place, double magnitude,String date ,String time,String url) {
        this.place = place;
        this.magnitude = magnitude;
        this.date = date;
        this.time = time;
        this.url = url;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
