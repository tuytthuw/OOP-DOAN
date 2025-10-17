package models;

import enums.Tier;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Customer extends Person {
    private Tier memberTier;
    private BigDecimal totalSpent;
    private LocalDate registrationDate;
    private LocalDate lastValidDate;
    private boolean isActive;
    //Constructor
    public Customer(String personId, String fullName, String phoneNumber, boolean isMale, LocalDate birthDate,
                    String email, String address, Tier memberTier, LocalDate lastValidDate, LocalDate registrationDate ,boolean isActive, BigDecimal totalSpent){
        super(personId, fullName, phoneNumber, isMale, birthDate, email, address);
        this.memberTier = memberTier;
        this.lastValidDate = lastValidDate;
        this.registrationDate = registrationDate;
        this.isActive = isActive;
        this.totalSpent = BigDecimal.ZERO;

    }

    public Tier getMemberTier() {
        return memberTier;
    }

    public void setMemberTier(Tier memberTier) {
        this.memberTier = memberTier;
    }

    public LocalDate getLastValidDate() {
        return lastValidDate;
    }

    public void setLastValidDate(LocalDate lastValidDate) {
        this.lastValidDate = lastValidDate;
    }
    public LocalDate getRegistrationDate(){
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
        updateTier(); // tự động cập nhật tier mỗi khi set lại tổng chi tiêu
    }
    //Phương thức
    /**
     * Cập nhật tier dựa trên tổng chi tiêu.
     */
    public void updateTier() {
        if (totalSpent.compareTo(new BigDecimal("10000000")) >= 0) {
            this.memberTier = Tier.PLATINUM;
        } else if (totalSpent.compareTo(new BigDecimal("5000000")) >= 0) {
            this.memberTier = Tier.GOLD;
        } else if (totalSpent.compareTo(new BigDecimal("1000000")) >= 0) {
            this.memberTier = Tier.SILVER;
        } else {
            this.memberTier = Tier.BRONZE;
        }
    }
    /**
     * Cập nhật ngày ghé thăm cuối cùng thành hôm nay.
     */
    public void updateLastVisit() {
        this.lastValidDate = LocalDate.now();
    }
    /**
     * Cộng thêm chi tiêu.
     * @param amount số tiền thêm vào
     */
    public void addToTotalSpent(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.totalSpent = this.totalSpent.add(amount);
            updateTier();
        }
    }
    /**
     * Trả về thông tin chi tiết khách hàng.
     */
    public String getCustomerInfo() {
        return String.format(
                """
                === THÔNG TIN KHÁCH HÀNG ===
                ID: %s
                Họ tên: %s
                SĐT: %s
                Email: %s
                Địa chỉ: %s
                Hạng: %s
                Ngày đăng ký: %s
                Tổng chi tiêu: %s VND
                Lần ghé gần nhất: %s
                Trạng thái: %s
                =============================
                """,
                getId(), getFullName(), getPhoneNumber(), getEmail(), getAddress(),
                memberTier, registrationDate, totalSpent,
                (lastValidDate == null ? "Chưa có" : lastValidDate.toString()),
                (isActive ? "Đang hoạt động" : "Ngưng hoạt động")
        );
    }
    // ==========================
    // Override từ Person & IEntity
    // ==========================

    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public String getPrefix() {
        return "CUST";
    }

    @Override
    public void display() {
        System.out.println(getCustomerInfo());
    }

    @Override
    public void input() {
    }
    // ==========================
    // equals & hashCode dựa theo customerId
    // ==========================

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        return obj instanceof Customer;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return String.format("Customer{id='%s', name='%s', tier='%s'}", getId(), getFullName(), memberTier);
    }
}
