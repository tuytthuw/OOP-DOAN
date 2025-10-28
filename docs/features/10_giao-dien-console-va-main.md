### 3.1. Mô tả Ngữ cảnh
- Xây dựng giao diện console điều khiển ứng dụng, bao gồm menu chính và các menu con cho từng nhóm chức năng (khách hàng, dịch vụ, kho hàng, lịch hẹn).
- Thiết lập điểm khởi đầu `SpaManagementApp` để phối hợp DataStore, AuthService và Validation vận hành xuyên suốt vòng đời ứng dụng.

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- Không tạo model mới; sử dụng các thực thể đã xây dựng qua DataStore.
- Bảo đảm mỗi thực thể có `display()` và `input()` để UI gọi đa hình khi cần in hoặc nhập dữ liệu.

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `class MenuUI`:
  - Thuộc tính:
    - `private final AuthService authService;`
    - `private final DataStore<Customer> customerStore;`
    - `private final DataStore<Service> serviceStore;`
    - `private final DataStore<Product> productStore;`
    - `private final DataStore<Appointment> appointmentStore;`
    - `private final DataStore<Invoice> invoiceStore;`
    - Các DataStore khác tùy nhu cầu (Promotion, Supplier, GoodsReceipt, Payment).
  - Phương thức:
    - `public void displayMainMenu()`.
    - `private void handleCustomerMenu()`.
    - `private void handleServiceMenu()`.
    - `private void handleInventoryMenu()`.
    - `private void handleAppointmentMenu()`.
    - `private void handleInvoiceMenu()`.
    - `private void handlePromotionMenu()`.
    - `private void handleSupplierMenu()`.
    - `private void handlePaymentMenu()`.
    - `private void handleAuthMenu()` (đổi mật khẩu, đăng xuất).
    - Các phương thức tiện ích dùng `Validation` để nhận input và gọi DataStore tương ứng.
- `class SpaManagementApp`:
  - Thuộc tính `static`:
    - `private static final String DATA_FOLDER = "data";`
  - Phương thức `static`:
    - `public static void main(String[] args)` khởi tạo DataStore, đọc file, tạo `AuthService`, `MenuUI`, và khởi chạy vòng lặp menu.
    - `private static void initializeStores()` (tùy chọn) tạo các DataStore, gọi `readFile()`.

### 3.4. Yêu cầu Giao diện (Interfaces)
- `MenuUI` tương tác với DataStore thông qua các phương thức `IActionManager<T>` và `IDataManager`.
- Có thể định nghĩa interface `IMenu` nếu muốn tái sử dụng cấu trúc menu cho các module khác.

### 3.5. Yêu cầu Đọc/Ghi File
- `SpaManagementApp.main` gọi `readFile()` trước khi hiển thị menu và `writeFile()` sau mỗi thao tác CRUD thông qua `MenuUI`.
- Ghi chú đường dẫn file và đảm bảo kho dữ liệu văn bản tồn tại trước khi đọc/ghi (kiểm tra và tạo nếu cần).

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Menu chính sử dụng vòng lặp `while(true)` và `switch-case`; thoát chương trình khi người dùng chọn `Exit`.
- Phân quyền menu: Nếu `AuthService.getCurrentRole()` là `Technician`, ẩn chức năng quản lý nhân sự; nếu `Receptionist`, ẩn chức năng kỹ thuật viên.
- Kết hợp `Validation` để mọi input đều được lọc, tránh crash vì dữ liệu sai.
- Sau mỗi thao tác CRUD quan trọng, gọi `writeFile()` để lưu trạng thái DataStore tương ứng.
