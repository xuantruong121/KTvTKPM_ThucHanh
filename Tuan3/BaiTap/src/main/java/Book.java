//Factory Method Pattern

// 1. Product Interface
abstract class Book {
    protected String title;
    protected String author;
    protected String category;

    public Book(String title, String author, String category) {
        this.title = title; this.author = author; this.category = category;
    }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }

    public abstract void read();
}

// 2. Concrete Products
class PaperBook extends Book {
    public PaperBook(String title, String author, String category) { super(title, author, category); }
    public void read() { System.out.println("Đọc sách giấy: " + title); }
}

class EBook extends Book {
    public EBook(String title, String author, String category) { super(title, author, category); }
    public void read() { System.out.println("Đọc sách điện tử (PDF/EPUB): " + title); }
}

// 3. Creator (Factory)
abstract class BookFactory {
    public abstract Book createBook(String title, String author, String category);
}

// 4. Concrete Creators
class PaperBookFactory extends BookFactory {
    public Book createBook(String title, String author, String category) {
        return new PaperBook(title, author, category);
    }
}

class EBookFactory extends BookFactory {
    public Book createBook(String title, String author, String category) {
        return new EBook(title, author, category);
    }
}