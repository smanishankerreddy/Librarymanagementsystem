/**
 * Thrown when an operation refers to a book that does not exist in
 * the catalog, is currently unavailable, or was never issued to the
 * student attempting to return it.
 */
public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
