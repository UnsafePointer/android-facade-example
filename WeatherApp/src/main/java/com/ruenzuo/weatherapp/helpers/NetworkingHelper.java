package com.ruenzuo.weatherapp.helpers;

import com.ruenzuo.weatherapp.definitions.CitiesFetcher;
import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Country;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class NetworkingHelper implements CountriesFetcher, CitiesFetcher {

    private OkHttpClient client = new OkHttpClient();
    private TranslatorHelper translatorHelper = new TranslatorHelper();

    private String get(String URLString) throws Exception {
        HttpURLConnection connection = client.open(new URL(URLString));
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return stringBuilder.toString();
    }

    @Override
    public Task<Country[]> getCountries() {
        return Task.callInBackground(new Callable<Country[]>() {
            @Override
            public Country[] call() throws Exception {
                return translatorHelper.translateCountries(get("http://api.geonames.org/countryInfoJSON?username=WeatherApp"));
            }
        });
    }

    @Override
    public Task<City[]> getCities(final String countryCode) {
        return Task.callInBackground(new Callable<City[]>() {
            @Override
            public City[] call() throws Exception {
                return translatorHelper.translateCities(get("http://api.geonames.org/searchJSON?country=" + countryCode + "&username=WeatherApp"));
            }
        });
    }

}
