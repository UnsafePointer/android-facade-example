package com.ruenzuo.weatherapp.definitions;

import com.ruenzuo.weatherapp.models.City;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public interface CitiesStorer {

    public Task<Boolean> storeCities(City[] cities);

}
