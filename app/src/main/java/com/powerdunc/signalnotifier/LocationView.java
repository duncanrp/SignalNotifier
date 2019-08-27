package com.powerdunc.signalnotifier;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.powerdunc.signalnotifier.Models.LocationWithSignal;
import com.powerdunc.signalnotifier.Utils.NumberUtils;

public class LocationView extends AppCompatActivity {

    MapView mapView;
    double latitude = 0F;
    double longitude = 0F;

    Button backBtn;
    TextView viewTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);

        //Get the default actionbar instance
        ActionBar mActionBar = getSupportActionBar();


        //Setup our actionbar
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.activity_locationviewer_actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);


        viewTitleTxt = (TextView)mCustomView.findViewById(R.id.viewerTitle);

        backBtn = (Button)mCustomView.findViewById(R.id.viewerCancelButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mapView = findViewById(R.id.locationViewerMap);
        mapView.onCreate(savedInstanceState);


        Intent intent = getIntent();

        final LocationWithSignal lws = (LocationWithSignal)intent.getSerializableExtra("lwsObject");


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                LatLng latLng = new LatLng(lws.GetLatitide(), lws.GetLongitude());

                googleMap.clear();
                googleMap.addMarker(
                            new MarkerOptions()
                                    .position(latLng)
                                    .title("Location with Signal")
                                    .snippet("Strength: " + lws.GetSignalStrength()
                                              + " Bar(s).\nLatitude: " + lws.GetLatitide()
                                              + "\nLongitude: " + lws.GetLongitude()
                                            )

                                    );

                googleMap.addCircle(new CircleOptions()
                                    .center(latLng)
                                    .radius(50)
                                    .fillColor(Color.argb(127, 0, 0, 255))

                );

                googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(latLng, 14)
                );


                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        LinearLayout info = new LinearLayout(getBaseContext());
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getBaseContext());
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(getBaseContext());
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());


                        info.addView(title);
                        info.addView(snippet);

                        return info;
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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
