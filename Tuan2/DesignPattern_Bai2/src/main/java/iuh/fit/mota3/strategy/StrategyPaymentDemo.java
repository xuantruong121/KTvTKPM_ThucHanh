package iuh.fit.mota3.strategy;

interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardStrategy implements PaymentStrategy {
    private String cardNumber;
    public CreditCardStrategy(String cardNumber) { this.cardNumber = cardNumber; }

    public void pay(double amount) {
        System.out.println("Thanh toán $" + amount + " bằng Thẻ tín dụng (" + cardNumber + ")");
    }
}

class PayPalStrategy implements PaymentStrategy {
    private String email;
    public PayPalStrategy(String email) { this.email = email; }

    public void pay(double amount) {
        System.out.println("Thanh toán $" + amount + " bằng PayPal (Tài khoản: " + email + ")");
    }
}

class ShoppingCart {
    private double amount;
    private PaymentStrategy strategy;

    public ShoppingCart(double amount) { this.amount = amount; }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void checkout() {
        if (strategy == null) {
            System.out.println("Vui lòng chọn phương thức thanh toán!");
            return;
        }
        strategy.pay(amount);
    }
}

public class StrategyPaymentDemo {
    public static void main(String[] args) {
        System.out.println("=== STRATEGY: CHỌN PHƯƠNG THỨC THANH TOÁN ===");
        ShoppingCart cart = new ShoppingCart(150.0);

        // Chọn thanh toán bằng Thẻ
        cart.setPaymentStrategy(new CreditCardStrategy("1234-5678-9012"));
        cart.checkout();

        // Đổi ý, thanh toán bằng PayPal
        cart.setPaymentStrategy(new PayPalStrategy("user@example.com"));
        cart.checkout();
    }
}