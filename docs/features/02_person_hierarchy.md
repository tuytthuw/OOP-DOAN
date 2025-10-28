# Phase 2 – Hệ Phân Cấp Person & Employee

## 1. Mô tả Ngữ cảnh
- Xây dựng mô hình con người làm trung tâm cho hệ thống spa, bao gồm khách hàng và nhân viên.
- Thiết lập quan hệ kế thừa rõ ràng để phục vụ đa hình (polymorphism) cho tính lương và hành vi chuyên biệt.
- Bảo đảm mọi thực thể người đều tuân thủ `IEntity`, hỗ trợ quản lý bằng `BaseManager` ở các phase tiếp theo.

## 2. Cấu trúc Lớp Model (Entities/POJO)
### 2.1 Lớp cơ sở trừu tượng
- **`abstract class Person implements IEntity`**
  - **Thuộc tính:**
    - `protected String personId`
    - `protected String fullName`
    - `protected String phoneNumber`
    - `protected String email`
    - `protected LocalDate birthDate`
    - `protected LocalDateTime createdAt`
    - `protected boolean isDeleted`
    - `protected static final String ID_PREFIX = "PER"`
    - `protected static int runningNumber` (tạo mã tự tăng)
  - **Phương thức:**
    - `public String getId()` – trả về `personId`.
    - `public int getAge()` – tính tuổi từ `birthDate`.
    - `public void softDelete()` / `public void restore()` – bật/tắt cờ `isDeleted`.
    - `protected String generatePersonId()` – ghép `ID_PREFIX` với `runningNumber` (tăng dần).

### 2.2 Lớp trung gian trừu tượng
- **`abstract class Employee extends Person`**
  - **Thuộc tính:**
    - `protected String employeeCode`
    - `protected EmployeeRole role`
    - `protected EmployeeStatus status`
    - `protected BigDecimal salary`
    - `protected String passwordHash`
    - `protected LocalDate hireDate`
    - `protected LocalDateTime lastLoginAt`
    - `protected static final String CODE_PREFIX = "EMP"`
    - `protected static int employeeSequence`
  - **Phương thức:**
    - `public abstract BigDecimal calculatePay()` – bắt buộc lớp con ghi đè.
    - `public boolean checkPassword(String raw)` – so sánh với `passwordHash` (cài đặt chi tiết ở phase sau).
    - `public void updateRole(EmployeeRole newRole)` – cập nhật vai trò.
    - `protected String generateEmployeeCode()` – sinh mã nhân viên duy nhất.

### 2.3 Các lớp kế thừa cụ thể
- **`class Customer extends Person`**
  - **Thuộc tính riêng:** `Tier tier`, `BigDecimal totalSpent`, `int loyaltyPoints`, `LocalDateTime lastVisitAt`, `String notes`.
  - **Phương thức:**
    - `public void addSpending(BigDecimal amount)` – cộng dồn chi tiêu và điểm loyalty.
    - `public void redeemPoints(int value)` – trừ điểm khi đổi thưởng.
    - `public void updateTier(Tier newTier)` – chuyển cấp thành viên.
    - `public void markVisit(LocalDateTime visitAt)` – cập nhật lần ghé gần nhất.

- **`class Receptionist extends Employee`**
  - **Thuộc tính riêng:** `BigDecimal bonusPerSale`, `int monthlyTarget`, `int achievedSalesCount`.
  - **Phương thức:**
    - `public BigDecimal calculatePay()` – trả lương cơ bản + thưởng theo số sale đạt.
    - `public void recordSale(BigDecimal amount)` – tăng `achievedSalesCount`, chuẩn bị dữ liệu cho phase thanh toán.

- **`class Technician extends Employee`**
  - **Thuộc tính riêng:** `String[] skillSet`, `double commissionRate`, `int maxDailyAppointments`, `double rating`, `int completedAppointments`.
  - **Phương thức:**
    - `public BigDecimal calculatePay()` – lương cơ bản + hoa hồng theo số ca hoàn thành.
    - `public boolean isQualifiedFor(ServiceCategory category)` – kiểm tra kỹ năng.
    - `public void updateRating(double newRating)` – cập nhật đánh giá sau dịch vụ.

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
- Phase này chưa tạo lớp Manager mới, nhưng cần ghi nhận yêu cầu để Phase 5 tái sử dụng:
  - `CustomerManager` và `EmployeeManager` sẽ kế thừa `BaseManager<Person>` / `BaseManager<Employee>`.
  - Cần bảo đảm các phương thức CRUD tận dụng `getId()` và xử lý trạng thái `isDeleted`.
  - Yêu cầu dữ liệu mảng: `Customer[] customers`, `Employee[] employees`, `int count`, `static final int MAX_SIZE` (xác định cụ thể ở Phase 5).

## 4. Yêu cầu Giao diện (Interfaces)
- Tiếp tục sử dụng `IEntity` cho tất cả lớp con của `Person`.
- Chưa phát sinh interface mới; các hành vi đặc thù (ví dụ xác thực) sẽ bổ sung ở phase sau thông qua service layer.

## 5. Yêu cầu Đọc/Ghi File
- Chưa triển khai đọc/ghi trong phase này. Ghi chú: các thuộc tính cần serializable (chuẩn bị cho `FileHandler` ở Phase 10, có thể dùng định dạng JSON hoặc CSV theo quyết định chung).

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- Đảm bảo mọi ID (`personId`, `employeeCode`) được sinh tự động và duy nhất bằng thành phần `static`.
- `calculatePay()` phải được override ở từng lớp nhân viên để thể hiện đa hình.
- `softDelete` giữ dữ liệu cho mục đích báo cáo; các manager sau này phải tôn trọng cờ `isDeleted`.
- Không chứa logic IO hoặc UI trong các lớp model; chỉ cung cấp trạng thái và hành vi nghiệp vụ thuần.
- Mảng `skillSet` của `Technician` phải được khởi tạo và quản lý thủ công (không dùng `List`).
