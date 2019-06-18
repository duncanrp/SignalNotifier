package com.powerdunc.signalnotifier.Models;

import com.powerdunc.signalnotifier.R;

import java.util.ArrayList;
import java.util.List;

public enum NotificationSound {
    Stairs(R.raw.stairs, "Stairs"),
    Jump(R.raw.jump, "Video Game Jump");

    private final int value;
    private final String displayValue;

    private NotificationSound(int value, String displayValue)
    {
        this.value = value;
        this.displayValue = displayValue;
    }

    public int GetValue()
    {
        return value;
    }

    public String GetDisplayValue()
    {
        return displayValue;
    }

    public static List<String> GetDisplayValues()
    {
        List<String> returnArr = new ArrayList<>();

        for(NotificationSound sound : values())
        {
            returnArr.add(sound.displayValue);
        }

        return returnArr;
    }

    public static NotificationSound GetByValue(int value)
    {
        String s = "";

       for(NotificationSound sound : values())
       {
           if(sound.value == value)
               return sound;
       }

       return null;
    }

    public static NotificationSound GetByDisplayValue(String displayValue)
    {

        for(NotificationSound sound : values())
        {
            if(sound.displayValue.equals(displayValue))
                return sound;
        }

        return null;
    }
}
