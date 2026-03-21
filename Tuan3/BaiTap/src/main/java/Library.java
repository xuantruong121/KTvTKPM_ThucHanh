import java.util.ArrayList;
import java.util.List;

//Singleton Pattern
public class Library {
    // 1. Biến instance static volatile để đảm bảo Thread-safe
    private static volatile Library instance;
    private List<Book> books; // Giả sử Book là interface/class ta sẽ tạo ở phần sau

    // 2. Private constructor
    private Library() {
        books = new ArrayList<>();
        System.out.println("Khởi tạo hệ thống Thư viện duy nhất.");
    }

    // 3. Double-Checked Locking getInstance()
    public static Library getInstance() {
        if (instance == null) {
            synchronized (Library.class) {
                if (instance == null) {
                    instance = new Library();
                }
            }
        }
        return instance;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return books;
    }
}