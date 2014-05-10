package com.ruenzuo.weatherapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ruenzuo.weatherapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ruenzuo on 10/05/14.
 */
public class StationDataAdapter extends ArrayAdapter<String> {

    private int resourceId;
    private ArrayList<String> titles;

    public StationDataAdapter(Context context, int resource) {
        super(context, resource);
        resourceId = resource;
        titles =  new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.titles)));
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            convertView = inflater.inflate(resourceId, null);
        }
        String value = getItem(position);
        TextView txtViewValueTitle = (TextView) convertView.findViewById(R.id.txtViewValueTitle);
        txtViewValueTitle.setText(titles.get(position));
        TextView txtViewValue = (TextView) convertView.findViewById(R.id.txtViewValue);
        txtViewValue.setText(value);
        return convertView;
    }

}
