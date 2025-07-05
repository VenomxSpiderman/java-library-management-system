# 📚 Library Management System

> A Java application demonstrating Object-Oriented Programming principles with modern Java features.

## 🎯 Project Overview

This Library Management System demonstrates core OOP principles and modern Java feature

### 🌟 Key Features

- **OOP Implementation**: All four pillars demonstrated (Abstraction, Inheritance, Encapsulation, Polymorphism)
- **Modern Java**: Switch expressions, Stream API, Optional handling
- **Separation of Concerns**: UI layer separate from business logic
- **Good Documentation**: Inline comments and JavaDoc throughout
- **Simple Design**: Easy to understand and modify

---

## �️ Architecture & Design Patterns

### **Object-Oriented Programming Principles**

| Principle | Implementation | Example |
|-----------|----------------|---------|
| **🔸 Abstraction** | `LibraryItem` abstract class | Common interface for Books/Magazines |
| **🔸 Inheritance** | `Book` and `Magazine` extend `LibraryItem` | Shared behavior, specific implementations |
| **🔸 Encapsulation** | Private fields with getter/setter methods | Data protection and controlled access |
| **🔸 Polymorphism** | `displayDetails()` method overriding | Same method, different behaviors |

### **Design Patterns Used**

- **Facade Pattern**: `LibraryManagementSystem` provides simplified interface
- **Repository Pattern**: Data access through HashMap collections
- **Dependency Injection**: Configuration passed to system constructor
- **Template Method**: Abstract methods in `LibraryItem`

---

## 🚀 Features & Functionality

### **Core Features**
- ✅ **Item Management**: Add and manage Books and Magazines
- ✅ **Member Registration**: Register library members with validation
- ✅ **Borrowing System**: Borrow/return items with automatic due dates
- ✅ **Fine Calculation**: Automatic fine calculation for overdue items
- ✅ **Search Functionality**: Find items and members by ID
- ✅ **Reporting**: Comprehensive overdue items reports

### **Advanced Features**
- ⚙️ **Configurable Settings**: Custom fine rates and borrow periods
- 🛡️ **Input Validation**: Error handling with user-friendly messages
- 📊 **HashMap Lookups**: O(1) performance for item/member searches
- 🎨 **Console Interface**: Clean menu system with status indicators
- 📅 **Date Handling**: Simple date arithmetic using LocalDate

### **Business Rules**
- Members with overdue items cannot borrow new items
- Different item types have different borrow periods
- Automatic fine calculation based on days overdue
- Real-time availability tracking

---

## 🛠️ Technology Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Language** | Java 14+ | Core application development |
| **Input Handling** | BufferedReader | Efficient console input |
| **Collections** | ArrayList, HashMap | Data storage and retrieval |
| **Date Handling** | LocalDate | Simple date operations |
| **Functional Programming** | Stream API | Data processing and filtering |
| **Error Handling** | Optional, try-catch | Null safety and exception management |

---

## 📋 Getting Started

### **Prerequisites**
- Java Development Kit (JDK) 14 or higher
- Command line terminal or Java IDE

### **Installation & Setup**

```bash
# 1. Clone or download the project
git clone https://github.com/VenomxSpiderman/java-library-management-system.git
cd java-library-management-system

# 2. Compile the application
javac LibraryMain.java

# 3. Run the application
java LibraryMain
```

### **Initial Configuration**
Upon first run, you'll be prompted to configure:
- **Daily fine rate** (e.g., $0.50 per day)
- **Book borrow period** (default: 14 days)
- **Magazine borrow period** (default: 7 days)

---

## 🎮 Usage Guide

### **Main Menu Options**

```
========== MAIN MENU ==========
1. Display All Items          - View all library items with status
2. Add New Book              - Register a new book in the system
3. Add New Magazine          - Register a new magazine in the system
4. Add New Member            - Register a new library member
5. Borrow Item               - Process borrowing transaction
6. Return Item               - Process return and calculate fines
7. Search Item by ID         - Find specific items
8. Display Member Info       - View member details and borrowed items
9. Check Overdue Items       - Generate overdue reports
10. Exit                     - Close the application
```

### **Sample Workflow**

1. **Add a Member**: Register with ID, name, and email
2. **Add Items**: Add books and magazines to the library
3. **Borrow Items**: Members can borrow available items
4. **Check Status**: View all items and their availability
5. **Return Items**: Process returns and handle any fines
6. **Generate Reports**: Check overdue items and fines

---

## 💻 Code Structure

### **Class Hierarchy**
```
LibraryMain (UI Layer)
├── LibraryManagementSystem (Business Logic)
├── LibraryConfig (Configuration)
├── LibraryItem (Abstract Base)
│   ├── Book (Concrete Implementation)
│   └── Magazine (Concrete Implementation)
├── Member (Data Model)
└── BorrowRecord (Transaction Record)
```

### **Key Classes Overview**

| Class | Responsibility | OOP Principle |
|-------|----------------|---------------|
| `LibraryItem` | Abstract base for all items | Abstraction |
| `Book/Magazine` | Specific item implementations | Inheritance |
| `Member` | Member data and behavior | Encapsulation |
| `BorrowRecord` | Immutable transaction record | Data Integrity |
| `LibraryManagementSystem` | Core business logic | Facade Pattern |
| `LibraryMain` | User interface layer | Separation of Concerns |

---

## 🧪 Testing the Application

### **Test Scenarios**

1. **Basic Operations**
   ```bash
   # Add a member: ID=M001, Name=John Doe, Email=john@email.com
   # Add a book: ID=B001, Title=Java Programming, Author=James Gosling, ISBN=123456
   # Borrow the book with member M001
   # Return the book and check for fines
   ```

2. **Error Handling**
   ```bash
   # Try borrowing non-existent item
   # Try borrowing with non-existent member
   # Try borrowing when member has overdue items
   ```

3. **Date Scenarios**
   ```bash
   # Add magazine with issue date: 2024-01-15
   # Test overdue calculations
   # Check fine calculations
   ```

---

## 🎯 Interview Discussion Points

### **Technical Highlights**
- **OOP Implementation**: Clear demonstration of all four principles
- **Modern Java**: Switch expressions, Stream API, Optional usage
- **Good Documentation**: Comprehensive comments and naming conventions
- **Error Handling**: Simple validation without complex exception hierarchies
- **Performance**: O(1) lookups using HashMap for scalability

### **Design Decisions**
- **BufferedReader vs Scanner**: More efficient for console applications
- **LocalDate everywhere**: Consistent date handling without complexity
- **Boolean returns**: Simple error handling delegated to UI layer
- **Configuration injection**: Flexible system without hardcoded values
- **Immutable records**: Data integrity for transaction history

### **Scalability Considerations**
- HashMap-based storage for O(1) lookups
- Defensive copying for data protection
- Stream API for functional data processing
- Configurable parameters for different library types

---

## 📈 Future Enhancements

This project can be extended with:
- **Persistence Layer**: Database integration with JPA/Hibernate
- **Web Interface**: Spring Boot REST API with React frontend
- **Authentication**: User roles and permissions system
- **Notifications**: Email/SMS alerts for due dates
- **Reporting**: Advanced analytics and reporting features
- **Multi-tenancy**: Support for multiple library branches

---

## 🏆 Project Benefits

### **For Learning**
- Complete OOP implementation
- Modern Java features usage
- Good architecture principles
- Documentation practices

### **For Portfolio**
- Clear demonstration of skills
- Understandable codebase
- Extensible foundation

### **For Development**
- Real-world problem solving
- Standard practices
- Maintainable code structure
- Performance considerations

---

## 📝 License

This project is created for educational and portfolio purposes. Feel free to use it as a reference for learning Java and OOP principles.

---

## 👨‍💻 Author

**Tathagata**
- GitHub: [VenomxSpiderman](https://github.com/VenomxSpiderman)
- LinkedIn: [tathagata06](https://linkedin.com/in/tathagata06)

---

## 📋 Menu Options

When you run the program, you'll see an interactive menu with the following options:

1. **Display All Items** - View all library items with availability, due dates, and overdue status
2. **Add New Book** - Register a new book with ID, title, author, and ISBN
3. **Add New Magazine** - Register a new magazine with ID, title, issue date (YYYY-MM), and number
4. **Add New Member** - Add a new library member with ID, name, and email
5. **Borrow Item** - Allow a member to borrow an available item (with overdue checks)
6. **Return Item** - Process item returns with automatic fine calculation
7. **Search Item by ID** - Find and display details of a specific item with borrowing info
8. **Display Member Info** - View member details, borrowed items, and outstanding fines
9. **Check Overdue Items** - Generate overdue reports for all members or specific member
10. **Exit** - Close the application

## 🏗️ System Architecture

### Class Structure

```
LibraryItem (Abstract Class)
├── Book (Concrete Class)
└── Magazine (Concrete Class)

Member (Class)
BorrowRecord (Class)

LibraryManagementSystem (Core Business Logic)
LibraryMain (UI Layer)
```

### Key Components

#### Error Handling with Clear Messages
The system provides specific error messages for different scenarios:
- Member not found
- Item not found  
- Item already borrowed
- Member has overdue items

#### Borrow Records
```java
class BorrowRecord {
    private final String memberId;
    private final String itemId;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    
    public boolean isOverdue() { /* ... */ }
    public double calculateFine(double dailyRate) { /* ... */ }
}
```

#### Performance Optimizations
- HashMap-based lookups: `O(1)` instead of `O(n)` for finding items/members
- Dual data structures: Lists for iteration, Maps for fast lookup

## 🏗️ System Architecture

### Class Structure

```
LibraryItem (Abstract Class)
├── Book (Concrete Class)
└── Magazine (Concrete Class)

Member (Class)

LibraryManagementSystem (Main System Class)

LibraryMain (Entry Point with Menu Interface)
```

### Key Classes

#### 📖 LibraryItem (Abstract Class)
- **Purpose**: Base class for all library items
- **Key Features**: Abstract methods for display and borrow period
- **Attributes**: ID, title, availability status

#### 📚 Book Class
- **Inherits**: LibraryItem
- **Additional Attributes**: Author, ISBN
- **Borrow Period**: 14 days

#### 📰 Magazine Class
- **Inherits**: LibraryItem
- **Additional Attributes**: Issue date, issue number
- **Borrow Period**: 7 days

#### 👤 Member Class
- **Purpose**: Represents library members
- **Attributes**: Member ID, name, email, borrowed items list
- **Features**: Track borrowed items, borrowing/returning functionality

#### 🏛️ LibraryManagementSystem Class
- **Purpose**: Core system logic
- **Features**: Item management, member management, borrowing/returning logic
- **Data Structures**: Lists for items/members, Map for borrow records

## 💡 Sample Usage

### Getting Started
The system starts with an empty library. You can:
1. Start the application
2. Choose option 2 to add a new book
3. Choose option 4 to add a new member  
4. Choose option 5 to borrow items
5. Choose option 1 to view all items and their status

### Example Workflow
1. Add some books and magazines using options 2 and 3
2. Add library members using option 4
3. Use option 5 to borrow items
4. Use option 8 to view member information and borrowed items
5. Use option 6 to return items

## 💡 Sample Usage & Examples

### Error Handling Implementation
```java
// Simple boolean return with detailed error checking in UI
boolean success = library.borrowItem(memberId, itemId);
if (!success) {
    // UI layer handles specific error messages
    if (!library.findMember(memberId).isPresent()) {
        System.out.println("❌ Member not found");
    } else if (!library.findItem(itemId).isPresent()) {
        System.out.println("❌ Item not found");
    } else if (!library.findItem(itemId).get().isAvailable()) {
        System.out.println("❌ Item already borrowed");
    } else if (library.hasOverdueItems(memberId)) {
        System.out.println("❌ Member has overdue items");
    }
}
```

### Type-Safe Date Handling
```java
// Magazine with proper date type
Magazine magazine = new Magazine("M001", "Tech Today", YearMonth.of(2024, 1), 15);

// Borrow record with automatic due date calculation
BorrowRecord record = new BorrowRecord("MEM001", "B001", LocalDate.now(), 14);
LocalDate dueDate = record.getDueDate(); // borrowDate + 14 days
```

### Overdue Management
```java
// Check if item is overdue
if (record.isOverdue()) {
    long daysOverdue = record.getDaysOverdue();
    double fine = record.calculateFine(0.50); // $0.50 per day
    System.out.printf("Fine: $%.2f for %d days overdue%n", fine, daysOverdue);
}
```

## 📊 Features Implementation

### Borrowing Rules
- **Availability Check**: Can't borrow already borrowed items
- **Member Validation**: Ensures member exists in system
- **Overdue Prevention**: Blocks borrowing if member has overdue items
- **Due Date Tracking**: Automatic calculation based on item type (Books: 14 days, Magazines: 7 days)

### User Interface Feedback
- **Precise Error Messages**: Know exactly what went wrong
- **Due Date Display**: See when items need to be returned
- **Overdue Alerts**: Visual indicators for late items
- **Fine Calculation**: Real-time fine computation and display

### Reporting System
- **Individual Reports**: Member-specific overdue and fine reports
- **System-wide Reports**: All overdue items across the library
- **Financial Tracking**: Outstanding fines per member
- **Borrowing History**: Track who borrowed what and when

## 🔧 Technical Details

### Object-Oriented Features

#### Abstraction
```java
abstract class LibraryItem {
    public abstract void displayDetails();
    public abstract int getBorrowDays();
}
```

#### Inheritance
```java
class Book extends LibraryItem {
    // Book-specific implementation with 14-day borrow period
}

class Magazine extends LibraryItem {
    // Magazine-specific implementation with 7-day borrow period
    private YearMonth issueDate; // Type-safe date handling
}
```

#### Polymorphism
```java
// Works with any LibraryItem type
public void displayAllItems() {
    for (LibraryItem item : items) {
        item.displayDetails(); // Polymorphic call
        System.out.println("Borrow Period: " + item.getBorrowDays() + " days");
    }
}
```

#### Encapsulation
```java
private String memberId;
private Map<String, LibraryItem> itemMap; // Fast O(1) lookups
private Map<String, BorrowRecord> borrowRecords; // Rich borrowing data

public Optional<LibraryItem> findItem(String itemId) { 
    return Optional.ofNullable(itemMap.get(itemId)); 
}
```

### Design Patterns

#### Separation of Concerns
```java
// Business Logic Layer (LibraryManagementSystem)
public class LibraryManagementSystem {
    public boolean borrowItem(String memberId, String itemId) {
        // Pure business logic, no UI concerns
        Member member = memberMap.get(memberId);
        if (member == null) return false;
        
        LibraryItem item = itemMap.get(itemId);
        if (item == null) return false;
        
        if (!item.isAvailable()) return false;
        
        // Business logic here
        return true;
    }
}

// UI Layer (LibraryMain)
public class LibraryMain {
    private static void borrowItem() {
        // Handle user input and display specific error messages
        boolean success = library.borrowItem(memberId, itemId);
        if (!success) {
            // UI determines specific error and displays appropriate message
        }
    }
}
    }
}
```

### Performance Optimizations

#### Dual Data Structures
```java
private List<LibraryItem> items;           // For iteration
private Map<String, LibraryItem> itemMap;  // For O(1) lookups
```

#### Time Complexity Analysis
- **Finding Items/Members**: O(1) with HashMap vs O(n) with List scanning
- **Adding Items**: O(1) average case
- **Overdue Calculation**: O(k) where k is number of borrowed items per member
private List<LibraryItem> borrowedItems;

```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/NewFeature`)
3. Commit your changes (`git commit -m 'Add some NewFeature'`)
4. Push to the branch (`git push origin feature/NewFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for more details.


⭐ Star this repository if you found it helpful! ⭐
