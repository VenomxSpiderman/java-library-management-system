# Library Management System

A simple command-line library management system built in Java that demonstrates object-oriented programming principles.

## What it does

This system helps manage a library by tracking books, magazines, members, and borrowing transactions. It calculates fines for overdue items and prevents members with overdue items from borrowing new ones.

## Quick Start

### Prerequisites
- Java 14 or higher

### Running the Application

```bash
# Clone the repository
git clone https://github.com/VenomxSpiderman/java-library-management-system.git
cd java-library-management-system

# Compile and run
javac LibraryMain.java
java LibraryMain
```

### First-time Setup

When you first run the program, you'll configure:
- Daily fine rate (e.g., $0.50 per day)
- Book borrow period (default: 14 days)  
- Magazine borrow period (default: 7 days)

## How to Use

The system presents a menu with these options:

1. **Display All Items** - See all books and magazines with their status
2. **Add New Book** - Register a book with ID, title, author, and ISBN
3. **Add New Magazine** - Register a magazine with ID, title, issue date, and number
4. **Add New Member** - Add a library member with ID, name, and email
5. **Borrow Item** - Let a member borrow an available item
6. **Return Item** - Process returns and calculate any fines
7. **Search Item by ID** - Find specific items
8. **Display Member Info** - View member details and borrowed items
9. **Check Overdue Items** - See overdue reports and fines
10. **Exit** - Close the program

### Example Workflow

1. Add some books and magazines (options 2 and 3)
2. Register library members (option 4)
3. Let members borrow items (option 5)
4. Check what's been borrowed (option 1)
5. Process returns (option 6)

## Key Features

**Core Functionality**
- Add and manage books and magazines
- Register library members
- Borrow and return items with due dates
- Automatic fine calculation for overdue items
- Search items and members by ID
- Generate overdue reports

**Business Rules**
- Members with overdue items cannot borrow new items
- Books can be borrowed for 14 days (configurable)
- Magazines can be borrowed for 7 days (configurable)
- Fines are calculated per day overdue

**Technical Features**
- Fast lookups using HashMap collections
- Input validation and error handling
- Date arithmetic for due dates and fines
- Clean separation between UI and business logic

## Code Structure

The system is organized into these main classes:

- **LibraryMain** - Handles user interface and menu interactions
- **LibraryManagementSystem** - Contains core business logic
- **LibraryItem** - Abstract base class for books and magazines
- **Book** - Represents books with author and ISBN
- **Magazine** - Represents magazines with issue date and number
- **Member** - Represents library members
- **BorrowRecord** - Tracks borrowing transactions
- **LibraryConfig** - Stores system configuration

## Testing the System

Try these scenarios to test the functionality:

1. **Basic operations**: Add members, add items, borrow and return
2. **Error handling**: Try borrowing non-existent items or with invalid members
3. **Overdue scenarios**: Manually adjust dates to test fine calculations
4. **Business rules**: Try borrowing when a member has overdue items

## Technical Implementation

This project demonstrates object-oriented programming concepts:

- **Abstraction**: `LibraryItem` abstract class defines common behavior
- **Inheritance**: `Book` and `Magazine` extend `LibraryItem`
- **Encapsulation**: Private fields with controlled access through methods
- **Polymorphism**: Different item types handled uniformly

The system uses modern Java features like:
- LocalDate for date handling
- Optional for null safety
- HashMap for efficient lookups
- Stream API for data processing
- Switch expressions for cleaner code

## Author

**Tathagata**
- GitHub: [VenomxSpiderman](https://github.com/VenomxSpiderman)
- LinkedIn: [tathagata06](https://linkedin.com/in/tathagata06)

## License

This project is created for educational and portfolio purposes.