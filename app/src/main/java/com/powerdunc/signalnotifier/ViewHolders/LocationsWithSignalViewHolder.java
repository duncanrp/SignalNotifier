package com.powerdunc.signalnotifier.ViewHolders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.MapView;
import com.powerdunc.signalnotifier.R;

public class LocationsWithSignalViewHolder extends RecyclerView.ViewHolder {

    public TextView locationTxt;
    public TextView notifiedDateTxt;

    public MapView mapView;

    public RelativeLayout viewButtonLayout;
    public LinearLayout strengthIndicator;

    public LocationsWithSignalViewHolder(@NonNull View itemView) {
        super(itemView);

        locationTxt = itemView.findViewById(R.id.lws_item_positionTxt);
        notifiedDateTxt = itemView.findViewById(R.id.lws_item_discoveredtimeTxt);
        viewButtonLayout = itemView.findViewById(R.id.viewButtonLayout);
        strengthIndicator = itemView.findViewById(R.id.strengthIndicator);

        mapView = itemView.findViewById(R.id.lws_item_mapView);
        mapView.setEnabled(false);
        mapView.setFocusable(false);
    }
}
