package com.ruenzuo.weatherapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.ruenzuo.weatherapp.R;
import com.ruenzuo.weatherapp.fragments.CountriesListFragment;
import com.ruenzuo.weatherapp.managers.WeatherAppManager;
import com.ruenzuo.weatherapp.models.Country;

public class CountriesActivity extends Activity implements CountriesListFragment.CountriesListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
    }

    @Override
    public void onCountrySelected(Country country) {

    }

}
