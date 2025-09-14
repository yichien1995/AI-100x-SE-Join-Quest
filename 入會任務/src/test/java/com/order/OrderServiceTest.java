package com.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    
    private OrderService orderService;
    private Order order;
    
    @BeforeEach
    void setUp() {
        orderService = new OrderService();
        order = new Order();
    }
    
    @Test
    void testSingleProductWithoutPromotions() {
        // Given
        Product product = new Product();
        product.setName("T-shirt");
        product.setQuantity(1);
        product.setUnitPrice(500);
        order.addProduct(product);
        
        // When
        OrderSummary summary = orderService.calculateOrder(order);
        
        // Then
        assertNotNull(summary);
        // 驗證總金額應該是500，但OrderService還沒有實作，所以會失敗
        assertEquals(500, summary.getTotalAmount());
        // 驗證商品數量應該是1
        assertEquals(1, summary.getProducts().size());
        assertEquals("T-shirt", summary.getProducts().get(0).getName());
        assertEquals(1, summary.getProducts().get(0).getQuantity());
    }
    
    @Test
    void testThresholdDiscountPromotion() {
        // Given - 設定門檻折扣優惠（1000以上折扣100）
        PromotionConfig promotion = new PromotionConfig(1000, 100);
        orderService.setThresholdPromotion(promotion);
        
        // When - 訂單總金額1600（應該有折扣）
        Product product1 = new Product();
        product1.setName("T-shirt");
        product1.setQuantity(2);
        product1.setUnitPrice(500);
        order.addProduct(product1);
        
        Product product2 = new Product();
        product2.setName("褲子");
        product2.setQuantity(1);
        product2.setUnitPrice(600);
        order.addProduct(product2);
        
        OrderSummary summary = orderService.calculateOrder(order);
        
        // Then - 驗證折扣邏輯
        assertNotNull(summary);
        // 原始金額應該是1600
        assertEquals(1600, summary.getOriginalAmount());
        // 折扣應該是100
        assertEquals(100, summary.getDiscount());
        // 總金額應該是1500
        assertEquals(1500, summary.getTotalAmount());
    }
    
    @Test
    void testBuyOneGetOneForCosmetics() {
        // Given - 設定買一送一優惠
        BuyOneGetOneConfig promotion = new BuyOneGetOneConfig("cosmetics");
        orderService.setBuyOneGetOnePromotion(promotion);
        
        // When - 訂單包含化妝品
        Product product1 = new Product();
        product1.setName("口紅");
        product1.setCategory("cosmetics");
        product1.setQuantity(1);
        product1.setUnitPrice(300);
        order.addProduct(product1);
        
        Product product2 = new Product();
        product2.setName("粉底液");
        product2.setCategory("cosmetics");
        product2.setQuantity(1);
        product2.setUnitPrice(400);
        order.addProduct(product2);
        
        OrderSummary summary = orderService.calculateOrder(order);
        
        // Then - 驗證買一送一邏輯
        assertNotNull(summary);
        // 總金額應該是700（300+400，但買一送一後數量翻倍）
        assertEquals(700, summary.getTotalAmount());
        // 驗證商品數量翻倍
        assertEquals(2, summary.getProducts().size());
        
        // 口紅應該有2個（買1送1）
        Product lipstick = summary.getProducts().stream()
            .filter(p -> p.getName().equals("口紅"))
            .findFirst()
            .orElse(null);
        assertNotNull(lipstick);
        assertEquals(2, lipstick.getQuantity());
        
        // 粉底液應該有2個（買1送1）
        Product foundation = summary.getProducts().stream()
            .filter(p -> p.getName().equals("粉底液"))
            .findFirst()
            .orElse(null);
        assertNotNull(foundation);
        assertEquals(2, foundation.getQuantity());
    }
    
    @Test
    void testBuyOneGetOneForCosmeticsSameProductTwice() {
        // Given - 設定買一送一優惠
        BuyOneGetOneConfig promotion = new BuyOneGetOneConfig("cosmetics");
        orderService.setBuyOneGetOnePromotion(promotion);
        
        // When - 訂單包含同一個化妝品買兩次
        Product product = new Product();
        product.setName("口紅");
        product.setCategory("cosmetics");
        product.setQuantity(2);
        product.setUnitPrice(300);
        order.addProduct(product);
        
        OrderSummary summary = orderService.calculateOrder(order);
        
        // Then - 驗證買一送一邏輯
        assertNotNull(summary);
        // 總金額應該是600（2×300，但買一送一後數量翻倍）
        assertEquals(600, summary.getTotalAmount());
        // 驗證商品數量翻倍
        assertEquals(1, summary.getProducts().size());
        
        // 口紅應該有3個（買2送1）
        Product lipstick = summary.getProducts().get(0);
        assertEquals("口紅", lipstick.getName());
        assertEquals(3, lipstick.getQuantity());
    }
    
    @Test
    void testBuyOneGetOneForCosmeticsMixedCategories() {
        // Given - 設定買一送一優惠
        BuyOneGetOneConfig promotion = new BuyOneGetOneConfig("cosmetics");
        orderService.setBuyOneGetOnePromotion(promotion);
        
        // When - 訂單包含混合類別商品
        Product product1 = new Product();
        product1.setName("襪子");
        product1.setCategory("apparel");
        product1.setQuantity(1);
        product1.setUnitPrice(100);
        order.addProduct(product1);
        
        Product product2 = new Product();
        product2.setName("口紅");
        product2.setCategory("cosmetics");
        product2.setQuantity(1);
        product2.setUnitPrice(300);
        order.addProduct(product2);
        
        OrderSummary summary = orderService.calculateOrder(order);
        
        // Then - 驗證買一送一邏輯
        assertNotNull(summary);
        // 總金額應該是400（100+300，但化妝品買一送一）
        assertEquals(400, summary.getTotalAmount());
        // 驗證商品數量
        assertEquals(2, summary.getProducts().size());
        
        // 襪子應該有1個（非化妝品，沒有優惠）
        Product socks = summary.getProducts().stream()
            .filter(p -> p.getName().equals("襪子"))
            .findFirst()
            .orElse(null);
        assertNotNull(socks);
        assertEquals(1, socks.getQuantity());
        
        // 口紅應該有2個（化妝品，買1送1）
        Product lipstick = summary.getProducts().stream()
            .filter(p -> p.getName().equals("口紅"))
            .findFirst()
            .orElse(null);
        assertNotNull(lipstick);
        assertEquals(2, lipstick.getQuantity());
    }
    
    @Test
    void testMultiplePromotionsStacked() {
        // Given - 設定門檻折扣優惠（1000以上折扣100）和買一送一優惠
        PromotionConfig thresholdPromotion = new PromotionConfig(1000, 100);
        orderService.setThresholdPromotion(thresholdPromotion);
        
        BuyOneGetOneConfig buyOneGetOnePromotion = new BuyOneGetOneConfig("cosmetics");
        orderService.setBuyOneGetOnePromotion(buyOneGetOnePromotion);
        
        // When - 訂單包含服裝和化妝品，總金額1800
        Product product1 = new Product();
        product1.setName("T-shirt");
        product1.setCategory("apparel");
        product1.setQuantity(3);
        product1.setUnitPrice(500);
        order.addProduct(product1);
        
        Product product2 = new Product();
        product2.setName("口紅");
        product2.setCategory("cosmetics");
        product2.setQuantity(1);
        product2.setUnitPrice(300);
        order.addProduct(product2);
        
        OrderSummary summary = orderService.calculateOrder(order);
        
        // Then - 驗證多個優惠同時使用
        assertNotNull(summary);
        // 原始金額應該是1800（3×500 + 1×300）
        assertEquals(1800, summary.getOriginalAmount());
        // 門檻折扣應該是100（因為1800 > 1000）
        assertEquals(100, summary.getDiscount());
        // 總金額應該是1700（1800 - 100）
        assertEquals(1700, summary.getTotalAmount());
        // 驗證商品數量
        assertEquals(2, summary.getProducts().size());
        
        // T-shirt應該有3個（非化妝品，沒有優惠）
        Product tshirt = summary.getProducts().stream()
            .filter(p -> p.getName().equals("T-shirt"))
            .findFirst()
            .orElse(null);
        assertNotNull(tshirt);
        assertEquals(3, tshirt.getQuantity());
        
        // 口紅應該有2個（化妝品，買1送1）
        Product lipstick = summary.getProducts().stream()
            .filter(p -> p.getName().equals("口紅"))
            .findFirst()
            .orElse(null);
        assertNotNull(lipstick);
        assertEquals(2, lipstick.getQuantity());
    }
}
