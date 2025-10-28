### 3.1. Mô tả Ngữ cảnh
- Hoàn thiện bộ API quản lý DataStore để thực hiện đầy đủ CRUD, thống kê và hỗ trợ đa hình khi hiển thị dữ liệu.
- Đảm bảo mọi lớp DataStore chuyên biệt vận hành thống nhất, phục vụ cho UI console và các dịch vụ nghiệp vụ.

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- Không tạo lớp model mới; DataStore thao tác trên các thực thể có sẵn thông qua phương thức `IEntity`.

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `class DataStore<T>` (com.spa.data) mở rộng nội dung:
  - Phương thức CRUD:
    - `public void add(T item)` sử dụng `ensureCapacity()` và gán vào `list[count++]`.
    - `public boolean delete(String id)` xác định `index`, dịch phần tử và giảm `count`.
    - `public void update(String id)` tìm phần tử, gọi `input()` hoặc setter theo yêu cầu.
  - Phương thức truy vấn:
    - `public T findById(String id)` duyệt tuyến tính theo `IEntity.getId()`.
    - `public T[] getAll()` trả về bản sao mảng kích thước `count`.
    - `public void displayList()` lặp từ `0` đến `count - 1` và gọi `display()` (đa hình).
    - `public void generateStatistics()` tùy từng DataStore sẽ ghi đè để đếm số lượng theo điều kiện.
  - Phương thức tiện ích:
    - `protected int indexOf(String id)` trả về chỉ số phần tử hoặc `-1`.
    - `protected void shiftLeftFrom(int index)` để tái sử dụng logic dồn mảng.
    - `protected T createCopy(T item)` (tùy chọn) giúp sao chép khi cần trả về mảng dữ liệu mới mà không lộ tham chiếu nội bộ.

### 3.4. Yêu cầu Giao diện (Interfaces)
- `DataStore<T>` triển khai `IActionManager<T>` và `IDataManager`:
  - Ánh xạ phương thức interface với triển khai cụ thể nêu trên.
  - Cho phép lớp con (ví dụ: `CustomerStore`) ghi đè `generateStatistics()` để cung cấp thống kê đặc thù.

### 3.5. Yêu cầu Đọc/Ghi File
- Chuẩn bị chữ ký phương thức `readFile()` và `writeFile()` trong DataStore để giai đoạn 4 hiện thực, tập trung đọc/ghi văn bản thuần.
- Việc tạo mới đối tượng cụ thể sẽ do các DataStore chuyên biệt đảm nhiệm thông qua phương thức riêng hoặc factory truyền vào từ ngoài.
- Xác định cơ chế ánh xạ field ID sang thực thể liên kết (sẽ xử lý ở giai đoạn nghiệp vụ phức tạp sau).

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Bảo đảm `delete` trả về `true/false` để UI hiển thị kết quả cho người dùng.
- Đảm bảo `update` sử dụng đa hình: các lớp thực thể cụ thể sẽ chịu trách nhiệm thu thập dữ liệu mới qua `input()`.
- `generateStatistics()` cơ bản đếm `count`; lớp con có thể ghi đè để thống kê doanh thu, số lượng đơn hoàn thành.
