import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowManager {
    private Map<String, BorrowRecord> borrowRecords;
    private int borrowCounter;

    public BorrowManager() {
        borrowRecords = new HashMap<>();
        borrowCounter = 1;
    }

    public boolean requestBorrow(String studentUsername, String bookId, LocalDate startDate, LocalDate endDate) {
        String borrowId = "B" + borrowCounter++;
        BorrowRecord record = new BorrowRecord(borrowId, studentUsername, bookId, startDate, endDate);
        borrowRecords.put(borrowId, record);
        return true;
    }

    public boolean approveBorrow(String borrowId, String employeeUsername) {
        BorrowRecord record = borrowRecords.get(borrowId);
        if (record != null && record.getStatus() == BorrowStatus.REQUESTED) {
            record.setStatus(BorrowStatus.APPROVED);
            record.setEmployeeUsername(employeeUsername);
            return true;
        }
        return false;
    }

    public boolean registerBorrow(String borrowId) {
        BorrowRecord record = borrowRecords.get(borrowId);
        if (record != null && record.getStatus() == BorrowStatus.APPROVED) {
            record.setStatus(BorrowStatus.BORROWED);
            return true;
        }
        return false;
    }

    public boolean returnBook(String borrowId, LocalDate returnDate) {
        BorrowRecord record = borrowRecords.get(borrowId);
        if (record != null && record.getStatus() == BorrowStatus.BORROWED) {
            record.setStatus(BorrowStatus.RETURNED);
            record.setActualReturnDate(returnDate);
            return true;
        }
        return false;
    }

    public List<BorrowRecord> getPendingRequests() {
        return borrowRecords.values().stream()
                .filter(record -> record.getStatus() == BorrowStatus.REQUESTED &&
                        (record.getStartDate().isEqual(LocalDate.now()) ||
                                record.getStartDate().isBefore(LocalDate.now())))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<BorrowRecord> getUserBorrowHistory(String username) {
        return borrowRecords.values().stream()
                .filter(record -> record.getStudentUsername().equals(username))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public int getTotalBorrows() {
        return borrowRecords.size();
    }

    public int getActiveBorrowsCount() {
        return (int) borrowRecords.values().stream()
                .filter(record -> record.getStatus() == BorrowStatus.BORROWED)
                .count();
    }

    public BorrowStatistics getBorrowStatistics() {
        BorrowStatistics stats = new BorrowStatistics();

        List<BorrowRecord> allRecords = new ArrayList<>(borrowRecords.values());

        stats.setTotalRequests(allRecords.size());
        stats.setTotalBorrowed((int) allRecords.stream()
                .filter(r -> r.getStatus() == BorrowStatus.BORROWED || r.getStatus() == BorrowStatus.RETURNED)
                .count());

        // محاسبه میانگین مدت امانت
        double avgDays = allRecords.stream()
                .filter(r -> r.getStatus() == BorrowStatus.RETURNED && r.getActualReturnDate() != null)
                .mapToLong(r -> ChronoUnit.DAYS.between(r.getStartDate(), r.getActualReturnDate()))
                .average()
                .orElse(0.0);
        stats.setAverageBorrowDays(avgDays);

        return stats;
    }

    public EmployeePerformance.StudentBorrowStats getStudentBorrowStats(String username) {
        EmployeePerformance.StudentBorrowStats stats = new EmployeePerformance.StudentBorrowStats(username);

        List<BorrowRecord> studentRecords = getUserBorrowHistory(username);

        stats.setTotalBorrows(studentRecords.size());
        stats.setNotReturnedCount((int) studentRecords.stream()
                .filter(r -> r.getStatus() == BorrowStatus.BORROWED && r.isOverdue())
                .count());
        stats.setDelayedReturnsCount((int) studentRecords.stream()
                .filter(r -> r.getStatus() == BorrowStatus.RETURNED &&
                        r.getActualReturnDate().isAfter(r.getEndDate()))
                .count());

        return stats;
    }

    public List<Map.Entry<String, Long>> getTopStudentsWithDelays() {
        Map<String, Long> studentDelays = new HashMap<>();

        for (BorrowRecord record : borrowRecords.values()) {
            if (record.getStatus() == BorrowStatus.RETURNED &&
                    record.getActualReturnDate().isAfter(record.getEndDate())) {

                long delayDays = ChronoUnit.DAYS.between(record.getEndDate(), record.getActualReturnDate());
                studentDelays.merge(record.getStudentUsername(), delayDays, Long::sum);
            }
        }

        return studentDelays.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}