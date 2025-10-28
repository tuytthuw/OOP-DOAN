# Sơ Đồ Lớp (Class Diagram) - Hệ Thống Quản Lý Spa

**Cập nhật lần cuối:** 28/10/2025

**Ghi chú:** Tài liệu này phản ánh thiết kế mới nhất, bao gồm nghiệp vụ Thanh toán, Khuyến mãi, Kho hàng và xác thực người dùng. Toàn bộ lớp quản lý dữ liệu tuân thủ quy tắc chỉ dùng mảng (`T[]`) và biến đếm.

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
| **Person** | `personId: String`<br>`fullName: String`<br>`phoneNumber: String`<br>`email: String`<br>`birthDate: LocalDate`<br>`createdAt: LocalDateTime`<br>`isDeleted: boolean` | `getAge(): int`<br>`softDelete(): void`<br>`restore(): void` | `IEntity` |
| **Employee** | `employeeCode: String`<br>`role: EmployeeRole`<br>`status: EmployeeStatus`<br>`salary: BigDecimal`<br>`passwordHash: String`<br>`hireDate: LocalDate`<br>`lastLoginAt: LocalDateTime` | `calculatePay(): BigDecimal` (abstract)<br>`checkPassword(String raw): boolean`<br>`updateRole(EmployeeRole): void` | `Person` |
| **BaseManager<T>** | `items: T[]`<br>`count: int`<br>`capacity: int` | `add(T): void`<br>`update(T): void`<br>`removeById(String): boolean`<br>`getById(String): T`<br>`getAll(): T[]`<br>`findIndexById(String): int` | - |

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

> Các getter/setter tiêu chuẩn được ngầm định, chỉ liệt kê phương thức nghiệp vụ hoặc logic tính toán.

### 4.1 Person Hierarchy

#### Customer (`extends Person`)
- **Thuộc tính:** `tier: Tier`, `totalSpent: BigDecimal`, `loyaltyPoints: int`, `lastVisitAt: LocalDateTime`, `notes: String`.
- **Phương thức:** `addSpending(BigDecimal amount)`, `redeemPoints(int value)`, `updateTier(Tier newTier)`, `markVisit(LocalDateTime visitAt)`.

#### Receptionist (`extends Employee`)
- **Thuộc tính:** `bonusPerSale: BigDecimal`, `monthlyTarget: int`.
- **Phương thức:** `calculatePay(): BigDecimal`, `recordSale(BigDecimal amount)`.

#### Technician (`extends Employee`)
- **Thuộc tính:** `skillSet: String[]`, `commissionRate: double`, `maxDailyAppointments: int`, `rating: double`.
- **Phương thức:** `calculatePay(): BigDecimal`, `isQualifiedFor(ServiceCategory category): boolean`, `updateRating(double newRating)`.

### 4.2 Bán hàng & Dịch vụ

#### Service (`implements IEntity, Sellable`)
- **Thuộc tính:** `serviceId: String`, `serviceName: String`, `description: String`, `basePrice: BigDecimal`, `durationMinutes: int`, `category: ServiceCategory`, `isActive: boolean`, `createdAt: LocalDateTime`.
- **Phương thức:** `getPrice(): BigDecimal`, `getDescription(): String`, `isAvailable(): boolean`, `updatePrice(BigDecimal newPrice)`, `toggleActive(boolean active)`.

#### Product (`implements IEntity, Sellable`)
- **Thuộc tính:** `productId: String`, `productName: String`, `description: String`, `basePrice: BigDecimal`, `stockQuantity: int`, `reorderLevel: int`, `isActive: boolean`, `lastUpdated: LocalDateTime`.
- **Phương thức:** `getPrice(): BigDecimal`, `getDescription(): String`, `isAvailable(): boolean`, `adjustStock(int delta)`, `needsRestock(): boolean`, `toggleActive(boolean active)`.

### 4.3 Lịch hẹn & Giao dịch

#### Appointment (`implements IEntity`)
- **Thuộc tính:** `appointmentId: String`, `customerId: String`, `serviceId: String`, `technicianId: String`, `startTime: LocalDateTime`, `endTime: LocalDateTime`, `status: AppointmentStatus`, `notes: String`, `createdAt: LocalDateTime`.
- **Phương thức:** `reschedule(LocalDateTime newStart, LocalDateTime newEnd)`, `assignTechnician(String technicianId)`, `updateStatus(AppointmentStatus status)`, `cancel(String reason)`.

#### InvoiceItem
- **Thuộc tính:** `sellableId: String`, `sellableType: String`, `quantity: int`, `unitPrice: BigDecimal`, `discountPerUnit: BigDecimal`.
- **Phương thức:** `calculateSubtotal(): BigDecimal`, `calculateDiscount(): BigDecimal`.

#### Invoice (`implements IEntity`)
- **Thuộc tính:** `invoiceId: String`, `customerId: String`, `appointmentId: String`, `items: InvoiceItem[]`, `discountType: DiscountType`, `discountValue: BigDecimal`, `subtotal: BigDecimal`, `totalAmount: BigDecimal`, `issuedAt: LocalDateTime`, `isPaid: boolean`.
- **Phương thức:** `addItem(InvoiceItem item)`, `removeItem(String sellableId)`, `applyPromotion(Promotion promotion)`, `calculateTotals()`, `markPaid()`.

#### Transaction (`implements IEntity`)
- **Thuộc tính:** `transactionId: String`, `invoiceId: String`, `amount: BigDecimal`, `method: PaymentMethod`, `status: TransactionStatus`, `referenceCode: String`, `processedAt: LocalDateTime`.
- **Phương thức:** `markSuccess(String referenceCode)`, `markFailed(String reason)`, `refund(BigDecimal amount)`.

#### Promotion (`implements IEntity`)
- **Thuộc tính:** `promotionId: String`, `code: String`, `description: String`, `type: DiscountType`, `value: BigDecimal`, `minOrderTotal: BigDecimal`, `usageLimit: int`, `usedCount: int`, `validFrom: LocalDate`, `validTo: LocalDate`, `isActive: boolean`.
- **Phương thức:** `isApplicable(BigDecimal subtotal, LocalDateTime at): boolean`, `incrementUsage()`, `deactivate()`.

### 4.4 Kho hàng

#### GoodsReceiptItem
- **Thuộc tính:** `productId: String`, `productName: String`, `quantity: int`, `purchasePrice: BigDecimal`.
- **Phương thức:** `calculateLineTotal(): BigDecimal`.

#### GoodsReceipt (`implements IEntity`)
- **Thuộc tính:** `receiptId: String`, `supplierName: String`, `receivedDate: LocalDate`, `items: GoodsReceiptItem[]`, `processed: boolean`, `note: String`.
- **Phương thức:** `markProcessed()`, `totalQuantity(): int`, `totalCost(): BigDecimal`.

### 4.5 Xác thực

#### AuthSession
- **Thuộc tính:** `sessionId: String`, `employeeId: String`, `loginAt: LocalDateTime`, `expiresAt: LocalDateTime`, `role: EmployeeRole`.
- **Phương thức:** `isExpired(LocalDateTime now): boolean`, `refresh(LocalDateTime newExpiry)`.

---

## 5. Manager Layer (Quản Lý Dữ Liệu)

Tất cả các lớp `*Manager` kế thừa `BaseManager<T>` và quản lý dữ liệu bằng mảng `T[]` cùng biến `count`, hoàn toàn không sử dụng collection của `java.util`.

| Manager | Mảng Dữ Liệu | Phương thức chính |
|---|---|---|
| **CustomerManager** | `Customer[]` | `add(Customer)`, `update(Customer)`, `removeById(String)`, `findByTier(Tier)`, `findByPhone(String)` |
| **EmployeeManager** | `Employee[]` | `add(Employee)`, `update(Employee)`, `removeById(String)`, `findByRole(EmployeeRole)`, `findByUsername(String)` |
| **ServiceManager** | `Service[]` | `add(Service)`, `update(Service)`, `removeById(String)`, `listByCategory(ServiceCategory)`, `searchByName(String)` |
| **ProductManager** | `Product[]` | `add(Product)`, `update(Product)`, `removeById(String)`, `findLowStock(int threshold)`, `adjustStock(String, int)` |
| **AppointmentManager** | `Appointment[]` | `add(Appointment)`, `update(Appointment)`, `removeById(String)`, `findByDate(LocalDate)`, `findByTechnician(String)` |
| **InvoiceManager** | `Invoice[]` | `add(Invoice)`, `update(Invoice)`, `removeById(String)`, `findByCustomer(String)`, `findUnpaid()` |
| **TransactionManager** | `Transaction[]` | `add(Transaction)`, `update(Transaction)`, `removeById(String)`, `findByInvoice(String)`, `findByStatus(TransactionStatus)` |
| **PromotionManager** | `Promotion[]` | `add(Promotion)`, `update(Promotion)`, `removeById(String)`, `findActive()`, `findByCode(String)` |
| **GoodsReceiptManager** | `GoodsReceipt[]` | `add(GoodsReceipt)`, `update(GoodsReceipt)`, `removeById(String)`, `findUnprocessed()`, `findByDate(LocalDate)` |

---

## 6. Business Service Layer (Lớp Nghiệp Vụ)

| Service | Trách nhiệm chính | Phương thức đề xuất | Phụ thuộc |
|---|---|---|---|
| **CustomerService** | Quản lý vòng đời khách hàng và loyalty. | `register(Customer)`, `updateProfile(Customer)`, `remove(String id)`, `getByTier(Tier)`, `calculateLifetimeValue(String id)` | `CustomerManager` |
| **EmployeeService** | Quản lý nhân sự và xác thực cơ bản. | `create(Employee)`, `update(Employee)`, `deactivate(String id)`, `changePassword(String id, String newRaw)`, `authenticate(String username, String password)` | `EmployeeManager` |
| **AppointmentService** | Điều phối lịch hẹn và trạng thái. | `schedule(Appointment)`, `reschedule(String id, LocalDateTime start, LocalDateTime end)`, `assignTechnician(String id, String technicianId)`, `cancel(String id, String reason)`, `complete(String id)` | `AppointmentManager`, `TechnicianAvailabilityService` |
| **TechnicianAvailabilityService** | Kiểm tra và đề xuất slot rảnh cho kỹ thuật viên. | `isAvailable(String technicianId, LocalDateTime start, int duration)`, `nextAvailableSlot(String technicianId, LocalDateTime from)` | `AppointmentManager` |
| **ServiceCatalog** | Cung cấp truy vấn danh mục dịch vụ. | `listAll()`, `listByCategory(ServiceCategory)`, `search(String keyword)` | `ServiceManager` |
| **InventoryService** | Quản trị kho và phiếu nhập. | `recordGoodsReceipt(GoodsReceipt receipt)`, `applyReceipt(String receiptId)`, `getLowStockProducts(int threshold)`, `reconcileStock()` | `GoodsReceiptManager`, `ProductManager` |
| **InvoiceService** | Lập hóa đơn và tính toán chi phí. | `createInvoice(String customerId, String appointmentId)`, `addSellable(String invoiceId, Sellable item, int quantity)`, `applyPromotion(String invoiceId, String code)`, `finalize(String invoiceId)` | `InvoiceManager`, `ProductManager`, `ServiceManager`, `PromotionManager` |
| **PaymentService** | Xử lý thanh toán và giao dịch. | `processPayment(String invoiceId, PaymentMethod method, BigDecimal amount)`, `recordTransaction(Transaction txn)`, `refund(String transactionId, BigDecimal amount)` | `TransactionManager` |
| **PromotionService** | Duy trì mã khuyến mãi và kiểm tra điều kiện. | `create(Promotion)`, `update(Promotion)`, `deactivate(String id)`, `validateCode(String code, BigDecimal subtotal)`, `markUsage(String code)` | `PromotionManager` |
| **AuthService** *(Singleton)* | Quản lý đăng nhập, phiên và phân quyền. | `login(String username, String password)`, `logout()`, `getCurrentSession()`, `hasRole(EmployeeRole role)`, `refreshSession()` | `EmployeeManager` |

---

## 7. Presentation & IO Layers (Lớp Trình Bày & Nhập/Xuất)

| Class | Mô Tả | Phương thức/Chức năng chính |
|---|---|---|
| **InputHandler** | Chuẩn hóa việc đọc dữ liệu từ console và kiểm tra định dạng. | `readString(prompt)`, `readInt(prompt, min, max)`, `readDecimal(prompt)`, `confirm(prompt)` |
| **OutputFormatter** | Định dạng dữ liệu ra console theo bảng và thông báo. | `printTable(String[] headers, String[][] rows)`, `printStatus(String message, boolean success)`, `printInvoice(Invoice)` |
| **FileHandler** | Đọc/ghi dữ liệu mảng của các `Manager` dưới dạng JSON. | `save(String key, IEntity[] data)`, `load(String key, IEntityFactory factory)` |
| **MainMenu** | Điều hướng chính tới các menu con. | `display()`, `handleSelection(int choice)` |
| **CustomerMenu** | Thao tác CRUD khách hàng. | `showMenu()`, `createCustomer()`, `updateCustomer()`, `deleteCustomer()` |
| **AppointmentMenu** | Quản lý lịch hẹn, phân bổ kỹ thuật viên. | `scheduleAppointment()`, `rescheduleAppointment()`, `cancelAppointment()` |
| **InventoryMenu** | Quản lý nhập kho, cập nhật tồn. | `recordGoodsReceipt()`, `viewInventory()`, `processReceipt()` |
| **BillingMenu** | Xử lý hóa đơn và thanh toán. | `createInvoiceFlow()`, `applyPromotionFlow()`, `processPaymentFlow()` |

---

## 8. Sơ Đồ Quan Hệ Chính

- **Kế thừa (Inheritance):**
  - `Customer`, `Employee` kế thừa từ `Person`.
  - `Receptionist`, `Technician` kế thừa từ `Employee`.
  - Tất cả các lớp `...Manager` kế thừa từ `BaseManager<T>`.
  - `Service`, `Product`, `Appointment`, `Transaction`, `Promotion`, `GoodsReceipt` triển khai `IEntity`. `Service` và `Product` đồng thầm triển khai `Sellable`.

- **Thực thi (Implementation):**
  - `Service`, `Product` thực thi `Sellable`.

- **Hợp thành (Composition / Aggregation):**
  - `Invoice` chứa một mảng các `InvoiceItem`.
  - `GoodsReceipt` chứa một mảng các `GoodsReceiptItem`.
  - `InventoryService` sử dụng `ProductManager` và `GoodsReceiptManager`.
  - `InvoiceService` sử dụng `ProductManager` và `ServiceManager` để lấy thông tin `Sellable`.
  - `PaymentService` tạo và cập nhật `Transaction` dựa trên `Invoice`.
  - `AuthService` duy trì `AuthSession` hiện hành.

---