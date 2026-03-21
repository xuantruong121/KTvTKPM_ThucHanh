import java.util.ArrayList;
import java.util.List;

// 1. Component
interface UIElement {
    void render(String indent);
}

// 2. Leaf (Phần tử UI đơn lẻ)
class Button implements UIElement {
    private String label;

    public Button(String label) { this.label = label; }

    @Override
    public void render(String indent) {
        System.out.println(indent + "[Button] " + label);
    }
}

class TextBox implements UIElement {
    private String placeholder;

    public TextBox(String placeholder) { this.placeholder = placeholder; }

    @Override
    public void render(String indent) {
        System.out.println(indent + "[TextBox] " + placeholder);
    }
}

// 3. Composite (Nhóm các phần tử UI phức tạp)
class UIContainer implements UIElement {
    private String name;
    private List<UIElement> elements = new ArrayList<>();

    public UIContainer(String name) { this.name = name; }

    public void addElement(UIElement element) {
        elements.add(element);
    }

    public void removeElement(UIElement element) {
        elements.remove(element);
    }

    @Override
    public void render(String indent) {
        System.out.println(indent + "<Container: " + name + ">");
        for (UIElement element : elements) {
            element.render(indent + "  "); // Thụt lề cho phần tử con
        }
        System.out.println(indent + "</Container: " + name + ">");
    }
}

// 4. Client
public class CompositeUIDemo {
    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG GIAO DIỆN UI (COMPOSITE PATTERN) ===\n");

        // Tạo các nút và ô nhập liệu
        UIElement btnLogin = new Button("Đăng nhập");
        UIElement btnCancel = new Button("Hủy bỏ");
        UIElement txtUsername = new TextBox("Nhập tên tài khoản...");
        UIElement txtPassword = new TextBox("Nhập mật khẩu...");

        // Nhóm Form đăng nhập
        UIContainer loginForm = new UIContainer("LoginForm");
        loginForm.addElement(txtUsername);
        loginForm.addElement(txtPassword);

        // Nhóm Action (Các nút bấm)
        UIContainer actionPanel = new UIContainer("ActionPanel");
        actionPanel.addElement(btnLogin);
        actionPanel.addElement(btnCancel);

        // Hộp thoại tổng (Dialog chứa cả Form và Action)
        UIContainer mainDialog = new UIContainer("LoginDialog");
        mainDialog.addElement(loginForm);
        mainDialog.addElement(actionPanel);

        // Render toàn bộ giao diện
        // Khách hàng (Client) chỉ cần gọi 1 hàm render() duy nhất cho cả hệ thống phức tạp
        mainDialog.render("");
    }
}