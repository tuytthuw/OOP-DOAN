# Kế Hoạch Kỹ Thuật #0007a - Xây Dựng BaseManager Generic Class

## 1. Mô Tả Ngữ Cảnh

Tạo lớp trừu tượng `BaseManager<T>` làm lớp cơ sở cho tất cả các Manager classes. Lớp này chứa các phương thức CRUD chung để tránh lặp code và cải thiện bảo trì.

---

## 2. Các Tệp và Hàm Liên Quan

### Tệp TẠO MỚI:

- `src/main/java/collections/BaseManager.java` - Lớp abstract generic cơ sở

### Tệp THAY ĐỔI:

- `src/main/java/collections/Init.java` - Không cần thay đổi trực tiếp
- Tất cả Manager classes (0007) sẽ extend `BaseManager<T>`

---

## 3. Thuật Toán / Logic (Từng Bước)

### 3.1 Cấu Trúc Lớp BaseManager<T>

```
abstract BaseManager<T>
├── Thuộc tính (Attributes):
│   └── items: List<T> (Danh sách lưu trữ các đối tượng)
│
├── Phương Thức Chung (Common Methods - Concrete):
│   ├── add(item: T): boolean
│   ├── update(item: T): boolean
│   ├── delete(id: String): boolean
│   ├── getById(id: String): T
│   ├── getAll(): List<T>
│   ├── size(): int
│   ├── clear(): void
│   ├── exists(id: String): boolean
│   └── getAllActive(): List<T> (cho các entity có isDeleted)
│
├── Phương Thức Trừu Tượng (Abstract - Lớp Con Override):
│   ├── getId(item: T): String (Lấy ID từ item)
│   ├── createDefaultInstance(): T (Tạo instance mặc định, nếu cần)
│   └── validateItem(item: T): boolean (Validate dữ liệu của item)
│
└── Phương Thức Bảo Vệ (Protected - Cho Lớp Con Sử Dụng):
    ├── findIndex(id: String): int (Tìm index của item)
    ├── sort(comparator): void (Sắp xếp danh sách)
    └── filter(predicate): List<T> (Lọc danh sách)
```

### 3.2 Logic Các Phương Thức Chung

#### **Logic `add(item: T)`:**

1. Kiểm tra `item` không null
   - Nếu null: log error, trả về false
2. Gọi `validateItem(item)` để validate
   - Nếu không hợp lệ: log error, trả về false
3. Lấy ID: `id = getId(item)`
4. Kiểm tra ID không trùng lặp (gọi `exists(id)`)
   - Nếu trùng: log warning, trả về false
5. Thêm item vào list: `items.add(item)`
6. Log success
7. Trả về true

```java
/**
 * Thêm một item mới vào danh sách
 * @param item Item cần thêm
 * @return true nếu thêm thành công, false nếu thất bại
 */
public boolean add(T item) {
    if (item == null) {
        logger.error("Item không được null");
        return false;
    }

    if (!validateItem(item)) {
        logger.error("Item không hợp lệ");
        return false;
    }

    String id = getId(item);
    if (exists(id)) {
        logger.warn("ID " + id + " đã tồn tại");
        return false;
    }

    items.add(item);
    logger.info("Thêm item thành công: " + id);
    return true;
}
```

#### **Logic `update(item: T)`:**

1. Kiểm tra `item` không null
2. Gọi `validateItem(item)` để validate
3. Lấy ID: `id = getId(item)`
4. Tìm index của item cũ: `index = findIndex(id)`
   - Nếu không tìm thấy (index == -1): trả về false
5. Thay thế item cũ bằng item mới: `items.set(index, item)`
6. Trả về true

#### **Logic `delete(id: String)`:**

1. Kiểm tra `id` không null/trống
2. Tìm index: `index = findIndex(id)`
   - Nếu không tìm thấy: trả về false
3. Xóa item: `items.remove(index)`
4. Trả về true

#### **Logic `getById(id: String)`:**

1. Kiểm tra `id` không null/trống
2. Lặp qua danh sách, tìm item có `getId(item).equals(id)`
3. Trả về item nếu tìm thấy, ngược lại trả về `null`

#### **Logic `exists(id: String)`:**

1. Trả về `getById(id) != null`

#### **Logic `getAllActive()`:**

1. Lọc tất cả items có `isDeleted == false` (nếu có thuộc tính này)
2. Trả về danh sách đã lọc

### 3.3 Phương Thức Bảo Vệ (Protected Helper Methods)

#### **Logic `findIndex(id: String): int`:**

```java
protected int findIndex(String id) {
    for (int i = 0; i < items.size(); i++) {
        if (getId(items.get(i)).equals(id)) {
            return i;
        }
    }
    return -1; // Không tìm thấy
}
```

#### **Logic `sort(comparator): void`:**

```java
protected void sort(Comparator<T> comparator) {
    items.sort(comparator);
}
```

#### **Logic `filter(predicate): List<T>`:**

```java
protected List<T> filter(Predicate<T> predicate) {
    return items.stream()
               .filter(predicate)
               .collect(Collectors.toList());
}
```

### 3.4 Cách Lớp Con Sử Dụng BaseManager

**Ví dụ: CustomerManager extends BaseManager<Customer>**

```java
public class CustomerManager extends BaseManager<Customer> {

    // Override các phương thức trừu tượng
    @Override
    protected String getId(Customer customer) {
        return customer.getCustomerId();
    }

    @Override
    protected boolean validateItem(Customer customer) {
        return customer.getCustomerId() != null
            && customer.getFullName() != null
            && !customer.getFullName().isEmpty();
    }

    // Thêm các phương thức tìm kiếm riêng cho Customer
    public Customer findByPhone(String phoneNumber) {
        return this.filter(c -> c.getPhoneNumber().equals(phoneNumber))
                   .stream()
                   .findFirst()
                   .orElse(null);
    }

    public List<Customer> findByTier(Tier tier) {
        return this.filter(c -> c.getTier() == tier);
    }
}
```

---

## 4. Lợi Ích Của BaseManager

| Lợi Ích            | Mô Tả                                                  |
| ------------------ | ------------------------------------------------------ |
| **Tránh Lặp Code** | Không phải viết CRUD cơ bản cho mỗi Manager            |
| **Dễ Bảo Trì**     | Sửa lỗi ở BaseManager → tất cả Manager được sửa        |
| **Consistent API** | Tất cả Manager có cùng interface công khai             |
| **Tăng Hiệu Suất** | Có thể optimize BaseManager → cải thiện tất cả Manager |
| **OOP Đúng Cách**  | Sử dụng Generics, Inheritance, Polymorphism            |

---

## 5. Ghi Chú Kỹ Thuật

- `BaseManager<T>` là **abstract class**, không thể instantiate trực tiếp
- Sử dụng **Generic Type `<T>`** để lưu trữ bất kỳ loại đối tượng nào
- Sử dụng `List<T>` (ArrayList implementation) để lưu trữ dữ liệu
- Các lớp con phải override:
  - `getId(item: T)` - Lấy ID từ item
  - `validateItem(item: T)` - Validate dữ liệu item
- Phương thức `filter()` sử dụng Java Streams API
- Comment bằng tiếng Việt cho Javadoc và logic phức tạp
- Nên thêm logging (log4j/slf4j) để debug dễ hơn

---

## 6. Mối Quan Hệ Với Các Lớp Khác

```
BaseManager<T> (Abstract)
├── CustomerManager extends BaseManager<Customer>
├── ServiceManager extends BaseManager<Service>
├── AppointmentManager extends BaseManager<Appointment>
├── TransactionManager extends BaseManager<Transaction>
├── DiscountManager extends BaseManager<Discount>
└── InvoiceManager extends BaseManager<Invoice>
```

---

## 7. Cách Tích Hợp Vào Kế Hoạch 0007

**Thứ tự thực hiện:**

1. **Thực hiện 0007a trước tiên:** Tạo BaseManager<T>
2. **Cập nhật 0007:** Tất cả Manager classes sẽ extend BaseManager<T>
3. **Lợi ích:** Giảm code từ ~500 lines xuống ~250 lines cho 0007

---

## 8. Lưu Ý Quan Trọng

- ⚠️ **Ưu tiên:** Kế hoạch 0007a phải được thực hiện **TRƯỚC** 0007
- ⚠️ **Tương thích:** Kế hoạch 0007 sẽ được điều chỉnh để sử dụng BaseManager<T>
- ✅ **Tiết kiệm:** Giảm significantly các phương thức lặp lại
