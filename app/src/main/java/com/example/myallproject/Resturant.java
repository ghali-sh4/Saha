package com.example.myallproject;

public class Resturant {
    private String id;
    private String name;
    private String location;
    private String phone ;
    private String category;
    private  String photo;
    private String OID;
    private double latitude;
    private double longitude;

    public Resturant() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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


    public Resturant(String name, String location, String phone, String category, String photo, String OID, double latitude, double longitude) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.category = category;
        this.photo = photo;
        this.OID = OID;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return "Resturant{" +
                "name='" + name + '\'' +
                ", loction='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", category='" + category + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
