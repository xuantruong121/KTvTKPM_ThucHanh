package com.flashsale.orderservice.model;

import iuh.fit.cartpu.entity.CartItem;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable {
    private String orderId;
    private List<CartItem> items;
    private double totalAmount;
    private String status;

    public Order() {
    }

    public Order(List<CartItem> items, double totalAmount, String status) {
        this.orderId = UUID.randomUUID().toString();
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
