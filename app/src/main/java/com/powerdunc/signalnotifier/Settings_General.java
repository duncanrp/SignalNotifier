package com.powerdunc.signalnotifier;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.powerdunc.signalnotifier.Models.SettingsViewModel;

public class Settings_General extends Fragment {


    SettingsViewModel viewModel;

    private Settings_Sound.OnFragmentInteractionListener mListener;

    SharedPreferences preferences;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings__general, container, false);

        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(SettingsViewModel.class);

        LoadSettings();
        LoadControls(view);

        // Inflate the layout for this fragment
        return view;
    }

    public void LoadSettings()
    {

    }

    public void LoadControls(View parent)
    {
        boolean notifySignalLossEnabled = preferences.getBoolean("notifySignalLoss", false);
        viewModel.notifySignalLossBtn = (ToggleButton)parent.findViewById(R.id.notifySignalLossBtn);
        viewModel.notifySignalLossBtn.setChecked(notifySignalLossEnabled);
    }


}
