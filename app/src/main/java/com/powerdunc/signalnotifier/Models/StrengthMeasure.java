package com.powerdunc.signalnotifier.Models;

import com.powerdunc.signalnotifier.Annotations.DatabaseAnnotations;

/**
 * Created by Duncan on 07-Jan-19.
 */

public class StrengthMeasure extends DatabaseObject {

    //Model
    @DatabaseAnnotations.PrimaryKey
    @DatabaseAnnotations.AutoIncrement
    @DatabaseAnnotations.DBField
    private int id;


    @DatabaseAnnotations.DBField
    private int barStrength;


    @DatabaseAnnotations.DBField
    private String createdDate;


    public StrengthMeasure(int barStrength, String createdDate) {
        this.barStrength = barStrength;
        this.createdDate = createdDate;
    }

    public int GetId() {
        return id;
    }

    public int GetBarStrength() {
        return barStrength;
    }

    public String GetCreatedDate() {
        return createdDate;
    }


    public static String GetClassNameString() {
        String className  = StrengthMeasure.class.getSimpleName();
        return className;
    }
}
