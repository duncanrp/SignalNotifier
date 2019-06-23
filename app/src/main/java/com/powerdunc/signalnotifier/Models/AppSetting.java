package com.powerdunc.signalnotifier.Models;

import android.content.Context;

import com.powerdunc.signalnotifier.Annotations.DatabaseAnnotations;
import com.powerdunc.signalnotifier.DataAccess.AppSettingsDAC;

public class AppSetting  extends DatabaseObject {

    //Model
    @DatabaseAnnotations.PrimaryKey
    @DatabaseAnnotations.AutoIncrement
    @DatabaseAnnotations.DBField
    private int id = -1;


    @DatabaseAnnotations.DBField
    private String key;


    @DatabaseAnnotations.DBField
    private String value;


    public AppSetting(int Id, String Key, String Value) {
        this.id = Id;
        this.key = Key;
        this.value = Value;
    }

    public AppSetting(String Key, Object value)
    {
       this.key = Key;
       this.value = String.valueOf(value);
    }

    public AppSetting(String Key, String Value) {
        this.key = Key;
        this.value = Value;
    }

    public AppSetting(String Key, int Value)
    {
        this.key = Key;
        this.value = String.valueOf(Value);
    }

    public int GetId() {
        return id;
    }

    public String GetKey() {
        return key;
    }

    public String GetValue() {
        return value;
    }

    public int GetValueInt()
    {
        return Integer.valueOf(value);
    }

    public boolean GetValueBool() { return Boolean.valueOf(value); }

    public void SetValue(String newValue)
    {
        this.value = newValue;
    }

    public void SetValue(int newValue)
    {
        this.value = String.valueOf(newValue);
    }

    public boolean Save(Context context)
    {
        boolean saved = AppSettingsDAC.SaveSetting(context, this);

        if(saved)
        {
            AppSetting setting = AppSettingsDAC.GetSetting(context, GetKey());

            this.id=setting.id;
        }

        return saved;
    }


    public static String GetClassNameString() {
        String className = AppSetting.class.getSimpleName();
        return className;
    }

}
