package iuh.fit.cartpu.entity;

import java.io.Serializable;
import java.util.List;

public class UserCart implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private List<CartItem> items;

    public UserCart() {}

    public UserCart(String userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
