package com.example.globalbeershop.Order;

public class OrderItem {

    private long orderID;
    private long beerID;
    private String name;
    private Double price;
    private int quantity;


    public OrderItem(long orderID, long beerID, String name, Double price, int quantity) {
        this.orderID = orderID;
        this.beerID = beerID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public long getBeerID() {
        return beerID;
    }

    public void setBeerID(long beerID) {
        this.beerID = beerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
