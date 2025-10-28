---
description: 
auto_execution_mode: 1
---

---
description: 
auto_execution_mode: 1
---

# LẬP KẾ HOẠCH KỸ THUẬT (TECHNICAL PLAN)

## 1. VAI TRÒ & NHIỆM VỤ
Với vai trò là Kiến trúc sư Giải pháp (Solution Architect), tạo một Kế hoạch Kỹ thuật (Technical Plan) súc tích cho một tính năng của ứng dụng Quản lý Java OOP.

## 2. YÊU CẦU (CONSTRAINTS)
- [cite_start]**Chỉ "Cái gì" (What):** Tập trung vào các Gói (Package), Lớp (Class), Lớp Trừu tượng (Abstract Class), Giao diện (Interface), Thuộc tính (Attribute) và Phương thức (Method)[cite: 24, 26, 29, 40].
- **Không "Cách làm" (How):** NGHIÊM CẤM viết code triển khai chi tiết (ví dụ: logic vòng lặp `for` để duyệt mảng).
- **Tuân thủ Đề án:**
  - [cite_start]Chỉ sử dụng Mảng thuần túy (`T[]`) để lưu trữ danh sách.
  - [cite_start]Phải bao gồm Lớp Trừu tượng (Abstract Class) làm lớp cơ sở[cite: 24, 60].
  - [cite_start]Phải có ít nhất 2 lớp con kế thừa từ lớp cơ sở[cite: 24].
  - [cite_start]Phải định nghĩa các Interface cho các hành vi chung (ví dụ: `IFileService`)[cite: 40, 61].
  - [cite_start]Phải xác định các thành phần `static` (ví dụ: biến đếm ID, hằng số `MAX_SIZE`)[cite: 42, 59].

## 3. CẤU TRÚC KẾ HOẠCH (OUTPUT STRUCTURE)

### 3.1. Mô tả Ngữ cảnh
- Mục tiêu kỹ thuật của tính năng này là gì?

### 3.2. Cấu trúc Lớp Model (Entities/POJO)
- [cite_start]**Lớp cơ sở (Trừu tượng):** (Ví dụ: `abstract class Person`) [cite: 24, 60]
  - Thuộc tính:
  - Phương thức trừu tượng: (Ví dụ: `public abstract double getDiemTrungBinh();`)
- **Các lớp Kế thừa:** (Ví dụ: `class Student extends Person`, `class Teacher extends Person`)
  - Thuộc tính riêng:
  - Ghi đè (Override) phương thức:

### 3.3. Cấu trúc Lớp Quản lý (Services/Manager)
- **Tên lớp:** (Ví dụ: `class StudentManager`)
- **Thuộc tính (Data):**
  - (Ví dụ: `private Student[] studentList;`)
  - (Ví dụ: `private int count;`)
  - (Ví dụ: `private static final int MAX_SIZE = 100;`) [cite_start][cite: 59]
- **Phương thức (CRUD & Nghiệp vụ):**
  - (Ví dụ: `public void addStudent(Student s)`) [cite: 32]
  - (Ví dụ: `public void updateStudent(String id, Student updatedInfo)`) [cite: 33]
  - (Ví dụ: `public boolean deleteStudent(String id)`) [cite: 34]
  - (Ví dụ: `public Student findById(String id)`) [cite: 35]
  - (Ví dụ: `public Student[] getAllStudents()`) [cite: 31]
  - (Ví dụ: `public void statisticByGpa()`) [cite: 36]

### 3.4. [cite_start]Yêu cầu Giao diện (Interfaces) [cite: 40]
- (Ví dụ: Tạo `interface IFileService` với các phương thức `readData()` và `writeData()`).

### 3.5. [cite_start]Yêu cầu Đọc/Ghi File [cite: 41, 58]
- Lớp nào sẽ chịu trách nhiệm đọc/ghi file?
- Dữ liệu file (text) sẽ được lưu ở định dạng nào (ví dụ: CSV)?

### 3.6. Yêu cầu Nghiệp vụ Cốt lõi
- (Ví dụ: "Lớp Manager phải tự quản lý mảng, bao gồm logic nới rộng (resize) mảng khi đầy và logic dồn mảng (shift) khi xóa.")
- (Ví dụ: "Khi xuất danh sách, phải sử dụng tính đa hình để in thông tin đặc thù của từng loại đối tượng (Student, Teacher)[cite_start].") [cite: 25, 38]