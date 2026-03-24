import java.util.ArrayList;
import java.util.List;

// Observer Pattern
interface Observer {
    void update(String message);
}

interface Subject {
    void addObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers(String message);
}

class LibraryNotifier implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }

    public void newBookAdded(Book book) {
        String msg = "Sach moi vua ve: " + book.getTitle() + " (Tac gia: " + book.getAuthor() + ")";
        notifyObservers(msg);
    }
}

class UserSubscriber implements Observer {
    private final String name;

    public UserSubscriber(String name) {
        this.name = name;
    }

    public void update(String message) {
        System.out.println("[Email toi User " + name + "]: " + message);
    }
}

class Librarian implements Observer {
    private final String employeeId;

    public Librarian(String employeeId) {
        this.employeeId = employeeId;
    }

    public void update(String message) {
        System.out.println("[He thong quan tri - NV " + employeeId + "]: " + message);
    }
}
