package com.powerdunc.signalnotifier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.powerdunc.signalnotifier.Controls.LocationsWithSignalRecyclerView;
import com.powerdunc.signalnotifier.DataAccess.LocationWithSignalDAC;
import com.powerdunc.signalnotifier.Models.LocationWithSignal;

import java.util.ArrayList;


public class MainAtivity_LocationsWithSignal_All  extends Fragment {


    MapView mapView;
    GoogleMap googleMap2;
    ArrayList<LocationWithSignal> LocationsWithSignal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_locationswithsignal_all, container, false);




        mapView = view.findViewById(R.id.allSignalsMapView);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
             googleMap2 = googleMap;
             LoadMap();
            }
        });

        return view;


    }

    public void LoadMap()
    {
        if(googleMap2 == null)
            return;

        LocationsWithSignal = LocationWithSignalDAC.GetRemindersList(getContext());
        googleMap2.clear();
        for(LocationWithSignal signal : LocationsWithSignal) {


            googleMap2.addMarker(
                    new MarkerOptions()
                            .position(signal.GetLatLng())
                            .title("Location with Signal")
                            .snippet("Strength: " + signal.GetSignalStrength()
                                    + " Bar(s).\nLatitude: " + signal.GetLatitide()
                                    + "\nLongitude: " + signal.GetLongitude()
                            )

            );

            googleMap2.addCircle(new CircleOptions()
                    .center(signal.GetLatLng())
                    .radius(50)
                    .fillColor(Color.argb(127, 0, 0, 255))

            );

            googleMap2.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    LinearLayout info = new LinearLayout(getContext());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(getContext());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(getContext());
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());


                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        LoadMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
