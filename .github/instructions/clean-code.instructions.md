## ⚙️ Hướng Dẫn Toàn Diện Cho AI: Clean Code, Google Java Style & Comment Tiếng Việt

**Mục tiêu cốt lõi:** Luôn tạo ra code **sạch (Clean Code)**, tuân thủ nghiêm ngặt **Google Java Style Guide**, và sử dụng **tiếng Việt** để comment code một cách ngắn gọn, rõ ràng, và có ý nghĩa.

---

## I. Nguyên Tắc Clean Code Bắt Buộc (Áp dụng chung)

| Quy Tắc                      | Mô Tả Yêu Cầu Chi Tiết                                                                                                           |
| :--------------------------- | :------------------------------------------------------------------------------------------------------------------------------- |
| **Tên Rõ Ràng & Có Ý Nghĩa** | Tên biến, phương thức, lớp (class) phải **mô tả rõ ràng** mục đích của chúng. (Ví dụ: `tinhTongDonHang` thay vì `ttdh`).         |
| **Phương Thức Ngắn Gọn**     | Phương thức (method) phải **ngắn nhất có thể** và chỉ làm **một việc duy nhất** (Single Responsibility Principle - SRP).         |
| **Tránh Lặp Lại (DRY)**      | Không lặp lại cùng một đoạn logic (Don't Repeat Yourself). Tái sử dụng code thông qua phương thức, lớp hoặc package.             |
| **Xử Lý Ngoại Lệ Rõ Ràng**   | Sử dụng `try-catch-finally` và các ngoại lệ (Exception) cụ thể để xử lý lỗi một cách tường minh, tránh trả về `null` khi có thể. |
| **Định Dạng Nhất Quán**      | Luôn áp dụng phong cách định dạng (dấu cách, thụt đầu dòng, dấu ngoặc) một cách nhất quán trong toàn bộ dự án.                   |

---

## II. Quy Tắc Java Cụ Thể (Theo Google Java Style Guide)

Khi viết code Java, **BẮT BUỘC** tuân thủ các quy tắc sau:

1.  **Thụt Lề (Indentation):** Dùng **4 khoảng trắng** (spaces) cho mỗi cấp độ thụt lề. **Không dùng tab.**
2.  **Độ Dài Dòng:** Giới hạn dòng ở **100 ký tự**.
3.  **Dấu Ngoặc Nhọn (`{}`):**
    - Dấu mở ngoặc `{` nằm cuối cùng trên cùng một dòng với khai báo.
    - Dấu đóng ngoặc `}` bắt đầu một dòng mới.
    - Luôn sử dụng dấu ngoặc nhọn cho các khối lệnh `if`, `else`, `for`, `do`, `while`, ngay cả khi chỉ có một câu lệnh.
4.  **Đặt Tên:**
    - **`lowerCamelCase`** cho biến, phương thức, tham số (ví dụ: `duLieuKhachHang`, `tinhToanGiaTri()`).
    - **`UpperCamelCase`** (PascalCase) cho tên Lớp (Class), Giao diện (Interface), Enum (ví dụ: `DonHangMoi`, `UserService`).
    - **`CONSTANT_CASE`** cho Hằng số (ví dụ: `GIOI_HAN_TUOI`, `MAX_CONNECTIONS`).
5.  **Imports:**
    - Mỗi `import` trên một dòng riêng.
    - **Không** sử dụng import đại diện (wildcard `*`).
    - Thứ tự: `com`, `edu`, `gov`..., `org`, `net`..., sau đó là `java`, `javax`.

---

## III. Quy Tắc Comment (Sử Dụng Tiếng Việt)

| Loại Comment          | Yêu Cầu Thực Hiện Bằng Tiếng Việt                                                                                                                            | Ghi Chú                                                                  |
| :-------------------- | :----------------------------------------------------------------------------------------------------------------------------------------------------------- | :----------------------------------------------------------------------- |
| **Javadoc**           | Bắt buộc mô tả **Chức năng chính**, **Tham số (`@param`)**, **Giá trị trả về (`@return`)** và **Ngoại lệ (`@throws`)** của các lớp và phương thức công khai. | Sử dụng cú pháp Javadoc `/** ... */` và Tiếng Việt chuẩn.                |
| **Giải Thích Logic**  | Chỉ comment những đoạn code có **logic phức tạp** hoặc **cần giải thích LÝ DO** cho quyết định thiết kế.                                                     | **Hạn chế** comment code đã rõ ràng.                                     |
| **Định Dạng**         | Comment phải **ngắn gọn**, **súc tích**, và **nằm trên dòng riêng** ngay trước dòng code mà nó giải thích.                                                   | Sử dụng `//` cho comment một dòng và `/* ... */` cho comment nhiều dòng. |
| **Ghi Chú Công Việc** | Sử dụng **`// TODO:`** hoặc **`// FIXME:`** để đánh dấu những việc cần làm hoặc cần sửa lỗi sau này.                                                         | Giữ nguyên từ khóa tiếng Anh (`TODO`, `FIXME`).                          |

---

## 🚀 Tóm Tắt & Ưu Tiên

1.  **Code phải tự giải thích trước.**
2.  **Chỉ comment những gì code không thể tự diễn tả.**
3.  **Tất cả comment phải ngắn gọn và rõ ràng bằng TIẾNG VIỆT.**
4.  **Code Java phải tuân thủ Google Java Style Guide.**
