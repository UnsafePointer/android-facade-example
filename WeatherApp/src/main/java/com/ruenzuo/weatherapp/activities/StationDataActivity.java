package com.ruenzuo.weatherapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ruenzuo.weatherapp.R;
import com.ruenzuo.weatherapp.fragments.StationDataListFragment;
import com.ruenzuo.weatherapp.models.StationData;

/**
 * Created by ruenzuo on 10/05/14.
 */
public class StationDataActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_data);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        StationData stationData = (StationData) getIntent().getExtras().getSerializable("StationData");
        StationDataListFragment fragment = new StationDataListFragment();
        fragment.setStationData(stationData);
        getFragmentManager().beginTransaction().replace(R.id.placeholder_container, fragment, "stationDataListFragment").commit();
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
