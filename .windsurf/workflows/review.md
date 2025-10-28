---
description: 
auto_execution_mode: 1
---

---
description: 
auto_execution_mode: 1
---

# ĐÁNH GIÁ CODE (CODE REVIEW)

## 1. VAI TRÒ & NHIỆM VỤ
[cite_start]Với vai trò là Senior Developer, thực hiện đánh giá code toàn diện dựa trên code và Kế hoạch, tập trung vào các yêu cầu bắt buộc của đồ án[cite: 44].

## 2. PHẠM VI ĐÁNH GIÁ (SCOPE)

### 2.1. Đối chiếu Kế hoạch & Yêu cầu
- Code có triển khai đúng theo Kế hoạch Kỹ thuật không?

### 2.2. Kiểm tra Ràng buộc Mảng (Array Constraint)
- [cite_start]**[LỖI NGHIÊM TRỌNG]** Code có `import` hoặc sử dụng `ArrayList`, `Vector`, `LinkedList` không? 
- Logic **nới rộng mảng** (resize) khi thêm (add) có chính xác không? Có bị `ArrayIndexOutOfBoundsException` không?
- Logic **dồn mảng** (shift) khi xóa (delete) có chính xác không? Biến `count` có được cập nhật đúng không?
- Logic tìm kiếm có duyệt đúng `count` (số phần tử thực tế) thay vì `array.length` (kích thước tối đa) không?

### 2.3. Kiểm tra Tuân thủ OOP (Thang điểm Đồ án)
- [cite_start]**Kế thừa:** Có Lớp cơ sở và các lớp con kế thừa hợp lý không? [cite: 56]
- [cite_start]**Đa hình:** Có sử dụng đa hình khi duyệt mảng (ví dụ: gọi một phương thức được override) không? [cite: 55]
- [cite_start]**Trừu tượng:** Có `abstract class` và `abstract method` không? [cite: 60]
- [cite_start]**Interface:** Có `interface` và lớp `implements` nó không? [cite: 61]
- [cite_start]**Static:** Có sử dụng `static` cho thuộc tính hoặc phương thức hợp lý không? [cite: 59]
- [cite_start]**Constructor:** Các lớp đã có hàm thiết lập (constructor) đầy đủ chưa? [cite: 54]

### 2.4. Kiểm tra Logic Nghiệp vụ
- Phát hiện lỗi logic, `NullPointerException` (ví dụ: khi tìm kiếm không thấy nhưng vẫn cố truy cập), hoặc các trường hợp biên (edge case) bị bỏ sót (ví dụ: xóa phần tử cuối cùng, xóa phần tử duy nhất).
- **File I/O:** Logic Đọc/Ghi file có chạy đúng không? [cite_start]Có xử lý `try-catch` cho `IOException` không? [cite: 58]