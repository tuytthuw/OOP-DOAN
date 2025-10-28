---
description: 
auto_execution_mode: 1
---

---
description: 
auto_execution_mode: 1
---

# TÁI CẤU TRÚC CODE (REFACTOR CODE)

## 1. VAI TRÒ & NHIỆM VỤ
Với vai trò là Senior Developer, tái cấu trúc khối mã Java được cung cấp để cải thiện khả năng bảo trì, tuân thủ OOP và triết lý "Giữ code đơn giản".

## 2. YÊU CẦU ĐẦU RA (OUTPUT)
Cung cấp một phản hồi duy nhất bao gồm 3 phần:

### 2.1. Phân tích & Chiến lược Tái cấu trúc
- Vấn đề của code hiện tại là gì?
  - *(Gợi ý: "Logic quản lý mảng (thêm/xóa) đang bị lặp lại ở nhiều lớp Manager.")*
  - *(Gợi ý: "Lớp Manager đang làm cả việc `System.out.println`, vi phạm SRP. Cần tách logic UI ra khỏi logic nghiệp vụ.")*
  - *(Gợi ý: "Code tìm kiếm index của phần tử đang được viết 2-3 lần trong `update` và `delete`. Cần tách ra hàm `private int findIndexById(String id)`.")*
- Chiến lược tái cấu trúc là gì?

### 2.2. Mã đã được Tái cấu trúc
- Cung cấp khối mã đã được tái cấu trúc, sạch sẽ và hoàn chỉnh.
- [cite_start](Lưu ý: Vẫn phải tuân thủ 100% ràng buộc **không dùng ArrayList** ).

### 2.3. Tóm tắt Thay đổi (Lợi ích)
- 3 thay đổi quan trọng nhất là gì và lợi ích của chúng?
- *(Ví dụ: "Tách `findIndexById`: Logic tìm kiếm tập trung một chỗ, dễ sửa, hàm `update` và `delete` ngắn gọn hơn.")*
- *(Ví dụ: "Tách logic UI: Lớp Manager giờ chỉ trả về dữ liệu (mảng hoặc đối tượng), lớp Main chịu trách nhiệm in. Dễ bảo trì hơn.")*