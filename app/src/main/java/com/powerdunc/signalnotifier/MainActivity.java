package com.powerdunc.signalnotifier;


import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.powerdunc.signalnotifier.Adapters.MainActivityViewerPager;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;


public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;

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


        viewPager = findViewById(R.id.MainViewerPage);

        MainActivityViewerPager settingsViewerPager = new MainActivityViewerPager(this, getSupportFragmentManager());

        viewPager.setAdapter(settingsViewerPager);


        String[] permissionsReq = getString(R.string.app_permissions).split(",");

        if(!EasyPermissions.hasPermissions(this,  permissionsReq))
        {
            EasyPermissions.requestPermissions(this, "", 0, permissionsReq);
        }
    }
}
