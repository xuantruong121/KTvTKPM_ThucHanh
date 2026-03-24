import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1) Singleton: lay instance duy nhat cua thu vien
        Library library = Library.getInstance();

        // 2) Factory Method: tao sach bang cac factory khac nhau
        BookFactory paperFactory = new PaperBookFactory();
        BookFactory ebookFactory = new EBookFactory();

        Book b1 = paperFactory.createBook("Design Patterns", "GoF", "Software Engineering");
        Book b2 = ebookFactory.createBook("Clean Code", "Robert C. Martin", "Programming");
        Book b3 = paperFactory.createBook("Java Concurrency", "Brian Goetz", "Programming");

        library.addBook(b1);
        library.addBook(b2);
        library.addBook(b3);

        // Goi hanh vi cua product
        b1.read();
        b2.read();

        // 3) Strategy: tim kiem sach theo tieu de / tac gia
        SearchContext searchContext = new SearchContext();

        searchContext.setSearchStrategy(new SearchByName());
        List<Book> byName = searchContext.executeSearch(library.getBooks(), "code");
        System.out.println("\nKet qua tim theo ten 'code':");
        for (Book book : byName) {
            System.out.println("- " + book.getTitle() + " | " + book.getAuthor());
        }

        searchContext.setSearchStrategy(new SearchByAuthor());
        List<Book> byAuthor = searchContext.executeSearch(library.getBooks(), "goetz");
        System.out.println("\nKet qua tim theo tac gia 'goetz':");
        for (Book book : byAuthor) {
            System.out.println("- " + book.getTitle() + " | " + book.getAuthor());
        }

        // 4) Observer: thong bao khi co sach moi
        LibraryNotifier notifier = new LibraryNotifier();
        notifier.addObserver(new UserSubscriber("An"));
        notifier.addObserver(new Librarian("LIB-001"));

        Book newBook = ebookFactory.createBook("Refactoring", "Martin Fowler", "Programming");
        library.addBook(newBook);
        notifier.newBookAdded(newBook);

        // 5) Decorator: mo rong quy trinh muon sach
        BorrowingProcess borrowing = new BasicBorrowing(b1);
        borrowing = new ExtendedTimeDecorator(borrowing);
        borrowing = new BrailleVersionDecorator(borrowing);

        System.out.println("\nThong tin muon sach:");
        System.out.println("Mo ta: " + borrowing.getDescription());
        System.out.println("Tong phi: $" + borrowing.getCost());
    }
}
