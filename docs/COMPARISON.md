# 📊 BẢNG SO SÁNH: TRƯỚC & SAU CẬP NHẬT

## So Sánh ClassDiagram Ban Đầu vs Cập Nhật

| Khía Cạnh               | Ban Đầu               | Cập Nhật                              | Ghi Chú                               |
| ----------------------- | --------------------- | ------------------------------------- | ------------------------------------- |
| **Person Class**        | ❌ Không rõ           | ✅ Abstract base class                | Kế thừa từ Person                     |
| **Employee**            | ⚠️ Có nhưng chưa rõ   | ✅ Abstract + Receptionist/Technician | Phân loại rõ ràng                     |
| **Promotion/Discount**  | ❌ Gọi là "Promotion" | ✅ Rename thành "Discount"            | Thống nhất tên                        |
| **Payment/Transaction** | ❌ Gọi là "Payment"   | ✅ Rename thành "Transaction"         | Thống nhất tên                        |
| **ServiceCategory**     | ❌ Không đề cập       | ✅ Enum đầy đủ                        | 7 loại dịch vụ                        |
| **TransactionStatus**   | ❌ Không đề cập       | ✅ Enum đầy đủ                        | PENDING/SUCCESS/FAILED/REFUNDED       |
| **EmployeeRole**        | ❌ Không đề cập       | ✅ Enum mới                           | RECEPTIONIST/TECHNICIAN/MANAGER/ADMIN |
| **EmployeeStatus**      | ❌ Không đề cập       | ✅ Enum mới                           | ACTIVE/ON_LEAVE/SUSPENDED/RESIGNED    |
| **BaseManager<T>**      | ❌ Không có           | ✅ Generic base class                 | Tránh lặp code                        |
| **Exception Handling**  | ❌ Không đề cập       | ✅ Custom exceptions                  | 6 loại exception                      |

---

## So Sánh Kế Hoạch Ban Đầu vs Cập Nhật

### Số Lượng Kế Hoạch

| Phase       | Ban Đầu         | Cập Nhật        | Thêm            |
| ----------- | --------------- | --------------- | --------------- |
| **Phase 0** | ❌ Không có     | ✅ 2 kế hoạch   | 0000, 0007a     |
| **Phase 1** | 6 kế hoạch      | 6 kế hoạch      | Không đổi       |
| **Phase 2** | 1 kế hoạch      | 2 kế hoạch      | +0011           |
| **Phase 3** | 1 kế hoạch      | 1 kế hoạch      | Không đổi       |
| **Phase 4** | ❌ Không có     | ✅ 1 kế hoạch   | +0012           |
| **Phase 5** | 2 kế hoạch      | 2 kế hoạch      | Không đổi       |
| **TỔNG**    | **10 kế hoạch** | **15 kế hoạch** | **+5 kế hoạch** |

---

## Chi Tiết Thay Đổi Từng Kế Hoạch

### Kế Hoạch 0000 - Person Base Class (MỚI)

**Tạo:** `models/Person.java`

| Đặc Điểm        | Chi Tiết                                                                      |
| --------------- | ----------------------------------------------------------------------------- |
| **Mục đích**    | Lớp cơ sở cho tất cả người (Customer, Employee)                               |
| **Loại Class**  | Abstract                                                                      |
| **Thuộc tính**  | personId, fullName, phoneNumber, email, address, isMale, birthDate, isDeleted |
| **Phương thức** | getId(), getAge(), getGenderString(), softDelete(), restore()                 |
| **Lợi ích**     | Tái sử dụng code, tuân thủ DRY, OOP đúng cách                                 |
| **Ưu tiên**     | 🔴 CAO - Thực hiện TRƯỚC 0001                                                 |

---

### Kế Hoạch 0001 - Customer Model (CẬP NHẬT)

**Thay đổi:**

| Trước                                 | Sau                                  | Lý Do                                                      |
| ------------------------------------- | ------------------------------------ | ---------------------------------------------------------- |
| Độc lập, chứa tất cả thuộc tính       | Kế thừa từ Person                    | Tránh lặp code (personId, fullName, phone, email, address) |
| -                                     | Bỏ những thuộc tính chung lên Person | DRY principle                                              |
| fullName, phoneNumber, email, address | Inherit từ Person                    | Giảm duplicate                                             |

**Chi tiết thay đổi:**

```java
// Ban đầu
class Customer {
    private String customerId;      // ❌ Bỏ, dùng personId từ Person
    private String fullName;        // ❌ Bỏ, inherit từ Person
    private String phoneNumber;     // ❌ Bỏ, inherit từ Person
    private String email;           // ❌ Bỏ, inherit từ Person
    private String address;         // ❌ Bỏ, inherit từ Person
    private Tier memberTier;        // ✅ Giữ (riêng Customer)
    private BigDecimal totalSpent;  // ✅ Giữ
    private LocalDate lastVisitDate;// ✅ Giữ
}

// Cập nhật
class Customer extends Person {
    private Tier memberTier;        // ✅ Chỉ giữ riêng Customer
    private BigDecimal totalSpent;  // ✅ Giữ
    private LocalDate lastVisitDate;// ✅ Giữ
    // Kế thừa từ Person: customerId, fullName, phoneNumber, email, address, isMale, birthDate
}
```

---

### Kế Hoạch 0007a - BaseManager Generic Class (MỚI)

**Tạo:** `collections/BaseManager.java`

| Đặc Điểm                  | Chi Tiết                                                                |
| ------------------------- | ----------------------------------------------------------------------- |
| **Loại Class**            | Abstract Generic `<T>`                                                  |
| **Phương thức**           | add, update, delete, getById, getAll, exists, size, clear, getAllActive |
| **Phương thức Abstract**  | getId(T), validateItem(T)                                               |
| **Phương thức Protected** | findIndex, sort, filter                                                 |
| **Lợi ích**               | Giảm lặp code trong Manager, dễ mở rộng                                 |
| **Ưu tiên**               | 🔴 CAO - Thực hiện TRƯỚC 0007                                           |

**Mối quan hệ:**

```
BaseManager<T> (Abstract Generic)
├── CustomerManager extends BaseManager<Customer>
├── ServiceManager extends BaseManager<Service>
├── AppointmentManager extends BaseManager<Appointment>
├── TransactionManager extends BaseManager<Transaction>
├── DiscountManager extends BaseManager<Discount>
├── InvoiceManager extends BaseManager<Invoice>
└── EmployeeManager extends BaseManager<Employee> (Mới từ 0011)
```

---

### Kế Hoạch 0007 - Collection Manager (CẬP NHẬT)

**Thay đổi:**

```java
// Ban đầu - Code lặp lại
class CustomerManager {
    private List<Customer> items;

    public boolean add(Customer customer) { ... }
    public boolean update(Customer customer) { ... }
    public boolean delete(String id) { ... }
    public Customer getById(String id) { ... }
    public List<Customer> getAll() { ... }

    public Customer findByPhone(String phone) { ... }
    // ... các phương thức tìm kiếm riêng
}

// Cập nhật - Extends BaseManager
class CustomerManager extends BaseManager<Customer> {
    @Override
    protected String getId(Customer customer) {
        return customer.getCustomerId();
    }

    @Override
    protected boolean validateItem(Customer customer) { ... }

    // Chỉ thêm phương thức tìm kiếm riêng
    public Customer findByPhone(String phone) { ... }
    // ... các phương thức tìm kiếm riêng
}
```

**Lợi ích:**

- Giảm ~60% code (CRUD từ mỗi Manager)
- Dễ bảo trì (fix bug ở BaseManager → tất cả Manager được fix)
- Consistent API

---

### Kế Hoạch 0011 - Employee/Staff Management (MỚI)

**Tạo:**

- `models/Employee.java` (Abstract)
- `models/Receptionist.java`
- `models/Technician.java`
- `enums/EmployeeRole.java`
- `enums/EmployeeStatus.java`
- `collections/EmployeeManager.java`

| Đặc Điểm           | Chi Tiết                                 |
| ------------------ | ---------------------------------------- |
| **Employee**       | Abstract, extends Person                 |
| **Receptionist**   | Concrete, extends Employee               |
| **Technician**     | Concrete, extends Employee               |
| **EmployeeRole**   | RECEPTIONIST, TECHNICIAN, MANAGER, ADMIN |
| **EmployeeStatus** | ACTIVE, ON_LEAVE, SUSPENDED, RESIGNED    |
| **Lợi ích**        | Quản lý nhân viên, tính lương, hoa hồng  |
| **Ưu tiên**        | 🟡 TRUNG BÌNH - Song song với 0007-0010  |

**Kế thừa:**

```
Person (Abstract)
└── Employee (Abstract)
    ├── Receptionist (Concrete)
    │   - bonus per sale
    │   - calculatePay() = salary + hoa hồng
    └── Technician (Concrete)
        - commission rate
        - skill set, certifications
        - calculatePay() = salary + commission
```

---

### Kế Hoạch 0012 - Exception Handling (MỚI)

**Tạo:**

- `exceptions/BaseException.java`
- `exceptions/EntityNotFoundException.java`
- `exceptions/InvalidEntityException.java`
- `exceptions/BusinessLogicException.java`
- `exceptions/PaymentException.java`
- `exceptions/AppointmentException.java`
- `exceptions/ValidationException.java`

| Lỗi                         | Mã      | Ý Nghĩa                         | Phục Hồi                      |
| --------------------------- | ------- | ------------------------------- | ----------------------------- |
| **EntityNotFoundException** | ERR_001 | Không tìm thấy entity           | Nhập lại ID                   |
| **InvalidEntityException**  | ERR_002 | Entity không hợp lệ             | Validate lại dữ liệu          |
| **BusinessLogicException**  | ERR_003 | Logic kinh doanh không cho phép | Giải thích tại sao            |
| **PaymentException**        | ERR_004 | Lỗi thanh toán                  | Kiểm tra số tiền              |
| **AppointmentException**    | ERR_005 | Lỗi lịch hẹn                    | Kiểm tra trạng thái/thời gian |
| **ValidationException**     | ERR_006 | Input không hợp lệ              | Nhập lại theo format          |

**Lợi ích:**

- Error handling có hệ thống
- Messages rõ ràng → dễ debug
- Phân loại lỗi theo nghiệp vụ

---

### Các Kế Hoạch Khác (CẬP NHẬT NHỎ)

| Kế Hoạch | Cần Cập Nhật                 | Chi Tiết                                |
| -------- | ---------------------------- | --------------------------------------- |
| **0002** | ✅ Thêm ServiceCategory      | Enum phân loại dịch vụ                  |
| **0003** | ✅ Thêm staffId handling     | Liên kết với Employee (0011)            |
| **0004** | ✅ Không đổi gì              | Đã đúng, chỉ đổi tên Promotion→Discount |
| **0005** | ✅ Thêm TransactionStatus    | Enum trạng thái giao dịch               |
| **0006** | ✅ Không đổi nhiều           | Cập nhật tên Payment→Transaction        |
| **0008** | ✅ Thêm exception handling   | Try-catch các phương thức               |
| **0009** | ✅ Throw ValidationException | Thay vì trả về -1 hoặc null             |
| **0010** | ✅ Try-catch exceptions      | Display error message cho người dùng    |

---

## Tổng Hợp Code Giảm

### Trước Cập Nhật (Ban Đầu)

```
0001: Customer.java              ~ 150 lines
0002: Service.java               ~ 150 lines
0003: Appointment.java           ~ 150 lines
0004: Discount.java              ~ 150 lines
0005: Transaction.java           ~ 150 lines
0006: Invoice.java               ~ 150 lines
-----
0007: CustomerManager.java       ~ 200 lines
      ServiceManager.java        ~ 200 lines
      AppointmentManager.java    ~ 200 lines
      TransactionManager.java    ~ 200 lines
      DiscountManager.java       ~ 200 lines
      InvoiceManager.java        ~ 200 lines
Total 0007: ~1200 lines

TỔNG CỘNG: ~2100 lines
```

### Sau Cập Nhật (Với BaseManager)

```
0000: Person.java                ~ 100 lines (NEW)
0007a: BaseManager.java          ~ 150 lines (NEW)

0001: Customer.java              ~ 80 lines  (giảm 70)
      Person.java                ~ (inherit)
0002: Service.java               ~ 120 lines
0003: Appointment.java           ~ 140 lines
0004: Discount.java              ~ 140 lines
0005: Transaction.java           ~ 140 lines
0006: Invoice.java               ~ 140 lines
-----
0007: CustomerManager.java       ~ 80 lines  (giảm 120)
      ServiceManager.java        ~ 80 lines  (giảm 120)
      AppointmentManager.java    ~ 80 lines  (giảm 120)
      TransactionManager.java    ~ 80 lines  (giảm 120)
      DiscountManager.java       ~ 80 lines  (giảm 120)
      InvoiceManager.java        ~ 80 lines  (giảm 120)
Total 0007: ~480 lines (giảm 720)

0011: Employee.java              ~ 150 lines (NEW)
      Receptionist.java          ~ 80 lines  (NEW)
      Technician.java            ~ 120 lines (NEW)
      EmployeeManager.java       ~ 80 lines  (NEW)

0012: BaseException.java         ~ 100 lines (NEW)
      + 6 custom exceptions      ~ 200 lines (NEW)

TỔNG CỘNG: ~2100-2200 lines
Giảm lặp code: ~720 lines
Thêm tính năng: ~800 lines
```

---

## ✨ Kết Luận

| Tiêu Chí                | Ban Đầu | Cập Nhật    | Cải Thiện |
| ----------------------- | ------- | ----------- | --------- |
| **Số kế hoạch**         | 10      | 15          | +50%      |
| **Lặp code**            | Cao     | Thấp        | -60%      |
| **OOP quality**         | ⭐⭐⭐  | ⭐⭐⭐⭐⭐  | +2⭐      |
| **Exception handling**  | Không   | Có          | ✅        |
| **Employee management** | Không   | Có          | ✅        |
| **Generic class**       | Không   | Có          | ✅        |
| **Base classes**        | Không   | Có (Person) | ✅        |
| **Tính bảo trì**        | ⭐⭐⭐  | ⭐⭐⭐⭐⭐  | +2⭐      |
| **Tính mở rộng**        | ⭐⭐⭐  | ⭐⭐⭐⭐⭐  | +2⭐      |
