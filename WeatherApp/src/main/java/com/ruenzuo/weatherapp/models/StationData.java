package com.ruenzuo.weatherapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ruenzuo on 09/05/14.
 */
@Table(name = "StationData")
public class StationData extends Model implements Serializable {

    @Column(name = "temperature")
    @SerializedName("temp")
    private float temperature;
    @Column(name = "pressure")
    @SerializedName("pressure")
    private float pressure;
    @Column(name = "humidity")
    @SerializedName("humidity")
    private float humidity;

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

}
