package com.ruenzuo.weatherapp.fragments;

import android.app.ListFragment;
import android.os.Bundle;

import com.ruenzuo.weatherapp.R;
import com.ruenzuo.weatherapp.adapters.StationDataAdapter;
import com.ruenzuo.weatherapp.models.StationData;

import java.util.ArrayList;

/**
 * Created by ruenzuo on 10/05/14.
 */
public class StationDataListFragment extends ListFragment {

    private StationData stationData;
    private static final String DEGREE  = "\u00b0";

    public StationData getStationData() {
        return stationData;
    }

    public void setStationData(StationData stationData) {
        this.stationData = stationData;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<String> values = new ArrayList<String>();
        values.add(String.format("%.2f ", stationData.getTemperature()) + DEGREE + "K");
        values.add(String.format("%.2f hpa", stationData.getPressure()));
        values.add(String.format("%.2f %%", stationData.getHumidity()));
        StationDataAdapter adapter = new StationDataAdapter(getActivity(), R.layout.row_station_data);
        adapter.addAll(values);
        setListAdapter(adapter);
    }

}
