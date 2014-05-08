package com.ruenzuo.weatherapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ruenzuo on 07/05/14.
 */
@Table(name = "Country")
public class Country extends Model {

    @Column(name = "name")
    @SerializedName("countryName")
    private String name;
    @Column(name = "code")
    @SerializedName("countryCode")
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
