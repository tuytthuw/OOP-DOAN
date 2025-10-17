# ✅ Fix ClassCastException - Giải pháp Abstract getAll()

## 📋 Tóm tắt vấn đề

**Lỗi gốc:**

```
class [Linterfaces.IEntity; cannot be cast to class [Lmodels.Customer;
```

**Nguyên nhân:**

- `BaseManager<T>` sử dụng generics nhưng Java không thể tạo `T[]` trực tiếp ở runtime
- Cách cũ tạo mảy `Object[]` hoặc `IEntity[]` rồi ép kiểu toàn bộ mảy
- Khi UI gọi `CustomerManager.getAll()`, nó nhận `IEntity[]` và cố ép sang `Customer[]` → lỗi ClassCastException

## 🔧 Giải pháp Applied

### 1. **BaseManager - Để mảy nội bộ là IEntity[]**

```java
// Constructor
this.items = (T[]) new IEntity[capacity];  // ✅ Mảy nội bộ là IEntity[]

// Trong resize()
T[] newItems = (T[]) new IEntity[newCapacity];  // ✅ Tạo IEntity[]
```

**Lợi ích:**

- `IEntity` là interface mà tất cả các entity implement
- Runtime type checking hợp lệ vì `Customer implements IEntity`

### 2. **getAll() - Phương thức trừu tượng**

Thay vì cài đặt trong `BaseManager`:

```java
// ❌ TRƯỚC (Gây lỗi)
public T[] getAll() {
    T[] result = (T[]) new Object[size];  // Casting mảy → lỗi
    ...
}
```

Giải pháp mới:

```java
// ✅ SAU (Từng lớp con cài đặt)
public abstract T[] getAll();
```

### 3. **Mỗi lớp con cài đặt getAll() riêng**

**Ví dụ CustomerManager:**

```java
@Override
public Customer[] getAll() {
    // Tạo mảy Customer mới
    Customer[] result = new Customer[size];
    for (int i = 0; i < size; i++) {
        // ✅ Cast từng phần tử một (IEntity → Customer)
        // Điều này luôn hợp lệ vì items[i] là Customer
        result[i] = (Customer) items[i];
    }
    return result;
}
```

**Tương tự cho tất cả các Manager:**

- `AppointmentManager` → `(Appointment) items[i]`
- `EmployeeManager` → `(Employee) items[i]`
- `ServiceManager` → `(Service) items[i]`
- `InvoiceManager` → `(Invoice) items[i]`
- `TransactionManager` → `(Transaction) items[i]`
- `DiscountManager` → `(Discount) items[i]`

## 🎯 Tại sao giải pháp này hoạt động

### Casting phần tử vs Casting mảy

| Loại               | Mã                             | Kết quả                             |
| ------------------ | ------------------------------ | ----------------------------------- |
| ❌ Casting mảy     | `(Customer[]) new IEntity[10]` | ClassCastException                  |
| ✅ Casting phần tử | `(Customer) entity`            | Thành công (nếu entity là Customer) |

### Luồng hoạt động

1. **Tạo Manager**: `customerManager = new CustomerManager()`

   - Constructor gọi `super()` → tạo `items = new IEntity[10]`

2. **Thêm khách hàng**: `add(customer)`

   - Thêm `Customer` object vào `items[0]`
   - Runtime biết `items[0]` thực sự là `Customer`

3. **Lấy danh sách**: `getAll()`

   - `CustomerManager.getAll()` chạy
   - Tạo mảy `Customer[]` mới
   - Với mỗi `items[i]`, cast `(Customer) items[i]`
   - Cast phần tử → thành công ✅

4. **Sử dụng ở UI**
   - Nhận `Customer[]` từ `getAll()`
   - Không cần ép kiểu → không lỗi ✅

## 📊 Các tệp đã cập nhật

| Tệp                       | Thay đổi                                           |
| ------------------------- | -------------------------------------------------- |
| `BaseManager.java`        | - Thay đổi `getAll()` thành phương thức trừu tượng |
| `CustomerManager.java`    | + Cài đặt `getAll()` với casting phần tử           |
| `AppointmentManager.java` | + Cài đặt `getAll()` với casting phần tử           |
| `EmployeeManager.java`    | + Cài đặt `getAll()` với casting phần tử           |
| `ServiceManager.java`     | + Cài đặt `getAll()` với casting phần tử           |
| `InvoiceManager.java`     | + Cài đặt `getAll()` với casting phần tử           |
| `TransactionManager.java` | + Cài đặt `getAll()` với casting phần tử           |
| `DiscountManager.java`    | + Cài đặt `getAll()` với casting phần tử           |

## ✅ Ưu điểm của giải pháp

1. **Không có casting mảy** → Không bao giờ xảy ra lỗi ClassCastException
2. **Type-safe** → Mỗi lớp con biết chính xác kiểu nó trả về
3. **Mạnh mẽ** → Compile-time checking và runtime correctness
4. **Rõ ràng** → Mỗi Manager tự quản lý cách nó trả về dữ liệu

## 🧪 Kiểm tra

```
✅ Thêm khách hàng - hoạt động
✅ Xem tất cả khách hàng - hoạt động
✅ Tìm kiếm khách hàng - hoạt động
✅ Các hoạt động khác - hoạt động
```

---

**Status:** ✅ **FIXED**  
**Ngày cập nhật:** October 17, 2025
