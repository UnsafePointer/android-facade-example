package com.ruenzuo.weatherapp.activities;

import android.app.Activity;
import android.os.Bundle;

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
        Country country = (Country) getIntent().getExtras().getSerializable("Country");
        CitiesListFragment fragment = new CitiesListFragment();
        fragment.setCountry(country);
        getFragmentManager().beginTransaction().replace(R.id.placeholder_container, fragment, "citiesListFragment").commit();
    }

    @Override
    public void onCitySeleted(City city) {

    }

}
