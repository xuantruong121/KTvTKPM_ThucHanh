package MoTa1.StatePattern;

interface OrderState {
    void processOrder(OrderContext context);
    void cancelOrder(OrderContext context);
}

class NewState implements OrderState {
    public void processOrder(OrderContext context) {
        System.out.println("[Mới tạo] -> Đã kiểm tra thông tin. Chuyển sang Đang xử lý.");
        context.setState(new ProcessingState());
    }
    public void cancelOrder(OrderContext context) {
        System.out.println("[Mới tạo] -> Hủy đơn hàng thành công.");
        context.setState(new CancelledState());
    }
}

class ProcessingState implements OrderState {
    public void processOrder(OrderContext context) {
        System.out.println("[Đang xử lý] -> Đã đóng gói và vận chuyển. Chuyển sang Đã giao.");
        context.setState(new DeliveredState());
    }
    public void cancelOrder(OrderContext context) {
        System.out.println("[Đang xử lý] -> Khách hàng yêu cầu hủy. Bắt đầu hoàn tiền.");
        context.setState(new CancelledState());
    }
}

class DeliveredState implements OrderState {
    public void processOrder(OrderContext context) {
        System.out.println("[Đã giao] -> Đơn hàng đã hoàn tất, không thể xử lý thêm.");
    }
    public void cancelOrder(OrderContext context) {
        System.out.println("[Lỗi] -> Không thể hủy đơn hàng đã giao.");
    }
}

class CancelledState implements OrderState {
    public void processOrder(OrderContext context) {
        System.out.println("[Lỗi] -> Đơn hàng đã hủy, không thể xử lý.");
    }
    public void cancelOrder(OrderContext context) {
        System.out.println("[Lỗi] -> Đơn hàng đã ở trạng thái hủy từ trước.");
    }
}

class OrderContext {
    private OrderState currentState;

    public OrderContext() {
        this.currentState = new NewState(); // Trạng thái mặc định
    }
    public void setState(OrderState state) {
        this.currentState = state;
    }
    public void process() {
        currentState.processOrder(this);
    }
    public void cancel() {
        currentState.cancelOrder(this);
    }
}

public class StatePatternDemo {
    public static void main(String[] args) {
        System.out.println("--- Kịch bản 1: Đơn hàng giao thành công ---");
        OrderContext order1 = new OrderContext();
        order1.process(); // Chuyển sang Đang xử lý
        order1.process(); // Chuyển sang Đã giao
        order1.cancel();  // Cố tình hủy khi đã giao

        System.out.println("\n--- Kịch bản 2: Hủy đơn hàng khi đang xử lý ---");
        OrderContext order2 = new OrderContext();
        order2.process(); // Chuyển sang Đang xử lý
        order2.cancel();  // Hủy đơn hàng
        order2.process(); // Cố tình xử lý tiếp
    }
}
