package com.powerdunc.signalnotifier;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.powerdunc.signalnotifier.Adapters.SimpleSpinnerAdapter;
import com.powerdunc.signalnotifier.DataAccess.NotificationSettingsDAC;
import com.powerdunc.signalnotifier.Models.NotificationSound;
import com.powerdunc.signalnotifier.Models.NotificationStyle;
import com.powerdunc.signalnotifier.Models.SettingsViewModel;
import com.powerdunc.signalnotifier.Providers.SoundProvider;


public class Settings_Sound extends Fragment {

    SettingsViewModel viewModel;
    public Button cancelSettingsBtn, saveSettingsBtn;
    public RelativeLayout soundSettingsLinearLayout;

    private OnFragmentInteractionListener mListener;

    SharedPreferences preferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings__sound, container, false);

        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(SettingsViewModel.class);


        LoadSettings(view);

        LoadControls(view);
        InitUI();

        // Inflate the layout for this fragment
        return view;
    }

    public void LoadSettings(View parent)
    {
        int notificationStyle = preferences.getInt("notificationStyle", 0);
        viewModel.currentNotificationStyle = ((NotificationStyle)NotificationStyle.values()[notificationStyle]);
    }

    public void LoadControls(View parent)
    {


        soundSettingsLinearLayout = (RelativeLayout)parent.findViewById(R.id.soundSettingsLayout);

        boolean notificationSoundEanbled = preferences.getBoolean("notificationSoundEnabled", true);

        if(!notificationSoundEanbled)
        {
            soundSettingsLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            soundSettingsLinearLayout.setVisibility(View.VISIBLE);
        }


        viewModel.notificationSoundEnabledBtn = (ToggleButton)parent.findViewById(R.id.soundToggleButton);
        viewModel.notificationSoundEnabledBtn.setChecked(notificationSoundEanbled);

        viewModel.notificationSoundSelector = (Spinner)parent.findViewById(R.id.notificationSoundNameSelector);

        viewModel.notificationSoundPreviewBtn = (Button) parent.findViewById(R.id.selectNotificationSoundPreviewBtn);


        viewModel.notificationSoundEnabledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean enabled = viewModel.notificationSoundEnabledBtn.isChecked();

                if(!enabled)
                {
                    soundSettingsLinearLayout.setVisibility(View.INVISIBLE);
                }
                else
                {
                    soundSettingsLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.notificationSoundPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selected = viewModel.notificationSoundSelector.getSelectedItem().toString();
                NotificationSound selectedSound = NotificationSound.GetByDisplayValue(selected);

                SoundProvider.PlaySound(view.getContext(), selectedSound.GetValue());
            }
        });

        viewModel.notificationStyleSelector = (Spinner)parent.findViewById(R.id.notificationStyleSelector);
    }

    public void InitUI()
    {
        int soundSettingVal = preferences.getInt("notificationSound", NotificationSound.Jump.GetValue());


        viewModel.currentNotificationSound = NotificationSound.GetByValue(soundSettingVal);


        SetNotificationSoundData();
        SetNotificationStyleData();
    }


    public void SetNotificationSoundData()
    {
        int currentNotificationStyle = preferences.getInt("notificationStyle", 0);

        SimpleSpinnerAdapter adapter = NotificationSettingsDAC.GetNotificationSounds(this.getContext());

        viewModel.notificationSoundSelector.setAdapter(adapter);

        String currentNotificationStyleStr = NotificationStyle.values()[currentNotificationStyle].DisplayValue();

        int position = adapter.getPosition(viewModel.currentNotificationSound.GetDisplayValue());

        viewModel.notificationSoundSelector.setSelection(position);
    }

    public void SetNotificationStyleData()
    {
        SimpleSpinnerAdapter adapter = NotificationSettingsDAC.GetNotificationStyles(this.getContext());

        viewModel.notificationStyleSelector.setAdapter(adapter);

        String currentNotificationStyleStr = viewModel.currentNotificationStyle.DisplayValue();

        int position = adapter.getPosition(currentNotificationStyleStr);

        viewModel.notificationStyleSelector.setSelection(position);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
