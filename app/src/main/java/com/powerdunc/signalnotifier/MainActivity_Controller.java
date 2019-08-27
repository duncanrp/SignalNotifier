package com.powerdunc.signalnotifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.powerdunc.signalnotifier.Services.MobileStrengthService;

public class MainActivity_Controller extends Fragment  {


    ImageView settingsButton;
    Button helpButton;

    ToggleButton enabledToggleButton;
    TextView listeningMessageTextView;
    WebView listeningHTMLTextView;

    ViewGroup mainActivityViewGroup;

    AdView adView;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_controller, container, false);


        preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());


        InitControls(view);

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

        // Inflate the layout for this fragment
        return view;
    }


    public void InitControls(View view) {

        adView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mainActivityViewGroup = (ViewGroup)view.findViewById(R.id.subActivityLayout);
        settingsButton = (ImageView)((AppCompatActivity) getActivity()).findViewById(R.id.settingsButton);
        helpButton = (Button)view.findViewById(R.id.helpButton);
        enabledToggleButton = (ToggleButton) view.findViewById(R.id.enabledToggleButton);
        listeningMessageTextView = (TextView) view.findViewById(R.id.listeningMessage);
        listeningHTMLTextView = (WebView) view.findViewById(R.id.listeningHTML);


        enabledToggleButton.setChecked(preferences.getBoolean("listenerEnabled", false));

        listeningHTMLTextView.loadDataWithBaseURL("", getString(R.string.loadingBlocksHTML2), "text/html", "UTF-8", "");
    }


    public void StartListening()
    {
        //Get current volume
        AudioManager audioManager = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        if(vol <= 2)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        Intent intent = new Intent(getContext(), MobileStrengthService.class);
        getContext().stopService(intent);
    }

    public void StartService()
    {
        Intent intent = new Intent(getContext(), MobileStrengthService.class);
        getContext().startService(intent);
    }
}
