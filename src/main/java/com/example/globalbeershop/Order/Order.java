package com.example.globalbeershop.Order;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private long orderID;
    private List<OrderItem> items;
    private String name;
    private String email;
    private String address;
    private String city;
    private String county;
    private String postcode;
    private Double total;

    public Order(long orderID, String name, String email, String address, String city, String county, String postcode) {
        this.orderID = orderID;
        this.items = new ArrayList<>();
        this.name = name;
        this.email = email;
        this.address = address;
        this.city = city;
        this.county = county;
        this.postcode = postcode;
        this.total = 0.0;
    }

    public void addItem (OrderItem item){
        items.add(item);
        total+=item.getQuantity()*item.getPrice();
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
