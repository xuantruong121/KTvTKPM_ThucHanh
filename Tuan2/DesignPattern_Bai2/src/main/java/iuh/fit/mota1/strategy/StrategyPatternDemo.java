package iuh.fit.mota1.strategy;

interface TaxStrategy {
    double calculateTax(double price);
}

class VATTaxStrategy implements TaxStrategy {
    public double calculateTax(double price) { return price * 0.10; } // VAT 10%
}

class ConsumptionTaxStrategy implements TaxStrategy {
    public double calculateTax(double price) { return price * 0.05; } // Tiêu thụ 5%
}

class LuxuryTaxStrategy implements TaxStrategy {
    public double calculateTax(double price) { return price * 0.30; } // Xa xỉ 30%
}

class Product {
    private String name;
    private double basePrice;
    private TaxStrategy taxStrategy;

    public Product(String name, double basePrice, TaxStrategy taxStrategy) {
        this.name = name;
        this.basePrice = basePrice;
        this.taxStrategy = taxStrategy;
    }

    public void setTaxStrategy(TaxStrategy taxStrategy) {
        this.taxStrategy = taxStrategy;
    }

    public double getFinalPrice() {
        return basePrice + taxStrategy.calculateTax(basePrice);
    }

    public void printInfo() {
        System.out.println("Sản phẩm: " + name + " | Giá gốc: $" + basePrice + " | Giá sau thuế: $" + getFinalPrice());
    }
}

public class StrategyPatternDemo {
    public static void main(String[] args) {
        // Áp dụng thuế VAT
        Product laptop = new Product("Laptop Dell", 1000, new VATTaxStrategy());
        laptop.printInfo();

        // Áp dụng thuế xa xỉ phẩm
        Product rolex = new Product("Đồng hồ Rolex", 5000, new LuxuryTaxStrategy());
        rolex.printInfo();

        // Đổi luật thuế lúc runtime (ví dụ: chuyển thành thuế tiêu thụ)
        System.out.println("\n--- Đổi chính sách thuế cho Laptop ---");
        laptop.setTaxStrategy(new ConsumptionTaxStrategy());
        laptop.printInfo();
    }
}
