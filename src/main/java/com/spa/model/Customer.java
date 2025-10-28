package com.spa.model;

import com.spa.model.enums.Tier;
import java.time.LocalDate;

/**
 * Khách hàng của spa.
 */
public class Customer extends Person {
    private static final String PREFIX = "CUS";

    private Tier memberTier;
    private String notes;
    private int points;
    private LocalDate lastVisitDate;

    public Customer() {
        this("", "", "", true, null, "", "", false,
                Tier.STANDARD, "", 0, null);
    }

    public Customer(String personId,
                    String fullName,
                    String phoneNumber,
                    boolean male,
                    LocalDate birthDate,
                    String email,
                    String address,
                    boolean deleted,
                    Tier memberTier,
                    String notes,
                    int points,
                    LocalDate lastVisitDate) {
        super(personId, fullName, phoneNumber, male, birthDate, email, address, deleted);
        this.memberTier = memberTier;
        this.notes = notes;
        this.points = points;
        this.lastVisitDate = lastVisitDate;
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public void display() {
        // Giai đoạn này chưa in trực tiếp ra giao diện.
    }

    @Override
    public void input() {
        // Giai đoạn này chưa nhập liệu trực tiếp.
    }

    /**
     * Cộng điểm thưởng cho khách hàng.
     *
     * @param additionalPoints số điểm tăng thêm
     */
    public void earnPoints(int additionalPoints) {
        if (additionalPoints <= 0) {
            return;
        }
        points += additionalPoints;
    }

    /**
     * Thử nâng hạng thành viên dựa trên điểm tích lũy hiện tại.
     */
    public void upgradeTier() {
        if (points >= 1000) {
            memberTier = Tier.GOLD;
        } else if (points >= 500) {
            memberTier = Tier.SILVER;
        } else {
            memberTier = Tier.STANDARD;
        }
    }

    /**
     * Lấy tỷ lệ giảm giá tương ứng với hạng thành viên.
     *
     * @return tỷ lệ giảm giá (0-1)
     */
    public double getDiscountRate() {
        if (memberTier == Tier.GOLD) {
            return 0.15;
        }
        if (memberTier == Tier.SILVER) {
            return 0.1;
        }
        return 0.05;
    }

    public Tier getMemberTier() {
        return memberTier;
    }

    public void setMemberTier(Tier memberTier) {
        this.memberTier = memberTier;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDate getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(LocalDate lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + getId() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", tier=" + memberTier +
                ", points=" + points +
                ", lastVisitDate=" + lastVisitDate +
                '}';
    }
}
