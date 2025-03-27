package com.example.myallproject;

public class Meal {
    private String name;
    private double price;

    private String description;
    private String imgUrl ;

    public Meal(){

    }
    public Meal(String name, double price, String description, String imgUrl){
        this.name=name;
        this.description=description;
        this.imgUrl=imgUrl;
        this.price =price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
