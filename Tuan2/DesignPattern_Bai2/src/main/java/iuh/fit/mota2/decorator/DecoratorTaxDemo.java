package iuh.fit.mota2.decorator;

interface SellableItem {
    double getPrice();
    String getDescription();
}

class BaseProduct implements SellableItem {
    private String name;
    private double price;

    public BaseProduct(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() { return price; }
    public String getDescription() { return name; }
}

abstract class TaxDecorator implements SellableItem {
    protected SellableItem wrappedItem;

    public TaxDecorator(SellableItem item) { this.wrappedItem = item; }
    public double getPrice() { return wrappedItem.getPrice(); }
    public String getDescription() { return wrappedItem.getDescription(); }
}

class VATTaxDecorator extends TaxDecorator {
    public VATTaxDecorator(SellableItem item) { super(item); }
    public double getPrice() { return super.getPrice() + (super.getPrice() * 0.10); } // Cộng thêm 10%
    public String getDescription() { return super.getDescription() + " (+ VAT 10%)"; }
}

class LuxuryTaxDecorator extends TaxDecorator {
    public LuxuryTaxDecorator(SellableItem item) { super(item); }
    public double getPrice() { return super.getPrice() + (super.getPrice() * 0.30); } // Cộng thêm 30%
    public String getDescription() { return super.getDescription() + " (+ Thuế Xa xỉ 30%)"; }
}

public class DecoratorTaxDemo {
    public static void main(String[] args) {
        System.out.println("=== DECORATOR PATTERN: CỘNG DỒN THUẾ ===");
        SellableItem car = new BaseProduct("Xe Oto", 50000);

        // Vừa áp VAT, vừa áp Thuế xa xỉ
        car = new VATTaxDecorator(car);
        car = new LuxuryTaxDecorator(car);

        System.out.println("Mô tả: " + car.getDescription());
        System.out.println("Tổng giá trị: $" + car.getPrice());
    }
}
