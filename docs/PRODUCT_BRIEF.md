# Product Brief - Ứng dụng Quản lý Spa

## 1. Tổng Quan / Mô Tả Dự Án

**Ứng dụng Quản lý Spa** là một hệ thống console được xây dựng bằng Java sử dụng các nguyên lý Object-Oriented Programming (OOP). Ứng dụng cung cấp các chức năng toàn diện để quản lý các hoạt động hàng ngày của một spa, bao gồm:

- Quản lý khách hàng và phân loại membership
- Quản lý lịch hẹn dịch vụ
- Quản lý dịch vụ và giá cả
- Quản lý thanh toán và khuyến mãi
- Tính toán hóa đơn với các loại chiết khấu

Mục tiêu chính là cung cấp một giải pháp quản lý hoạt động spa hiệu quả, từ đó giúp tăng trải nghiệm khách hàng và tối ưu hóa lợi nhuận kinh doanh.

## 2. Đối Tượng Mục Tiêu

- **Chủ spa / Quản lý spa:** Quản lý các hoạt động kinh doanh hàng ngày, theo dõi doanh thu và khách hàng
- **Nhân viên spa:** Xem lịch hẹn, quản lý dịch vụ, xử lý thanh toán
- **Khách hàng:** Được quản lý thông qua hệ thống phân loại membership (Tier), nhận ưu đãi chiết khấu

## 3. Lợi ích / Tính Năng Chính

### Tính Năng Chính:

1. **Quản lý Khách hàng (Customers)**

   - Lưu trữ thông tin khách hàng
   - Phân loại khách hàng theo Tier (Platinum, Gold, Silver, Bronze)
   - Theo dõi lịch sử khách hàng

2. **Quản lý Lịch hẹn (Appointments)**

   - Đặt lịch hẹn dịch vụ
   - Theo dõi trạng thái lịch hẹn: SCHEDULED, SPENDING, COMPLETED, CANCELLED
   - Gán nhân viên cho lịch hẹn

3. **Quản lý Dịch vụ (Services)**

   - Quản lý danh sách dịch vụ spa
   - Quản lý giá dịch vụ
   - Phân loại dịch vụ

4. **Quản lý Thanh toán (Transactions)**

   - Ghi nhận các giao dịch thanh toán
   - Hỗ trợ nhiều phương thức thanh toán: CASH, CARD, TRANSFER, E_WALLET
   - Tính toán tiền thanh toán

5. **Quản lý Khuyến mãi (Discounts)**

   - Áp dụng chiết khấu: PERCENTAGE (%), FIXED_AMOUNT (tiền cố định)
   - Quản lý mã khuyến mãi
   - Theo dõi hiệu quả khuyến mãi

6. **Tính toán Hóa đơn (Invoices)**
   - Tạo hóa đơn chi tiết cho lịch hẹn
   - Tính toán tổng tiền với khuyến mãi
   - Quản lý tình trạng thanh toán

### Lợi Ích:

- **Tiết kiệm thời gian:** Tự động hóa quy trình quản lý spa
- **Tăng hiệu quả:** Quản lý lịch hẹn và dịch vụ tập trung
- **Cải thiện trải nghiệm khách hàng:** Hỗ trợ hệ thống membership và khuyến mãi linh hoạt
- **Dễ theo dõi:** Tất cả dữ liệu được ghi chép và quản lý tập trung

## 4. Công Nghệ / Kiến Trúc Cấp Cao

### Tech Stack:

| Thành phần             | Công nghệ                         |
| ---------------------- | --------------------------------- |
| **Ngôn ngữ lập trình** | Java 23                           |
| **Paradigm**           | Object-Oriented Programming (OOP) |
| **Build Tool**         | Apache Maven                      |
| **Kiến trúc**          | Layered Architecture              |
| **Giao diện**          | Console-based CLI                 |

### Cấu Trúc Dự Án:

```
src/main/java/
├── Main.java                          # Entry point
├── collections/                       # Quản lý tập hợp dữ liệu
├── enums/                             # Các kiểu liệt kê
│   ├── AppointmentStatus              # Trạng thái lịch hẹn
│   ├── DiscountType                   # Loại chiết khấu
│   ├── PaymentMethod                  # Phương thức thanh toán
│   └── Tier                           # Loại membership
├── models/                            # Các lớp mô hình dữ liệu
│   ├── Customer
│   ├── Appointment
│   ├── Service
│   ├── Transaction
│   ├── Discount
│   └── Invoice
├── services/                          # Logic xử lý nghiệp vụ
│   ├── CustomerService
│   ├── AppointmentService
│   ├── ServiceService
│   ├── TransactionService
│   └── DiscountService
├── io/                                # Xử lý input/output dữ liệu
└── ui/                                # Giao diện người dùng
```

### Nguyên Lý OOP Được Áp Dụng:

- **Encapsulation:** Bảo đóng dữ liệu trong các lớp
- **Inheritance:** Kế thừa cho các loại dịch vụ và khách hàng
- **Polymorphism:** Xử lý các loại chiết khấu và phương thức thanh toán khác nhau
- **Abstraction:** Tách biệt giao diện từ thực thi chi tiết

---

**Phiên bản:** 1.0  
**Ngày cập nhật:** October 2025  
**Trạng thái:** Trong phát triển
