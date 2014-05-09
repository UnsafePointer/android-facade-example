package com.ruenzuo.weatherapp.definitions;

import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Station;

import bolts.Task;

/**
 * Created by ruenzuo on 09/05/14.
 */
public interface StationsStorer {

    public Task<Boolean> storeStations(Station[] stations, City city);

}
