# 📋 CẬP NHẬT TẤT CẢ CÁC KẾ HOẠCH - TUÂN THỦ INSTRUCTIONS (Dùng Mảng T[] thay vì List)

## ⚠️ PHÁT HIỆN LỖI QUAN TRỌNG

**Instruction yêu cầu:**

```
Cấm: Không dùng java.util.List/ArrayList và cũng không dùng Set/HashSet/Map/HashMap hay các implementation tương tự.
Lưu trữ dữ liệu: Dùng mảng (Type[]) hoặc tự triển khai cấu trúc dữ liệu (dynamic array, linked list, hoặc hashtable do bạn viết).
DataStore<T>: thực thi bằng T[] list với API gợi ý: add, update(id), delete(id), findById, findByCondition, getAll, sort, count.
```

**Vấn đề hiện tại:**

- ❌ Kế hoạch 0007 (Collection Manager) sử dụng `java.util.List<T>`
- ❌ Các kế hoạch sau (0008-0012) cũng sẽ gặp vấn đề tương tự

---

## ✅ GIẢI PHÁP

Cập nhật tất cả các kế hoạch để sử dụng:

1. **Mảng động T[]** thay vì List
2. **BaseManager<T>** generic class với mảng nội bộ
3. Tự triển khai **resize logic** khi mảng đầy

---

## 📋 DANH SÁCH CÁC KẾ HOẠCH CẦN CẬP NHẬT

### ✅ Kế Hoạch 0007 - Xây Dựng Collection Manager

**Trạng thái**: Đã tạo file `0007_PLAN_UPDATED.md`

**Thay đổi chính:**

- Sử dụng `T[]` thay vì `List<T>`
- BaseManager quản lý:
  - `items: T[]` - mảng lưu trữ
  - `size: int` - số phần tử hiện tại
  - `capacity: int` - dung lượng hiện tại
- Phương thức `resize()` tự động tăng gấp đôi dung lượng khi cần
- Tất cả phương thức tìm kiếm trả về mảng `T[]` thay vì `List<T>`

**Các Manager con:**

- CustomerManager extends BaseManager<Customer>
- ServiceManager extends BaseManager<Service>
- AppointmentManager extends BaseManager<Appointment>
- TransactionManager extends BaseManager<Transaction>
- DiscountManager extends BaseManager<Discount>
- InvoiceManager extends BaseManager<Invoice>

---

### 🔄 Kế Hoạch 0008 - Xây Dựng Business Logic Services

**Sẽ được cập nhật để:**

- Sử dụng các Manager (chứ không là List)
- Tất cả input/output sử dụng mảng `T[]`
- Không import java.util.\* collections

**Các Services:**

- `CustomerService` - CRUD customer, tier management, search
- `AppointmentService` - Booking, status management, conflict checking
- `InvoiceService` - Invoice generation, calculation
- `PaymentService` - Payment processing, refund
- `ReportService` - Reports with aggregation

---

### 🔄 Kế Hoạch 0009 - Xây Dựng Input/Output Manager

**Sẽ được cập nhật để:**

- `InputHandler` - Nhập dữ liệu từ console (vẫn không thay đổi nhiều)
- `OutputFormatter` - Định dạng output (vẫn không thay đổi nhiều)
- `FileHandler` - Lưu/tải dữ liệu từ file (có thể thêm logic convert mảng ↔ file)

---

### 🔄 Kế Hoạch 0010 - Xây Dựng Giao Diện Menu

**Sẽ được cập nhật để:**

- Sử dụng các Manager và Service
- Hiển thị mảng dữ liệu thay vì List
- Menu các phương thức CRUD trên mảng

---

### 🔄 Kế Hoạch 0011 - Xây Dựng Employee/Staff

**Sẽ được cập nhật để:**

- Employee model (kế thừa Person)
- EmployeeManager extends BaseManager<Employee>
- Tất cả logic sử dụng mảng `Employee[]`

---

### 🔄 Kế Hoạch 0012 - Exception Handling & Custom Exceptions

**Sẽ được cập nhật để:**

- Tất cả Manager method throw custom exceptions
- Service layer xử lý exception
- UI layer hiển thị lỗi thân thiện

---

## 🎯 NGUYÊN TẮC CHUNG KHI CẬP NHẬT

### ❌ KHÔNG ĐƯỢC DÀI:

```java
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;

// ❌ SAI
List<Customer> customers = new ArrayList<>();
```

### ✅ PHẢI DÙNG:

```java
// ✅ ĐÚNG - Dùng mảng
Customer[] customers = new Customer[10];
int size = 0;

// Hoặc dùng BaseManager
CustomerManager manager = new CustomerManager();
Customer[] allCustomers = manager.getAll();
```

### ✅ PHƯƠNG THỨC TRẢ VỀ MẢNG:

```java
// ✅ ĐÚNG - Trả về mảng
public Customer[] findByTier(Tier tier) {
    Customer[] result = new Customer[size];
    int count = 0;
    for (int i = 0; i < size; i++) {
        if (items[i].getMemberTier() == tier) {
            result[count++] = items[i];
        }
    }
    // Thay đổi kích thước mảng nếu cần
    Customer[] finalResult = new Customer[count];
    for (int i = 0; i < count; i++) {
        finalResult[i] = result[i];
    }
    return finalResult;
}
```

---

## 📝 HÀNH ĐỘNG CỤ THỂ

### Bước 1: Triển khai BaseManager<T> với Mảng

- ✅ Attributes: `items[]`, `size`, `capacity`
- ✅ Methods: `add()`, `update()`, `delete()`, `getById()`, `getAll()`, `exists()`, `clear()`, `size()`
- ✅ Private method: `resize()` để tăng dung lượng gấp đôi

### Bước 2: Triển khai 6 Managers

- ✅ CustomerManager extends BaseManager<Customer>
- ✅ ServiceManager extends BaseManager<Service>
- ✅ AppointmentManager extends BaseManager<Appointment>
- ✅ TransactionManager extends BaseManager<Transaction>
- ✅ DiscountManager extends BaseManager<Discount>
- ✅ InvoiceManager extends BaseManager<Invoice>

### Bước 3: Cập nhật kế hoạch 0008-0012

- Đảm bảo không dùng java.util collections
- Tất cả input/output sử dụng mảng

---

## 💡 LỢI ÍC CỦA CÁC ĐỔI

✅ **Tuân thủ 100% instructions**
✅ **Chứng tỏ hiểu biết về OOP**
✅ **Tập làm việc với bộ nhớ trực tiếp**
✅ **Hiểu sâu hơn về data structure**
✅ **Không phụ thuộc Java Collections Framework**

---

## 🚀 TIẾP THEO

1. Triển khai BaseManager<T> với mảng động
2. Triển khai 6 Manager classes
3. Cập nhật Init.java để khởi tạo managers
4. Cập nhật các kế hoạch 0008-0012 để phù hợp
