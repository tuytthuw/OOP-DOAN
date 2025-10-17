# ✅ THÊM KỾ HOẠCH 0002b - Interface Sellable

**Ngày:** 17/10/2025  
**Cập Nhật:** Thêm kế hoạch cho Interface Sellable  
**Trạng Thái:** ✅ Hoàn thành

---

## 📌 TÓMSẮT

Tạo kế hoạch **0002b - Interface Sellable** để định nghĩa hành vi chung cho các đối tượng có thể bán (Service, Product).

---

## 🎯 THAY ĐỔI

### ✅ Tạo Kế Hoạch 0002b

**File:** `docs/features/0002b_PLAN.md`

**Nội Dung:**

- Mô tả Interface Sellable
- 8 phương thức:
  - `getId()` - Lấy ID
  - `getName()` - Lấy tên
  - `getPrice()` - Lấy giá
  - `getPriceFormatted()` - Giá định dạng
  - `display()` - Hiển thị
  - `input()` - Nhập dữ liệu
  - `isAvailable()` - Kiểm tra khả dụng
  - `getDescription()` - Lấy mô tả
- Chi tiết từng phương thức
- Implement ví dụ trong Service
- Mối quan hệ với Service, Product

**Lợi Ích:**

- ✅ Polymorphism - Service/Product dùng chung interface
- ✅ Flexibility - Dễ thêm item mới (Package, Bundle)
- ✅ Contract - Đảm bảo tất cả item có thể bán implement đúng
- ✅ Code reuse - Xử lý tất cả item chung 1 list

### ✏️ Cập Nhật INDEX.md

**Thay Đổi:**

- Thêm bảng kế hoạch 0002b
- File tạo mới: `Interfaces/Sellable.java`

### ✏️ Cập Nhật FILE_MANIFEST.md

**Thay Đổi:**

- Số file tạo mới: 8 → 9
- Số file cập nhật: 1 → 2
- Thêm 0002b vào danh sách file
- Cập nhật thống kê

---

## 📊 THỐNG KÊ CẬP NHẬT

### File Tạo Mới

| Số     | File                    | Loại                  |
| ------ | ----------------------- | --------------------- |
| 1      | REVIEW_AND_UPDATE.md    | Phân tích             |
| 2      | SUMMARY.md              | Tóm tắt               |
| 3      | COMPARISON.md           | So sánh               |
| 4      | QUICK_REFERENCE.md      | Hướng dẫn             |
| 5      | ClassDiagram_Updated.md | ClassDiagram          |
| 6      | 0000_PLAN.md            | Kế hoạch              |
| 7      | 0007a_PLAN.md           | Kế hoạch              |
| 8      | 0011_PLAN.md            | Kế hoạch              |
| 9      | 0012_PLAN.md            | Kế hoạch              |
| **10** | **0002b_PLAN.md**       | **Kế hoạch (MỚI)** ⭐ |

### File Cập Nhật

| Số  | File             | Loại     |
| --- | ---------------- | -------- |
| 1   | INDEX.md         | Cập nhật |
| 2   | FILE_MANIFEST.md | Cập nhật |

---

## 🔗 MỐI QUAN HỆ

### Interface Sellable

```
Sellable (Interface)
├── Service implements Sellable
├── Product implements Sellable (nếu tạo)
└── Dùng chung trong List<Sellable>
```

### Kế Hoạch Liên Quan

- **0002_PLAN.md** - Service Model (implement Sellable)
- **0002b_PLAN.md** - Interface Sellable (MỚI)

### Thứ Tự Implement

1. **0002b** - Tạo Interface Sellable trước
2. **0002** - Service implement Sellable

---

## 💡 VÍ DỤ SỬ DỤNG

### Trước (Không Interface)

```java
// Phải làm riêng từng loại
List<Service> services = serviceManager.getAll();
for (Service s : services) {
    System.out.println(s.getId() + " - " + s.getPrice());
}

List<Product> products = productManager.getAll();
for (Product p : products) {
    System.out.println(p.getId() + " - " + p.getPrice());
}
// → Lặp code
```

### Sau (Với Interface)

```java
// Xử lý chung tất cả
List<Sellable> allItems = new ArrayList<>();
allItems.addAll(serviceManager.getAll());  // Service implements Sellable
allItems.addAll(productManager.getAll());  // Product implements Sellable

for (Sellable item : allItems) {
    if (item.isAvailable()) {
        System.out.println(item.getId() + " - " + item.getPriceFormatted());
    }
}
// → Chỉ 1 vòng lặp, xử lý tất cả
```

---

## ✨ LỢI ÍCH

| Khía Cạnh           | Lợi Ích                              |
| ------------------- | ------------------------------------ |
| **Code Reuse**      | ✅ Xử lý Service/Product chung       |
| **Polymorphism**    | ✅ Thay thế cho nhau                 |
| **Flexibility**     | ✅ Dễ thêm item mới                  |
| **Consistency**     | ✅ Tất cả item có thể bán giống nhau |
| **Maintainability** | ✅ Dễ bảo trì                        |

---

## 📋 DANH SÁCH PHƯƠNG THỨC

| Phương Thức           | Kiểu Trả Về | Mục Đích                    |
| --------------------- | ----------- | --------------------------- |
| `getId()`             | String      | Lấy ID                      |
| `getName()`           | String      | Lấy tên                     |
| `getPrice()`          | BigDecimal  | Lấy giá                     |
| `getPriceFormatted()` | String      | Giá định dạng (500.000 VND) |
| `display()`           | void        | Hiển thị ra console         |
| `input()`             | void        | Nhập từ người dùng          |
| `isAvailable()`       | boolean     | Kiểm tra có thể bán         |
| `getDescription()`    | String      | Lấy mô tả                   |

---

## 🎯 NEXT STEPS

1. ✅ **HOÀN THÀNH:** Tạo 0002b_PLAN.md
2. ✅ **HOÀN THÀNH:** Cập nhật INDEX.md
3. ✅ **HOÀN THÀNH:** Cập nhật FILE_MANIFEST.md
4. ⏭️ **TIẾP THEO:** Implement Sellable.java khi code
5. ⏭️ **TIẾP THEO:** Implement Service.java (implement Sellable)

---

## ✅ HOÀN THÀNH

Kế hoạch 0002b đã được tạo & cập nhật.

**Tổng:**

- ✅ 9 file tạo mới (thêm 0002b_PLAN.md)
- ✅ 2 file cập nhật (INDEX.md, FILE_MANIFEST.md)
- ✅ ~10 kế hoạch chi tiết
