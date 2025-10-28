### 3.1. Mô tả Ngữ cảnh
- Thiết lập khung DataStore tổng quát sử dụng mảng thô `T[]` để quản lý mọi tập dữ liệu trong ứng dụng.
- Đảm bảo cơ chế khởi tạo, mở rộng dung lượng và quản lý chỉ số `count` hoạt động thống nhất cho tất cả loại thực thể.

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- Không bổ sung thực thể mới; DataStore vận hành trên các lớp đã định nghĩa ở giai đoạn 1 và 2 (Customer, Service, Product, ...).

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `class DataStore<T>`:
  - Thuộc tính dữ liệu:
    - `private T[] list;`
    - `private int count;`
    - `private static final int DEFAULT_CAPACITY = 10;`
  - Phương thức khởi tạo:
    - `public DataStore()` tạo mảng mới với kích thước `DEFAULT_CAPACITY` thông qua helper generic an toàn.
    - `public DataStore(int capacity)` cho phép truyền kích thước khởi tạo riêng khi cần (sử dụng cùng helper để xây mảng).
- Thuộc tính `static` bổ sung (nếu cần):
  - `private static final int GROWTH_FACTOR = 2;`

### 3.4. Yêu cầu Giao diện (Interfaces)
- DataStore triển khai `IActionManager<T>` và `IDataManager` (phần phương thức sẽ hoàn thiện trong kế hoạch 3.B).

### 3.5. Yêu cầu Đọc/Ghi File
- Chưa xử lý ở bước này; tập trung vào logic mảng nội bộ (sẽ hoàn thiện ở giai đoạn 4).

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Phương thức nội bộ `ensureCapacity()` kiểm tra `count == list.length` và nhân đôi kích thước mảng.
- Duy trì `count` để chỉ ra số phần tử hợp lệ, không sử dụng cấu trúc mảng động nào khác.
- Đảm bảo `add(T item)` cập nhật `count` sau khi thêm, và `delete(int index)` thực hiện dịch chuyển phần tử để tránh khoảng trống (logic chi tiết sang giai đoạn 3.B).
