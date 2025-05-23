
package com.example.lostfoundapp;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class LocationInfo {

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "location_name")
    private String locationName;

    public LocationInfo(double latitude, double longitude, String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
    }

    public LocationInfo() {
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.locationName = "";
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void print() {
        System.out.println("Location `" + this.locationName + "` lon:" + this.longitude + " lat:" + this.latitude);
    }
}