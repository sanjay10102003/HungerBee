package com.food.hungerbee.ModelClasses;

public class CategoriesModelClass {
    String Name;
    int Img;

    public CategoriesModelClass(int img, String name) {
        this.Img = img;
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getImg() {
        return Img;
    }

    public void setImg(int img) {
        Img = img;
    }
}
