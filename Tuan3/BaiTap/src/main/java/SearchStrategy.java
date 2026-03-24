import java.util.List;
import java.util.stream.Collectors;

// Strategy Pattern
interface SearchStrategy {
    List<Book> search(List<Book> books, String keyword);
}

class SearchByName implements SearchStrategy {
    public List<Book> search(List<Book> books, String keyword) {
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}

class SearchByAuthor implements SearchStrategy {
    public List<Book> search(List<Book> books, String keyword) {
        return books.stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}

class SearchContext {
    private SearchStrategy strategy;

    public void setSearchStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Book> executeSearch(List<Book> books, String keyword) {
        if (strategy == null) {
            throw new IllegalStateException("Chua thiet lap chien luoc tim kiem!");
        }
        return strategy.search(books, keyword);
    }
}
