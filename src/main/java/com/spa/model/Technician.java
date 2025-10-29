package com.spa.model;

import com.spa.model.enums.EmployeeType;
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
        setEmployeeType(EmployeeType.TECHNICIAN);
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
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public void display() {
        System.out.println("---------------- THÔNG TIN KỸ THUẬT VIÊN ----------------");
        System.out.printf("Mã nhân viên : %s%n", getId());
        System.out.printf("Họ tên       : %s%n", getFullName());
        System.out.printf("Kỹ năng      : %s%n", skill == null || skill.isEmpty() ? "(trống)" : skill);
        System.out.printf("Chứng chỉ    : %s%n", certifications == null || certifications.isEmpty() ? "(trống)" : certifications);
        System.out.printf("Hoa hồng     : %.2f%%%n", commissionRate * 100);
        System.out.println("----------------------------------------------------------");
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
