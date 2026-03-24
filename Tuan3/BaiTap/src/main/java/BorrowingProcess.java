// Decorator Pattern
interface BorrowingProcess {
    String getDescription();

    double getCost();
}

class BasicBorrowing implements BorrowingProcess {
    private final Book book;

    public BasicBorrowing(Book book) {
        this.book = book;
    }

    public String getDescription() {
        return "Muon sach: " + book.getTitle();
    }

    public double getCost() {
        return 5.0;
    }
}

abstract class BorrowingDecorator implements BorrowingProcess {
    protected BorrowingProcess wrappedProcess;

    public BorrowingDecorator(BorrowingProcess p) {
        this.wrappedProcess = p;
    }

    public String getDescription() {
        return wrappedProcess.getDescription();
    }

    public double getCost() {
        return wrappedProcess.getCost();
    }
}

class ExtendedTimeDecorator extends BorrowingDecorator {
    public ExtendedTimeDecorator(BorrowingProcess p) {
        super(p);
    }

    public String getDescription() {
        return super.getDescription() + " [+ Gia han them 7 ngay]";
    }

    public double getCost() {
        return super.getCost() + 2.0;
    }
}

class BrailleVersionDecorator extends BorrowingDecorator {
    public BrailleVersionDecorator(BorrowingProcess p) {
        super(p);
    }

    public String getDescription() {
        return super.getDescription() + " [+ Kem phien ban chu noi Braille]";
    }

    public double getCost() {
        return super.getCost() + 3.5;
    }
}
