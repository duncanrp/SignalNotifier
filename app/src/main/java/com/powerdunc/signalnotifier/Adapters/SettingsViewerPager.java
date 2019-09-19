package com.powerdunc.signalnotifier.Adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.powerdunc.signalnotifier.Settings_General;
import com.powerdunc.signalnotifier.Settings_Sound;
import com.powerdunc.signalnotifier.Settings_Vibration;

public class SettingsViewerPager extends FragmentPagerAdapter {

    private Context context;


    public SettingsViewerPager(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position)
        {
            case 0:
                fragment = new Settings_General();
                break;
            case 1:
                fragment = new Settings_Sound();
                break;
            case 2:
                fragment = new Settings_Vibration();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
