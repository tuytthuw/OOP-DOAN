package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import enums.Tier;

/**
 * Lớp Customer đại diện cho khách hàng của hệ thống spa.
 *
 * Extend từ Person và thêm các thông tin quản lý membership và chi tiêu.
 * Quản lý tier membership, lịch sử sử dụng dịch vụ và tổng chi tiêu.
 */
public class Customer extends Person {

    // ============ Thuộc tính (Attributes) ============
    private Tier memberTier; // Loại membership (PLATINUM, GOLD, SILVER, BRONZE)
    private BigDecimal totalSpent; // Tổng tiền đã chi tiêu (VND)
    private LocalDate registrationDate; // Ngày đăng ký membership
    private LocalDate lastValidDate; // Lần cuối sử dụng dịch vụ
    private boolean isActive; // Có hoạt động không

    // ============ Hằng số (Constants) ============
    public static final BigDecimal PLATINUM_THRESHOLD = new BigDecimal("10000000");
    public static final BigDecimal GOLD_THRESHOLD = new BigDecimal("5000000");
    public static final BigDecimal SILVER_THRESHOLD = new BigDecimal("1000000");

    /**
     * Constructor khởi tạo Customer với đầy đủ thông tin.
     *
     * @param personId         Mã định danh (ID của người)
     * @param fullName         Tên đầy đủ
     * @param phoneNumber      Số điện thoại
     * @param isMale           Giới tính (true = Nam, false = Nữ)
     * @param birthDate        Ngày sinh
     * @param email            Email
     * @param address          Địa chỉ
     * @param memberTier       Tier membership
     * @param registrationDate Ngày đăng ký
     * @param lastValidDate    Lần ghé thăm cuối cùng
     * @param isActive         Trạng thái hoạt động
     * @param totalSpent       Tổng chi tiêu
     */
    public Customer(String personId, String fullName, String phoneNumber, boolean isMale, LocalDate birthDate,
            String email, String address, Tier memberTier, LocalDate registrationDate,
            LocalDate lastValidDate, boolean isActive, BigDecimal totalSpent) {
        super(personId, fullName, phoneNumber, isMale, birthDate, email, address);
        this.memberTier = memberTier;
        this.registrationDate = registrationDate;
        this.lastValidDate = lastValidDate;
        this.isActive = isActive;
        this.totalSpent = (totalSpent != null) ? totalSpent : BigDecimal.ZERO;
        // Tự động cập nhật tier dựa trên totalSpent
        updateTier();
    }

    // ============ Getter / Setter ============

    /**
     * Lấy tier membership của khách hàng.
     *
     * @return Tier membership hiện tại
     */
    public Tier getMemberTier() {
        return memberTier;
    }

    /**
     * Đặt tier membership cho khách hàng.
     *
     * @param memberTier Tier cần đặt
     */
    public void setMemberTier(Tier memberTier) {
        this.memberTier = memberTier;
    }

    /**
     * Lấy ngày ghé thăm cuối cùng.
     *
     * @return Ngày ghé thăm cuối cùng
     */
    public LocalDate getLastValidDate() {
        return lastValidDate;
    }

    /**
     * Đặt ngày ghé thăm cuối cùng.
     *
     * @param lastValidDate Ngày cần đặt
     */
    public void setLastValidDate(LocalDate lastValidDate) {
        this.lastValidDate = lastValidDate;
    }

    /**
     * Lấy ngày đăng ký membership.
     *
     * @return Ngày đăng ký
     */
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Đặt ngày đăng ký membership.
     *
     * @param registrationDate Ngày cần đặt
     */
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Kiểm tra khách hàng có đang hoạt động không.
     *
     * @return true nếu đang hoạt động, false nếu không
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Đặt trạng thái hoạt động của khách hàng.
     *
     * @param active Trạng thái mới
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Lấy tổng chi tiêu của khách hàng.
     *
     * @return Tổng chi tiêu (VND)
     */
    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    /**
     * Đặt tổng chi tiêu và tự động cập nhật tier.
     *
     * @param totalSpent Tổng chi tiêu mới (VND)
     */
    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = (totalSpent != null) ? totalSpent : BigDecimal.ZERO;
        // Tự động cập nhật tier mỗi khi thay đổi tổng chi tiêu
        updateTier();
    }
    // ============ Phương thức Chính (Core Methods) ============

    /**
     * Cập nhật tier membership dựa trên tổng chi tiêu.
     *
     * Logic:
     * - PLATINUM: totalSpent >= 10,000,000 VND
     * - GOLD: totalSpent >= 5,000,000 VND
     * - SILVER: totalSpent >= 1,000,000 VND
     * - BRONZE: totalSpent < 1,000,000 VND
     */
    public void updateTier() {
        if (totalSpent.compareTo(PLATINUM_THRESHOLD) >= 0) {
            this.memberTier = Tier.PLATINUM;
        } else if (totalSpent.compareTo(GOLD_THRESHOLD) >= 0) {
            this.memberTier = Tier.GOLD;
        } else if (totalSpent.compareTo(SILVER_THRESHOLD) >= 0) {
            this.memberTier = Tier.SILVER;
        } else {
            this.memberTier = Tier.BRONZE;
        }
    }

    /**
     * Cập nhật ngày ghé thăm cuối cùng thành ngày hôm nay.
     */
    public void updateLastVisit() {
        this.lastValidDate = LocalDate.now();
    }

    /**
     * Cộng thêm chi tiêu vào tổng và tự động cập nhật tier.
     *
     * @param amount Số tiền cần thêm (VND)
     */
    public void addToTotalSpent(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.totalSpent = this.totalSpent.add(amount);
            // Tự động cập nhật tier sau khi thêm chi tiêu
            updateTier();
        }
    }

    // ============ Override từ Person & IEntity ============

    /**
     * Lấy vai trò của người.
     * Với Customer, vai trò luôn là "Customer".
     *
     * @return "Customer"
     */
    @Override
    public String getRole() {
        return "Customer";
    }

    /**
     * Lấy prefix dùng cho sinh ID tự động.
     *
     * @return "CUST"
     */
    @Override
    public String getPrefix() {
        return "CUST";
    }

    /**
     * Hiển thị thông tin chi tiết khách hàng ra console.
     */
    @Override
    public void display() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ ID: " + getId());
        System.out.println("│ Họ tên: " + getFullName());
        System.out.println("│ SĐT: " + getPhoneNumber());
        System.out.println("│ Email: " + getEmail());
        System.out.println("│ Địa chỉ: " + getAddress());
        System.out.println("│ Hạng: " + memberTier);
        System.out.println("│ Ngày đăng ký: " + registrationDate);
        System.out.println("│ Tổng chi tiêu: " + totalSpent + " VND");
        System.out.println("│ Lần ghé gần nhất: " + (lastValidDate == null ? "Chưa có" : lastValidDate.toString()));
        System.out.println("│ Trạng thái: " + (isActive ? "Hoạt động" : "Không hoạt động"));
        System.out.println("└─────────────────────────────────────────────┘");
    }

    /**
     * Nhập thông tin khách hàng từ người dùng (sẽ implement sau).
     */
    @Override
    public void input() {
        // TODO: Implement nhập thông tin khách hàng từ người dùng
    }

    // ============ equals & hashCode ============

    /**
     * So sánh hai khách hàng dựa trên customerId.
     *
     * @param obj Đối tượng cần so sánh
     * @return true nếu cùng ID, false nếu khác
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Customer customer = (Customer) obj;
        return Objects.equals(getId(), customer.getId());
    }

    /**
     * Lấy hashCode dựa trên ID.
     *
     * @return hashCode của ID
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Biểu diễn chuỗi của Customer.
     *
     * @return Chuỗi dạng "Customer{id='...', name='...', tier='...'}"
     */
    @Override
    public String toString() {
        return String.format("Customer{id='%s', name='%s', tier='%s'}",
                getId(), getFullName(), memberTier);
    }
}
