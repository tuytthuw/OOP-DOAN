# Phase 3 – Mô Hình Hóa Hàng Hóa & Kho

## 1. Mô tả Ngữ cảnh
- Bổ sung các thực thể phục vụ hoạt động bán dịch vụ/sản phẩm trong spa và quản lý nhập kho.
- Thiết lập interface `Sellable` để đồng nhất hành vi định giá, giúp các phase sau (Invoice, Promotion) khai thác đa hình.
- Chuẩn bị dữ liệu đầu vào cho InventoryService và GoodsReceiptManager ở các phase kế tiếp.

## 2. Cấu trúc Lớp Model (Entities/POJO)
### 2.1 Interface hành vi
- **`interface Sellable`**
  - **Phương thức:**
    - `BigDecimal getPrice()` – giá hiện hành.
    - `String getDescription()` – mô tả ngắn gọn.
    - `boolean isAvailable()` – trạng thái sẵn sàng để bán.

### 2.2 Sản phẩm & Dịch vụ (implements IEntity, Sellable)
- **`class Service implements IEntity, Sellable`**
  - **Thuộc tính:** `String serviceId`, `String serviceName`, `String description`, `BigDecimal basePrice`, `int durationMinutes`, `ServiceCategory category`, `boolean isActive`, `LocalDateTime createdAt`.
  - **Phương thức:**
    - `public String getId()` – trả về `serviceId`.
    - `public BigDecimal getPrice()` – giá cơ bản hoặc giá đã điều chỉnh (nếu có phase sau).
    - `public String getDescription()` – mô tả dịch vụ.
    - `public boolean isAvailable()` – dựa trên `isActive`.
    - `public void updatePrice(BigDecimal newPrice)` – cập nhật giá.
    - `public void toggleActive(boolean active)` – bật/tắt dịch vụ.

- **`class Product implements IEntity, Sellable`**
  - **Thuộc tính:** `String productId`, `String productName`, `String description`, `BigDecimal basePrice`, `int stockQuantity`, `int reorderLevel`, `boolean isActive`, `LocalDateTime lastUpdated`.
  - **Phương thức:**
    - `public String getId()` – trả về `productId`.
    - `public BigDecimal getPrice()` – giá bán lẻ hiện tại.
    - `public String getDescription()` – mô tả sản phẩm.
    - `public boolean isAvailable()` – dựa trên `isActive` và `stockQuantity` > 0.
    - `public void adjustStock(int delta)` – tăng/giảm tồn kho.
    - `public boolean needsRestock()` – so sánh `stockQuantity` với `reorderLevel`.
    - `public void toggleActive(boolean active)` – bật/tắt sản phẩm.

### 2.3 Phiếu nhập kho
- **`class GoodsReceiptItem`**
  - **Thuộc tính:** `String productId`, `String productName`, `int quantity`, `BigDecimal purchasePrice`.
  - **Phương thức:** `public BigDecimal calculateLineTotal()` – giá trị từng dòng.

- **`class GoodsReceipt implements IEntity`**
  - **Thuộc tính:** `String receiptId`, `String supplierName`, `LocalDate receivedDate`, `GoodsReceiptItem[] items`, `boolean processed`, `String note`, `int itemCount`, `static final int MAX_ITEMS`.
  - **Phương thức:**
    - `public String getId()` – trả về `receiptId`.
    - `public void addItem(GoodsReceiptItem item)` – thêm dòng nhập (tự quản lý mảng `items`).
    - `public void removeItem(int index)` – xóa dòng và dồn mảng.
    - `public void markProcessed()` – cập nhật trạng thái xử lý.
    - `public int totalQuantity()` – tổng số lượng.
    - `public BigDecimal totalCost()` – tổng chi phí.

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
- Chuẩn bị yêu cầu để Phase 5 hiện thực các manager:
  - `ServiceManager` sẽ quản lý `Service[] services`, `int count`, `static final int MAX_SERVICES`.
  - `ProductManager` sẽ quản lý `Product[] products`, `int count`, `static final int MAX_PRODUCTS`.
  - `GoodsReceiptManager` sẽ quản lý `GoodsReceipt[] receipts`, `int count`, `static final int MAX_RECEIPTS`.
- Các phương thức dự kiến: `add`, `update`, `removeById`, `findBy...` sẽ sử dụng logic của `BaseManager`.

## 4. Yêu cầu Giao diện (Interfaces)
- `Sellable` áp dụng cho cả `Service` và `Product`, hỗ trợ polymorphism khi tính hóa đơn.
- Duy trì `IEntity` cho tất cả các thực thể lưu vào manager.

## 5. Yêu cầu Đọc/Ghi File
- Chưa triển khai IO trong phase này. Ghi chú: dữ liệu `GoodsReceipt` và `GoodsReceiptItem` cần cấu trúc rõ ràng (ví dụ JSON) để Phase 10 dễ serialize.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- `adjustStock` không cho phép tồn kho âm; phải ràng buộc trong lớp model.
- `addItem` của `GoodsReceipt` phải nới mảng thủ công khi cần (tuân thủ quy tắc array-only).
- `isAvailable` phải phản ánh trạng thái hoạt động và điều kiện tồn kho (đối với sản phẩm).
- `needsRestock` hỗ trợ manager/service cảnh báo nhập hàng.
- Các lớp model không chứa logic IO hoặc UI; chỉ mô tả trạng thái và hành vi nghiệp vụ tối thiểu.
