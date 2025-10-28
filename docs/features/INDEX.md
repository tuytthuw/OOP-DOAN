# Kế Hoạch Phát Triển Tổng Thể - Dự Án Quản Lý Spa (Đã Sắp Xếp Lại)

## Tổng Quan

Dự án được chia thành 6 giai đoạn phát triển chính, đảm bảo tính logic và tuân thủ các yêu cầu kỹ thuật, đặc biệt là việc **không sử dụng `java.util.List`** và áp dụng các nguyên tắc code sạch.

---

## Giai Đoạn 1: Nền Tảng & Mô Hình Hóa (Core Foundations & Models)

Mục tiêu: Xây dựng các lớp và giao diện cốt lõi, định hình cấu trúc dữ liệu và xử lý lỗi.

| # | Tên Kế Hoạch | Mô Tả | Tệp Tạo Mới |
|---|---|---|---|
| **0000** | [Xây Dựng Person Base Class](./0000_PLAN.md) | Lớp `abstract` cơ sở cho `Customer` và `Employee`. | `models/Person.java` |
| **0002b** | [Xây Dựng Interface Sellable](./0002b_PLAN.md) | Interface cho các đối tượng có thể bán được. | `interfaces/Sellable.java` |
| **0012** | [Exception Handling & Custom Exceptions](./0012_PLAN.md) | Định nghĩa hệ thống exception riêng cho ứng dụng. | `exceptions/*.java` |
| **0001** | [Xây Dựng Model Khách Hàng](./0001_PLAN.md) | Lớp `Customer` kế thừa `Person`. | `models/Customer.java` |
| **0002** | [Xây Dựng Model Dịch Vụ](./0002_PLAN.md) | Lớp `Service` implement `Sellable`. | `models/Service.java` |
| **0015** | [Mô hình Sản phẩm (Product Model)](./0015_PLAN.md) | Lớp `Product` để quản lý sản phẩm bán lẻ và tồn kho. | `models/Product.java` |
| **0003** | [Xây Dựng Model Lịch Hẹn](./0003_PLAN.md) | Lớp `Appointment` quản lý lịch hẹn. | `models/Appointment.java` |
| **0004** | [Xây Dựng Model Chiết Khấu](./0004_PLAN.md) | Lớp `Discount` cho các chương trình khuyến mãi. | `models/Discount.java` |
| **0006** | [Xây Dựng Model Hóa Đơn (CẬP NHẬT)](./0006_PLAN.md) | Lớp `Invoice` tổng hợp chi phí cho cả dịch vụ và sản phẩm. | `models/Invoice.java`, `models/InvoiceItem.java` |
| **0005** | [Xây Dựng Model Giao Dịch](./0005_PLAN.md) | Lớp `Transaction` ghi nhận thanh toán. | `models/Transaction.java` |
| **0011** | [Xây Dựng Employee/Staff](./0011_PLAN.md) | Các lớp `Employee`, `Receptionist`, `Technician`. | `models/Employee.java`, ... |
| **0016** | [Quản lý Nhập kho (Goods Receipt)](./0016_PLAN.md) | Lớp `GoodsReceipt` để quản lý việc nhập sản phẩm vào kho. | `models/GoodsReceipt.java`, ... |

---

## Giai Đoạn 2: Lớp Truy Cập Dữ Liệu (Data Access Layer)

Mục tiêu: Xây dựng các lớp quản lý (Manager) để thao tác với dữ liệu trong bộ nhớ, **sử dụng mảng `T[]`**.

| # | Tên Kế Hoạch | Mô Tả | Tệp Tạo Mới |
|---|---|---|---|
| **0007** | [Xây Dựng Collection Manager](./0007_PLAN.md) | Lớp `BaseManager<T>` và các lớp con (`CustomerManager`, `ProductManager`, `GoodsReceiptManager`...). | `collections/*.java` |

---

## Giai Đoạn 3: Lớp Logic Nghiệp Vụ (Business Logic Layer)

Mục tiêu: Tách biệt logic nghiệp vụ và thêm các dịch vụ cốt lõi.

| # | Tên Kế Hoạch | Mô Tả | Tệp Tạo Mới |
|---|---|---|---|
| **0008** | [Xây Dựng Business Logic Services](./0008_PLAN.md) | Các lớp Service (`CustomerService`, `InvoiceService`...). | `services/*.java` |
| **0014** | [Authentication Service](./0014_PLAN.md) | Dịch vụ Singleton để quản lý đăng nhập và phân quyền. | `services/AuthService.java` |
| - | **InventoryService** (trong #0016) | Logic xử lý nghiệp vụ kho hàng. | `services/InventoryService.java` |

---

## Giai Đoạn 4: Lớp Trình Bày & Lưu Trữ (Presentation & Persistence Layer)

Mục tiêu: Xây dựng giao diện người dùng và cơ chế lưu trữ dữ liệu.

| # | Tên Kế Hoạch | Mô Tả | Tệp Tạo Mới |
|---|---|---|---|
| **0009** | [Xây Dựng Input/Output Manager](./0009_PLAN.md) | Các lớp xử lý input từ người dùng và output ra console. | `io/InputHandler.java`, ... |
| **0013** | [Data Persistence](./0013_PLAN.md) | Lưu và tải dữ liệu ứng dụng từ/vào file JSON. | `io/FileHandler.java` |
| **0010** | [Xây Dựng Giao Diện Menu](./0010_PLAN.md) | Hệ thống menu tương tác trên console. | `ui/*.java` |

---

## Giai Đoạn 5: Tích Hợp và Hoàn Thiện

- **Tệp:** `Main.java`
- **Công việc:**
  - Khởi tạo tất cả các Manager, Service và Handler.
  - Gọi `FileHandler` để **tải dữ liệu** (`loadAllData`).
  - Khởi chạy `MainMenu` để bắt đầu chương trình.
  - Thêm `Shutdown Hook` để **lưu dữ liệu** (`saveAllData`) khi thoát.