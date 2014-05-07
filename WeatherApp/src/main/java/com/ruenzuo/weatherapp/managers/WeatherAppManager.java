package com.ruenzuo.weatherapp.managers;

import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.helpers.CacheHelper;
import com.ruenzuo.weatherapp.helpers.DatabaseHelper;
import com.ruenzuo.weatherapp.helpers.NetworkingHelper;
import com.ruenzuo.weatherapp.models.Country;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public enum WeatherAppManager implements CountriesFetcher {

    INSTANCE;

    private CacheHelper cacheHelper = new CacheHelper();
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private NetworkingHelper networkingHelper = new NetworkingHelper();

    public Task<Country[]> getCountries() {
        return cacheHelper.getCountries().continueWithTask(new Continuation<Country[], Task<Country[]>>() {
            @Override
            public Task<Country[]> then(Task<Country[]> task) throws Exception {
                if (!task.isFaulted()) {
                    return task;
                }
                return databaseHelper.getCountries();
            }
        }).continueWithTask(new Continuation<Country[], Task<Country[]>>() {
            @Override
            public Task<Country[]> then(Task<Country[]> task) throws Exception {
                if (!task.isFaulted()) {
                    return task;
                }
                return networkingHelper.getCountries();
            }
        }).continueWithTask(new Continuation<Country[], Task<Country[]>>() {
            @Override
            public Task<Country[]> then(Task<Country[]> task) throws Exception {
                if (!task.isFaulted()) {
                    return task;
                }
                return networkingHelper.getCountries();
            }
        }).continueWithTask(new Continuation<Country[], Task<Country[]>>() {
            @Override
            public Task<Country[]> then(Task<Country[]> task) throws Exception {
                if (task.isFaulted()) {
                    throw task.getError();
                }
                final Country[] countries = task.getResult();
                final Task<Boolean> storeCountriesInMemory = cacheHelper.storeCountries(countries);
                final Task<Boolean> storeCountriesInDatabase = databaseHelper.storeCountries(countries);
                List<Task<Boolean>> tasks = Arrays.asList(storeCountriesInMemory, storeCountriesInDatabase);
                return Task.whenAll(tasks).continueWithTask(new Continuation<Void, Task<Country[]>>() {
                    @Override
                    public Task<Country[]> then(Task<Void> task) throws Exception {
                        if (storeCountriesInMemory.isFaulted()) {
                            throw storeCountriesInMemory.getError();
                        }
                        if (storeCountriesInDatabase.isFaulted()) {
                            throw storeCountriesInDatabase.getError();
                        }
                        Task<Country[]>.TaskCompletionSource completionSource = Task.create();
                        completionSource.setResult(countries);
                        return completionSource.getTask();
                    }
                });
            }
        });
    }

}
