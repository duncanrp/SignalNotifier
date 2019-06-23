package com.powerdunc.signalnotifier.DataAccess;

import android.content.Context;

import com.powerdunc.signalnotifier.SignalNotifierApp;

public class AppDAC {

    private static SignalNotifierApp _App;

    public static SignalNotifierApp GetApp(Context context)
    {
         if(_App == null)
         {
             _App =  ((SignalNotifierApp)context.getApplicationContext());
         }

         return _App;
    }
}
