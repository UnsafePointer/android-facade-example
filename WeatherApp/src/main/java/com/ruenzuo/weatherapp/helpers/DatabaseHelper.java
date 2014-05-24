package com.ruenzuo.weatherapp.helpers;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ruenzuo.weatherapp.definitions.CitiesFetcher;
import com.ruenzuo.weatherapp.definitions.CitiesStorer;
import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.definitions.CountriesStorer;
import com.ruenzuo.weatherapp.definitions.StationsFetcher;
import com.ruenzuo.weatherapp.definitions.StationsStorer;
import com.ruenzuo.weatherapp.extensions.NotFoundInDatabaseException;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Country;
import com.ruenzuo.weatherapp.models.Station;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class DatabaseHelper implements CountriesFetcher, CountriesStorer, CitiesFetcher, CitiesStorer, StationsFetcher, StationsStorer {

    @Override
    public Task<Country[]> getCountries() {
        return Task.callInBackground(new Callable<Country[]>() {
            @Override
            public Country[] call() throws Exception {
                List<Country> countries = new Select().from(Country.class).execute();
                if (countries.size() == 0) {
                    throw new NotFoundInDatabaseException("Countries not found on database");
                }
                return countries.toArray(new Country[countries.size()]);
            }
        });
    }

    @Override
    public Task<Boolean> storeCountries(final Country[] countries) {
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ActiveAndroid.beginTransaction();
                try {
                    for (Country country : countries) {
                        country.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } catch (Exception e) {
                    throw e;
                } finally {
                    ActiveAndroid.endTransaction();
                }
                return true;
            }
        });
    }

    @Override
    public Task<City[]> getCities(final Country country) {
        return Task.callInBackground(new Callable<City[]>() {
            @Override
            public City[] call() throws Exception {
                List<City> cities = new Select().from(City.class).where("countryCode = ?", country.getCode()).execute();
                if (cities.size() == 0) {
                    throw new NotFoundInDatabaseException("Cities not found on database");
                }
                return cities.toArray(new City[cities.size()]);
            }
        });
    }

    @Override
    public Task<Boolean> storeCities(final City[] cities) {
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ActiveAndroid.beginTransaction();
                try {
                    for (City city : cities) {
                        city.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } catch (Exception e) {
                  throw e;
                } finally {
                    ActiveAndroid.endTransaction();
                }
                return true;
            }
        });
    }

    @Override
    public Task<Station[]> getStations(final City city) {
        return Task.callInBackground(new Callable<Station[]>() {
            @Override
            public Station[] call() throws Exception {
                List<Station> stations = new Select().from(Station.class).where("cityName = ?", city.getName()).execute();
                if (stations.size() == 0) {
                    throw new NotFoundInDatabaseException("Cities not found on database");
                }
                return stations.toArray(new Station[stations.size()]);
            }
        });
    }

    @Override
    public Task<Boolean> storeStations(final Station[] stations, final City city) {
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ActiveAndroid.beginTransaction();
                try {
                    for (Station station : stations) {
                        station.setCityName(city.getName());
                        station.getData().save();
                        station.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } catch (Exception e) {
                    throw e;
                } finally {
                    ActiveAndroid.endTransaction();
                }
                return true;
            }
        });
    }

}
