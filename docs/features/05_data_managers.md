# Phase 5 – Lớp Quản Lý Dữ Liệu (Managers)

## 1. Mô tả Ngữ cảnh
- Hiện thực các lớp `*Manager` kế thừa `BaseManager<T>` để quản lý toàn bộ thực thể đã định nghĩa ở các phase trước.
- Đảm bảo mọi thao tác CRUD dùng mảng thuần (`T[]`) với biến đếm, tuân thủ triết lý KISS và SRP: mỗi manager chỉ xử lý lưu trữ, không chứa logic nghiệp vụ/UI.
- Chuẩn bị dữ liệu tin cậy cho các lớp Service (Phase 6) và đảm bảo tính mở rộng cho đọc/ghi file ở Phase 10.

## 2. Cấu trúc Lớp Model (Entities/POJO)
- Phase này **không** tạo thêm model mới; tái sử dụng các lớp đã thiết kế:
  - `Person`, `Customer`, `Receptionist`, `Technician`.
  - `Service`, `Product`, `GoodsReceipt`, `GoodsReceiptItem`.
  - `Appointment`, `Invoice`, `InvoiceItem`, `Promotion`, `Transaction`, `AuthSession`.
- Tất cả đều triển khai `IEntity`, bảo đảm tương thích với `BaseManager<T>`.

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
### 3.1 Yêu cầu chung
- Mỗi manager kế thừa `BaseManager<T>` và định nghĩa:
  - Mảng nội bộ: `private T[] items` (được nhận từ `BaseManager`).
  - Biến đếm `protected int count` (kế thừa).
  - Hằng số kích thước: `private static final int MAX_SIZE` hoặc `DEFAULT_CAPACITY` tùy nhu cầu.
- Override `createArray(int size)` để cấp phát mảng kiểu cụ thể.
- Bổ sung phương thức tìm kiếm/lọc đặc thù theo nghiệp vụ.

### 3.2 Danh sách manager cụ thể
- **`CustomerManager extends BaseManager<Customer>`**
  - `private static final int DEFAULT_CAPACITY = 50`.
  - Phương thức: `findByTier(Tier tier)`, `findByPhone(String phone)`, `findActiveLoyaltyCustomers(int minPoints)`.
- **`EmployeeManager extends BaseManager<Employee>`**
  - `private static final int DEFAULT_CAPACITY = 30`.
  - Phương thức: `findByRole(EmployeeRole role)`, `findByCode(String code)`, `findAvailableTechnicians()` (lọc `EmployeeStatus` và loại `Technician`).
- **`ServiceManager extends BaseManager<Service>`**
  - `private static final int DEFAULT_CAPACITY = 40`.
  - Phương thức: `listByCategory(ServiceCategory category)`, `searchByName(String keyword)`, `findActiveServices()`.
- **`ProductManager extends BaseManager<Product>`**
  - `private static final int DEFAULT_CAPACITY = 60`.
  - Phương thức: `findLowStock(int threshold)`, `findByName(String keyword)`, `updateStock(String productId, int delta)`.
- **`GoodsReceiptManager extends BaseManager<GoodsReceipt>`**
  - `private static final int DEFAULT_CAPACITY = 25`.
  - Phương thức: `findUnprocessed()`, `findByDate(LocalDate date)`, `findBySupplier(String supplier)`.
- **`AppointmentManager extends BaseManager<Appointment>`**
  - `private static final int DEFAULT_CAPACITY = 80`.
  - Phương thức: `findByDate(LocalDate date)`, `findByTechnician(String technicianId)`, `findByStatus(AppointmentStatus status)`.
- **`InvoiceManager extends BaseManager<Invoice>`**
  - `private static final int DEFAULT_CAPACITY = 80`.
  - Phương thức: `findByCustomer(String customerId)`, `findUnpaid()`, `findByDateRange(LocalDate start, LocalDate end)`.
- **`PromotionManager extends BaseManager<Promotion>`**
  - `private static final int DEFAULT_CAPACITY = 40`.
  - Phương thức: `findActive()`, `findByCode(String code)`, `findUpcoming(LocalDate today)`.
- **`TransactionManager extends BaseManager<Transaction>`**
  - `private static final int DEFAULT_CAPACITY = 100`.
  - Phương thức: `findByInvoice(String invoiceId)`, `findByStatus(TransactionStatus status)`, `findRecent(LocalDateTime from)`.
- **`AuthSessionManager extends BaseManager<AuthSession>`**
  - `private static final int DEFAULT_CAPACITY = 50`.
  - Phương thức: `findActiveSession(String employeeId)`, `findExpired(LocalDateTime now)`, `terminateAll()`.

## 4. Yêu cầu Giao diện (Interfaces)
- Không tạo interface mới; tất cả managers dựa trên `BaseManager<T>` và các entity triển khai `IEntity`.
- Ghi chú: nếu cần thêm hành vi dùng chung cho Manager (ví dụ `searchByKeyword`) sẽ được cân nhắc ở phase refactor, không nằm trong phạm vi hiện tại.

## 5. Yêu cầu Đọc/Ghi File
- Chưa triển khai IO; mỗi manager nên chuẩn bị phương thức `toArray()` trả về dữ liệu để Phase 10 tuần tự hóa.
- Cần tài liệu hóa định dạng file dự kiến (JSON hoặc CSV) tương ứng với từng manager.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- Tất cả phương thức `find` trả về mảng mới chứa kết quả (không lộ mảng nội bộ).
- `add` phải kiểm tra trùng ID (sử dụng `findIndexById`).
- `removeById` sử dụng `shiftLeftFrom` và đặt phần tử cuối `null` nhằm tránh rò rỉ tham chiếu.
- `update` phải giữ nguyên thứ tự hiện tại trong mảng.
- Manager không xử lý logic nghiệp vụ phức tạp (ví dụ tính tiền, xác thực); nhiệm vụ đó thuộc Phase 6.
- Khi số lượng vượt `capacity`, gọi `ensureCapacity()` để nhân đôi dung lượng, tuyệt đối không chuyển sang Collection.
