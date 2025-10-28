# Phase 6 – Lớp Nghiệp Vụ (Services)

## 1. Mô tả Ngữ cảnh
- Hiện thực tầng dịch vụ xử lý nghiệp vụ cho từng miền (khách hàng, lịch hẹn, hàng hóa, thanh toán, xác thực...).
- Đảm bảo mỗi service chỉ tương tác với các lớp `*Manager` tương ứng (tuân thủ SRP, tránh phụ thuộc chéo), ngoại lệ duy nhất: `AppointmentService` được phép dùng `TechnicianAvailabilityService`.
- Chuẩn bị các điểm mở rộng để UI (Phase 7-9) gọi thao tác nghiệp vụ nhất quán.

## 2. Cấu trúc Lớp Model (Entities/POJO)
- Không tạo model mới; tận dụng các entity từ Phase 2-4.
- Các service cần hiểu rõ thuộc tính quan trọng (ví dụ `isDeleted`, `status`, `loyaltyPoints`, `stockQuantity`) để quyết định nghiệp vụ.

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
### 3.1 Yêu cầu chung cho Service
- Mỗi service giữ tham chiếu tới manager tương ứng (truyền qua constructor).
- Không lưu cache dữ liệu; mọi truy vấn đều thông qua manager đảm bảo dữ liệu mới nhất trong mảng.
- Các phương thức public chỉ cung cấp hành vi nghiệp vụ, không xử lý nhập liệu hoặc in ấn.

### 3.2 Danh sách Service cụ thể
- **`CustomerService`**
  - Thuộc tính: `private final CustomerManager customerManager`.
  - Phương thức chính: `register(Customer)`, `updateProfile(Customer)`, `remove(String id)`, `getByTier(Tier tier)`, `calculateLifetimeValue(String id)`, `redeemPoints(String id, int value)`.
- **`EmployeeService`**
  - Thuộc tính: `private final EmployeeManager employeeManager`.
  - Phương thức: `create(Employee)`, `update(Employee)`, `deactivate(String id)`, `changePassword(String id, String newRaw)`, `authenticate(String username, String password)` (trả về `AuthSession` hoặc thông tin xác thực).
- **`TechnicianAvailabilityService`**
  - Thuộc tính: `private final AppointmentManager appointmentManager`.
  - Phương thức: `isAvailable(String technicianId, LocalDateTime start, int durationMinutes)`, `nextAvailableSlot(String technicianId, LocalDateTime from)`, `countDailyAppointments(String technicianId, LocalDate date)`.
- **`AppointmentService`**
  - Thuộc tính: `private final AppointmentManager appointmentManager`, `private final TechnicianAvailabilityService technicianAvailabilityService`.
  - Phương thức: `schedule(Appointment)`, `reschedule(String id, LocalDateTime start, LocalDateTime end)`, `assignTechnician(String id, String technicianId)`, `cancel(String id, String reason)`, `complete(String id)`.
- **`ServiceCatalog`**
  - Thuộc tính: `private final ServiceManager serviceManager`.
  - Phương thức: `listAll()`, `listByCategory(ServiceCategory category)`, `search(String keyword)`, `listActive()`.
- **`InventoryService`**
  - Thuộc tính: `private final GoodsReceiptManager goodsReceiptManager`, `private final ProductManager productManager`.
  - Phương thức: `recordGoodsReceipt(GoodsReceipt receipt)`, `applyReceipt(String receiptId)`, `getLowStockProducts(int threshold)`, `reconcileStock()`.
- **`InvoiceService`**
  - Thuộc tính: `private final InvoiceManager invoiceManager`, `private final ProductManager productManager`, `private final ServiceManager serviceManager`, `private final PromotionManager promotionManager`.
  - Phương thức: `createInvoice(String customerId, String appointmentId)`, `addSellable(String invoiceId, Sellable item, int quantity)`, `applyPromotion(String invoiceId, String code)`, `removeItem(String invoiceId, String sellableId)`, `finalize(String invoiceId)`.
- **`PromotionService`**
  - Thuộc tính: `private final PromotionManager promotionManager`.
  - Phương thức: `create(Promotion)`, `update(Promotion)`, `deactivate(String id)`, `validateCode(String code, BigDecimal subtotal)`, `markUsage(String code)`.
- **`PaymentService`**
  - Thuộc tính: `private final TransactionManager transactionManager`, `private final InvoiceManager invoiceManager`.
  - Phương thức: `processPayment(String invoiceId, PaymentMethod method, BigDecimal amount)`, `recordTransaction(Transaction transaction)`, `refund(String transactionId, BigDecimal amount)`, `listTransactionsByStatus(TransactionStatus status)`.
- **`AuthService`** *(Singleton dự kiến)*
  - Thuộc tính: `private static AuthService instance`, `private final EmployeeManager employeeManager`, `private final AuthSessionManager authSessionManager`, `private AuthSession currentSession`.
  - Phương thức: `public static AuthService getInstance(EmployeeManager, AuthSessionManager)`, `login(String username, String password)`, `logout()`, `getCurrentSession()`, `hasRole(EmployeeRole role)`, `refreshSession()`.

## 4. Yêu cầu Giao diện (Interfaces)
- Không phát sinh interface mới; các service hoạt động trực tiếp với manager.
- Ghi chú: nếu cần test hoặc hoán đổi implementation, có thể cân nhắc interface `IService` ở phase sau.

## 5. Yêu cầu Đọc/Ghi File
- Service không trực tiếp đọc/ghi file; trách nhiệm thuộc Phase 10 (FileHandler).
- Cần chuẩn bị phương thức để service kích hoạt lưu/đọc thông qua FileHandler khi được tích hợp (ví dụ `saveAll()` sẽ được định nghĩa sau trong UI hoặc lớp điều phối).

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- Mọi thao tác phải kiểm tra trạng thái `isDeleted`, `status`, `stockQuantity` trước khi xử lý.
- Không được tự ý thay đổi dữ liệu mảng trong manager; luôn thông qua phương thức CRUD sẵn có (`add`, `update`, `removeById`).
- Service không trả về mảng nội bộ của manager; phải sử dụng bản sao hoặc dữ liệu đã xử lý.
- `AuthService` phải quản lý `AuthSession` duy nhất tại một thời điểm và đảm bảo phiên hết hạn được thu hồi.
- Ngoại lệ duy nhất cho tương tác service-service là `AppointmentService` gọi `TechnicianAvailabilityService`; các service khác không được gọi lẫn nhau để giữ SRP.
- Cần ghi chú các hằng số hoặc giới hạn (ví dụ số lần retry đăng nhập) bằng biến `static final` khi triển khai.
