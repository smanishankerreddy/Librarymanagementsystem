import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Common contract for anything that can perform core book operations.
 * Librarian and Student both implement this interface, but with
 * different rules for who is allowed to issue/return/search - this
 * is where Polymorphism and Interfaces come together in the project.
 */
interface BookOperation {
    void issueBook(int bookId, int studentId) throws BookNotFoundException, InvalidUserException;
    void returnBook(int bookId, int studentId) throws BookNotFoundException;
    void searchBook(String keyword);
}

/**
 * Central data store for the whole system. Instead of plain arrays,
 * the Java Collections Framework is used throughout:
 *   - ArrayList<Book>          -> catalog of books
 *   - HashMap<Integer,Student> -> fast lookup of students by ID
 *   - ArrayList<Transaction>   -> full issue/return/reserve history
 *
 * Admin, Librarian, and Student all share one Library instance so
 * that every role sees the same, consistent data.
 */
public class Library {

    private final List<Book> books = new ArrayList<>();
    private final HashMap<Integer, Student> students = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    // ---------------------------------------------------------------
    // Book management
    // ---------------------------------------------------------------

    public void addBook(Book book) {
        books.add(book);
    }

    public Book getBookById(int bookId) throws BookNotFoundException {
        for (Book b : books) {
            if (b.getBookId() == bookId) {
                return b;
            }
        }
        throw new BookNotFoundException("Book ID " + bookId + " does not exist in the catalog.");
    }

    public void updateBookDetails(int bookId, String title, String author) throws BookNotFoundException {
        Book book = getBookById(bookId);
        book.setTitle(title);
        book.setAuthor(author);
    }

    public void removeBook(int bookId) throws BookNotFoundException {
        Book book = getBookById(bookId);
        books.remove(book);
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(lowerKeyword)
                    || b.getAuthor().toLowerCase().contains(lowerKeyword)
                    || String.valueOf(b.getBookId()).equals(keyword)) {
                results.add(b);
            }
        }
        return results;
    }

    // ---------------------------------------------------------------
    // Student management
    // ---------------------------------------------------------------

    public void registerStudent(Student student) throws InvalidUserException {
        if (student.getId() <= 0 || student.getName() == null || student.getName().trim().isEmpty()) {
            throw new InvalidUserException("Invalid user details: ID must be positive and name cannot be empty.");
        }
        if (students.containsKey(student.getId())) {
            throw new InvalidUserException("A student with ID " + student.getId() + " is already registered.");
        }
        students.put(student.getId(), student);
    }

    public Student getStudentById(int studentId) throws InvalidUserException {
        Student student = students.get(studentId);
        if (student == null) {
            throw new InvalidUserException("No registered student found with ID " + studentId + ".");
        }
        return student;
    }

    // ---------------------------------------------------------------
    // Transaction management
    // ---------------------------------------------------------------

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Finds the active (still ISSUED) transaction for a given book and
     * student, or returns null if no such record exists - used when
     * validating a return request.
     */
    public Transaction findActiveIssue(int bookId, int studentId) {
        for (Transaction t : transactions) {
            if (t.getBookId() == bookId && t.getStudentId() == studentId
                    && t.getStatus() == Transaction.Status.ISSUED) {
                return t;
            }
        }
        return null;
    }

    public List<Transaction> getTransactionsByStudent(int studentId) {
        List<Transaction> history = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getStudentId() == studentId) {
                history.add(t);
            }
        }
        return history;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }
}
