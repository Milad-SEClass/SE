import java.util.*;

public class UserManager {
    private Map<String, User> users;
    private Map<String, EmployeePerformance> employeePerformance;

    public UserManager() {
        users = new HashMap<>();
        employeePerformance = new HashMap<>();
    }

    public boolean registerStudent(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        User student = new User(username, password, UserRole.STUDENT);
        users.put(username, student);
        return true;
    }

    public boolean registerEmployee(String username, String password, UserRole role) {
        if (users.containsKey(username)) {
            return false;
        }
        User employee = new User(username, password, role);
        users.put(username, employee);
        employeePerformance.put(username, new EmployeePerformance(username));
        return true;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password) && user.isActive()) {
            return user;
        }
        return null;
    }

    public void changePassword(String username, String newPassword) {
        User user = users.get(username);
        if (user != null) {
            user.setPassword(newPassword);
        }
    }

    public boolean toggleStudentStatus(String username) {
        User student = users.get(username);
        if (student != null && student.getRole() == UserRole.STUDENT) {
            student.setActive(!student.isActive());
            return true;
        }
        return false;
    }

    public int getStudentCount() {
        return (int) users.values().stream()
                .filter(user -> user.getRole() == UserRole.STUDENT)
                .count();
    }

    public List<User> getAllStudents() {
        return users.values().stream()
                .filter(user -> user.getRole() == UserRole.STUDENT)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void recordBookAdded(String employeeUsername) {
        EmployeePerformance perf = employeePerformance.get(employeeUsername);
        if (perf != null) {
            perf.incrementBooksAdded();
        }
    }

    public void recordBookBorrowed(String employeeUsername) {
        EmployeePerformance perf = employeePerformance.get(employeeUsername);
        if (perf != null) {
            perf.incrementBooksBorrowed();
        }
    }

    public void recordBookReturned(String employeeUsername) {
        EmployeePerformance perf = employeePerformance.get(employeeUsername);
        if (perf != null) {
            perf.incrementBooksReturned();
        }
    }

    public List<EmployeePerformance> getEmployeePerformance() {
        return new ArrayList<>(employeePerformance.values());
    }
}