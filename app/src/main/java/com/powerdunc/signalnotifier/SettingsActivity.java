package com.powerdunc.signalnotifier;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.powerdunc.signalnotifier.Adapters.SettingsViewerPager;
import com.powerdunc.signalnotifier.Models.NotificationSound;
import com.powerdunc.signalnotifier.Models.NotificationStyle;
import com.powerdunc.signalnotifier.Models.SettingsViewModel;
import com.powerdunc.signalnotifier.Models.VibrationStyle;

public class SettingsActivity
        extends AppCompatActivity
        implements Settings_Sound.OnFragmentInteractionListener, Settings_Vibration.OnFragmentInteractionListener {




    Button cancelSettingsBtn, saveSettingsBtn;
    TabLayout tabLayout;
    ViewPager viewPager;

    FragmentManager fragmentManager;

    private SettingsViewModel viewModel;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

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


        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        LoadControls();
        InitUI();
    }

    public void InitUI()
    {
        SettingsViewerPager settingsViewerPager = new SettingsViewerPager(this, getSupportFragmentManager());

        viewPager.setAdapter(settingsViewerPager);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    public void LoadControls()
    {
        tabLayout = (TabLayout)findViewById(R.id.SettingsTabs);
        viewPager = (ViewPager)findViewById(R.id.SettingViewerPager);

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
        boolean enabled = viewModel.notificationSoundEnabledBtn.isChecked();

        editor = preferences.edit();


        //Notification Sounds Enabled
        editor.putBoolean("notificationSoundEnabled", enabled);


        //Notification Sound
        String selectedSound = viewModel.notificationSoundSelector.getSelectedItem().toString();
        NotificationSound selectedNotificationSound = NotificationSound.GetByDisplayValue(selectedSound);

        editor.putInt("notificationSound", selectedNotificationSound.GetValue());

        //Notification Style
        String selected = viewModel.notificationStyleSelector.getSelectedItem().toString();
        NotificationStyle selectedNotificationStyle = NotificationStyle.GetByDisplayValue(selected);
        editor.putInt("notificationStyle", selectedNotificationStyle.ordinal());


        //Vibration Enabled
        boolean vibrationEnabled = viewModel.vibrationEnabledBtn.isChecked();
        editor.putBoolean("notificationVibrationEnabled", vibrationEnabled);

        //Vibration Style
        String selectedVibrationStyleStr = viewModel.vibrationStyleSelector.getSelectedItem().toString();
        VibrationStyle selectedVibrationStyle = VibrationStyle.GetByDisplayValue(selectedVibrationStyleStr);
        editor.putInt("vibrationStyle", selectedVibrationStyle.ordinal());

        editor.commit();
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }
}
