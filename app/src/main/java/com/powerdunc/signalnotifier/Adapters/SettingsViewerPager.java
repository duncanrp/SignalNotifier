package com.powerdunc.signalnotifier.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
                fragment = new Settings_Sound();
                break;
            case 1:
                fragment = new Settings_Vibration();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
