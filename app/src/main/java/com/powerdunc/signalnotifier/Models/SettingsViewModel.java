package com.powerdunc.signalnotifier.Models;

import androidx.lifecycle.ViewModel;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class SettingsViewModel extends ViewModel {

    //General Settings
    public ToggleButton notifySignalLossBtn;


    //Sounds Settings
    public NotificationSound currentNotificationSound;
    public NotificationStyle currentNotificationStyle;

    public ToggleButton notificationSoundEnabledBtn;
    public Button notificationSoundPreviewBtn;
    public Spinner notificationStyleSelector;
    public Spinner notificationSoundSelector;


    //Vibration Settings
    public ToggleButton vibrationEnabledBtn;
    public Spinner vibrationStyleSelector;


}
