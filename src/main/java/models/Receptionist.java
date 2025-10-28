package models;

import enums.EmployeeRole;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Receptionist extends Employee {
    private BigDecimal bonusPerSale;
    private int monthlyTarget;
    private int achievedSalesCount;
    private BigDecimal totalSales;

    public Receptionist(String fullName,
                        String phoneNumber,
                        String email,
                        LocalDate birthDate,
                        BigDecimal baseSalary,
                        String passwordHash,
                        LocalDate hireDate,
                        BigDecimal bonusPerSale,
                        int monthlyTarget) {
        super(fullName, phoneNumber, email, birthDate, EmployeeRole.RECEPTIONIST, baseSalary, passwordHash, hireDate);
        this.bonusPerSale = bonusPerSale == null ? BigDecimal.ZERO : bonusPerSale;
        this.monthlyTarget = Math.max(0, monthlyTarget);
        this.achievedSalesCount = 0;
        this.totalSales = BigDecimal.ZERO;
    }

    public void recordSale(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return;
        }
        achievedSalesCount++;
        totalSales = totalSales.add(amount);
    }

    @Override
    public BigDecimal calculatePay() {
        BigDecimal bonus = bonusPerSale.multiply(BigDecimal.valueOf(achievedSalesCount));
        BigDecimal targetBonus = achievedSalesCount >= monthlyTarget && monthlyTarget > 0
                ? bonusPerSale.multiply(BigDecimal.valueOf(monthlyTarget)).multiply(BigDecimal.valueOf(0.1))
                : BigDecimal.ZERO;
        return salary.add(bonus).add(targetBonus);
    }

    public int getAchievedSalesCount() {
        return achievedSalesCount;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setBonusPerSale(BigDecimal bonusPerSale) {
        if (bonusPerSale != null && bonusPerSale.signum() >= 0) {
            this.bonusPerSale = bonusPerSale;
        }
    }

    public BigDecimal getBonusPerSale() {
        return bonusPerSale;
    }

    public void setMonthlyTarget(int monthlyTarget) {
        if (monthlyTarget >= 0) {
            this.monthlyTarget = monthlyTarget;
        }
    }

    public int getMonthlyTarget() {
        return monthlyTarget;
    }
}
