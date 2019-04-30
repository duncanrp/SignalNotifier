package com.powerdunc.signalnotifier.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.powerdunc.signalnotifier.Models.StrengthMeasure;
import com.powerdunc.signalnotifier.SignalNotifierApp;

public class StrengthMeasureDAC {

    public static boolean CreateReminder(Context context , StrengthMeasure measure) {

        SignalNotifierApp app =  ((SignalNotifierApp)context.getApplicationContext());
        SQLiteDatabase database = app.GetDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("barStrength", measure.GetBarStrength());
        contentValues.put("createdDate", measure.GetCreatedDate());


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
