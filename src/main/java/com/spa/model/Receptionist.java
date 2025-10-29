package com.spa.model;

import com.spa.model.enums.EmployeeType;
import java.time.LocalDate;

/**
 * Lễ tân quản lý lịch hẹn và thanh toán.
 */
public class Receptionist extends Employee {
    private static final String PREFIX = "REC";

    private double monthlyBonus;

    public Receptionist() {
        this("", "", "", true, null, "", "", false,
                0.0, "", null, 0.0);
    }

    public Receptionist(String personId,
                        String fullName,
                        String phoneNumber,
                        boolean male,
                        LocalDate birthDate,
                        String email,
                        String address,
                        boolean deleted,
                        double salary,
                        String passwordHash,
                        LocalDate hireDate,
                        double monthlyBonus) {
        super(personId, fullName, phoneNumber, male, birthDate, email, address, deleted,
                salary, passwordHash, hireDate);
        setEmployeeType(EmployeeType.RECEPTIONIST);
        this.monthlyBonus = monthlyBonus;
    }

    @Override
    public double calculatePay() {
        return getSalary() + monthlyBonus;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override

    public void display() {
        System.out.println("---------------- THÔNG TIN LỄ TÂN ----------------");
        System.out.printf("Mã nhân viên : %s%n", getId());
        System.out.printf("Họ tên       : %s%n", getFullName());
        System.out.printf("Thưởng tháng : %.2f%n", monthlyBonus);
        System.out.println("---------------------------------------------------");
    }

    @Override
    public void input() {
        // Logic nhập liệu sẽ được bổ sung sau.
    }

    public double getMonthlyBonus() {
        return monthlyBonus;
    }

    public void setMonthlyBonus(double monthlyBonus) {
        this.monthlyBonus = monthlyBonus;
    }

    @Override
    public String toString() {
        return "Receptionist{" +
                "id='" + getId() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", monthlyBonus=" + monthlyBonus +
                '}';
    }
}
