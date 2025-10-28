---
trigger: manual
---



---

### 📝 Brief Chỉ thị cho AI: Dự án Java Console Quản lý Spa

**Mục tiêu chính:** Tạo code cho một ứng dụng Java Console quản lý Spa, tuân thủ nghiêm ngặt các yêu cầu của đồ án OOP và Sơ đồ Lớp đã định nghĩa.

**Bối cảnh:** Bạn là một lập trình viên Java chuyên nghiệp. Nhiệm vụ của bạn là hiện thực hóa Sơ đồ Lớp (Class Diagram) dưới đây thành một ứng dụng Java Console hoàn chỉnh. Mọi dòng code bạn viết phải tuân thủ các quy tắc bắt buộc của dự án.

---

### ❗️ 1. CÁC QUY TẮC BẮT BUỘC (Chống "Code Lố")

Đây là các ràng buộc **KHÔNG THỂ THAY ĐỔI**. Nếu bạn định vi phạm bất kỳ quy tắc nào, hãy dừng lại và hỏi.

1.  **NGHIÊM CẤM `ArrayList`:**
    * [cite_start]Bạn **không được phép** `import` hay sử dụng `java.util.ArrayList`, `java.util.List`, `LinkedList`, hoặc `Vector` cho việc lưu trữ danh sách[cite: 57].
    * **Thay thế bắt buộc:** Mọi hoạt động quản lý danh sách (thêm, sửa, xoá, tìm kiếm) phải được thực hiện thông qua lớp `DataStore<T>` mà chúng ta đã định nghĩa. [cite_start]Lớp này *chỉ được phép* sử dụng một mảng thô (`private T[] list;`) bên trong[cite: 57].

2.  **PHẠM VI LỚP BỊ KHÓA:**
    * Bạn **chỉ được phép** tạo code cho 15 Lớp, 4 Giao diện, và 5 Enum đã được định nghĩa trong Sơ đồ Lớp.
    * Không được tự ý thêm thư viện bên ngoài (ví dụ: thư viện JSON, thư viện xử lý ngày giờ bên thứ ba, v.v.). Chỉ dùng các gói `java.io`, `java.lang`, `java.math`, `java.time`.

3.  **MÔI TRƯỜNG CONSOLE:**
    * Đây là một ứng dụng **Console 100%**.
    * **NGHIÊM CẤM** tạo bất kỳ mã nguồn nào liên quan đến Giao diện Đồ họa (GUI) như Swing, AWT, JavaFX.
    * **NGHIÊM CẤM** tạo bất kỳ mã nguồn nào liên quan đến web (ví dụ: Spring Boot, Servlet, JSP).
    * Tất cả đầu vào phải nhận từ `Scanner` và đầu ra phải là `System.out.println()`.

4.  **LƯU TRỮ FILE TEXT:**
    * [cite_start]Việc đọc và ghi dữ liệu **bắt buộc** phải sử dụng file text thuần túy (ví dụ: `.txt`)[cite: 58].
    * **NGHIÊM CẤM** sử dụng cơ sở dữ liệu (Database), file JSON, XML, hay bất kỳ hình thức tuần tự hóa đối tượng (Serialization) nào khác.
    * Logic đọc/ghi file phải được hiện thực bên trong lớp `DataStore<T>` (theo giao diện `IDataManager`).

5.  **TUÂN THỦ YÊU CẦU OOP CỦA ĐỀ BÀI:**
    * [cite_start]Code phải thể hiện rõ các tính chất OOP đã được chấm điểm [cite: 53-62].
    * [cite_start]**Kế thừa:** Cây kế thừa `Person` -> `Employee` -> `Technician`/`Receptionist` phải rõ ràng[cite: 56].
    * [cite_start]**Đa hình:** Phương thức `calculatePay()` và các phương thức trong `Sellable` phải được ghi đè (override)[cite: 55].
    * [cite_start]**Trừu tượng:** Phải có lớp trừu tượng (`Person`, `Employee`) và phương thức trừu tượng (`calculatePay()`)[cite: 60].
    * [cite_start]**Giao diện:** Phải hiện thực đầy đủ 4 Giao diện (`IEntity`, `Sellable`, `IDataManager`, `IActionManager`)[cite: 61].
    * [cite_start]**Static:** Phải sử dụng `static` cho mẫu Singleton (`AuthService`) và các lớp tiện ích (ví dụ: `Validation`)[cite: 59].
    * [cite_start]**Constructor:** Mọi lớp phải có hàm thiết lập[cite: 54].

---

### 🗺️ 2. NGUỒN THAM CHIẾU (Sơ đồ Lớp Đã Chốt)

Toàn bộ code phải dựa trên cấu trúc 15 lớp + 4 giao diện + 5 enum sau:

* **Interfaces (4):** `IEntity`, `Sellable`, `IDataManager`, `IActionManager<T>`
* **Abstract Classes (2):** `Person`, `Employee`
* **Concrete Classes (13):**
    * Users: `Customer`, `Technician`, `Receptionist`
    * Business: `Service`, `Appointment`, `Invoice`, `Payment`, `Promotion`
    * Inventory & Data: `Product`, `Supplier`, `GoodsReceipt`
    * Core: `DataStore<T>`, `AuthService (Singleton)`
* **Enums (5):** `AppointmentStatus`, `Tier`, `PaymentMethod`, `DiscountType`, `ServiceCategory`

---
