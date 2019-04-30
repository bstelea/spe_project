package com.example.globalbeershop.ShoppingCart;

import com.example.globalbeershop.BeerStocked.BeerStocked;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private final String sessionId;
    private List<CartItem> items;
    private double total;


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }



    @Override
    public String toString() {
        return "ShoppingCart{" +
                "sessionId=" + sessionId +
                ", items=" + items +
                ", total=" + total +
                '}';
    }

    public ShoppingCart(String sessionId) {

        this.sessionId = sessionId;
        this.items = new ArrayList<>();
        this.total = 0;
    }

    public String getUserID() {
        return sessionId;
    }

    public List<CartItem> getItems(){
        return items;
    }

    public void addItem (CartItem item) {
        items.add(item);
        total += item.getItem().getPrice() * item.getQuantity();
    }
}
