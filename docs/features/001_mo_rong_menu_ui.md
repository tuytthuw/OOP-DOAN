---
title: "Kế hoạch mở rộng các menu UI"
---

## 1. Mô tả Ngữ cảnh
- Mục tiêu: hoàn thiện hệ thống menu tại `src/main/java/com/spa/ui` để đáp ứng đầy đủ yêu cầu quản lý (nhập nhiều phần tử, tìm kiếm đa khóa, thống kê, nghiệp vụ nâng cao, thể hiện đa hình) cho mọi đối tượng (khách hàng, dịch vụ, sản phẩm, nhà cung cấp, nhân sự, lịch hẹn, hóa đơn, thanh toán, phiếu nhập).
- Phạm vi áp dụng: các lớp UI hiện có (`CustomerMenu`, `ServiceMenu`, `ProductMenu`, `SupplierMenu`, `PromotionMenu`, `AppointmentMenu`, `InvoiceMenu`, `PaymentMenu`, `GoodsReceiptMenu`, `MenuUI`, `MenuHelper`, `Validation`).

## 2. Cấu trúc Lớp Model (Entities/POJO)
- **Lớp cơ sở (Trừu tượng):** `abstract class Person`
  - Thuộc tính: `personId`, `fullName`, `phoneNumber`, `male`, `birthDate`, `email`, `address`, `deleted`.
  - Phương thức trừu tượng: `public abstract String getRole();`.
- **Các lớp kế thừa:**
  1. `class Customer extends Person`
     - Thuộc tính riêng: `Tier memberTier`, `String notes`, `int points`, `LocalDate lastVisitDate`.
     - Ghi đè: `display()` in định dạng bảng; `getRole()` trả về "CUSTOMER".
  2. `abstract class Employee extends Person` cùng các lớp con `Technician`, `Receptionist`.
     - Thuộc tính riêng: `double salary`, `String passwordHash`, `LocalDate hireDate`.
     - Ghi đè: `calculatePay()` đa hình ở lớp con.
  3. `class Service`, `class Product`, `class Supplier`, `class Promotion`, `class Invoice`, `class Payment`, `class GoodsReceipt` giữ nguyên mô hình hiện có nhưng bảo đảm cung cấp phương thức `display()` phục vụ UI.

## 3. Cấu trúc Lớp Quản lý (Services/Manager)
- **Tên lớp:** `CustomerStore`, `ServiceStore`, `ProductStore`, `SupplierStore`, `EmployeeStore`, `AppointmentStore`, `InvoiceStore`, `PaymentStore`, `GoodsReceiptStore`, `PromotionStore`.
- **Thuộc tính dữ liệu:**
  - `private static final int MAX_SIZE = ...;` (bổ sung nơi còn thiếu để biểu diễn giới hạn lưu trữ).
  - `private T[] items;`
  - `private int count;`
- **Phương thức (CRUD & Nghiệp vụ):**
  - `public void add(T item)` (gắn ID qua `DataStore`).
  - `public T findById(String id)`.
  - `public boolean delete(String id)` (đặt cờ `deleted`).
  - `public T[] getAll()` (trả về bản sao mảng hiện hành).
  - `public void writeFile()` / `readFile()`.
  - `public T[] search(FilterCriteria criteria)` (bổ sung cho nhu cầu tìm kiếm).
  - `public StatisticResult statisticBy(StatisticCriteria criteria)` (tạo lớp kết quả thống kê thuần dữ liệu).

## 4. Yêu cầu Giao diện (Interfaces)
- `interface MenuModule`
  - `void show();`
- `interface IEntity`
  - `String getId();`, `String getPrefix();`, `void display();`
- Đề xuất `interface IReportable` (nếu cần) cho các đối tượng có khả năng tổng hợp thống kê chuyên sâu (`double calculateTotal();`).

## 5. Yêu cầu Đọc/Ghi File
- Lớp chịu trách nhiệm: các lớp `*Store` tiếp tục đảm nhận đọc/ghi file text.
- Định dạng: CSV thuần với dấu phân cách `;`, đồng bộ với hiện trạng.
- Quy ước: sau mọi thao tác biến đổi tại menu, phải gọi `writeFile()` để lưu.

## 6. Yêu cầu Nghiệp vụ Cốt lõi
1. **Nhập nhiều phần tử (Bulk Create):**
   - Mỗi menu đối tượng (dịch vụ, sản phẩm, nhà cung cấp, nhân sự, khuyến mãi, lịch hẹn, hóa đơn, thanh toán, phiếu nhập) bổ sung lựa chọn nhập `k` phần tử.
2. **Xuất danh sách:**
   - Sử dụng `display()` của từng model để thể hiện đa hình khi liệt kê.
3. **Thêm/Sửa/Xóa theo mã:**
   - Bảo đảm tất cả menu hỗ trợ hủy thao tác (`Validation.get*OrCancel`) và cập nhật tệp.
4. **Tìm kiếm đa khóa:**
   - Thêm menu con tìm kiếm cho từng đối tượng; hỗ trợ nhiều từ khóa gần đúng và lọc theo thuộc tính đặc thù (ví dụ dịch vụ theo danh mục, sản phẩm theo nhãn hiệu, lịch hẹn theo trạng thái/technician).
5. **Thống kê số liệu:**
   - Mỗi menu cần tối thiểu một báo cáo: dịch vụ theo danh mục, sản phẩm theo tồn kho/ngưỡng cảnh báo, khách hàng theo hạng & điểm trung bình, nhà cung cấp theo khu vực, khuyến mãi theo trạng thái, lịch hẹn theo trạng thái/thời gian, hóa đơn theo trạng thái thanh toán, thanh toán theo phương thức/ngày, phiếu nhập theo nhà cung cấp & tổng chi phí.
6. **Nghiệp vụ nâng cao:**
   - Bổ sung thao tác đặc thù (ví dụ kích hoạt/khóa dịch vụ hàng loạt, cập nhật cost price sản phẩm từ phiếu nhập, nhắc lịch hẹn sắp tới, tái sử dụng khuyến mãi đang hiệu lực).
7. **Thể hiện đa hình:**
   - Khi in danh sách nhân sự, gọi `calculatePay()`.
   - Khi in dịch vụ/sản phẩm, thể hiện thông tin riêng (thời lượng, tồn kho, giá vốn).

## 7. Phân tách gói `com.spa.ui`
- `MenuUI`: điều phối, hiển thị menu chính, phân quyền.
- `MenuContext`: cung cấp phụ thuộc (`AuthService`, các `Store`).
- `MenuHelper`, `Validation`: cung cấp tiện ích chọn danh mục, đọc input, hỗ trợ hủy với từ khóa `Q`.
- Các lớp `*Menu` tập trung vào điều hướng, không chứa logic lưu trữ.
- Khi mở rộng, mỗi tính năng mới trong menu cần được trích thành phương thức riêng để tuân thủ KISS/SRP.

## 8. Lộ trình Thực hiện
1. Chuẩn hóa `Validation` và `MenuHelper` với các hàm `get*OrCancel`, lựa chọn danh mục cho mọi menu.
2. Triển khai mở rộng lần lượt các menu theo thứ tự ưu tiên: `ServiceMenu` → `ProductMenu` → `SupplierMenu` → `PromotionMenu` → `AppointmentMenu` → `InvoiceMenu` → `PaymentMenu` → `GoodsReceiptMenu` → `MenuUI` (bổ sung mục điều hướng) → `Employee` menu (nếu tách riêng).
3. Sau mỗi menu, bổ sung thống kê & tìm kiếm phù hợp, cập nhật tài liệu sử dụng.
4. Viết test thủ công/checklist để xác nhận toàn bộ yêu cầu (bulk, search, statistic, nghiệp vụ, đa hình) hoạt động.
