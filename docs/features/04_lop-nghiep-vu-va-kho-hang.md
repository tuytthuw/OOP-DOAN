### 3.1. Mô tả Ngữ cảnh
- Mở rộng mô hình dữ liệu để bao phủ các thực thể nghiệp vụ (dịch vụ, sản phẩm, khuyến mãi, hóa đơn, thanh toán, lịch hẹn, nhà cung cấp, phiếu nhập kho).
- Chuẩn bị hạ tầng cho quản lý kho hàng và tính toán doanh thu thông qua đa hình của `Sellable` và mảng thô trong `DataStore<T>`.

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- `class Service`:
  - Thuộc tính: `serviceId`, `serviceName`, `basePrice`, `durationMinutes`, `bufferTime`, `description`, `createdDate`, `isActive`, `category`.
  - Thực thi: `IEntity`, `Sellable`.
  - Ghi đè: `getBasePrice()`, `calculateFinalPrice()`, `getType()`, `display()`, `input()`.
- `class Product`:
  - Thuộc tính: `productId`, `productName`, `brand`, `basePrice`, `costPrice`, `unit`, `supplier`, `stockQuantity`, `expiryDate`, `isDeleted`, `reorderLevel`.
  - Thực thi: `IEntity`, `Sellable`.
  - Ghi đè: `calculateFinalPrice()`, `display()`, `input()`.
- `class Promotion`:
  - Thuộc tính: `promotionId`, `name`, `description`, `discountType`, `discountValue`, `startDate`, `endDate`, `minPurchaseAmount`, `isDeleted`.
  - Thực thi: `IEntity`.
  - Phương thức: `isValid()`, `calculateDiscount(double totalAmount)`.
- `class Appointment`:
  - Thuộc tính: `appointmentId`, `customer`, `technician`, `service`, `startTime`, `endTime`, `notes`, `status`, `rating`, `feedback`.
  - Thực thi: `IEntity`.
  - Phương thức: `schedule()`, `start()`, `cancel()`, `complete()`, `submitFeedback(int rating, String feedback)`.
- `class Invoice`:
  - Thuộc tính: `invoiceId`, `customer`, `receptionist`, `appointment`, `promotion`, `productList`, `totalAmount`, `status`, `taxRate`, `serviceChargeRate`.
  - Thực thi: `IEntity`.
  - Phương thức: `addProduct(Product item)`, `calculateTotal()`, `applyTaxAndCharge()`.
- `class Payment`:
  - Thuộc tính: `paymentId`, `invoice`, `amount`, `paymentMethod`, `receptionist`, `paymentDate`.
  - Thực thi: `IEntity`.
  - Phương thức: `processPayment()`.
- `class Supplier`:
  - Thuộc tính: `supplierId`, `supplierName`, `contactPerson`, `phoneNumber`, `address`, `email`, `notes`, `isDeleted`.
  - Thực thi: `IEntity`.
- `class GoodsReceipt`:
  - Thuộc tính: `receiptId`, `receiptDate`, `employee`, `supplier`, `receivedProducts`, `totalCost`, `notes`, `warehouseLocation`.
  - Thực thi: `IEntity`.
  - Phương thức: `addProduct(Product item)`, `calculateTotalCost()`, `processReceipt()`.

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `DataStore<Service>` quản lý dữ liệu dịch vụ.
- `DataStore<Product>` quản lý dữ liệu sản phẩm.
- `DataStore<Promotion>`, `DataStore<Appointment>`, `DataStore<Invoice>`, `DataStore<Payment>`, `DataStore<Supplier>`, `DataStore<GoodsReceipt>` tương ứng.
- Mỗi DataStore duy trì mảng thô `T[] list`, `int count`, hằng `DEFAULT_CAPACITY` và triển khai đầy đủ `IActionManager<T>`, `IDataManager`.

### 3.4. Yêu cầu Giao diện (Interfaces)
- `Sellable` áp dụng cho `Service` và `Product` để thống nhất xử lý giá.
- `IEntity` đảm bảo mọi lớp nghiệp vụ hiển thị/nhập liệu nhất quán.
- `IActionManager<T>` và `IDataManager` cung cấp hợp đồng CRUD và file cho toàn bộ DataStore chuyên biệt.

### 3.5. Yêu cầu Đọc/Ghi File
- Mỗi loại thực thể dùng file `.txt`, mỗi dòng chứa bản ghi với ký tự phân tách thống nhất (gợi ý dấu `|`) và thứ tự trường cố định.
- `DataStore<Service>` và `DataStore<Product>` cần chuẩn hóa việc chuyển đổi `BigDecimal`/`double` sang chuỗi và ngược lại.
- Các DataStore khác lưu trữ quan hệ bằng ID (ví dụ: `invoice` ghi `customerId`, `receptionistId`, `appointmentId`).

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- `Service` và `Product` cung cấp `calculateFinalPrice()` phục vụ tính tổng hóa đơn kết hợp khuyến mãi.
- `Invoice` dùng `productList` (mảng thô) để gộp sản phẩm kèm dịch vụ truy xuất từ `DataStore<Service>`.
- `GoodsReceipt` cập nhật `Product.stockQuantity` và tính `totalCost` dựa trên danh sách sản phẩm nhận.
- `Promotion` kiểm tra hiệu lực theo ngày và giá trị đơn hàng để áp dụng giảm giá cho `Invoice` và `Service`.
