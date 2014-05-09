package com.ruenzuo.weatherapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ruenzuo.weatherapp.R;
import com.ruenzuo.weatherapp.fragments.CitiesListFragment;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Country;

/**
 * Created by ruenzuo on 09/05/14.
 */
public class CitiesActivity extends Activity implements CitiesListFragment.CitiesListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Country country = (Country) getIntent().getExtras().getSerializable("Country");
        CitiesListFragment fragment = new CitiesListFragment();
        fragment.setCountry(country);
        getFragmentManager().beginTransaction().replace(R.id.placeholder_container, fragment, "citiesListFragment").commit();
    }

    @Override
    public void onCitySeleted(City city) {
        Intent intent = new Intent(this, StationsActivity.class);
        intent.putExtra("City", city);
        startActivity(intent);
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
