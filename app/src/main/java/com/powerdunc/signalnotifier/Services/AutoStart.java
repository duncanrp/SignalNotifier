package com.powerdunc.signalnotifier.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.powerdunc.signalnotifier.DataAccess.AppSettingsDAC;
import com.powerdunc.signalnotifier.Models.AppSetting;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intentRec)
    {
        AppSetting setting = AppSettingsDAC.GetSetting(context, "enabled");


        if (setting.GetValue().equals("true") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(context, MobileStrengthService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startForegroundService(intent);

            Log.d("SignalStrengthAutoStart", "Service Auto-Started");
        }
    }

}
