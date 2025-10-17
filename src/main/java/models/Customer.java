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
                    String email, String address, Tier memberTier, String notes, int points, LocalDate lastValidDate ){
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
    public void updateLastVisit() {
        this.lastValidDate = LocalDate.now();
    }

}
