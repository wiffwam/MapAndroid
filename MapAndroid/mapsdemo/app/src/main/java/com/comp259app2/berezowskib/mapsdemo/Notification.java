package com.comp259app2.berezowskib.mapsdemo;


import android.media.audiofx.AudioEffect;
import android.net.Uri;

import java.util.Date;
//Class is called notificaiton is because it is using information from a notification that was sent from Firebase

public class Notification {
    private int _id;
    private String lat;
    private String lon;
    private String description;
    private String date;

//Constructor for the Notification that will be sent from Firebase
    public Notification(int id, String Lat, String Lon, String Description,String DDate){
        _id = id;
        lat = Lat;
        lon = Lon;
        description = Description;
        date = DDate;

    }



    public int get_id() {
        return _id;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getDescription() {
        return description;
    }

    public String getDate(){return date;}

    @Override
    public String toString() {
        return String.format("(Lat)"+lat+" (Lon)"+lon+ " Desc:"+ description+"Date:"+ date);
    }
}
