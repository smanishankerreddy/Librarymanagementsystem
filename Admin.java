import java.util.List;

/**
 * The Admin manages the book catalog and registers new users, and
 * can monitor every transaction happening in the system.
 */
public class Admin extends User {

    private final Library library;

    public Admin(int id, String name, Library library) {
        super(id, name);
        this.library = library;
    }

    @Override
    public void login() {
        System.out.println(name + " (Admin) logged in. Permissions: manage books, manage users, monitor activity.");
    }

    public void addBook(int bookId, String title, String author) {
        library.addBook(new Book(bookId, title, author));
        System.out.println("Book added: [" + bookId + "] " + title);
    }

    public void updateBook(int bookId, String title, String author) throws BookNotFoundException {
        library.updateBookDetails(bookId, title, author);
        System.out.println("Book updated: ID " + bookId);
    }

    public void deleteBook(int bookId) throws BookNotFoundException {
        library.removeBook(bookId);
        System.out.println("Book deleted: ID " + bookId);
    }

    public void registerStudent(Student student) throws InvalidUserException {
        library.registerStudent(student);
        System.out.println("Student registered: [" + student.getId() + "] " + student.getName());
    }

    public void monitorActivity() {
        List<Transaction> all = library.getAllTransactions();
        System.out.println("---- All Library Transactions (" + all.size() + ") ----");
        for (Transaction t : all) {
            System.out.println("  " + t);
        }
    }
}
