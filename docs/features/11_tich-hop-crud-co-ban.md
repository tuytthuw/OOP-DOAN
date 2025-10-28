### 3.1. Mô tả Ngữ cảnh
- Kết nối UI console với DataStore để thao tác CRUD cho từng nhóm đối tượng (khách hàng, dịch vụ, sản phẩm, lịch hẹn, khuyến mãi, nhà cung cấp, nhân viên, hóa đơn, thanh toán).
- Đảm bảo mỗi hành động người dùng đều cập nhật DataStore tương ứng và ghi file nhằm duy trì tính nhất quán dữ liệu.

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- Không thêm lớp mới; sử dụng các thực thể hiện có với `display()` và `input()` đảm bảo nhập/xuất dữ liệu đa hình.

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `MenuUI` bổ sung phương thức xử lý CRUD:
  - `private void addCustomer()` -> sử dụng `Validation`, tạo `Customer`, gọi `customerStore.add()`.
  - `private void updateCustomer()` -> tìm `Customer` theo ID, gọi `customerStore.update()`.
  - `private void deleteCustomer()` -> gọi `customerStore.delete()`.
  - Các phương thức tương tự cho Service, Product, Promotion, Supplier, Employee, Appointment, Invoice, Payment.
  - `private void displayCustomers()` -> `customerStore.displayList()`.
  - `private void displayServices()`... tương tự cho từng thực thể.
- Sau mỗi thao tác, gọi `writeFile()` tương ứng để lưu dữ liệu.

### 3.4. Yêu cầu Giao diện (Interfaces)
- Tận dụng `IActionManager<T>` và `IDataManager` để đảm bảo `MenuUI` tương tác thông qua hợp đồng chung.
- Có thể sử dụng phương thức mặc định trong `MenuUI` như `private <T extends IEntity> void handleAdd(DataStore<T> store)` để tránh lặp code (nếu không vi phạm KISS).

### 3.5. Yêu cầu Đọc/Ghi File
- Mỗi thao tác CRUD thành công phải gọi `store.writeFile()` ngay sau đó để tránh mất dữ liệu khi thoát ứng dụng đột ngột.
- Các mục hiển thị danh sách lấy từ dữ liệu đã được `readFile()` tại `main`.

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Duy trì phân quyền: menu CRUD cho nhân viên chỉ hiển thị khi `currentRole` phù hợp.
- Thông báo kết quả thao tác (thành công/thất bại) bằng `System.out.println()`.
- `delete` phải xử lý cả xóa mềm (`isDeleted`) lẫn xóa cứng tùy đặc trưng lớp; DataStore cần hỗ trợ (ví dụ: chuyển sang đánh dấu `isDeleted` thay vì xóa khỏi mảng nếu cần lịch sử).
