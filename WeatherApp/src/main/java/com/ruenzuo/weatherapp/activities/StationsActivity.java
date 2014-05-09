package com.ruenzuo.weatherapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        City city = (City) getIntent().getExtras().getSerializable("City");
        StationsListFragment fragment = new StationsListFragment();
        fragment.setCity(city);
        getFragmentManager().beginTransaction().replace(R.id.placeholder_container, fragment, "stationsListFragment").commit();
    }

    @Override
    public void onStationSeleted(Station station) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
