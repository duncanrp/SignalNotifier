package com.powerdunc.signalnotifier.DataAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.powerdunc.signalnotifier.Models.AppSetting;
import com.powerdunc.signalnotifier.Models.DatabaseObject;
import com.powerdunc.signalnotifier.Models.StrengthMeasure;
import com.powerdunc.signalnotifier.Utils.DbUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Duncan on 07-Jan-19.
 */

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SignalStrength_DB";
    private static final int DATABASE_VERSION = 10;
    private Context context;

    private Class<? extends DatabaseObject>[] DatabaseObjects = new Class[] {
            StrengthMeasure.class,
            AppSetting.class
    };

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;

        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        for(Class<? extends DatabaseObject> dbObj : DatabaseObjects) {
            Object obj;
            String objName = "java.lang.Object";
            Field[] fields;
            Field tableNameField;
            String tableName;

            String tableCreateSql;


            tableName = dbObj.getSimpleName();
            fields = dbObj.getDeclaredFields();

            if(fields.length > 0) {
                tableCreateSql = DbUtils.GetTableCreateSql(tableName, fields);

                db.execSQL(tableCreateSql);

                Log.d("SignalStrengthDB", "Created Table '" + tableName + "'.");
            } else {
                Log.d("SignalStrengthDB", "No DBFields were found for Object(" + objName + ").");
            }


        }


        //Default Settings

        Set<String> defaultSettingKeys = Settings.Defaults.keySet();

        for(String key : defaultSettingKeys)
        {

            Object defaultValue = Settings.Defaults.get(key);

            AppSetting defaultSetting = new AppSetting(key, defaultValue);
            defaultSetting.Save(context);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        for(Class<? extends DatabaseObject> dbObj : DatabaseObjects) {

                String tableName = dbObj.getSimpleName();

                db.execSQL("DROP TABLE IF EXISTS " + tableName);
        }

        onCreate(db);
    }



}
