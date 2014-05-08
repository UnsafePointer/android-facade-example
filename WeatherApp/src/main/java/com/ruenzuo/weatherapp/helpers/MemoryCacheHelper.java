package com.ruenzuo.weatherapp.helpers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.definitions.CountriesStorer;
import com.ruenzuo.weatherapp.extensions.NotFoundInMemoryException;
import com.ruenzuo.weatherapp.models.Country;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class MemoryCacheHelper implements CountriesFetcher, CountriesStorer {

    private static final String MEMORY_CACHE_COUNTRIES_KEY = "MEMORY_CACHE_COUNTRIES_KEY";

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

}
