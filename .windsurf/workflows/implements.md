---
description: 
auto_execution_mode: 1
---

---
description: 
auto_execution_mode: 1
---

# TRIỂN KHAI KẾ HOẠCH (IMPLEMENT PLAN)

## 1. VAI TRÒ & NHIỆM VỤ
Với vai trò là Lập trình viên (Developer), triển khai code Java cho tính năng được mô tả trong Kế hoạch Kỹ thuật, tuân thủ nghiêm ngặt các yêu cầu của đồ án.

## 2. TIÊU CHUẨN ĐẦU RA (OUTPUT REQUIREMENTS)

### 2.1. Tuân thủ Kế hoạch
- Code phải phản ánh 100% các Lớp, Interface, và Phương thức đã được định nghĩa trong Kế hoạch.

### 2.2. RÀNG BUỘC KỸ THUẬT (BẮT BUỘC)
- [cite_start]**NGHIÊM CẤM** `import java.util.ArrayList`, `java.util.List`, `java.util.Map`, `java.util.Vector`, hoặc bất kỳ lớp nào thuộc Collection Framework.
- **BẮT BUỘC** sử dụng mảng thuần túy (`T[]`) cho mọi danh sách.
- **BẮT BUỘC** triển khai logic quản lý mảng thủ công:
  - **Thêm (Add):** Phải kiểm tra mảng đầy. Nếu đầy, tạo mảng mới (ví dụ: kích thước `oldSize * 2`) và sao chép toàn bộ phần tử cũ sang.
  - **Xóa (Delete):** Sau khi tìm thấy và xóa (set `null`), phải "dồn" (shift) các phần tử từ vị trí đó về trước để lấp chỗ trống, và giảm biến `count`.
- [cite_start]**BẮT BUỘC** sử dụng `abstract class` và `interface` đúng như kế hoạch[cite: 60, 61].
- [cite_start]**BẮT BUỘC** sử dụng Đa hình (Polymorphism) khi xử lý danh sách chung (ví dụ: khi gọi phương thức `toString()` hoặc phương thức trừu tượng đã được override)[cite: 55].

### 2.3. Chất lượng Code
- Tuân thủ "Bộ Quy Tắc (Rules) Cho Dự Án" (đơn giản, rõ ràng).
- Sử dụng Javadoc (bằng **tiếng Việt**) cho các phương thức public để giải thích mục đích, `Args:`, `Returns:`.
- Chỉ comment inline (`//`) cho những đoạn logic phức tạp (ví dụ: `// Bắt đầu dồn mảng sau khi xóa`).