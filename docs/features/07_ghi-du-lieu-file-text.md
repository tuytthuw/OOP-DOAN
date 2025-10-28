### 3.1. Mô tả Ngữ cảnh
- Hiện thực chức năng ghi dữ liệu từ các DataStore xuống file văn bản thuần (`.txt`) để bảo toàn thông tin giữa các phiên làm việc.
- Đảm bảo cơ chế ghi thống nhất cho toàn bộ thực thể, xử lý đúng kiểu dữ liệu (chuỗi, số, ngày) và tránh mất dữ liệu khi ghi đè.

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- Không bổ sung lớp model mới; sử dụng các thực thể hiện có (Customer, Service, Product, ...).
- Mỗi thực thể cần cung cấp phương thức hỗ trợ chuyển đổi sang chuỗi văn bản (ví dụ: `toDataLine()` hoặc `String[] exportFields()`), trong đó các trường được ghép bằng ký tự phân tách thống nhất (gợi ý dấu `|`).

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `class DataStore<T>` bổ sung phương thức hỗ trợ ghi file:
  - Thuộc tính hỗ trợ: `protected String dataFilePath;`
  - Phương thức: `protected String convertToLine(T item)` xử lý chuẩn hóa dữ liệu thành chuỗi văn bản với ký tự phân tách thống nhất.
  - `public void writeFile()` triển khai ghi tất cả phần tử đang quản lý.
- Các DataStore chuyên biệt có thể ghi đè `convertToLine` để tùy chỉnh format theo thực thể.

### 3.4. Yêu cầu Giao diện (Interfaces)
- `IDataManager.writeFile()` được DataStore hiện thực đầy đủ.
- Các lớp thực thể có thể triển khai interface bổ sung (ví dụ: `RecordWritable`) nếu cần ràng buộc chặt chẽ hơn.

### 3.5. Yêu cầu Đọc/Ghi File
- Sử dụng `BufferedWriter` và `FileWriter` trong `writeFile()`.
- Đảm bảo mỗi dòng kết thúc bằng `System.lineSeparator()`.
- Chuẩn hóa việc ghi ngày (`LocalDate`, `LocalDateTime`) bằng `DateTimeFormatter` thống nhất (ví dụ: `yyyy-MM-dd` hoặc `yyyy-MM-dd HH:mm`).
- Đối với số thập phân (`BigDecimal`, `double`) sử dụng `String.format(Locale.US, ...)` để tránh dấu phẩy thập phân.

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Ghi dữ liệu chỉ bao gồm các phần tử chưa bị đánh dấu `isDeleted` (nếu có cờ xóa mềm).
- Đảm bảo `writeFile()` được gọi sau khi thực hiện thao tác CRUD quan trọng (thêm, cập nhật, xóa).
- Cung cấp cơ chế sao lưu (ví dụ: ghi file tạm `*.tmp` rồi đổi tên) để tránh mất dữ liệu khi ghi thất bại giữa chừng (tùy chọn nâng cao).
