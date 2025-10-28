// آمار امانت
public class BorrowStatistics {
    private int totalRequests;
    private int totalBorrowed;
    private double averageBorrowDays;

    // Getter and Setter methods
    public int getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

    public int getTotalBorrowed() {
        return totalBorrowed;
    }

    public void setTotalBorrowed(int totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }

    public double getAverageBorrowDays() {
        return averageBorrowDays;
    }

    public void setAverageBorrowDays(double averageBorrowDays) {
        this.averageBorrowDays = averageBorrowDays;
    }
}
