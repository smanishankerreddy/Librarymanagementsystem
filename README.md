# Smart Library Management System

A Java-based console application that automates library operations —
managing books, users, and transactions — replacing manual, paper-based
library management with a digital system.

## Overview

Built with Core Java and Object-Oriented Programming principles, the system
supports three roles — **Admin**, **Librarian**, and **Student** — each with
their own permissions and workflows, backed by a shared in-memory data store
built on the Java Collections Framework.

## Project Structure

```
LibraryManagementSystem/
│
├── Main.java                    # Demo driver — runs a full end-to-end walkthrough
├── User.java                    # Abstract base class shared by all roles
├── Admin.java                   # Manages books and users, monitors activity
├── Librarian.java               # Issues/returns books, calculates fines
├── Student.java                 # Searches, reserves, borrows books, views history
├── Book.java                    # Book entity
├── Transaction.java             # Issue / return / reserve record
├── Library.java                 # Shared data store + BookOperation interface
├── BookNotFoundException.java   # Custom checked exception
└── InvalidUserException.java    # Custom checked exception
```

## Roles & Responsibilities

| Role | Capabilities |
|---|---|
| **Admin** | Add, update, and delete books; register students; monitor all library transactions |
| **Librarian** | Issue and return books; calculate late fines; keep availability in sync |
| **Student** | Search books; reserve unavailable ones; borrow available ones; view their own transaction history |

## OOP Concepts Applied

- **Inheritance** — `Admin`, `Librarian`, and `Student` all extend the
  abstract `User` class, sharing common `id`/`name` fields.
- **Abstraction** — `User` defines the shared contract (`login()`) without
  dictating how each role implements it.
- **Polymorphism** — each role overrides `login()` differently; `Librarian`
  and `Student` both implement `BookOperation`'s `issueBook`/`returnBook`/
  `searchBook` with different rules (staff issue vs. self-service borrow).
- **Interfaces** — `BookOperation` defines the common book-handling contract
  implemented by both `Librarian` and `Student`.
- **Collections Framework** — `ArrayList<Book>` for the catalog,
  `HashMap<Integer, Student>` for fast student lookup, and
  `ArrayList<Transaction>` for the full history, instead of fixed-size arrays.
- **Exception Handling** — custom checked exceptions (`BookNotFoundException`,
  `InvalidUserException`) prevent invalid operations (issuing an unavailable
  book, returning a book never issued, registering a student with bad
  details) from crashing the program.

## Fine Calculation

```
Fine = Late Days × Fine Per Day
```

Example: 5 days late × ₹10/day = **₹50**

## How to Compile & Run

```bash
javac *.java
java Main
```

`Main.java` runs a scripted demo (no input required) that walks through:
1. Admin adding books and registering students
2. Polymorphic `login()` calls across roles
3. A librarian issuing a book to a student
4. A second student's borrow attempt failing because the book is taken
   (`BookNotFoundException`)
5. That student reserving the book instead
6. A failed return attempt for a book never issued (`BookNotFoundException`)
7. The fine calculation formula in action
8. A successful on-time return
9. An invalid book update (`BookNotFoundException`)
10. Invalid student registration — empty name, negative ID
    (`InvalidUserException`)
11. A student's transaction history
12. The admin's full activity log

## Note

This was written and reviewed in an environment without a full JDK
available to compile against (only a JRE was present), so it hasn't been
compiler-verified here. The logic was manually traced through end-to-end
against `Main.java`, but please compile it in your own environment first —
if you hit any compiler error, share it and it can be fixed directly.

## Tech Stack

- Java (Core Java, JDK 17+ recommended)
- Java Collections Framework (`ArrayList`, `HashMap`)
- `java.time` for issue/return dates
