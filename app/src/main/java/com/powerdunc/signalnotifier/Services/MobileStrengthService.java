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
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.powerdunc.signalnotifier.DataAccess.Database;
import com.powerdunc.signalnotifier.Listener.SignalStateListener;
import com.powerdunc.signalnotifier.MainActivity;
import com.powerdunc.signalnotifier.R;

public class MobileStrengthService extends Service {


    public NotificationManager notificationManager;
    TelephonyManager telephonyManager;
    SignalStateListener signalStateListener;
    Database database;
    SharedPreferences preferences;
    Notification notification;

    Intent intent;

    private static final int NOTIFICATION_ID = 1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        signalStateListener = new SignalStateListener(this);

        telephonyManager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        telephonyManager.listen(signalStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        notification = new Notification();

        Intent showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelName = getResources().getString(R.string.notificationChannelName);

            NotificationChannel notificationChannel = new NotificationChannel(
                    channelName,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
            );

            notificationManager.createNotificationChannel(notificationChannel);

            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("MobileSignalStrength")
                    .setChannelId(channelName)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.signalnotifier_launcher_icon_round)
                    .setContentIntent(contentIntent)
                    .setContentText("Signal Notifier is running. Tap here to open the application.")
                    .setSubText("Plss")
                    .build();




        }

        //Start Servuce
        startForeground(NOTIFICATION_ID, notification);

        //Acquire WakeLock
        PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);

        if(mgr != null) {
            PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MobileSignalStrength:WakeLock");
            wakeLock.acquire();
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();


        telephonyManager.listen(signalStateListener, PhoneStateListener.LISTEN_NONE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;

        return START_STICKY;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {

        if(preferences.getBoolean("listenerEnabled", false)) {
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
