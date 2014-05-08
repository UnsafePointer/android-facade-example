package com.ruenzuo.weatherapp.helpers;

import com.ruenzuo.weatherapp.definitions.CountriesFetcher;
import com.ruenzuo.weatherapp.models.Country;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class NetworkingHelper implements CountriesFetcher {

    private OkHttpClient client = new OkHttpClient();
    private TranslatorHelper translatorHelper = new TranslatorHelper();

    @Override
    public Task<Country[]> getCountries() {
        return Task.callInBackground(new Callable<Country[]>() {
            @Override
            public Country[] call() throws Exception {
                HttpURLConnection connection = client.open(new URL("http://api.geonames.org/countryInfoJSON?username=WeatherApp"));
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return translatorHelper.translateCountries(stringBuilder.toString());
            }
        });
    }

}
