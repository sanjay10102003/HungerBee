package com.food.hungerbee.ModelClasses;

public class CartModelClass {
    String FoodName,FoodPrice,FoodId,FoodQuantity,UserId;

    public CartModelClass() {
    }

    public CartModelClass(String foodName, String foodPrice, String foodId,String FoodQuantity,String UserId) {
        this.FoodName = foodName;
        this.FoodPrice = foodPrice;
        this.FoodId = foodId;
        this.FoodQuantity = FoodQuantity;
        this.UserId = UserId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        this.FoodName = foodName;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.FoodPrice = foodPrice;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        this.FoodId = foodId;
    }

    public String getFoodQuantity() {
        return FoodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.FoodQuantity = foodQuantity;
    }
}
