package iuh.fit.mota2.strategy;

interface TaxStrategy {
    double calculateTax(double price);
}

class VATTaxStrategy implements TaxStrategy {
    public double calculateTax(double price) { return price * 0.10; } // 10% VAT
}

class ConsumptionTaxStrategy implements TaxStrategy {
    public double calculateTax(double price) { return price * 0.05; } // 5% Tiêu thụ
}

class LuxuryTaxStrategy implements TaxStrategy {
    public double calculateTax(double price) { return price * 0.30; } // 30% Xa xỉ
}

class Product {
    private String name;
    private double price;
    private TaxStrategy taxStrategy;

    public Product(String name, double price, TaxStrategy taxStrategy) {
        this.name = name;
        this.price = price;
        this.taxStrategy = taxStrategy;
    }

    public void setTaxStrategy(TaxStrategy taxStrategy) {
        this.taxStrategy = taxStrategy;
    }

    public double calculateFinalPrice() {
        return price + taxStrategy.calculateTax(price);
    }
}

public class StrategyTaxDemo {
    public static void main(String[] args) {
        System.out.println("=== STRATEGY PATTERN: TÍNH THUẾ ===");
        Product item = new Product("Túi xách", 1000, new ConsumptionTaxStrategy());
        System.out.println("Giá áp dụng Thuế Tiêu thụ: $" + item.calculateFinalPrice());

        // Đổi chiến lược thuế lúc runtime
        item.setTaxStrategy(new LuxuryTaxStrategy());
        System.out.println("Giá áp dụng Thuế Xa xỉ: $" + item.calculateFinalPrice());
    }
}
