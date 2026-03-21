import java.util.ArrayList;
import java.util.List;

// 1. Component
interface FileSystemComponent {
    void showDetails(String indent);
}

// 2. Leaf (Lá - Không có phần tử con)
class File implements FileSystemComponent {
    private String name;
    private String data; // Kích thước hoặc dữ liệu

    public File(String name, String data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public void showDetails(String indent) {
        System.out.println(indent + "- (File) " + name + " [" + data + "]");
    }
}

// 3. Composite (Nhánh - Chứa các phần tử con)
class Folder implements FileSystemComponent {
    private String name;
    // Chứa danh sách các Component (có thể là File hoặc Folder khác)
    private List<FileSystemComponent> components = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

    public void addComponent(FileSystemComponent component) {
        components.add(component);
    }

    public void removeComponent(FileSystemComponent component) {
        components.remove(component);
    }

    @Override
    public void showDetails(String indent) {
        System.out.println(indent + "+ (Folder) " + name);
        // Duyệt qua tất cả các thành phần con và gọi hàm showDetails của chúng
        for (FileSystemComponent component : components) {
            component.showDetails(indent + "    "); // Thêm khoảng trắng để thụt lề
        }
    }
}

// 4. Client (Hàm chạy thử nghiệm)
public class CompositeFileSystemDemo {
    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG QUẢN LÝ TẬP TIN (COMPOSITE PATTERN) ===\n");

        // Tạo các tập tin (Leaf)
        FileSystemComponent file1 = new File("tailieu.docx", "500KB");
        FileSystemComponent file2 = new File("anh_the.jpg", "2MB");
        FileSystemComponent file3 = new File("source_code.java", "15KB");
        FileSystemComponent file4 = new File("huong_dan.txt", "2KB");

        // Tạo thư mục con và thêm tập tin vào
        Folder docsFolder = new Folder("Documents");
        docsFolder.addComponent(file1);
        docsFolder.addComponent(file4);

        Folder picturesFolder = new Folder("Pictures");
        picturesFolder.addComponent(file2);

        Folder codeFolder = new Folder("Projects");
        codeFolder.addComponent(file3);

        // Tạo thư mục gốc và chứa các thư mục con
        Folder rootFolder = new Folder("C_Drive");
        rootFolder.addComponent(docsFolder);
        rootFolder.addComponent(picturesFolder);
        rootFolder.addComponent(codeFolder);

        // Hiển thị toàn bộ cấu trúc cây từ thư mục gốc
        rootFolder.showDetails("");
    }
}