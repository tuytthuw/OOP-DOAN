# Phase 8 – Giao Diện Con (Sub-Menus)

## 1. Mô tả Ngữ cảnh
- Xây dựng các menu chức năng riêng (Customer, Inventory, Appointment) để tách biệt luồng thao tác, bảo đảm SRP giữa từng UI module.
- Mỗi menu con sử dụng `InputHandler`, `OutputFormatter` (Phase 7) và gọi service tương ứng (Phase 6) để thực hiện nghiệp vụ.
- Không menu nào được truy cập trực tiếp vào lớp `Manager`; toàn bộ hành vi thông qua service layer.

## 2. Cấu trúc Lớp Model (Entities/POJO)
- Không phát sinh model mới; menu chỉ thao tác với các entity đã xây dựng (Customer, Product, Appointment...).

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
- Không tạo manager/service mới; menu sử dụng các service hiện hữu (CustomerService, InventoryService, AppointmentService...).

## 4. Yêu cầu Giao diện (Interfaces)
- Có thể cân nhắc `interface Menu` dùng chung (ví dụ: `showMenu()`, `handleSelection(int choice)`), nhưng không bắt buộc ở phase này.

## 5. Yêu cầu Đọc/Ghi File
- Menu không xử lý IO file; khi cần lưu/trả dữ liệu sẽ gọi service hoặc `FileHandler` (Phase 10) thông qua lớp điều phối.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- `CustomerMenu` phải cung cấp các bước CRUD đầy đủ, luôn gọi `CustomerService` để đảm bảo kiểm tra điểm loyalty, trạng thái xoá mềm.
- `InventoryMenu` cho phép nhập phiếu `GoodsReceipt` mới, xem tồn kho và xử lý receipt chưa áp dụng; chỉ dùng `InventoryService`, `ProductManager` không được gọi trực tiếp.
- `AppointmentMenu` hỗ trợ đặt, đổi lịch, hủy, hoàn tất; tận dụng `AppointmentService` và kiểm tra slot rảnh qua `TechnicianAvailabilityService`.
- Mỗi menu phải xử lý input không hợp lệ (dùng `InputHandler`) và hiển thị phản hồi rõ ràng (dùng `OutputFormatter`).
- Menu cần trả về control cho `MainMenu` sau khi hoàn tất thao tác, tránh vòng lặp vô hạn không cần thiết.
