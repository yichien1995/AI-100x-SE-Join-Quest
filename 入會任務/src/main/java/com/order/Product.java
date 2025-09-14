package com.order;

public class Product {
    private String name;
    private String category;
    private int quantity;
    private int unitPrice;
    
    public Product() {}
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public int getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
}

