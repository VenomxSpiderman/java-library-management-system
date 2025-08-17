import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.io.*;

class LibraryConfig {
    private double dailyFineRate;
    private int bookBorrowDays;
    private int magazineBorrowDays;

    public LibraryConfig(double dailyFineRate, int bookBorrowDays, int magazineBorrowDays) {
        this.dailyFineRate = dailyFineRate;
        this.bookBorrowDays = bookBorrowDays;
        this.magazineBorrowDays = magazineBorrowDays;
    }

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

class BorrowRecord {
    private final String memberId;
    private final String itemId;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;

    public BorrowRecord(String memberId, String itemId, LocalDate borrowDate, int borrowDays) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(borrowDays);
    }

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

    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }

    public long getDaysOverdue() {
        if (!isOverdue())
            return 0;

        LocalDate today = LocalDate.now();
        long daysDiff = today.toEpochDay() - dueDate.toEpochDay();
        return daysDiff;
    }

    public double calculateFine(double dailyFineRate) {
        return getDaysOverdue() * dailyFineRate;
    }
}

abstract class LibraryItem {
    private String id;
    private String title;
    private boolean isAvailable;

    public LibraryItem(String id, String title) {
        this.id = id;
        this.title = title;
        this.isAvailable = true; // Items are available by default
    }

    public abstract void displayDetails();

    public abstract int getBorrowDays();


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}

class Book extends LibraryItem {
    private String author;
    private String isbn;
    private static LibraryConfig config;

    public Book(String id, String title, String author, String isbn) {
        super(id, title); // Call parent constructor
        this.author = author;
        this.isbn = isbn;
    }

    public static void setConfig(LibraryConfig libraryConfig) {
        config = libraryConfig;
    }

    @Override
    public void displayDetails() {
        System.out.println("Book: " + getTitle() + " by " + author + " (ISBN: " + isbn + ")");
    }

    @Override
    public int getBorrowDays() {
        return config != null ? config.getBookBorrowDays() : 14; // Default 14 days
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }
}

class Magazine extends LibraryItem {
    private LocalDate issueDate;
    private int issueNumber;
    private static LibraryConfig config;

    public Magazine(String id, String title, LocalDate issueDate, int issueNumber) {
        super(id, title); // Call parent constructor
        this.issueDate = issueDate;
        this.issueNumber = issueNumber;
    }

    public static void setConfig(LibraryConfig libraryConfig) {
        config = libraryConfig;
    }

    @Override
    public void displayDetails() {
        System.out.println("Magazine: " + getTitle() + " Issue #" + issueNumber + " (" + issueDate + ")");
    }

    @Override
    public int getBorrowDays() {
        return config != null ? config.getMagazineBorrowDays() : 7; // Default 7 days
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public int getIssueNumber() {
        return issueNumber;
    }
}

class Member {
    private String memberId;
    private String name;
    private String email;
    private List<LibraryItem> borrowedItems;

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.borrowedItems = new ArrayList<>();
    }


    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<LibraryItem> getBorrowedItems() {
        return borrowedItems;
    }

    public void borrowItem(LibraryItem item) {
        borrowedItems.add(item);
    }

    public void returnItem(LibraryItem item) {
        borrowedItems.remove(item);
    }
}

class LibraryManagementSystem {
    private LibraryConfig config;
    private List<LibraryItem> items;
    private List<Member> members;
    private Map<String, LibraryItem> itemMap;
    private Map<String, Member> memberMap;
    private Map<String, BorrowRecord> borrowRecords;
    public LibraryManagementSystem(LibraryConfig config) {
        this.config = config;
        this.items = new ArrayList<>();
        this.members = new ArrayList<>();
        this.itemMap = new HashMap<>();
        this.memberMap = new HashMap<>();
        this.borrowRecords = new HashMap<>();
        Book.setConfig(config);
        Magazine.setConfig(config);
    }

    public void addItem(LibraryItem item) {
        items.add(item);
        itemMap.put(item.getId(), item);
    }

    public void addMember(Member member) {
        members.add(member);
        memberMap.put(member.getMemberId(), member);
    }

    public boolean borrowItem(String memberId, String itemId) {
        Member member = memberMap.get(memberId);
        if (member == null)
            return false;

        LibraryItem item = itemMap.get(itemId);
        if (item == null)
            return false;

        if (!item.isAvailable())
            return false;

        if (hasOverdueItems(memberId))
            return false;

        item.setAvailable(false);
        member.borrowItem(item);
        borrowRecords.put(itemId, new BorrowRecord(memberId, itemId,
                LocalDate.now(), item.getBorrowDays()));

        return true;
    }

    public boolean returnItem(String memberId, String itemId) {
        Member member = memberMap.get(memberId);
        if (member == null)
            return false;

        LibraryItem item = itemMap.get(itemId);
        if (item == null)
            return false;

        BorrowRecord record = borrowRecords.get(itemId);
        if (record == null || !record.getMemberId().equals(memberId)) {
            return false;
        }

        item.setAvailable(true);
        member.returnItem(item);
        borrowRecords.remove(itemId);

        return true;
    }

    public List<LibraryItem> getAllItems() {
        return new ArrayList<>(items);
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    public Optional<LibraryItem> findItem(String itemId) {
        return Optional.ofNullable(itemMap.get(itemId));
    }

    public Optional<Member> findMember(String memberId) {
        return Optional.ofNullable(memberMap.get(memberId));
    }

    public boolean hasOverdueItems(String memberId) {
        return borrowRecords.values().stream()
                .anyMatch(record -> record.getMemberId().equals(memberId) && record.isOverdue());
    }

    public List<BorrowRecord> getOverdueItems(String memberId) {
        return borrowRecords.values().stream()
                .filter(record -> record.getMemberId().equals(memberId) && record.isOverdue())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public double calculateTotalFines(String memberId) {
        return getOverdueItems(memberId).stream()
                .mapToDouble(record -> record.calculateFine(config.getDailyFineRate()))
                .sum();
    }

    public Optional<BorrowRecord> getBorrowRecord(String itemId) {
        return Optional.ofNullable(borrowRecords.get(itemId));
    }

    public LibraryConfig getConfig() {
        return config;
    }
}

public class LibraryMain {
    private static LibraryManagementSystem library;
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) {
        try {
            LibraryConfig config = setupConfiguration();
            library = new LibraryManagementSystem(config);
            displayWelcome();
            showMenu();
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
    }

    private static LibraryConfig setupConfiguration() throws IOException {
        System.out.println("========================================");
        System.out.println("   LIBRARY SYSTEM CONFIGURATION");
        System.out.println("========================================");

        System.out.print("Enter daily fine rate (e.g., 0.50 for $0.50 per day): $");
        double dailyFineRate = getDoubleInput();

        System.out.print("Enter book borrow period (days, default 14): ");
        int bookBorrowDays = getIntInput();
        if (bookBorrowDays <= 0)
            bookBorrowDays = 14;

        System.out.print("Enter magazine borrow period (days, default 7): ");
        int magazineBorrowDays = getIntInput();
        if (magazineBorrowDays <= 0)
            magazineBorrowDays = 7;

        System.out.println("\nConfiguration saved:");
        System.out.printf("- Daily fine rate: $%.2f%n", dailyFineRate);
        System.out.println("- Book borrow period: " + bookBorrowDays + " days");
        System.out.println("- Magazine borrow period: " + magazineBorrowDays + " days");

        return new LibraryConfig(dailyFineRate, bookBorrowDays, magazineBorrowDays);
    }

    private static void displayWelcome() {
        System.out.println("=========================================");
        System.out.println("   WELCOME TO LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=========================================");
    }

    private static void showMenu() throws IOException {
        while (true) {
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

            switch (choice) {
                case 1 -> displayAllItems();
                case 2 -> addNewBook();
                case 3 -> addNewMagazine();
                case 4 -> addNewMember();
                case 5 -> borrowItem();
                case 6 -> returnItem();
                case 7 -> searchItem();
                case 8 -> displayMemberInfo();
                case 9 -> checkOverdueItems();
                case 10 -> {
                    System.out.println("Thank you for using Tathagata's Library Management System!");
                    reader.close();
                    System.exit(0);
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
            item.displayDetails();
            System.out.println("Status: " + (item.isAvailable() ? "Available" : "Borrowed"));
            System.out.println("Borrow Period: " + item.getBorrowDays() + " days");

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

        Optional<Member> memberOpt = library.findMember(memberId);
        if (!memberOpt.isPresent()) {
            System.out.println("❌ Member with ID '" + memberId + "' not found.");
            return;
        }

        Optional<LibraryItem> itemOpt = library.findItem(itemId);
        if (!itemOpt.isPresent()) {
            System.out.println("❌ Item with ID '" + itemId + "' not found.");
            return;
        }

        if (!itemOpt.get().isAvailable()) {
            System.out.println("❌ Item is already borrowed by someone else.");
            return;
        }

        if (library.hasOverdueItems(memberId)) {
            System.out.println("❌ Member has overdue items. Please return them first.");
            double totalFines = library.calculateTotalFines(memberId);
            System.out.printf("Total outstanding fines: $%.2f%n", totalFines);
            return;
        }

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

        Optional<Member> memberOpt = library.findMember(memberId);
        if (!memberOpt.isPresent()) {
            System.out.println("❌ Member with ID '" + memberId + "' not found.");
            return;
        }

        Optional<LibraryItem> itemOpt = library.findItem(itemId);
        if (!itemOpt.isPresent()) {
            System.out.println("❌ Item with ID '" + itemId + "' not found.");
            return;
        }

        Optional<BorrowRecord> recordOpt = library.getBorrowRecord(itemId);
        if (!recordOpt.isPresent() || !recordOpt.get().getMemberId().equals(memberId)) {
            System.out.println("❌ This item was not borrowed by this member.");
            return;
        }

        BorrowRecord record = recordOpt.get();
        boolean wasOverdue = record.isOverdue();
        double fine = wasOverdue ? record.calculateFine(library.getConfig().getDailyFineRate()) : 0;

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

            if (totalFines > 0) {
                System.out.printf("
💰 Total Outstanding Fines: $%.2f%n", totalFines);
            }
        } else {
            System.out.println("❌ Member with ID '" + memberId + "' not found.");
        }
    }

    private static int getIntInput() throws IOException {

    private static void checkOverdueItems() throws IOException {
        System.out.println("\n=== Overdue Items Report ===");
        System.out.print("Enter Member ID (or press Enter for all members): ");
        String memberId = reader.readLine().trim();

        if (memberId.isEmpty()) {
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
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private static double getDoubleInput() throws IOException {
        while (true) {
            try {
                String input = reader.readLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}