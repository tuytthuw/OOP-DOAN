

## 📅 Kế hoạch Chi tiết Hoàn thành Đồ án (14 giai đoạn)

### Giai đoạn 1: 🏗️ Khởi tạo và Thiết lập Cấu trúc
* **1.A: Thiết lập Nền tảng Dự án**
    * Tạo dự án Java mới (ví dụ: `SpaManagementConsole`).
    * Tạo cấu trúc thư mục (packages): `com.spa.model` (cho các lớp đối tượng), `com.spa.data` (cho `DataStore`), `com.spa.service` (cho `AuthService`, `Validation`), `com.spa.ui` (cho `MenuUI`), và `com.spa.main`.
    * Tạo tất cả các file `Enum` (Kiểu liệt kê) cần thiết: `AppointmentStatus`, `Tier`, `PaymentMethod`, `DiscountType`, `ServiceCategory`.

* **1.B: Định nghĩa Hợp đồng và Khuôn mẫu**
    * Tạo file cho 4 `Interface`: `IEntity`, `Sellable`, `IDataManager`, `IActionManager`.
    * Định nghĩa chính xác tất cả các phương thức trừu tượng (abstract methods) bên trong mỗi interface.
    * Tạo 2 Lớp trừu tượng (Abstract Class): `Person` và `Employee`.
    * [cite_start]Hiện thực các thuộc tính, phương thức chung, và định nghĩa phương thức trừu tượng `public abstract double calculatePay();` trong lớp `Employee`[cite: 60].

---

### Giai đoạn 2: 🧱 Xây dựng các Lớp Đối tượng Cốt lõi
* **2.A: Hiện thực Cây Kế thừa `Person`**
    * Tạo lớp `Customer` kế thừa `Person`.
    * Tạo lớp `Technician` kế thừa `Employee`.
    * Tạo lớp `Receptionist` kế thừa `Employee`.
    * [cite_start]**(Quan trọng - Đa hình)** Ghi đè (Override) phương thức `calculatePay()` trong `Technician` và `Receptionist` với logic tính lương khác nhau (ví dụ: một bên tính hoa hồng, một bên tính thưởng)[cite: 55].

* **2.B: Hiện thực các Lớp Nghiệp vụ và Kho hàng**
    * Tạo các lớp thực thể: `Supplier`, `Promotion`.
    * Tạo các lớp thực thi `Sellable`: `Product` và `Service`.
    * Tạo các lớp xử lý nghiệp vụ: `Appointment`, `Invoice`, `Payment`, `GoodsReceipt`.
    * [cite_start]**(Quan trọng - Yêu cầu)** Đảm bảo *tất cả 15 lớp* đều có ít nhất hai hàm thiết lập (constructor): một hàm không tham số và một hàm đầy đủ tham số[cite: 54].

---

### Giai đoạn 3: ⚙️ Hiện thực hóa Logic Quản lý `DataStore` (2 điểm)
* **3.A: Xử lý Logic Mảng thô (Thêm/Xoá)**
    * Tạo lớp `DataStore<T>` thực thi `IActionManager`, `IDataManager`.
    * Khai báo thuộc tính: `private T[] list;` và `private int count;`.
    * Hiện thực logic `add(T item)`: Kiểm tra mảng đầy (`count == list.length`), nếu đầy thì tạo mảng mới lớn hơn (`list.length * 2`) và dùng `System.arraycopy()` để sao chép dữ liệu cũ qua.
    * Hiện thực logic `delete(String id)`: Tìm `index` của phần tử, sau đó dùng vòng lặp `for` để dịch chuyển (shift) các phần tử từ `index + 1` về bên trái, cuối cùng gán `list[count - 1] = null;` và giảm `count`.

* **3.B: Hoàn thiện các Chức năng Quản lý**
    * Hiện thực `update(String id)`: Tìm phần tử theo `id`, sau đó gọi các phương thức `setter` (hoặc `input()` mới) để cập nhật.
    * Hiện thực `findById(String id)`: Duyệt mảng từ `0` đến `count` và trả về đối tượng nếu tìm thấy.
    * [cite_start]Hiện thực các hàm tìm kiếm và thống kê theo yêu cầu [cite: 35, 36] (ví dụ: `findByName(String name)`, `generateStatistics()` đếm số lượng đối tượng).
    * Hiện thực `displayList()`: Duyệt mảng và gọi phương thức `display()` của từng đối tượng (tận dụng tính đa hình của `IEntity`).

---

### Giai đoạn 4: 💾 Hiện thực hóa Đọc/Ghi File
* **4.A: Logic Ghi Dữ liệu xuống File Text**
    * Trong `DataStore<T>`, hiện thực phương thức `writeFile()`.
    * Sử dụng `FileWriter` và `BufferedWriter` để mở file (ví dụ: `customers.txt`).
    * Duyệt mảng `list` từ `0` đến `count`.
    * Với mỗi đối tượng, chuyển đổi các thuộc tính của nó thành một dòng `String` (định dạng CSV, ví dụ: `CUS001,Nguyen Van A,090...`).
    * Ghi dòng `String` đó vào file, kèm theo ký tự xuống dòng `\n`.

* **4.B: Logic Đọc Dữ liệu từ File Text**
    * Trong `DataStore<T>`, hiện thực phương thức `readFile()`.
    * Sử dụng `FileReader` và `BufferedReader` để đọc file.
    * Sử dụng vòng lặp `while ((line = br.readLine()) != null)` để đọc từng dòng.
    * Với mỗi `line`, dùng `line.split(",")` để tách thành một mảng `String[] properties`.
    * Tạo một đối tượng `T` mới bằng cách gọi hàm thiết lập đầy đủ tham số và ép kiểu các thuộc tính (ví dụ: `Integer.parseInt(properties[2])`).
    * Gọi phương thức `add(newItem)` (đã làm ở Giai đoạn 3.A) để nạp đối tượng vào mảng `list`.

---

### Giai đoạn 5: 🕹️ Xây dựng Dịch vụ Phụ trợ và Giao diện
* **5.A: Xây dựng các Dịch vụ Lõi (Auth & Validation)**
    * [cite_start]Hiện thực lớp `AuthService` theo mẫu Singleton: `private static instance`, `private` constructor, `public static getInstance()`[cite: 59].
    * Thêm các phương thức `login()`, `logout()`, `getCurrentUser()` vào `AuthService`.
    * [cite_start]Tạo một lớp `Validation` chứa các phương thức `static` [cite: 59] (ví dụ: `public static String getString(String prompt)`, `public static int getInt(String prompt, int min, int max)`), xử lý `try-catch` cho `InputMismatchException` bên trong.

* **5.B: Xây dựng Giao diện Console (UI) và Lớp `Main`**
    * Tạo lớp `MenuUI` chịu trách nhiệm `System.out.println()` các menu (ví dụ: `displayMainMenu()`, `displayCustomerMenu()`, `displayProductMenu()`).
    * Tạo lớp `SpaManagementApp` (hoặc `Main`) chứa phương thức `public static void main(String[] args)`.
    * Trong `main`, khởi tạo tất cả các lớp `DataStore` (ví dụ: `DataStore<Customer> customerManager = new DataStore<>()`).
    * **(Quan trọng)** Gọi `customerManager.readFile()`, `productManager.readFile()`, v.v. để tải dữ liệu ngay khi khởi động.
    * Tạo vòng lặp `while(true)` chính của ứng dụng và hiển thị Main Menu.

---

### Giai đoạn 6: 🔄 Tích hợp Hệ thống
* **6.A: Tích hợp các Chức năng CRUD Đơn giản**
    * Sử dụng `switch-case` cho lựa chọn của người dùng trong `main`.
    * Kết nối các mục menu "Thêm/Sửa/Xoá/Xem" với các phương thức tương ứng của `DataStore`.
    * [cite_start]Ví dụ (Case "Thêm Khách hàng"): Gọi `customerManager.input()` (hoặc dùng `Validation` để hỏi từng thuộc tính), tạo đối tượng `Customer`, gọi `customerManager.add(newCustomer)`, và cuối cùng gọi `customerManager.writeFile()` để lưu thay đổi[cite: 58].

* **6.B: Tích hợp các Luồng Nghiệp vụ Phức tạp**
    * Xây dựng chức năng "Tạo Hóa đơn": Yêu cầu người dùng chọn `Customer` (từ `customerManager`), chọn `Service` (từ `serviceManager`), chọn `Product` (từ `productManager`), sau đó tạo đối tượng `Invoice` và tính tổng tiền.
    * Xây dựng chức năng "Tạo Lịch hẹn": Yêu cầu chọn `Customer`, `Technician`, `Service` và thời gian.
    * Xây dựng chức năng "Nhập kho": Yêu cầu chọn `Supplier`, nhập thông tin `Product`, tạo `GoodsReceipt`.

---

### Giai đoạn 7: 🐞 Kiểm thử và Hoàn thiện
* **7.A: Kiểm thử các Yêu cầu Kỹ thuật**
    * [cite_start]**Test `DataStore`[cite: 57]:** Cố tình thêm 11 phần tử vào mảng (giả sử kích thước ban đầu là 10) để kiểm tra xem logic `resize` mảng có chạy đúng không.
    * [cite_start]**Test Đa hình[cite: 55]:** Tạo một mảng `Employee[]` chứa cả `Technician` và `Receptionist`. Duyệt mảng và gọi `emp.calculatePay()`, sau đó in kết quả để chứng minh lương được tính khác nhau.
    * [cite_start]**Test Đọc/Ghi File[cite: 58]:** Khởi động app, thêm 3 khách hàng. Tắt app. Mở lại app và chọn "Hiển thị danh sách khách hàng", đảm bảo 3 khách hàng đó vẫn còn.

* **7.B: Kiểm thử Trải nghiệm Người dùng và Rà soát**
    * **Test `Validation`:** Tại mọi điểm nhập số (`Scanner.nextInt()`), cố tình nhập chữ ("abc") để đảm bảo ứng dụng không bị crash và yêu cầu nhập lại.
    * **Test Toàn bộ luồng:** Thử mọi lựa chọn trong tất cả các menu, đảm bảo không có `NullPointerException` hay `ArrayIndexOutOfBoundsException`.
    * [cite_start]**Rà soát lần cuối:** Mở file `Do an_OOP (2).pdf`, đọc lại Thang điểm [cite: 52-62] và tích vào từng mục (15 class, constructor, đa hình, kế thừa, mảng 2đ, file, static, abstract, interface, sáng tạo) để chắc chắn 100% đạt yêu cầu.