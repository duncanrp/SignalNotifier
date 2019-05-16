package com.powerdunc.signalnotifier;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.powerdunc.signalnotifier.DataAccess.AppSettingsDAC;
import com.powerdunc.signalnotifier.Models.AppSetting;
import com.powerdunc.signalnotifier.Models.NotificationStyle;
import com.powerdunc.signalnotifier.Services.MobileStrengthService;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    Button settingsButton;
    Button helpButton;

    ToggleButton enabledToggleButton;
    TextView listeningMessageTextView;
    WebView listeningHTMLTextView;

    ViewGroup mainActivityViewGroup;

    AppSetting enabledSetting;
    AppSetting notificationSoundSetting;
    AppSetting notificationStyleSetting;

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

        InitControls();

        if (enabledSetting.GetValueBool() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
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

                boolean isEnabled = enabledToggleButton.isChecked();

                enabledSetting.SetValue(Boolean.toString(isEnabled));

                boolean saved = enabledSetting.Save(view.getContext());

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
        mainActivityViewGroup = (ViewGroup)findViewById(R.id.mainActivityLayout);
        settingsButton = (Button)findViewById(R.id.settingsButton);
        helpButton = (Button)findViewById(R.id.helpButton);
        enabledToggleButton = (ToggleButton) findViewById(R.id.enabledToggleButton);
        listeningMessageTextView = (TextView)findViewById(R.id.listeningMessage);
        listeningHTMLTextView = (WebView) findViewById(R.id.listeningHTML);

        enabledSetting = AppSettingsDAC.GetSetting(this, "enabled");

        notificationSoundSetting = AppSettingsDAC.GetSetting(this, "notificationSound");
        notificationStyleSetting = AppSettingsDAC.GetSetting(this, "notificationStyle");

        if(enabledSetting==null)
        {
            enabledSetting = new AppSetting("enabled", "true");
            enabledSetting.Save(this);
        }

        if(notificationSoundSetting == null)
        {
            notificationSoundSetting = new AppSetting("notificationSound", String.valueOf(R.raw.stairs));
            notificationSoundSetting.Save(this);
        }

        if(notificationStyleSetting == null)
        {
            notificationStyleSetting = new AppSetting("notificationStyle", NotificationStyle.BarStrength.ordinal());
            notificationStyleSetting.Save(this);
        }

        enabledToggleButton.setChecked(Boolean.valueOf(enabledSetting.GetValue()));

        listeningHTMLTextView.loadDataWithBaseURL("", getString(R.string.loadingBlocksHTML), "text/html", "UTF-8", "");
    }


    public void StartListening()
    {
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
