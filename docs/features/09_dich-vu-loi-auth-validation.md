### 3.1. Mô tả Ngữ cảnh
- Cung cấp dịch vụ xác thực người dùng và tiện ích kiểm tra đầu vào để đảm bảo an toàn, thống nhất dữ liệu nhập từ console.
- Đáp ứng yêu cầu sử dụng `static` cho Singleton (`AuthService`) và lớp tiện ích (`Validation`).

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- Không giới thiệu thực thể mới; `AuthService` thao tác trên `Employee` hiện có.
- Yêu cầu bổ sung thuộc tính trong `Employee`: `passwordHash`, `hireDate` để phù hợp với logic xác thực và theo dõi nhân sự.

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `class AuthService`:
  - Thuộc tính:
    - `private static AuthService instance;`
    - `private DataStore<Employee> employeeStore;`
    - `private Employee currentUser;`
  - Phương thức:
    - `private AuthService(DataStore<Employee> employeeStore)`.
    - `public static AuthService getInstance(DataStore<Employee> employeeStore)`: triển khai Singleton.
    - `public boolean login(String id, String password)`.
    - `public void logout()`.
    - `public boolean isLoggedIn()`.
    - `public Employee getCurrentUser()`.
    - `public String getCurrentRole()`.
    - `public boolean changePassword(String oldPassword, String newPassword)`.
    - `private String encryptPassword(String raw)`.
- `class Validation`:
  - Thuộc tính `static`:
    - `private static final Scanner SCANNER = new Scanner(System.in);`
  - Phương thức `static`:
    - `public static String getString(String prompt)`.
    - `public static int getInt(String prompt, int min, int max)`.
    - `public static double getDouble(String prompt, double min, double max)`.
    - `public static LocalDate getDate(String prompt, DateTimeFormatter formatter)`.
    - `public static boolean getBoolean(String prompt)`.

### 3.4. Yêu cầu Giao diện (Interfaces)
- `AuthService` không thực thi thêm interface mới nhưng cần tương tác với `IActionManager<Employee>` thông qua `employeeStore`.
- Có thể định nghĩa interface `InputProvider` nếu muốn trừu tượng hóa `Validation` (tùy chọn).

### 3.5. Yêu cầu Đọc/Ghi File
- `AuthService` sử dụng `employeeStore.readFile()` để tải danh sách nhân viên khi khởi tạo.
- `changePassword` gọi `employeeStore.writeFile()` sau khi cập nhật `passwordHash`.

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Đảm bảo `login` so sánh `encryptPassword(password)` với `employee.getPasswordHash()` và chỉ chấp nhận nhân viên chưa bị `isDeleted`.
- `Validation` phải xử lý ngoại lệ nhập liệu, yêu cầu nhập lại khi dữ liệu sai định dạng.
- `AuthService` phải phân quyền dựa trên `getCurrentRole()` để UI quyết định menu hiển thị cho Technician/Receptionist.
