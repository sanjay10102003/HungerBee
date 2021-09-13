package com.food.hungerbee.ModelClasses;

public class LatLngModelClass {
    Double lat,lng;

    public LatLngModelClass(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLngModelClass() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
