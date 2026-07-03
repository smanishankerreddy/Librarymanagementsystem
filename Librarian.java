import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * The Librarian issues and returns books, calculates late fines, and
 * keeps book availability in sync. Implements BookOperation - the
 * same interface Student implements, but with staff-level rules
 * (Polymorphism: same method signatures, different behavior).
 */
public class Librarian extends User implements BookOperation {

    private final Library library;
    private static final int ALLOWED_DAYS = 14;
    private static final double FINE_PER_DAY = 10.0;

    public Librarian(int id, String name, Library library) {
        super(id, name);
        this.library = library;
    }

    @Override
    public void login() {
        System.out.println(name + " (Librarian) logged in. Permissions: issue/return books, calculate fines.");
    }

    @Override
    public void issueBook(int bookId, int studentId) throws BookNotFoundException, InvalidUserException {
        Book book = library.getBookById(bookId);
        if (!book.isAvailable()) {
            throw new BookNotFoundException("Book ID " + bookId + " is currently unavailable.");
        }
        Student student = library.getStudentById(studentId);

        book.setAvailable(false);
        Transaction transaction = new Transaction(studentId, bookId, Transaction.Status.ISSUED);
        library.addTransaction(transaction);
        System.out.println("Book issued: \"" + book.getTitle() + "\" to " + student.getName());
    }

    @Override
    public void returnBook(int bookId, int studentId) throws BookNotFoundException {
        Transaction transaction = library.findActiveIssue(bookId, studentId);
        if (transaction == null) {
            throw new BookNotFoundException(
                    "No active issue record found for Book ID " + bookId + " and Student ID " + studentId + ".");
        }

        long lateDays = ChronoUnit.DAYS.between(transaction.getIssueDate(), LocalDate.now()) - ALLOWED_DAYS;
        double fine = calculateFine((int) Math.max(lateDays, 0));

        transaction.markReturned();
        Book book = library.getBookById(bookId);
        book.setAvailable(true);

        if (fine > 0) {
            System.out.println("Book returned late by " + lateDays + " day(s). Fine: Rs." + fine);
        } else {
            System.out.println("Book returned on time. No fine.");
        }
    }

    @Override
    public void searchBook(String keyword) {
        var results = library.searchBooks(keyword);
        if (results.isEmpty()) {
            System.out.println("No books found matching: " + keyword);
        } else {
            System.out.println("Search results for \"" + keyword + "\":");
            for (Book b : results) {
                System.out.println("  " + b);
            }
        }
    }

    /**
     * Fine = Late Days x Fine Per Day.
     */
    public double calculateFine(int lateDays) {
        return lateDays > 0 ? lateDays * FINE_PER_DAY : 0;
    }

    public double getFinePerDay() {
        return FINE_PER_DAY;
    }
}
