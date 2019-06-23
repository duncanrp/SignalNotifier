package com.powerdunc.signalnotifier;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.powerdunc.signalnotifier.DataAccess.AppSettingsDAC;
import com.powerdunc.signalnotifier.Models.AppSetting;
import com.powerdunc.signalnotifier.Models.SettingsViewModel;


public class Settings_Vibration extends Fragment {


    private OnFragmentInteractionListener mListener;
    private SettingsViewModel viewModel;
    private RelativeLayout vibrationSettingsRL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings__vibration, container, false);

        viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(SettingsViewModel.class);

        viewModel.notificationVibrationEnabledSetting = AppSettingsDAC.GetSetting(getContext(), "vibrationEnabled");


        if(viewModel.notificationVibrationEnabledSetting == null)
        {
            viewModel.notificationVibrationEnabledSetting = new AppSetting("vibrationEnabled", "true");
            viewModel.notificationVibrationEnabledSetting.Save(getContext());
        }

        if(!viewModel.notificationVibrationEnabledSetting.GetValueBool())
        {
            vibrationSettingsRL.setVisibility(View.INVISIBLE);
        }

        vibrationSettingsRL = (RelativeLayout) view.findViewById(R.id.vibrationSettingsLayout);

        viewModel.vibrationEnabledBtn = (ToggleButton) view.findViewById(R.id.vibrationToggleButton);
        viewModel.vibrationEnabledBtn.setChecked(viewModel.notificationVibrationEnabledSetting.GetValueBool());

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

        // Inflate the layout for this fragment
        return view;
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
