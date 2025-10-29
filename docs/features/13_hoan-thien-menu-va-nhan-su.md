# KẾ HOẠCH KỸ THUẬT: HOÀN THIỆN MENU & MÔ HÌNH NHÂN SỰ

## 1. MÔ TẢ NGỮ CẢNH
- Hoàn thiện toàn bộ menu UI còn thiếu (InvoiceMenu, PaymentMenu, MenuUI tổng, Employee/Technician/Receptionist menu) theo chuẩn bulk input, tìm kiếm đa khóa, thống kê, hiển thị chi tiết.
- Tách biệt khu vực quản lý nhân sự thành module riêng (EmployeeManagementMenu) để phân nhóm chức năng nhân viên, kỹ thuật viên, lễ tân.
- Bổ sung mô hình tác nhân **Admin** nhằm đại diện quyền quản trị hệ thống, làm rõ vai trò trong luồng nghiệp vụ và phân quyền menu.

## 2. CẤU TRÚC LỚP MODEL (ENTITIES / POJO)
### 2.1 Lớp cơ sở (Trừu tượng)
- `abstract class Person`
  - Thuộc tính: `personId`, `fullName`, `phoneNumber`, `male`, `birthDate`, `email`, `address`, `deleted`.
  - Phương thức trừu tượng: `public abstract String getRole();`
  - Phương thức đã có: getter/setter, `display()` (đa hình), hỗ trợ định dạng theo yêu cầu.

### 2.2 Các lớp kế thừa hiện hữu
- `class Customer extends Person`
  - Thuộc tính riêng: `Tier memberTier`, `String notes`, `int points`, `LocalDate lastVisitDate`.
  - Ghi đè `display()` (đã có), `getRole()` trả về "CUSTOMER".
- `class Employee extends Person`
  - Thuộc tính riêng: `String employeeId`, `LocalDate hireDate`, `double salary`, `EmployeeType employeeType`.
  - Phương thức: `public abstract EmployeeType getEmployeeType();`
- `class Technician extends Employee`
  - Thuộc tính riêng: `String speciality`, `int yearsExperience`.
  - Override `display()`, `getEmployeeType()` trả về `EmployeeType.TECHNICIAN`.
- `class Receptionist extends Employee`
  - Thuộc tính riêng: `String shift`, `String deskCode`.
  - Override `display()`, `getEmployeeType()` trả về `EmployeeType.RECEPTIONIST`.

### 2.3 Lớp mới cần bổ sung
- `class Admin extends Employee`
  - Thuộc tính riêng: `String permissionGroup`, `boolean superAdmin`.
  - Override `display()` hiển thị đầy đủ quyền hạn.
  - Override `getRole()` trả về "ADMIN" và `getEmployeeType()` trả về `EmployeeType.ADMIN`.
  - Phương thức phụ: `public boolean canAccess(String moduleKey)` phục vụ phân quyền.

### 2.4 Các entity liên quan menu còn thiếu
- `class Invoice` (cập nhật)
  - Bổ sung `display()` để in rõ dịch vụ, sản phẩm, thuế, phí, khuyến mãi, trạng thái thanh toán.
  - Giữ `DataStore<Product>` bằng mảng thuần với `count` nội bộ.
- `class Payment` (mới)
  - Thuộc tính: `String paymentId`, `Invoice invoice`, `double amount`, `PaymentMethod method`, `LocalDateTime paidAt`, `String note`, `boolean refunded`.
  - `display()` in bảng thanh toán chi tiết.
- `class MenuActionLog` (tùy chọn mở rộng)
  - Thuộc tính: `String actionId`, `String module`, `String actorId`, `LocalDateTime timestamp`, `String description`.
  - Phục vụ thống kê hoạt động và theo dõi quyền Admin.

## 3. CẤU TRÚC LỚP QUẢN LÝ (SERVICES / MANAGER)
### 3.1 Kho dữ liệu chung
- Các Store tiếp tục dùng `DataStore<T>` (mảng thuần), bao gồm `private static final int MAX_SIZE`, `private T[] data`, `private int count`.

### 3.2 Manager hiện có cần mở rộng
- `EmployeeStore`
  - Thuộc tính: `private static final int MAX_EMPLOYEES`, `private Employee[] employees`, `private int count`.
  - Phương thức mới: `public Admin[] getAllAdmins()`, `public Employee[] findByType(EmployeeType type)`, `public Employee[] searchByKeywords(String[] tokens)`, `public void writeFile()` cập nhật dữ liệu.
- `InvoiceStore`
  - Phương thức thêm: `public Invoice[] findByDateRange(LocalDate from, LocalDate to)`, `public Invoice[] findByCustomer(String customerId)`, `public double calculateRevenue(LocalDate from, LocalDate to)`, `public Invoice[] findUnpaidInvoices()`.
- `PaymentStore` (mới)
  - Thuộc tính: `private static final int MAX_PAYMENTS`, `private Payment[] payments`, `private int count`.
  - Phương thức: `add(Payment)`, `delete(String id)`, `findById`, `findByInvoiceId`, `findByDateRange`, `calculateTotalByMethod(PaymentMethod)`.
- `AppointmentStore`
  - Bổ sung `public Appointment[] getByTechnician(String technicianId)`, `public Appointment[] getUpcoming(LocalDateTime from, LocalDateTime to)` hỗ trợ thống kê nhân sự.

### 3.3 Manager mới đề xuất
- `AdminStore`
  - Thuộc tính: `private Admin[] admins`, `private int count`, `private static final int MAX_ADMINS`.
  - Phương thức: `add(Admin)`, `update(Admin)`, `delete(String id)`, `findById`, `searchByPermissionGroup`, `getSuperAdmins()`.
- `MenuAccessControl`
  - Thuộc tính: `private String moduleKey`, `private String[] requiredRoles`, `private int roleCount`.
  - Phương thức: `public boolean isAccessible(Admin admin)`; `public void grantRole(String role)`.

## 4. YÊU CẦU GIAO DIỆN (INTERFACES)
- `interface Displayable`
  - Phương thức: `void display();` (tất cả entity sử dụng chung theo đa hình).
- `interface IFileService<T>`
  - Phương thức: `T[] readData()`, `void writeData(T[] data, int count)`.
  - Các Store như `AdminStore`, `PaymentStore` triển khai thông qua lớp đọc/ghi cụ thể.
- `interface Searchable<T>`
  - Phương thức: `T[] search(String[] tokens)`.
  - `EmployeeStore`, `InvoiceStore`, `PaymentStore` kế thừa để thống nhất nghiệp vụ tìm kiếm nhiều khóa.

## 5. YÊU CẦU ĐỌC / GHI FILE
- Cập nhật/ tạo mới các tệp dữ liệu text:
  - `data/employees.txt`, `data/admins.txt`, `data/invoices.txt`, `data/payments.txt`, `data/menu_logs.txt`.
- Định dạng: CSV phân tách `;` hoặc `|`, đảm bảo xử lý null bằng chuỗi rỗng.
- Sau mỗi thao tác CRUD tại menu, gọi `writeFile()` tương ứng.
- Đảm bảo Admin có thể trigger sao lưu (backup) thủ công thông qua menu quản trị.

## 6. YÊU CẦU NGHIỆP VỤ CỐT LÕI
### 6.1 Hoàn thiện menu thiếu
- `InvoiceMenu`
  - Chức năng: thêm nhiều hóa đơn, tìm kiếm theo khách hàng/lễ tân/ngày/trạng thái, thống kê doanh thu, in chi tiết bằng `invoice.display()`.
  - Sau khi tạo hóa đơn, gắn thanh toán tự động nếu có.
- `PaymentMenu`
  - Chức năng: ghi nhận thanh toán, hoàn tiền, tìm kiếm theo phương thức, thống kê tổng tiền theo phương thức và theo khoảng thời gian.
  - Cho phép xem danh sách hóa đơn chưa thanh toán và thực hiện thu tiền nhanh.
- `MenuUI`
  - Nhóm menu: Dịch vụ, Sản phẩm, Khuyến mãi, Kho, Lịch hẹn, Hóa đơn, Thanh toán, Nhân sự, Hệ thống.
  - Kiểm tra quyền(Admin) trước khi cho truy cập menu "Hệ thống", "Nhân sự".
- `EmployeeManagementMenu`
  - Tách khỏi menu chung; cung cấp sub menu cho Technician, Receptionist, Admin.
  - Hỗ trợ bulk input, cập nhật, xoá, tìm kiếm đa khóa (theo kỹ năng, ca làm, nhóm quyền), thống kê số lượng từng loại nhân viên, lương trung bình.
- `AdminMenu`
  - Chức năng: thêm Admin, gán nhóm quyền, xem nhật ký hoạt động, bật/tắt quyền truy cập module.

### 6.2 Phân quyền tác nhân
- Admin đăng nhập sẽ tải dữ liệu `AdminStore`, tùy theo `permissionGroup` để hiển thị menu phù hợp.
- Các menu kiểm tra `MenuAccessControl` trước khi thực thi thao tác nhạy cảm (ví dụ: xoá dữ liệu, xem báo cáo tài chính).

### 6.3 Thống kê & báo cáo
- Invoice: tổng doanh thu, doanh thu theo khách hàng, theo dịch vụ, số hóa đơn chưa thanh toán.
- Payment: phân tích phương thức thanh toán, tổng tiền hoàn trả, số giao dịch theo ca làm việc của lễ tân.
- Employee: tổng số nhân viên theo loại, kỹ thuật viên đang rảnh (chưa gán lịch), lịch sử lịch hẹn đã hoàn thành.

### 6.4 Kiểm soát dữ liệu nhập
- Mọi input trong menu mới sử dụng `Validation.getStringOrCancel`, `getIntOrCancel`, `getDoubleOrCancel`, `getDateOrCancel`.
- Bulk input kết thúc khi người dùng nhập `Q` hoặc đạt số lượng tối đa.

## 7. MENU NHÂN SỰ TÁCH BIỆT
- `EmployeeManagementMenu implements MenuModule`
  - `show()` hiển thị lựa chọn: Quản lý kỹ thuật viên, Quản lý lễ tân, Quản lý Admin, Thống kê nhân sự, Quay lại.
  - Mỗi lựa chọn gọi menu con tương ứng.
- `TechnicianMenu`
  - Bulk nhập kỹ năng, tìm kiếm theo từ khóa, thống kê lịch hẹn đã thực hiện.
- `ReceptionistMenu`
  - Quản lý ca làm, thống kê số hóa đơn lập theo ca.
- `AdminMenu`
  - Thêm, cập nhật, xoá Admin; gán quyền module; thống kê hành động hệ thống.

## 8. GHI CHÚ TRIỂN KHAI
- Tuân thủ KISS & SRP: Model giữ dữ liệu, Manager xử lý nghiệp vụ, UI điều phối.
- Không dùng Collection Framework; mảng thuần và biến `count` để quản lý.
- Luôn gọi `writeFile()` sau khi CRUD và sau khi thay đổi phân quyền.
- Bổ sung `invoice.display()`, `payment.display()`, `admin.display()` để menu sử dụng đa hình hiển thị.
- Đảm bảo thống nhất chuẩn menu: bulk input, tìm kiếm đa khóa, thống kê, hiển thị chi tiết, hỗ trợ huỷ thao tác.
