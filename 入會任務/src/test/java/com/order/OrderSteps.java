package com.order;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.datatable.DataTable;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

public class OrderSteps {
    
    private OrderService orderService;
    private Order order;
    private OrderSummary orderSummary;
    
    @Given("no promotions are applied")
    public void no_promotions_are_applied() {
        // 設定沒有優惠的狀態
        orderService = new OrderService();
    }
    
    @Given("the threshold discount promotion is configured:")
    public void the_threshold_discount_promotion_is_configured(DataTable dataTable) {
        // 設定門檻折扣優惠
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> config = rows.get(0);
        int threshold = Integer.parseInt(config.get("threshold"));
        int discount = Integer.parseInt(config.get("discount"));
        
        // 優先使用雙十一優惠的OrderService，如果沒有則創建新的
        if (DoubleElevenOrderSteps.getOrderService() != null) {
            orderService = DoubleElevenOrderSteps.getOrderService();
        } else if (orderService == null) {
            orderService = new OrderService();
        }
        PromotionConfig promotion = new PromotionConfig(threshold, discount);
        orderService.setThresholdPromotion(promotion);
    }
    
    @Given("the buy one get one promotion for cosmetics is active")
    public void the_buy_one_get_one_promotion_for_cosmetics_is_active() {
        // 設定買一送一優惠
        // 優先使用雙十一優惠的OrderService，如果沒有則創建新的
        if (DoubleElevenOrderSteps.getOrderService() != null) {
            orderService = DoubleElevenOrderSteps.getOrderService();
        } else if (orderService == null) {
            orderService = new OrderService();
        }
        BuyOneGetOneConfig promotion = new BuyOneGetOneConfig("cosmetics");
        orderService.setBuyOneGetOnePromotion(promotion);
    }
    
    @When("a customer places an order with:")
    public void a_customer_places_an_order_with(DataTable dataTable) {
        // 建立訂單
        List<Map<String, String>> rows = dataTable.asMaps();
        order = new Order();
        for (Map<String, String> row : rows) {
            Product product = new Product();
            product.setName(row.get("productName"));
            if (row.containsKey("category")) {
                product.setCategory(row.get("category"));
            }
            product.setQuantity(Integer.parseInt(row.get("quantity")));
            product.setUnitPrice(Integer.parseInt(row.get("unitPrice")));
            order.addProduct(product);
        }
        
        // 檢查是否有雙十一優惠的OrderService
        if (DoubleElevenOrderSteps.getOrderService() != null) {
            orderService = DoubleElevenOrderSteps.getOrderService();
            // 使用雙十一優惠的計算方法
            orderSummary = orderService.calculateOrder(order);
        } else {
            // 使用order.feature的計算方法
            orderSummary = orderService.calculateOrderForOrderFeature(order);
        }
    }
    
    @Then("the order summary should be:")
    public void the_order_summary_should_be(DataTable dataTable) {
        // 驗證訂單摘要
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> expected = rows.get(0);
        
        if (expected.containsKey("totalAmount")) {
            int expectedTotal = Integer.parseInt(expected.get("totalAmount"));
            Assertions.assertEquals(expectedTotal, orderSummary.getTotalAmount());
        } else if (expected.containsKey("originalAmount") && expected.containsKey("discount") && expected.containsKey("totalAmount")) {
            int expectedOriginal = Integer.parseInt(expected.get("originalAmount"));
            int expectedDiscount = Integer.parseInt(expected.get("discount"));
            int expectedTotal = Integer.parseInt(expected.get("totalAmount"));
            
            Assertions.assertEquals(expectedOriginal, orderSummary.getOriginalAmount());
            Assertions.assertEquals(expectedDiscount, orderSummary.getDiscount());
            Assertions.assertEquals(expectedTotal, orderSummary.getTotalAmount());
        }
    }
    
    @And("the customer should receive:")
    public void the_customer_should_receive(DataTable dataTable) {
        // 驗證客戶收到的商品
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            int expectedQuantity = Integer.parseInt(row.get("quantity"));
            
            // 驗證商品數量和名稱
            boolean found = false;
            for (Product product : orderSummary.getProducts()) {
                if (product.getName().equals(productName)) {
                    Assertions.assertEquals(expectedQuantity, product.getQuantity());
                    found = true;
                    break;
                }
            }
            Assertions.assertTrue(found, "Product " + productName + " not found");
        }
    }
}
