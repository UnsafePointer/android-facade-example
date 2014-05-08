package com.ruenzuo.weatherapp.helpers;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.definitions.CountriesStorer;
import com.ruenzuo.weatherapp.extensions.NotFoundInDatabaseException;
import com.ruenzuo.weatherapp.models.Country;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class DatabaseHelper implements CountriesFetcher, CountriesStorer {

    @Override
    public Task<Country[]> getCountries() {
        return Task.callInBackground(new Callable<Country[]>() {
            @Override
            public Country[] call() throws Exception {
                List<Country> countries;
                countries = new Select().from(Country.class).execute();
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
                    for (int i = 0; i < countries.length; i++) {
                        Country country = countries[i];
                        country.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                return true;
            }
        });
    }

}
