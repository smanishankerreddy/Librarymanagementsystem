/**
 * Demonstration driver for the Smart Library Management System.
 * Walks through Admin, Librarian, and Student workflows end-to-end,
 * including the exception-handling paths for invalid operations.
 */
public class Main {

    public static void main(String[] args) {
        Library library = new Library();

        Admin admin = new Admin(1, "Ravi Kumar", library);
        Librarian librarian = new Librarian(2, "Sunita Rao", library);

        System.out.println("===== Smart Library Management System =====\n");

        // Polymorphism: the same login() call behaves differently per role
        User[] staff = { admin, librarian };
        for (User u : staff) {
            u.login();
        }

        System.out.println("\n--- Admin adds books to the catalog ---");
        admin.addBook(101, "Effective Java", "Joshua Bloch");
        admin.addBook(102, "Clean Code", "Robert C. Martin");
        admin.addBook(103, "Design Patterns", "Erich Gamma");

        System.out.println("\n--- Admin registers students ---");
        Student aditi = new Student(201, "Aditi Sharma", library);
        Student rahul = new Student(202, "Rahul Verma", library);
        try {
            admin.registerStudent(aditi);
            admin.registerStudent(rahul);
        } catch (InvalidUserException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }

        aditi.login();
        rahul.login();

        System.out.println("\n--- Student searches for a book ---");
        aditi.searchBook("Clean");

        System.out.println("\n--- Librarian issues a book to Aditi ---");
        try {
            librarian.issueBook(102, aditi.getId());
        } catch (BookNotFoundException | InvalidUserException e) {
            System.out.println("Issue failed: " + e.getMessage());
        }

        System.out.println("\n--- Rahul tries to borrow the same book (already issued) ---");
        try {
            rahul.borrowBook(102);
        } catch (BookNotFoundException | InvalidUserException e) {
            System.out.println("Borrow failed: " + e.getMessage());
        }

        System.out.println("\n--- Rahul reserves it instead ---");
        try {
            rahul.reserveBook(102);
        } catch (BookNotFoundException e) {
            System.out.println("Reservation failed: " + e.getMessage());
        }

        System.out.println("\n--- Rahul tries to return a book he never issued ---");
        try {
            rahul.returnBook(102, rahul.getId());
        } catch (BookNotFoundException e) {
            System.out.println("Return failed: " + e.getMessage());
        }

        System.out.println("\n--- Fine calculation example (5 days late) ---");
        double sampleFine = librarian.calculateFine(5);
        System.out.println("Late Days: 5 x Fine Per Day: Rs." + (int) librarian.getFinePerDay()
                + " = Rs." + sampleFine);

        System.out.println("\n--- Aditi returns the book on time ---");
        try {
            librarian.returnBook(102, aditi.getId());
        } catch (BookNotFoundException e) {
            System.out.println("Return failed: " + e.getMessage());
        }

        System.out.println("\n--- Admin tries to update a book that doesn't exist ---");
        try {
            admin.updateBook(9999, "Unknown", "Unknown");
        } catch (BookNotFoundException e) {
            System.out.println("Update failed: " + e.getMessage());
        }

        System.out.println("\n--- Attempt to register a student with invalid details ---");
        try {
            Student invalid = new Student(-5, "", library);
            admin.registerStudent(invalid);
        } catch (InvalidUserException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }

        System.out.println("\n--- Aditi's transaction history ---");
        aditi.viewHistory();

        System.out.println("\n--- Admin monitors all library activity ---");
        admin.monitorActivity();

        System.out.println("\n===== End of demo =====");
    }
}
