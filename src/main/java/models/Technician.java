package models;

import enums.EmployeeRole;
import enums.ServiceCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Nhân viên kỹ thuật với bộ kỹ năng và hoa hồng.
 */
public class Technician extends Employee {
    private static final int DEFAULT_SKILL_CAPACITY = 10;

    private String[] skillSet;
    private int skillCount;
    private double commissionRate;
    private int maxDailyAppointments;
    private double rating;
    private int completedAppointments;

    /**
     * Args:
     *     fullName: Họ tên.
     *     phoneNumber: Số điện thoại.
     *     email: Địa chỉ email.
     *     birthDate: Ngày sinh.
     *     baseSalary: Lương cơ bản.
     *     passwordHash: Mật khẩu băm.
     *     hireDate: Ngày nhận việc.
     *     commissionRate: Tỷ lệ hoa hồng cho mỗi ca.
     *     maxDailyAppointments: Giới hạn ca/ngày.
     *     initialRating: Điểm đánh giá ban đầu.
     *     skills: Danh sách kỹ năng ban đầu.
     * Returns:
     *     Không trả về.
     */
    public Technician(String fullName,
                      String phoneNumber,
                      String email,
                      LocalDate birthDate,
                      BigDecimal baseSalary,
                      String passwordHash,
                      LocalDate hireDate,
                      double commissionRate,
                      int maxDailyAppointments,
                      double initialRating,
                      String[] skills) {
        super(fullName, phoneNumber, email, birthDate, EmployeeRole.TECHNICIAN, baseSalary, passwordHash, hireDate);
        this.commissionRate = Math.max(0.0, commissionRate);
        this.maxDailyAppointments = Math.max(0, maxDailyAppointments);
        this.rating = clampRating(initialRating);
        this.completedAppointments = 0;
        this.skillSet = createSkillArray(skills);
    }

    private String[] createSkillArray(String[] skills) {
        int capacity = DEFAULT_SKILL_CAPACITY;
        if (skills != null && skills.length > capacity) {
            capacity = skills.length;
        }
        String[] buffer = new String[capacity];
        if (skills != null) {
            for (int i = 0; i < skills.length; i++) {
                buffer[i] = skills[i];
            }
            skillCount = skills.length;
        } else {
            skillCount = 0;
        }
        return buffer;
    }

    /**
     * Args:
     *     skill: Kỹ năng mới cần thêm.
     * Returns:
     *     Không trả về.
     */
    public void addSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return;
        }
        ensureSkillCapacity();
        skillSet[skillCount] = skill;
        skillCount++;
    }

    private void ensureSkillCapacity() {
        if (skillCount < skillSet.length) {
            return;
        }
        int newCapacity = skillSet.length * 2;
        String[] expanded = new String[newCapacity];
        for (int i = 0; i < skillSet.length; i++) {
            expanded[i] = skillSet[i];
        }
        skillSet = expanded;
    }

    /**
     * {@inheritDoc}
     *
     * Args:
     *     Không có.
     * Returns:
     *     Thu nhập gồm lương cơ bản và hoa hồng.
     */
    @Override
    public BigDecimal calculatePay() {
        BigDecimal commission = BigDecimal.valueOf(commissionRate).multiply(BigDecimal.valueOf(completedAppointments));
        return salary.add(commission);
    }

    /**
     * Args:
     *     category: Thể loại dịch vụ.
     * Returns:
     *     true nếu kỹ năng phù hợp.
     */
    public boolean isQualifiedFor(ServiceCategory category) {
        if (category == null) {
            return false;
        }
        for (int i = 0; i < skillCount; i++) {
            String skill = skillSet[i];
            if (skill != null && skill.equalsIgnoreCase(category.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Args:
     *     newRating: Điểm đánh giá mới (0-5).
     * Returns:
     *     Không trả về.
     */
    public void updateRating(double newRating) {
        rating = clampRating(newRating);
    }

    private double clampRating(double value) {
        if (value < 0.0) {
            return 0.0;
        }
        if (value > 5.0) {
            return 5.0;
        }
        return value;
    }

    /**
     * Args:
     *     Không có.
     * Returns:
     *     Tỷ lệ hoa hồng hiện tại.
     */
    public double getCommissionRate() {
        return commissionRate;
    }

    /**
     * Args:
     *     commissionRate: Tỷ lệ hoa hồng mới.
     * Returns:
     *     Không trả về.
     */
    public void setCommissionRate(double commissionRate) {
        if (commissionRate >= 0) {
            this.commissionRate = commissionRate;
        }
    }

    /**
     * Args:
     *     Không có.
     * Returns:
     *     Giới hạn ca/ngày.
     */
    public int getMaxDailyAppointments() {
        return maxDailyAppointments;
    }

    /**
     * Args:
     *     maxDailyAppointments: Giới hạn mới.
     * Returns:
     *     Không trả về.
     */
    public void setMaxDailyAppointments(int maxDailyAppointments) {
        if (maxDailyAppointments >= 0) {
            this.maxDailyAppointments = maxDailyAppointments;
        }
    }

    /**
     * Args:
     *     Không có.
     * Returns:
     *     Điểm đánh giá hiện tại.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Args:
     *     Không có.
     * Returns:
     *     Tổng số ca đã hoàn thành.
     */
    public int getCompletedAppointments() {
        return completedAppointments;
    }

    /**
     * Args:
     *     Không có.
     * Returns:
     *     Danh sách kỹ năng (bao gồm phần tử null nếu chưa sử dụng).
     */
    public String[] getSkillSet() {
        String[] snapshot = new String[skillCount];
        for (int i = 0; i < skillCount; i++) {
            snapshot[i] = skillSet[i];
        }
        return snapshot;
    }

    /**
     * Args:
     *     Không có.
     * Returns:
     *     Không trả về.
     */
    public void incrementCompletedAppointments() {
        completedAppointments++;
    }
}
