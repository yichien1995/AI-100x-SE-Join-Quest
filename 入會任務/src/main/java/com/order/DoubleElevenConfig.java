package com.order;

/**
 * 雙十一優惠配置類別
 */
public class DoubleElevenConfig {
    
    private boolean active;
    private int discountPercentage; // 折扣百分比，例如20表示20%折扣
    private int minQuantity; // 最小數量，例如10表示每10件一組
    
    public DoubleElevenConfig() {
        this.active = false;
        this.discountPercentage = 20; // 預設20%折扣
        this.minQuantity = 10; // 預設每10件一組
    }
    
    public DoubleElevenConfig(boolean active) {
        this.active = active;
        this.discountPercentage = 20;
        this.minQuantity = 10;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public int getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public int getMinQuantity() {
        return minQuantity;
    }
    
    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }
}
