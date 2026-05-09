package com.flashsale.orderservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash("carts")
public class UserCart implements Serializable {
    @Id
    private String userId;
    private List<CartItem> items = new ArrayList<>();

    public UserCart() {}

    public UserCart(String userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
