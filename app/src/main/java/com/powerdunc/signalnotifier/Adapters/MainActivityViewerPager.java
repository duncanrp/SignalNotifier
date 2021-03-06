package com.powerdunc.signalnotifier.Adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.powerdunc.signalnotifier.MainActivity_Controller;
import com.powerdunc.signalnotifier.MainActivity_LocationsWithSignal;
import com.powerdunc.signalnotifier.MainAtivity_LocationsWithSignal_All;

public class MainActivityViewerPager extends FragmentPagerAdapter {

    private Context context;


    public MainActivityViewerPager(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position)
        {
            case 0:
                fragment = new MainActivity_Controller();
                break;
            case 1:
                fragment = new MainActivity_LocationsWithSignal();
                break;
            case 2:
                fragment = new MainAtivity_LocationsWithSignal_All();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
