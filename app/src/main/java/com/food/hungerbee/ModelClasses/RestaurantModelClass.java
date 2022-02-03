package com.food.hungerbee.ModelClasses;

public class RestaurantModelClass {
    String Name,PhoneNumber,Address,access,Profile,Distance;


    public RestaurantModelClass() {
    }

    public RestaurantModelClass(String name, String phoneNumber, String address, String access, String profile, String Distance) {
        Name = name;
        PhoneNumber = phoneNumber;
        Address = address;
        this.access = access;
        Profile = profile;
        this.Distance = Distance;
    }

    public RestaurantModelClass(String name, String phoneNumber, String address, String access, String profile) {
        Name = name;
        PhoneNumber = phoneNumber;
        Address = address;
        this.access = access;
        Profile = profile;
    }

    public RestaurantModelClass(RestaurantModelClass restaurantModelClass, String Distance) {
        Name = restaurantModelClass.Name;
        PhoneNumber = restaurantModelClass.PhoneNumber;
        Address = restaurantModelClass.Address;
        this.access = restaurantModelClass.access;
        Profile = restaurantModelClass.Profile;
        this.Distance = Distance;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }
}
