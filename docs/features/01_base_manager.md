# Phase 1 – Lõi Dữ Liệu BaseManager

## 1. Mô tả Ngữ cảnh
- Thiết lập nền tảng quản lý dữ liệu dùng chung cho toàn bộ lớp `*Manager` của hệ thống quản lý spa.
- Bảo đảm tuân thủ quy tắc chỉ dùng mảng (`T[]`) và biến đếm, tách bạch hoàn toàn giữa tầng dữ liệu, nghiệp vụ và UI.
- Cung cấp khung CRUD nhất quán để các phase sau chỉ cần bổ sung logic đặc thù cho từng thực thể.

## 2. Cấu trúc Lớp Model (Entities/POJO)
### 2.1 Hợp đồng trừu tượng
- **`interface IEntity`**
  - **Phương thức:** `String getId()` nhằm cung cấp khóa định danh duy nhất cho mọi thực thể.

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
### 3.1 Lớp trừu tượng cốt lõi
- **`abstract class BaseManager<T extends IEntity>`**
  - **Thuộc tính dữ liệu:**
    - `protected T[] items` – mảng lưu trữ nội bộ.
    - `protected int count` – số phần tử hiện hữu.
    - `protected int capacity` – kích thước hiện tại của mảng.
    - `protected static final int DEFAULT_CAPACITY` – hằng số khởi tạo tối thiểu (ví dụ 10) để tránh magic number.
  - **Khởi tạo:**
    - `protected BaseManager(int initialCapacity)` – thiết lập mảng, `count = 0`, kiểm soát tối thiểu dung lượng.
  - **Phương thức bảo vệ (protected):**
    - `protected abstract T[] createArray(int size)` – cho phép lớp con khởi tạo mảng kiểu cụ thể.
    - `protected void ensureCapacity()` – mở rộng dung lượng khi đạt ngưỡng.
    - `protected void shiftLeftFrom(int index)` – dồn phần tử sau khi xóa.
  - **Phương thức công khai (CRUD hợp đồng):**
    - `public void add(T item)` – thêm thực thể hợp lệ.
    - `public void update(T item)` – thay thế thực thể theo `id`.
    - `public boolean removeById(String id)` – xóa mềm bằng cách dồn mảng và giảm `count`.
    - `public T getById(String id)` – truy xuất một phần tử.
    - `public T[] getAll()` – trả về bản sao chứa `count` phần tử.
    - `public int findIndexById(String id)` – tiện ích hỗ trợ tìm kiếm tuyến tính.

## 4. Yêu cầu Giao diện (Interfaces)
- `IEntity` là chuẩn bắt buộc cho mọi lớp domain, bảo đảm `BaseManager` hoạt động độc lập với từng loại thực thể.
- Các lớp trừu tượng hoặc cụ thể trong phase kế tiếp phải triển khai `getId()` thống nhất.

## 5. Yêu cầu Đọc/Ghi File
- Phase 1 chỉ xây khung dữ liệu nội bộ, chưa triển khai đọc/ghi file.
- Ghi chú trong tài liệu kỹ thuật rằng `BaseManager` sẽ tích hợp với `FileHandler` (Phase 10) thông qua các hook được bổ sung sau.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- Mọi thao tác thêm/xóa phải duy trì tính toàn vẹn mảng, không sử dụng Collection từ `java.util`.
- `add` phải từ chối thực thể null hoặc trùng `id`; các phương thức nhận `id` phải kiểm tra chuỗi rỗng.
- `getAll` không được trả về mảng nội bộ để tránh sửa đổi từ bên ngoài.
- Các lớp con chỉ bổ sung kiểm tra nghiệp vụ đặc thù (ví dụ: hợp lệ hóa dữ liệu khách hàng); logic quản lý mảng giữ nguyên trong `BaseManager` để bảo đảm SRP.
