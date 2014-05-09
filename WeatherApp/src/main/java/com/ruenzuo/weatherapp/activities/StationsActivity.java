package com.ruenzuo.weatherapp.activities;

import android.app.Activity;
import android.os.Bundle;

import com.ruenzuo.weatherapp.R;
import com.ruenzuo.weatherapp.fragments.StationsListFragment;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Station;

/**
 * Created by ruenzuo on 09/05/14.
 */
public class StationsActivity extends Activity implements StationsListFragment.StationsListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        City city = (City) getIntent().getExtras().getSerializable("City");
        StationsListFragment fragment = new StationsListFragment();
        fragment.setCity(city);
        getFragmentManager().beginTransaction().replace(R.id.placeholder_container, fragment, "stationsListFragment").commit();
    }

    @Override
    public void onStationSeleted(Station station) {

    }

}
