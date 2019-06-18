package com.powerdunc.signalnotifier.Models;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.powerdunc.signalnotifier.DataAccess.AppSettingsDAC;

public class SettingsViewModel extends ViewModel {

    //Sounds Settings
    public AppSetting notificationSoundEnabledSetting;
    public AppSetting notificationSoundSetting;
    public AppSetting notificationStyleSetting;
    public NotificationSound currentNotificationSound;
    public NotificationStyle currentNotificationStyle;

    public ToggleButton notificationSoundEnabledBtn;
    public Button notificationSoundPreviewBtn;
    public Spinner notificationStyleSelector;
    public Spinner notificationSoundSelector;


    //Vibration Settings
    public AppSetting notificationVibrationSetting;
    public AppSetting notificationVibrationStyleSetting;

}
