package com.example.globalbeershop.ShoppingCart;

import com.example.globalbeershop.BeerStocked.BeerStocked;

public class CartItem {


    private final BeerStocked item;
    private int quantity;

    public CartItem(BeerStocked item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void editQuantity(int quantity) {
        this.quantity += quantity;
    }


    public BeerStocked getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}
