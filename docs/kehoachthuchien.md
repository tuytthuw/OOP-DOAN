
---

### 🚀 Pha 1: Lõi Dữ Liệu (`BaseManager`)
* Tạo `abstract class BaseManager<T>` (với `T[] items`, `int count`).
* Triển khai logic CRUD cốt lõi: `add` (nới rộng mảng), `removeById` (dồn mảng).
* Định nghĩa `interface IEntity` với `getId()` để `BaseManager` sử dụng.

---
### 🧍 Pha 2: Mô Hình Hóa Con Người
* Tạo `abstract class Person`, `abstract class Employee`.
* Tạo các lớp con `Customer`, `Receptionist`, `Technician`.
* Triển khai phương thức `abstract calculatePay()` (Đa hình).

---
### 🛍️ Pha 3: Mô Hình Hóa Hàng Hóa & Kho
* Định nghĩa `interface Sellable` (với `getPrice()`, `isAvailable()`).
* Tạo các lớp `Service`, `Product`.
* Tạo model `GoodsReceipt` (phiếu nhập kho) và `GoodsReceiptItem`.

---
### 🗓️ Pha 4: Mô Hình Hóa Nghiệp Vụ
* Tạo model `Appointment`.
* Tạo các model `Invoice`, `InvoiceItem`, `Promotion`.
* Tạo model `Transaction` và `AuthSession`.

---
### 🗃️ Pha 5: Lớp Quản Lý Dữ Liệu (Managers)
* Tạo tất cả các lớp Manager kế thừa `BaseManager` (ví dụ: `CustomerManager`, `ProductManager`, `AppointmentManager`, `InvoiceManager`...).
* Xây dựng các phương thức tìm kiếm tùy chỉnh trong từng Manager (ví dụ: `findByPhone`, `findLowStock`).
* **Kiểm thử nội bộ (Unit Test)** các Manager này với dữ liệu giả lập (chưa cần UI).

---
### 🧠 Pha 6: Lớp Nghiệp Vụ (Services)
* Tạo tất cả các lớp Service (ví dụ: `CustomerService`, `InventoryService`, `AppointmentService`, `InvoiceService`, `AuthService`...).
* Đưa toàn bộ logic nghiệp vụ (business logic) vào đây (ví dụ: `InventoryService.applyReceipt()`).
* Các Service này **chỉ gọi đến các Manager**, không gọi lẫn nhau (trừ trường hợp đặc biệt như `AppointmentService` gọi `TechnicianAvailabilityService`).

---
### 🖥️ Pha 7: Xây Dựng Công Cụ Giao Diện (UI-Tools)
* Tạo `InputHandler` để chuẩn hóa việc nhập liệu (`readString`, `readInt`, `readDecimal`).
* Tạo `OutputFormatter` để chuẩn hóa việc in ra (`printTable`, `printStatus`).
* Xây dựng `MainMenu` (UI chính) làm khung điều hướng.

---
### 🧑‍💼 Pha 8: Xây Dựng Giao Diện Con (Sub-Menus)
* Tạo `CustomerMenu`, `InventoryMenu`, `AppointmentMenu`.
* Kết nối các Menu này với các `Service` tương ứng (ví dụ: `CustomerMenu` gọi `CustomerService`).
* Đảm bảo các Menu này **KHÔNG BAO GIỜ** gọi trực tiếp vào lớp `Manager`.

---
### 💳 Pha 9: Xây Dựng Giao Diện Phức Tạp (Flow-Menus)
* Xây dựng `BillingMenu` (UI Thanh toán).
* Xây dựng luồng (flow) đăng nhập, gọi `AuthService`.
* Tích hợp `BillingMenu` và `AuthService` vào `MainMenu`.

---
### 💾 Pha 10: Hoàn Thiện & Lưu Trữ
* Tạo `FileHandler` với các phương thức `static` để đọc/ghi file text.
* Gọi `loadAllData()` khi khởi động và `saveAllData()` khi thoát chương trình (trong `Main.java`).
* Rà soát lại tất cả các quy tắc (Rule 1-7).