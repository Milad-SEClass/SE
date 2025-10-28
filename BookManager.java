import java.util.*;
import java.util.stream.Collectors;

public class BookManager {
    private Map<String, Book> books;

    public BookManager() {
        books = new HashMap<>();
    }

    public boolean addBook(Book book) {
        if (books.containsKey(book.getId())) {
            return false;
        }
        books.put(book.getId(), book);
        return true;
    }

    public boolean updateBook(String bookId, Book updatedBook) {
        if (!books.containsKey(bookId)) {
            return false;
        }
        books.put(bookId, updatedBook);
        return true;
    }

    public Book getBook(String bookId) {
        return books.get(bookId);
    }

    public List<Book> searchBooks(String title, String author, String publisher, int year) {
        return books.values().stream()
                .filter(book ->
                        (title.isEmpty() || book.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                                (author.isEmpty() || book.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                                (publisher.isEmpty() || book.getPublisher().toLowerCase().contains(publisher.toLowerCase())) &&
                                (year == 0 || book.getPublicationYear() == year)
                )
                .collect(Collectors.toList());
    }

    public List<Book> searchByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public int getTotalBooks() {
        return books.size();
    }

    public boolean isBookAvailable(String bookId) {
        Book book = books.get(bookId);
        return book != null && book.isAvailable();
    }

    public void setBookAvailability(String bookId, boolean available) {
        Book book = books.get(bookId);
        if (book != null) {
            book.setAvailable(available);
        }
    }
}