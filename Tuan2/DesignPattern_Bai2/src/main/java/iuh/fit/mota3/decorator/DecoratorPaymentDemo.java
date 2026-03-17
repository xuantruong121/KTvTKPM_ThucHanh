package iuh.fit.mota3.decorator;

interface PaymentMethod {
    double getAmount();
    String getDescription();
}

class BasicPayment implements PaymentMethod {
    private double amount;
    private String type; // Ví dụ: "Credit Card" hoặc "PayPal"

    public BasicPayment(double amount, String type) {
        this.amount = amount;
        this.type = type;
    }

    public double getAmount() { return amount; }
    public String getDescription() { return "Thanh toán qua " + type; }
}

abstract class PaymentFeatureDecorator implements PaymentMethod {
    protected PaymentMethod wrappedMethod;
    public PaymentFeatureDecorator(PaymentMethod method) { this.wrappedMethod = method; }
    public double getAmount() { return wrappedMethod.getAmount(); }
    public String getDescription() { return wrappedMethod.getDescription(); }
}

class ProcessingFeeDecorator extends PaymentFeatureDecorator {
    public ProcessingFeeDecorator(PaymentMethod method) { super(method); }
    public double getAmount() { return super.getAmount() + 2.0; } // Phí $2.0
    public String getDescription() { return super.getDescription() + " (+ Phí xử lý $2.0)"; }
}

class DiscountDecorator extends PaymentFeatureDecorator {
    public DiscountDecorator(PaymentMethod method) { super(method); }
    public double getAmount() { return super.getAmount() * 0.9; } // Giảm 10%
    public String getDescription() { return super.getDescription() + " (- Giảm 10%)"; }
}

public class DecoratorPaymentDemo {
    public static void main(String[] args) {
        System.out.println("=== DECORATOR: THÊM TÍNH NĂNG BỔ SUNG ===");

        // 1. Thanh toán gốc bằng PayPal ($100)
        PaymentMethod myPayment = new BasicPayment(100.0, "PayPal");

        // 2. Thêm phí xử lý
        myPayment = new ProcessingFeeDecorator(myPayment);

        // 3. Áp dụng mã giảm giá 10% (Giảm trên tổng tiền đã cộng phí)
        myPayment = new DiscountDecorator(myPayment);

        System.out.println("Mô tả: " + myPayment.getDescription());
        System.out.println("Tổng tiền cuối cùng phải trả: $" + myPayment.getAmount());
    }
}