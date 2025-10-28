import java.time.LocalDate;

public class BorrowRecord {
    private String id;
    private String studentUsername;
    private String bookId;
    private LocalDate requestDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;
    private String employeeUsername;
    private BorrowStatus status;

    public BorrowRecord(String id, String studentUsername, String bookId, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.studentUsername = studentUsername;
        this.bookId = bookId;
        this.requestDate = LocalDate.now();
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = BorrowStatus.REQUESTED;
    }

    // Getter and Setter methods
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String studentUsername) { this.studentUsername = studentUsername; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public String getEmployeeUsername() { return employeeUsername; }
    public void setEmployeeUsername(String employeeUsername) { this.employeeUsername = employeeUsername; }

    public BorrowStatus getStatus() { return status; }
    public void setStatus(BorrowStatus status) { this.status = status; }

    public String getInfo() {
        return String.format("Borrow ID: %s, Book: %s, Period: %s to %s, Status: %s",
                id, bookId, startDate, endDate, status);
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(endDate) && status == BorrowStatus.BORROWED;
    }

    public long getDaysOverdue() {
        if (isOverdue()) {
          //  return ChronoUnit.DAYS.between(endDate, LocalDate.now());
        }
        return 0;
    }
}