# Phase 12 – Hoàn Thiện Persistence & Danh Sách UI

## 1. Mô tả Ngữ cảnh
- Mục tiêu: hoàn thiện tính năng lưu/đọc CSV bằng `FileHandler` (triển khai `toString()` và `IEntityFactory` cho từng entity chính), đồng thời mở rộng menu phụ để hiển thị danh sách nhân viên/dịch vụ chi tiết và cập nhật tài liệu hướng dẫn.
- Phạm vi: mọi entity được lưu (Customer, Employee, Service, Product, GoodsReceipt, Appointment, Invoice, Promotion, Transaction, AuthSession). UI cập nhật: `CustomerMenu`, `InventoryMenu`, `AppointmentMenu`, `BillingMenu`, `MainMenu`. Tài liệu: `USER_GUIDE.md`.

## 2. Cấu trúc Lớp Model (Entities/POJO)
- **Yêu cầu chung:**
  - Bổ sung `toString()` cho từng model, định dạng CSV: cột phân tách bằng `;`, sắp xếp trường rõ ràng, tránh ký tự `;` trong dữ liệu (nếu có phải escape đơn giản).
  - Tạo lớp phụ trợ (inner static class hoặc file riêng) triển khai `FileHandler.IEntityFactory` cho từng entity, ví dụ: `CustomerFactory implements IEntityFactory`.
- **Danh sách entity cần hỗ trợ:**
  - `Customer`, `Employee` (bao gồm subtype `Receptionist`, `Technician` – cần phân biệt loại trong CSV bằng trường type), `Service`, `Product`, `GoodsReceipt` + `GoodsReceiptItem` (có thể serialize dạng mảng lồng, ví dụ JSON nhẹ hoặc số lượng + lặp), `Appointment`, `Invoice` + `InvoiceItem`, `Promotion`, `Transaction`, `AuthSession`.

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
- Giữ nguyên `BaseManager`.
- Mở rộng service/manager nếu cần hàm hỗ trợ chuyển đổi:
  - Ví dụ: `InvoiceManager` cần hàm ghi `Invoice` bao gồm `InvoiceItem[]`; factory tương ứng tạo lại các `InvoiceItem`.
- Đảm bảo `loadAllData()` đọc dữ liệu vào manager bằng cách gọi `manager.add()` để giữ nguyên logic kiểm tra.
- Cập nhật `saveAllData()` sử dụng `manager.getAll()` và `FileHandler.save()`.

## 4. Yêu cầu Giao diện (Interfaces)
- Không bắt buộc tạo interface mới, song cân nhắc `CsvSerializable` nếu muốn chuẩn hóa `toString()` và factory map.
- Cập nhật menu con (`CustomerMenu`, `InventoryMenu`, `AppointmentMenu`, `BillingMenu`) để hiển thị danh sách nhân viên/dịch vụ chi tiết theo kế hoạch Phase 11 (đã triển khai code – ở kế hoạch này chỉ xác nhận).
- `MainMenu` giữ quyền kiểm tra (role MANAGER/ADMIN) trước khi xem danh sách nhân viên/dịch vụ.

## 5. Yêu cầu Đọc/Ghi File
- `Main.ApplicationContext.loadAllData()` phải sử dụng các factory để nạp dữ liệu thực sự thay vì placeholder `line -> null`.
- `FileHandler` cần bản đồ `key -> factory` (ví dụ: `Map<String, IEntityFactory>` hoặc mảng song song như hiện dùng) để gọi `factory.fromLine` và `manager.add()` cho mỗi entity.
- Khi ghi file, đảm bảo `FileHandler.save()` gọi `entity.toString()` trả về một dòng CSV hoàn chỉnh.
- Đối với entity chứa mảng (Invoice, GoodsReceipt), cân nhắc encode mảng thành chuỗi JSON đơn giản hoặc chuỗi có phân tách rõ ràng (`|`), đảm bảo factory biết cách parse.
- Kiểm thử với dữ liệu unicode (tiếng Việt) trong CSV.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
1. **Persistence:**
   - Hoàn thiện `toString()` + factory cho từng entity.
   - Cập nhật `ApplicationContext.loadAllData()` để đọc file, gọi `manager.add()`.
   - Bảo đảm `saveAllData()` được gọi khi thoát chương trình, ghi đầy đủ.
2. **UI danh sách:**
   - Đảm bảo menu phụ hiển thị danh sách sử dụng dữ liệu load từ file (sau khi persistence hoàn thiện).
   - Thêm hiển thị danh sách nhân viên/dịch vụ ngay trong menu con nếu cần (hiện đã có ở MainMenu; có thể bổ sung `EmployeeMenu` trong tương lai – ngoài phạm vi phase này).
3. **Tài liệu:**
   - Cập nhật `USER_GUIDE.md` mô tả các tùy chọn menu mới (danh sách khách hàng, sản phẩm, lịch hẹn, hóa đơn, v.v.) và hướng dẫn cách dữ liệu được lưu/đọc.
4. **Kiểm thử:**
   - Chạy chương trình: thêm dữ liệu -> thoát -> chạy lại -> kiểm tra dữ liệu khôi phục.
   - Kiểm chứng trình tự: tạo hoá đơn + thanh toán + xem danh sách để chắc chắn CSV ghi/đọc chính xác.

## 7. Checklist Triển khai
1. Viết `toString()` cho tất cả entity.
2. Tạo lớp factory tương ứng (có thể đặt trong package `io.factory`), implement `fromLine()` trả về entity.
3. Cập nhật `FileHandler` để map key -> factory.
4. Bổ sung logic load vào `ApplicationContext.loadAllData()` (loop over `IEntity[]` và add).
5. Kiểm thử chạy thực tế với dữ liệu demo.
6. Sửa `USER_GUIDE.md` bổ sung menu list và hướng dẫn persistence.

## 8. Mở rộng Tương Lai
- Hỗ trợ backup nhiều file (ví dụ: `customers_yyyyMMdd.csv`).
- Tạo script import/export JSON.
- Tối ưu hóa hiển thị danh sách (lọc theo vai trò, phân trang) khi lượng dữ liệu lớn.
- Triển khai menu nhân viên riêng để quản lý role/password trực quan.
