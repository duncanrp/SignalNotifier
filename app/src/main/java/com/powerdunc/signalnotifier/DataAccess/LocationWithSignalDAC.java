package com.powerdunc.signalnotifier.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.powerdunc.signalnotifier.Models.LocationWithSignal;
import com.powerdunc.signalnotifier.Models.StrengthMeasure;
import com.powerdunc.signalnotifier.SignalNotifierApp;

import java.util.ArrayList;
import java.util.List;

public class LocationWithSignalDAC {


    public static ArrayList<LocationWithSignal> GetRemindersList(Context context) {

        ArrayList<LocationWithSignal> locationsWithSignalList = new ArrayList<>();

        String selection = null;
        String[] selectionArgs = null;

        SignalNotifierApp app =  ((SignalNotifierApp)context.getApplicationContext());
        SQLiteDatabase database = app.GetDatabase();


        Cursor cursor = database.query(
                LocationWithSignal.GetClassNameString(),
                new String[] {"id", "latitude", "longitude", "signalStrength", "createdDate"},
                selection,
                null,
                null,
                null,
                "id DESC"
        );

        while(cursor.moveToNext()) {

            int id = cursor.getInt(0);
            double latitude = cursor.getDouble(1);
            double longitude = cursor.getDouble(2);
            int signalStrength = cursor.getInt(3);
            String createdDate = cursor.getString(4);


            LocationWithSignal reminder = new LocationWithSignal(id, latitude, longitude, signalStrength, createdDate);

            locationsWithSignalList.add(reminder);
        }

        return locationsWithSignalList;
    }

    public static boolean Create(Context context , LocationWithSignal measure) {

        SignalNotifierApp app =  ((SignalNotifierApp)context.getApplicationContext());
        SQLiteDatabase database = app.GetDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("longitude", measure.GetLongitude());
        contentValues.put("latitude", measure.GetLatitide());
        contentValues.put("signalStrength", measure.GetSignalStrength());
        contentValues.put("createdDate", measure.GetCreatedDateAsString());


        try {
            long result = database.insert(measure.GetClassNameString(), null, contentValues);
            return result > 0;
        } catch (Exception ex) {
            Log.d("SignalNotifierDB", "Failed to insert into '" + measure.GetClassNameString() + "'.");
            ex.printStackTrace();
            return false;
        }
    }
}
