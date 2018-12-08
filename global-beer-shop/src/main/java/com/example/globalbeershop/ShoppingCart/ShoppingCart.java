package com.example.globalbeershop.ShoppingCart;

import com.example.globalbeershop.BeerStocked.BeerStocked;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private final String sessionId;
    private List<CartItem> items;
    private double total;


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

    public CartItem getItem (long beerID){

        for(CartItem i : items) if(i.isSameBeer(beerID)) return i;
        return null;
    }


    public boolean itemAlreadyInCart (long beerID){

        for(CartItem i : items) if(i.isSameBeer(beerID)) return true;
        return false;

    }

    /**
     * Adds given beer in given quantity to this shopping cart
     * (If the same type of beer is already in cart, increases current quantity by the quantity given)
     *
     * @param beerID ID of a BeerStocked to add to cart
     * @param quantity The quantity of beer to add to cart
     * @throws IllegalArgumentException if quantity < 1
     */
    public void addItem (long beerID, double price, int quantity){

        if(itemAlreadyInCart(beerID)){
            getItem(beerID).editQuantity(quantity);
        }
        else{
            if (quantity<1) throw new IllegalArgumentException("Can only add items by a quantity of 1 or more");
            items.add(new CartItem(beerID,price, quantity));
        }
        total+= price * quantity;
    }

    public void setItemQuantity (BeerStocked beer, int quantity){
    }

    public void editItemQuantity (BeerStocked beer, int quantity){
        getItem(beer.getId()).setQuantity(getItem(beer.getId()).getQuantity()+ quantity);
    }






}
