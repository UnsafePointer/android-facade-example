package com.ruenzuo.weatherapp.helpers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ruenzuo.weatherapp.definitions.CitiesFetcher;
import com.ruenzuo.weatherapp.definitions.CitiesStorer;
import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.definitions.CountriesStorer;
import com.ruenzuo.weatherapp.extensions.NotFoundInMemoryException;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Country;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class MemoryCacheHelper implements CountriesFetcher, CountriesStorer, CitiesFetcher, CitiesStorer {

    private static final String MEMORY_CACHE_COUNTRIES_KEY = "MEMORY_CACHE_COUNTRIES_KEY";
    private static final String MEMORY_CACHE_CITIES_KEY = "MEMORY_CACHE_CITIES_KEY_";

    private LoadingCache<String, Object> cache = CacheBuilder.newBuilder().
            maximumSize(10).
            expireAfterWrite(10, TimeUnit.MINUTES).
            build(new CacheLoader<String, Object>() {

                @Override
                public Object load(String key) throws Exception {
                    throw new NotFoundInMemoryException("Value not found for key: " + key);
                }

            });

    @Override
    public Task<Country[]> getCountries() {
        return Task.callInBackground(new Callable<Country[]>() {
            @Override
            public Country[] call() throws Exception {
                Country[] countries = (Country[]) cache.get(MEMORY_CACHE_COUNTRIES_KEY);
                return countries;
            }
        });
    }

    @Override
    public Task<Boolean> storeCountries(final Country[] countries) {
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                cache.put(MEMORY_CACHE_COUNTRIES_KEY, countries);
                return true;
            }
        });
    }

    @Override
    public Task<City[]> getCities(final String countryCode) {
        return Task.callInBackground(new Callable<City[]>() {
            @Override
            public City[] call() throws Exception {
                City[] cities = (City[]) cache.get(MEMORY_CACHE_COUNTRIES_KEY + countryCode);
                return cities;
            }
        });
    }

    @Override
    public Task<Boolean> storeCities(final City[] cities) {
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                cache.put(MEMORY_CACHE_CITIES_KEY + cities[0].getCountryCode(), cities);
                return true;
            }
        });
    }

}
