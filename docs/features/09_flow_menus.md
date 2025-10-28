# Phase 9 – Giao Diện Phức Tạp (Flow-Menus)

## 1. Mô tả Ngữ cảnh
- Xây dựng các luồng UI phức tạp kết hợp nhiều bước (thanhtoán, đăng nhập) dựa trên công cụ Phase 7 và service Phase 6.
- Tích hợp `AuthService` vào hệ thống menu để kiểm soát truy cập theo vai trò.
- Ràng buộc mọi thao tác vẫn đi qua service; UI chỉ điều phối, thu thập input, và hiển thị output.

## 2. Cấu trúc Lớp Model (Entities/POJO)
- Không phát sinh model mới; luồng thao tác dựa trên các entity đã có (Invoice, Transaction, Promotion, AuthSession...).

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
- Không tạo manager mới; các flow sẽ tận dụng `PaymentService`, `InvoiceService`, `PromotionService`, `AuthService`, `CustomerService`.

## 4. Yêu cầu Giao diện (Interfaces)
- Có thể sử dụng chung interface `Menu` (nếu đã tạo ở Phase 8) để thống nhất phương thức `showMenu()`, `handleSelection()`.
- Tối ưu trải nghiệm console: hiển thị thông tin từng bước, nhắc xác nhận trước khi thực hiện giao dịch.

## 5. Yêu cầu Đọc/Ghi File
- Flow không thao tác trực tiếp với file; nếu cần lưu thay đổi, gọi service để cập nhật manager (Phase 10 sẽ đảm nhận lưu ra file).

## 6. Yêu cầu Nghiệp vụ Cốt lõi
- **`BillingMenu`**
  - Bước chuẩn: chọn hóa đơn -> hiển thị chi tiết -> chọn phương thức thanh toán -> gọi `PaymentService.processPayment()` -> cập nhật trạng thái -> in kết quả.
  - Hỗ trợ nhập mã khuyến mãi (gọi `PromotionService.validateCode()` trước khi áp dụng `InvoiceService.applyPromotion()`).
  - Sau giao dịch thành công, hiển thị thông tin hóa đơn và giao dịch.
- **Luồng đăng nhập / đăng xuất**
  - Dùng `AuthService.login()` để thiết lập `AuthSession`; kiểm tra vai trò, quyền truy cập menu con.
  - `AuthService.logout()` xoá phiên; `AuthService.refreshSession()` xử lý timeout.
  - `MainMenu` phải yêu cầu đăng nhập trước khi truy cập menu nhạy cảm (ví dụ Billing, Inventory).
- **Tích hợp vào MainMenu**
  - Các lựa chọn mới: "Thanh toán hóa đơn", "Quản lý phiên đăng nhập".
  - Kiểm tra role trước khi cho phép truy cập (ví dụ: chỉ `MANAGER`/`ADMIN` mở BillingMenu).
- Flow phải xử lý lỗi nhập liệu, lỗi nghiệp vụ (ví dụ: thanh toán vượt số tiền còn lại) và hiển thị thông báo rõ ràng qua `OutputFormatter`.
