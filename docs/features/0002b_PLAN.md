# Kế Hoạch Kỹ Thuật #0002b - Xây Dựng Interface Sellable

## 1. Mô Tả Ngữ Cảnh

Tạo interface `Sellable` để định nghĩa hành vi chung cho các đối tượng có thể bán được trong hệ thống spa (Service, Product). Interface này cung cấp contract cho các lớp implement với các phương thức cần thiết để hiển thị, quản lý giá, và xử lý input.

---

## 2. Các Tệp và Hàm Liên Quan

### Tệp TẠO MỚI:

- `src/main/java/interfaces/Sellable.java` - Interface cho các item có thể bán

### Tệp THAY ĐỔI:

- `src/main/java/models/Service.java` - Implement Sellable interface
- `src/main/java/models/Product.java` - Implement Sellable interface (nếu có)

---

## 3. Thuật Toán / Logic (Từng Bước)

### 3.1 Cấu Trúc Interface Sellable

**Lưu ý:** Service/Product sẽ implement cả `IEntity` và `Sellable`. Các phương thức `getId()`, `display()`, `input()` đã có trong `IEntity`, nên `Sellable` chỉ định nghĩa các phương thức **riêng cho việc bán hàng**.

```java
public interface Sellable {

    /**
     * Lấy giá bán của item
     * @return Giá bán (BigDecimal)
     */
    BigDecimal getPrice();

    /**
     * Lấy giá bán dưới dạng chuỗi định dạng
     * @return Giá dạng "1.000.000 VND"
     */
    String getPriceFormatted();

    /**
     * Kiểm tra item có khả dụng (có thể bán) không
     * @return true nếu khả dụng, false nếu không
     */
    boolean isAvailable();

    /**
     * Lấy mô tả chi tiết của item
     * @return Mô tả
     */
    String getDescription();
}
```

### 3.2 Lý Do Tạo Interface Sellable

**Lợi ích:**

1. **Separation of Concerns:** Tách riêng interface quản lý Entity (`IEntity`) từ interface bán hàng (`Sellable`)
2. **Single Responsibility:** `IEntity` xử lý ID, display, input chung; `Sellable` xử lý logic bán hàng (giá, khả dụng, mô tả)
3. **Polymorphism:** Service và Product có thể được sử dụng thay thế cho nhau ở những nơi cần Sellable
4. **Flexibility:** Dễ thêm loại item mới (ví dụ: Package, Bundle) chỉ cần implement `IEntity` + `Sellable`
5. **Reusability:** Tất cả item có thể bán đều implement các phương thức bán hàng cần thiết

**Ví dụ:**

```java
// Quản lý tất cả item có thể bán chung một list
Sellable[] items = getAllSellableItems();

// Hiển thị thông tin và giá tất cả item khả dụng
for (Sellable item : items) {
    if (item.isAvailable()) {
        // item.display() từ IEntity
        // item.getPrice() từ Sellable
        System.out.println("Giá: " + item.getPriceFormatted());
    }
}
```

### 3.3 Phương Thức Chi Tiết

#### **`getPrice(): BigDecimal`**

```
Mục đích: Trả về giá bán
Implement:
  - Service: trả về basePrice
  - Product: trả về basePrice
Ví dụ: BigDecimal.valueOf(500000)
```

#### **`getPriceFormatted(): String`**

```
Mục đích: Trả về giá dạng chuỗi định dạng
Logic:
  1. Lấy giá: BigDecimal price = getPrice()
  2. Format với dấu phân cách: "500.000"
  3. Thêm đơn vị: "500.000 VND"
Ví dụ: "500.000 VND", "1.000.000 VND"
```

#### **`isAvailable(): boolean`**

```
Mục đích: Kiểm tra item có khả dụng (có thể bán) không
Logic (Service):
  - Trả về: isActive == true
Logic (Product):
  - Trả về: (stockQuantity > 0 && !isExpired() && isActive)
Ví dụ:
  - Dịch vụ không active → false
  - Sản phẩm hết hạn → false
  - Sản phẩm hết kho → false
```

#### **`getDescription(): String`**

```
Mục đích: Lấy mô tả chi tiết
Logic:
  - Service: trả về description
  - Product: trả về description hoặc "Brand: X, Unit: Y"
Ví dụ: "Xoa bóp toàn thân giúp thư giãn, giảm stress"
```

### 3.4 Implement trong Service

```java
public class Service implements IEntity, Sellable {

    // Từ IEntity:
    @Override
    public String getId() {
        return this.serviceId;
    }

    @Override
    public void display() {
        System.out.println("┌─────────────────────────────┐");
        System.out.println("│ ID: " + getId());
        System.out.println("│ Tên: " + getName());
        System.out.println("│ Giá: " + getPriceFormatted());
        System.out.println("│ Thời gian: " + getDurationFormatted());
        System.out.println("│ Mô tả: " + getDescription());
        System.out.println("└─────────────────────────────┘");
    }

    @Override
    public void input() {
        // Nhập từ người dùng (ID, tên, giá, mô tả, v.v.)
    }

    @Override
    public String getPrefix() {
        return "SVC";
    }

    // Từ Sellable:
    @Override
    public BigDecimal getPrice() {
        return this.basePrice;
    }

    @Override
    public String getPriceFormatted() {
        // Format: "500.000 VND"
        return String.format("%,d VND",
            this.basePrice.longValue()).replace(",", ".");
    }

    @Override
    public boolean isAvailable() {
        return this.isActive;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
```

---

## 4. Ghi Chú Kỹ Thuật

- Interface `Sellable` định nghĩa contract cho các item có thể bán
- Các lớp implement phải cung cấp implementation cho tất cả phương thức
- `getPriceFormatted()` nên format giá theo tiền Việt (VND, dấu phân cách hàng nghìn)
- `isAvailable()` logic khác nhau tùy theo loại item (Service vs Product)
- Comment bằng tiếng Việt cho Javadoc

---

## 5. Mối Quan Hệ Với Các Lớp Khác

```
IEntity (Interface)
├── getId()
├── display()
├── input()
└── getPrefix()

Sellable (Interface)
├── getPrice()
├── getPriceFormatted()
├── isAvailable()
└── getDescription()

Service implements IEntity, Sellable
├── Từ IEntity: getId(), display(), input(), getPrefix()
└── Từ Sellable: getPrice(), getPriceFormatted(), isAvailable(), getDescription()

Product implements IEntity, Sellable (nếu tạo)
├── Từ IEntity: getId(), display(), input(), getPrefix()
└── Từ Sellable: getPrice(), getPriceFormatted(), isAvailable(), getDescription()
```

**Cách Sử Dụng:**

```java
// Quản lý tất cả item có thể bán (Service, Product, v.v.)
Sellable[] allItems = getAllSellableItems();

// Hiển thị thông tin và kiểm tra khả dụng
for (Sellable item : allItems) {
    if (item.isAvailable()) {
        // item.display() từ IEntity
        System.out.println("Giá: " + item.getPriceFormatted());
        System.out.println("Mô tả: " + item.getDescription());
    }
}
```

---

## 6. Ưu Tiên Thực Hiện

- ✅ Ưu tiên: 🟡 TRUNG BÌNH
- ✅ Thực hiện: Cùng lúc với 0002 (Service Model)
- ✅ Lợi ích: Service và Product sử dụng interface chung

---

## 7. Lưu Ý Quan Trọng

- Interface `Sellable` nên được tạo **cùng lúc hoặc TRƯỚC** khi tạo Service.java
- Service/Product phải implement **cả** `IEntity` **và** `Sellable`
- Các phương thức `getId()`, `display()`, `input()` đã có trong `IEntity`, không lặp lại trong `Sellable`
- Chỉ định nghĩa trong `Sellable` những phương thức **riêng cho việc bán hàng** (getPrice, getPriceFormatted, isAvailable, getDescription)
- Comment bằng tiếng Việt cho Javadoc theo quy tắc
