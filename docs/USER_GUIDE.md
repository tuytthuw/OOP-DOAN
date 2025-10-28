# HƯỚNG DẪN SỬ DỤNG HỆ THỐNG QUẢN LÝ SPA

## 1. Chuẩn bị & Chạy chương trình
1. Cài JDK 17+ và Maven.
2. Từ thư mục dự án, biên dịch và chạy:
   ```bash
   mvn compile
   mvn exec:java -Dexec.mainClass=Main
   ```
3. Khi chương trình chạy, dữ liệu demo sẽ tự sinh nếu chưa có dịch vụ/sản phẩm. Dữ liệu sẽ lưu vào thư mục `data/` dạng CSV (hiện tại nội dung lưu sử dụng `toString()`—cần triển khai factory cho việc khôi phục).

## 2. Cấu trúc tầng chính
- **collections/**: Toàn bộ `*Manager` quản lý mảng thuần, hỗ trợ CRUD và tìm kiếm đặc thù.
- **models/**: Các mô hình dữ liệu (Person/Employee/Customer/Service/Product/Invoice/...).
- **services/**: Business logic (CustomerService, AppointmentService,...).
- **ui/**: Tầng giao diện console (`MainMenu`, các menu con, input/output helper).
- **io/FileHandler**: Tiện ích lưu/đọc file (CSV). Cần bổ sung `IEntityFactory` cho từng entity nếu muốn load thực sự.

## 3. Luồng tương tác chính
1. **Đăng nhập** (`MainMenu` lựa chọn 1):
   - Nhập mã nhân viên (employeeCode) và mật khẩu.
   - Quyền truy cập Billing/Inventory yêu cầu vai trò `MANAGER` hoặc `ADMIN`.
2. **Quản lý khách hàng** (`MainMenu` -> 3):
   - Thêm khách hàng.
   - Liệt kê khách hàng theo tier.
3. **Quản lý lịch hẹn** (`MainMenu` -> 4):
   - Tạo lịch hẹn mới (mặc định 1 giờ, tự sinh thời gian hiện tại).
   - Gán kỹ thuật viên, hủy lịch.
4. **Quản lý kho** (`MainMenu` -> 5, yêu cầu đăng nhập role):
   - Ghi nhận phiếu nhập hàng gồm nhiều dòng sản phẩm.
   - Áp dụng phiếu để cập nhật tồn kho.
   - Xem sản phẩm tồn thấp.
5. **Thanh toán hóa đơn** (`MainMenu` -> 6, yêu cầu role):
   - Chọn hóa đơn, áp mã khuyến mãi (nếu hợp lệ).
   - Chuyển trạng thái hóa đơn, tạo giao dịch thanh toán.
   - Xem danh sách giao dịch theo trạng thái.

## 4. Quản lý dữ liệu
- **Lưu dữ liệu**: khi thoát chương trình, `saveAllData()` ghi toàn bộ manager vào CSV.
- **Tải dữ liệu**: `loadAllData()` hiện là placeholder (`line -> null`). Cần hiện thực `IEntityFactory` và `toString()` để nạp thực sự.
- **Seed demo**: tự tạo 1 dịch vụ và 1 sản phẩm mẫu nếu chưa có dữ liệu.

## 5. Mở rộng & Lưu ý
- **Persistence hoàn chỉnh**: bổ sung cặp `toString()`/`IEntityFactory` cho từng entity để FileHandler parse.
- **Validation nâng cao**: hiện tại nhiều hàm mặc định thời gian (dùng `LocalDateTime.now()`). Tùy chỉnh theo nhu cầu thực tế.
- **Quy tắc project**: luôn dùng `T[]`, quản lý biến `count` thủ công, không dùng `java.util` Collections.

## 6. Danh sách Service & Menu
| Lớp | Chức năng chính |
| --- | --- |
| `CustomerService` | Đăng ký, cập nhật, tính lifetime value, đổi điểm loyalty |
| `EmployeeService` | Tạo nhân viên, đổi mật khẩu, tìm theo vai trò |
| `AppointmentService` | Tạo/đổi/hủy lịch, gán kỹ thuật viên |
| `InventoryService` | Ghi nhận/áp dụng phiếu nhập, báo cáo tồn kho |
| `InvoiceService` | Tạo hóa đơn, thêm sản phẩm/dịch vụ, áp khuyến mãi |
| `PromotionService` | Quản lý mã khuyến mãi, kiểm tra điều kiện |
| `PaymentService` | Xử lý thanh toán, hoàn tiền |
| `AuthService` | Đăng nhập, quản lý phiên, kiểm tra vai trò |
| `BillingMenu` | Luồng thanh toán, xem giao dịch |
| `CustomerMenu`, `InventoryMenu`, `AppointmentMenu` | Menu con cho CRUD/flow tương ứng |

## 7. Checklist trước khi bàn giao
- Implement `IEntityFactory` và `toString()` cho từng entity nếu muốn persistence hoàn chỉnh.
- Viết thêm unit test cho manager/service chính.
- Kiểm tra logic múi giờ/hạn session cho Auth nếu triển khai thực tế.
- Xem xét bổ sung xử lý nhập liệu lịch hẹn (ngày giờ do người dùng nhập thay vì `now()`).

---
**Liên hệ**: Nếu cần hỗ trợ thêm, hãy cập nhật README/Issues trên repo để nhóm phối hợp.
