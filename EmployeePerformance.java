// عملکرد کارمند
public class EmployeePerformance {
    private String username;
    private int booksAdded;
    private int booksBorrowed;
    private int booksReturned;

    public EmployeePerformance(String username) {
        this.username = username;
    }

    // Getter and Setter methods
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBooksAdded() {
        return booksAdded;
    }

    public void incrementBooksAdded() {
        this.booksAdded++;
    }

    public int getBooksBorrowed() {
        return booksBorrowed;
    }

    public void incrementBooksBorrowed() {
        this.booksBorrowed++;
    }

    public int getBooksReturned() {
        return booksReturned;
    }

    public void incrementBooksReturned() {
        this.booksReturned++;
    }

    // آمار دانشجو
    public static class StudentBorrowStats {
        private String username;
        private int totalBorrows;
        private int notReturnedCount;
        private int delayedReturnsCount;

        public StudentBorrowStats(String username) {
            this.username = username;
        }

        // Getter and Setter methods
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public int getTotalBorrows() { return totalBorrows; }
        public void setTotalBorrows(int totalBorrows) { this.totalBorrows = totalBorrows; }

        public int getNotReturnedCount() { return notReturnedCount; }
        public void setNotReturnedCount(int notReturnedCount) { this.notReturnedCount = notReturnedCount; }

        public int getDelayedReturnsCount() { return delayedReturnsCount; }
        public void setDelayedReturnsCount(int delayedReturnsCount) { this.delayedReturnsCount = delayedReturnsCount; }
    }
}
