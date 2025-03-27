package com.example.myallproject;

import java.util.ArrayList;

public class CartManager {
    private static final ArrayList<Meal> cartList = new ArrayList<>();

    public static void addToCart(Meal meal) {
        cartList.add(meal);
    }

    public static ArrayList<Meal> getCartItems() {
        return cartList;
    }

    public static void clearCart() {
        cartList.clear();
    }
}