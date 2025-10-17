# 📋 TRIỂN KHAI KẾ HOẠCH 0009 - XÂY DỰNG INPUT/OUTPUT MANAGER - HOÀN THÀNH

## ✅ Tóm Tắt Các File Được Tạo/Thay Đổi

### 🆕 File TẠO MỚI:

#### 1. **`src/main/java/io/InputHandler.java`** - 410 dòng

- `readString(prompt)` - Đọc chuỗi từ người dùng
- `readInt(prompt)` - Đọc số nguyên với retry (max 3 lần)
- `readDouble(prompt)` - Đọc số thực với validation
- `readBigDecimal(prompt)` - Đọc số tiền (BigDecimal)
- `readLocalDate(prompt)` - Đọc ngày (dd/MM/yyyy) với validation
- `readLocalDateTime(prompt)` - Đọc ngày giờ (dd/MM/yyyy HH:mm)
- `readBoolean(prompt)` - Đọc yes/no, y/n, true/false
- `readOption(prompt, options[])` - Đọc lựa chọn từ mảy tùy chọn
- `readEmail(prompt)` - Đọc email với regex validation
- `readPhoneNumber(prompt)` - Đọc số điện thoại (0XXXXXXXXX hoặc +84XXXXXXXXX)
- `close()` - Đóng Scanner

#### 2. **`src/main/java/io/OutputFormatter.java`** - 480 dòng

- `printHeader(title)` - In tiêu đề section
- `printSeparator()` - In đường phân cách
- `printSuccess(message)` - In thông báo thành công (màu xanh)
- `printError(message)` - In thông báo lỗi (màu đỏ)
- `printWarning(message)` - In cảnh báo (màu vàng)
- `printBox(message)` - In message trong khung
- `printCustomerInfo(customer)` - In thông tin khách hàng
- `printServiceInfo(service)` - In thông tin dịch vụ
- `printAppointmentInfo(appointment)` - In thông tin lịch hẹn
- `printInvoiceInfo(invoice)` - In thông tin hóa đơn (định dạng đẹp)
- `printTransactionInfo(transaction)` - In thông tin giao dịch
- `printCustomerList(Customer[])` - In bảng danh sách khách hàng (trả về mảy)
- `printServiceList(Service[])` - In bảng danh sách dịch vụ (trả về mảy)
- `printAppointmentList(Appointment[])` - In bảng lịch hẹn (trả về mảy)
- `printDiscountList(Discount[])` - In bảng chiết khấu (trả về mảy)
- Utility methods: `formatCurrency()`, `truncate()`, etc.

#### 3. **`src/main/java/io/FileHandler.java`** - 330 dòng

- `ensureFileExists(filename)` - Tạo file & folder nếu chưa tồn tại
- `writeToFile(filename, content, append)` - Viết dữ liệu vào file
- `readFromFile(filename)` - Đọc nội dung file
- `deleteFile(filename)` - Xóa file
- `fileExists(filename)` - Kiểm tra file có tồn tại
- `backupFile(filename)` - Backup file (rename thành .bak)
- `clearFile(filename)` - Xóa sạch nội dung file
- `escapeCSVField(field)` - Escape CSV field (thêm quote nếu cần)
- `createCSVHeader(columnNames[])` - Tạo header CSV từ mảy
- `createCSVRow(values[])` - Tạo row CSV từ mảy
- `exportToCSV(filename, headers[], dataRows[][])` - Export dữ liệu ra CSV
- `generateBackupFilename(baseFilename)` - Tạo tên file backup với timestamp

### ✏️ File THAY ĐỔI:

#### **`src/main/java/io/Init.java`** - 50 dòng

- `initInputHandler()` - Khởi tạo InputHandler
- `initOutputFormatter()` - Khởi tạo OutputFormatter
- `getFileHandlerClass()` - Lấy class FileHandler (static utility)
- `initAllIOHandlers()` - Khởi tạo tất cả IO handlers cùng lúc (trả về Object[])

---

## 🎯 Tuân Thủ Tất Cả Yêu Cầu

| Yêu Cầu                  | ✅ Tuân Thủ | Chi Tiết                                                                                                       |
| ------------------------ | ----------- | -------------------------------------------------------------------------------------------------------------- |
| **Mảy T[], không List**  | ✅          | InputHandler nhận mảy options, OutputFormatter in mảy items (Customer[], Service[], Appointment[], Discount[]) |
| **Clean Code**           | ✅          | Tên rõ ràng, phương thức ngắn, không lặp code                                                                  |
| **Google Java Style**    | ✅          | 4 khoảng trắng, dấu {}, import sạch, max 100 ký tự/dòng                                                        |
| **Comment Tiếng Việt**   | ✅          | Javadoc + comment logic phức tạp 100%                                                                          |
| **Validation Input**     | ✅          | Regex cho email & phone, retry tối đa 3 lần, format date kiểm tra                                              |
| **OutputFormatter**      | ✅          | Hiển thị mảy, không lưu dữ liệu, định dạng đẹp mắt                                                             |
| **FileHandler**          | ✅          | CSV format (không JSON), static methods, escape CSV fields                                                     |
| **Dependency Injection** | ✅          | InputHandler có constructor tùy chỉnh Scanner (cho testing)                                                    |
| **ANSI Colors**          | ✅          | Màu cho success (xanh), error (đỏ), warning (vàng)                                                             |
| **No Compile Errors**    | ✅          | Build clean, không có lỗi                                                                                      |

---

## 🔄 Chi Tiết Các Phương Thức

### InputHandler

#### Đọc Cơ Bản

```java
String name = inputHandler.readString("Nhập tên: ");
int age = inputHandler.readInt("Nhập tuổi: ");
double price = inputHandler.readDouble("Nhập giá: ");
BigDecimal amount = inputHandler.readBigDecimal("Nhập tiền: ");
```

#### Đọc Ngày/Giờ

```java
LocalDate date = inputHandler.readLocalDate("Nhập ngày sinh");        // dd/MM/yyyy
LocalDateTime datetime = inputHandler.readLocalDateTime("Nhập thời gian"); // dd/MM/yyyy HH:mm
```

#### Đọc Yes/No

```java
boolean agree = inputHandler.readBoolean("Bạn đồng ý?"); // y/n
```

#### Đọc Email & Điện Thoại

```java
String email = inputHandler.readEmail("Nhập email: ");           // regex validation
String phone = inputHandler.readPhoneNumber("Nhập SĐT: ");       // 0XXXXXXXXX
```

#### Đọc Lựa Chọn

```java
String[] options = {"Tùy chọn 1", "Tùy chọn 2", "Tùy chọn 3"};
String choice = inputHandler.readOption("Chọn tùy chọn:", options);
// Output:
// Chọn tùy chọn:
//   1. Tùy chọn 1
//   2. Tùy chọn 2
//   3. Tùy chọn 3
// Chọn (1-3): _
```

### OutputFormatter

#### In Thông Tin Entity

```java
OutputFormatter out = new OutputFormatter();
out.printCustomerInfo(customer);
out.printServiceInfo(service);
out.printAppointmentInfo(appointment);
out.printInvoiceInfo(invoice);
out.printTransactionInfo(transaction);
```

#### In Danh Sách (Mảy)

```java
out.printCustomerList(customerArray);          // In bảng khách hàng
out.printServiceList(serviceArray);            // In bảng dịch vụ
out.printAppointmentList(appointmentArray);    // In bảng lịch hẹn
out.printDiscountList(discountArray);          // In bảng chiết khấu
```

#### In Thông Báo

```java
out.printSuccess("Thao tác thành công!");      // ✅ (xanh)
out.printError("Lỗi xảy ra!");                 // ❌ (đỏ)
out.printWarning("Cảnh báo!");                 // ⚠️  (vàng)
out.printBox("Hộp thông báo");                 // Khung đơn giản
```

### FileHandler

#### Viết/Đọc File

```java
FileHandler.writeToFile("data.txt", "Nội dung", false);  // Ghi đè
FileHandler.writeToFile("data.txt", "Thêm dòng", true);   // Append
String content = FileHandler.readFromFile("data.txt");
```

#### Backup File

```java
FileHandler.backupFile("data.txt");              // Rename → data.txt.bak
String backupName = FileHandler.generateBackupFilename("data.txt");
// Output: data_1697520000000.txt.bak
```

#### Export CSV

```java
String[] headers = {"ID", "Tên", "Email"};
String[][] data = {
    {"1", "Nguyễn Văn A", "a@example.com"},
    {"2", "Trần Thị B", "b@example.com"}
};
FileHandler.exportToCSV("customers.csv", headers, data);
```

---

## 📊 Thống Kê

| Metric             | Số lượng                                  |
| ------------------ | ----------------------------------------- |
| File tạo mới       | 3                                         |
| File thay đổi      | 1                                         |
| Tổng dòng code     | ~1,270 dòng                               |
| Phương thức public | 38                                        |
| Retry tối đa input | 3 lần                                     |
| Regex validation   | 2 (email, phone)                          |
| ANSI colors        | 6 (RESET, GREEN, RED, YELLOW, BLUE, CYAN) |

---

## ✨ Điểm Nổi Bật

### InputHandler

- ✅ **Retry logic** - Tối đa 3 lần nếu input lỗi
- ✅ **Format flexible** - Hỗ trợ ngày, giờ, tiền, Boolean
- ✅ **Validation mạnh** - Email & phone qua regex
- ✅ **Mảy tùy chọn** - readOption() nhận mảy String[] (không List)
- ✅ **Testing-friendly** - Constructor tùy chỉnh Scanner

### OutputFormatter

- ✅ **In mảy** - printCustomerList(), printServiceList(), etc. → nhận mảy T[]
- ✅ **Bảng đẹp** - Box drawing characters (┌, ─, ┬, ├, etc.)
- ✅ **Màu ANSI** - Thông báo success (xanh), error (đỏ), warning (vàng)
- ✅ **Format tiền** - Với dấu phân cách hàng nghìn
- ✅ **Cắt ngắn text** - Truncate text dài quá

### FileHandler

- ✅ **CSV support** - Escape quote, separator
- ✅ **Backup logic** - Tự động backup với timestamp
- ✅ **Folder auto-create** - Tạo thư mục nếu chưa tồn tại
- ✅ **Static methods** - Không cần khởi tạo instance
- ✅ **Mảy dữ liệu** - exportToCSV() nhận String[][] (không List<List>)

---

## 🚀 Ví Dụ Sử Dụng Toàn Bộ

```java
// Khởi tạo
InputHandler input = io.Init.initInputHandler();
OutputFormatter output = io.Init.initOutputFormatter();

// Đầu vào
String name = input.readString("Tên khách hàng: ");
String phone = input.readPhoneNumber("Số điện thoại: ");
String email = input.readEmail("Email: ");
BigDecimal amount = input.readBigDecimal("Số tiền: ");
LocalDate date = input.readLocalDate("Ngày giao dịch");

// Xử lý...
Customer customer = new Customer(...);

// Đầu ra
output.printHeader("THÔNG TIN KHÁCH HÀNG");
output.printCustomerInfo(customer);
output.printSuccess("Lưu thông tin thành công!");

// Export dữ liệu
String[] headers = {"ID", "Tên", "SĐT", "Tier"};
String[][] rows = {
    {customer.getId(), customer.getFullName(), customer.getPhoneNumber(), customer.getMemberTier().toString()}
};
FileHandler.exportToCSV("customers.csv", headers, rows);
```

---

## 📌 Lưu Ý Kỹ Thuật

1. **InputHandler retry logic**

   - Mỗi loại dữ liệu cho phép retry tối đa 3 lần
   - Nếu vượt quá, trả về default value (0, "", false, ngày hôm nay, etc.)

2. **OutputFormatter nhận mảy**

   - Tất cả phương thức printXxxList() nhận mảy T[], không List
   - Kiểm tra null và length == 0 trước khi in

3. **FileHandler static**

   - Tất cả phương thức là static (utility class)
   - Không cần khởi tạo instance
   - io.Init.getFileHandlerClass() chỉ dùng cho reference

4. **CSV Escape**

   - Nếu field chứa dấu phẩy, newline, hoặc quote → đặt trong quote
   - Quote được escape bằng cách nhân đôi: " → ""

5. **ANSI Colors**
   - Có thể disable nếu console không hỗ trợ (Windows Command Prompt)
   - Nên test trên PowerShell hoặc Git Bash

---

## ✅ Compliance Checklist

- ✅ Không dùng List/ArrayList - sử dụng mảy T[] (readOption, printXxxList)
- ✅ Clean Code - tên rõ ràng, phương thức ngắn (SRP)
- ✅ Google Java Style - 4 spaces, dấu {}, import sạch
- ✅ Comment Tiếng Việt - Javadoc 100%
- ✅ Validation Input - regex, retry, format check
- ✅ Error Handling - try-catch, kiểm tra null
- ✅ No Compile Errors - build clean
- ✅ ANSI Colors - thêm màu cho UI thân thiện

---

**✨ PLAN 0009 TRIỂN KHAI HOÀN THÀNH! ✨**
