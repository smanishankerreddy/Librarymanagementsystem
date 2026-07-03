/**
 * Thrown when a student/user cannot be found, or when registration
 * details (ID, name) are invalid.
 */
public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }
}
