package com.powerdunc.signalnotifier.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intentRec)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences.getBoolean("listenerEnabled", false) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(context, MobileStrengthService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startForegroundService(intent);

            Log.d("SignalStrengthAutoStart", "Service Auto-Started");
        }
    }

}
