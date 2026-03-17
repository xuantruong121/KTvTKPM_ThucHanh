package iuh.fit.mota2.state;

interface TaxState {
    double calculateTax(double price);
    void checkStateTransition(ProductContext context, double price);
}

class NormalTaxState implements TaxState {
    public double calculateTax(double price) { return price * 0.10; } // VAT 10%

    public void checkStateTransition(ProductContext context, double price) {
        if (price >= 1000) {
            System.out.println(">> Giá trị vượt $1000. Tự động chuyển sang hạng Xa xỉ phẩm.");
            context.setState(new LuxuryItemState());
        }
    }
}

class LuxuryItemState implements TaxState {
    public double calculateTax(double price) { return price * 0.30; } // Thuế Xa xỉ 30%

    public void checkStateTransition(ProductContext context, double price) {
        if (price < 1000) {
            System.out.println(">> Giá trị dưới $1000. Tự động trở về hạng Bình thường.");
            context.setState(new NormalTaxState());
        }
    }
}

class ProductContext {
    private String name;
    private double basePrice;
    private TaxState currentState;

    public ProductContext(String name, double basePrice) {
        this.name = name;
        this.currentState = new NormalTaxState(); // Mặc định là bình thường
        setPrice(basePrice);
    }

    public void setState(TaxState state) { this.currentState = state; }

    public void setPrice(double price) {
        this.basePrice = price;
        currentState.checkStateTransition(this, price); // Kiểm tra xem có cần đổi trạng thái thuế không
    }

    public double getFinalPrice() {
        return basePrice + currentState.calculateTax(basePrice);
    }
}

public class StateTaxDemo {
    public static void main(String[] args) {
        System.out.println("=== STATE PATTERN: TỰ ĐỘNG CHUYỂN ĐỔI THUẾ ===");
        ProductContext watch = new ProductContext("Đồng hồ", 500);
        System.out.println("Giá (VAT 10%): $" + watch.getFinalPrice());

        System.out.println("\n-- Cập nhật giá sản phẩm lên $1200 --");
        watch.setPrice(1200); // Tự động kích hoạt chuyển đổi trạng thái
        System.out.println("Giá (Thuế xa xỉ 30%): $" + watch.getFinalPrice());
    }
}
