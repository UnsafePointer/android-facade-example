package com.ruenzuo.weatherapp.definitions;

import com.ruenzuo.weatherapp.models.Country;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public interface CountriesStorer {

    public Task<Boolean> storeCountries(Country[] countries);

}
