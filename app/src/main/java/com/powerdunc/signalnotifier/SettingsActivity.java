package com.powerdunc.signalnotifier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.powerdunc.signalnotifier.Adapters.SimpleSpinnerAdapter;
import com.powerdunc.signalnotifier.DataAccess.AppSettingsDAC;
import com.powerdunc.signalnotifier.DataAccess.NotificationSettingsDAC;
import com.powerdunc.signalnotifier.Models.AppSetting;
import com.powerdunc.signalnotifier.Models.NotificationSound;
import com.powerdunc.signalnotifier.Models.NotificationStyle;
import com.powerdunc.signalnotifier.Providers.SoundProvider;

public class SettingsActivity extends AppCompatActivity {


    AppSetting notificationSoundSetting;
    AppSetting notificationStyleSetting;

    Button cancelSettingsBtn, saveSettingsBtn;

    Button notificationSoundPreviewBtn;
    Spinner notificationStyleSelector;
    Spinner notificationSoundSelector;

    NotificationStyle currentNotificationStyle;
    NotificationSound currentNotificationSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //Get the default actionbar instance
        ActionBar mActionBar = getSupportActionBar();


        //Setup our actionbar
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.activity_settings_actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        //Setup our actionbar
        //LayoutInflater mInflater = LayoutInflater.from(this);
        //View mCustomView = mInflater.inflate(R.layout.activity_settings, null);
        //mActionBar.setCustomView(mCustomView);
        //mActionBar.setDisplayShowCustomEnabled(true);

        //Intent intent = new Intent(this, MobileStrengthService.class);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //startService(intent);
        //}

        LoadSettings();
        LoadControls();
        InitUI();
    }

    public void InitUI()
    {
        currentNotificationSound = NotificationSound.GetByValue(notificationSoundSetting.GetValueInt());

        SetNotificationSoundData();
        SetNotificationStyleData();
    }

    public void SetNotificationSoundData()
    {
       SimpleSpinnerAdapter adapter = NotificationSettingsDAC.GetNotificationSounds(this);

        notificationSoundSelector.setAdapter(adapter);

        String currentNotificationStyleStr = notificationSoundSetting.GetValue();

        int position = adapter.getPosition(currentNotificationSound.GetDisplayValue());

        notificationSoundSelector.setSelection(position);
    }

    public void SetNotificationStyleData()
    {
        SimpleSpinnerAdapter adapter = NotificationSettingsDAC.GetNotificationStyles(this);

        notificationStyleSelector.setAdapter(adapter);

        String currentNotificationStyleStr = currentNotificationStyle.DisplayValue();

        int position = adapter.getPosition(currentNotificationStyleStr);

        notificationStyleSelector.setSelection(position);
    }

    public void LoadControls()
    {
        notificationSoundSelector = (Spinner) findViewById(R.id.notificationSoundNameSelector);
        notificationSoundPreviewBtn = (Button)findViewById(R.id.selectNotificationSoundPreviewBtn);

        notificationSoundPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selected = notificationSoundSelector.getSelectedItem().toString();
                NotificationSound selectedSound = NotificationSound.GetByDisplayValue(selected);

                SoundProvider.PlaySound(view.getContext(), selectedSound.GetValue());
            }
        });

        notificationStyleSelector = (Spinner)findViewById(R.id.notificationStyleSelector);

        notificationStyleSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cancelSettingsBtn = (Button)findViewById(R.id.settingsCancelButton);
        cancelSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveSettingsBtn = (Button)findViewById(R.id.settingsSaveButton);
        saveSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveSettings();
                finish();
            }
        });
    }

    public void SaveSettings()
    {
        //Notification Sound
        String selectedSound = notificationSoundSelector.getSelectedItem().toString();
        NotificationSound selectedNotificationSound = NotificationSound.GetByDisplayValue(selectedSound);

        notificationSoundSetting.SetValue(selectedNotificationSound.GetValue());
        notificationSoundSetting.Save(this);

        //Notification Style
        String selected = notificationStyleSelector.getSelectedItem().toString();
        NotificationStyle selectedNotificationStyle = NotificationStyle.GetByDisplayValue(selected);


        notificationStyleSetting.SetValue(selectedNotificationStyle.ordinal());
        notificationStyleSetting.Save(this);
    }

    public void LoadSettings()
    {
        notificationSoundSetting = AppSettingsDAC.GetSetting(this, "notificationSound");
        notificationStyleSetting = AppSettingsDAC.GetSetting(this, "notificationStyle");
        currentNotificationStyle = ((NotificationStyle)NotificationStyle.values()[notificationStyleSetting.GetValueInt()]);
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}
