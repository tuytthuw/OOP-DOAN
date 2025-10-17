# Kế Hoạch Kỹ Thuật #0002b - Xây Dựng Interface Sellable

## 1. Mô Tả Ngữ Cảnh

Tạo interface `Sellable` để định nghĩa hành vi chung cho các đối tượng có thể bán được trong hệ thống spa (Service, Product). Interface này cung cấp contract cho các lớp implement với các phương thức cần thiết để hiển thị, quản lý giá, và xử lý input.

---

## 2. Các Tệp và Hàm Liên Quan

### Tệp TẠO MỚI:

- `src/main/java/Interfaces/Sellable.java` - Interface cho các item có thể bán

### Tệp THAY ĐỔI:

- `src/main/java/models/Service.java` - Implement Sellable interface
- `src/main/java/models/Product.java` - Implement Sellable interface (nếu có)

---

## 3. Thuật Toán / Logic (Từng Bước)

### 3.1 Cấu Trúc Interface Sellable

```java
public interface Sellable {

    /**
     * Lấy ID duy nhất của item
     * @return ID của item
     */
    String getId();

    /**
     * Lấy tên của item có thể bán
     * @return Tên item
     */
    String getName();

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
     * Hiển thị thông tin item ra console
     */
    void display();

    /**
     * Nhập thông tin item từ người dùng
     */
    void input();

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

1. **Polymorphism:** Service và Product có thể được sử dụng thay thế cho nhau ở những nơi cần Sellable
2. **Contract:** Đảm bảo tất cả item có thể bán đều implement các phương thức cần thiết
3. **Flexibility:** Dễ thêm loại item mới (ví dụ: Package, Bundle) chỉ cần implement Sellable
4. **Separation of Concerns:** Tách rõ interface bán hàng từ business logic

**Ví dụ:**

```java
// Trước (không có interface)
List<Service> services = getAllServices();
services.forEach(s -> s.display());

List<Product> products = getAllProducts();
products.forEach(p -> p.display());
// → Lặp code

// Sau (có interface)
List<Sellable> items = getAllSellableItems();
items.forEach(item -> item.display());
// → Chỉ một vòng lặp, xử lý tất cả
```

### 3.3 Phương Thức Chi Tiết

#### **`getId(): String`**

```
Mục đích: Trả về ID duy nhất của item
Implement:
  - Service: trả về serviceId
  - Product: trả về productId
Ví dụ: "SVC_001", "PRD_001"
```

#### **`getName(): String`**

```
Mục đích: Trả về tên item
Implement:
  - Service: trả về serviceName
  - Product: trả về productName
Ví dụ: "Massage toàn thân", "Serum dưỡng da"
```

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

#### **`display(): void`**

```
Mục đích: Hiển thị thông tin item ra console
Logic:
  1. In ID
  2. In tên
  3. In giá định dạng
  4. In mô tả (nếu cần)
Ví dụ:
  ┌─────────────────────────────┐
  │ ID: SVC_001                 │
  │ Tên: Massage toàn thân      │
  │ Giá: 500.000 VND            │
  │ Thời gian: 60 phút          │
  └─────────────────────────────┘
```

#### **`input(): void`**

```
Mục đích: Nhập thông tin item từ người dùng
Logic:
  1. Nhập ID
  2. Nhập tên
  3. Nhập giá
  4. Nhập mô tả/thông tin bổ sung
Logic có thể override ở lớp con nếu có input khác nhau
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
public class Service implements Sellable {

    @Override
    public String getId() {
        return this.serviceId;
    }

    @Override
    public String getName() {
        return this.serviceName;
    }

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
        // Nhập từ người dùng
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
Sellable (Interface)
├── Service implements Sellable
├── Product implements Sellable (nếu tạo)
└── Có thể dùng chung trong List<Sellable>
```

**Cách Sử Dụng:**

```java
// Quản lý tất cả item có thể bán chung một list
List<Sellable> allItems = new ArrayList<>();
allItems.addAll(serviceManager.getAll());  // Thêm services
allItems.addAll(productManager.getAll());  // Thêm products

// Hiển thị tất cả
for (Sellable item : allItems) {
    if (item.isAvailable()) {
        item.display();
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
- Khi implement Service, phải implement tất cả phương thức của Sellable
- Product (nếu có) cũng phải implement Sellable
