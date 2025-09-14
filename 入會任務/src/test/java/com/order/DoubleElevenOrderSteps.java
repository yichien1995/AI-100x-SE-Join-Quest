package com.order;

import io.cucumber.java.en.Given;

public class DoubleElevenOrderSteps {
    
    private static OrderService orderService;
    
    @Given("the double eleven promotion is active")
    public void the_double_eleven_promotion_is_active() {
        // 設定雙十一優惠
        orderService = new OrderService();
        DoubleElevenConfig promotion = new DoubleElevenConfig(true);
        orderService.setDoubleElevenPromotion(promotion);
    }
    
    public static OrderService getOrderService() {
        return orderService;
    }
}
