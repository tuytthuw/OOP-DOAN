# 📖 CHI TIẾT TRIỂN KHAI CÁC SERVICES - PLAN 0008

## 1️⃣ CustomerService - Quản Lý Khách Hàng

### Mục đích

Xử lý logic nghiệp vụ liên quan đến khách hàng spa: đăng ký, cập nhật, quản lý tier membership.

### Phương thức chính

| Phương thức               | Tham số                                            | Trả về        | Ghi chú                             |
| ------------------------- | -------------------------------------------------- | ------------- | ----------------------------------- |
| `registerNewCustomer()`   | fullName, phone, email, address, isMale, birthDate | Customer/null | Kiểm tra duplicate phone & email    |
| `updateCustomerInfo()`    | customerId, fullName, phone, email, address        | boolean       | Validate duplicate (trừ chính mình) |
| `updateCustomerTier()`    | customerId                                         | void          | Gọi `customer.updateTier()`         |
| `addSpendingToCustomer()` | customerId, amount                                 | void          | Cộng tiền + update lastValidDate    |
| `deactivateCustomer()`    | customerId                                         | boolean       | Set isActive = false                |
| `reactivateCustomer()`    | customerId                                         | boolean       | Set isActive = true                 |

### Cơ chế Tier Tự Động

- Mỗi khi `registerNewCustomer()` → Tier = BRONZE
- Mỗi khi `addSpendingToCustomer()` → Gọi `updateCustomerTier()` để nâng tier nếu đủ điều kiện
- Tier update dựa trên `totalSpent`:
  - PLATINUM: >= 10,000,000 VND
  - GOLD: >= 5,000,000 VND
  - SILVER: >= 1,000,000 VND
  - BRONZE: < 1,000,000 VND

---

## 2️⃣ AppointmentService - Quản Lý Lịch Hẹn

### Mục đích

Quản lý toàn bộ vòng đời lịch hẹn: từ đặt lịch, bắt đầu, cho đến hoàn thành.

### Trạng thái Lịch Hẹn (State Machine)

```
SCHEDULED → SPENDING → COMPLETED
   ↓          ↓
CANCELLED  CANCELLED
```

### Phương thức chính

| Phương thức                       | Tham số                                    | Trả về           | Ghi chú                                     |
| --------------------------------- | ------------------------------------------ | ---------------- | ------------------------------------------- |
| `bookAppointment()`               | customerId, serviceId, appointmentDateTime | Appointment/null | Kiểm tra thời gian không quá khứ            |
| `cancelAppointment()`             | appointmentId                              | boolean          | Chỉ cancel từ SCHEDULED/SPENDING            |
| `rescheduleAppointment()`         | appointmentId, newDateTime                 | boolean          | Xóa cũ + tạo mới (vì không có setter)       |
| `startAppointment()`              | appointmentId                              | boolean          | SCHEDULED → SPENDING                        |
| `completeAppointment()`           | appointmentId                              | boolean          | SPENDING → COMPLETED + update lastVisitDate |
| `assignStaffToAppointment()`      | appointmentId, staffId                     | boolean          | Gán nhân viên cho lịch                      |
| `getCustomerAppointmentHistory()` | customerId                                 | Appointment[]    | Trả về mảy **không dùng List**              |

### Lưu ý Kỹ Thuật

- `rescheduleAppointment()`: Vì Appointment không có setter cho appointmentDateTime, logic xóa lịch cũ + tạo lịch mới
- `completeAppointment()`: Cập nhật `lastValidDate` của Customer tự động
- Tất cả phương thức kiểm tra dữ liệu trước: Customer tồn tại, Service tồn tại, thời gian hợp lệ

---

## 3️⃣ InvoiceService - Quản Lý Hóa Đơn

### Mục đích

Tạo hóa đơn từ lịch hẹn, áp dụng chiết khấu, tính thuế và tổng tiền.

### Quy trình Tạo Hóa Đơn

1. Lấy Appointment → Lấy Service → Lấy giá
2. Tạo Invoice với subtotal = basePrice
3. Tính thuế 10% tự động
4. Có thể áp dụng Discount sau đó

### Phương thức chính

| Phương thức                     | Tham số                      | Trả về          | Ghi chú                               |
| ------------------------------- | ---------------------------- | --------------- | ------------------------------------- |
| `createInvoiceForAppointment()` | appointmentId, paymentMethod | Invoice/null    | Tạo + tính thuế tự động (10%)         |
| `applyDiscountToInvoice()`      | invoiceId, discountCode      | boolean         | Kiểm tra Discount hợp lệ + tăng usage |
| `calculateFinalAmount()`        | invoiceId                    | BigDecimal/null | Tính lại total và trả về              |
| `getInvoiceDetails()`           | invoiceId                    | String          | Trả về hóa đơn được format            |

### Logic Chi Tiết

**Tạo Hóa Đơn:**

```
Appointment → Service → basePrice
Invoice: subtotal = basePrice
calculateTax(10%) → taxAmount = subtotal * 0.1
calculateTotal() → totalAmount = subtotal - discount + tax
```

**Áp Dụng Chiết Khấu:**

- Kiểm tra `discount.canUse()` (còn hiệu lực, còn lượt sử dụng)
- Tính `discount.calculateDiscount(subtotal)`
- Áp dụng vào Invoice: `applyDiscount(discountAmount, discountCode)`
- Tăng lượt sử dụng: `discount.incrementUsage()`

---

## 4️⃣ PaymentService - Quản Lý Thanh Toán

### Mục đích

Xử lý thanh toán, ghi nhận giao dịch, hoàn tiền.

### Quy trình Thanh Toán

1. Lấy hóa đơn → Tạo Transaction → Xử lý thanh toán
2. Đánh dấu hóa đơn PAID
3. Cộng chi tiêu vào Customer
4. Nâng tier Customer nếu đủ điều kiện

### Phương thức chính

| Phương thức                  | Tham số                                   | Trả về           | Ghi chú                                             |
| ---------------------------- | ----------------------------------------- | ---------------- | --------------------------------------------------- |
| `processPaymentForInvoice()` | invoiceId, paymentMethod                  | Transaction/null | Thanh toán hoàn chỉnh + update customer             |
| `recordTransaction()`        | appointmentId, customerId, amount, method | Transaction/null | Ghi nhận giao dịch (không liên kết hóa đơn)         |
| `refundTransaction()`        | transactionId, refundAmount               | boolean          | Hoàn tiền từ giao dịch thành công                   |
| `getPaymentStatus()`         | transactionId                             | String           | Trả về trạng thái (SUCCESS, FAILED, REFUNDED, etc.) |

### Flow Xử Lý Thanh Toán

```
Invoice → check isPaid()
   ↓ (chưa paid)
Tạo Transaction với status PENDING
   ↓
Gọi transaction.processPayment() → status = SUCCESS
   ↓
Đánh dấu invoice.markAsPaid(today)
   ↓
customerService.addSpendingToCustomer(amount)
   ↓
customerService.updateCustomerTier()
```

---

## 5️⃣ ReportService - Thống Kê & Báo Cáo

### Mục đích

Cung cấp các báo cáo và thống kê về doanh thu, khách hàng, dịch vụ.

### Phương thức chính

| Phương thức                     | Tham số            | Trả về     | Ghi chú                            |
| ------------------------------- | ------------------ | ---------- | ---------------------------------- |
| `getTotalRevenueByDateRange()`  | startDate, endDate | BigDecimal | Tính tổng từ Invoice đã thanh toán |
| `getCustomerCountByTier()`      | tier               | int        | Đếm khách theo tier                |
| `getAppointmentCountByStatus()` | status             | int        | Đếm lịch theo trạng thái           |
| `getTopCustomersBySpending()`   | limit              | Customer[] | Top N khách (sắp xếp giảm) → mảy   |
| `getMostPopularService()`       | -                  | Service    | Dịch vụ được dùng nhiều nhất       |
| `getMonthlyRevenue()`           | year, month        | BigDecimal | Doanh thu tháng cụ thể             |
| `getPaymentMethodStatistics()`  | -                  | String[]   | Thống kê theo phương thức → mảy    |

### Ví dụ Sử Dụng

```java
// Doanh thu tháng 10/2024
BigDecimal oct2024Revenue = reportService.getMonthlyRevenue(2024, 10);

// Top 5 khách hàng chi tiêu nhiều
Customer[] top5 = reportService.getTopCustomersBySpending(5);

// Số khách PLATINUM
int platinumCount = reportService.getCustomerCountByTier(Tier.PLATINUM);

// Thống kê thanh toán
String[] paymentStats = reportService.getPaymentMethodStatistics();
// Output: ["CASH: 50000000 VND", "CARD: 30000000 VND", "TRANSFER: 20000000 VND"]
```

---

## 🔄 Init.java - Khởi Tạo Services

### Phương thức Khởi Tạo Riêng Lẻ

```java
CustomerService cs = Init.initCustomerService(customerManager);
AppointmentService as = Init.initAppointmentService(apptMgr, custMgr, svcMgr);
// etc.
```

### Khởi Tạo Toàn Bộ Cùng Lúc

```java
Object[] services = Init.initAllServices(
    appointmentManager, customerManager, discountManager,
    invoiceManager, serviceManager, transactionManager
);

// Lấy từng service
CustomerService cs = (CustomerService) services[0];
AppointmentService as = (AppointmentService) services[1];
InvoiceService is = (InvoiceService) services[2];
PaymentService ps = (PaymentService) services[3];
ReportService rs = (ReportService) services[4];
```

---

## ✅ Compliance Checklist

### Yêu Cầu Dùng Mảy (T[])

- ✅ `getCustomerAppointmentHistory()` → Appointment[]
- ✅ `getTopCustomersBySpending()` → Customer[]
- ✅ `getPaymentMethodStatistics()` → String[]
- ✅ Tất cả filter logic dùng `for` loop, không `List.stream()`

### Clean Code & OOP

- ✅ Tên rõ ràng (lowerCamelCase phương thức, UpperCamelCase class)
- ✅ Phương thức ngắn, một chức năng (SRP)
- ✅ Dependency Injection qua constructor
- ✅ Validation input trước xử lý
- ✅ Throw exception hoặc return null/false khi lỗi
- ✅ Không khởi tạo dependencies bên trong service

### Comment Tiếng Việt

- ✅ Javadoc cho tất cả public method
- ✅ @param, @return, @throws khi cần
- ✅ Comment logic phức tạp bằng Tiếng Việt
- ✅ Comment ngắn gọn, rõ ràng

### Khác

- ✅ ID sinh tự động: CUST*, APT*, INV*, TXN*
- ✅ Không sửa file Manager (chỉ sử dụng công khai API)
- ✅ Tất cả import không dùng wildcard
- ✅ Thụt lề 4 khoảng trắng

---

## 📌 Lưu Ý Kỹ Thuật

1. **CustomerService.addSpendingToCustomer()** cũng gọi `updateCustomerTier()` (không trực tiếp gọi)

   - Để đảm bảo mỗi khi chi tiêu, tier được cập nhật tự động

2. **AppointmentService.rescheduleAppointment()** xóa lịch cũ + tạo lịch mới

   - Vì Appointment model không có setter cho appointmentDateTime

3. **InvoiceService.createInvoiceForAppointment()** tính thuế tự động 10%

   - Luôn tính thuế, có thể áp dụng chiết khấu sau đó

4. **PaymentService** tích hợp với **CustomerService**

   - `processPaymentForInvoice()` gọi `customerService.addSpendingToCustomer()`
   - `processPaymentForInvoice()` gọi `customerService.updateCustomerTier()`

5. **ReportService.getTopCustomersBySpending()** sắp xếp kiểu Bubble Sort

   - Sắp xếp giảm dần theo totalSpent
   - Lấy top N phần tử từ mảy đã sắp xếp

6. **ReportService.getPaymentMethodStatistics()** trả về String[]
   - Mỗi element: "PAYMENT_METHOD: AMOUNT VND"
   - Đếm từ tất cả Transaction thành công

---

## 🎯 Test Scenarios (Gợi ý)

### CustomerService

```java
// Test 1: Đăng ký khách hàng
Customer c1 = customerService.registerNewCustomer(...);
assert c1.getMemberTier() == Tier.BRONZE;

// Test 2: Cộng chi tiêu → tier tự động nâng
customerService.addSpendingToCustomer(c1.getId(), new BigDecimal("5000000"));
Customer c1Updated = customerManager.getById(c1.getId());
assert c1Updated.getMemberTier() == Tier.GOLD;
```

### AppointmentService

```java
// Test 1: Đặt lịch hẹn
Appointment apt = appointmentService.bookAppointment(customerId, serviceId, futureTime);
assert apt.getStatus() == AppointmentStatus.SCHEDULED;

// Test 2: Hoàn thành lịch
appointmentService.startAppointment(apt.getId());
appointmentService.completeAppointment(apt.getId());
Appointment aptCompleted = appointmentManager.getById(apt.getId());
assert aptCompleted.getStatus() == AppointmentStatus.COMPLETED;
```

### PaymentService + InvoiceService

```java
// Test: Luồng thanh toán hoàn chỉnh
Invoice inv = invoiceService.createInvoiceForAppointment(aptId, PaymentMethod.CASH);
Transaction txn = paymentService.processPaymentForInvoice(inv.getId(), PaymentMethod.CASH);
assert txn.getStatus() == TransactionStatus.SUCCESS;
assert invoiceManager.getById(inv.getId()).isPaid() == true;
```

---

## 📊 Tóm Tắt Số Lượng

- **Services**: 5 (Customer, Appointment, Invoice, Payment, Report)
- **Phương thức public**: 28
- **Lines of Code**: ~1,200
- **Dòng Javadoc**: ~400
- **Mảy được trả về**: 3 loại (Appointment[], Customer[], String[])
