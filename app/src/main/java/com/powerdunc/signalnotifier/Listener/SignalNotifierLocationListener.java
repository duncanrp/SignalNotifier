package com.powerdunc.signalnotifier.Listener;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.powerdunc.signalnotifier.DataAccess.LocationWithSignalDAC;
import com.powerdunc.signalnotifier.Models.LocationWithSignal;
import com.powerdunc.signalnotifier.SignalNotifierApp;
import com.powerdunc.signalnotifier.Utils.ImageUtils;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Date;

public class SignalNotifierLocationListener implements LocationListener {

    private int lastSignalStrength;
    private Context context;
    private GoogleMap map;

    public SignalNotifierLocationListener(Context context)
    {
        this.context = context;
    }

    public void SetLastSignalStrength(int strength)
    {
        this.lastSignalStrength = strength;
    }

    @Override
    public void onLocationChanged(Location location) {


        Log.d("SignalNotifierLWS", "Location with signal recieved!");

        LocationWithSignal lws = new LocationWithSignal(
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            lastSignalStrength,
                                            ((SignalNotifierApp)context.getApplicationContext()).GetStoredDateFormat().format(new Date())
        );


        LocationWithSignalDAC.Create(context, lws);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}