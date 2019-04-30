package com.powerdunc.signalnotifier.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.powerdunc.signalnotifier.R;

import java.util.List;

public class SimpleSpinnerAdapter extends BaseAdapter {

    Context context;
    List<String> values;
    LayoutInflater inflater;

    public SimpleSpinnerAdapter(Context context, List<String> values) {
        this.context = context;
        this.values = values;

        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public String getItem(int i) {
        return values.get(i);
    }

    public int getPosition(String value)
    {
        for(int i = 0; i < values.size(); i++)
        {
            String val = values.get(i);

            if(val.equals(value))
                return i;
        }

        return -1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_item, null);
        TextView names = (TextView) view.findViewById(R.id.textView);

        names.setText(values.get(i));

        return view;
    }

}
