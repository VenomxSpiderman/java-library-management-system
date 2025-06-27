import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.io.*;

/**
 * Library Management System - A comprehensive Java application demonstrating OOP principles
 * 
 * This system showcases:
 * - Abstraction: Abstract LibraryItem class
 * - Inheritance: Book and Magazine extending LibraryItem
 * - Encapsulation: Private fields with controlled access
 * - Polymorphism: Unified handling of different item types
 * 
 * Features:
 * - Configurable settings (fine rates, borrow periods)
 * - BufferedReader for input handling
 * - Modern Java switch expressions
 * - Simple date arithmetic without complex temporal libraries
 * 
 * @author Tathagata
 * @version 2.0
 * @since 2025
 */

/**
 * Configuration class to hold system-wide settings
 * 
 * This class encapsulates all configurable parameters of the library system,
 * allowing for easy customization without modifying core business logic.
 */
class LibraryConfig {
    // Private fields ensuring encapsulation
    private double dailyFineRate; // Fine amount per day for overdue items
    private int bookBorrowDays; // Default borrow period for books
    private int magazineBorrowDays; // Default borrow period for magazines

    /**
     * Constructor to initialize library configuration
     * 
     * @param dailyFineRate      Fine amount charged per day for overdue items
     * @param bookBorrowDays     Number of days books can be borrowed
     * @param magazineBorrowDays Number of days magazines can be borrowed
     */
    public LibraryConfig(double dailyFineRate, int bookBorrowDays, int magazineBorrowDays) {
        this.dailyFineRate = dailyFineRate;
        this.bookBorrowDays = bookBorrowDays;
        this.magazineBorrowDays = magazineBorrowDays;
    }

    // Getter methods providing controlled read access to private fields
    public double getDailyFineRate() {
        return dailyFineRate;
    }

    public int getBookBorrowDays() {
        return bookBorrowDays;
    }

    public int getMagazineBorrowDays() {
        return magazineBorrowDays;
    }
}

/**
 * BorrowRecord class to track borrowing transaction details
 * 
 * This immutable class represents a single borrowing transaction,
 * maintaining all necessary information for tracking and fine calculation.
 * Uses final fields to ensure data integrity after creation.
 */
class BorrowRecord {
    // Final fields ensure immutability - values cannot be changed after
    // construction
    private final String memberId; // ID of the member who borrowed the item
    private final String itemId; // ID of the borrowed item
    private final LocalDate borrowDate; // Date when item was borrowed
    private final LocalDate dueDate; // Date when item should be returned

    /**
     * Constructor to create a new borrow record
     * 
     * @param memberId   ID of the borrowing member
     * @param itemId     ID of the borrowed item
     * @param borrowDate Date of borrowing
     * @param borrowDays Number of days the item can be borrowed
     */
    public BorrowRecord(String memberId, String itemId, LocalDate borrowDate, int borrowDays) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.borrowDate = borrowDate;
        // Calculate due date by adding borrow days to borrow date
        this.dueDate = borrowDate.plusDays(borrowDays);
    }

    // Getter methods providing read-only access to record data
    public String getMemberId() {
        return memberId;
    }

    public String getItemId() {
        return itemId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Checks if the borrowed item is overdue
     * 
     * @return true if current date is after due date, false otherwise
     */
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }

    /**
     * Calculates number of days the item is overdue
     * Uses simple epoch day arithmetic instead of ChronoUnit for simplicity
     * 
     * @return number of days overdue, 0 if not overdue
     */
    public long getDaysOverdue() {
        if (!isOverdue())
            return 0;

        // Simple date arithmetic - calculate days between due date and today
        // Using epoch days: number of days since 1970-01-01
        LocalDate today = LocalDate.now();
        long daysDiff = today.toEpochDay() - dueDate.toEpochDay();
        return daysDiff;
    }

    /**
     * Calculates fine amount based on days overdue and daily rate
     * 
     * @param dailyFineRate Amount charged per day for overdue items
     * @return Total fine amount owed
     */
    public double calculateFine(double dailyFineRate) {
        return getDaysOverdue() * dailyFineRate;
    }
}

/**
 * Abstract base class demonstrating Abstraction principle
 * 
 * This class defines the common structure and behavior for all library items
 * while leaving specific implementation details to concrete subclasses.
 * 
 * Key OOP Concepts:
 * - Abstract class cannot be instantiated directly
 * - Provides common fields and methods for all library items
 * - Defines abstract methods that must be implemented by subclasses
 */
abstract class LibraryItem {
    // Private fields demonstrating Encapsulation
    private String id; // Unique identifier for the item
    private String title; // Title of the library item
    private boolean isAvailable; // Availability status

    /**
     * Constructor for LibraryItem
     * Protected access ensures only subclasses can call this constructor
     * 
     * @param id    Unique identifier for the item
     * @param title Title of the library item
     */
    public LibraryItem(String id, String title) {
        this.id = id;
        this.title = title;
        this.isAvailable = true; // Items are available by default
    }

    // Abstract methods - must be implemented by concrete subclasses
    // This demonstrates the Template Method pattern

    /**
     * Abstract method to display item-specific details
     * Each subclass implements this differently based on their data
     */
    public abstract void displayDetails();

    /**
     * Abstract method to get borrow period for the item type
     * Different item types may have different borrow periods
     * 
     * @return Number of days the item can be borrowed
     */
    public abstract int getBorrowDays();

    // Concrete methods providing common functionality
    // These demonstrate Encapsulation with controlled access to private fields

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Controls availability status with validation
     * This method encapsulates the business rule for item availability
     * 
     * @param available New availability status
     */
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}

/**
 * Book class demonstrating Inheritance principle
 * 
 * This concrete class extends LibraryItem and adds book-specific functionality.
 * It demonstrates:
 * - Inheritance: Extends LibraryItem to reuse common functionality
 * - Method Override: Provides specific implementations for abstract methods
 * - Static Configuration: Shares configuration across all book instances
 */
class Book extends LibraryItem {
    // Private fields specific to books
    private String author; // Author of the book
    private String isbn; // International Standard Book Number

    // Static configuration shared across all Book instances
    // This demonstrates the Singleton-like pattern for configuration
    private static LibraryConfig config;

    /**
     * Constructor for Book
     * Calls parent constructor and initializes book-specific fields
     * 
     * @param id     Unique identifier for the book
     * @param title  Title of the book
     * @param author Author of the book
     * @param isbn   ISBN number of the book
     */
    public Book(String id, String title, String author, String isbn) {
        super(id, title); // Call parent constructor
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * Static method to set configuration for all Book instances
     * This ensures consistent behavior across all books
     * 
     * @param libraryConfig Configuration object containing system settings
     */
    public static void setConfig(LibraryConfig libraryConfig) {
        config = libraryConfig;
    }

    /**
     * Override method demonstrating Polymorphism
     * Provides book-specific display format
     */
    @Override
    public void displayDetails() {
        System.out.println("Book: " + getTitle() + " by " + author + " (ISBN: " + isbn + ")");
    }

    /**
     * Override method to get book-specific borrow period
     * Uses configuration if available, otherwise defaults to 14 days
     * 
     * @return Number of days books can be borrowed
     */
    @Override
    public int getBorrowDays() {
        return config != null ? config.getBookBorrowDays() : 14; // Default 14 days
    }

    // Getter methods for book-specific fields
    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }
}

/**
 * Magazine class demonstrating Inheritance principle
 * 
 * Another concrete implementation of LibraryItem with magazine-specific
 * features.
 * Uses LocalDate for issue dates to maintain consistency with borrow records.
 */
class Magazine extends LibraryItem {
    // Private fields specific to magazines
    private LocalDate issueDate; // Publication date of the magazine issue
    private int issueNumber; // Issue number of the magazine

    // Static configuration shared across all Magazine instances
    private static LibraryConfig config;

    /**
     * Constructor for Magazine
     * 
     * @param id          Unique identifier for the magazine
     * @param title       Title of the magazine
     * @param issueDate   Publication date of this issue
     * @param issueNumber Issue number
     */
    public Magazine(String id, String title, LocalDate issueDate, int issueNumber) {
        super(id, title); // Call parent constructor
        this.issueDate = issueDate;
        this.issueNumber = issueNumber;
    }

    /**
     * Static method to set configuration for all Magazine instances
     * 
     * @param libraryConfig Configuration object containing system settings
     */
    public static void setConfig(LibraryConfig libraryConfig) {
        config = libraryConfig;
    }

    /**
     * Override method providing magazine-specific display format
     * Demonstrates Polymorphism - same method name, different behavior
     */
    @Override
    public void displayDetails() {
        System.out.println("Magazine: " + getTitle() + " Issue #" + issueNumber + " (" + issueDate + ")");
    }

    /**
     * Override method to get magazine-specific borrow period
     * Magazines typically have shorter borrow periods than books
     * 
     * @return Number of days magazines can be borrowed
     */
    @Override
    public int getBorrowDays() {
        return config != null ? config.getMagazineBorrowDays() : 7; // Default 7 days
    }

    // Getter methods for magazine-specific fields
    public LocalDate getIssueDate() {
        return issueDate;
    }

    public int getIssueNumber() {
        return issueNumber;
    }
}

/**
 * Member class demonstrating Encapsulation principle
 * 
 * This class represents a library member with complete encapsulation of member
 * data.
 * It maintains a list of borrowed items and provides controlled access to
 * member information.
 */
class Member {
    // Private fields ensuring data encapsulation
    private String memberId; // Unique member identifier
    private String name; // Member's full name
    private String email; // Member's email address
    private List<LibraryItem> borrowedItems; // Items currently borrowed by member

    /**
     * Constructor to create a new library member
     * Initializes borrowed items list as empty ArrayList
     * 
     * @param memberId Unique identifier for the member
     * @param name     Full name of the member
     * @param email    Email address of the member
     */
    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        // Initialize with empty list - member starts with no borrowed items
        this.borrowedItems = new ArrayList<>();
    }

    // Getter methods providing controlled read access to private fields
    // These methods demonstrate Encapsulation by controlling how data is accessed

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Returns a reference to borrowed items list
     * Note: In production code, this might return a defensive copy
     * to prevent external modification of the internal list
     * 
     * @return List of currently borrowed items
     */
    public List<LibraryItem> getBorrowedItems() {
        return borrowedItems;
    }

    /**
     * Adds an item to member's borrowed items list
     * This method encapsulates the business logic of borrowing
     * 
     * @param item LibraryItem being borrowed
     */
    public void borrowItem(LibraryItem item) {
        borrowedItems.add(item);
    }

    /**
     * Removes an item from member's borrowed items list
     * This method encapsulates the business logic of returning
     * 
     * @param item LibraryItem being returned
     */
    public void returnItem(LibraryItem item) {
        borrowedItems.remove(item);
    }
}

/**
 * Main Library Management System class - The Core Business Logic Layer
 * 
 * This class demonstrates the Facade pattern by providing a simplified
 * interface
 * to the complex subsystem of library operations. It manages all business logic
 * while keeping the UI layer clean and focused on presentation.
 * 
 * Key Design Patterns:
 * - Facade Pattern: Simplified interface to complex operations
 * - Repository Pattern: Centralized data access through maps
 * - Dependency Injection: Configuration injected through constructor
 * 
 * Performance Considerations:
 * - HashMap for O(1) lookup performance
 * - Stream API for functional programming style operations
 * - ArrayList for ordered collections
 */
class LibraryManagementSystem {
    // Core system configuration
    private LibraryConfig config;

    // Data storage collections - demonstrating different collection types
    private List<LibraryItem> items; // Ordered list of all items
    private List<Member> members; // Ordered list of all members
    private Map<String, LibraryItem> itemMap; // Fast lookup by item ID
    private Map<String, Member> memberMap; // Fast lookup by member ID
    private Map<String, BorrowRecord> borrowRecords; // Track active borrowings

    /**
     * Constructor demonstrating Dependency Injection
     * Configuration is injected rather than created internally
     * 
     * @param config Library configuration containing system settings
     */
    public LibraryManagementSystem(LibraryConfig config) {
        this.config = config;

        // Initialize all collections as empty
        this.items = new ArrayList<>();
        this.members = new ArrayList<>();
        this.itemMap = new HashMap<>();
        this.memberMap = new HashMap<>();
        this.borrowRecords = new HashMap<>();

        // Configure static settings for item classes
        // This ensures all items use the same configuration
        Book.setConfig(config);
        Magazine.setConfig(config);
    }

    /**
     * Adds a new item to the library system
     * Maintains both list (for ordering) and map (for fast lookup)
     * 
     * @param item LibraryItem to be added to the system
     */
    public void addItem(LibraryItem item) {
        items.add(item); // Add to ordered list
        itemMap.put(item.getId(), item); // Add to lookup map
    }

    /**
     * Registers a new member in the library system
     * Maintains both list (for ordering) and map (for fast lookup)
     * 
     * @param member Member to be registered in the system
     */
    public void addMember(Member member) {
        members.add(member); // Add to ordered list
        memberMap.put(member.getMemberId(), member); // Add to lookup map
    }

    /**
     * Processes a borrowing request with comprehensive validation
     * 
     * Business Rules Enforced:
     * - Member must exist in the system
     * - Item must exist and be available
     * - Member cannot have overdue items
     * 
     * @param memberId ID of the member requesting to borrow
     * @param itemId   ID of the item to be borrowed
     * @return true if borrowing successful, false otherwise
     */
    public boolean borrowItem(String memberId, String itemId) {
        // Validate member exists
        Member member = memberMap.get(memberId);
        if (member == null)
            return false;

        // Validate item exists
        LibraryItem item = itemMap.get(itemId);
        if (item == null)
            return false;

        // Validate item is available
        if (!item.isAvailable())
            return false;

        // Business rule: Members with overdue items cannot borrow
        if (hasOverdueItems(memberId))
            return false;

        // Process the borrowing transaction
        item.setAvailable(false); // Mark item as borrowed
        member.borrowItem(item); // Add to member's list
        borrowRecords.put(itemId, new BorrowRecord(memberId, itemId,
                LocalDate.now(), item.getBorrowDays())); // Create borrow record

        return true; // Successful borrowing
    }

    /**
     * Processes a return request with validation
     * 
     * Business Rules Enforced:
     * - Member must exist in the system
     * - Item must exist in the system
     * - Valid borrow record must exist for this member-item combination
     * 
     * @param memberId ID of the member returning the item
     * @param itemId   ID of the item being returned
     * @return true if return successful, false otherwise
     */
    public boolean returnItem(String memberId, String itemId) {
        // Validate member exists
        Member member = memberMap.get(memberId);
        if (member == null)
            return false;

        // Validate item exists
        LibraryItem item = itemMap.get(itemId);
        if (item == null)
            return false;

        // Validate borrow record exists and belongs to this member
        BorrowRecord record = borrowRecords.get(itemId);
        if (record == null || !record.getMemberId().equals(memberId)) {
            return false;
        }

        // Process the return transaction
        item.setAvailable(true); // Mark item as available
        member.returnItem(item); // Remove from member's list
        borrowRecords.remove(itemId); // Remove borrow record

        return true; // Successful return
    }

    // Data Access Methods - demonstrating Repository pattern

    /**
     * Returns defensive copy of all items to prevent external modification
     * 
     * @return List containing all library items
     */
    public List<LibraryItem> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * Returns defensive copy of all members to prevent external modification
     * 
     * @return List containing all library members
     */
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    /**
     * Finds an item by ID using Optional to handle null cases elegantly
     * 
     * @param itemId ID of the item to find
     * @return Optional containing the item if found, empty Optional otherwise
     */
    public Optional<LibraryItem> findItem(String itemId) {
        return Optional.ofNullable(itemMap.get(itemId));
    }

    /**
     * Finds a member by ID using Optional to handle null cases elegantly
     * 
     * @param memberId ID of the member to find
     * @return Optional containing the member if found, empty Optional otherwise
     */
    public Optional<Member> findMember(String memberId) {
        return Optional.ofNullable(memberMap.get(memberId));
    }

    /**
     * Checks if a member has any overdue items using Stream API
     * Demonstrates functional programming approach
     * 
     * @param memberId ID of the member to check
     * @return true if member has overdue items, false otherwise
     */
    public boolean hasOverdueItems(String memberId) {
        return borrowRecords.values().stream()
                .anyMatch(record -> record.getMemberId().equals(memberId) && record.isOverdue());
    }

    /**
     * Gets all overdue items for a specific member
     * Uses Stream API for filtering and collecting
     * 
     * @param memberId ID of the member
     * @return List of overdue borrow records for the member
     */
    public List<BorrowRecord> getOverdueItems(String memberId) {
        return borrowRecords.values().stream()
                .filter(record -> record.getMemberId().equals(memberId) && record.isOverdue())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Calculates total outstanding fines for a member
     * Demonstrates Stream API with mapping and reduction operations
     * 
     * @param memberId ID of the member
     * @return Total fine amount owed by the member
     */
    public double calculateTotalFines(String memberId) {
        return getOverdueItems(memberId).stream()
                .mapToDouble(record -> record.calculateFine(config.getDailyFineRate()))
                .sum();
    }

    /**
     * Retrieves borrow record for a specific item
     * 
     * @param itemId ID of the borrowed item
     * @return Optional containing the borrow record if found
     */
    public Optional<BorrowRecord> getBorrowRecord(String itemId) {
        return Optional.ofNullable(borrowRecords.get(itemId));
    }

    /**
     * Provides access to system configuration
     * 
     * @return Current library configuration
     */
    public LibraryConfig getConfig() {
        return config;
    }
}

/**
 * Main application class - User Interface Layer
 * 
 * This class handles all user interactions and serves as the presentation
 * layer.
 * It demonstrates clean separation of concerns by keeping UI logic separate
 * from business logic.
 * 
 * Modern Java Features Used:
 * - BufferedReader for efficient input handling
 * - Modern switch expressions with arrow syntax (Java 14+)
 * - IOException handling with try-with-resources pattern
 * - Stream API and Optional for null safety
 * 
 * Design Principles:
 * - Single Responsibility: Each method handles one specific UI operation
 * - Clean Architecture: UI layer only handles presentation, delegates business
 * logic
 * - Error Handling: Comprehensive validation with user-friendly messages
 */
public class LibraryMain {
    // Static fields for application-wide access
    private static LibraryManagementSystem library; // Core business logic system
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // Input handler

    /**
     * Main entry point of the application
     * Demonstrates exception handling and application initialization flow
     * 
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        try {
            // Step 1: Initialize system configuration with user input
            LibraryConfig config = setupConfiguration();

            // Step 2: Create main library system with configuration
            library = new LibraryManagementSystem(config);

            // Step 3: Start interactive user interface
            displayWelcome();
            showMenu();

        } catch (IOException e) {
            // Handle any IO errors gracefully
            System.out.println("Error reading input: " + e.getMessage());
        }
    }

    /**
     * Initial configuration setup allowing customization of system parameters
     * This method demonstrates input validation and default value handling
     * 
     * @return LibraryConfig object with user-specified or default settings
     * @throws IOException if input reading fails
     */
    private static LibraryConfig setupConfiguration() throws IOException {
        System.out.println("========================================");
        System.out.println("   LIBRARY SYSTEM CONFIGURATION");
        System.out.println("========================================");

        // Get daily fine rate with input validation
        System.out.print("Enter daily fine rate (e.g., 0.50 for $0.50 per day): $");
        double dailyFineRate = getDoubleInput();

        // Get book borrow period with default fallback
        System.out.print("Enter book borrow period (days, default 14): ");
        int bookBorrowDays = getIntInput();
        if (bookBorrowDays <= 0)
            bookBorrowDays = 14; // Use default if invalid

        // Get magazine borrow period with default fallback
        System.out.print("Enter magazine borrow period (days, default 7): ");
        int magazineBorrowDays = getIntInput();
        if (magazineBorrowDays <= 0)
            magazineBorrowDays = 7; // Use default if invalid

        // Display configuration summary for user confirmation
        System.out.println("\nConfiguration saved:");
        System.out.printf("- Daily fine rate: $%.2f%n", dailyFineRate);
        System.out.println("- Book borrow period: " + bookBorrowDays + " days");
        System.out.println("- Magazine borrow period: " + magazineBorrowDays + " days");

        return new LibraryConfig(dailyFineRate, bookBorrowDays, magazineBorrowDays);
    }

    /**
     * Displays welcome message with ASCII art formatting
     * Creates a professional first impression for users
     */
    private static void displayWelcome() {
        System.out.println("=========================================");
        System.out.println("   WELCOME TO LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=========================================");
    }

    /**
     * Main menu loop demonstrating modern switch expressions
     * Uses Java 14+ arrow syntax for cleaner, more readable code
     * 
     * @throws IOException if input reading fails
     */
    private static void showMenu() throws IOException {
        // Infinite loop for continuous menu interaction
        while (true) {
            // Display menu options
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Display All Items");
            System.out.println("2. Add New Book");
            System.out.println("3. Add New Magazine");
            System.out.println("4. Add New Member");
            System.out.println("5. Borrow Item");
            System.out.println("6. Return Item");
            System.out.println("7. Search Item by ID");
            System.out.println("8. Display Member Info");
            System.out.println("9. Check Overdue Items");
            System.out.println("10. Exit");
            System.out.print("Enter your choice (1-10): ");

            int choice = getIntInput();

            // Modern switch expression with arrow syntax - no breaks needed
            switch (choice) {
                case 1 -> displayAllItems(); // Single statement
                case 2 -> addNewBook(); // Single statement
                case 3 -> addNewMagazine(); // Single statement
                case 4 -> addNewMember(); // Single statement
                case 5 -> borrowItem(); // Single statement
                case 6 -> returnItem(); // Single statement
                case 7 -> searchItem(); // Single statement
                case 8 -> displayMemberInfo(); // Single statement
                case 9 -> checkOverdueItems(); // Single statement
                case 10 -> { // Multiple statements need block
                    System.out.println("Thank you for using Tathagata's Library Management System!");
                    reader.close(); // Clean up resources
                    System.exit(0); // Graceful exit
                }
                default -> System.out.println("Invalid choice! Please enter a number between 1-10.");
            }
        }
    }

    private static void addNewBook() throws IOException {
        System.out.println("\n=== Add New Book ===");
        System.out.print("Enter Book ID: ");
        String id = reader.readLine();
        System.out.print("Enter Book Title: ");
        String title = reader.readLine();
        System.out.print("Enter Author Name: ");
        String author = reader.readLine();
        System.out.print("Enter ISBN: ");
        String isbn = reader.readLine();

        LibraryItem book = new Book(id, title, author, isbn);
        library.addItem(book);
        System.out.println("Added: " + book.getTitle());
    }

    private static void addNewMagazine() throws IOException {
        System.out.println("\n=== Add New Magazine ===");
        System.out.print("Enter Magazine ID: ");
        String id = reader.readLine();
        System.out.print("Enter Magazine Title: ");
        String title = reader.readLine();

        LocalDate issueDate = null;
        while (issueDate == null) {
            System.out.print("Enter Issue Date (YYYY-MM-DD): ");
            String dateInput = reader.readLine();
            try {
                issueDate = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }

        System.out.print("Enter Issue Number: ");
        int issueNumber = getIntInput();

        LibraryItem magazine = new Magazine(id, title, issueDate, issueNumber);
        library.addItem(magazine);
        System.out.println("Added: " + magazine.getTitle());
    }

    private static void addNewMember() throws IOException {
        System.out.println("\n=== Add New Member ===");
        System.out.print("Enter Member ID: ");
        String memberId = reader.readLine();
        System.out.print("Enter Member Name: ");
        String name = reader.readLine();
        System.out.print("Enter Email: ");
        String email = reader.readLine();

        Member member = new Member(memberId, name, email);
        library.addMember(member);
        System.out.println("Member added: " + member.getName());
    }

    // UI method with proper error handling
    private static void displayAllItems() {
        System.out.println("\n=== Library Items ===");
        List<LibraryItem> items = library.getAllItems();

        if (items.isEmpty()) {
            System.out.println("No items in the library.");
            return;
        }

        for (LibraryItem item : items) {
            item.displayDetails(); // Polymorphic call
            System.out.println("Status: " + (item.isAvailable() ? "Available" : "Borrowed"));
            System.out.println("Borrow Period: " + item.getBorrowDays() + " days");

            // Show borrower info if borrowed
            if (!item.isAvailable()) {
                library.getBorrowRecord(item.getId()).ifPresent(record -> {
                    System.out.println("Borrowed by: " + record.getMemberId());
                    System.out.println("Due Date: " + record.getDueDate());
                    if (record.isOverdue()) {
                        System.out.println("⚠️  OVERDUE by " + record.getDaysOverdue() + " days");
                    }
                });
            }
            System.out.println("---");
        }
    }

    private static void borrowItem() throws IOException {
        System.out.println("\n=== Borrow Item ===");
        System.out.print("Enter Member ID: ");
        String memberId = reader.readLine();
        System.out.print("Enter Item ID: ");
        String itemId = reader.readLine();

        // Check if member exists
        Optional<Member> memberOpt = library.findMember(memberId);
        if (!memberOpt.isPresent()) {
            System.out.println("❌ Member with ID '" + memberId + "' not found.");
            return;
        }

        // Check if item exists
        Optional<LibraryItem> itemOpt = library.findItem(itemId);
        if (!itemOpt.isPresent()) {
            System.out.println("❌ Item with ID '" + itemId + "' not found.");
            return;
        }

        // Check if item is available
        if (!itemOpt.get().isAvailable()) {
            System.out.println("❌ Item is already borrowed by someone else.");
            return;
        }

        // Check if member has overdue items
        if (library.hasOverdueItems(memberId)) {
            System.out.println("❌ Member has overdue items. Please return them first.");
            double totalFines = library.calculateTotalFines(memberId);
            System.out.printf("Total outstanding fines: $%.2f%n", totalFines);
            return;
        }

        // Attempt to borrow
        boolean success = library.borrowItem(memberId, itemId);
        if (success) {
            Member member = memberOpt.get();
            LibraryItem item = itemOpt.get();
            System.out.println("✅ " + member.getName() + " successfully borrowed: " + item.getTitle());
            library.getBorrowRecord(itemId)
                    .ifPresent(record -> System.out.println("Due Date: " + record.getDueDate()));
        } else {
            System.out.println("❌ Unable to borrow item. Please try again.");
        }
    }

    private static void returnItem() throws IOException {
        System.out.println("\n=== Return Item ===");
        System.out.print("Enter Member ID: ");
        String memberId = reader.readLine();
        System.out.print("Enter Item ID: ");
        String itemId = reader.readLine();

        // Check if member exists
        Optional<Member> memberOpt = library.findMember(memberId);
        if (!memberOpt.isPresent()) {
            System.out.println("❌ Member with ID '" + memberId + "' not found.");
            return;
        }

        // Check if item exists
        Optional<LibraryItem> itemOpt = library.findItem(itemId);
        if (!itemOpt.isPresent()) {
            System.out.println("❌ Item with ID '" + itemId + "' not found.");
            return;
        }

        // Get record before returning to calculate fine
        Optional<BorrowRecord> recordOpt = library.getBorrowRecord(itemId);
        if (!recordOpt.isPresent() || !recordOpt.get().getMemberId().equals(memberId)) {
            System.out.println("❌ This item was not borrowed by this member.");
            return;
        }

        BorrowRecord record = recordOpt.get();
        boolean wasOverdue = record.isOverdue();
        double fine = wasOverdue ? record.calculateFine(library.getConfig().getDailyFineRate()) : 0;

        // Attempt to return
        boolean success = library.returnItem(memberId, itemId);
        if (success) {
            Member member = memberOpt.get();
            LibraryItem item = itemOpt.get();
            System.out.println("✅ " + member.getName() + " successfully returned: " + item.getTitle());

            if (wasOverdue) {
                System.out.println("⚠️  Item returned late! Fine applied: $" + String.format("%.2f", fine));
                System.out.println("Days overdue: " + record.getDaysOverdue());
            }
        } else {
            System.out.println("❌ Unable to return item. Please try again.");
        }
    }

    private static void searchItem() throws IOException {
        System.out.println("\n=== Search Item ===");
        System.out.print("Enter Item ID: ");
        String itemId = reader.readLine();

        Optional<LibraryItem> itemOpt = library.findItem(itemId);
        if (itemOpt.isPresent()) {
            LibraryItem item = itemOpt.get();
            System.out.println("\n=== Item Found ===");
            item.displayDetails();
            System.out.println("Status: " + (item.isAvailable() ? "Available" : "Borrowed"));
            System.out.println("Borrow Period: " + item.getBorrowDays() + " days");

            if (!item.isAvailable()) {
                library.getBorrowRecord(itemId).ifPresent(record -> {
                    System.out.println("Borrowed by: " + record.getMemberId());
                    System.out.println("Borrow Date: " + record.getBorrowDate());
                    System.out.println("Due Date: " + record.getDueDate());
                    if (record.isOverdue()) {
                        System.out.println("⚠️  OVERDUE by " + record.getDaysOverdue() + " days");
                    }
                });
            }
        } else {
            System.out.println("❌ Item with ID '" + itemId + "' not found.");
        }
    }

    private static void displayMemberInfo() throws IOException {
        System.out.println("\n=== Member Information ===");
        System.out.print("Enter Member ID: ");
        String memberId = reader.readLine();

        Optional<Member> memberOpt = library.findMember(memberId);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            System.out.println("\n=== Member Information ===");
            System.out.println("Member ID: " + member.getMemberId());
            System.out.println("Name: " + member.getName());
            System.out.println("Email: " + member.getEmail());
            System.out.println("Borrowed Items: " + member.getBorrowedItems().size());

            if (!member.getBorrowedItems().isEmpty()) {
                System.out.println("\nCurrently Borrowed Items:");
                for (LibraryItem item : member.getBorrowedItems()) {
                    System.out.println("- " + item.getTitle() + " (ID: " + item.getId() + ")");
                    library.getBorrowRecord(item.getId()).ifPresent(record -> {
                        System.out.println("  Due: " + record.getDueDate());
                        if (record.isOverdue()) {
                            System.out.println("  ⚠️  OVERDUE by " + record.getDaysOverdue() + " days");
                        }
                    });
                }
            }

            // Show total fines
            double totalFines = library.calculateTotalFines(memberId);
            if (totalFines > 0) {
                System.out.printf("\n💰 Total Outstanding Fines: $%.2f%n", totalFines);
            }
        } else {
            System.out.println("❌ Member with ID '" + memberId + "' not found.");
        }
    }

    private static void checkOverdueItems() throws IOException {
        System.out.println("\n=== Overdue Items Report ===");
        System.out.print("Enter Member ID (or press Enter for all members): ");
        String memberId = reader.readLine().trim();

        if (memberId.isEmpty()) {
            // Show all overdue items
            List<Member> members = library.getAllMembers();
            boolean hasOverdue = false;

            for (Member member : members) {
                List<BorrowRecord> overdueItems = library.getOverdueItems(member.getMemberId());
                if (!overdueItems.isEmpty()) {
                    hasOverdue = true;
                    System.out.println("\n👤 Member: " + member.getName() + " (" + member.getMemberId() + ")");
                    double totalFines = 0;

                    for (BorrowRecord record : overdueItems) {
                        library.findItem(record.getItemId()).ifPresent(item -> {
                            System.out.println("  📚 " + item.getTitle() + " (ID: " + item.getId() + ")");
                            System.out.println("     Due: " + record.getDueDate());
                            System.out.println("     Overdue by: " + record.getDaysOverdue() + " days");
                        });
                        totalFines += record.calculateFine(library.getConfig().getDailyFineRate());
                    }
                    System.out.printf("  💰 Total Fines: $%.2f%n", totalFines);
                }
            }

            if (!hasOverdue) {
                System.out.println("✅ No overdue items found!");
            }
        } else {
            // Show overdue items for specific member
            Optional<Member> memberOpt = library.findMember(memberId);
            if (memberOpt.isPresent()) {
                List<BorrowRecord> overdueItems = library.getOverdueItems(memberId);
                if (overdueItems.isEmpty()) {
                    System.out.println("✅ No overdue items for this member.");
                } else {
                    System.out.println("\n👤 Member: " + memberOpt.get().getName());
                    double totalFines = 0;

                    for (BorrowRecord record : overdueItems) {
                        library.findItem(record.getItemId()).ifPresent(item -> {
                            System.out.println("📚 " + item.getTitle() + " (ID: " + item.getId() + ")");
                            System.out.println("   Due: " + record.getDueDate());
                            System.out.println("   Overdue by: " + record.getDaysOverdue() + " days");
                        });
                        totalFines += record.calculateFine(library.getConfig().getDailyFineRate());
                    }
                    System.out.printf("💰 Total Fines: $%.2f%n", totalFines);
                }
            } else {
                System.out.println("❌ Member with ID '" + memberId + "' not found.");
            }
        }
    }

    /**
     * Utility method for safe integer input with error handling
     * Demonstrates robust input validation and error recovery
     * 
     * @return Valid integer input from user
     * @throws IOException if input reading fails
     */
    private static int getIntInput() throws IOException {
        while (true) {
            try {
                String input = reader.readLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // Handle invalid number format gracefully
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    /**
     * Utility method for safe double input with error handling
     * Ensures valid decimal numbers for monetary values
     * 
     * @return Valid double input from user
     * @throws IOException if input reading fails
     */
    private static double getDoubleInput() throws IOException {
        while (true) {
            try {
                String input = reader.readLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                // Handle invalid number format gracefully
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}