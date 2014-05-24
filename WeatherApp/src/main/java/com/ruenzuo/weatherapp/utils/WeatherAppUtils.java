package com.ruenzuo.weatherapp.utils;

import com.ruenzuo.weatherapp.models.Country;

import java.util.ArrayList;

/**
 * Created by ruenzuo on 24/05/14.
 */
public class WeatherAppUtils {

    public static ArrayList<String> getIdentifiers(Country[] countries) {
        ArrayList<String> identifiers = new ArrayList<String>();
        for (Country country : countries) {
            identifiers.add(country.getCode());
        }
        return identifiers;
    }

}
