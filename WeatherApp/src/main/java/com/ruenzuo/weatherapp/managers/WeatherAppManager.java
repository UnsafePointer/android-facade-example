package com.ruenzuo.weatherapp.managers;

import android.content.Context;

import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.helpers.MemoryCacheHelper;
import com.ruenzuo.weatherapp.helpers.DatabaseHelper;
import com.ruenzuo.weatherapp.helpers.NetworkingHelper;
import com.ruenzuo.weatherapp.models.Country;

import java.util.Arrays;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public enum WeatherAppManager implements CountriesFetcher {

    INSTANCE;

    private MemoryCacheHelper memoryCacheHelper = new MemoryCacheHelper();
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private NetworkingHelper networkingHelper = new NetworkingHelper();

    public Task<Country[]> getCountries() {
        return memoryCacheHelper.getCountries().continueWithTask(new Continuation<Country[], Task<Country[]>>() {
            @Override
            public Task<Country[]> then(Task<Country[]> task) throws Exception {
                if (!task.isFaulted()) {
                    return task;
                }
                return databaseHelper.getCountries().continueWithTask(new Continuation<Country[], Task<Country[]>>() {
                    @Override
                    public Task<Country[]> then(Task<Country[]> task) throws Exception {
                        if (!task.isFaulted()) {
                            return task.continueWithTask(new Continuation<Country[], Task<Country[]>>() {
                                @Override
                                public Task<Country[]> then(Task<Country[]> task) throws Exception {
                                    final Country[] countries = task.getResult();
                                    return memoryCacheHelper.storeCountries(countries).continueWith(new Continuation<Boolean, Country[]>() {
                                        @Override
                                        public Country[] then(Task<Boolean> task) throws Exception {
                                            if (task.isFaulted()) {
                                                throw task.getError();
                                            }
                                            return countries;
                                        }
                                    });
                                }
                            });
                        }
                        return networkingHelper.getCountries().continueWithTask(new Continuation<Country[], Task<Country[]>>() {
                            @Override
                            public Task<Country[]> then(Task<Country[]> task) throws Exception {
                                if (task.isFaulted()) {
                                    throw task.getError();
                                }
                                final Country[] countries = task.getResult();
                                final Task<Boolean> storeCountriesInMemory = memoryCacheHelper.storeCountries(countries);
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
                });
            }
        });
    }

}
