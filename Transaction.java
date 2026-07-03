import java.time.LocalDate;

/**
 * Represents one transaction record: a book being issued, returned,
 * or reserved by a student. The Library keeps every Transaction ever
 * created, which is what powers "transaction history" and the
 * Admin's "monitor all activity" view.
 */
public class Transaction {

    public enum Status {
        ISSUED, RETURNED, RESERVED
    }

    private static int counter = 1;

    private final int transactionId;
    private final int studentId;
    private final int bookId;
    private final LocalDate issueDate;
    private LocalDate returnDate;
    private Status status;

    public Transaction(int studentId, int bookId, Status status) {
        this.transactionId = counter++;
        this.studentId = studentId;
        this.bookId = bookId;
        this.issueDate = LocalDate.now();
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Status getStatus() {
        return status;
    }

    public void markReturned() {
        this.returnDate = LocalDate.now();
        this.status = Status.RETURNED;
    }

    @Override
    public String toString() {
        return String.format("Txn#%d | Student:%d | Book:%d | Issued:%s | Returned:%s | Status:%s",
                transactionId, studentId, bookId, issueDate,
                returnDate == null ? "-" : returnDate, status);
    }
}
