package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import enums.EmployeeRole;
import enums.EmployeeStatus;

/**
 * Lớp trừu tượng đại diện cho một nhân viên trong hệ thống Spa.
 * Kế thừa từ Person và cung cấp các thông tin cơ bản về nhân viên.
 * Không thể tạo instance trực tiếp, phải dùng các lớp con (Receptionist,
 * Technician).
 */
public abstract class Employee extends Person {

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    private String employeeId; // Mã nhân viên (ví dụ: EMP_00001)
    private EmployeeRole role; // Vai trò
    private BigDecimal salary; // Lương cơ bản/tháng (VND)
    private LocalDate hireDate; // Ngày tuyển dụng
    private EmployeeStatus status; // Trạng thái hiện tại
    private String passwordHash; // Hash của mật khẩu
    private String department; // Phòng ban
    private String notes; // Ghi chú bổ sung

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo Employee.
     *
     * @param employeeId  Mã nhân viên
     * @param fullName    Họ tên đầy đủ
     * @param phoneNumber Số điện thoại
     * @param email       Email
     * @param address     Địa chỉ
     * @param isMale      Giới tính (true = nam, false = nữ)
     * @param birthDate   Ngày sinh
     * @param role        Vai trò
     * @param salary      Lương cơ bản
     * @param hireDate    Ngày tuyển dụng
     */
    public Employee(String employeeId, String fullName, String phoneNumber, String email,
            String address, boolean isMale, LocalDate birthDate,
            EmployeeRole role, BigDecimal salary, LocalDate hireDate) {
        super(employeeId, fullName, phoneNumber, isMale, birthDate, email, address);
        this.employeeId = employeeId;
        this.role = role;
        this.salary = salary;
        this.hireDate = hireDate;
        this.status = EmployeeStatus.ACTIVE;
        this.department = "";
        this.notes = "";
        // Set mật khẩu mặc định (hash)
        this.passwordHash = hashPassword("123456");
    }

    // ============ PHƯƠNG THỨC ABSTRACT ============

    /**
     * Tính lương tháng cho nhân viên.
     * Mỗi lớp con có thể tính khác nhau dựa trên loại nhân viên.
     *
     * @return Số tiền lương tháng
     */
    public abstract BigDecimal calculatePay();

    // ============ PHƯƠNG THỨC KIỂM TRA MẬT KHẨU ============

    /**
     * Kiểm tra xem password có đúng không.
     *
     * @param password Mật khẩu cần kiểm tra
     * @return true nếu mật khẩu đúng, false nếu sai
     */
    public boolean checkPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        String hash = hashPassword(password);
        return hash.equals(this.passwordHash);
    }

    /**
     * Cập nhật mật khẩu nhân viên.
     *
     * @param oldPassword Mật khẩu cũ
     * @param newPassword Mật khẩu mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updatePassword(String oldPassword, String newPassword) {
        // Kiểm tra mật khẩu cũ
        if (!checkPassword(oldPassword)) {
            System.out.println("❌ Mật khẩu cũ không chính xác!");
            return false;
        }

        // Kiểm tra mật khẩu mới
        if (newPassword == null || newPassword.length() < 6) {
            System.out.println("❌ Mật khẩu mới phải ít nhất 6 ký tự!");
            return false;
        }

        // Cập nhật mật khẩu
        this.passwordHash = hashPassword(newPassword);
        System.out.println("✓ Mật khẩu đã được cập nhật thành công!");
        return true;
    }

    /**
     * Hash mật khẩu bằng SHA-256.
     * Phương thức này đơn giản hóa cho mục đích demo.
     * Trong production, nên dùng bcrypt hoặc argon2.
     *
     * @param password Mật khẩu cần hash
     * @return Chuỗi hash
     */
    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // ============ PHƯƠNG THỨC TÍNH TOÁN ============

    /**
     * Tính số năm công tác của nhân viên.
     *
     * @return Số năm công tác
     */
    public int getYearsOfService() {
        LocalDate today = LocalDate.now();
        int years = today.getYear() - hireDate.getYear();

        // Nếu chưa tới ngày tuyển dụng năm nay, trừ 1
        if (today.getMonthValue() < hireDate.getMonthValue() ||
                (today.getMonthValue() == hireDate.getMonthValue() &&
                        today.getDayOfMonth() < hireDate.getDayOfMonth())) {
            years--;
        }

        return Math.max(0, years);
    }

    // ============ PHƯƠNG THỨC QUẢN LÝ TRẠNG THÁI ============

    /**
     * Thay đổi trạng thái nhân viên.
     *
     * @param newStatus Trạng thái mới
     */
    public void changeStatus(EmployeeStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Kiểm tra nhân viên có đang hoạt động không.
     *
     * @return true nếu đang hoạt động
     */
    public boolean isActive() {
        return status == EmployeeStatus.ACTIVE;
    }

    // ============ PHƯƠNG THỨC THÔNG TIN ============

    /**
     * Lấy thông tin chi tiết của nhân viên.
     *
     * @return Chuỗi chứa thông tin nhân viên
     */
    public String getEmployeeInfo() {
        return String.format(
                "ID: %s | Tên: %s | Vai trò: %s | Lương: %,d VND | Ngày tuyển: %s | Trạng thái: %s",
                employeeId, getFullName(), role, salary, hireDate, status);
    }

    // ============ PHƯƠNG THỨC IEntity ============

    @Override
    public String getId() {
        return employeeId;
    }

    @Override
    public void display() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ ID: " + getId());
        System.out.println("│ Tên: " + getFullName());
        System.out.println("│ SĐT: " + getPhoneNumber());
        System.out.println("│ Email: " + getEmail());
        System.out.println("│ Vai trò: " + role);
        System.out.println("│ Lương: " + salary + " VND");
        System.out.println("│ Ngày tuyển: " + hireDate);
        System.out.println("│ Trạng thái: " + status);
        System.out.println("│ Năm công tác: " + getYearsOfService());
        System.out.println("└─────────────────────────────────────────────┘");
    }

    @Override
    public void input() {
        // TODO: Implement nhập thông tin nhân viên từ người dùng
    }

    @Override
    public String getPrefix() {
        return "EMP";
    }

    // ============ GETTER & SETTER ============

    public String getEmployeeId() {
        return employeeId;
    }

    public String getRole() {
        return role.toString();
    }

    public EmployeeRole getEmployeeRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ============ equals & hashCode ============

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Employee employee))
            return false;
        return Objects.equals(employeeId, employee.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    @Override
    public String toString() {
        return getEmployeeInfo();
    }
}
