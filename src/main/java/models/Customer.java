package models;

import enums.Tier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Đại diện cho khách hàng của spa.
 */
public class Customer extends Person {
    private Tier tier;
    private BigDecimal totalSpent;
    private int loyaltyPoints;
    private LocalDateTime lastVisitAt;
    private String notes;

    public Customer(String fullName,
                    String phoneNumber,
                    String email,
                    LocalDate birthDate,
                    Tier tier) {
        super(fullName, phoneNumber, email, birthDate);
        this.tier = tier == null ? Tier.BRONZE : tier;
        this.totalSpent = BigDecimal.ZERO;
        this.loyaltyPoints = 0;
        this.lastVisitAt = null;
        this.notes = null;
    }

    /**
     * Cộng dồn chi tiêu và điểm loyalty.
     *
     * @param amount Số tiền phát sinh.
     */
    public void addSpending(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return;
        }
        totalSpent = totalSpent.add(amount);
        loyaltyPoints += amount.intValue();
    }

    /**
     * Đổi điểm tích lũy.
     *
     * @param value Số điểm muốn đổi.
     */
    public void redeemPoints(int value) {
        if (value <= 0 || value > loyaltyPoints) {
            return;
        }
        loyaltyPoints -= value;
    }

    /**
     * Cập nhật hạng thành viên của khách hàng.
     *
     * @param newTier Hạng mới.
     */
    public void updateTier(Tier newTier) {
        if (newTier != null) {
            this.tier = newTier;
        }
    }

    /**
     * Ghi nhận lần ghé gần nhất.
     *
     * @param visitAt Thời điểm ghé.
     */
    public void markVisit(LocalDateTime visitAt) {
        this.lastVisitAt = visitAt;
    }

    public Tier getTier() {
        return tier;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public LocalDateTime getLastVisitAt() {
        return lastVisitAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
