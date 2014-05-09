package com.ruenzuo.weatherapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruenzuo on 09/05/14.
 */
@Table(name = "Station")
public class Station extends Model {

    @Column(name = "name")
    @SerializedName("name")
    private String name;
    @Column(name = "data")
    @SerializedName("main")
    private StationData data;
    @Column(name = "cityName")
    private String cityName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StationData getData() {
        return data;
    }

    public void setData(StationData data) {
        this.data = data;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
