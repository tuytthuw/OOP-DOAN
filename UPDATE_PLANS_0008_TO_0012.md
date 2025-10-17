# 📋 HƯỚNG DẪN CẬP NHẬT TẤT CẢ CÁC KẾ HOẠCH (0008-0012) - TUÂN THỨ INSTRUCTIONS

## ⚠️ NGUYÊN TẮC CHUNG: Không dùng java.util Collections, dùng mảng T[]

---

## 📋 DANH SÁCH CÁC KẾ HOẠCH CẦN CẬP NHẬT

### ✅ Kế Hoạch 0007 - Xây Dựng Collection Manager

**Trạng thái**: ✅ **ĐÃ CẬP NHẬT**

- Sử dụng `T[]` thay vì `List<T>`
- BaseManager quản lý mảy động với logic resize
- Tất cả managers extends BaseManager

---

### 🔄 Kế Hoạch 0008 - Xây Dựng Business Logic Services

**Cần cập nhật:**

**Tệp TẠO MỚI:**

- `src/main/java/services/CustomerService.java`
- `src/main/java/services/AppointmentService.java`
- `src/main/java/services/InvoiceService.java`
- `src/main/java/services/PaymentService.java`
- `src/main/java/services/ReportService.java`

**Thay đổi chính:**

```java
// ❌ SAI - Không dùng
List<Customer> customers = customerManager.getAll();

// ✅ ĐÚNG - Dùng mảy
Customer[] customers = customerManager.getAll();
```

**Phương thức:**

- Tất cả input/output là mảy `T[]`
- Không import `java.util.*`
- Service gọi các Manager method

**Ví dụ - CustomerService:**

```java
public class CustomerService {
    private CustomerManager customerManager;

    public CustomerService(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    // ✅ Trả về mảy thay vì List
    public Customer[] getActiveCustomers() {
        return customerManager.findActiveCustomers();
    }

    // ✅ Tính toán trên mảy
    public java.math.BigDecimal getTotalSpentByAllCustomers() {
        Customer[] customers = customerManager.getAll();
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (Customer customer : customers) {
            total = total.add(customer.getTotalSpent());
        }
        return total;
    }
}
```

---

### 🔄 Kế Hoạch 0009 - Xây Dựng Input/Output Manager

**Cần cập nhật:**

**Tệp TẠO MỚI:**

- `src/main/java/io/InputHandler.java`
- `src/main/java/io/OutputFormatter.java`

**Thay đổi chính:**

```java
// ✅ InputHandler - nhập dữ liệu từ console
public class InputHandler {
    public String getString(String prompt) { ... }
    public int getInt(String prompt) { ... }
    public double getDouble(String prompt) { ... }
    // Không thay đổi nhiều (vẫn dùng Scanner)
}

// ✅ OutputFormatter - định dạng output
public class OutputFormatter {
    // Phương thức hiển thị mảy
    public void displayArray(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println((i+1) + ". " + array[i]);
        }
    }

    public void displayCustomers(Customer[] customers) {
        for (Customer c : customers) {
            c.display();
        }
    }
}
```

---

### 🔄 Kế Hoạch 0010 - Xây Dựng Giao Diện Menu

**Cần cập nhật:**

**Tệp TẠO MỚI:**

- `src/main/java/ui/MainMenu.java`
- `src/main/java/ui/CustomerMenu.java`
- `src/main/java/ui/AppointmentMenu.java`
- `src/main/java/ui/InvoiceMenu.java`

**Thay đổi chính:**

```java
// ✅ Menu
public class MainMenu {
    private CustomerManager customerManager;
    private CustomerService customerService;

    public void showCustomerList() {
        // ✅ Dùng mảy, không dùng List
        Customer[] customers = customerManager.getAll();
        for (Customer c : customers) {
            System.out.println(c);
        }
    }

    public void searchCustomerByTier() {
        Tier tier = // ... nhập từ console
        Customer[] results = customerManager.findByTier(tier);
        // Hiển thị kết quả từ mảy
    }
}
```

---

### 🔄 Kế Hoạch 0011 - Xây Dựng Employee/Staff

**Cần cập nhật:**

**Tệp TẠO MỚI:**

- `src/main/java/models/Employee.java` (extends Person)
- `src/main/java/models/Receptionist.java` (extends Employee)
- `src/main/java/models/Technician.java` (extends Employee)
- `src/main/java/enums/EmployeeRole.java`
- `src/main/java/enums/EmployeeStatus.java`
- `src/main/java/collections/EmployeeManager.java` (extends BaseManager<Employee>)

**Thay đổi chính:**

```java
// ✅ EmployeeManager
public class EmployeeManager extends BaseManager<Employee> {
    // Sử dụng mảy giống như các Manager khác

    public Employee[] findByRole(EmployeeRole role) {
        Employee[] result = new Employee[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getRole() == role) {
                result[count++] = items[i];
            }
        }
        Employee[] finalResult = new Employee[count];
        for (int i = 0; i < count; i++) {
            finalResult[i] = result[i];
        }
        return finalResult;
    }
}
```

---

### 🔄 Kế Hoạch 0012 - Exception Handling & Custom Exceptions

**Cần cập nhật:**

**Tệp TẠO MỚI:**

- `src/main/java/exceptions/BaseException.java`
- `src/main/java/exceptions/EntityNotFoundException.java`
- `src/main/java/exceptions/InvalidEntityException.java`
- `src/main/java/exceptions/BusinessLogicException.java`
- `src/main/java/exceptions/PaymentException.java`
- `src/main/java/exceptions/ValidationException.java`

**Thay đổi chính:**

```java
// ✅ Custom Exceptions
public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}

// ✅ Manager & Service throw exceptions
public class CustomerManager extends BaseManager<Customer> {
    @Override
    public boolean add(Customer item) {
        if (item == null) {
            throw new InvalidEntityException("Customer không được null");
        }

        if (exists(item.getId())) {
            throw new EntityNotFoundException("Customer với ID '" + item.getId() + "' đã tồn tại");
        }

        // ... tiếp tục logic
        return true;
    }
}
```

---

## 🎯 NGUYÊN TẮC CHUNG KHÔNG ĐỔI

### ❌ TUYỆT ĐỐI KHÔNG DÙNG:

```java
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
```

### ✅ PHẢI DÙNG:

```java
// Mảy thay vì List
Customer[] customers = new Customer[capacity];
int size = 0;

// Dynamic array: dùng mảy với logic resize
// Hoặc dùng BaseManager<T> extends managers
```

### ✅ QUY TẮC CÓ THỂ DỮ:

```java
// Collections framework cho tính toán nội bộ (scanner, etc)
import java.util.Scanner;  // ✅ OK
import java.util.stream.*;  // ❌ KHÔNG - stream dùng internally collection

// Date/Time
import java.time.*;  // ✅ OK
import java.time.format.*;  // ✅ OK
```

---

## 📝 CHECKLIST CẬP NHẬT TỪNG KẾ HOẠCH

### Kế Hoạch 0008 - Business Logic Services

- [ ] Không import java.util.List, ArrayList, etc
- [ ] Tất cả method trả về mảy `T[]`
- [ ] Gọi Manager methods
- [ ] Comment bằng tiếng Việt

### Kế Hoạch 0009 - IO Manager

- [ ] InputHandler (dùng Scanner OK)
- [ ] OutputFormatter (hiển thị mảy)

### Kế Hoạch 0010 - Menu UI

- [ ] MainMenu, CustomerMenu, AppointmentMenu, InvoiceMenu
- [ ] Dùng Service & Manager
- [ ] Hiển thị mảy kết quả

### Kế Hoạch 0011 - Employee Models

- [ ] Employee, Receptionist, Technician classes
- [ ] EmployeeManager extends BaseManager
- [ ] Enums: EmployeeRole, EmployeeStatus

### Kế Hoạch 0012 - Exception Handling

- [ ] Custom Exception classes
- [ ] Manager & Service throw exceptions
- [ ] UI layer catch & display errors

---

## 🚀 LỢI ÍC CỦA CÁC ĐỔI

✅ **Tuân thủ 100% instructions**
✅ **Chứng tỏ hiểu biết OOP & Data Structure**
✅ **Không phụ thuộc Java Collections Framework**
✅ **Kiểm soát tốt bộ nhớ & hiệu năng**
✅ **Dễ port sang các ngôn ngữ khác**

---

## 📌 LƯU Ý QUAN TRỌNG

1. **Mảy động resize**: Khi `size >= capacity`, tự động tăng gấp đôi
2. **Kết quả tìm kiếm**: Trả về mảy con (size = số kết quả), không phải null
3. **Không import collections**: Tuyệt đối không import `java.util.List`, `ArrayList`, etc
4. **BaseManager**: Là lớp generic cơ sở, tất cả manager extends nó
5. **Soft delete**: Có thể implement bằng `isDeleted` flag thay vì xóa vật lý
