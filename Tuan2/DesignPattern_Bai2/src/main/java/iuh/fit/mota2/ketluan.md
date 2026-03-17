# KẾT LUẬN: SO SÁNH ÁP DỤNG DESIGN PATTERN CHO BÀI TOÁN TÍNH THUẾ

Bài toán tính toán thuế sản phẩm (VAT, Tiêu thụ, Xa xỉ) là một ví dụ tuyệt vời để phân tích sự khác biệt trong tư duy thiết kế của 3 Pattern: Strategy, Decorator và State. Mặc dù cả 3 đều có thể giải quyết vấn đề, nhưng mỗi Pattern phục vụ một kịch bản nghiệp vụ hoàn toàn khác nhau.

## 1. Strategy Pattern: "Chọn một thuật toán duy nhất"
* **Bối cảnh phù hợp:** Khi mỗi sản phẩm chỉ được phép áp dụng **một loại thuế duy nhất** tại một thời điểm.
* **Cách hoạt động:** Thuật toán tính thuế được trừu tượng hóa thành các Strategy độc lập. Hệ thống sẽ thay thế linh hoạt (swap) các thuật toán này (từ VAT sang Tiêu thụ) mà không làm ảnh hưởng đến mã nguồn của lớp `Product`.
* **Đánh giá:** Đây là Pattern **chuẩn xác và tự nhiên nhất** cho mô tả gốc của bài toán, tuân thủ hoàn hảo nguyên lý OCP (Open/Closed Principle).

## 2. Decorator Pattern: "Cộng dồn nhiều loại thuế"
* **Bối cảnh phù hợp:** Khi pháp luật yêu cầu một sản phẩm phải chịu **thuế chồng thuế** (Ví dụ: Tính VAT 10% vào giá gốc, sau đó lại áp tiếp Thuế xa xỉ 30% lên tổng giá trị đó).
* **Cách hoạt động:** Các loại thuế đóng vai trò là lớp "vỏ bọc" (wrapper). Ta có thể đắp liên tục nhiều loại thuế lên nhau bằng cách truyền đối tượng vào trong hàm tạo của Decorator.
* **Đánh giá:** Giải quyết triệt để vấn đề "Bùng nổ lớp con" (Class explosion). Tránh việc phải tạo ra các class như `ProductWithVATAndLuxuryTax`. Tuy nhiên, logic cộng dồn giá trị có thể trở nên phức tạp nếu thứ tự bọc thuế ảnh hưởng đến kết quả toán học.

## 3. State Pattern: "Thuế thay đổi tự động theo vòng đời sản phẩm"
* **Bối cảnh phù hợp:** Khi mức thuế phụ thuộc vào **điều kiện nội tại** của sản phẩm (ví dụ: Quy định giá > $1000 tự động thành hàng xa xỉ).
* **Cách hoạt động:** Lớp `Product` sở hữu một trạng thái thuế (`TaxState`). Khi giá cả biến động vượt ngưỡng, bản thân trạng thái đó sẽ tự ra quyết định ép `Product` chuyển sang trạng thái thuế khác.
* **Đánh giá:** Điểm khác biệt lớn nhất so với Strategy là tính **chủ động tự chuyển đổi**. Client (Hàm main) không cần phải biết khi nào nên dùng thuế nào, hệ thống sẽ tự động điều phối dựa trên các quy tắc (rules) được cài cắm sẵn trong các State.

**Tóm tắt:** - Dùng **Strategy** nếu người dùng tự chọn loại thuế.
- Dùng **Decorator** nếu muốn áp dụng nhiều thuế cùng lúc.
- Dùng **State** nếu hạng mức thuế bị chi phối tự động bởi giá trị hoặc vòng đời của sản phẩm.