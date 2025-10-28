### 3.1. Mô tả Ngữ cảnh
- Tích hợp các quy trình nghiệp vụ đa bước (tạo hóa đơn, tạo lịch hẹn, nhập kho) dựa trên dữ liệu từ nhiều DataStore, đảm bảo phối hợp giữa khách hàng, nhân viên, dịch vụ và kho hàng.
- Tận dụng đa hình và quan hệ giữa các lớp để cập nhật trạng thái hệ thống đồng nhất sau mỗi luồng nghiệp vụ.

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- Sử dụng các thực thể hiện có: `Customer`, `Technician`, `Receptionist`, `Service`, `Product`, `Promotion`, `Appointment`, `Invoice`, `Payment`, `GoodsReceipt`, `Supplier`.
- Bổ sung phương thức hỗ trợ trong thực thể nếu cần, ví dụ:
  - `Appointment.submitFeedback(int rating, String feedback)`.
  - `Invoice.addProduct(Product product)`.
  - `GoodsReceipt.addProduct(Product product)`.

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `MenuUI` bổ sung các luồng nghiệp vụ:
  - `private void createAppointmentFlow()`:
    - Chọn `Customer` và `Technician` từ DataStore.
    - Chọn `Service`, nhập thời gian, tạo `Appointment`, cập nhật trạng thái `Technician` nếu cần.
    - Gọi `appointmentStore.add()` và `writeFile()`.
  - `private void createInvoiceFlow()`:
    - Chọn `Customer`, `Receptionist`, liên kết `Appointment`.
    - Chọn nhiều `Service`/`Product` (thông qua `productStore`, `serviceStore`).
    - Áp dụng `Promotion`, tính `totalAmount`, `taxRate`, `serviceChargeRate`.
    - Tạo `Invoice`, lưu vào `invoiceStore`, đồng thời cập nhật điểm khách hàng (`Customer.earnPoints()`).
  - `private void processPaymentFlow()`:
    - Liên kết với `Invoice`, xác định `PaymentMethod`, tạo `Payment`, cập nhật trạng thái hóa đơn.
  - `private void createGoodsReceiptFlow()`:
    - Chọn `Supplier`, `Employee`, thêm danh sách `Product`, cập nhật `stockQuantity`.
    - Lưu `GoodsReceipt` và ghi file cho `productStore`.

### 3.4. Yêu cầu Giao diện (Interfaces)
- Sử dụng `IActionManager<T>` để truy xuất dữ liệu cần thiết trong từng bước.
- Có thể định nghĩa interface phụ (ví dụ: `FlowStep`) nếu muốn chuẩn hóa các bước, nhưng ưu tiên giữ đơn giản.

### 3.5. Yêu cầu Đọc/Ghi File
- Sau mỗi luồng nghiệp vụ, ghi file cho tất cả DataStore bị ảnh hưởng (ví dụ: tạo hóa đơn -> `invoiceStore`, `customerStore`, `appointmentStore`).
- Đảm bảo khi đọc file ở giai đoạn khởi động, có thể tái lập quan hệ (ví dụ: `Invoice` chứa ID để `MenuUI` tra cứu lại đối tượng tương ứng khi cần hiển thị chi tiết).

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Khi tạo lịch hẹn, kiểm tra trùng lịch dựa trên `Appointment` hiện có (có thể bổ sung phương thức `isAvailable()` ở `Service` hoặc `Technician`).
- Khi tạo hóa đơn, cập nhật điểm tích lũy và cấp bậc của `Customer` thông qua `earnPoints()` và `upgradeTier()`.
- Khi nhập kho, cập nhật `Product.stockQuantity` và kiểm tra ngưỡng cảnh báo (`reorderLevel`).
- Log hoạt động quan trọng (ví dụ: in thông báo "Tạo hóa đơn thành công với tổng tiền ...") để người dùng nắm state.
