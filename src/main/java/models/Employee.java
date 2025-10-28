package models;

import enums.EmployeeRole;
import enums.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Lớp trừu tượng đại diện cho nhân viên trong spa.
 */
public abstract class Employee extends Person {
    protected static final String CODE_PREFIX = "EMP";
    protected static int employeeSequence = 1;

    protected final String employeeCode;
    protected EmployeeRole role;
    protected EmployeeStatus status;
    protected BigDecimal salary;
    protected String passwordHash;
    protected LocalDate hireDate;
    protected LocalDateTime lastLoginAt;

    protected Employee(String fullName,
                       String phoneNumber,
                       String email,
                       LocalDate birthDate,
                       EmployeeRole role,
                       BigDecimal salary,
                       String passwordHash,
                       LocalDate hireDate) {
        super(fullName, phoneNumber, email, birthDate);
        this.employeeCode = generateEmployeeCode();
        this.role = role;
        this.status = EmployeeStatus.ACTIVE;
        this.salary = salary == null ? BigDecimal.ZERO : salary;
        this.passwordHash = passwordHash;
        this.hireDate = hireDate;
        this.lastLoginAt = null;
    }

    /**
     * @return Mã nhân viên duy nhất.
     */
    public String getEmployeeCode() {
        return employeeCode;
    }

    /**
     * @return Vai trò hiện tại của nhân viên.
     */
    public EmployeeRole getRole() {
        return role;
    }

    /**
     * Cập nhật vai trò của nhân viên.
     *
     * @param newRole Vai trò mới.
     */
    public void updateRole(EmployeeRole newRole) {
        if (newRole != null) {
            this.role = newRole;
        }
    }

    /**
     * @return Trạng thái làm việc hiện tại.
     */
    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        if (status != null) {
            this.status = status;
        }
    }

    /**
     * @return Lương cơ bản hàng tháng.
     */
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        if (salary != null) {
            this.salary = salary;
        }
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    /**
     * Kiểm tra mật khẩu thô với giá trị đã băm.
     *
     * @param rawPassword Mật khẩu người dùng nhập.
     * @return true nếu khớp, ngược lại false.
     */
    public boolean checkPassword(String rawPassword) {
        if (passwordHash == null || rawPassword == null) {
            return false;
        }
        return passwordHash.equals(rawPassword);
    }

    /**
     * Tính toán tổng thu nhập phải trả cho nhân viên.
     *
     * @return Tổng tiền lương theo chính sách riêng.
     */
    public abstract BigDecimal calculatePay();

    private String generateEmployeeCode() {
        String code = CODE_PREFIX + String.format("%04d", employeeSequence);
        employeeSequence++;
        return code;
    }
}
