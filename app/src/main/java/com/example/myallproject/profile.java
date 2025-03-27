package com.example.myallproject;

public class profile {
    private int phoneNUM;
    private String location;
    private boolean cards;
    private boolean cuopons;

    public profile(int phoneNUM, String location, boolean cards, boolean cuopons) {
        this.phoneNUM = phoneNUM;
        this.location = location;
        this.cards = cards;
        this.cuopons = cuopons;
    }

    public int getPhoneNUM() {
        return phoneNUM;
    }

    public void setPhoneNUM(int phoneNUM) {
        this.phoneNUM = phoneNUM;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isCards() {
        return cards;
    }

    public void setCards(boolean cards) {
        this.cards = cards;
    }

    public boolean isCuopons() {
        return cuopons;
    }

    public void setCuopons(boolean cuopons) {
        this.cuopons = cuopons;
    }

    @Override
    public String toString() {
        return "profile{" +
                "phoneNUM=" + phoneNUM +
                ", location='" + location + '\'' +
                ", cards=" + cards +
                ", cuopons=" + cuopons +
                '}';
    }
}
