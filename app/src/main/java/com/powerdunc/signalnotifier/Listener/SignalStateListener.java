package com.powerdunc.signalnotifier.Listener;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;

import com.powerdunc.signalnotifier.DataAccess.AppSettingsDAC;
import com.powerdunc.signalnotifier.DataAccess.Database;
import com.powerdunc.signalnotifier.DataAccess.StrengthMeasureDAC;
import com.powerdunc.signalnotifier.Models.AppSetting;
import com.powerdunc.signalnotifier.Models.NotificationSound;
import com.powerdunc.signalnotifier.Models.NotificationStyle;
import com.powerdunc.signalnotifier.Models.StrengthMeasure;
import com.powerdunc.signalnotifier.Providers.SoundProvider;
import com.powerdunc.signalnotifier.Providers.VibrationProvider;
import com.powerdunc.signalnotifier.R;
import com.powerdunc.signalnotifier.Services.MobileStrengthService;
import com.powerdunc.signalnotifier.SignalNotifierApp;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignalStateListener extends PhoneStateListener {

    private String TAG = "SignalStrengthListener";

    private int lastStrength = -1;
    private Context context;
    private SignalNotifierApp application;
    private AppSetting notificationSoundSetting;
    private AppSetting notificationStyleSetting;
    private AppSetting notificationSoundEnabledSetting;

    public SignalStateListener(Context context) {
        this.context = context;

        this.application = (SignalNotifierApp)context.getApplicationContext();
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);

        notificationSoundEnabledSetting = AppSettingsDAC.GetSetting(context.getApplicationContext(), "notificationSoundEnabled");
        notificationSoundSetting = AppSettingsDAC.GetSetting(context.getApplicationContext(), "notificationSound");
        notificationStyleSetting = AppSettingsDAC.GetSetting(context.getApplicationContext(), "notificationStyle");


        int barStrength = -1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            barStrength = signalStrength.getLevel();
        }

        barStrength = barStrength;

        Log.d(TAG, "Signal strength changed. Current Strength=" + barStrength + ", Last Strength=" + lastStrength);


        if(lastStrength == 0 && barStrength > 0) {

            NotificationStyle style = NotificationStyle.values()[notificationStyleSetting.GetValueInt()];

            switch(style)
            {
                case BarStrength:
                    PlayNotificationSound(barStrength);
                    break;

                case Single:
                    PlayNotificationSound(1);
                    break;
            }
        }

        //Store strength measure
        StrengthMeasure measure = new StrengthMeasure(
                                            barStrength,
                                            application.GetStoredDateFormat().format(new Date())
                                    );

        boolean storedMeasure = StrengthMeasureDAC.CreateReminder(context, measure);


        lastStrength = barStrength;
    }

    public void PlayNotificationSound(int totalPlays)
    {
        for(int i = 0; i < totalPlays; i++) {

            if(notificationSoundEnabledSetting.GetValueBool())
                SoundProvider.PlaySound(context, notificationSoundSetting.GetValueInt());


            VibrationProvider.Vibrate(context, 250);

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
                    .setSmallIcon(android.support.compat.R.drawable.notification_icon_background)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .build();

            ((MobileStrengthService)context).notificationManager.notify(1, notification);
        }
    }
}
