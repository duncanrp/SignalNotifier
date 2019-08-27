package com.powerdunc.signalnotifier.Models;

import com.powerdunc.signalnotifier.Annotations.DatabaseAnnotations;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class LocationWithSignal extends DatabaseObject implements Serializable {


    //Model
    @DatabaseAnnotations.PrimaryKey
    @DatabaseAnnotations.AutoIncrement
    @DatabaseAnnotations.DBField
    private int id;


    @DatabaseAnnotations.DBField
    private int signalStrength;


    @DatabaseAnnotations.DBField
    private double latitude;


    @DatabaseAnnotations.DBField
    private double longitude;

    @DatabaseAnnotations.DBField
    private String createdDate;


    public LocationWithSignal(int id, double latitude, double longitude, int signalStrength, String createdDate) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.signalStrength = signalStrength;
        this.createdDate = createdDate;
    }

    public LocationWithSignal(double latitude, double longitude, int signalStrength, String createdDate) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.signalStrength = signalStrength;
        this.createdDate = createdDate;
    }

    public int GetId() {
        return id;
    }

    public double GetLatitide() { return latitude; }

    public double GetLongitude() { return longitude; }

    public int GetSignalStrength() {
        return signalStrength;
    }

    public String GetCreatedDateAsString()
    {
        return createdDate;
    }

    public String GetStrengthColor()
    {
        switch(signalStrength) {
            case 1:
                return "#e82b2b";
            case 2:
                return "#e8a72b";
            case 3:
                return "#e8dd2b";
            case 4:
                return "#88e82b";
            default:
                return "#000000";
        }
    }

    public Date GetCreatedDate() {

        Date dt = null;

        try {
            dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdDate);
        } catch (Exception ex)
        {

        }

        return dt;
    }


    public static String GetClassNameString() {
        String className  = LocationWithSignal.class.getSimpleName();
        return className;
    }
}
