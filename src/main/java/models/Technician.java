package models;

import java.math.BigDecimal;
import java.time.LocalDate;

import enums.EmployeeRole;

/**
 * Lớp Technician đại diện cho một kỹ thuật viên/nhân viên dịch vụ trong hệ
 * thống Spa.
 * Kỹ thuật viên được đánh giá dựa trên kỹ năng, chứng chỉ, và hiệu suất làm
 * việc.
 * Lương được tính = Lương cơ bản + Hoa hồng từ doanh số dịch vụ.
 * Quản lý danh sách kỹ năng và chứng chỉ bổ trợ.
 */
public class Technician extends Employee {

    // ============ HẰNG SỐ ============
    private static final int MAX_SKILLS = 20; // Tối đa 20 kỹ năng
    private static final int MAX_CERTIFICATIONS = 15; // Tối đa 15 chứng chỉ

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    private String[] skillSet; // Mảng các kỹ năng
    private int skillCount; // Số lượng kỹ năng hiện tại
    private String[] certifications; // Mảng các chứng chỉ
    private int certificationCount; // Số lượng chứng chỉ hiện tại
    private double commissionRate; // Tỷ lệ hoa hồng (0.05 = 5%)
    private BigDecimal totalCommission; // Tổng hoa hồng tích lũy
    private double performanceRating; // Đánh giá hiệu suất (0.0 - 5.0)

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo Technician.
     *
     * @param employeeId     Mã nhân viên
     * @param fullName       Họ tên đầy đủ
     * @param phoneNumber    Số điện thoại
     * @param email          Email
     * @param address        Địa chỉ
     * @param isMale         Giới tính
     * @param birthDate      Ngày sinh
     * @param salary         Lương cơ bản
     * @param hireDate       Ngày tuyển dụng
     * @param commissionRate Tỷ lệ hoa hồng
     */
    public Technician(String employeeId, String fullName, String phoneNumber,
            String email, String address, boolean isMale, LocalDate birthDate,
            BigDecimal salary, LocalDate hireDate, double commissionRate) {
        super(employeeId, fullName, phoneNumber, email, address, isMale,
                birthDate, EmployeeRole.TECHNICIAN, salary, hireDate);
        this.skillSet = new String[MAX_SKILLS];
        this.skillCount = 0;
        this.certifications = new String[MAX_CERTIFICATIONS];
        this.certificationCount = 0;
        this.commissionRate = commissionRate;
        this.totalCommission = BigDecimal.ZERO;
        this.performanceRating = 3.0; // Đánh giá mặc định là 3.0
    }

    // ============ CÁC PHƯƠNG THỨC CHÍNH ============

    /**
     * Tính lương của kỹ thuật viên.
     * Lương = Lương cơ bản + Tổng hoa hồng.
     *
     * @return Lương tính toán (BigDecimal)
     */
    @Override
    public BigDecimal calculatePay() {
        return getSalary().add(totalCommission);
    }

    /**
     * Thêm hoa hồng từ doanh số dịch vụ.
     *
     * @param serviceAmount Giá trị dịch vụ (VND)
     */
    public void addCommission(BigDecimal serviceAmount) {
        if (serviceAmount == null || serviceAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        BigDecimal commission = serviceAmount.multiply(BigDecimal.valueOf(commissionRate));
        this.totalCommission = this.totalCommission.add(commission);
    }

    /**
     * Thiết lập lại tổng hoa hồng (thường dùng khi reset tháng/năm).
     *
     * @param newTotal Tổng hoa hồng mới
     */
    public void resetTotalCommission(BigDecimal newTotal) {
        this.totalCommission = newTotal != null ? newTotal : BigDecimal.ZERO;
    }

    /**
     * Thêm một kỹ năng mới cho kỹ thuật viên.
     *
     * @param skill Tên kỹ năng
     * @return true nếu thêm thành công, false nếu đã đầy hoặc skill rỗng
     */
    public boolean addSkill(String skill) {
        if (skill == null || skill.trim().isEmpty() || skillCount >= MAX_SKILLS) {
            return false;
        }
        // Kiểm tra kỹ năng có tồn tại chưa
        if (hasSkill(skill)) {
            return false;
        }
        skillSet[skillCount] = skill.trim();
        skillCount++;
        return true;
    }

    /**
     * Xóa một kỹ năng từ danh sách.
     *
     * @param skill Tên kỹ năng cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    public boolean removeSkill(String skill) {
        if (skill == null) {
            return false;
        }
        for (int i = 0; i < skillCount; i++) {
            if (skillSet[i].equalsIgnoreCase(skill.trim())) {
                // Dịch các phần tử sau lên
                for (int j = i; j < skillCount - 1; j++) {
                    skillSet[j] = skillSet[j + 1];
                }
                skillSet[skillCount - 1] = null;
                skillCount--;
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem kỹ thuật viên có kỹ năng nhất định không.
     *
     * @param skill Tên kỹ năng cần kiểm tra
     * @return true nếu có, false nếu không
     */
    public boolean hasSkill(String skill) {
        if (skill == null) {
            return false;
        }
        for (int i = 0; i < skillCount; i++) {
            if (skillSet[i] != null && skillSet[i].equalsIgnoreCase(skill.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả kỹ năng.
     *
     * @return Mảng các kỹ năng
     */
    public String[] getSkills() {
        String[] result = new String[skillCount];
        System.arraycopy(skillSet, 0, result, 0, skillCount);
        return result;
    }

    /**
     * Thêm một chứng chỉ mới cho kỹ thuật viên.
     *
     * @param certification Tên chứng chỉ
     * @return true nếu thêm thành công, false nếu đã đầy hoặc cert rỗng
     */
    public boolean addCertification(String certification) {
        if (certification == null || certification.trim().isEmpty() || certificationCount >= MAX_CERTIFICATIONS) {
            return false;
        }
        // Kiểm tra chứng chỉ có tồn tại chưa
        if (hasCertification(certification)) {
            return false;
        }
        certifications[certificationCount] = certification.trim();
        certificationCount++;
        return true;
    }

    /**
     * Xóa một chứng chỉ từ danh sách.
     *
     * @param certification Tên chứng chỉ cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    public boolean removeCertification(String certification) {
        if (certification == null) {
            return false;
        }
        for (int i = 0; i < certificationCount; i++) {
            if (certifications[i].equalsIgnoreCase(certification.trim())) {
                // Dịch các phần tử sau lên
                for (int j = i; j < certificationCount - 1; j++) {
                    certifications[j] = certifications[j + 1];
                }
                certifications[certificationCount - 1] = null;
                certificationCount--;
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem kỹ thuật viên có chứng chỉ nhất định không.
     *
     * @param certification Tên chứng chỉ cần kiểm tra
     * @return true nếu có, false nếu không
     */
    public boolean hasCertification(String certification) {
        if (certification == null) {
            return false;
        }
        for (int i = 0; i < certificationCount; i++) {
            if (certifications[i] != null && certifications[i].equalsIgnoreCase(certification.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả chứng chỉ.
     *
     * @return Mảng các chứng chỉ
     */
    public String[] getCertifications() {
        String[] result = new String[certificationCount];
        System.arraycopy(certifications, 0, result, 0, certificationCount);
        return result;
    }

    /**
     * Cập nhật đánh giá hiệu suất của kỹ thuật viên.
     * Giá trị từ 0.0 đến 5.0.
     *
     * @param rating Đánh giá hiệu suất mới
     */
    public void updatePerformanceRating(double rating) {
        if (rating < 0.0 || rating > 5.0) {
            return;
        }
        this.performanceRating = rating;
    }

    /**
     * Lấy đánh giá hiệu suất hiện tại.
     *
     * @return Đánh giá hiệu suất (0.0 - 5.0)
     */
    public double getPerformanceRating() {
        return performanceRating;
    }

    /**
     * Lấy tỷ lệ hoa hồng.
     *
     * @return Tỷ lệ hoa hồng
     */
    public double getCommissionRate() {
        return commissionRate;
    }

    /**
     * Cập nhật tỷ lệ hoa hồng.
     *
     * @param commissionRate Tỷ lệ hoa hồng mới
     */
    public void setCommissionRate(double commissionRate) {
        if (commissionRate >= 0.0 && commissionRate <= 1.0) {
            this.commissionRate = commissionRate;
        }
    }

    /**
     * Lấy tổng hoa hồng tích lũy.
     *
     * @return Tổng hoa hồng
     */
    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    /**
     * Lấy số lượng kỹ năng hiện tại.
     *
     * @return Số lượng kỹ năng
     */
    public int getSkillCount() {
        return skillCount;
    }

    /**
     * Lấy số lượng chứng chỉ hiện tại.
     *
     * @return Số lượng chứng chỉ
     */
    public int getCertificationCount() {
        return certificationCount;
    }

    @Override
    public void display() {
        System.out.println("\n========== THÔNG TIN KỸ THUẬT VIÊN ==========");
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
        System.out.println("Tỷ lệ hoa hồng: " + (commissionRate * 100) + "%");
        System.out.println("Tổng hoa hồng: " + totalCommission + " VND");
        System.out.println("Đánh giá hiệu suất: " + performanceRating + "/5.0");
        System.out.println("Số kỹ năng: " + skillCount);
        System.out.println("Số chứng chỉ: " + certificationCount);
        System.out.println("Lương tính toán: " + calculatePay() + " VND");
        System.out.println("===========================================\n");
    }

    @Override
    public void input() {
        // TODO: Implement nhập thông tin Kỹ thuật viên từ người dùng
    }

    @Override
    public String toString() {
        return "Technician{" +
                "employeeId='" + getEmployeeId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", role=" + getEmployeeRole() +
                ", salary=" + getSalary() +
                ", skillCount=" + skillCount +
                ", certificationCount=" + certificationCount +
                ", commissionRate=" + commissionRate +
                ", totalCommission=" + totalCommission +
                ", performanceRating=" + performanceRating +
                ", status=" + getStatus() +
                '}';
    }
}
