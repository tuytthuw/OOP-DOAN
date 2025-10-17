# Danh Sách Kế Hoạch Kỹ Thuật - Dự Án Quản Lý Spa

## Tổng Quan

Dự án được chia thành **10 kế hoạch chính**, bao gồm model, manager, services, IO và UI.

---

## Danh Sách Kế Hoạch

### Phase 0: Chuẩn Bị & Base Classes (0000, 0007a)

| #         | Tên Kế Hoạch                                    | Mô Tả                                                | Tệp Tạo Mới                    |
| --------- | ----------------------------------------------- | ---------------------------------------------------- | ------------------------------ |
| **0000**  | [Xây Dựng Person Base Class](./0000_PLAN.md)    | Lớp abstract cơ sở cho Customer, Employee            | `models/Person.java`           |
| **0007a** | [Xây Dựng BaseManager Generic](./0007a_PLAN.md) | Lớp generic cơ sở cho tất cả Manager, tránh lặp code | `collections/BaseManager.java` |

---

### Phase 1: Xây Dựng Model (0001 - 0006)

| #         | Tên Kế Hoạch                                   | Mô Tả                                                     | Tệp Tạo Mới                                                 |
| --------- | ---------------------------------------------- | --------------------------------------------------------- | ----------------------------------------------------------- |
| **0001**  | [Xây Dựng Model Khách Hàng](./0001_PLAN.md)    | Lớp Customer kế thừa Person, Tier, chi tiêu               | `models/Customer.java`                                      |
| **0002**  | [Xây Dựng Model Dịch Vụ](./0002_PLAN.md)       | Lớp Service với giá, danh mục, thời gian                  | `models/Service.java`<br>`enums/ServiceCategory.java`       |
| **0002b** | [Xây Dựng Interface Sellable](./0002b_PLAN.md) | Interface cho các item có thể bán (Service, Product)      | `Interfaces/Sellable.java`                                  |
| **0003**  | [Xây Dựng Model Lịch Hẹn](./0003_PLAN.md)      | Lớp Appointment với trạng thái, liên kết Customer/Service | `models/Appointment.java`                                   |
| **0004**  | [Xây Dựng Model Chiết Khấu](./0004_PLAN.md)    | Lớp Discount với kiểu %, tiền cố định, điều kiện          | `models/Discount.java`                                      |
| **0005**  | [Xây Dựng Model Giao Dịch](./0005_PLAN.md)     | Lớp Transaction với phương thức thanh toán, trạng thái    | `models/Transaction.java`<br>`enums/TransactionStatus.java` |
| **0006**  | [Xây Dựng Model Hóa Đơn](./0006_PLAN.md)       | Lớp Invoice tính toán tổng tiền, chiết khấu, thuế         | `models/Invoice.java`                                       |

---

### Phase 2: Xây Dựng Manager (0007) & Nhân Viên (0011)

| #        | Tên Kế Hoạch                                  | Mô Tả                                                     | Tệp Tạo Mới                                                                                                                                                                                                                          |
| -------- | --------------------------------------------- | --------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **0007** | [Xây Dựng Collection Manager](./0007_PLAN.md) | Quản lý tập hợp dữ liệu extends BaseManager               | `collections/CustomerManager.java`<br>`collections/ServiceManager.java`<br>`collections/AppointmentManager.java`<br>`collections/TransactionManager.java`<br>`collections/DiscountManager.java`<br>`collections/InvoiceManager.java` |
| **0011** | [Xây Dựng Employee/Staff](./0011_PLAN.md)     | Lớp Employee, Receptionist, Technician, quản lý nhân viên | `models/Employee.java`<br>`models/Receptionist.java`<br>`models/Technician.java`<br>`enums/EmployeeRole.java`<br>`enums/EmployeeStatus.java`<br>`collections/EmployeeManager.java`                                                   |

---

### Phase 3: Xây Dựng Business Logic (0008)

| #        | Tên Kế Hoạch                                       | Mô Tả                         | Tệp Tạo Mới                                                                                                                                                                |
| -------- | -------------------------------------------------- | ----------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **0008** | [Xây Dựng Business Logic Services](./0008_PLAN.md) | Logic xử lý nghiệp vụ cao cấp | `services/CustomerService.java`<br>`services/AppointmentService.java`<br>`services/InvoiceService.java`<br>`services/PaymentService.java`<br>`services/ReportService.java` |

---

### Phase 4: Exception Handling (0012)

| #        | Tên Kế Hoạch                                             | Mô Tả                 | Tệp Tạo Mới                                                                                                                                                                                                                                         |
| -------- | -------------------------------------------------------- | --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **0012** | [Exception Handling & Custom Exceptions](./0012_PLAN.md) | Xử lý lỗi có hệ thống | `exceptions/BaseException.java`<br>`exceptions/EntityNotFoundException.java`<br>`exceptions/InvalidEntityException.java`<br>`exceptions/BusinessLogicException.java`<br>`exceptions/PaymentException.java`<br>`exceptions/ValidationException.java` |

---

### Phase 5: Xây Dựng IO & UI (0009 - 0010)

| #        | Tên Kế Hoạch                                    | Mô Tả                      | Tệp Tạo Mới                                                                                                                |
| -------- | ----------------------------------------------- | -------------------------- | -------------------------------------------------------------------------------------------------------------------------- |
| **0009** | [Xây Dựng Input/Output Manager](./0009_PLAN.md) | Xử lý input/output console | `io/InputHandler.java`<br>`io/OutputFormatter.java`<br>`io/FileHandler.java` (tùy chọn)                                    |
| **0010** | [Xây Dựng Giao Diện Menu](./0010_PLAN.md)       | Menu console tương tác     | `ui/MainMenu.java`<br>`ui/CustomerMenu.java`<br>`ui/AppointmentMenu.java`<br>`ui/PaymentMenu.java`<br>`ui/ReportMenu.java` |

---

## Sơ Đồ Quan Hệ Giữa Các Phần

```
┌─────────────────────────────────────────────────────────────┐
│                      UI Layer (0010)                        │
│  MainMenu → CustomerMenu, AppointmentMenu, PaymentMenu...  │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                  IO Layer (0009)                            │
│  InputHandler, OutputFormatter, FileHandler                │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│         Exception Handling Layer (0012)                     │
│  BaseException, BusinessLogicException, ValidationEx...    │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│            Business Logic Layer (0008)                      │
│  CustomerService, AppointmentService, InvoiceService...    │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│             Collection Manager Layer (0007)                 │
│  CustomerMgr extends BaseManager<Customer>                 │
│  ServiceMgr extends BaseManager<Service>                   │
│  AppointmentMgr, TransactionMgr, DiscountMgr, etc...      │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│            BaseManager<T> Generic Layer (0007a)             │
│  add, update, delete, getById, getAll, filter, sort...     │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│               Model Layer (0000-0006, 0011)                 │
│  Person, Customer, Employee, Technician, Receptionist      │
│  Service, Appointment, Discount, Transaction, Invoice      │
│  + All Enums (AppointmentStatus, ServiceCategory, etc)     │
└─────────────────────────────────────────────────────────────┘
```

---

## Quy Trình Phát Triển Đề Nghị

### ✅ Bước 1: Chuẩn Bị Cơ Sở (0000, 0007a)

- Tạo Person base class (0000)
- Tạo BaseManager generic class (0007a)
- Giảm lặp code, cải thiện kiến trúc

### ✅ Bước 2: Xây Dựng Model Cơ Bản (0001-0006)

- Tạo các lớp model với các thuộc tính và getter/setter
- Customer kế thừa từ Person
- Thêm các enum cần thiết
- Implement phương thức `equals()`, `hashCode()`, `toString()`

### ✅ Bước 3: Xây Dựng Nhân Viên (0011)

- Tạo Employee, Receptionist, Technician classes
- Quản lý lương, kỹ năng, hóa hồng
- Implement EmployeeManager

### ✅ Bước 4: Xây Dựng Exception Handling (0012)

- Tạo custom exception classes
- Chuẩn bị cho error handling trong Manager/Service

### ✅ Bước 5: Xây Dựng Manager (0007)

- Tạo các lớp manager extends BaseManager<T>
- Implement các phương thức CRUD cơ bản
- Thêm các phương thức tìm kiếm cụ thể

### ✅ Bước 6: Xây Dựng Business Logic (0008)

- Tạo các service xử lý logic phức tạp
- Kết nối các model với manager
- Implement các tính toán như cập nhật tier, tính hóa đơn

### ✅ Bước 7: Xây Dựng Input/Output (0009)

- Tạo InputHandler với validation
- Tạo OutputFormatter để hiển thị dữ liệu
- Tạo FileHandler (tùy chọn) cho lưu trữ

### ✅ Bước 8: Xây Dựng Giao Diện Menu (0010)

- Tạo menu console tương tác
- Kết nối tất cả Service
- Implement error handling từ exceptions

### Bước 4: Xây Dựng IO Handler (0009)

- Tạo InputHandler và OutputFormatter
- Test các phương thức input/output
- Tùy chọn: thêm FileHandler để lưu dữ liệu

### Bước 5: Xây Dựng UI Menu (0010)

- Tạo các menu cho từng chức năng
- Kết nối menu với services
- Test toàn bộ luồng từ input đến output

### Bước 6: Tích Hợp & Testing

- Khởi tạo dữ liệu sample
- Test tất cả các chức năng
- Sửa bug và tối ưu

---

## Các Enum Sẽ Được Tạo

| Enum                  | Loại Dữ Liệu           | Giá Trị                                                                    |
| --------------------- | ---------------------- | -------------------------------------------------------------------------- |
| **Tier**              | Loại membership        | PLATINUM, GOLD, SILVER, BRONZE                                             |
| **AppointmentStatus** | Trạng thái lịch hẹn    | SCHEDULED, SPENDING, COMPLETED, CANCELLED                                  |
| **ServiceCategory**   | Danh mục dịch vụ       | MASSAGE, FACIAL, BODY_TREATMENT, HAIR_CARE, NAIL_CARE, MAKEUP, COMBINATION |
| **DiscountType**      | Loại chiết khấu        | PERCENTAGE, FIXED_AMOUNT                                                   |
| **PaymentMethod**     | Phương thức thanh toán | CASH, CARD, TRANSFER, E_WALLET                                             |
| **TransactionStatus** | Trạng thái giao dịch   | PENDING, SUCCESS, FAILED, REFUNDED                                         |

---

## Công Nghệ & Quy Chuẩn

- **Ngôn Ngữ:** Java 23
- **Kiến Trúc:** Layered Architecture
- **Giao Diện:** Console-based
- **Quy Chuẩn Code:** Google Java Style Guide
- **Comment:** Tiếng Việt, ngắn gọn, rõ ràng
- **Xử Lý Ngoại Lệ:** Try-catch rõ ràng
- **Kiểu Dữ Liệu:** BigDecimal cho tiền, LocalDate/LocalDateTime cho thời gian

---

**Ngày Cập Nhật:** October 2025  
**Phiên Bản:** 1.0  
**Trạng Thái:** Hoàn thành kế hoạch
