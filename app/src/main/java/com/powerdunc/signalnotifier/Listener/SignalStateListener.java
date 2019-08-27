package com.powerdunc.signalnotifier.Listener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;

import com.powerdunc.signalnotifier.DataAccess.StrengthMeasureDAC;
import com.powerdunc.signalnotifier.Models.NotificationSound;
import com.powerdunc.signalnotifier.Models.NotificationStyle;
import com.powerdunc.signalnotifier.Models.StrengthMeasure;
import com.powerdunc.signalnotifier.Models.VibrationStyle;
import com.powerdunc.signalnotifier.Providers.SoundProvider;
import com.powerdunc.signalnotifier.Providers.VibrationProvider;
import com.powerdunc.signalnotifier.R;
import com.powerdunc.signalnotifier.Services.MobileStrengthService;
import com.powerdunc.signalnotifier.SignalNotifierApp;

import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;

public class SignalStateListener extends PhoneStateListener {

    private String TAG = "SignalStrengthListener";

    private int lastStrength = -1;
    private Context context;
    private SignalNotifierApp application;
    SharedPreferences preferences;
    LocationManager locationManager;
    SignalNotifierLocationListener locationListener;

    public SignalStateListener(Context context) {
        this.context = context;

        this.application = (SignalNotifierApp)context.getApplicationContext();

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        locationListener = new SignalNotifierLocationListener(context);
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);

        String[] permissionsReq = context.getString(R.string.app_permissions).split(",");

        int barStrength = -1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            barStrength = signalStrength.getLevel();
        }

        Log.d(TAG, "Signal strength changed. Current Strength=" + barStrength + ", Last Strength=" + lastStrength);


        if(lastStrength == 0 && barStrength > 0) {

            locationListener.SetLastSignalStrength(barStrength);

            final NotificationStyle style = NotificationStyle.values()[preferences.getInt("notificationStyle", 0)];
            final VibrationStyle vStyle = VibrationStyle.values()[preferences.getInt("vibrationStyle", 0)];

            final int actualStrength = barStrength;



            //Request current location
            if(EasyPermissions.hasPermissions(context, permissionsReq)) {
                if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, Looper.getMainLooper());
                else
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, Looper.getMainLooper());
            }

            Runnable soundThread = new Runnable() {

                @Override
                public void run() {
                    switch(style)
                    {
                        case BarStrength:
                            PlayNotificationSound(actualStrength);
                            break;

                        case Single:
                            PlayNotificationSound(1);
                            break;
                    }
                }
            };



            Runnable vibrateThread = new Runnable() {
                @Override
                public void run() {
                    switch(vStyle)
                    {
                        case BarStrength:
                            Vibrate(actualStrength);
                            break;

                        case Single:
                            Vibrate(1);
                            break;
                    }
                }
            };

            soundThread.run();
            vibrateThread.run();


        } else if(lastStrength >= 0 && barStrength == 0) {
            PlayNotificationSound(1);
        }



        //Store strength measure
        StrengthMeasure measure = new StrengthMeasure(
                                            barStrength,
                                            application.GetStoredDateFormat().format(new Date())
                                    );

        boolean storedMeasure = StrengthMeasureDAC.CreateReminder(context, measure);


        lastStrength = barStrength;
    }

    public void Vibrate(int totalVibrations)
    {
        if(!preferences.getBoolean("notificationVibrationEnabled", true))
            return;

        for(int i = 0; i < totalVibrations; i++)
        {
            VibrationProvider.Vibrate(context, 250);

            try {
                Thread.sleep(250);
            } catch (Exception ex) {}
        }
    }

    public void PlayNotificationSound(int totalPlays)
    {
        if(!preferences.getBoolean("notificationSoundEnabled", true))
            return;

        for(int i = 0; i < totalPlays; i++) {

            SoundProvider.PlaySound(context, preferences.getInt("notificationSound", NotificationSound.Jump.GetValue()));

            try {
                Thread.sleep(250);
            } catch (Exception ex) {}
        }
    }

    public void CreateNotification(String title, String text) {
        Notification notification = null;

        final Intent emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 1, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context.getApplicationContext(), Resources.getSystem().getString(R.string.notificationChannelName))
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .build();

            ((MobileStrengthService)context).notificationManager.notify(1, notification);
        }
    }
}
