package iuh.fit.mota3.state;

interface TransactionState {
    void authorize(TransactionContext context); // Xác thực tài khoản
    void capture(TransactionContext context);   // Trừ tiền
}

class PendingState implements TransactionState {
    public void authorize(TransactionContext context) {
        System.out.println("[Pending] -> Xác thực thông tin thẻ thành công. Chuyển sang Authorized.");
        context.setState(new AuthorizedState());
    }
    public void capture(TransactionContext context) {
        System.out.println("[Lỗi] -> Không thể trừ tiền khi chưa xác thực thẻ!");
    }
}

class AuthorizedState implements TransactionState {
    public void authorize(TransactionContext context) {
        System.out.println("[Lỗi] -> Thẻ đã được xác thực rồi.");
    }
    public void capture(TransactionContext context) {
        System.out.println("[Authorized] -> Trừ tiền thành công. Giao dịch Hoàn tất.");
        context.setState(new CompletedState());
    }
}

class CompletedState implements TransactionState {
    public void authorize(TransactionContext context) {
        System.out.println("[Lỗi] -> Giao dịch đã hoàn tất, không thể xác thực lại.");
    }
    public void capture(TransactionContext context) {
        System.out.println("[Lỗi] -> Giao dịch đã hoàn tất, không thể trừ tiền thêm.");
    }
}

class TransactionContext {
    private TransactionState currentState;

    public TransactionContext() {
        this.currentState = new PendingState(); // Mới tạo là Pending
    }
    public void setState(TransactionState state) { this.currentState = state; }
    public void authorize() { currentState.authorize(this); }
    public void capture() { currentState.capture(this); }
}

public class StatePaymentDemo {
    public static void main(String[] args) {
        System.out.println("=== STATE: QUẢN LÝ VÒNG ĐỜI GIAO DỊCH ===");
        TransactionContext transaction = new TransactionContext();

        transaction.capture();   // Cố tình trừ tiền khi chưa xác thực -> Báo lỗi
        transaction.authorize(); // Xác thực thẻ hợp lệ
        transaction.capture();   // Trừ tiền thành công
        transaction.capture();   // Cố tình trừ tiền lần 2 -> Báo lỗi
    }
}
