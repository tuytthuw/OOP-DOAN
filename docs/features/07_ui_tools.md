# Phase 7 – Công Cụ Giao Diện (UI-Tools)

## 1. Mô tả Ngữ cảnh
- Xây dựng bộ công cụ nhập/xuất chuẩn hóa cho ứng dụng console, bảo đảm tách biệt hoàn toàn giữa logic UI và nghiệp vụ.
- Cung cấp nền tảng để các menu (Phase 8-9) tương tác với người dùng một cách thống nhất, tránh lặp lại mã xử lý nhập/xuất.
- Tuân thủ quy tắc SRP: mỗi lớp UI-tool đảm nhiệm một nhiệm vụ cụ thể (đọc input, format output, điều phối menu chính).

## 2. Cấu trúc Lớp Model (Entities/POJO)
- Không tạo thêm POJO mới trong phase này; `InputHandler`, `OutputFormatter`, `MainMenu` thuộc nhóm hỗ trợ UI, không lưu trữ dữ liệu nghiệp vụ.

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
- Không tạo service/manager mới; các UI-tool sẽ gọi tới tầng service đã xây dựng ở Phase 6.

## 4. Yêu cầu Giao diện (Interfaces)
- Có thể cân nhắc `interface Menu` (nếu cần tái sử dụng ở Phase 8-9), nhưng Phase 7 chỉ tập trung vào công cụ chung.

## 5. Yêu cầu Đọc/Ghi File
- Không có chức năng IO file trong phase này; mọi thao tác đọc/ghi file sẽ do `FileHandler` (Phase 10) đảm nhiệm.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- `InputHandler` phải kiểm soát lỗi nhập liệu (trim, validate khoảng giá trị) và trả về kiểu dữ liệu đúng.
- `OutputFormatter` định dạng dữ liệu rõ ràng, tránh logic nghiệp vụ; cung cấp phương thức hiển thị bảng, thông báo trạng thái, in hóa đơn.
- `MainMenu` chỉ điều hướng lựa chọn; không gọi trực tiếp `Manager`, mà chỉ gọi `Service` hoặc `Menu` con (Phase 8-9).
