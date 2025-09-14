package com.order;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<Product> products;
    
    public Order() {
        this.products = new ArrayList<>();
    }
    
    public void addProduct(Product product) {
        // TODO: 實作 - 添加商品到訂單
        products.add(product);
    }
    
    public List<Product> getProducts() {
        return products;
    }
}

