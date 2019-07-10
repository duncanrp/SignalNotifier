package com.powerdunc.signalnotifier;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.powerdunc.signalnotifier.Adapters.SimpleSpinnerAdapter;
import com.powerdunc.signalnotifier.DataAccess.NotificationSettingsDAC;
import com.powerdunc.signalnotifier.Models.SettingsViewModel;
import com.powerdunc.signalnotifier.Models.VibrationStyle;


public class Settings_Vibration extends Fragment {


    private OnFragmentInteractionListener mListener;
    private SettingsViewModel viewModel;
    private RelativeLayout vibrationSettingsRL;

    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings__vibration, container, false);

        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(SettingsViewModel.class);

        vibrationSettingsRL = (RelativeLayout) view.findViewById(R.id.vibrationSettingsLayout);

        if(!preferences.getBoolean("notificationVibrationEnabled", true))
        {
            vibrationSettingsRL.setVisibility(View.INVISIBLE);
        }


        viewModel.vibrationEnabledBtn = (ToggleButton) view.findViewById(R.id.vibrationToggleButton);

        viewModel.vibrationEnabledBtn.setChecked(preferences.getBoolean("notificationVibrationEnabled", true));

        viewModel.vibrationStyleSelector = (Spinner) view.findViewById(R.id.vibrationStyleSelector);


        viewModel.vibrationEnabledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean enabled = viewModel.vibrationEnabledBtn.isChecked();

                if(!enabled)
                {
                    vibrationSettingsRL.setVisibility(View.INVISIBLE);
                }
                else
                {
                    vibrationSettingsRL.setVisibility(View.VISIBLE);
                }
            }
        });

        SetVibrationStyleData();

        // Inflate the layout for this fragment
        return view;
    }

    public void SetVibrationStyleData()
    {
        SimpleSpinnerAdapter adapter = NotificationSettingsDAC.GetVibrationStyles(this.getContext());

        viewModel.vibrationStyleSelector.setAdapter(adapter);

        String currentVibrationStyleStr = VibrationStyle.values()[preferences.getInt("vibrationStyle", 0)].DisplayValue();

        int position = adapter.getPosition(currentVibrationStyleStr);

        viewModel.vibrationStyleSelector.setSelection(position);
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
