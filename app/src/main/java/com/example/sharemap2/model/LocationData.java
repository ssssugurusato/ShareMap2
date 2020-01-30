package com.example.sharemap2.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LocationData {

    public static final String FIELD_DESTINATION = "destination";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_LATLNG = "latlng";
    public static final String FIELD_ACCURACY = "accuracy";
    public static final String FIELD_CREATE_AT = "created_at";
    public static final String FIELD_UID = "uid";


    public String title;
    public LatLng latlng;
    public double accuracy;
    public String created_at;
    public String uid;

    public LocationData() {}

    public LocationData(String title, LatLng latlng, double accuracy, String created_at, String uid) {
        this.title = title;
        this.latlng = latlng;
        this.accuracy = accuracy;
        this.created_at = created_at;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

