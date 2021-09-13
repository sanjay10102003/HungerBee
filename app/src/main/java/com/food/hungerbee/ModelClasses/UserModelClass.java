package com.food.hungerbee.ModelClasses;

public class UserModelClass {
    String Name,PhoneNumber,Address,access;

    public UserModelClass() {
    }
    public UserModelClass(String name,  String phoneNumber, String address, String access) {
        this.Name = name;
        this.PhoneNumber = phoneNumber;
        this.Address = address;
        this.access = access;
    }


    public String  getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

}
