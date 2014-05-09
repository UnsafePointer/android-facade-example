package com.ruenzuo.weatherapp.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class TranslatorHelper {

    private Gson gson = new GsonBuilder().create();
    private JsonParser jsonParser = new JsonParser();

    public Country[] translateCountries(String payload) throws Exception {
        JsonObject response = (JsonObject) jsonParser.parse(payload);
        JsonArray geonames = (JsonArray) response.get("geonames");
        return gson.fromJson(geonames, Country[].class);
    }

    public City[] translateCities(String payload) throws Exception {
        JsonObject response = (JsonObject) jsonParser.parse(payload);
        JsonArray geonames = (JsonArray) response.get("geonames");
        return gson.fromJson(geonames, City[].class);
    }

}
