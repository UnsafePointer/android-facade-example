package com.ruenzuo.weatherapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ruenzuo.weatherapp.helpers.TranslatorHelper;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Station;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruenzuo on 24/05/14.
 */
public class SyncService extends IntentService {

    public static final String SYNC_DONE = "com.ruenzuo.weatherapp.service.receiver";
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

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<String> identifiers = intent.getExtras().getStringArrayList("CountriesIdentifiers");
        for (String countryIdentifier : identifiers) {
            List<City> fetchedCities = new Select().from(City.class).where("countryCode = ?", countryIdentifier).execute();
            if (!fetchedCities.isEmpty()) {
                continue;
            }
            City[] cities;
            try {
                cities = translatorHelper.translateCities(get("http://api.geonames.org/searchJSON?country=" + countryIdentifier + "&username=WeatherApp"));
            } catch (Exception e) {
                continue;
            }
            try {
                ActiveAndroid.beginTransaction();
                for (City city : cities) {
                    city.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } catch (Exception e) {
                continue;
            } finally {
                ActiveAndroid.endTransaction();
                Log.i("SyncService", countryIdentifier + " cities stored in database");
            }
            for (City city : cities) {
                List<Station> fetchedStations = new Select().from(Station.class).where("cityName = ?", city.getName()).execute();
                if (!fetchedStations.isEmpty()) {
                    continue;
                }
                Station[] stations;
                try {
                    stations = translatorHelper.translateStations(get("http://api.openweathermap.org/data/2.5/find?lat=" + city.getLatitude() + "&lon=" + city.getLongitude()));
                } catch (Exception e) {
                    continue;
                }
                try {
                    ActiveAndroid.beginTransaction();
                    for (Station station : stations) {
                        station.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } catch (Exception e) {
                    continue;
                } finally {
                    ActiveAndroid.endTransaction();
                    Log.i("SyncService", city.getName() + " stations stored in database");
                }
            }
            publishResults();
        }

    }

    private void publishResults() {
        Intent intent = new Intent(SYNC_DONE);
        sendBroadcast(intent);
    }

}
