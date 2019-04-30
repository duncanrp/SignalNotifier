package com.powerdunc.signalnotifier.Models;

import java.util.ArrayList;
import java.util.List;

public enum NotificationStyle {
    BarStrength(0, "Sound Per Signal Strength Bar"),
    Single(1, "Single Notification");

    private final int value;
    private final String displayValue;

    private NotificationStyle(int value, String displayValue)
    {
        this.value = value;
        this.displayValue = displayValue;
    }

    public String DisplayValue()
    {
        return this.displayValue;
    }

    public static List<String> GetDisplayValues()
    {
        List<String> returnArr = new ArrayList<>();

        for(NotificationStyle style : values())
        {
            returnArr.add(style.displayValue);
        }

        return returnArr;
    }

    public static NotificationStyle GetByDisplayValue(String displayValue)
    {

        for(NotificationStyle style : values())
        {
            if(style.displayValue.equals(displayValue))
                return style;
        }

        return null;
    }
}
