package com.example.crud;

public class Product {
    private int id; // unique ID of the product in the table
    private String name;
    private String unit;
    private double price;
    private String expirationDate;
    private int quantity;
    private String imageUri;

    public Product() {}

    public Product(int id, String name, String unit, double price, String expirationDate, int quantity, String imageUri) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
