package com.ruenzuo.weatherapp.application;

import com.activeandroid.app.Application;
import com.ruenzuo.weatherapp.managers.WeatherAppManager;

/**
 * Created by ruenzuo on 24/05/14.
 */
public class WeatherAppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WeatherAppManager.INSTANCE.setContext(getApplicationContext());
    }

}
