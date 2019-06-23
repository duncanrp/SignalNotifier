package com.powerdunc.signalnotifier;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.powerdunc.signalnotifier.DataAccess.Database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Duncan on 08-Jan-19.
 */

public class SignalNotifierApp extends Application {

    private Database database;
    private DateFormat storedDateFormat;
    private SQLiteDatabase sqLiteDatabase;

    public SQLiteDatabase GetDatabase() {

        if(database == null) {
            database = new Database(this);

        }

        if(sqLiteDatabase == null)
        {
            sqLiteDatabase = database.getWritableDatabase();
        }

        return sqLiteDatabase;
    }

    public DateFormat GetStoredDateFormat() {

        if(storedDateFormat == null) {
            storedDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        }

        return storedDateFormat;
    }
}
