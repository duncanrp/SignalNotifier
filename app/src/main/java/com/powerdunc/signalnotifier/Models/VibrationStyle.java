package com.powerdunc.signalnotifier.Models;

import java.util.ArrayList;
import java.util.List;

public enum VibrationStyle {
    BarStrength(0, "Vibrate Per Signal Strength Bar"),
    Single(1, "Single Vibration");

    private final int value;
    private final String displayValue;

    private VibrationStyle(int value, String displayValue)
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

        for(VibrationStyle style : values())
        {
            returnArr.add(style.displayValue);
        }

        return returnArr;
    }

    public static VibrationStyle GetByDisplayValue(String displayValue)
    {

        for(VibrationStyle style : values())
        {
            if(style.displayValue.equals(displayValue))
                return style;
        }

        return null;
    }
}
