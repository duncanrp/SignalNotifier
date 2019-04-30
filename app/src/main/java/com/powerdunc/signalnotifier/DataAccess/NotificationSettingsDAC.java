package com.powerdunc.signalnotifier.DataAccess;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.powerdunc.signalnotifier.Adapters.SimpleSpinnerAdapter;
import com.powerdunc.signalnotifier.Models.NotificationSound;
import com.powerdunc.signalnotifier.Models.NotificationStyle;
import com.powerdunc.signalnotifier.R;

public class NotificationSettingsDAC {

    public static SimpleSpinnerAdapter GetNotificationSounds(Context context)
    {
       SimpleSpinnerAdapter notificationStyleAdapter = new SimpleSpinnerAdapter(
                context,
                NotificationSound.GetDisplayValues()
        );


        return notificationStyleAdapter;
    }

    public static SimpleSpinnerAdapter GetNotificationStyles(Context context)
    {
        SimpleSpinnerAdapter notificationStyleAdapter = new SimpleSpinnerAdapter(
                context,
                NotificationStyle.GetDisplayValues()
        );


        return notificationStyleAdapter;
    }
}
