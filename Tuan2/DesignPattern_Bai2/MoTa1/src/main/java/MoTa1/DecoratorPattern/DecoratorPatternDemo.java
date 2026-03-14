package MoTa1.DecoratorPattern;

interface Payment {
    double getAmount();
    String getDescription();
}

class CreditCardPayment implements Payment {
    private double amount;
    public CreditCardPayment(double amount) { this.amount = amount; }
    public double getAmount() { return amount; }
    public String getDescription() { return "Thanh toán qua Credit Card"; }
}

class PayPalPayment implements Payment {
    private double amount;
    public PayPalPayment(double amount) { this.amount = amount; }
    public double getAmount() { return amount; }
    public String getDescription() { return "Thanh toán qua PayPal"; }
}

abstract class PaymentDecorator implements Payment {
    protected Payment wrappedPayment;
    public PaymentDecorator(Payment payment) {
        this.wrappedPayment = payment;
    }
    public double getAmount() { return wrappedPayment.getAmount(); }
    public String getDescription() { return wrappedPayment.getDescription(); }
}

class ProcessingFeeDecorator extends PaymentDecorator {
    public ProcessingFeeDecorator(Payment payment) { super(payment); }
    public double getAmount() { return super.getAmount() + 2.5; } // Phí xử lý $2.5
    public String getDescription() { return super.getDescription() + " (+ Phí xử lý $2.5)"; }
}

class DiscountCodeDecorator extends PaymentDecorator {
    public DiscountCodeDecorator(Payment payment) { super(payment); }
    public double getAmount() { return super.getAmount() * 0.9; } // Giảm 10%
    public String getDescription() { return super.getDescription() + " (- Giảm giá 10%)"; }
}

public class DecoratorPatternDemo {
    public static void main(String[] args) {
        // Tạo thanh toán gốc 100$ qua Credit Card
        Payment myPayment = new CreditCardPayment(100.0);

        System.out.println("Thanh toán gốc:");
        System.out.println("Mô tả: " + myPayment.getDescription());
        System.out.println("Tổng tiền: $" + myPayment.getAmount() + "\n");

        // Bọc thêm mã giảm giá 10%
        myPayment = new DiscountCodeDecorator(myPayment);

        // Bọc thêm phí xử lý
        myPayment = new ProcessingFeeDecorator(myPayment);

        System.out.println("Thanh toán sau khi áp dụng tính năng bổ sung:");
        System.out.println("Mô tả: " + myPayment.getDescription());
        System.out.println("Tổng tiền cuối cùng: $" + myPayment.getAmount());
    }
}
