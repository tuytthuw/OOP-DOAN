# Phase 4 – Mô Hình Hóa Nghiệp Vụ

## 1. Mô tả Ngữ cảnh
- Hoàn thiện nhóm thực thể nghiệp vụ hỗ trợ quy trình đặt lịch, lập hóa đơn, khuyến mãi và thanh toán trong spa.
- Đảm bảo mỗi mô hình đều tuân thủ `IEntity` để hoạt động nhất quán với `BaseManager` (Phase 1) và hierachy con người (Phase 2).
- Chuẩn hóa dữ liệu đầu vào cho các service ở Phase 6 (AppointmentService, InvoiceService, PromotionService, PaymentService, AuthService).

## 2. Cấu trúc Lớp Model (Entities/POJO)
### 2.1 Lịch hẹn
- **`class Appointment implements IEntity`**
  - **Thuộc tính:** `String appointmentId`, `String customerId`, `String serviceId`, `String technicianId`, `LocalDateTime startTime`, `LocalDateTime endTime`, `AppointmentStatus status`, `String notes`, `LocalDateTime createdAt`.
  - **Phương thức:**
    - `public String getId()` – trả về `appointmentId`.
    - `public void reschedule(LocalDateTime newStart, LocalDateTime newEnd)` – cập nhật khung giờ.
    - `public void assignTechnician(String technicianId)` – thay đổi nhân sự phục vụ.
    - `public void updateStatus(AppointmentStatus status)` – chuyển trạng thái.
    - `public void cancel(String reason)` – đánh dấu hủy và lưu ghi chú.

### 2.2 Hóa đơn và chi tiết
- **`class InvoiceItem`**
  - **Thuộc tính:** `String sellableId`, `String sellableType`, `int quantity`, `BigDecimal unitPrice`, `BigDecimal discountPerUnit`.
  - **Phương thức:** `public BigDecimal calculateSubtotal()`, `public BigDecimal calculateDiscount()`.

- **`class Invoice implements IEntity`**
  - **Thuộc tính:** `String invoiceId`, `String customerId`, `String appointmentId`, `InvoiceItem[] items`, `DiscountType discountType`, `BigDecimal discountValue`, `BigDecimal subtotal`, `BigDecimal totalAmount`, `LocalDateTime issuedAt`, `boolean isPaid`, `int itemCount`, `static final int MAX_ITEMS`.
  - **Phương thức:**
    - `public String getId()` – trả về `invoiceId`.
    - `public void addItem(InvoiceItem item)` – chèn dòng mới (quản lý mảng thủ công).
    - `public void removeItem(String sellableId)` – loại bỏ dòng theo ID và dồn mảng.
    - `public void applyPromotion(Promotion promotion)` – cập nhật thông tin khuyến mãi.
    - `public void calculateTotals()` – tính `subtotal` và `totalAmount`.
    - `public void markPaid()` – đánh dấu thanh toán hoàn tất.

### 2.3 Khuyến mãi
- **`class Promotion implements IEntity`**
  - **Thuộc tính:** `String promotionId`, `String code`, `String description`, `DiscountType type`, `BigDecimal value`, `BigDecimal minOrderTotal`, `int usageLimit`, `int usedCount`, `LocalDate validFrom`, `LocalDate validTo`, `boolean isActive`.
  - **Phương thức:**
    - `public String getId()` – trả về `promotionId`.
    - `public boolean isApplicable(BigDecimal subtotal, LocalDateTime at)` – kiểm tra điều kiện áp dụng.
    - `public void incrementUsage()` – tăng bộ đếm sử dụng.
    - `public void deactivate()` – vô hiệu hóa mã khi cần.

### 2.4 Giao dịch tài chính
- **`class Transaction implements IEntity`**
  - **Thuộc tính:** `String transactionId`, `String invoiceId`, `BigDecimal amount`, `PaymentMethod method`, `TransactionStatus status`, `String referenceCode`, `LocalDateTime processedAt`, `String failureReason`.
  - **Phương thức:**
    - `public String getId()` – trả về `transactionId`.
    - `public void markSuccess(String referenceCode)` – cập nhật trạng thái thành công.
    - `public void markFailed(String reason)` – cập nhật thất bại và ghi chú.
    - `public void refund(BigDecimal amount)` – đánh dấu hoàn tiền (cập nhật `status`).

### 2.5 Phiên xác thực
- **`class AuthSession implements IEntity`**
  - **Thuộc tính:** `String sessionId`, `String employeeId`, `LocalDateTime loginAt`, `LocalDateTime expiresAt`, `EmployeeRole role`.
  - **Phương thức:**
    - `public String getId()` – trả về `sessionId`.
    - `public boolean isExpired(LocalDateTime now)` – kiểm tra hết hạn.
    - `public void refresh(LocalDateTime newExpiry)` – gia hạn phiên.

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
- Chuẩn bị yêu cầu để Phase 5 triển khai các manager tương ứng:
  - `AppointmentManager` (kế thừa `BaseManager<Appointment>`) cần phương thức `findByDate(LocalDate)`, `findByTechnician(String)`, `findByStatus(AppointmentStatus)`.
  - `InvoiceManager` (kế thừa `BaseManager<Invoice>`) cần `findByCustomer(String)`, `findUnpaid()`.
  - `PromotionManager` (kế thừa `BaseManager<Promotion>`) cần `findActive()`, `findByCode(String)`.
  - `TransactionManager` (kế thừa `BaseManager<Transaction>`) cần `findByInvoice(String)`, `findByStatus(TransactionStatus)`.
  - `AuthSessionManager` (kế thừa `BaseManager<AuthSession>`) cần `findActiveSession(String employeeId)`.
- Mỗi manager sẽ duy trì mảng riêng (`Appointment[]`, `Invoice[]`, …), biến đếm `count`, hằng số `MAX_SIZE`, và tận dụng phương thức `ensureCapacity()` của `BaseManager`.

## 4. Yêu cầu Giao diện (Interfaces)
- Tất cả thực thể ở phase này thực thi `IEntity`.
- Chưa cần interface bổ sung; các hành vi đặc thù (ví dụ gateway thanh toán) sẽ được đặc tả ở phase dịch vụ nếu cần.

## 5. Yêu cầu Đọc/Ghi File
- Chưa thực hiện IO trong phase này. Cần ghi chú định dạng dữ liệu dự kiến (ví dụ JSON với trường lồng nhau cho `InvoiceItem[]`) để Phase 10 dễ serialize/deserialise.
- `AuthSession` không nên được lưu lâu dài; ghi chú rằng chỉ cần lưu phiên hoạt động nếu tuân theo yêu cầu bảo mật.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- Bảo đảm mọi ID (`appointmentId`, `invoiceId`, …) là duy nhất và sinh tự động (có thể dùng tiền tố + bộ đếm `static`).
- `Appointment` phải kiểm soát thời gian: `endTime` luôn sau `startTime`.
- `Invoice` không cho phép số lượng âm; khi xóa `InvoiceItem` phải dồn mảng tránh rỗng.
- `Promotion` phải chặn sử dụng vượt `usageLimit` và kiểm tra thời gian hợp lệ.
- `Transaction` cần liên kết chặt với `Invoice` thông qua `invoiceId` để đảm bảo tính nhất quán.
- `AuthSession` phải giới hạn thời gian sống, chuẩn bị cho logic đăng nhập/đăng xuất ở Phase 6/9.
