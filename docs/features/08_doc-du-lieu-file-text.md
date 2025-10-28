### 3.1. Mô tả Ngữ cảnh
- Tải dữ liệu đã lưu vào DataStore khi ứng dụng khởi động để tái sử dụng thông tin khách hàng, dịch vụ, sản phẩm, v.v.
- Đảm bảo tái tạo chính xác đối tượng từ dòng văn bản đã lưu (các trường phân tách bằng ký tự thống nhất), bao gồm cả quan hệ khóa ngoại (ví dụ: liên kết tới Service hoặc Customer khác).

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- Không cần lớp mới; các thực thể hiện hữu phải có constructor đầy đủ tham số để khởi tạo từ dữ liệu file.
- Có thể bổ sung phương thức hỗ trợ như `public void loadFromFields(String[] fields)` nếu cần tái sử dụng.

- `class DataStore<T>` mở rộng:
  - Phương thức `public void readFile()` đọc toàn bộ file và khởi tạo lại mảng `list`.
  - `protected T parseLine(String line)` chịu trách nhiệm chuyển một dòng văn bản thành đối tượng.
  - Nhận `RecordFactory<T>` (hoặc functional interface tương đương) thông qua constructor để tạo instance mới mà không cần phản chiếu.
- Lớp con của DataStore hoặc factory bên ngoài xử lý đặc thù (ví dụ: khởi tạo `ServiceCategory` từ chuỗi, ánh xạ `Supplier`).

### 3.4. Yêu cầu Giao diện (Interfaces)
- `IDataManager.readFile()` được DataStore hiện thực.
- Nếu cần, định nghĩa interface `CsvParsable` cho thực thể để tách trách nhiệm (mỗi lớp tự parse).

### 3.5. Yêu cầu Đọc/Ghi File
- Sử dụng `BufferedReader` và `FileReader`.
- Bỏ qua dòng trống và bỏ qua dòng comment (nếu được định nghĩa bằng ký tự `#`).
- Áp dụng `String.split(",")` với số trường kỳ vọng; kiểm tra dữ liệu thiếu.
- Ánh xạ `LocalDate` và `LocalDateTime` bằng `DateTimeFormatter` thống nhất với giai đoạn 4.A.
- Xử lý ngoại lệ bằng cách ghi log (System.err) nhưng không làm dừng chương trình.

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Sau khi đọc xong file, cập nhật `count` bằng số phần tử hợp lệ đã nạp.
- Hỗ trợ nạp dữ liệu danh mục (enum) bằng `Enum.valueOf(...)`.
- Đảm bảo quan hệ khóa ngoại: ví dụ `Invoice` cần truy xuất `customerId`, `serviceId` để liên kết lại đối tượng từ DataStore tương ứng (cần hỗ trợ từ giai đoạn tiếp theo để binding sau).
