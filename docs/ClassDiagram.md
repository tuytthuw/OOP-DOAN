# Đặc Tả Chi Tiết Class Diagram Hệ Thống Quản Lý Dịch Vụ

## 📌 CẬP NHẬT (17/10/2025)

**Thay Đổi:**

- ✅ Thêm `Person` abstract class (base class)
- ✅ Rename `Promotion` → `Discount`
- ✅ Rename `Payment` → `Transaction`
- ✅ Thêm `ServiceCategory` enum
- ✅ Thêm `TransactionStatus` enum
- ✅ Thêm `BaseManager<T>` generic class
- ✅ Cập nhật relationships

---

## 1. Class Person (Lớp Trừu Tượng - BASE CLASS)

**Mục đích:** Quản lý xác thực và phân quyền người dùng

**Thuộc tính:**

- `instance: AuthService` - Instance duy nhất của lớp
- `employeeList: DataStore<Employee>` - Danh sách nhân viên
- `currentUser: Employee` - Người dùng hiện tại

**Phương thức:**

- `AuthService()` - Constructor
- `getInstance(): AuthService` - Lấy instance duy nhất
- `login(): boolean` - Đăng nhập
- `logout(): void` - Đăng xuất
- `isLoggedIn(): boolean` - Kiểm tra đã đăng nhập
- `getCurrentRole(): String` - Lấy vai trò hiện tại
- `getCurrentUser(): Employee` - Lấy thông tin người dùng hiện tại
- `changePassword(): boolean` - Thay đổi mật khẩu
- `getUsersByRole(): DataStore<Employee>` - Lấy danh sách người dùng theo vai trò

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

## 2. Class AuthService

**Mục đích:** Quản lý xác thực và phân quyền người dùng

**Thuộc tính:**

- `instance: AuthService` - Instance duy nhất của lớp
- `employeeList: DataStore<Employee>` - Danh sách nhân viên
- `currentUser: Employee` - Người dùng hiện tại

**Phương thức:**

- `AuthService()` - Constructor
- `getInstance(): AuthService` - Lấy instance duy nhất
- `login(): boolean` - Đăng nhập
- `logout(): void` - Đăng xuất
- `isLoggedIn(): boolean` - Kiểm tra đã đăng nhập
- `getCurrentRole(): String` - Lấy vai trò hiện tại
- `getCurrentUser(): Employee` - Lấy thông tin người dùng hiện tại
- `changePassword(): boolean` - Thay đổi mật khẩu
- `getUsersByRole(): DataStore<Employee>` - Lấy danh sách người dùng theo vai trò

---

## 3. Class Employee (Kế Thừa Person) - ABSTRACT

---

## 3. Class Employee (Kế Thừa Person)

**Mục đích:** Quản lý thông tin nhân viên

**Thuộc tính riêng:**

- `salary: double` - Mức lương
- `passwordHash: String` - Hash của mật khẩu
- `hireDate: LocalDate` - Ngày tuyển dụng

**Phương thức:**

- `calculatePay(): double` - Tính lương
- `checkPassword(): boolean` - Kiểm tra mật khẩu

---

## 4. Class Receptionist (Kế Thừa Employee)

**Mục đích:** Quản lý lễ tân

**Phương thức:**

- `calculatePay(): double` - Tính lương cho lễ tân

---

## 5. Class Technician (Kế Thừa Employee)

**Mục đích:** Quản lý kỹ thuật viên

**Thuộc tính riêng:**

- `skill: String` - Kỹ năng
- `certifications: String` - Chứng chỉ
- `commissionRate: double` - Tỷ lệ hoa hồng

**Phương thức:**

- `calculatePay(): double` - Tính lương kỹ thuật viên

---

## 6. Class Customer (Kế Thừa Person)

**Mục đích:** Quản lý thông tin khách hàng

**Thuộc tính riêng:**

- `memberTier: Tier` - Hạng thành viên (STANDARD, SILVER, GOLD)
- `notes: String` - Ghi chú
- `points: int` - Điểm tích lũy
- `lastVisitDate: LocalDate` - Ngày ghé thăm cuối cùng

**Phương thức:**

- `earnPoints(): void` - Tích lũy điểm
- `upgradeToTier(): void` - Nâng cấp hạng
- `getDiscountRate(): double` - Lấy tỷ lệ giảm giá

---

## 7. Class Appointment

**Mục đích:** Quản lý cuộc hẹn dịch vụ

**Thuộc tính:**

- `appointmentId: String` - ID cuộc hẹn
- `customer: Customer` - Khách hàng
- `technician: Technician` - Kỹ thuật viên
- `service: Service` - Dịch vụ
- `startTime: LocalDateTime` - Thời gian bắt đầu
- `endTime: LocalDateTime` - Thời gian kết thúc
- `notes: String` - Ghi chú
- `status: AppointmentStatus` - Trạng thái (SCHEDULED, SPENDING, COMPLETED, CANCELLED)

**Phương thức:**

- `schedule(): void` - Lên lịch hẹn
- `start(): void` - Bắt đầu
- `cancel(): void` - Hủy
- `complete(): void` - Hoàn thành

---

## 8. Enumeration AppointmentStatus

**Mục đích:** Định nghĩa các trạng thái cuộc hẹn

**Giá trị:**

- `SCHEDULED` - Đã lên lịch
- `SPENDING` - Đang thực hiện
- `COMPLETED` - Đã hoàn thành
- `CANCELLED` - Đã hủy

---

## 9. Class Service

**Mục đích:** Quản lý dịch vụ

**Thuộc tính:**

- `serviceId: String` - ID dịch vụ
- `serviceName: String` - Tên dịch vụ
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

## 10. Class Product

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

## 11. Interface Sellable

**Mục đích:** Giao diện cho các item có thể bán

**Phương thức:**

- `getId(): String` - Lấy ID
- `display(): void` - Hiển thị
- `input(): void` - Nhập
- `getPrice(): String` - Lấy giá

---

## 12. Class Invoice

**Mục đích:** Quản lý hóa đơn

**Thuộc tính:**

- `invoiceId: String` - ID hóa đơn
- `customer: Customer` - Khách hàng
- `receptionist: Receptionist` - Lễ tân
- `appointment: Appointment` - Cuộc hẹn
- `promotion: Promotion` - Khuyến mãi
- `creationDate: LocalDate` - Ngày tạo
- `productList: DataStore<Product>` - Danh sách sản phẩm
- `totalAmount: double` - Tổng tiền

**Phương thức:**

- `addProduct(): void` - Thêm sản phẩm
- `calculateTotal(): double` - Tính tổng tiền

---

## 13. Class Payment

**Mục đích:** Quản lý thanh toán

**Thuộc tính:**

- `paymentId: String` - ID thanh toán
- `invoice: Invoice` - Hóa đơn
- `amount: double` - Số tiền
- `paymentMethod: PaymentMethod` - Phương thức thanh toán
- `receptionist: Receptionist` - Lễ tân
- `paymentDate: LocalDate` - Ngày thanh toán

**Phương thức:**

- `payment()` - Thực hiện thanh toán
- `processPayment(): boolean` - Xử lý thanh toán

---

## 14. Enumeration PaymentMethod

**Mục đích:** Định nghĩa phương thức thanh toán

**Giá trị:**

- `CASH` - Tiền mặt
- `CARD` - Thẻ
- `TRANSFER` - Chuyển khoản
- `E_WALLET` - Ví điện tử

---

## 15. Class Promotion

**Mục đích:** Quản lý chương trình khuyến mãi

**Thuộc tính:**

- `promotionId: String` - ID khuyến mãi
- `name: String` - Tên khuyến mãi
- `description: String` - Mô tả
- `discountType: DiscountType` - Loại giảm giá
- `discountValue: double` - Giá trị giảm
- `startDate: LocalDate` - Ngày bắt đầu
- `endDate: LocalDate` - Ngày kết thúc
- `minPurchaseAmount: double` - Số tiền mua tối thiểu
- `isDeleted: boolean` - Trạng thái xóa

**Phương thức:**

- `isValid(): boolean` - Kiểm tra hợp lệ
- `calculateDiscount(): double` - Tính giảm giá

---

## 16. Enumeration DiscountType

**Mục đích:** Định nghĩa loại giảm giá

**Giá trị:**

- `PERCENTAGE` - Giảm theo phần trăm
- `FIXED_AMOUNT` - Giảm theo số tiền cố định

---

## 17. Class GoodsReceipt

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

## 18. Interface IActionManager

**Mục đích:** Giao diện quản lý các hành động

**Phương thức:**

- `display(): void` - Hiển thị
- `addItem(): void` - Thêm item
- `updateItem(): void` - Cập nhật item
- `deleteItem(): boolean` - Xóa item
- `findByIdItem(): T` - Tìm item theo ID
- `getAll(): T[]` - Lấy tất cả

---

## 19. Interface IDataManager

**Mục đích:** Giao diện quản lý dữ liệu

**Phương thức:**

- `readFile(): void` - Đọc file
- `writeFile(): void` - Ghi file

---

## 20. Class DataStore<T>

**Mục đích:** Lớp lưu trữ dữ liệu generic

**Phương thức:**

- `findByCondition(): T[]` - Tìm theo điều kiện
- `sort(): void` - Sắp xếp
- `count(): int` - Đếm số lượng

---

## 21. Interface Entities

**Mục đích:** Giao diện chung cho các entity

**Phương thức:**

- `getId(): String` - Lấy ID
- `display(): void` - Hiển thị
- `input(): void` - Nhập
- `getPrice(): String` - Lấy giá

---

## 22. Interface IActionManager (Duplicate)

**Mục đích:** Quản lý các hành động CRUD

**Phương thức:**

- `display(): void` - Hiển thị
- `addItem(): void` - Thêm
- `updateItem(): void` - Cập nhật
- `deleteItem(): boolean` - Xóa
- `findByIdItem(): T` - Tìm theo ID
- `getAll(): T[]` - Lấy tất cả
- `generateStatistics(): void` - Tạo thống kê

---

## Quan Hệ Giữa Các Class

1. **Kế Thừa:** Employee, Receptionist, Technician, Customer đều kế thừa từ Person
2. **Liên Kết:**
   - AuthService quản lý Employee
   - Appointment liên kết Customer, Technician, Service
   - Invoice liên kết Customer, Receptionist, Appointment, Promotion
   - Payment liên kết Invoice, Receptionist
   - GoodsReceipt liên kết Employee, Product
3. **Implement Interface:** Service, Product implement Sellable; GoodsReceipt, Appointment implement IActionManager
