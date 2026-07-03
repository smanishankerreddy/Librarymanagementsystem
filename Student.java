import java.util.List;

/**
 * A Student can search the catalog, reserve a book that's currently
 * unavailable, borrow (self-issue) an available book, return it, and
 * view their own transaction history. Implements BookOperation with
 * self-service rules, distinct from the Librarian's staff rules.
 */
public class Student extends User implements BookOperation {

    private final Library library;

    public Student(int id, String name, Library library) {
        super(id, name);
        this.library = library;
    }

    @Override
    public void login() {
        System.out.println(name + " (Student) logged in. Permissions: search, reserve, borrow books, view history.");
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

    public void reserveBook(int bookId) throws BookNotFoundException {
        Book book = library.getBookById(bookId);
        if (book.isAvailable()) {
            System.out.println("\"" + book.getTitle() + "\" is currently available - you can borrow it directly instead of reserving.");
            return;
        }
        Transaction transaction = new Transaction(this.id, bookId, Transaction.Status.RESERVED);
        library.addTransaction(transaction);
        System.out.println(name + " reserved: \"" + book.getTitle() + "\"");
    }

    @Override
    public void issueBook(int bookId, int studentId) throws BookNotFoundException, InvalidUserException {
        Book book = library.getBookById(bookId);
        if (!book.isAvailable()) {
            throw new BookNotFoundException("Book ID " + bookId + " is not available to borrow right now.");
        }
        book.setAvailable(false);
        Transaction transaction = new Transaction(studentId, bookId, Transaction.Status.ISSUED);
        library.addTransaction(transaction);
        System.out.println(name + " borrowed: \"" + book.getTitle() + "\"");
    }

    /**
     * Friendly wrapper so a student "borrows" a book for themself,
     * rather than calling issueBook(bookId, someOtherId) directly.
     */
    public void borrowBook(int bookId) throws BookNotFoundException, InvalidUserException {
        issueBook(bookId, this.id);
    }

    @Override
    public void returnBook(int bookId, int studentId) throws BookNotFoundException {
        Transaction transaction = library.findActiveIssue(bookId, studentId);
        if (transaction == null) {
            throw new BookNotFoundException("You have not issued Book ID " + bookId + ".");
        }
        transaction.markReturned();
        Book book = library.getBookById(bookId);
        book.setAvailable(true);
        System.out.println(name + " returned: \"" + book.getTitle() + "\"");
    }

    public void viewHistory() {
        List<Transaction> history = library.getTransactionsByStudent(this.id);
        if (history.isEmpty()) {
            System.out.println(name + " has no transaction history yet.");
        } else {
            System.out.println(name + "'s transaction history:");
            for (Transaction t : history) {
                System.out.println("  " + t);
            }
        }
    }
}
