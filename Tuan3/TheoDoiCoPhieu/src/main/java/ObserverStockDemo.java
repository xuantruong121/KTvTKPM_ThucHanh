import java.util.ArrayList;
import java.util.List;

// 1. Observer Interface (Giao diện cho người theo dõi)
interface Observer {
    void update(String stockSymbol, double price);
}

// 2. Subject Interface (Giao diện cho đối tượng bị theo dõi)
interface Subject {
    void attach(Observer observer); // Đăng ký
    void detach(Observer observer); // Hủy đăng ký
    void notifyObservers();         // Gửi thông báo
}

// 3. Concrete Subject (Lớp Cổ phiếu cụ thể)
class Stock implements Subject {
    private String symbol;
    private double price;
    private List<Observer> investors;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
        this.investors = new ArrayList<>();
    }

    @Override
    public void attach(Observer observer) {
        investors.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        investors.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer investor : investors) {
            investor.update(symbol, price);
        }
    }

    // Khi giá thay đổi, tự động gọi notifyObservers()
    public void setPrice(double price) {
        System.out.println("\n[HỆ THỐNG] Cổ phiếu " + symbol + " cập nhật giá mới: $" + price);
        this.price = price;
        notifyObservers();
    }
}

// 4. Concrete Observer (Lớp Nhà đầu tư cụ thể)
class Investor implements Observer {
    private String name;

    public Investor(String name) {
        this.name = name;
    }

    @Override
    public void update(String stockSymbol, double price) {
        System.out.println(" -> " + name + " nhận được thông báo: Cổ phiếu " + stockSymbol + " hiện có giá $" + price);
    }
}

// 5. Client
public class ObserverStockDemo {
    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG THEO DÕI CỔ PHIẾU ===");

        // Tạo một cổ phiếu
        Stock appleStock = new Stock("AAPL", 150.0);

        // Tạo các nhà đầu tư
        Observer investorA = new Investor("Nhà đầu tư A");
        Observer investorB = new Investor("Nhà đầu tư B");

        // Các nhà đầu tư bấm "Theo dõi" cổ phiếu Apple
        appleStock.attach(investorA);
        appleStock.attach(investorB);

        // Cổ phiếu biến động giá -> Các nhà đầu tư tự động nhận thông báo
        appleStock.setPrice(155.5);

        // Nhà đầu tư A hủy theo dõi
        System.out.println("\n[HỆ THỐNG] Nhà đầu tư A đã hủy theo dõi mã AAPL.");
        appleStock.detach(investorA);

        // Cổ phiếu tiếp tục biến động -> Chỉ còn B nhận được thông báo
        appleStock.setPrice(160.0);
    }
}