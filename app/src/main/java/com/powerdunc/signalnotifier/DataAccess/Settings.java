package com.powerdunc.signalnotifier.DataAccess;

import android.app.Notification;

import com.powerdunc.signalnotifier.Models.NotificationStyle;
import com.powerdunc.signalnotifier.R;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

public class Settings {

    public static HashMap<String, Object> Defaults = new HashMap<String, Object>()
    {{
        put("notificationSoundEnabled", "true");
        put("notificationSound", R.raw.jump);
        put("notificationStyle", NotificationStyle.BarStrength);


        put("vibrationEnabled", "false");
        put("vibrationStyle", NotificationStyle.BarStrength);
    }};

}
