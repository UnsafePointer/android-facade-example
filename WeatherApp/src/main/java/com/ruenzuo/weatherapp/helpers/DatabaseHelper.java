package com.ruenzuo.weatherapp.helpers;

import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.definitions.CountriesStorer;
import com.ruenzuo.weatherapp.models.Country;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class DatabaseHelper implements CountriesFetcher, CountriesStorer {

    @Override
    public Task<Country[]> getCountries() {
        return null;
    }

    @Override
    public Task<Boolean> storeCountries(Country[] countries) {
        return null;
    }

}
