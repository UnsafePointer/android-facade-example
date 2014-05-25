package com.ruenzuo.weatherapp.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.ruenzuo.weatherapp.definitions.CitiesFetcher;
import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.definitions.StationsFetcher;
import com.ruenzuo.weatherapp.helpers.MemoryCacheHelper;
import com.ruenzuo.weatherapp.helpers.DatabaseHelper;
import com.ruenzuo.weatherapp.helpers.NetworkingHelper;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Country;
import com.ruenzuo.weatherapp.models.Station;
import com.ruenzuo.weatherapp.services.SyncService;
import com.ruenzuo.weatherapp.utils.WeatherAppUtils;

import java.util.Arrays;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public enum WeatherAppManager implements CountriesFetcher, CitiesFetcher, StationsFetcher {

    INSTANCE;

    private MemoryCacheHelper memoryCacheHelper = new MemoryCacheHelper();
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private NetworkingHelper networkingHelper = new NetworkingHelper();
    private boolean isSyncing = false;
    private Context context;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            isSyncing = false;
            context.unregisterReceiver(receiver);
        }

    };

    public void setContext(Context context) {
        this.context = context;
        networkingHelper.setupCache(context);
    }

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
                                return Task.whenAll(tasks).continueWith(new Continuation<Void, Country[]>() {
                                    @Override
                                    public Country[] then(Task<Void> task) throws Exception {
                                        if (storeCountriesInMemory.isFaulted()) {
                                            throw storeCountriesInMemory.getError();
                                        }
                                        if (storeCountriesInDatabase.isFaulted()) {
                                            throw storeCountriesInDatabase.getError();
                                        }
                                        return countries;
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public Task<City[]> getCities(final Country country) {
        return memoryCacheHelper.getCities(country).continueWithTask(new Continuation<City[], Task<City[]>>() {
            @Override
            public Task<City[]> then(Task<City[]> task) throws Exception {
                if (!task.isFaulted()) {
                    Log.i("WeatherAppManager", "Cities retrieved from memory");
                    return task;
                }
                return databaseHelper.getCities(country).continueWithTask(new Continuation<City[], Task<City[]>>() {
                    @Override
                    public Task<City[]> then(Task<City[]> task) throws Exception {
                        if (!task.isFaulted()) {
                            final City[] cities = task.getResult();
                            return memoryCacheHelper.storeCities(cities).continueWith(new Continuation<Boolean, City[]>() {
                                @Override
                                public City[] then(Task<Boolean> task) throws Exception {
                                    if (task.isFaulted()) {
                                        throw task.getError();
                                    }
                                    Log.i("WeatherAppManager", "Cities retrieved from database");
                                    return cities;
                                }
                            });
                        }
                        return networkingHelper.getCities(country).continueWithTask(new Continuation<City[], Task<City[]>>() {
                            @Override
                            public Task<City[]> then(Task<City[]> task) throws Exception {
                                if (task.isFaulted()) {
                                    throw task.getError();
                                }
                                final City[] cities = task.getResult();
                                final Task<Boolean> storeCountriesInMemory = memoryCacheHelper.storeCities(cities);
                                final Task<Boolean> storeCountriesInDatabase = databaseHelper.storeCities(cities);
                                List<Task<Boolean>> tasks = Arrays.asList(storeCountriesInMemory, storeCountriesInDatabase);
                                return Task.whenAll(tasks).continueWith(new Continuation<Void, City[]>() {
                                    @Override
                                    public City[] then(Task<Void> task) throws Exception {
                                        if (storeCountriesInMemory.isFaulted()) {
                                            throw storeCountriesInMemory.getError();
                                        }
                                        if (storeCountriesInDatabase.isFaulted()) {
                                            throw storeCountriesInDatabase.getError();
                                        }
                                        Log.i("WeatherAppManager", "Cities retrieved from network");
                                        return cities;
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public Task<Station[]> getStations(final City city) {
        return memoryCacheHelper.getStations(city).continueWithTask(new Continuation<Station[], Task<Station[]>>() {
            @Override
            public Task<Station[]> then(Task<Station[]> task) throws Exception {
                if (!task.isFaulted()) {
                    Log.i("WeatherAppManager", "Stations retrieved from memory");
                    return task;
                }
                return databaseHelper.getStations(city).continueWithTask(new Continuation<Station[], Task<Station[]>>() {
                    @Override
                    public Task<Station[]> then(Task<Station[]> task) throws Exception {
                        if (!task.isFaulted()) {
                            final Station[] stations = task.getResult();
                            return memoryCacheHelper.storeStations(stations, city).continueWith(new Continuation<Boolean, Station[]>() {
                                @Override
                                public Station[] then(Task<Boolean> task) throws Exception {
                                    if (task.isFaulted()) {
                                        throw task.getError();
                                    }
                                    Log.i("WeatherAppManager", "Stations retrieved from database");
                                    return stations;
                                }
                            });
                        }
                        return networkingHelper.getStations(city).continueWithTask(new Continuation<Station[], Task<Station[]>>() {
                            @Override
                            public Task<Station[]> then(Task<Station[]> task) throws Exception {
                                if (task.isFaulted()) {
                                    throw task.getError();
                                }
                                final Station[] stations = task.getResult();
                                final Task<Boolean> storeCountriesInMemory = memoryCacheHelper.storeStations(stations, city);
                                final Task<Boolean> storeCountriesInDatabase = databaseHelper.storeStations(stations, city);
                                List<Task<Boolean>> tasks = Arrays.asList(storeCountriesInMemory, storeCountriesInDatabase);
                                return Task.whenAll(tasks).continueWith(new Continuation<Void, Station[]>() {
                                    @Override
                                    public Station[] then(Task<Void> task) throws Exception {
                                        if (storeCountriesInMemory.isFaulted()) {
                                            throw storeCountriesInMemory.getError();
                                        }
                                        if (storeCountriesInDatabase.isFaulted()) {
                                            throw storeCountriesInDatabase.getError();
                                        }
                                        Log.i("WeatherAppManager", "Stations retrieved from network");
                                        return stations;
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void startSyncService(Country[] countries) {
        if (isSyncing) {
            return;
        }
        context.registerReceiver(receiver, new IntentFilter(SyncService.SYNC_DONE));
        isSyncing = true;
        Intent intent = new Intent(context, SyncService.class);
        intent.putExtra("CountriesIdentifiers", WeatherAppUtils.getIdentifiers(countries));
        context.startService(intent);
    }

}
