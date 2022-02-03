package com.food.hungerbee.ModelClasses;

public class LatLngModelClass {
    String  lat,lng;

    public LatLngModelClass(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLngModelClass() {
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
