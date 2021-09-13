package com.food.hungerbee.ModelClasses;

public class FoodModelClass {
    String imageUrl,ItemId,ItemName,ItemPrice,Category,UserId;
    public FoodModelClass() {
    }

    public FoodModelClass(String imageUrl, String ItemId, String itemName, String itemPrice, String category, String UserId) {
        this.imageUrl = imageUrl;
        this.ItemId = ItemId;
        this.ItemName = itemName;
        this.ItemPrice = itemPrice;
        this.Category = category;
        this.UserId = UserId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUri(String imageUrl) {
        imageUrl = imageUrl;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
