package com.ruenzuo.weatherapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruenzuo on 09/05/14.
 */
@Table(name = "City")
public class City extends Model {

    @Column(name = "name")
    @SerializedName("name")
    private String name;
    @Column(name = "latitude")
    @SerializedName("lat")
    private String latitude;
    @Column(name = "longitude")
    @SerializedName("lng")
    private String longitude;
    @Column(name = "countryCode")
    @SerializedName("countryCode")
    private String countryCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
