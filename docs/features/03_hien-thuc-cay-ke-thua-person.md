### 3.1. Mô tả Ngữ cảnh
- Hoàn thiện hệ phân cấp người dùng dựa trên lớp trừu tượng `Person`, đảm bảo đầy đủ thuộc tính cá nhân và cơ chế đa hình cho tính lương nhân viên.
- Thiết lập nền tảng dữ liệu khách hàng và nhân viên để các quy trình nghiệp vụ (đặt lịch, thanh toán) có thể khai thác thống nhất ở giai đoạn sau.

- `abstract class Person`:
  - Thuộc tính: `personId`, `fullName`, `phoneNumber`, `isMale`, `birthDate`, `email`, `address`, `isDeleted`.
  - Phương thức trừu tượng: `public abstract String getRole();`
  - Phương thức cụ thể: `getId()`, `getPrefix()`, `display()`, `input()`.
- `abstract class Employee extends Person`:
  - Thuộc tính bổ sung: `salary`, `passwordHash`, `hireDate`.
  - Phương thức trừu tượng: `public abstract double calculatePay();`
  - Phương thức cụ thể: `checkPassword()`, `getRole()`.
- `class Customer extends Person`:
  - Thuộc tính riêng: `memberTier`, `notes`, `points`, `lastVisitDate`.
  - Ghi đè: `getRole()`, `display()`, `input()`.
- `class Technician extends Employee`:
  - Thuộc tính riêng: `skill`, `certifications`, `commissionRate`.
  - Ghi đè: `calculatePay()`, `getRole()`, `display()`.
- `class Receptionist extends Employee`:
  - Thuộc tính riêng: `monthlyBonus`.
  - Ghi đè: `calculatePay()`, `getRole()`, `display()`.

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `DataStore<Customer>`: quản lý mảng khách hàng với `private Customer[] list;`, `private int count;`, `private static final String CUSTOMER_FILE`.
- `DataStore<Employee>`: lưu trữ kỹ thuật viên và lễ tân để hỗ trợ `AuthService`.
- `DataStore<Technician>` và `DataStore<Receptionist>` có thể được tách riêng nếu cần thống kê chuyên biệt.

- Mọi lớp trên thực thi `IEntity`; `Employee`, `Technician`, `Receptionist` phục vụ cho `IActionManager<Employee>` thông qua `DataStore<Employee>`.
- `Customer` không thực thi `Sellable` nhưng tuân thủ `IEntity` để hiển thị/nhập liệu chuẩn hóa.

### 3.5. Yêu cầu Đọc/Ghi File
- `DataStore<Customer>` ghi/đọc dưới dạng văn bản thuần: mỗi dòng là một khách hàng, các trường cách nhau bởi ký tự phân tách thống nhất (ví dụ: `|`).
- `DataStore<Employee>` tải danh sách nhân viên (bao gồm loại hình) nhằm hỗ trợ đăng nhập và phân quyền theo định dạng tương tự.

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Đảm bảo phương thức `calculatePay()` ghi đè khác biệt giữa `Technician` (hoa hồng) và `Receptionist` (thưởng cố định).
- Thiết lập cơ chế nâng điểm, nâng hạng thành viên cho `Customer` thông qua các phương thức `earnPoints()`, `upgradeTier()`.
- Bảo đảm mọi lớp kế thừa thực thi `getPrefix()` để hỗ trợ sinh ID thống nhất trong các giai đoạn tiếp theo.
