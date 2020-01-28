package com.example.sharemap2.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LocationData {

    public static final String FIELD_DESTINATION = "destination";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_LATITUDE = "latitude";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_ACCURACY = "accuracy";
    public static final String FIELD_CREATE_AT = "created_at";


    public String title;
    public double latitude;
    public double longitude;
    public double accuracy;
    public String created_at;

    public LocationData() {}

    public LocationData(String tag, double latitude, double longitude,double accuracy, String created_at) {
        this.title = tag;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.created_at = created_at;
    }

    public String getTag() {
        return title;
    }

    public void setTag(String tag) {
        this.title = tag;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double Longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

