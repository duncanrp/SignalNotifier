package com.powerdunc.signalnotifier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.powerdunc.signalnotifier.Adapters.GenericListViewAdapter;
import com.powerdunc.signalnotifier.Controls.LocationsWithSignalRecyclerView;
import com.powerdunc.signalnotifier.DataAccess.LocationWithSignalDAC;
import com.powerdunc.signalnotifier.Models.LocationWithSignal;
import com.powerdunc.signalnotifier.ViewHolders.LocationsWithSignalViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity_LocationsWithSignal extends Fragment {

    SharedPreferences.Editor editor;
    LocationsWithSignalRecyclerView locationsWithSignalList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_locationswithsignal, container, false);


        locationsWithSignalList = (LocationsWithSignalRecyclerView) view.findViewById(R.id.locationsWithSignalList);
        locationsWithSignalList.setEmptyView(view.findViewById(R.id.lwsListEmptyView));
        locationsWithSignalList.setAdapter(GetAdapter(savedInstanceState));


        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        locationsWithSignalList.setLayoutManager(layoutManager);
        locationsWithSignalList.setHasFixedSize(true);



        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.lwsSwipeRefreshLayout);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                locationsWithSignalList.setAdapter(GetAdapter(savedInstanceState));
                locationsWithSignalList.notifyDataUpdated();
                pullToRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    GenericListViewAdapter<LocationWithSignal> GetAdapter(final Bundle bundle)
    {
        List<LocationWithSignal> signals =LocationWithSignalDAC.GetRemindersList(getContext());

        return new GenericListViewAdapter<LocationWithSignal>(getContext(), signals){

            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                View view =  LayoutInflater.from(getContext()).inflate(R.layout.location_with_signal_item, parent, false);

                final View finalView = view;

                LocationsWithSignalViewHolder viewHolder = new LocationsWithSignalViewHolder(finalView);
                return viewHolder;
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, final LocationWithSignal val) {


                final LocationsWithSignalViewHolder viewholder = (LocationsWithSignalViewHolder)holder;

                CharSequence locTxt = String.format("%.2f, %.2f", val.GetLatitide(), val.GetLongitude());
                CharSequence notifiedDtTxt = String.format("%s", new SimpleDateFormat("HH:mm  dd/MM/YYYY").format(val.GetCreatedDate()));

                viewholder.locationTxt.setText(locTxt);
                viewholder.notifiedDateTxt.setText(notifiedDtTxt);
                viewholder.strengthIndicator.setBackgroundColor(Color.parseColor(val.GetStrengthColor()));
                viewholder.mapView.onCreate(bundle);
                viewholder.mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        LatLng latLng = new LatLng(val.GetLatitide(), val.GetLongitude());

                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(val.GetLatitide(), val.GetLongitude()), 14));
                        googleMap.getUiSettings().setAllGesturesEnabled(false);
                        viewholder.mapView.onResume();
                    }
                });


                viewholder.viewButtonLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getContext(), LocationView.class);

                        intent.putExtra("lwsObject", val);

                        startActivity(intent);
                    }
                });
            }
        };
    }

}
