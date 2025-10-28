### 3.1. Mô tả Ngữ cảnh
- Thiết lập bộ khung dự án console Java theo kiến trúc packages đã chuẩn hóa để các giai đoạn sau có thể triển khai logic nghiệp vụ trên nền tảng thống nhất.
- Chuẩn bị sẵn các enum mô tả tập giá trị cố định nhằm bảo đảm tính nhất quán dữ liệu ngay từ đầu.

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- Enum `AppointmentStatus` (com.spa.model.enums): `SCHEDULED`, `SPENDING`, `COMPLETED`, `CANCELLED`.
- Enum `Tier` (com.spa.model.enums): `STANDARD`, `SILVER`, `GOLD`.
- Enum `PaymentMethod` (com.spa.model.enums): `CASH`, `CARD`, `TRANSFER`, `E_WALLET`.
- Enum `DiscountType` (com.spa.model.enums): `PERCENTAGE`, `FIXED_AMOUNT`.
- Enum `ServiceCategory` (com.spa.model.enums): `MASSAGE`, `FACIAL`, `BODY_TREATMENT`, `HAIR_CARE`, `NAIL_CARE`, `MAKEUP`, `COMBINATION`.

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- Tạm thời chưa có lớp quản lý; tạo package `com.spa.data` để chứa `DataStore<T>` ở giai đoạn tiếp theo.
- Định nghĩa hằng số cấu hình ban đầu trong lớp tiện ích (dự kiến `Validation`) ở package `com.spa.service` khi được triển khai.

### 3.4. Yêu cầu Giao diện (Interfaces)
- Chưa phát sinh ở giai đoạn này; đảm bảo sẵn package `com.spa.service` để đặt interface dùng chung nếu cần mở rộng.

### 3.5. Yêu cầu Đọc/Ghi File
- Tạo thư mục dữ liệu `data/` trong cấp dự án để lưu file `.txt` (ví dụ: `customers.txt`, `services.txt`).
- Quy ước mỗi dòng trong file là một bản ghi văn bản thuần, các trường ghép bằng ký tự phân tách thống nhất (ví dụ: dấu `|`).

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Chưa triển khai logic nghiệp vụ; mục tiêu hiện tại là bảo đảm cấu trúc thư mục rõ ràng và sẵn sàng cho DataStore sử dụng mảng thô `T[]` khi được hiện thực.
- Xác nhận mọi enum được xây dựng không chứa logic vượt quá getter mặc định, tập trung vào giá trị cố định để hỗ trợ đa hình ở các lớp kế thừa sau này.
