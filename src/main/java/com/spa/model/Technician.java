package com.spa.model;

import java.time.LocalDate;

/**
 * Kỹ thuật viên thực hiện dịch vụ.
 */
public class Technician extends Employee {
    private static final String PREFIX = "TEC";

    private String skill;
    private String certifications;
    private double commissionRate;

    public Technician() {
        this("", "", "", true, null, "", "", false,
                0.0, "", null, "", "", 0.0);
    }

    public Technician(String personId,
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
                      String skill,
                      String certifications,
                      double commissionRate) {
        super(personId, fullName, phoneNumber, male, birthDate, email, address, deleted,
                salary, passwordHash, hireDate);
        this.skill = skill;
        this.certifications = certifications;
        this.commissionRate = commissionRate;
    }

    @Override
    public double calculatePay() {
        double baseSalary = getSalary();
        return baseSalary + (baseSalary * commissionRate);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public void display() {
        // Hiển thị sẽ được xử lý ở tầng UI.
    }

    @Override
    public void input() {
        // Sẽ bổ sung logic nhập liệu ở giai đoạn giao diện.
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    @Override
    public String toString() {
        return "Technician{" +
                "id='" + getId() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", skill='" + skill + '\'' +
                ", commissionRate=" + commissionRate +
                '}';
    }
}
