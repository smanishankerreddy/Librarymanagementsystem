/**
 * Abstract base class representing any user of the Smart Library
 * Management System.
 *
 * Admin, Librarian, and Student all share common identity fields
 * (id, name), so those are defined once here (Abstraction). Each
 * subclass must provide its own login() behavior (Polymorphism via
 * method overriding).
 */
public abstract class User {

    protected int id;
    protected String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Every role logs in differently, so subclasses must implement this.
    public abstract void login();

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "'}";
    }
}
