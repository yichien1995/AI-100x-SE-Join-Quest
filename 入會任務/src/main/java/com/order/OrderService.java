package com.order;

import java.util.List;
import java.util.ArrayList;

/**
 * 訂單服務類別，負責計算訂單總金額和應用各種優惠
 */
public class OrderService {
    
    private static final String COSMETICS_CATEGORY = "cosmetics";
    private static final int MAX_FREE_QUANTITY = 1;
    private boolean useDoubleElevenBuyOneGetOne = false;
    
    private PromotionConfig thresholdPromotion;
    private BuyOneGetOneConfig buyOneGetOnePromotion;
    private DoubleElevenConfig doubleElevenPromotion;
    
    public OrderService() {
        this.thresholdPromotion = null;
        this.buyOneGetOnePromotion = null;
        this.doubleElevenPromotion = null;
    }
    
    public void setThresholdPromotion(PromotionConfig promotion) {
        this.thresholdPromotion = promotion;
    }
    
    public void setBuyOneGetOnePromotion(BuyOneGetOneConfig promotion) {
        this.buyOneGetOnePromotion = promotion;
    }
    
    public void setDoubleElevenPromotion(DoubleElevenConfig promotion) {
        this.doubleElevenPromotion = promotion;
        this.useDoubleElevenBuyOneGetOne = true; // 啟用雙十一的買一送一邏輯
    }
    
    /**
     * 計算訂單總金額，應用所有相關優惠
     * 
     * @param order 訂單
     * @return 訂單摘要，包含原始金額、折扣、總金額和最終商品列表
     */
    public OrderSummary calculateOrder(Order order) {
        OrderSummary summary = new OrderSummary();
        
        // 計算原始金額
        int originalAmount = calculateOriginalAmount(order);
        
        // 先應用買一送一優惠（影響商品數量）
        List<Product> productsAfterBuyOneGetOne = applyBuyOneGetOnePromotion(order.getProducts());
        
        // 再應用雙十一優惠（基於買一送一後的商品數量）
        int doubleElevenAmount = applyDoubleElevenPromotion(productsAfterBuyOneGetOne);
        
        // 設定originalAmount為雙十一優惠後的金額（用於門檻折扣計算）
        summary.setOriginalAmount(doubleElevenAmount);
        
        // 計算門檻折扣（基於雙十一優惠後的金額）
        int discount = calculateThresholdDiscount(doubleElevenAmount);
        summary.setDiscount(discount);
        
        // 設定最終商品列表（買一送一後的商品）
        summary.setProducts(productsAfterBuyOneGetOne);
        
        // 最終總金額 = 雙十一優惠後金額 - 門檻折扣
        summary.setTotalAmount(doubleElevenAmount - discount);
        
        return summary;
    }
    
    /**
     * 計算訂單（僅適用於order.feature的買一送一邏輯）
     * 買一送一：客戶只支付原始數量的價格，但收到雙倍的數量
     */
    public OrderSummary calculateOrderForOrderFeature(Order order) {
        OrderSummary summary = new OrderSummary();
        
        // 計算原始金額（只計算原始數量的價格）
        int originalAmount = calculateOriginalAmount(order);
        
        // 應用買一送一優惠（影響商品數量，但不影響價格計算）
        List<Product> productsAfterBuyOneGetOne = applyBuyOneGetOnePromotion(order.getProducts());
        
        // 設定originalAmount為原始金額
        summary.setOriginalAmount(originalAmount);
        
        // 計算門檻折扣（基於原始金額）
        int discount = calculateThresholdDiscount(originalAmount);
        summary.setDiscount(discount);
        
        // 設定最終商品列表（買一送一後的商品）
        summary.setProducts(productsAfterBuyOneGetOne);
        
        // 最終總金額 = 原始金額 - 門檻折扣
        summary.setTotalAmount(originalAmount - discount);
        
        return summary;
    }
    
    /**
     * 計算訂單原始金額（未應用任何優惠）
     */
    private int calculateOriginalAmount(Order order) {
        return order.getProducts().stream()
            .mapToInt(product -> product.getQuantity() * product.getUnitPrice())
            .sum();
    }
    
    /**
     * 計算門檻折扣金額
     */
    private int calculateThresholdDiscount(int originalAmount) {
        if (thresholdPromotion != null && originalAmount >= thresholdPromotion.getThreshold()) {
            return thresholdPromotion.getDiscount();
        }
        return 0;
    }
    
    /**
     * 應用買一送一優惠到商品列表
     */
    private List<Product> applyBuyOneGetOnePromotion(List<Product> products) {
        if (buyOneGetOnePromotion == null || !buyOneGetOnePromotion.isActive()) {
            return new ArrayList<>(products);
        }
        
        return products.stream()
            .map(this::createProductWithPromotion)
            .toList();
    }
    
    /**
     * 為單個商品應用買一送一優惠
     * 化妝品類別：每買1個送1個，但最多送1個
     * 其他類別：無優惠
     */
    private Product createProductWithPromotion(Product product) {
        Product finalProduct = new Product();
        finalProduct.setName(product.getName());
        finalProduct.setCategory(product.getCategory());
        finalProduct.setUnitPrice(product.getUnitPrice());
        
        if (COSMETICS_CATEGORY.equals(product.getCategory())) {
            int originalQuantity = product.getQuantity();
            int freeQuantity;
            if (useDoubleElevenBuyOneGetOne) {
                // 雙十一模式：買超過一件則多送一件，相同品項最多送一樣
                freeQuantity = originalQuantity > 1 ? 1 : 0;
            } else {
                // order.feature模式：最多送1個
                freeQuantity = Math.min(originalQuantity, MAX_FREE_QUANTITY);
            }
            finalProduct.setQuantity(originalQuantity + freeQuantity);
        } else {
            finalProduct.setQuantity(product.getQuantity());
        }
        
        return finalProduct;
    }
    
    /**
     * 應用雙十一優惠到商品列表
     * 同一種商品每買10件，該10件商品享有20%折扣
     */
    private int applyDoubleElevenPromotion(List<Product> products) {
        if (doubleElevenPromotion == null || !doubleElevenPromotion.isActive()) {
            return calculateOriginalAmountFromProducts(products);
        }
        
        int totalAmount = 0;
        for (Product product : products) {
            totalAmount += calculateDoubleElevenPrice(product);
        }
        
        return totalAmount;
    }
    
    /**
     * 計算單個商品的雙十一優惠價格
     */
    private int calculateDoubleElevenPrice(Product product) {
        int quantity = product.getQuantity();
        int unitPrice = product.getUnitPrice();
        int minQuantity = doubleElevenPromotion.getMinQuantity();
        int discountPercentage = doubleElevenPromotion.getDiscountPercentage();
        
        int discountGroups = quantity / minQuantity; // 計算有幾組可以享受折扣
        int remainingQuantity = quantity % minQuantity; // 剩餘不足一組的數量
        
        // 折扣組的價格：每組10件 × 單價 × (100 - 折扣百分比)%
        int discountGroupPrice = discountGroups * minQuantity * unitPrice * (100 - discountPercentage) / 100;
        
        // 剩餘數量的原價
        int remainingPrice = remainingQuantity * unitPrice;
        
        return discountGroupPrice + remainingPrice;
    }
    
    /**
     * 從商品列表計算原始金額
     */
    private int calculateOriginalAmountFromProducts(List<Product> products) {
        return products.stream()
            .mapToInt(product -> product.getQuantity() * product.getUnitPrice())
            .sum();
    }
}
