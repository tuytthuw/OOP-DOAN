# Sơ Đồ Lớp (Class Diagram) - Hệ Thống Quản Lý Spa

**Cập nhật lần cuối:** 18/10/2025

**Ghi chú:** Tài liệu này được cập nhật để bao gồm các chức năng quản lý Sản phẩm và Kho hàng, phản ánh chính xác các kế hoạch kỹ thuật từ #0000 đến #0016.

---

## 1. Interfaces (Giao Diện)

| Interface | Phương Thức | Mô Tả |
|---|---|---|
| **IEntity** | `getId(): String` | Giao diện cơ sở cho tất cả các đối tượng có ID trong hệ thống. |
| **Sellable** | `getPrice(): BigDecimal`<br>`getDescription(): String`<br>`isAvailable(): boolean` | Giao diện cho các đối tượng có thể bán được (`Service`, `Product`). |

---

## 2. Abstract Classes (Lớp Trừu Tượng)

| Class | Thuộc Tính | Phương Thức | Kế Thừa |
|---|---|---|---|
| **Person** | `personId: String`<br>`fullName: String`<br>`phoneNumber: String`<br>... | `getAge(): int`<br>`softDelete(): void` | `IEntity` |
| **Employee** | `role: EmployeeRole`<br>`salary: BigDecimal`<br>`passwordHash: String` | `calculatePay(): BigDecimal` (abstract)<br>`checkPassword(String): boolean` | `Person` |
| **BaseManager<T>** | `items: T[]`<br>`size: int` | `add(T): void`<br>`getById(String): T`<br>`getAll(): T[]` | - |

---

## 3. Enums (Kiểu Liệt Kê)

| Enum | Giá Trị |
|---|---|
| **Tier** | `PLATINUM`, `GOLD`, `SILVER`, `BRONZE` |
| **ServiceCategory** | `MASSAGE`, `FACIAL`, `BODY_TREATMENT`, ... |
| **AppointmentStatus** | `SCHEDULED`, `COMPLETED`, `CANCELLED`, ... |
| **DiscountType** | `PERCENTAGE`, `FIXED_AMOUNT` |
| **PaymentMethod** | `CASH`, `CARD`, `TRANSFER`, `E_WALLET` |
| **TransactionStatus** | `PENDING`, `SUCCESS`, `FAILED`, `REFUNDED` |
| **EmployeeRole** | `RECEPTIONIST`, `TECHNICIAN`, `MANAGER`, `ADMIN` |
| **EmployeeStatus** | `ACTIVE`, `ON_LEAVE`, `RESIGNED`, ... |

---

## 4. Model Classes (Lớp Mô Hình Dữ Liệu)

| Class | Thuộc Tính Chính | Kế Thừa / Implement |
|---|---|---|
| **Customer** | `tier: Tier`<br>`totalSpent: BigDecimal` | `Person` |
| **Receptionist** | `bonusPerSale: BigDecimal` | `Employee` |
| **Technician** | `skillSet: String[]`<br>`commissionRate: double` | `Employee` |
| **Service** | `serviceName: String`<br>`basePrice: BigDecimal`<br>`durationMinutes: int` | `IEntity`, `Sellable` |
| **Product** | `productName: String`<br>`basePrice: BigDecimal`<br>`stockQuantity: int` | `IEntity`, `Sellable` |
| **Appointment** | `customerId: String`<br>`serviceId: String`<br>`appointmentDateTime: LocalDateTime` | `IEntity` |
| **InvoiceItem** | `sellableId: String`<br>`quantity: int`<br>`unitPrice: BigDecimal` | - |
| **Invoice** | `customerId: String`<br>`items: InvoiceItem[]`<br>`totalAmount: BigDecimal` | `IEntity` |
| **Transaction** | `invoiceId: String`<br>`amount: BigDecimal`<br>`status: TransactionStatus` | `IEntity` |
| **GoodsReceiptItem**| `productId: String`<br>`quantity: int`<br>`purchasePrice: BigDecimal` | - |
| **GoodsReceipt** | `supplierName: String`<br>`items: GoodsReceiptItem[]`<br>`isProcessed: boolean` | `IEntity` |

---

## 5. Data & Business Layers (Lớp Dữ Liệu & Nghiệp Vụ)

| Class | Mô Tả | Phụ Thuộc Chính |
|---|---|---|
| **ProductManager** | Quản lý danh sách `Product` bằng mảng `Product[]`. | `BaseManager<Product>` |
| **GoodsReceiptManager**| Quản lý danh sách `GoodsReceipt` bằng mảng `GoodsReceipt[]`. | `BaseManager<GoodsReceipt>` |
| *(...các Manager khác)* | Tương tự cho `Customer`, `Service`, `Appointment`, v.v. | `BaseManager<T>` |
| **InventoryService** | Chứa logic nghiệp vụ cho kho hàng (xử lý phiếu nhập). | `GoodsReceiptManager`, `ProductManager` |
| **InvoiceService** | Logic tạo hóa đơn, thêm sản phẩm/dịch vụ vào hóa đơn. | `InvoiceManager`, `ProductManager`, `ServiceManager` |
| *(...các Service khác)* | Tương tự cho `CustomerService`, `AppointmentService`, v.v. | Các `Manager` tương ứng |
| **AuthService** | (Singleton) Quản lý đăng nhập, đăng xuất, và phân quyền. | `EmployeeManager` |

---

## 6. Presentation & IO Layers (Lớp Trình Bày & Nhập/Xuất)

| Class | Mô Tả |
|---|---|
| **InputHandler** | Xử lý việc đọc và xác thực dữ liệu nhập từ console. |
| **OutputFormatter** | Định dạng và hiển thị dữ liệu ra console. |
| **FileHandler** | Xử lý việc đọc và ghi dữ liệu của các Manager vào file JSON. |
| **MainMenu** | Lớp menu chính, điều hướng đến các menu con. |
| **InventoryMenu** | (Mới) Giao diện cho các chức năng quản lý kho và nhập hàng. |

---

## 7. Sơ Đồ Quan Hệ Chính

- **Kế thừa (Inheritance):**
  - `Customer`, `Employee` kế thừa từ `Person`.
  - `Receptionist`, `Technician` kế thừa từ `Employee`.
  - Tất cả các lớp `...Manager` kế thừa từ `BaseManager<T>`.

- **Thực thi (Implementation):**
  - `Service`, `Product` thực thi `Sellable`.

- **Hợp thành (Composition / Aggregation):**
  - `Invoice` chứa một mảng các `InvoiceItem`.
  - `GoodsReceipt` chứa một mảng các `GoodsReceiptItem`.
  - `InventoryService` sử dụng `ProductManager` và `GoodsReceiptManager`.
  - `InvoiceService` sử dụng `ProductManager` và `ServiceManager` để lấy thông tin `Sellable`.
