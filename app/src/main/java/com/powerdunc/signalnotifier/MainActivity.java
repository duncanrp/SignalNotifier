package com.powerdunc.signalnotifier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.powerdunc.signalnotifier.DataAccess.AppDAC;
import com.powerdunc.signalnotifier.Services.MobileStrengthService;


public class MainActivity extends AppCompatActivity {


    Button settingsButton;
    Button helpButton;

    ToggleButton enabledToggleButton;
    TextView listeningMessageTextView;
    WebView listeningHTMLTextView;

    ViewGroup mainActivityViewGroup;

    AdView adView;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the default actionbar instance
        ActionBar mActionBar = getSupportActionBar();
        //Setup our actionbar
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.activity_main_actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        InitControls();

        if (preferences.getBoolean("listenerEnabled", false) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            StartListening();
        }
        else
        {
            StopListening();
        }




        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SettingsActivity.class);

                startActivityForResult(intent, 0);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HelpActivity.class);

                startActivityForResult(intent, 0);
            }
        });


        enabledToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor = preferences.edit();

                boolean isEnabled = enabledToggleButton.isChecked();

                editor.putBoolean("listenerEnabled", isEnabled);

                editor.commit();

                if(isEnabled)
                {
                    StartListening();
                }
                else
                {
                    StopListening();
                }

            }
        });

    }

    public void InitControls() {

        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mainActivityViewGroup = (ViewGroup)findViewById(R.id.mainActivityLayout);
        settingsButton = (Button)findViewById(R.id.settingsButton);
        helpButton = (Button)findViewById(R.id.helpButton);
        enabledToggleButton = (ToggleButton) findViewById(R.id.enabledToggleButton);
        listeningMessageTextView = (TextView)findViewById(R.id.listeningMessage);
        listeningHTMLTextView = (WebView) findViewById(R.id.listeningHTML);


//        if(notificationSoundSetting == null)
//        {
//            notificationSoundSetting = new AppSetting("notificationSound", String.valueOf(R.raw.stairs));
//            notificationSoundSetting.Save(this);
//        }
//
//        if(notificationStyleSetting == null)
//        {
//            notificationStyleSetting = new AppSetting("notificationStyle", NotificationStyle.BarStrength.ordinal());
//            notificationStyleSetting.Save(this);
//        }

        enabledToggleButton.setChecked(preferences.getBoolean("listenerEnabled", false));

        listeningHTMLTextView.loadDataWithBaseURL("", getString(R.string.loadingBlocksHTML2), "text/html", "UTF-8", "");
    }


    public void StartListening()
    {
        //Get current volume
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        if(vol <= 2)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your notification volume is currently low. It is recommended to turn this up so that you hear SignalNotifiers notifications.");
            builder.setTitle("Recommendation");
            builder.setPositiveButton("Ok, Thanks!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //Transition on text display
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(mainActivityViewGroup);
        }

        listeningMessageTextView.setVisibility(View.VISIBLE);
        listeningHTMLTextView.setVisibility(View.VISIBLE);

        StartService();
    }

    public void StopListening()
    {
        //Transition on text display
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(mainActivityViewGroup);
        }


        listeningMessageTextView.setVisibility(View.GONE);
        listeningHTMLTextView.setVisibility(View.GONE);

        StopService();
    }


    public void StopService()
    {
        Intent intent = new Intent(MainActivity.this, MobileStrengthService.class);
        stopService(intent);
    }

    public void StartService()
    {
        Intent intent = new Intent(MainActivity.this, MobileStrengthService.class);
        startService(intent);
    }

}
