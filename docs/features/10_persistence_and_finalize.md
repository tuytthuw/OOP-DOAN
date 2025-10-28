# Phase 10 – Hoàn Thiện & Lưu Trữ

## 1. Mô tả Ngữ cảnh
- Thiết lập cơ chế đọc/ghi dữ liệu từ file text để duy trì trạng thái ứng dụng giữa các lần chạy.
- Đảm bảo khi khởi động hệ thống sẽ nạp dữ liệu vào các manager, và khi thoát sẽ lưu lại toàn bộ.
- Tổng rà soát dự án, kiểm tra tuân thủ quy tắc (array-only, SRP, cấu trúc phân tầng, comment hợp lý).

## 2. Cấu trúc Lớp Model (Entities/POJO)
- Không tạo model mới; tập trung vào việc chuyển đổi các entity hiện có sang/ từ định dạng lưu trữ (JSON hoặc CSV).

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
- Không tạo manager mới; `FileHandler` sẽ làm việc với dữ liệu của các manager đã triển khai.

## 4. Yêu cầu Giao diện (Interfaces)
- Cân nhắc định nghĩa `interface IEntityFactory` để tạo instance từ dữ liệu file (nếu cần hỗ trợ deserialize linh hoạt).

## 5. Yêu cầu Đọc/Ghi File
- **`class FileHandler`** (các phương thức `static`):
  - Thuộc tính: đường dẫn file tương ứng cho từng loại dữ liệu (ví dụ: `CUSTOMERS_FILE`, `SERVICES_FILE`...).
  - Phương thức đề xuất:
    - `public static void save(String key, IEntity[] data)` – ghi dữ liệu mảng xuống file (key quyết định file đích).
    - `public static IEntity[] load(String key, IEntityFactory factory)` – đọc dữ liệu và tạo đối tượng.
    - `public static void ensureDataFolder()` – bảo đảm thư mục lưu trữ tồn tại.
  - Định dạng lưu trữ: ưu tiên JSON đơn giản (mỗi thực thể một đối tượng), hoặc CSV với delimiter rõ ràng.
- **`Main.java`**
  - Khi khởi động: gọi `loadAllData()` để nạp dữ liệu vào từng manager.
  - Trước khi thoát: gọi `saveAllData()` để ghi dữ liệu.
  - Cần xử lý ngoại lệ I/O, hiển thị thông báo qua `OutputFormatter`.
- Cân nhắc tạo lớp điều phối (ví dụ `DataBootstrapper`) để gom việc load/save thay vì đặt trực tiếp trong `Main`.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- Bảo đảm dữ liệu đọc lên được kiểm tra hợp lệ (ví dụ: ID không trống, số lượng không âm) trước khi đưa vào manager.
- Khi lưu, chỉ xuất các phần tử hợp lệ (tới `count`), bỏ qua phần tử null trong mảng.
- Tăng cường logging hoặc thông báo nếu xảy ra lỗi load/save để người dùng biết cách xử lý.
- Hoàn thiện kiểm tra tuân thủ quy tắc dự án (Rule 1-7) trước khi kết thúc phase:
  - Không sử dụng `java.util` collections.
  - Đảm bảo phân tách Model/Manager/Service/UI.
  - Comment nêu lý do ở các đoạn logic phức tạp.
  - Rà soát `static` (ID generator, hằng số) theo thiết kế.
