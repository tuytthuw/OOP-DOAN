
📝 **Tài liệu Mô tả Sơ đồ Lớp Hoàn Chỉnh (Phiên bản Final)**

### 1\. Các Giao diện (Interfaces)

Các giao diện định nghĩa các hợp đồng chung cho các thực thể và chức năng quản lý trong hệ thống, đảm bảo tính trừu tượng.

  * **«interface» IEntity**

      * **Mô tả:** Cung cấp giao diện cơ bản cho các thực thể (ID, hiển thị, nhập liệu).
      * **Phương thức:** `+ getId(): String`, `+ display(): void` (cần ghi đè), `+ input(): void`, `+ getPrefix(): String`

  * **«interface» Sellable**

      * **Mô tả:** Đại diện cho bất kỳ mục nào có thể bán được (Sản phẩm hoặc Dịch vụ).
      * **Phương thức:** `+ getBasePrice(): BigDecimal`, `+ calculateFinalPrice(): BigDecimal`, `+ getType(): String`

  * **«interface» IDataManager**

      * [cite\_start]**Mô tả:** Cung cấp các chức năng đọc/ghi dữ liệu (theo yêu cầu Bước 4 [cite: 41]).
      * **Phương thức:** `+ readFile(): void`, `+ writeFile(): void`

  * **«interface» IActionManager\<T\>**

      * [cite\_start]**Mô tả:** Cung cấp các chức năng quản lý danh sách chung (CRUD và thống kê, theo yêu cầu Bước 2 [cite: 29-39]).
      * **Phương thức:** `+ displayList(): void`, `+ add(item: T): void`, `+ update(id: String): void`, `+ delete(id: String): boolean`, `+ findById(id: String): T`, `+ getAll(): T[]`, `+ generateStatistics(): void`

-----

### 2\. Các Lớp (Classes)

Tổng số: **15 Lớp**.

#### A. Quản lý Người dùng (Users Management)

  * **Person {abstract}** ⚠️

      * **Mô tả:** Lớp trừu tượng cơ sở cho tất cả người dùng, không thể được khởi tạo trực tiếp. Đảm bảo mọi người trong hệ thống đều có vai trò cụ thể. [cite\_start](Đáp ứng yêu cầu lớp trừu tượng [cite: 60]).
      * **Thuộc tính:** `- personId: String`, `- fullName: String`, `- phoneNumber: String`, `- isMale: boolean`, `- birthDate: LocalDate`, `- email: String`, `- address: String`, `- isDeleted: boolean`
      * **Phương thức:** `+ getRole(): String`, `+ display(): void` (Ghi đè `IEntity`).
      * **Quan hệ:** Thực thi `IEntity`.

  * **Customer**

      * **Mô tả:** Khách hàng của spa, quản lý cấp bậc thành viên và điểm tích lũy.
      * **Thuộc tính:** `- memberTier: Tier`, `- notes: String`, `- points: int`, `- lastVisitDate: LocalDate`
      * **Phương thức:** `+ earnPoints(): void`, `+ upgradeTier(): void`, `+ getDiscountRate(): double`
      * [cite\_start]**Quan hệ:** Kế thừa `Person`[cite: 56]. Kết hợp với `Tier` (Enum).

  * **Employee {abstract}**

      * **Mô tả:** Lớp trừu tượng cơ sở cho nhân viên. Định nghĩa cấu trúc lương và xác thực mật khẩu.
      * **Thuộc tính:** `- salary: double`, `- passwordHash: String`, `- hireDate: LocalDate`
      * [cite\_start]**Phương thức:** `+ calculatePay(): double {abstract}` (Hàm trừu tượng[cite: 60], buộc các lớp con định nghĩa logic tính lương), `+ checkPassword(): boolean`
      * [cite\_start]**Quan hệ:** Kế thừa `Person`[cite: 56].

  * **Technician**

      * **Mô tả:** Kỹ thuật viên thực hiện dịch vụ, tính lương dựa trên hoa hồng.
      * **Thuộc tính:** `- skill: String`, `- certifications: String`, `- commissionRate: double`
      * [cite\_start]**Phương thức:** `+ calculatePay(): double` (**Đa hình**[cite: 55]: Ghi đè: Lương cơ bản + Hoa hồng).
      * **Quan hệ:** Kế thừa `Employee`. Kết hợp với `Appointment`.

  * **Receptionist**

      * **Mô tả:** Lễ tân, chịu trách nhiệm quản lý cuộc hẹn và giao dịch thanh toán.
      * **Thuộc tính:** `+ monthlyBonus: double` (Tiền thưởng cố định hàng tháng).
      * [cite\_start]**Phương thức:** `+ calculatePay(): double` (**Đa hình**[cite: 55]: Ghi đè: Lương cơ bản + Tiền thưởng), `+ createAppointment(customer, service, time): Appointment` (Tạo cuộc hẹn).
      * **Quan hệ:** Kế thừa `Employee`. Kết hợp với `Invoice`, `Payment`.

#### B. Quản lý Nghiệp vụ (Business Management)

  * **Service**

      * **Mô tả:** Các dịch vụ được cung cấp tại spa.
      * **Thuộc tính:** `- serviceId: String`, `- serviceName: String`, `- basePrice: BigDecimal`, `- durationMinutes: int`, `- bufferTime: int`, `- description: String`, `- createdDate: LocalDate`, `- isActive: boolean`, `- category: ServiceCategory` (Phân loại dịch vụ).
      * **Phương thức:** `+ isAvailable(): boolean`, `+ calculateFinalPrice(): BigDecimal`
      * **Quan hệ:** Thực thi `IEntity`, `Sellable`. Kết hợp với `Appointment`, `ServiceCategory` (Enum).

  * **Appointment**

      * **Mô tả:** Cuộc hẹn dịch vụ giữa khách hàng, kỹ thuật viên và dịch vụ.
      * **Thuộc tính:** `- appointmentId: String`, `- customer: Customer`, `- technician: Technician`, `- service: Service`, `- startTime: LocalDateTime`, `- endTime: LocalDateTime`, `- notes: String`, `- status: AppointmentStatus`, `- rating: int`, `- feedback: String` (Đánh giá sau dịch vụ).
      * **Phương thức:** `+ schedule(): void`, `+ start(): void`, `+ cancel(): void`, `+ complete(): void`, `+ submitFeedback(rating, feedback): void`.
      * **Quan hệ:** Kết hợp với `Customer`, `Technician`, `Service`, `AppointmentStatus` (Enum).

  * **Invoice**

      * **Mô tả:** Hóa đơn chi tiết cho các giao dịch dịch vụ và sản phẩm, bao gồm tính toán thuế và phí.
      * **Thuộc tính:** `- invoiceId: String`, `- customer: Customer`, `- receptionist: Receptionist`, `- appointment: Appointment`, `- promotion: Promotion`, `- creationDate: LocalDate`, `- productList: DataStore<Product>`, `- totalAmount: double`, `- status: boolean`, `- taxRate: double`, `- serviceChargeRate: double` (Phí dịch vụ).
      * **Phương thức:** `+ addProduct(): void`, `+ calculateTotal(): double`, `+ applyTaxAndCharge(): void` (Áp dụng thuế và phí dịch vụ).
      * **Quan hệ:** Kết hợp với `Customer`, `Receptionist`, `Appointment`, `Promotion`, `DataStore<Product>`.

  * **Payment**

      * **Mô tả:** Giao dịch thanh toán cuối cùng của một hóa đơn.
      * **Thuộc tính:** `- paymentId: String`, `- invoice: Invoice`, `- amount: double`, `- paymentMethod: PaymentMethod`, `- receptionist: Receptionist`, `- paymentDate: LocalDate`
      * **Phương thức:** `+ payment()`, `+ processPayment(): boolean`
      * **Quan hệ:** Kết hợp với `Invoice`, `Receptionist`, `PaymentMethod` (Enum).

  * **Promotion**

      * **Mô tả:** Các chương trình khuyến mãi và giảm giá.
      * **Thuộc tính:** `- promotionId: String`, `- name: String`, `- description`, `- discountType: DiscountType`, `- discountValue: double`, `- startDate: LocalDate`, `- endDate: LocalDate`, `- minPurchaseAmount: double`, `- isDeleted: boolean`
      * **Phương thức:** `+ isValid(): boolean`, `+ calculateDiscount(): double`
      * **Quan hệ:** Thực thi `IEntity`. Kết hợp với `DiscountType` (Enum).

#### C. Quản lý Kho & Dữ liệu (Inventory & Data Management)

  * **Product**

      * **Mô tả:** Các sản phẩm bán kèm tại spa, quản lý tồn kho và ngày hết hạn.
      * **Thuộc tính:** `- productId: String`, `- productName: String`, `- brand: String`, `- basePrice: BigDecimal`, `- costPrice: double`, `- unit: String`, `- supplier: Supplier` (CẬP NHẬT), `- stockQuantity: int`, `- expiryDate: LocalDate`, `- isDeleted: boolean`, `- reorderLevel: int` (Ngưỡng đặt hàng lại).
      * **Phương thức:** `+ updateStock(): void`, `+ isExpired(): boolean`, `+ checkReorderStatus(): boolean`, `+ calculateFinalPrice(): BigDecimal`
      * **Quan hệ:** Thực thi `IEntity`, `Sellable`. Kết hợp với `Invoice`, `GoodsReceipt`, `Supplier`.

  * **Supplier** (MỚI)

      * **Mô tả:** Quản lý thông tin nhà cung cấp sản phẩm cho spa (Lớp thứ 15).
      * **Thuộc tính:** `- supplierId: String`, `- supplierName: String`, `- contactPerson: String`, `- phoneNumber: String`, `- address: String`, `- email: String`, `- notes: String`, `- isDeleted: boolean`
      * **Phương thức:** `+ display(): void` (Ghi đè `IEntity`), `+ input(): void`
      * **Quan hệ:** Thực thi `IEntity`.

  * **GoodsReceipt**

      * **Mô tả:** Phiếu nhập kho sản phẩm từ nhà cung cấp.
      * **Thuộc tính:** `- receiptId: String`, `- receiptDate: LocalDate`, `- employee: Employee`, `- supplier: Supplier` (CẬP NHẬT), `- receivedProducts: DataStore<Product>`, `- totalCost: double`, `- notes: String`, `- warehouseLocation: String` (Vị trí lưu trữ trong kho).
      * **Phương thức:** `+ addProduct(): void`, `+ calculateTotalCost(): double`, `+ processReceipt(): void`
      * **Quan hệ:** Kết hợp với `Employee`, `DataStore<Product>`, `Supplier`.

  * **DataStore\<T\>**

      * [cite\_start]**Mô tả:** Lớp tiện ích chung để quản lý danh sách dữ liệu bằng **mảng thô** (đáp ứng yêu cầu [cite: 57]).
      * [cite\_start]**Thuộc tính:** `- list: T[]` (Mảng thô, không dùng `ArrayList`, `Vector`... [cite: 57]).
      * **Phương thức:** `+ findByCondition(): T[]`, `+ sort(): void`, `+ count(): int`, `+ displayList(): void` (và các phương thức trong `IActionManager`, `IDataManager`).
      * **Quan hệ:** Thực thi `IActionManager`, `IDataManager`.

  * **AuthService (Singleton)**

      * **Mô tả:** Lớp quản lý xác thực người dùng, được thiết kế theo mẫu Singleton để đảm bảo chỉ có một phiên bản.
      * [cite\_start]**Thuộc tính:** `- **instance : AuthService**` (Thuộc tính **static** [cite: 59]), `- employeeList: DataStore<Employee>`, `- currentUser: Employee`
      * [cite\_start]**Phương thức:** `- AuthService()` (private constructor), `+ **getInstance(): AuthService**` (Phương thức **static** [cite: 59]), `+ login(): boolean`, `+ logout(): void`, `+ isLoggedIn(): bolean`, `+ getCurrentRole(): String`, `+ getCurrentUser: Employee`, `+ changePassword(): boolean`, `+ encryptPassword(raw: String): String`.
      * **Quan hệ:** Phụ thuộc vào `Employee`, `DataStore<Employee>`.

-----

### 3\. Các Loại Liệt kê (Enumerations)

Các kiểu liệt kê cung cấp các tập hợp giá trị cố định cho các thuộc tính liên quan.

  * **AppointmentStatus:** `SCHEDULED`, `SPENDING`, `COMPLETED`, `CANCELLED`
  * **Tier:** `STANDARD`, `SILVER`, `GOLD`
  * **PaymentMethod:** `CASH`, `CARD`, `TRANSFER`, `E_WALLET`
  * **DiscountType:** `PERCENTAGE`, `FIXED_AMOUNT`
  * **ServiceCategory:** `MASSAGE`, `FACIAL`, `BODY_TREATMENT`, `HAIR_CARE`, `NAIL_CARE`, `MAKEUP`, `COMBINATION`

-----
