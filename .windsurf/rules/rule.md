---
trigger: always_on
---

Chắc chắn rồi, đây là nội dung bộ quy tắc của bạn được định dạng lại bằng Markdown sạch:

-----

# BỘ QUY TẮC (RULES) CHUẨN CHO DỰ ÁN

Bộ quy tắc này được thiết kế để tuân thủ yêu cầu "giữ code đơn giản nhất có thể, tránh phức tạp hoá vấn đề, comments code đúng nơi đúng chỗ".

-----

### 1\. Ràng buộc Cốt lõi: Chỉ dùng Mảng (Array-Only)

  * **TUYỆT ĐỐI KHÔNG** `import` hoặc sử dụng `java.util.ArrayList`, `java.util.List`, `java.util.Map`, `java.util.Vector`, `java.util.LinkedList`.
  * Mọi danh sách phải được quản lý thủ công bằng mảng (`T[]`) và một biến đếm (`int count`).

### 2\. Triết lý KISS (Keep It Simple, Stupid)

  * "Giữ code đơn giản nhất có thể."
  * Ưu tiên sự rõ ràng hơn là sự thông minh. Nếu một đoạn code cần 10 dòng để rõ ràng, đừng cố viết nó trong 3 dòng nếu nó trở nên khó hiểu.
  * Tránh lồng ghép quá nhiều cấp (ví dụ: `for` lồng `if` lồng `for`). Nếu cần, hãy tách ra thành một phương thức (hàm) con.

### 3\. Nguyên tắc Đơn nhiệm (SRP - Single Responsibility Principle)

  * "Mỗi class một nhiệm vụ."
  * **Model/Entity** (ví dụ: `Student.java`): Chỉ chứa dữ liệu (thuộc tính, getters, setters, constructors, `toString`). **Không** chứa logic nghiệp vụ (như `addStudent`).
  * **Service/Manager** (ví dụ: `StudentManager.java`): Chỉ chứa logic nghiệp vụ (thêm, sửa, xóa, tìm, thống kê). **Không** chứa logic UI (như `System.out.println` hoặc `Scanner`).
  * **UI/Main** (ví dụ: `Main.java`): Chỉ chịu trách nhiệm hiển thị menu, nhận input (`Scanner`), gọi các hàm của Manager, và in kết quả ra console.

### 4\. Quy tắc Comment: "Tại sao", không "Cái gì"

  * "Comments code đúng nơi đúng chỗ, tránh dài dòng lê thê."
  * **TRÁNH** comment thừa:
    ```java
    // Khởi tạo biến i = 0
    int i = 0;
    // Vòng lặp for
    for (i = 0; i < count; i++) {
        // Tăng i
        i++;
    }
    ```
  * **NÊN** comment giải thích logic nghiệp vụ (tại sao):
    ```java
    // Dồn các phần tử mảng về bên trái để lấp chỗ trống sau khi xóa
    for (int j = index; j < count - 1; j++) {
        list[j] = list[j + 1];
    }
    // Xóa tham chiếu ở phần tử cuối cùng sau khi dồn
    list[count - 1] = null;
    count--;
    ```

### 5\. Quy tắc Đặt tên Rõ nghĩa (Tiếng Anh)

  * Dù comment bằng tiếng Việt, nhưng tên biến, tên hàm, tên lớp nên dùng tiếng Anh chuẩn để thống nhất.
  * **Tốt:** `class StudentManager`, `private Student[] studentList`, `public void addStudent(Student s)`, `private int findStudentById(String id)`
  * **Không tốt:** `class QLSinhVien`, `private Student[] dsSV`, `public void them(Student s)`, `private int tim(String id)`

### 6\. Tránh "Magic Numbers"

  * Không sử dụng các con số vô nghĩa trực tiếp trong code.
  * **Không tốt:** `for (i = 0; i < 100; i++)`
  * **Tốt:**
    ```java
    private static final int MAX_STUDENTS = 100;
    private Student[] studentList = new Student[MAX_STUDENTS];
    ...
    // (Nên dùng biến 'count' thay vì MAX_STUDENTS để lặp qua các phần tử thực tế)
    for (i = 0; i < count; i++)
    ```

### 7\. Tuân thủ Yêu cầu Đồ án

  * Đảm bảo code có đủ:
      * Lớp cơ sở và Kế thừa (ít nhất 2 lớp con)
      * Đa hình (Polymorphism)
      * Lớp trừu tượng (Abstract Class) và Hàm trừu tượng
      * Interface
      * Thuộc tính/Phương thức `static`
      * Đọc/Ghi file text