public class Book {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private String isbn;
    private boolean available;

    public Book(String id, String title, String author, String publisher, int publicationYear, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.available = true;
    }

    // Getter and Setter methods
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getBasicInfo() {
        return String.format("ID: %s, Title: %s, Author: %s", id, title, author);
    }

    public String getFullInfo() {
        return String.format("ID: %s, Title: %s, Author: %s, Publisher: %s, Year: %d, ISBN: %s",
                id, title, author, publisher, publicationYear, isbn);
    }
}