### 3.1. Mô tả Ngữ cảnh
- Xác lập các hợp đồng trừu tượng (interface, abstract class) làm nền tảng chung cho toàn bộ thực thể và dịch vụ trong hệ thống quản lý Spa.
- Đảm bảo mô hình kế thừa và đa hình được định nghĩa rõ ràng để các lớp triển khai ở giai đoạn sau tuân thủ kiến trúc OOP đã thống nhất.

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- `abstract class Person` (com.spa.model.people):
  - Thuộc tính: `personId`, `fullName`, `phoneNumber`, `isMale`, `birthDate`, `email`, `address`, `isDeleted`.
  - Phương thức trừu tượng: `public abstract String getRole();`
  - Phương thức cụ thể: `display()`, `input()` (ghi đè từ `IEntity`).
- `abstract class Employee` (com.spa.model.people):
  - Thuộc tính bổ sung: `salary`, `passwordHash`, `hireDate`.
  - Phương thức trừu tượng: `public abstract double calculatePay();`
  - Phương thức cụ thể: `checkPassword()`.
- `class Customer extends Person` (com.spa.model.people):
  - Thuộc tính riêng: `memberTier`, `notes`, `points`, `lastVisitDate`.
  - Ghi đè: `getRole()`, `display()`.
- `class Technician extends Employee` (com.spa.model.people):
  - Thuộc tính riêng: `skill`, `certifications`, `commissionRate`.
  - Ghi đè: `calculatePay()`, `getRole()`.
- `class Receptionist extends Employee` (com.spa.model.people):
  - Thuộc tính riêng: `monthlyBonus`.
  - Ghi đè: `calculatePay()`, `getRole()`.

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- `class DataStore<T>` (com.spa.data):
  - Thuộc tính: `private T[] list;`, `private int count;`, `private static final int DEFAULT_CAPACITY = 10;`.
  - Phương thức khung: `add(T item)`, `update(String id)`, `delete(String id)`, `findById(String id)`, `getAll()`, `displayList()`, `generateStatistics()`, `readFile()`, `writeFile()`.
- `interface IActionManager<T>` (com.spa.service.contract):
  - Phương thức: `displayList()`, `add(T item)`, `update(String id)`, `delete(String id)`, `findById(String id)`, `getAll()`, `generateStatistics()`.
- `interface IDataManager` (com.spa.service.contract):
  - Phương thức: `readFile()`, `writeFile()`.
- `interface IEntity` (com.spa.model.contract):
  - Phương thức: `getId()`, `display()`, `input()`, `getPrefix()`.
- `interface Sellable` (com.spa.model.contract):
  - Phương thức: `getBasePrice()`, `calculateFinalPrice()`, `getType()`.

### 3.4. Yêu cầu Giao diện (Interfaces)
- Đặt toàn bộ interface vào package `com.spa.model.contract` và `com.spa.service.contract` để tách biệt hợp đồng dữ liệu và hợp đồng dịch vụ.
- Đảm bảo các lớp cụ thể (Customer, Technician, Service, Product…) sẽ thực thi đúng interface tương ứng khi được triển khai.

### 3.5. Yêu cầu Đọc/Ghi File
- `DataStore<T>` chịu trách nhiệm cài đặt `readFile()` và `writeFile()` theo định dạng văn bản thuần, mỗi dòng là một bản ghi với ký tự phân tách thống nhất (gợi ý dùng dấu `|`).
- Duy trì biến `static` đường dẫn file trong từng DataStore chuyên biệt (ví dụ: `private static final String CUSTOMER_FILE = "data/customers.txt";`).

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- Bảo đảm cây kế thừa `Person -> Employee -> Technician/Receptionist` được xác định từ giai đoạn này để phục vụ đa hình tính lương.
- Đặt nền tảng cho việc quản lý bằng mảng thô trong `DataStore<T>` với cơ chế tự mở rộng và thống kê sẽ được hiện thực ở giai đoạn tiếp theo.
- Định nghĩa rõ hợp đồng `IEntity` giúp các lớp thuộc nhóm nghiệp vụ hiển thị và nhập dữ liệu nhất quán thông qua đa hình.
