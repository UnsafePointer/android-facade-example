package com.ruenzuo.weatherapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ruenzuo.weatherapp.R;
import com.ruenzuo.weatherapp.models.Station;

/**
 * Created by ruenzuo on 08/05/14.
 */
public class StationsAdapter extends ArrayAdapter<Station> {

    private int resourceId;

    public StationsAdapter(Context context, int resource) {
        super(context, resource);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            convertView = inflater.inflate(resourceId, null);
        }
        Station station = getItem(position);
        TextView txtViewStationName = (TextView) convertView.findViewById(R.id.txtViewStationName);
        txtViewStationName.setText(station.getName());
        return convertView;
    }

}