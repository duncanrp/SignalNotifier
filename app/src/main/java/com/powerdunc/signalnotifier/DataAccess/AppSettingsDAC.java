package com.powerdunc.signalnotifier.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.powerdunc.signalnotifier.Models.AppSetting;
import com.powerdunc.signalnotifier.SignalNotifierApp;

public class AppSettingsDAC {

    public static boolean SaveSetting(Context context, AppSetting setting)
    {
        boolean success = false;
        long result = 0;

        SignalNotifierApp app =  ((SignalNotifierApp)context.getApplicationContext());
        SQLiteDatabase database = app.GetDatabase();

        if(setting.GetId() > 0)
        {
            //Update
            ContentValues values = new ContentValues();
            values.put("value", setting.GetValue());

            try {
               String id = String.valueOf(setting.GetId());

                result = database.update(setting.GetClassNameString(), values, "id=?", new String[]{id});
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else
        {
            //Create
            ContentValues values = new ContentValues();
            values.put("key", setting.GetKey());
            values.put("value", setting.GetValue());

            try {
                result = database.insert(setting.GetClassNameString(), null, values);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        success = result > 0;

        return success;
    }

    public static AppSetting GetSetting(Context context, String settingKey)
    {
        SignalNotifierApp app =  ((SignalNotifierApp)context.getApplicationContext());
        SQLiteDatabase database = app.GetDatabase();


        Cursor cursor = database.query(
                "AppSetting",
                new String[] {"id", "key", "value"},
                "key='" + settingKey + "'",
                null,
                null,
                null,
                "id DESC"
        );

        while(cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String key = cursor.getString(1);
            String value = cursor.getString(2);

            return new AppSetting(id, key, value);
        }


        return null;
    }
}
