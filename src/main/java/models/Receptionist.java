package models;

import java.math.BigDecimal;
import java.time.LocalDate;

import enums.EmployeeRole;

/**
 * Lớp Receptionist đại diện cho một tiếp tân trong hệ thống Spa.
 * Tiếp tân chủ yếu làm việc tại quầy và phòng trực tiếp khách hàng,
 * có khả năng bán hàng và nhận giữ khác ngoài chứ danh của từng bộ phận.
 * Lương được tính = Lương cơ bản + (Tổng doanh số * Hoa hồng đơn vị).
 */
public class Receptionist extends Employee {

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    private BigDecimal bonusPerSale; // Hoa hồng trên mỗi đơn bán (VND)
    private BigDecimal totalSales; // Tổng doanh số bán hàng (VND)

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo Receptionist.
     *
     * @param employeeId   Mã nhân viên
     * @param fullName     Họ tên đầy đủ
     * @param phoneNumber  Số điện thoại
     * @param email        Email
     * @param address      Địa chỉ
     * @param isMale       Giới tính
     * @param birthDate    Ngày sinh
     * @param salary       Lương cơ bản
     * @param hireDate     Ngày tuyển dụng
     * @param bonusPerSale Hoa hồng trên mỗi đơn bán
     */
    public Receptionist(String employeeId, String fullName, String phoneNumber,
            String email, String address, boolean isMale, LocalDate birthDate,
            BigDecimal salary, LocalDate hireDate, BigDecimal bonusPerSale) {
        super(employeeId, fullName, phoneNumber, email, address, isMale,
                birthDate, EmployeeRole.RECEPTIONIST, salary, hireDate);
        this.bonusPerSale = bonusPerSale;
        this.totalSales = BigDecimal.ZERO;
    }

    // ============ CÁC PHƯƠNG THỨC CHÍNH ============

    /**
     * Tính lương của tiếp tân.
     * Lương = Lương cơ bản + (Tổng doanh số * Hoa hồng đơn vị).
     *
     * @return Lương tính toán (BigDecimal)
     */
    @Override
    public BigDecimal calculatePay() {
        BigDecimal bonus = totalSales.multiply(bonusPerSale);
        return getSalary().add(bonus);
    }

    /**
     * Thêm doanh số bán hàng cho tiếp tân.
     * Được gọi khi tiếp tân bán hàng hoặc phát sinh doanh số khác.
     *
     * @param amount Số tiền doanh số bán hàng (VND)
     */
    public void addSale(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            // Nếu input không hợp lệ, không cộng
            return;
        }
        this.totalSales = this.totalSales.add(amount);
    }

    /**
     * Thiết lập lại tổng doanh số (thường dùng khi reset tháng/năm).
     *
     * @param newTotal Tổng doanh số mới
     */
    public void resetTotalSales(BigDecimal newTotal) {
        this.totalSales = newTotal != null ? newTotal : BigDecimal.ZERO;
    }

    /**
     * Lấy doanh số bán hàng hiện tại.
     *
     * @return Tổng doanh số bán hàng
     */
    public BigDecimal getTotalSales() {
        return totalSales;
    }

    /**
     * Lấy hoa hồng trên mỗi đơn bán.
     *
     * @return Hoa hồng trên mỗi đơn bán
     */
    public BigDecimal getBonusPerSale() {
        return bonusPerSale;
    }

    /**
     * Cập nhật hoa hồng trên mỗi đơn bán.
     *
     * @param bonusPerSale Hoa hồng mới trên mỗi đơn bán
     */
    public void setBonusPerSale(BigDecimal bonusPerSale) {
        this.bonusPerSale = bonusPerSale;
    }

    /**
     * Lấy tiền hoa hồng từ doanh số hiện tại.
     *
     * @return Tổng tiền hoa hồng = Tổng doanh số * Hoa hồng đơn vị
     */
    public BigDecimal getCommissionAmount() {
        return totalSales.multiply(bonusPerSale);
    }

    @Override
    public void display() {
        System.out.println("\n========== THÔNG TIN TIẾP TÂN ==========");
        System.out.println("Mã nhân viên: " + getEmployeeId());
        System.out.println("Tên: " + getFullName());
        System.out.println("Điện thoại: " + getPhoneNumber());
        System.out.println("Email: " + getEmail());
        System.out.println("Địa chỉ: " + getAddress());
        System.out.println("Giới tính: " + (isMale() ? "Nam" : "Nữ"));
        System.out.println("Ngày sinh: " + getBirthDate());
        System.out.println("Vai trò: " + getRole());
        System.out.println("Lương cơ bản: " + getSalary() + " VND");
        System.out.println("Ngày tuyển dụng: " + getHireDate());
        System.out.println("Trạng thái: " + getStatus());
        System.out.println("Năm kinh nghiệm: " + getYearsOfService());
        System.out.println("Tổng doanh số: " + totalSales + " VND");
        System.out.println("Hoa hồng/đơn: " + bonusPerSale + " VND");
        System.out.println("Tiền hoa hồng: " + getCommissionAmount() + " VND");
        System.out.println("Lương tính toán: " + calculatePay() + " VND");
        System.out.println("=====================================\n");
    }

    @Override
    public void input() {
        // TODO: Implement nhập thông tin Tiếp tân từ người dùng
    }

    @Override
    public String toString() {
        return "Receptionist{" +
                "employeeId='" + getEmployeeId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", role=" + getEmployeeRole() +
                ", salary=" + getSalary() +
                ", totalSales=" + totalSales +
                ", bonusPerSale=" + bonusPerSale +
                ", status=" + getStatus() +
                '}';
    }
}
