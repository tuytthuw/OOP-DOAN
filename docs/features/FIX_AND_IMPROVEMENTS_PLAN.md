# 🎯 ACTIONABLE RECOMMENDATIONS & FIX PLAN

**Document:** Quick Fix Guide  
**Date:** October 17, 2025  
**Priority:** Based on Impact & Difficulty

---

## 📋 Quick Reference Table

| Priority | Issue                     | File              | Severity    | Effort  | Impact |
| :------- | :------------------------ | :---------------- | :---------- | :------ | :----- |
| 1        | Password hashing unsafe   | Employee.java     | 🔴 CRITICAL | 30 min  | HIGH   |
| 2        | Refactor large files      | EmployeeMenu.java | 🟠 MAJOR    | 1 hour  | MEDIUM |
| 3        | Add unit tests            | Multiple          | 🟠 MAJOR    | 3 hours | HIGH   |
| 4        | Implement equals/hashCode | Person.java       | 🟡 MINOR    | 30 min  | LOW    |
| 5        | Add logging system        | All               | 🟡 MINOR    | 1 hour  | MEDIUM |
| 6        | Configuration file        | -                 | 🟡 MINOR    | 30 min  | LOW    |

---

## 🔴 FIX #1: PASSWORD HASHING SECURITY (CRITICAL)

### Current Implementation ❌

```java
// Employee.java - Line 120
private String hashPassword(String password) {
    return Integer.toHexString(password.hashCode());
}

public boolean checkPassword(String password) {
    if (password == null || password.isEmpty()) return false;
    String hash = hashPassword(password);
    return hash.equals(this.passwordHash);
}
```

**Problem:**

- `hashCode()` is not cryptographic
- Collision risk (multiple passwords hash to same value)
- Not suitable for security-sensitive operations
- **Security Risk Level: HIGH**

### Recommended Fix ✅

```java
import java.security.MessageDigest;
import java.util.Base64;

private String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("SHA-256 algorithm not available", e);
    }
}
```

**Better Alternative (Recommended for Production):**

```java
// Add dependency to pom.xml:
// <dependency>
//     <groupId>at.favre.lib</groupId>
//     <artifactId>bcrypt</artifactId>
//     <version>0.10.2</version>
// </dependency>

import at.favre.lib.crypto.bcrypt.BCrypt;

private String hashPassword(String password) {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray());
}

public boolean checkPassword(String password) {
    if (password == null || password.isEmpty()) return false;
    return BCrypt.verifyer().verify(password.toCharArray(), this.passwordHash).verified;
}
```

**Steps to Implement:**

1. Add BCrypt dependency to `pom.xml`
2. Update `Employee.java` import
3. Modify `hashPassword()` method
4. Modify `checkPassword()` method
5. Test password creation and verification

**Estimated Time:** 30 minutes

---

## 🟠 FIX #2: REFACTOR LARGE FILES

### Issue: EmployeeMenu.java (543 lines)

**Current Structure:**

```
EmployeeMenu (543 lines)
├── displayMenu() - All 10+ options
├── handleChoice() - Switch with 10+ cases
└── All business logic in one file
```

**Problem:**

- Violates Single Responsibility Principle
- Hard to maintain and test
- Difficult to extend

### Recommended Fix ✅

**Option A: Split by Role (Recommended)**

```
ui/
├── EmployeeMenu.java (base, ~150 lines)
├── ReceptionistSubMenu.java (~200 lines)
└── TechnicianSubMenu.java (~200 lines)
```

**Implementation:**

```java
// EmployeeMenu.java (refactored)
public class EmployeeMenu extends MenuBase {
    private ReceptionistSubMenu receptionistMenu;
    private TechnicianSubMenu technicianMenu;

    @Override
    protected void displayMenu() {
        System.out.println("1. Quản lý Lễ Tân");
        System.out.println("2. Quản lý Thợ Xoa");
        System.out.println("0. Quay Lại");
    }

    @Override
    protected boolean handleChoice(int choice) {
        switch (choice) {
            case 1: receptionistMenu.run(); return true;
            case 2: technicianMenu.run(); return true;
            case 0: return false;
            default: return true;
        }
    }
}

// ReceptionistSubMenu.java (new)
public class ReceptionistSubMenu extends MenuBase {
    // Handle all receptionist operations (~200 lines)
}

// TechnicianSubMenu.java (new)
public class TechnicianSubMenu extends MenuBase {
    // Handle all technician operations (~200 lines)
}
```

**Estimated Time:** 1 hour

---

## 🟠 FIX #3: ADD UNIT TESTS

### Test Framework: JUnit 5 + Mockito

**Step 1: Add Dependencies**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.3.1</version>
    <scope>test</scope>
</dependency>
```

**Step 2: Create Test Files**

```java
// src/test/java/services/CustomerServiceTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {
    private CustomerService customerService;
    private CustomerManager mockCustomerManager;

    @BeforeEach
    public void setUp() {
        mockCustomerManager = mock(CustomerManager.class);
        customerService = new CustomerService(mockCustomerManager);
    }

    @Test
    public void testRegisterNewCustomer_Success() throws Exception {
        // Arrange
        when(mockCustomerManager.findByPhone("0901234567")).thenReturn(null);

        // Act
        Customer result = customerService.registerNewCustomer(
            "Nguyễn Văn A", "0901234567", "a@email.com",
            "123 Đường ABC", true, LocalDate.of(1990, 1, 1)
        );

        // Assert
        assertNotNull(result);
        assertEquals("Nguyễn Văn A", result.getFullName());
        verify(mockCustomerManager, times(1)).add(any(Customer.class));
    }

    @Test
    public void testRegisterNewCustomer_DuplicatePhone() throws Exception {
        // Arrange
        Customer existingCustomer = mock(Customer.class);
        when(mockCustomerManager.findByPhone("0901234567"))
            .thenReturn(existingCustomer);

        // Act & Assert
        assertThrows(BusinessLogicException.class, () -> {
            customerService.registerNewCustomer(
                "Nguyễn Văn B", "0901234567", "", "", true, null
            );
        });
    }
}
```

**Step 3: Test Coverage**

```java
// Create tests for:
// - BaseManager.add() / update() / delete() / getById()
// - CustomerService.registerNewCustomer()
// - AppointmentService.bookAppointment()
// - Exception handling in all services
// - Calculation logic in PaymentService
```

**Estimated Time:** 3 hours
**Coverage Target:** 80%+

---

## 🟡 FIX #4: IMPLEMENT EQUALS & HASHCODE

### Issue: Person.java Missing equals/hashCode

**Current:**

```java
// Uses default reference equality
Person p1 = new Person("P001", "A", ...);
Person p2 = new Person("P001", "A", ...);
p1.equals(p2); // false (should be true!)
```

**Fix:**

```java
// Person.java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return personId.equals(person.personId);
}

@Override
public int hashCode() {
    return Objects.hash(personId);
}

@Override
public String toString() {
    return String.format("Person(id=%s, name=%s)", personId, fullName);
}
```

**Estimated Time:** 30 minutes

---

## 🟡 FIX #5: ADD LOGGING SYSTEM

### Recommended: SLF4J + Logback

**Step 1: Add Dependencies**

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.11</version>
</dependency>
```

**Step 2: Create logback.xml**

```xml
<!-- src/main/resources/logback.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>
                %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>logs/spa-app.log</file>
        <encoder>
            <pattern>
                %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

**Step 3: Use in Code**

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public Customer registerNewCustomer(...) throws Exception {
        try {
            logger.info("Attempting to register customer: {}", phoneNumber);
            // ... registration logic
            logger.info("Customer registered successfully: {}", customerId);
            return newCustomer;
        } catch (Exception e) {
            logger.error("Error registering customer: {}", phoneNumber, e);
            throw e;
        }
    }
}
```

**Estimated Time:** 1 hour

---

## 🟡 FIX #6: ADD CONFIGURATION FILE

### Create application.properties

```properties
# src/main/resources/application.properties

# Input/Output Configuration
input.maxRetries=3
input.dateFormat=dd/MM/yyyy
input.dateTimeFormat=dd/MM/yyyy HH:mm

# Business Logic Configuration
customer.tier.platinum.threshold=10000000
customer.tier.gold.threshold=5000000
customer.tier.silver.threshold=1000000

# Service Configuration
service.duration.min=15
service.duration.max=240

# Security Configuration
password.algorithm=SHA-256
password.minLength=6

# Data Configuration
manager.defaultCapacity=10

# Logging Configuration
logging.level=INFO
logging.file=logs/spa-app.log
```

**Usage in Code:**

```java
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static Properties props;

    static {
        props = new Properties();
        try {
            props.load(ConfigManager.class.getClassLoader()
                .getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load configuration", e);
        }
    }

    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }
}

// Usage:
int maxRetries = ConfigManager.getInt("input.maxRetries", 3);
String dateFormat = ConfigManager.get("input.dateFormat", "dd/MM/yyyy");
```

**Estimated Time:** 30 minutes

---

## 📊 IMPLEMENTATION TIMELINE

### Phase 1: Critical Fixes (Week 1)

- **Day 1:** Fix password hashing (30 min)
- **Day 2-3:** Add unit tests (3 hours)
- **Day 4-5:** Test and validate

### Phase 2: Quality Improvements (Week 2)

- **Day 1-2:** Refactor large files (1 hour)
- **Day 3:** Add logging system (1 hour)
- **Day 4:** Add configuration (30 min)
- **Day 5:** Test all changes

### Phase 3: Optional Enhancements (Week 3+)

- Add database layer
- Add REST API
- Add web UI
- Add reporting features

---

## ✅ VALIDATION CHECKLIST

After implementing fixes:

- [ ] Password hashing uses SHA-256 or BCrypt
- [ ] `checkPassword()` correctly verifies hashed passwords
- [ ] All EmployeeMenu options work correctly
- [ ] Unit tests pass (80%+ coverage)
- [ ] No compilation errors or warnings
- [ ] equals/hashCode implemented in Person
- [ ] Logging configured and working
- [ ] Configuration file loaded successfully
- [ ] Code review complete
- [ ] All changes committed to git

---

## 📞 SUPPORT & QUESTIONS

For implementation help:

1. Refer to comprehensive review: `/docs/features/COMPREHENSIVE_CODE_REVIEW.md`
2. Check best practices in existing code
3. Follow Google Java Style Guide
4. Use Vietnamese comments for clarity

---

**End of Fix Plan**
