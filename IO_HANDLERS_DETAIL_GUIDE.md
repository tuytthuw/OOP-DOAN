# 📖 CHI TIẾT TRIỂN KHAI IO HANDLERS - PLAN 0009

## 1️⃣ InputHandler - Xử Lý Input

### Mục đích

Đọc dữ liệu từ người dùng qua console, validate, và xử lý lỗi.

### Đặc điểm Chính

- **Retry logic**: Tối đa 3 lần nếu input lỗi
- **Validation**: Regex cho email & phone, format check cho date/time
- **Flexible**: Hỗ trợ nhiều loại dữ liệu (String, int, double, BigDecimal, LocalDate, LocalDateTime, boolean)
- **Mảy**: `readOption()` nhận mảy String[] (không List)

### Phương thức Chi Tiết

#### `readString(prompt): String`

- Đơn giản nhất - đọc chuỗi, trim khoảng trắng
- Không validate
- Ví dụ:
  ```java
  String name = input.readString("Nhập tên: ");
  // Output: Nhập tên: _
  ```

#### `readInt(prompt): int`

- Retry tối đa 3 lần nếu NumberFormatException
- Trả về 0 nếu vượt quá retry
- Ví dụ:
  ```java
  int age = input.readInt("Nhập tuổi: ");
  // Output: Nhập tuổi: abc
  //        ❌ Vui lòng nhập một số nguyên hợp lệ!
  //        Nhập tuổi: 25
  //        (trả về 25)
  ```

#### `readDouble(prompt): double`

- Tương tự readInt nhưng cho số thực
- Trả về 0.0 nếu lỗi

#### `readBigDecimal(prompt): BigDecimal`

- Dùng cho tiền tệ (độ chính xác cao)
- Trả về BigDecimal.ZERO nếu lỗi
- Ví dụ:
  ```java
  BigDecimal amount = input.readBigDecimal("Nhập tiền: ");
  // Output: Nhập tiền: 500000.50
  //        (trả về BigDecimal("500000.50"))
  ```

#### `readLocalDate(prompt): LocalDate`

- Format: dd/MM/yyyy
- Retry tối đa 3 lần nếu parse fail
- Trả về LocalDate.now() nếu lỗi
- Ví dụ:
  ```java
  LocalDate date = input.readLocalDate("Nhập ngày sinh");
  // Output: Nhập ngày sinh (dd/MM/yyyy): 01/01/1990
  //        (trả về LocalDate.of(1990, 1, 1))
  ```

#### `readLocalDateTime(prompt): LocalDateTime`

- Format: dd/MM/yyyy HH:mm
- Retry & error handling tương tự readLocalDate
- Trả về LocalDateTime.now() nếu lỗi

#### `readBoolean(prompt): boolean`

- Nhận: y/yes/true → true, n/no/false → false
- Case-insensitive (Y, YES, TRUE cũng được)
- Trả về false nếu input không hợp lệ (sau 3 lần)
- Ví dụ:
  ```java
  boolean agree = input.readBoolean("Bạn đồng ý?");
  // Output: Bạn đồng ý? (y/n): yes
  //        (trả về true)
  ```

#### `readOption(prompt, options[]): String`

- **Nhận mảy String[], không List** ✅
- Hiển thị danh sách với số thứ tự
- Retry tối đa 3 lần
- Trả về phần tử đầu tiên nếu lỗi
- Ví dụ:
  ```java
  String[] options = {"Khách hàng", "Nhân viên", "Quản lý"};
  String role = input.readOption("Chọn vai trò:", options);
  // Output:
  //        Chọn vai trò:
  //          1. Khách hàng
  //          2. Nhân viên
  //          3. Quản lý
  //        Chọn (1-3): 2
  //        (trả về "Nhân viên")
  ```

#### `readEmail(prompt): String`

- Regex: `^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$`
- Cho phép email rỗng (return "")
- Retry tối đa 3 lần
- Ví dụ:
  ```java
  String email = input.readEmail("Nhập email: ");
  // Output: Nhập email: user@example.com
  //        (trả về "user@example.com")
  ```

#### `readPhoneNumber(prompt): String`

- Regex: `^(0|\+84)[0-9]{9,10}$`
- Chấp nhận: 0912345678, +84912345678
- Retry tối đa 3 lần
- Trả về "" nếu lỗi
- Ví dụ:
  ```java
  String phone = input.readPhoneNumber("Nhập SĐT: ");
  // Output: Nhập SĐT: 0987654321
  //        (trả về "0987654321")
  ```

---

## 2️⃣ OutputFormatter - Hiển Thị Output

### Mục đích

Định dạng và hiển thị dữ liệu ra console với giao diện đẹp mắt.

### Đặc điểm Chính

- **In mảy**: printXxxList() nhận mảy T[], không List
- **Bảng đẹp**: Box drawing characters (┌, ─, │, ├, etc.)
- **Màu ANSI**: Success (xanh), Error (đỏ), Warning (vàng)
- **Format đẹp**: Tiền tệ, cắt ngắn text, align cột

### Phương thức Chi Tiết

#### `printHeader(title): void`

- In tiêu đề section với khung
- Màu xanh
- Ví dụ:
  ```java
  output.printHeader("QUẢN LÝ KHÁCH HÀNG");
  // Output:
  //        ╔═══════════════════════════╗
  //        ║  QUẢN LÝ KHÁCH HÀNG      ║
  //        ╚═══════════════════════════╝
  ```

#### `printSuccess(message): void`

- In thông báo thành công với ✅ prefix
- Màu xanh
- Ví dụ:
  ```java
  output.printSuccess("Lưu dữ liệu thành công!");
  // Output: ✅ Lưu dữ liệu thành công!  (xanh)
  ```

#### `printError(message): void`

- In thông báo lỗi với ❌ prefix
- Màu đỏ
- Ví dụ:
  ```java
  output.printError("Khách hàng không tồn tại!");
  // Output: ❌ Khách hàng không tồn tại!  (đỏ)
  ```

#### `printWarning(message): void`

- In cảnh báo với ⚠️ prefix
- Màu vàng
- Ví dụ:
  ```java
  output.printWarning("Không có dữ liệu");
  // Output: ⚠️  Không có dữ liệu  (vàng)
  ```

#### `printBox(message): void`

- In message trong khung đơn giản (xanh)
- Ví dụ:
  ```java
  output.printBox("Thao tác hoàn tất");
  // Output:
  //        ┌─────────────────────┐
  //        │ Thao tác hoàn tất   │
  //        └─────────────────────┘
  ```

#### `printCustomerInfo(customer): void`

- In thông tin chi tiết 1 khách hàng
- Hiển thị: ID, Tên, SĐT, Email, Địa chỉ, Tier, Tổng chi tiêu, Trạng thái
- Ví dụ:
  ```java
  output.printCustomerInfo(customer);
  // Output:
  //        ┌─────────────────────────────────────────────┐
  //        │ ID: CUST_123456789                          │
  //        │ Tên: Nguyễn Văn A                           │
  //        │ SĐT: 0987654321                             │
  //        │ Email: a@example.com                        │
  //        │ Địa chỉ: TP. HCM                            │
  //        │ Tier: GOLD                                  │
  //        │ Tổng chi tiêu: 5,000,000 VND               │
  //        │ Trạng thái: Hoạt động                       │
  //        └─────────────────────────────────────────────┘
  ```

#### `printServiceInfo(service): void`

- In thông tin chi tiết 1 dịch vụ
- Hiển thị: ID, Tên, Giá, Thời gian, Loại, Trạng thái

#### `printAppointmentInfo(appointment): void`

- In thông tin chi tiết 1 lịch hẹn
- Hiển thị: ID, Khách, Dịch vụ, Thời gian, Trạng thái, Nhân viên

#### `printInvoiceInfo(invoice): void`

- In thông tin chi tiết 1 hóa đơn
- Định dạng như hóa đơn thực tế
- Hiển thị: Số HĐ, Khách, Lịch hẹn, Ngày phát hành, Subtotal, Chiết khấu, Thuế, Tổng, Trạng thái
- Ví dụ:
  ```java
  output.printInvoiceInfo(invoice);
  // Output:
  //        ╔═════════════════════════════════════════════╗
  //        ║           HÓA ĐƠN DỊCH VỤ SPA              ║
  //        ╠═════════════════════════════════════════════╣
  //        ║ Số hóa đơn: INV_123456                      ║
  //        ║ Khách hàng: CUST_123456                     ║
  //        ║ Lịch hẹn:   APT_123456                      ║
  //        ║ Ngày phát hành: 2024-10-17                  ║
  //        ╠═════════════════════════════════════════════╣
  //        ║ Tổng tiền:         500,000 VND              ║
  //        ║ Chiết khấu:       -50,000 VND               ║
  //        ║ Thuế (10%):        45,000 VND               ║
  //        ╠═════════════════════════════════════════════╣
  //        ║ Tổng cộng:         495,000 VND              ║
  //        ║ Trạng thái: Đã thanh toán                   ║
  //        ║ Phương thức: CASH                           ║
  //        ╚═════════════════════════════════════════════╝
  ```

#### `printTransactionInfo(transaction): void`

- In thông tin chi tiết 1 giao dịch
- Hiển thị: ID, Khách, Số tiền, Phương thức, Trạng thái

#### `printCustomerList(Customer[]): void`

- **Nhận mảy Customer[], không List** ✅
- In bảng danh sách khách hàng
- Hiển thị: ID, Tên, SĐT, Tier
- Ví dụ:
  ```java
  Customer[] customers = {...};
  output.printCustomerList(customers);
  // Output:
  //        ┌────────────────┬──────────────────────┬──────────────────┬──────────────┐
  //        │ ID             │ Tên                  │ Số Điện Thoại    │ Tier         │
  //        ├────────────────┼──────────────────────┼──────────────────┼──────────────┤
  //        │ CUST_123       │ Nguyễn Văn A         │ 0987654321       │ GOLD         │
  //        │ CUST_124       │ Trần Thị B           │ 0912345678       │ SILVER       │
  //        └────────────────┴──────────────────────┴──────────────────┴──────────────┘
  ```

#### `printServiceList(Service[]): void`

- **Nhận mảy Service[], không List** ✅
- In bảng danh sách dịch vụ
- Hiển thị: ID, Tên, Giá, Thời gian

#### `printAppointmentList(Appointment[]): void`

- **Nhận mảy Appointment[], không List** ✅
- In bảng danh sách lịch hẹn
- Hiển thị: ID, Khách, Dịch vụ, Thời gian, Trạng thái

#### `printDiscountList(Discount[]): void`

- **Nhận mảy Discount[], không List** ✅
- In bảng danh sách chiết khấu
- Hiển thị: Mã, Loại, Giá trị, Ngày kết thúc

---

## 3️⃣ FileHandler - Xử Lý File

### Mục đích

Lưu/tải dữ liệu từ file, hỗ trợ CSV format.

### Đặc điểm Chính

- **Static methods**: Không cần khởi tạo instance
- **Auto-create**: Tự động tạo folder nếu chưa tồn tại
- **CSV support**: Escape fields, tạo headers & rows từ mảy
- **Mảy dữ liệu**: exportToCSV() nhận String[][] (không List<List>)
- **Backup logic**: Tạo backup file tự động

### Phương thức Chi Tiết

#### `ensureFileExists(filename): boolean`

- Tạo file & folder nếu chưa tồn tại
- Trả về true nếu thành công
- Ví dụ:
  ```java
  FileHandler.ensureFileExists("output/data.csv");
  // Tạo thư mục "output" nếu chưa có
  // Tạo file "data.csv" nếu chưa có
  ```

#### `writeToFile(filename, content, append): boolean`

- Viết content vào file
- append = false → ghi đè
- append = true → append thêm
- Ví dụ:
  ```java
  FileHandler.writeToFile("log.txt", "User logged in", true);
  // Thêm dòng "User logged in" vào cuối file
  ```

#### `readFromFile(filename): String`

- Đọc toàn bộ nội dung file
- Trả về "" nếu file không tồn tại
- Ví dụ:
  ```java
  String content = FileHandler.readFromFile("data.txt");
  ```

#### `deleteFile(filename): boolean`

- Xóa file
- Trả về true nếu thành công
- Ví dụ:
  ```java
  FileHandler.deleteFile("temp.txt");
  ```

#### `fileExists(filename): boolean`

- Kiểm tra file có tồn tại không
- Ví dụ:
  ```java
  if (FileHandler.fileExists("config.cfg")) {
      // ...
  }
  ```

#### `backupFile(filename): boolean`

- Backup file bằng rename → filename.bak
- Ví dụ:
  ```java
  FileHandler.backupFile("data.csv");
  // Rename: data.csv → data.csv.bak
  ```

#### `clearFile(filename): boolean`

- Xóa sạch nội dung file (file vẫn tồn tại)
- Ví dụ:
  ```java
  FileHandler.clearFile("log.txt");
  // File "log.txt" trở thành rỗng
  ```

#### `generateBackupFilename(baseFilename): String`

- Tạo tên file backup với timestamp
- Ví dụ:
  ```java
  String backupName = FileHandler.generateBackupFilename("data.csv");
  // Output: "data_1697520000000.csv.bak"
  ```

#### `escapeCSVField(field): String`

- Escape CSV field
- Nếu chứa dấu phẩy, newline, hoặc quote → đặt trong quote
- Quote được escape bằng cách nhân đôi
- Ví dụ:

  ```java
  String escaped = FileHandler.escapeCSVField("Hello, World");
  // Output: "Hello, World"  (thêm quote vì có dấu phẩy)

  String escaped2 = FileHandler.escapeCSVField("He said \"Hi\"");
  // Output: "He said ""Hi"""  (escape quote)
  ```

#### `createCSVHeader(columnNames[]): String`

- **Nhận mảy String[], không List** ✅
- Tạo CSV header từ mảy cột
- Ví dụ:
  ```java
  String[] headers = {"ID", "Tên", "Email"};
  String csvHeader = FileHandler.createCSVHeader(headers);
  // Output: "ID,Tên,Email"
  ```

#### `createCSVRow(values[]): String`

- **Nhận mảy String[], không List** ✅
- Tạo CSV row từ mảy giá trị
- Ví dụ:
  ```java
  String[] values = {"1", "Nguyễn Văn A", "a@example.com"};
  String csvRow = FileHandler.createCSVRow(values);
  // Output: "1,Nguyễn Văn A,a@example.com"
  ```

#### `exportToCSV(filename, headers[], dataRows[][]): boolean`

- **Nhận mảy headers[] và dataRows[][], không List** ✅
- Export dữ liệu ra file CSV
- Dòng đầu = header, các dòng tiếp theo = dữ liệu
- Ví dụ:
  ```java
  String[] headers = {"ID", "Tên", "SĐT"};
  String[][] dataRows = {
      {"1", "Nguyễn Văn A", "0987654321"},
      {"2", "Trần Thị B", "0912345678"}
  };
  FileHandler.exportToCSV("customers.csv", headers, dataRows);
  // Output file customers.csv:
  // ID,Tên,SĐT
  // 1,Nguyễn Văn A,0987654321
  // 2,Trần Thị B,0912345678
  ```

---

## 🔄 Flow Sử Dụng Toàn Bộ

```java
// 1. Khởi tạo
InputHandler input = io.Init.initInputHandler();
OutputFormatter output = io.Init.initOutputFormatter();

// 2. In tiêu đề
output.printHeader("ĐĂNG KÝ KHÁCH HÀNG MỚI");

// 3. Nhập thông tin
String name = input.readString("Tên khách hàng: ");
String phone = input.readPhoneNumber("Số điện thoại: ");
String email = input.readEmail("Email (tùy chọn): ");
LocalDate birthDate = input.readLocalDate("Ngày sinh");
boolean isMale = input.readBoolean("Nam?");

// 4. Xử lý (tạo Customer, lưu vào database)
Customer customer = customerService.registerNewCustomer(
    name, phone, email, "", isMale, birthDate
);

// 5. Hiển thị kết quả
if (customer != null) {
    output.printSuccess("Đăng ký thành công!");
    output.printCustomerInfo(customer);

    // 6. Export dữ liệu
    String[] headers = {"ID", "Tên", "SĐT", "Email", "Tier"};
    String[][] data = {{
        customer.getId(),
        customer.getFullName(),
        customer.getPhoneNumber(),
        customer.getEmail(),
        customer.getMemberTier().toString()
    }};

    FileHandler.exportToCSV("new_customer.csv", headers, data);
    output.printSuccess("Đã export ra new_customer.csv");
} else {
    output.printError("Đăng ký thất bại!");
}

// 7. Đóng input
input.close();
```

---

## 📌 Lưu Ý & Best Practices

1. **InputHandler.close()**: Luôn gọi `close()` khi kết thúc để đóng Scanner

   ```java
   input.close();
   ```

2. **ANSI Colors**: Có thể không hoạt động trên Windows Command Prompt

   - Dùng PowerShell hoặc Git Bash để test
   - Nếu không muốn màu, comment ra các ANSI code

3. **CSV Escape**: Luôn escape fields trước khi export

   - FileHandler.createCSVRow() tự động escape
   - Không cần gọi escapeCSVField() riêng

4. **Mảy trong methods**:

   - printXxxList() nhận mảy T[], không List ✅
   - readOption() nhận mảy String[], không List ✅
   - exportToCSV() nhận String[], String[][], không List ✅

5. **Error messages**: Tất cả thông báo lỗi in ra console
   - Không throw exception (user-friendly)
   - Retry tự động nếu input lỗi

---

**✨ IO HANDLERS READY TO USE! ✨**
