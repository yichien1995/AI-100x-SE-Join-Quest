package com.order;

import java.util.List;

public class OrderSummary {
    private int totalAmount;
    private int originalAmount;
    private int discount;
    private List<Product> products;
    
    public OrderSummary() {}
    
    public int getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getOriginalAmount() {
        return originalAmount;
    }
    
    public void setOriginalAmount(int originalAmount) {
        this.originalAmount = originalAmount;
    }
    
    public int getDiscount() {
        return discount;
    }
    
    public void setDiscount(int discount) {
        this.discount = discount;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

