package com.order;

/**
 * 買一送一優惠配置類別
 */
public class BuyOneGetOneConfig {
    
    private String category;
    private boolean active;
    
    public BuyOneGetOneConfig() {
        this.active = false;
    }
    
    public BuyOneGetOneConfig(String category) {
        this.category = category;
        this.active = true;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}
