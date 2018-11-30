package com.example.globalbeershop.ShoppingCart;

import com.example.globalbeershop.BeerStocked.BeerStocked;

public class CartItem {


    private final long itemID;
    private int quantity;

    public CartItem(long id, int quantity) {
        this.itemID = id;
        this.quantity = quantity;
    }

    public long getItemID() {
        return itemID;
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

    public boolean isSameBeer (long beerID){
        return itemID == beerID;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "itemID=" + itemID +
                ", quantity=" + quantity +
                '}';
    }

}
