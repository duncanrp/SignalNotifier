package com.powerdunc.signalnotifier.Services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.CellSignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.powerdunc.signalnotifier.DataAccess.AppSettingsDAC;
import com.powerdunc.signalnotifier.DataAccess.Database;
import com.powerdunc.signalnotifier.Listener.SignalStateListener;
import com.powerdunc.signalnotifier.Models.AppSetting;
import com.powerdunc.signalnotifier.R;

public class MobileStrengthService extends Service {


    public NotificationManager notificationManager;
    TelephonyManager telephonyManager;
    SignalStateListener signalStateListener;
    Database database;

    Intent intent;
    String[] perms = new String[] {};

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        signalStateListener = new SignalStateListener(this);

        telephonyManager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        telephonyManager.listen(signalStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        Notification notification = new Notification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelName = getResources().getString(R.string.notificationChannelName);

            NotificationChannel notificationChannel = new NotificationChannel(
                    channelName,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
            );

            notificationManager.createNotificationChannel(notificationChannel);

            notification = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("MobileSignalStrength")
                    .setChannelId(channelName)
                    .setContentText("").build();


        }

        //Start Servuce
        startForeground(1, notification);

        //Acquire WakeLock
        PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);

        if(mgr != null) {
            PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MobileSignalStrength:WakeLock");
            wakeLock.acquire();
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;

        return START_STICKY;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {

        AppSetting setting = AppSettingsDAC.GetSetting(this, "enabled");


        if(setting.GetValue().equals("true")) {
            //Restart the service once it has been killed android
            Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());

            restartServiceIntent.setPackage(getPackageName());

            //Create pending intent
            PendingIntent restartServicePI = PendingIntent.getService(
                    getApplicationContext(),
                    1,
                    restartServiceIntent,
                    PendingIntent.FLAG_ONE_SHOT
            );

            AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartServicePI);
        }
    }
}
