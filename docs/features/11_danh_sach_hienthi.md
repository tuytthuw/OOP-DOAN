# Phase 11 – Bổ Sung Chức Năng Xem Danh Sách

## 1. Mô tả Ngữ cảnh
- Mục tiêu: cung cấp màn hình console hiển thị danh sách cho mọi thực thể chính (khách hàng, nhân viên, dịch vụ, sản phẩm, phiếu nhập, lịch hẹn, hoá đơn, khuyến mãi, giao dịch, phiên đăng nhập) để hỗ trợ kiểm tra dữ liệu nhanh.
- Tuân thủ quy tắc: vẫn dùng mảng + biến đếm, không sử dụng Collection Framework.
- Phối hợp với menu hiện tại (CustomerMenu, InventoryMenu, AppointmentMenu, BillingMenu, MainMenu) để bổ sung lựa chọn “Xem danh sách”.

## 2. Cấu trúc Model (Entities/POJO)
- Không tạo model mới; tận dụng các entity sẵn có (`Customer`, `Employee`, `Service`, `Product`, `GoodsReceipt`, `Appointment`, `Invoice`, `Promotion`, `Transaction`, `AuthSession`).
- Bảo đảm mỗi entity cung cấp đủ getter để hiện thông tin (nếu thiếu toString, cân nhắc bổ sung dạng rút gọn cho mục hiển thị).

## 3. Cấu trúc Lớp Quản lý (Managers/Services)
- Mở rộng các service để cung cấp phương thức lấy danh sách:
  - `CustomerService#getAllCustomers()` → trả về `Customer[]` (sử dụng `customerManager.getAll()`).
  - `EmployeeService#getAllEmployees()`.
  - `ServiceCatalog#listAll()` đã tồn tại, tái sử dụng.
  - `InventoryService#listProducts()`, `InventoryService#listReceipts()`.
  - `AppointmentService#listAppointments()`.
  - `InvoiceService#getAllInvoices()`.
  - `PromotionService#getAllPromotions()`.
  - `PaymentService#listAllTransactions()`.
  - `AuthService#getAllSessions()` (tuỳ chọn; chỉ hiển thị khi role phù hợp).
- Nếu manager chưa có getAll (đã có từ BaseManager), tái sử dụng `manager.getAll()`.

## 4. Yêu cầu Giao diện (Interfaces)
- Không cần interface mới; UI gọi trực tiếp service tương ứng.
- Xem xét tạo interface `ListableMenu` nếu muốn chuẩn hoá `showList()` giữa các menu, nhưng không bắt buộc.

## 5. Yêu cầu Đọc/Ghi File
- Khi hiển thị danh sách, không thực hiện IO file trực tiếp.
- Đảm bảo dữ liệu load từ FileHandler (sau khi hoàn thiện factory) sẽ được hiển thị đúng.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- Menu chính cần thêm lựa chọn “Hiển thị tất cả” ở từng module:
  - `CustomerMenu`: thêm mục “3. Danh sách khách hàng”.
  - `InventoryMenu`: thêm mục xem sản phẩm, phiếu nhập.
  - `AppointmentMenu`: bổ sung “Danh sách lịch hẹn”.
  - `BillingMenu`: ngoài danh sách giao dịch theo status, thêm mục xem tất cả hoá đơn, khuyến mãi.
  - `MainMenu`: thêm mục “Xem danh sách nhân viên”, “Xem danh sách dịch vụ” (tuỳ role).
- Sử dụng `OutputFormatter#printTable` để trình bày dữ liệu; đảm bảo xử lý trường hợp danh sách rỗng (in thông báo).
- Không đưa logic sắp xếp/phân trang ở phase này; chỉ hiển thị toàn bộ.

## 7. Checklist Triển khai
1. **Service Layer**: thêm các phương thức `getAll*()` trả về mảng.
2. **UI Layer**: cập nhật menu, thêm case in bảng:
   - Chuẩn bị `String[][] rows` từ mảng entity.
   - Gọi `outputFormatter.printTable` với cột phù hợp.
3. **MainMenu**: cập nhật role check cho các mục cần quyền quản trị.
4. **Kiểm thử**: chạy luồng menu, xác nhận danh sách hiển thị đúng; thử trường hợp trống.
5. **Docs**: cập nhật `USER_GUIDE.md` mục “Luồng tương tác” với các lựa chọn xem danh sách mới.

## 8. Mở rộng Tương Lai
- Sau khi persistence hoàn thiện, hỗ trợ lọc theo tiêu chí (ví dụ: nhân viên theo role, dịch vụ theo category ngay trong menu “Danh sách”).
- Thêm tính năng xuất danh sách ra file CSV riêng (sử dụng FileHandler).
