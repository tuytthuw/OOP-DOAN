# Đặc Tả Chi Tiết Class Diagram Hệ Thống Quản Lý Dịch Vụ

## 📌 CẬP NHẬT (17/10/2025)

**Thay Đổi So Với Phiên Bản Cũ:**

- ✅ Thêm `Person` abstract class (base class)
- ✅ Rename `Promotion` → `Discount` (thống nhất tên)
- ✅ Rename `Payment` → `Transaction` (thống nhất tên)
- ✅ Thêm `ServiceCategory` enum
- ✅ Thêm `TransactionStatus` enum
- ✅ Thêm `EmployeeRole` enum
- ✅ Thêm `EmployeeStatus` enum
- ✅ Thêm `BaseManager<T>` generic class
- ✅ Cập nhật relationships

---

## 1. Class Person (Lớp Trừu Tượng - BASE CLASS)

**Mục đích:** Lớp cơ sở cho tất cả người trong hệ thống

**Thuộc tính:**

- `personId: String` - ID cá nhân
- `fullName: String` - Họ tên
- `phoneNumber: String` - Số điện thoại
- `isMale: boolean` - Giới tính
- `birthDate: LocalDate` - Ngày sinh
- `email: String` - Email
- `address: String` - Địa chỉ
- `isDeleted: boolean` - Trạng thái xóa

**Phương thức:**

- `getId(): String` - Lấy ID
- `getAge(): int` - Tính tuổi
- `getGenderString(): String` - Trả về "Nam" hoặc "Nữ"
- `getPersonInfo(): String` - Thông tin chi tiết
- `updateContactInfo(): void` - Cập nhật thông tin liên hệ
- `softDelete(): void` - Xóa mềm
- `restore(): void` - Khôi phục

---

## 2. Class Employee (Kế Thừa Person) - ABSTRACT

**Mục đích:** Lớp cơ sở cho tất cả nhân viên

**Thuộc tính riêng:**

- `employeeId: String` - ID nhân viên
- `role: EmployeeRole` - Vai trò
- `salary: BigDecimal` - Mức lương cơ bản
- `passwordHash: String` - Hash của mật khẩu
- `hireDate: LocalDate` - Ngày tuyển dụng
- `status: EmployeeStatus` - Trạng thái
- `department: String` - Phòng ban

**Phương thức:**

- `calculatePay(): BigDecimal` - Tính lương (abstract)
- `checkPassword(): boolean` - Kiểm tra mật khẩu
- `updatePassword(): boolean` - Cập nhật mật khẩu
- `getYearsOfService(): int` - Tính năm công tác
- `changeStatus(): void` - Thay đổi trạng thái
- `isActive(): boolean` - Kiểm tra hoạt động

---

## 3. Class Receptionist (Kế Thừa Employee)

**Mục đích:** Quản lý thông tin lễ tân

**Thuộc tính riêng:**

- `bonusPerSale: BigDecimal` - Thưởng cho mỗi giao dịch
- `totalSales: BigDecimal` - Tổng giá trị giao dịch

**Phương thức:**

- `calculatePay(): BigDecimal` - Tính lương = salary + hoa hồng
- `addSale(amount): void` - Cộng giá trị giao dịch

---

## 4. Class Technician (Kế Thừa Employee)

**Mục đích:** Quản lý thông tin kỹ thuật viên

**Thuộc tính riêng:**

- `skillSet: List<String>` - Kỹ năng
- `certifications: List<String>` - Chứng chỉ
- `commissionRate: double` - Tỷ lệ hoa hồng (%)
- `totalCommission: BigDecimal` - Tổng hoa hồng tích lũy
- `performanceRating: double` - Đánh giá hiệu suất (0-5)

**Phương thức:**

- `calculatePay(): BigDecimal` - Tính lương = salary + commission
- `addCommission(amount): void` - Cộng hoa hồng
- `addSkill(skill): void` - Thêm kỹ năng
- `addCertification(cert): void` - Thêm chứng chỉ
- `hasSkill(skill): boolean` - Kiểm tra có kỹ năng
- `hasCertification(cert): boolean` - Kiểm tra có chứng chỉ
- `updatePerformanceRating(rating): void` - Cập nhật đánh giá

---

## 5. Class Customer (Kế Thừa Person)

**Mục đích:** Quản lý thông tin khách hàng

**Thuộc tính riêng:**

- `customerId: String` - ID khách hàng
- `memberTier: Tier` - Hạng thành viên (STANDARD, SILVER, GOLD, PLATINUM)
- `totalSpent: BigDecimal` - Tổng tiền đã chi tiêu
- `lastVisitDate: LocalDate` - Ngày ghé thăm cuối cùng
- `notes: String` - Ghi chú
- `isActive: boolean` - Có hoạt động không

**Phương thức:**

- `earnPoints(): void` - Tích lũy điểm
- `upgradeToTier(): void` - Nâng cấp hạng
- `getDiscountRate(): double` - Lấy tỷ lệ giảm giá
- `updateTier(): void` - Cập nhật tier dựa trên totalSpent
- `addToTotalSpent(amount): void` - Cộng chi tiêu
- `updateLastVisit(): void` - Cập nhật ngày ghé thăm

---

## 6. Class Service

**Mục đích:** Quản lý dịch vụ

**Thuộc tính:**

- `serviceId: String` - ID dịch vụ
- `serviceName: String` - Tên dịch vụ
- `description: String` - Mô tả
- `category: ServiceCategory` - Loại dịch vụ
- `basePrice: BigDecimal` - Giá cơ bản
- `durationMinutes: int` - Thời gian (phút)
- `isActive: boolean` - Dịch vụ còn cung cấp không
- `createdDate: LocalDate` - Ngày thêm vào

**Phương thức:**

- `getServiceInfo(): String` - Thông tin chi tiết
- `getPriceFormatted(): String` - Giá định dạng
- `getDurationFormatted(): String` - Thời gian định dạng

---

## 7. Enumeration ServiceCategory

**Mục đích:** Phân loại dịch vụ

**Giá trị:**

- `MASSAGE` - Xoa bóp, massage
- `FACIAL` - Chăm sóc mặt
- `BODY_TREATMENT` - Chăm sóc cơ thể
- `HAIR_CARE` - Chăm sóc tóc
- `NAIL_CARE` - Chăm sóc móng
- `MAKEUP` - Trang điểm
- `COMBINATION` - Gói dịch vụ kết hợp

---

## 8. Class Appointment

**Mục đích:** Quản lý cuộc hẹn dịch vụ

**Thuộc tính:**

- `appointmentId: String` - ID cuộc hẹn
- `customerId: String` - ID khách hàng
- `serviceId: String` - ID dịch vụ
- `appointmentDateTime: LocalDateTime` - Ngày giờ hẹn
- `status: AppointmentStatus` - Trạng thái
- `staffId: String` - ID nhân viên thực hiện
- `notes: String` - Ghi chú bổ sung
- `createdDate: LocalDateTime` - Ngày tạo
- `completedDate: LocalDateTime` - Ngày hoàn thành

**Phương thức:**

- `schedule(): void` - Lên lịch hẹn
- `start(): void` - Bắt đầu
- `cancel(): void` - Hủy
- `complete(): void` - Hoàn thành
- `updateStatus(): void` - Cập nhật trạng thái
- `assignStaff(staffId): void` - Gán nhân viên
- `isExpired(): boolean` - Kiểm tra hết hạn

---

## 9. Enumeration AppointmentStatus

**Mục đích:** Định nghĩa các trạng thái cuộc hẹn

**Giá trị:**

- `SCHEDULED` - Đã lên lịch
- `SPENDING` - Đang thực hiện
- `COMPLETED` - Đã hoàn thành
- `CANCELLED` - Đã hủy

---

## 10. Class Discount

**Mục đích:** Quản lý chương trình khuyến mãi/chiết khấu

**Thuộc tính:**

- `discountId: String` - ID chiết khấu
- `discountCode: String` - Mã khuyến mãi
- `description: String` - Mô tả
- `discountType: DiscountType` - Loại giảm giá
- `discountValue: double` - Giá trị giảm
- `startDate: LocalDate` - Ngày bắt đầu
- `endDate: LocalDate` - Ngày kết thúc
- `minPurchaseAmount: BigDecimal` - Tối thiểu mua
- `maxDiscount: BigDecimal` - Chiết khấu tối đa
- `usageLimit: int` - Giới hạn sử dụng
- `usageCount: int` - Lần đã sử dụng
- `isActive: boolean` - Có hoạt động không

**Phương thức:**

- `isValid(): boolean` - Kiểm tra hợp lệ
- `calculateDiscount(amount): BigDecimal` - Tính giảm giá
- `canUse(): boolean` - Có thể sử dụng không
- `incrementUsage(): void` - Tăng lượt sử dụng
- `isValidForDate(date): boolean` - Hợp lệ cho ngày nào

---

## 11. Enumeration DiscountType

**Mục đích:** Định nghĩa loại giảm giá

**Giá trị:**

- `PERCENTAGE` - Giảm theo phần trăm
- `FIXED_AMOUNT` - Giảm theo số tiền cố định

---

## 12. Class Transaction

**Mục đích:** Quản lý giao dịch thanh toán

**Thuộc tính:**

- `transactionId: String` - ID giao dịch
- `appointmentId: String` - ID lịch hẹn
- `customerId: String` - ID khách hàng
- `amount: BigDecimal` - Số tiền thanh toán
- `paymentMethod: PaymentMethod` - Phương thức thanh toán
- `transactionDate: LocalDateTime` - Ngày giờ giao dịch
- `status: TransactionStatus` - Trạng thái
- `referenceCode: String` - Mã tham chiếu
- `notes: String` - Ghi chú
- `refundedAmount: BigDecimal` - Số tiền hoàn lại

**Phương thức:**

- `processPayment(): void` - Xử lý thanh toán
- `failPayment(reason): void` - Thanh toán thất bại
- `refundPayment(amount): void` - Hoàn lại tiền
- `getTransactionInfo(): String` - Thông tin chi tiết
- `isSuccessful(): boolean` - Giao dịch thành công

---

## 13. Enumeration TransactionStatus

**Mục đích:** Định nghĩa các trạng thái giao dịch

**Giá trị:**

- `PENDING` - Đang chờ xử lý
- `SUCCESS` - Thanh toán thành công
- `FAILED` - Thanh toán thất bại
- `REFUNDED` - Đã hoàn tiền

---

## 14. Enumeration PaymentMethod

**Mục đích:** Định nghĩa phương thức thanh toán

**Giá trị:**

- `CASH` - Tiền mặt
- `CARD` - Thẻ
- `TRANSFER` - Chuyển khoản
- `E_WALLET` - Ví điện tử

---

## 15. Class Invoice

**Mục đích:** Quản lý hóa đơn

**Thuộc tính:**

- `invoiceId: String` - ID hóa đơn
- `appointmentId: String` - ID lịch hẹn
- `customerId: String` - ID khách hàng
- `issueDate: LocalDate` - Ngày phát hành
- `subtotal: BigDecimal` - Tổng tiền trước chiết khấu
- `discountAmount: BigDecimal` - Số tiền chiết khấu
- `taxAmount: BigDecimal` - Số tiền thuế
- `totalAmount: BigDecimal` - Tổng tiền cuối cùng
- `isPaid: boolean` - Đã thanh toán hay chưa
- `paidDate: LocalDate` - Ngày thanh toán
- `paymentMethod: PaymentMethod` - Phương thức thanh toán
- `discountCode: String` - Mã chiết khấu

**Phương thức:**

- `addProduct(): void` - Thêm sản phẩm (nếu cần)
- `applyDiscount(discountAmount, code): void` - Áp dụng chiết khấu
- `calculateTax(taxRate): void` - Tính thuế
- `calculateTotal(): void` - Tính tổng tiền
- `markAsPaid(paidDate): void` - Đánh dấu đã thanh toán
- `getInvoiceInfo(): String` - Thông tin chi tiết
- `getFormattedInvoice(): String` - Hóa đơn định dạng

---

## 16. Class GoodsReceipt

**Mục đích:** Quản lý phiếu nhập kho

**Thuộc tính:**

- `receiptId: String` - ID phiếu
- `receiptDate: LocalDate` - Ngày nhập
- `employee: Employee` - Nhân viên
- `supplierName: String` - Tên nhà cung cấp
- `receivedProducts: DataStore<Product>` - Sản phẩm nhận
- `totalCost: double` - Tổng chi phí
- `notes: String` - Ghi chú

**Phương thức:**

- `addProduct(): void` - Thêm sản phẩm
- `calculateTotal(): double` - Tính tổng
- `processReceipt(): void` - Xử lý phiếu

---

## 17. Class Product

**Mục đích:** Quản lý sản phẩm

**Thuộc tính:**

- `productId: String` - ID sản phẩm
- `productName: String` - Tên sản phẩm
- `brand: String` - Nhãn hiệu
- `basePrice: BigDecimal` - Giá cơ bản
- `costPrice: double` - Giá chi phí
- `unit: String` - Đơn vị
- `supplier: String` - Nhà cung cấp
- `stockQuantity: int` - Số lượng tồn kho
- `expiryDate: LocalDate` - Ngày hết hạn
- `isDeleted: boolean` - Trạng thái xóa

**Phương thức:**

- `updateStock(): void` - Cập nhật tồn kho
- `isExpired(): boolean` - Kiểm tra hết hạn

---

## 18. Interface Sellable

**Mục đích:** Giao diện cho các item có thể bán

**Phương thức:**

- `getId(): String` - Lấy ID
- `display(): void` - Hiển thị
- `input(): void` - Nhập
- `getPrice(): String` - Lấy giá

---

## 19. Class BaseManager<T> (GENERIC)

**Mục đích:** Lớp cơ sở quản lý dữ liệu generic

**Thuộc tính:**

- `items: List<T>` - Danh sách lưu trữ

**Phương thức:**

- `add(item: T): boolean` - Thêm item
- `update(item: T): boolean` - Cập nhật item
- `delete(id: String): boolean` - Xóa item
- `getById(id: String): T` - Tìm theo ID
- `getAll(): List<T>` - Lấy tất cả
- `size(): int` - Đếm số lượng
- `clear(): void` - Xóa tất cả
- `exists(id: String): boolean` - Kiểm tra tồn tại
- `getId(item: T): String` (abstract) - Lấy ID từ item
- `validateItem(item: T): boolean` (abstract) - Validate item
- `getAllActive(): List<T>` - Lấy tất cả hoạt động (nếu có isDeleted)
- `findIndex(id: String): int` (protected) - Tìm index
- `sort(comparator): void` (protected) - Sắp xếp
- `filter(predicate): List<T>` (protected) - Lọc

---

## 20. Interface IActionManager

**Mục đích:** Giao diện quản lý các hành động CRUD

**Phương thức:**

- `display(): void` - Hiển thị
- `addItem(): void` - Thêm item
- `updateItem(): void` - Cập nhật item
- `deleteItem(): boolean` - Xóa item
- `findByIdItem(): T` - Tìm item theo ID
- `getAll(): T[]` - Lấy tất cả
- `generateStatistics(): void` - Tạo thống kê

---

## 21. Interface IDataManager

**Mục đích:** Giao diện quản lý dữ liệu

**Phương thức:**

- `readFile(): void` - Đọc file
- `writeFile(): void` - Ghi file

---

## 22. Interface IEntity

**Mục đích:** Giao diện chung cho các entity

**Phương thức:**

- `getId(): String` - Lấy ID
- `display(): void` - Hiển thị
- `input(): void` - Nhập
- `getPrice(): String` - Lấy giá

---

## 23. Enumeration EmployeeRole

**Mục đích:** Định nghĩa vai trò nhân viên

**Giá trị:**

- `RECEPTIONIST` - Lễ tân
- `TECHNICIAN` - Kỹ thuật viên
- `MANAGER` - Quản lý
- `ADMIN` - Quản trị viên

---

## 24. Enumeration EmployeeStatus

**Mục đích:** Định nghĩa trạng thái nhân viên

**Giá trị:**

- `ACTIVE` - Hoạt động
- `ON_LEAVE` - Đang nghỉ phép
- `SUSPENDED` - Tạm dừng
- `RESIGNED` - Đã từ chức

---

## 25. Enumeration Tier

**Mục đích:** Định nghĩa hạng thành viên

**Giá trị:**

- `BRONZE` - Hạng đồng
- `SILVER` - Hạng bạc
- `GOLD` - Hạng vàng
- `PLATINUM` - Hạng bạch kim

---

## Quan Hệ Giữa Các Class

### Kế Thừa:

```
Person (Abstract)
├── Customer
└── Employee (Abstract)
    ├── Receptionist
    └── Technician
```

### Liên Kết:

1. **AuthService quản lý Employee**
2. **Appointment liên kết:**
   - Customer (khách hàng)
   - Service (dịch vụ)
   - Employee/Technician (nhân viên thực hiện)
3. **Invoice liên kết:**
   - Customer (khách hàng)
   - Appointment (lịch hẹn)
   - Discount (chiết khấu)
   - Receptionist (lễ tân)
4. **Transaction liên kết:**
   - Invoice (hóa đơn)
   - Customer (khách hàng)
   - PaymentMethod
5. **GoodsReceipt liên kết:**
   - Employee (nhân viên)
   - Product (sản phẩm)

### Implement Interface:

- Service, Product implement Sellable
- Appointment, GoodsReceipt implement IActionManager

### Generic Manager:

```
BaseManager<T>
├── CustomerManager extends BaseManager<Customer>
├── ServiceManager extends BaseManager<Service>
├── AppointmentManager extends BaseManager<Appointment>
├── TransactionManager extends BaseManager<Transaction>
├── DiscountManager extends BaseManager<Discount>
├── InvoiceManager extends BaseManager<Invoice>
└── EmployeeManager extends BaseManager<Employee>
```
