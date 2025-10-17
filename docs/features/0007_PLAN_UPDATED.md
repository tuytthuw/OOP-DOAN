# Kế Hoạch Kỹ Thuật #0007 - Xây Dựng Collection Manager (CẬP NHẬT: Dùng Mảng T[])

## 1. Mô Tả Ngữ Cảnh

Tạo các lớp quản lý tập hợp dữ liệu (Collection Manager) để lưu trữ và quản lý các danh sách khách hàng, dịch vụ, lịch hẹn, giao dịch, chiết khấu và hóa đơn.

**⚠️ QUAN TRỌNG**: Tuân thủ instructions - **KHÔNG dùng java.util.List/ArrayList**, phải dùng **mảng T[]** (Dynamic Array)

---

## 2. Các Tệp và Hàm Liên Quan

### Tệp TẠO MỚI:

- `src/main/java/collections/BaseManager.java` - Lớp generic cơ sở cho tất cả managers (sử dụng mảng)
- `src/main/java/collections/CustomerManager.java` - Quản lý danh sách khách hàng
- `src/main/java/collections/ServiceManager.java` - Quản lý danh sách dịch vụ
- `src/main/java/collections/AppointmentManager.java` - Quản lý danh sách lịch hẹn
- `src/main/java/collections/TransactionManager.java` - Quản lý danh sách giao dịch
- `src/main/java/collections/DiscountManager.java` - Quản lý danh sách chiết khấu
- `src/main/java/collections/InvoiceManager.java` - Quản lý danh sách hóa đơn

### Tệp THAY ĐỔI:

- `src/main/java/collections/Init.java` - Thêm khởi tạo tất cả managers

---

## 3. Thuật Toán / Logic (Từng Bước)

### 3.1 Cấu Trúc BaseManager<T> Generic (Dùng Mảng)

**Các Thuộc Tính:**

```
BaseManager<T>
├── items: T[] (Mảng lưu trữ các đối tượng)
├── size: int (Số lượng phần tử hiện tại)
├── capacity: int (Dung lượng hiện tại của mảng)
└── DEFAULT_CAPACITY: int (= 10 - dung lượng ban đầu)
```

**Phương Thức Chung (CRUD):**

```
├── add(T): boolean
│   Logic:
│   1. Kiểm tra item != null
│   2. Kiểm tra ID không trùng (gọi exists(id))
│   3. Nếu size >= capacity: gọi resize() để tăng dung lượng
│   4. Thêm item: items[size] = item; size++
│   5. Trả về true
│
├── update(T): boolean
│   Logic:
│   1. Kiểm tra item != null
│   2. Tìm vị trí của item trong mảng (dựa trên ID)
│   3. Nếu tìm thấy: thay thế items[index] = item; trả về true
│   4. Nếu không tìm: trả về false
│
├── delete(id): boolean
│   Logic (Soft-delete hoặc Hard-delete):
│   1. Tìm vị trí item có ID tương ứng
│   2. Nếu không tìm: trả về false
│   3. Xóa: dịch chuyển các phần tử sau lên một vị trí
│   4. size--
│   5. Trả về true
│
├── getById(id): T
│   Logic:
│   1. Duyệt mảng từ 0 đến size-1
│   2. So sánh ID với getItemId(items[i])
│   3. Nếu tìm thấy: trả về items[i]
│   4. Nếu hết mảng: trả về null
│
├── getAll(): T[]
│   Logic:
│   1. Tạo mảng mới: copy[] = new T[size]
│   2. Copy từng phần tử từ items vào copy
│   3. Trả về copy (để tránh thay đổi từ bên ngoài)
│
├── size(): int
│   Logic: Trả về biến size
│
├── clear(): void
│   Logic:
│   1. Đặt lại size = 0
│   2. Không cần xóa các phần tử cũ
│
├── exists(id): boolean
│   Logic: return getById(id) != null
│
└── resize(): void (Phương thức riêng)
    Logic (Tăng gấp đôi dung lượng):
    1. newCapacity = capacity * 2
    2. newItems = new T[newCapacity]
    3. Copy tất cả items cũ sang newItems
    4. items = newItems
    5. capacity = newCapacity
```

### 3.2 CustomerManager - Quản Lý Khách Hàng

**Extends**: BaseManager<Customer>

**Phương thức bổ sung:**

```
├── findByPhone(phoneNumber): Customer
│   Logic:
│   1. Duyệt mảng items từ 0 đến size-1
│   2. So sánh items[i].getPhoneNumber() == phoneNumber
│   3. Trả về customer nếu tìm thấy, null nếu không
│
├── findByEmail(email): Customer
│   Logic: Tương tự findByPhone nhưng so sánh email
│
├── findByTier(tier): Customer[]
│   Logic:
│   1. Tạo mảng temp để lưu kết quả
│   2. Duyệt items, lọc các customer có tierMembership == tier
│   3. Trả về mảng kết quả (hoặc mảng trống nếu không tìm)
│
├── findActiveCustomers(): Customer[]
│   Logic:
│   1. Lọc customer có isActive == true && isDeleted == false
│   2. Trả về mảng kết quả
│
└── getTopSpenders(limit): Customer[]
    Logic:
    1. Sao chép mảng items
    2. Sắp xếp giảm dần theo totalSpent
    3. Lấy limit phần tử đầu
    4. Trả về mảng kết quả
```

### 3.3 ServiceManager - Quản Lý Dịch Vụ

**Extends**: BaseManager<Service>

**Phương thức bổ sung:**

```
├── findByCategory(category): Service[]
│   Logic: Lọc service có serviceCategory == category
│
├── findByName(namePattern): Service[]
│   Logic: Lọc service có serviceName chứa namePattern
│
├── findActiveServices(): Service[]
│   Logic: Lọc service có isActive == true
│
├── findByPriceRange(minPrice, maxPrice): Service[]
│   Logic: Lọc service có basePrice trong [minPrice, maxPrice]
│
└── getServicesByDuration(duration): Service[]
    Logic: Lọc service có durationMinutes xấp xỉ duration
```

### 3.4 AppointmentManager - Quản Lý Lịch Hẹn

**Extends**: BaseManager<Appointment>

**Phương thức bổ sung:**

```
├── findByCustomerId(customerId): Appointment[]
│   Logic: Lọc appointment có customerId tương ứng
│
├── findByServiceId(serviceId): Appointment[]
│   Logic: Lọc appointment có serviceId tương ứng
│
├── findByStatus(status): Appointment[]
│   Logic: Lọc appointment có status tương ứng
│
├── findByDateRange(startDate, endDate): Appointment[]
│   Logic: Lọc appointment có appointmentDateTime trong [startDate, endDate]
│
├── findUpcomingAppointments(): Appointment[]
│   Logic: Lọc appointment có status == SCHEDULED && appointmentDateTime > now
│
└── getExpiredAppointments(): Appointment[]
    Logic: Lọc appointment có status == SCHEDULED && appointmentDateTime < now
```

### 3.5 TransactionManager - Quản Lý Giao Dịch

**Extends**: BaseManager<Transaction>

**Phương thức bổ sung:**

```
├── findByCustomerId(customerId): Transaction[]
│   Logic: Lọc transaction có customerId tương ứng
│
├── findByStatus(status): Transaction[]
│   Logic: Lọc transaction có status tương ứng
│
├── findByPaymentMethod(method): Transaction[]
│   Logic: Lọc transaction có paymentMethod tương ứng
│
├── findByDateRange(startDate, endDate): Transaction[]
│   Logic: Lọc transaction có transactionDate trong [startDate, endDate]
│
└── getTotalRevenue(startDate, endDate): BigDecimal
    Logic:
    1. Duyệt transaction trong dateRange
    2. Chỉ tính transaction có status == SUCCESS
    3. Cộng tất cả amount
    4. Trả về tổng
```

### 3.6 DiscountManager - Quản Lý Chiết Khấu

**Extends**: BaseManager<Discount>

**Phương thức bổ sung:**

```
├── findByCode(discountCode): Discount
│   Logic: Tìm discount có discountCode tương ứng
│
├── findActiveDiscounts(): Discount[]
│   Logic: Lọc discount có isActive == true && hợp lệ ngày hôm nay
│
├── findByType(type): Discount[]
│   Logic: Lọc discount có type == DiscountType.PERCENTAGE hoặc FIXED_AMOUNT
│
├── findValidDiscounts(date): Discount[]
│   Logic: Lọc discount có ngày hợp lệ cho date và isActive == true
│
└── findDiscountsByCodePattern(pattern): Discount[]
    Logic: Lọc discount có discountCode chứa pattern
```

### 3.7 InvoiceManager - Quản Lý Hóa Đơn

**Extends**: BaseManager<Invoice>

**Phương thức bổ sung:**

```
├── findByCustomerId(customerId): Invoice[]
│   Logic: Lọc invoice có customerId tương ứng
│
├── findByAppointmentId(appointmentId): Invoice
│   Logic: Tìm invoice có appointmentId tương ứng
│
├── findByPaymentStatus(isPaid): Invoice[]
│   Logic: Lọc invoice theo trạng thái thanh toán
│
├── findByDateRange(startDate, endDate): Invoice[]
│   Logic: Lọc invoice có issueDate trong [startDate, endDate]
│
└── getTotalRevenue(startDate, endDate): BigDecimal
    Logic:
    1. Duyệt invoice trong dateRange
    2. Chỉ tính invoice có isPaid == true
    3. Cộng tất cả totalAmount
    4. Trả về tổng doanh thu
```

---

## 4. Chi Tiết Resize Logic (Quan Trọng)

```
Quá trình Resize (Tăng gấp đôi dung lượng):

Trước khi thêm:
  items[] = [C1, C2, C3, C4, C5, ...]  (capacity = 10)
  size = 10

Khi size == capacity:
  1. Tạo mảng mới: newItems[] (capacity = 20)
  2. Copy: for i = 0 to size-1: newItems[i] = items[i]
  3. items = newItems
  4. capacity = 20

Sau Resize:
  items[] = [C1, C2, C3, C4, C5, ..., null, null, ...]  (capacity = 20)
  size = 10

Có thể tiếp tục thêm 10 phần tử nữa mà không cần resize
```

---

## 5. Ghi Chú Kỹ Thuật

- **Không dùng java.util.List/ArrayList** - tuân thủ instructions
- **Dùng mảng T[]** với dynamic resizing
- Mảng kết quả từ các phương thức tìm kiếm phải có `size` hợp lệ hoặc tạo mảng mới với kích thước đúng
- **Soft-delete** có thể implement bằng việc giữ lại item nhưng đánh dấu isDeleted = true
- **Hard-delete** xóa vật lý khỏi mảng bằng dịch chuyển phần tử
- Comment bằng tiếng Việt cho Javadoc và logic phức tạp
- Không copy mảng nếu không cần thiết (để tiết kiệm bộ nhớ)

---

## 6. Lợi Ích Của Cách Tiếp Cận Này

✅ **Tuân thủ instructions** - Không dùng collection framework
✅ **Learning OOP** - Hiểu rõ hơn về quản lý bộ nhớ
✅ **Dynamic allocation** - Tập làm việc với mảng động
✅ **Custom data structure** - Xây dựng data structure riêng
✅ **Performance** - Kiểm soát tốt hơn hiệu năng

---

## 7. Lưu Ý Quan Trọng

- Khi mảng kết quả trả về rỗng, nên trả về mảng rỗng (size = 0) chứ không phải null
- Phương thức resize nên được gọi tự động khi cần (trong phương thức add)
- Nên có hằng số DEFAULT_CAPACITY để xác định kích thước ban đầu
- Có thể thêm phương thức shrink() để giảm dung lượng khi có quá nhiều phần tử trống
