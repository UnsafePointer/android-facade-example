package com.ruenzuo.weatherapp.helpers;

import android.content.Context;

import com.ruenzuo.weatherapp.definitions.CitiesFetcher;
import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.definitions.StationsFetcher;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Country;
import com.ruenzuo.weatherapp.models.Station;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class NetworkingHelper implements CountriesFetcher, CitiesFetcher, StationsFetcher {

    private OkHttpClient client = new OkHttpClient();
    private TranslatorHelper translatorHelper = new TranslatorHelper();

    public void setupCache(Context context) {
        try {
            File httpCacheDirectory = new File(context.getCacheDir().getAbsolutePath(), "HttpCache");
            HttpResponseCache cache = new HttpResponseCache(httpCacheDirectory, 20 * 1024);
            client.setResponseCache(cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public Task<City[]> getCities(final Country country) {
        return Task.callInBackground(new Callable<City[]>() {
            @Override
            public City[] call() throws Exception {
                return translatorHelper.translateCities(get("http://api.geonames.org/searchJSON?country=" + country.getCode() + "&username=WeatherApp"));
            }
        });
    }

    @Override
    public Task<Station[]> getStations(final City city) {
        return Task.callInBackground(new Callable<Station[]>() {
            @Override
            public Station[] call() throws Exception {
                return translatorHelper.translateStations(get("http://api.openweathermap.org/data/2.5/find?lat=" + city.getLatitude() + "&lon=" + city.getLongitude()));
            }
        });
    }

}
