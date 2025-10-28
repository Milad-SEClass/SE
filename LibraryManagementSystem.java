import java.util.*;
import java.time.LocalDate;

public class LibraryManagementSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();
    private static BookManager bookManager = new BookManager();
    private static BorrowManager borrowManager = new BorrowManager();
    private static User currentUser = null;

    public static void main(String[] args) {
        initializeSampleData();

        while (true) {
            if (currentUser == null) {
                showGuestMenu();
            } else {
                switch (currentUser.getRole()) {
                    case STUDENT:
                        showStudentMenu();
                        break;
                    case EMPLOYEE:
                        showEmployeeMenu();
                        break;
                    case MANAGER:
                        showManagerMenu();
                        break;
                }
            }
        }
    }

    private static void initializeSampleData() {
        // ایجاد مدیر سیستم
        userManager.registerEmployee("admin", "admin123", UserRole.MANAGER);

        // ایجاد کارمند نمونه
        userManager.registerEmployee("emp1", "emp123", UserRole.EMPLOYEE);

        // ایجاد کتاب‌های نمونه
        Book book1 = new Book("1", "Java Programming", "John Doe", "Tech Publications", 2020, "1234567890");
        Book book2 = new Book("2", "Data Structures", "Jane Smith", "CS Books", 2019, "0987654321");
        bookManager.addBook(book1);
        bookManager.addBook(book2);

        // ایجاد دانشجوی نمونه
        userManager.registerStudent("student1", "stu123");
    }

    private static void showGuestMenu() {
        System.out.println("\n=== سیستم مدیریت کتابخانه دانشگاه ===");
        System.out.println("1. ثبت‌نام دانشجو");
        System.out.println("2. ورود به سیستم");
        System.out.println("3. جستجوی کتاب");
        System.out.println("4. مشاهده آمار کلی");
        System.out.println("5. خروج");
        System.out.print("انتخاب کنید: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // مصرف newline

        switch (choice) {
            case 1:
                registerStudent();
                break;
            case 2:
                login();
                break;
            case 3:
                searchBooksGuest();
                break;
            case 4:
                showStatistics();
                break;
            case 5:
                System.exit(0);
                break;
            default:
                System.out.println("انتخاب نامعتبر!");
        }
    }

    private static void registerStudent() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        if (userManager.registerStudent(username, password)) {
            System.out.println("ثبت‌نام با موفقیت انجام شد.");
        } else {
            System.out.println("خطا در ثبت‌نام. نام کاربری ممکن است تکراری باشد.");
        }
    }

    private static void login() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        currentUser = userManager.login(username, password);
        if (currentUser != null) {
            System.out.println("خوش آمدید " + currentUser.getUsername() + "!");
        } else {
            System.out.println("نام کاربری یا رمز عبور اشتباه است.");
        }
    }

    private static void searchBooksGuest() {
        System.out.print("عنوان کتاب: ");
        String title = scanner.nextLine();

        List<Book> results = bookManager.searchByTitle(title);
        if (results.isEmpty()) {
            System.out.println("کتابی یافت نشد.");
        } else {
            System.out.println("نتایج جستجو:");
            for (Book book : results) {
                System.out.println(book.getBasicInfo());
            }
        }
    }

    private static void showStatistics() {
        System.out.println("\n=== آمار کلی کتابخانه ===");
        System.out.println("تعداد دانشجویان: " + userManager.getStudentCount());
        System.out.println("تعداد کل کتاب‌ها: " + bookManager.getTotalBooks());
        System.out.println("تعداد کل امانت‌ها: " + borrowManager.getTotalBorrows());
        System.out.println("کتاب‌های در حال امانت: " + borrowManager.getActiveBorrowsCount());
    }

    private static void showStudentMenu() {
        System.out.println("\n=== پنل دانشجو ===");
        System.out.println("1. جستجوی کتاب");
        System.out.println("2. درخواست امانت کتاب");
        System.out.println("3. مشاهده تاریخچه امانت‌ها");
        System.out.println("4. خروج از سیستم");
        System.out.print("انتخاب کنید: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                searchBooksStudent();
                break;
            case 2:
                requestBorrow();
                break;
            case 3:
                viewBorrowHistory();
                break;
            case 4:
                currentUser = null;
                break;
            default:
                System.out.println("انتخاب نامعتبر!");
        }
    }

    private static void searchBooksStudent() {
        System.out.print("عنوان کتاب: ");
        String title = scanner.nextLine();
        System.out.print("نام نویسنده: ");
        String author = scanner.nextLine();
        System.out.print("سال نشر: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        List<Book> results = bookManager.searchBooks(title, author, "", year);
        if (results.isEmpty()) {
            System.out.println("کتابی یافت نشد.");
        } else {
            for (Book book : results) {
                System.out.println(book.getFullInfo() + " - وضعیت: " +
                        (book.isAvailable() ? "موجود" : "امانت داده شده"));
            }
        }
    }

    private static void requestBorrow() {
        System.out.print("شناسه کتاب: ");
        String bookId = scanner.nextLine();

        if (!bookManager.isBookAvailable(bookId)) {
            System.out.println("کتاب موجود نیست یا شناسه کتاب نامعتبر است.");
            return;
        }

        System.out.print("تاریخ شروع (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine();
        System.out.print("تاریخ پایان (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine();

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            if (borrowManager.requestBorrow(currentUser.getUsername(), bookId, startDate, endDate)) {
                System.out.println("درخواست امانت با موفقیت ثبت شد.");
            } else {
                System.out.println("خطا در ثبت درخواست امانت.");
            }
        } catch (Exception e) {
            System.out.println("فرت تاریخ نامعتبر است. از فرمت YYYY-MM-DD استفاده کنید.");
        }
    }

    private static void viewBorrowHistory() {
        List<BorrowRecord> history = borrowManager.getUserBorrowHistory(currentUser.getUsername());
        if (history.isEmpty()) {
            System.out.println("هیچ سابقه امانتی یافت نشد.");
        } else {
            for (BorrowRecord record : history) {
                System.out.println(record.getInfo());
            }
        }
    }

    private static void showEmployeeMenu() {
        System.out.println("\n=== پنل کارمند ===");
        System.out.println("1. تغییر رمز عبور");
        System.out.println("2. ثبت کتاب جدید");
        System.out.println("3. جستجو و ویرایش کتاب");
        System.out.println("4. بررسی درخواست‌های امانت");
        System.out.println("5. مشاهده تاریخچه امانت دانشجو");
        System.out.println("6. مدیریت وضعیت دانشجو");
        System.out.println("7. ثبت دریافت کتاب");
        System.out.println("8. خروج از سیستم");
        System.out.print("انتخاب کنید: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                changePassword();
                break;
            case 2:
                addNewBook();
                break;
            case 3:
                searchAndEditBook();
                break;
            case 4:
                reviewBorrowRequests();
                break;
            case 5:
                viewStudentBorrowHistory();
                break;
            case 6:
                manageStudentStatus();
                break;
            case 7:
                registerBookReturn();
                break;
            case 8:
                currentUser = null;
                break;
            default:
                System.out.println("انتخاب نامعتبر!");
        }
    }

    private static void changePassword() {
        System.out.print("رمز عبور جدید: ");
        String newPassword = scanner.nextLine();
        userManager.changePassword(currentUser.getUsername(), newPassword);
        System.out.println("رمز عبور با موفقیت تغییر یافت.");
    }

    private static void addNewBook() {
        System.out.print("شناسه کتاب: ");
        String id = scanner.nextLine();
        System.out.print("عنوان: ");
        String title = scanner.nextLine();
        System.out.print("نویسنده: ");
        String author = scanner.nextLine();
        System.out.print("ناشر: ");
        String publisher = scanner.nextLine();
        System.out.print("سال نشر: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("شابک: ");
        String isbn = scanner.nextLine();

        Book book = new Book(id, title, author, publisher, year, isbn);
        if (bookManager.addBook(book)) {
            userManager.recordBookAdded(currentUser.getUsername());
            System.out.println("کتاب با موفقیت ثبت شد.");
        } else {
            System.out.println("خطا در ثبت کتاب. شناسه کتاب ممکن است تکراری باشد.");
        }
    }

    // متد جدید: جستجو و ویرایش کتاب
    private static void searchAndEditBook() {
        System.out.print("عنوان کتاب برای جستجو: ");
        String title = scanner.nextLine();

        List<Book> results = bookManager.searchByTitle(title);
        if (results.isEmpty()) {
            System.out.println("کتابی یافت نشد.");
            return;
        }

        System.out.println("نتایج جستجو:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getFullInfo());
        }

        System.out.print("شماره کتاب برای ویرایش (0 برای انصراف): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0 || choice > results.size()) {
            return;
        }

        Book selectedBook = results.get(choice - 1);
        editBook(selectedBook);
    }

    // متد جدید: ویرایش کتاب
    private static void editBook(Book book) {
        System.out.println("ویرایش کتاب: " + book.getTitle());
        System.out.println("1. ویرایش عنوان");
        System.out.println("2. ویرایش نویسنده");
        System.out.println("3. ویرایش ناشر");
        System.out.println("4. ویرایش سال نشر");
        System.out.println("5. ویرایش وضعیت موجودیت");
        System.out.print("انتخاب کنید: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("عنوان جدید: ");
                String newTitle = scanner.nextLine();
                book.setTitle(newTitle);
                break;
            case 2:
                System.out.print("نویسنده جدید: ");
                String newAuthor = scanner.nextLine();
                book.setAuthor(newAuthor);
                break;
            case 3:
                System.out.print("ناشر جدید: ");
                String newPublisher = scanner.nextLine();
                book.setPublisher(newPublisher);
                break;
            case 4:
                System.out.print("سال نشر جدید: ");
                int newYear = scanner.nextInt();
                scanner.nextLine();
                book.setPublicationYear(newYear);
                break;
            case 5:
                book.setAvailable(!book.isAvailable());
                System.out.println("وضعیت موجودیت تغییر یافت به: " + (book.isAvailable() ? "موجود" : "امانت داده شده"));
                break;
            default:
                System.out.println("انتخاب نامعتبر!");
                return;
        }

        System.out.println("اطلاعات کتاب با موفقیت به‌روزرسانی شد.");
    }

    // متد جدید: بررسی درخواست‌های امانت
    private static void reviewBorrowRequests() {
        List<BorrowRecord> pendingRequests = borrowManager.getPendingRequests();
        if (pendingRequests.isEmpty()) {
            System.out.println("هیچ درخواست امانت در حال انتظار وجود ندارد.");
            return;
        }

        System.out.println("درخواست‌های امانت در حال انتظار:");
        for (int i = 0; i < pendingRequests.size(); i++) {
            BorrowRecord record = pendingRequests.get(i);
            System.out.println((i + 1) + ". " + record.getInfo());
        }

        System.out.print("شماره درخواست برای تایید (0 برای انصراف): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0 || choice > pendingRequests.size()) {
            return;
        }

        BorrowRecord selectedRequest = pendingRequests.get(choice - 1);
        if (borrowManager.approveBorrow(selectedRequest.getId(), currentUser.getUsername())) {
            userManager.recordBookBorrowed(currentUser.getUsername());
            System.out.println("درخواست امانت تایید شد.");
        } else {
            System.out.println("خطا در تایید درخواست امانت.");
        }
    }

    // متد جدید: مشاهده تاریخچه امانت دانشجو
    private static void viewStudentBorrowHistory() {
        System.out.print("نام کاربری دانشجو: ");
        String username = scanner.nextLine();

        List<BorrowRecord> history = borrowManager.getUserBorrowHistory(username);
        if (history.isEmpty()) {
            System.out.println("هیچ سابقه امانتی برای این دانشجو یافت نشد.");
        } else {
            System.out.println("تاریخچه امانت دانشجو " + username + ":");
            for (BorrowRecord record : history) {
                System.out.println(record.getInfo());
            }

            // نمایش آمار
            EmployeePerformance.StudentBorrowStats stats = borrowManager.getStudentBorrowStats(username);
            System.out.println("\nآمار دانشجو:");
            System.out.println("تعداد کل امانت‌ها: " + stats.getTotalBorrows());
            System.out.println("تعداد کتاب‌های تحویل داده نشده: " + stats.getNotReturnedCount());
            System.out.println("تعداد امانت‌های با تاخیر: " + stats.getDelayedReturnsCount());
        }
    }

    // متد جدید: مدیریت وضعیت دانشجو
    private static void manageStudentStatus() {
        System.out.print("نام کاربری دانشجو: ");
        String username = scanner.nextLine();

        if (userManager.toggleStudentStatus(username)) {
            User student = userManager.login(username, ""); // فقط برای بررسی وضعیت
            if (student != null) {
                System.out.println("وضعیت دانشجو تغییر یافت به: " + (student.isActive() ? "فعال" : "غیرفعال"));
            }
        } else {
            System.out.println("خطا در تغییر وضعیت دانشجو. دانشجو یافت نشد.");
        }
    }

    // متد جدید: ثبت دریافت کتاب
    private static void registerBookReturn() {
        System.out.print("شناسه درخواست امانت: ");
        String borrowId = scanner.nextLine();

        if (borrowManager.returnBook(borrowId, LocalDate.now())) {
            userManager.recordBookReturned(currentUser.getUsername());
            System.out.println("دریافت کتاب با موفقیت ثبت شد.");
        } else {
            System.out.println("خطا در ثبت دریافت کتاب. شناسه درخواست نامعتبر است.");
        }
    }

    private static void showManagerMenu() {
        System.out.println("\n=== پنل مدیر ===");
        System.out.println("1. تعریف کارمند جدید");
        System.out.println("2. مشاهده عملکرد کارمندان");
        System.out.println("3. مشاهده آمار امانت‌ها");
        System.out.println("4. مشاهده آمار دانشجویان");
        System.out.println("5. خروج از سیستم");
        System.out.print("انتخاب کنید: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                registerEmployee();
                break;
            case 2:
                viewEmployeePerformance();
                break;
            case 3:
                viewBorrowStatistics();
                break;
            case 4:
                viewStudentStatistics();
                break;
            case 5:
                currentUser = null;
                break;
            default:
                System.out.println("انتخاب نامعتبر!");
        }
    }

    private static void registerEmployee() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        if (userManager.registerEmployee(username, password, UserRole.EMPLOYEE)) {
            System.out.println("کارمند با موفقیت ثبت شد.");
        } else {
            System.out.println("خطا در ثبت کارمند. نام کاربری ممکن است تکراری باشد.");
        }
    }

    // متد جدید: مشاهده عملکرد کارمندان
    private static void viewEmployeePerformance() {
        List<EmployeePerformance> performanceList = userManager.getEmployeePerformance();
        if (performanceList.isEmpty()) {
            System.out.println("هیچ کارمندی ثبت نشده است.");
            return;
        }

        System.out.println("عملکرد کارمندان:");
        for (EmployeePerformance perf : performanceList) {
            System.out.println("کارمند: " + perf.getUsername());
            System.out.println("  - کتاب‌های ثبت شده: " + perf.getBooksAdded());
            System.out.println("  - کتاب‌های امانت داده شده: " + perf.getBooksBorrowed());
            System.out.println("  - کتاب‌های تحویل گرفته شده: " + perf.getBooksReturned());
            System.out.println();
        }
    }

    // متد جدید: مشاهده آمار امانت‌ها
    private static void viewBorrowStatistics() {
        BorrowStatistics stats = borrowManager.getBorrowStatistics();
        System.out.println("\n=== آمار امانت‌ها ===");
        System.out.println("تعداد درخواست‌های امانت ثبت شده: " + stats.getTotalRequests());
        System.out.println("تعداد کل امانت‌های داده شده: " + stats.getTotalBorrowed());
        System.out.println("میانگین مدت امانت (روز): " + String.format("%.2f", stats.getAverageBorrowDays()));
    }

    // متد جدید: مشاهده آمار دانشجویان
    private static void viewStudentStatistics() {
        System.out.println("\n=== آمار دانشجویان ===");

        List<User> students = userManager.getAllStudents();
        System.out.println("تعداد کل دانشجویان: " + students.size());

        for (User student : students) {
            EmployeePerformance.StudentBorrowStats stats = borrowManager.getStudentBorrowStats(student.getUsername());
            System.out.println("\nدانشجو: " + student.getUsername() + " (وضعیت: " + (student.isActive() ? "فعال" : "غیرفعال") + ")");
            System.out.println("  - تعداد کل امانت‌ها: " + stats.getTotalBorrows());
            System.out.println("  - کتاب‌های تحویل داده نشده: " + stats.getNotReturnedCount());
            System.out.println("  - امانت‌های با تاخیر: " + stats.getDelayedReturnsCount());
        }

        // نمایش 10 دانشجوی با بیشترین تاخیر
        System.out.println("\n=== 10 دانشجوی با بیشترین تاخیر در تحویل کتاب ===");
        List<Map.Entry<String, Long>> topDelayed = borrowManager.getTopStudentsWithDelays();
        if (topDelayed.isEmpty()) {
            System.out.println("هیچ دانشجویی با تاخیر وجود ندارد.");
        } else {
            for (int i = 0; i < topDelayed.size() && i < 10; i++) {
                Map.Entry<String, Long> entry = topDelayed.get(i);
                System.out.println((i + 1) + ". " + entry.getKey() + " - " + entry.getValue() + " روز تاخیر");
            }
        }
    }
}