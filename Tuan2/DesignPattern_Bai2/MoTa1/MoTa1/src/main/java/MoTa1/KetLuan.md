# KẾT LUẬN: ÁP DỤNG DESIGN PATTERN TRONG THIẾT KẾ HỆ THỐNG THỰC TẾ

Việc áp dụng các Design Pattern (State, Strategy, Decorator) vào các bài toán quản lý đơn hàng, tính thuế và thanh toán đã chứng minh rõ vai trò quan trọng của kiến trúc phần mềm trong việc giải quyết các vấn đề mở rộng, bảo trì và tính linh hoạt của hệ thống backend.

Dưới đây là kết luận chi tiết cho từng trường hợp:

## 1. Bài toán Quản lý đơn hàng (Áp dụng State Pattern)
* **Vấn đề giải quyết:** Hệ thống có nhiều trạng thái chuyển tiếp vòng vằng và phụ thuộc lẫn nhau. Nếu dùng các câu lệnh `if-else` hoặc `switch-case` gom chung vào một class `Order`, code sẽ nhanh chóng trở thành một "khối spaghetti" khó kiểm soát, vi phạm nghiêm trọng nguyên lý Single Responsibility (Đơn trách nhiệm).
* **Hiệu quả mang lại:** * **Phân tách trách nhiệm:** State Pattern cô lập logic của từng trạng thái (Mới tạo, Đang xử lý, Đã giao, Hủy) vào các lớp độc lập. Mỗi lớp tự quản lý hành vi và quy tắc chuyển đổi trạng thái của riêng nó.
  * **Dễ bảo trì và mở rộng:** Khi nghiệp vụ yêu cầu thêm một trạng thái mới (ví dụ: *Đang hoàn trả - Refunding*), ta chỉ cần định nghĩa một class mới implements `OrderState` mà không làm vỡ logic của các trạng thái hiện tại.
* **Trade-off (Sự đánh đổi):** Đánh đổi lại sự linh hoạt là số lượng class trong hệ thống sẽ tăng lên tỷ lệ thuận với số lượng trạng thái nghiệp vụ.

## 2. Bài toán Tính toán thuế sản phẩm (Áp dụng Strategy Pattern)
* **Vấn đề giải quyết:** Thuật toán tính thuế thường xuyên thay đổi do các chính sách pháp luật hoặc biến động thị trường. Nếu hardcode logic tính thuế vào class `Product`, hệ thống sẽ phải liên tục chỉnh sửa mã nguồn gốc, gây rủi ro cao.
* **Hiệu quả mang lại:**
  * **Tuân thủ Open/Closed Principle (OCP):** Class `Product` hoàn toàn đóng với việc sửa đổi nhưng mở với việc mở rộng. Các thuật toán thuế (VAT, Thuế tiêu thụ, Thuế xa xỉ) được đóng gói thành các đối tượng Strategy riêng biệt và tiêm (inject) vào Context.
  * **Thay đổi linh hoạt tại Runtime:** Có thể dễ dàng thay đổi chính sách thuế cho một sản phẩm ngay trong lúc chương trình đang chạy chỉ bằng một lệnh `setTaxStrategy()`.
* **Trade-off:** Client (lớp gọi hàm) phải nắm rõ sự khác biệt giữa các Strategy (các loại thuế) để có thể khởi tạo và truyền đúng đối tượng vào Product.

## 3. Bài toán Hệ thống thanh toán (Áp dụng Decorator Pattern)
* **Vấn đề giải quyết:** Khi muốn đắp thêm các tính năng phụ (Phí xử lý, Mã giảm giá) vào một phương thức thanh toán gốc (Credit Card, PayPal), nếu dùng cơ chế kế thừa (Inheritance), ta sẽ gặp tình trạng "bùng nổ lớp con" (Class Explosion) với vô số các tổ hợp như `CreditCardWithFee`, `CreditCardWithDiscountAndFee`, v.v.
* **Hiệu quả mang lại:**
  * **Composition over Inheritance:** Thay vì kế thừa tĩnh, Decorator sử dụng cơ chế bao bọc (wrap) đệ quy để mở rộng tính năng một cách linh hoạt tại thời điểm chạy (runtime).
  * **Tổ hợp tính năng tự do:** Có thể áp dụng nhiều loại phí, cộng dồn nhiều mã giảm giá theo bất kỳ thứ tự nào chỉ bằng cách bọc các Decorator chồng lên nhau, mà cấu trúc của class gốc (Credit Card/PayPal) vẫn được giữ nguyên vẹn.
* **Trade-off:** Quá trình khởi tạo đối tượng trở nên dài dòng hơn do phải lồng ghép nhiều lớp constructor. Đồng thời, việc debug có thể phức tạp hơn khi phải trace qua nhiều tầng bọc của Decorator.

---
**Tổng kết chung:** Cả ba Pattern đều hướng tới việc giảm thiểu sự phụ thuộc (loose coupling) và tối ưu hóa tính mở rộng (high cohesion). Việc nhận diện đúng đặc thù bài toán (quản lý trạng thái, thay đổi thuật toán, hay đắp thêm tính năng) là chìa khóa để áp dụng Pattern một cách chính xác nhất.