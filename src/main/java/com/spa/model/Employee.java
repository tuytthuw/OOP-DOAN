package com.spa.model;

import java.time.LocalDate;

/**
 * Lớp trừu tượng cho nhân viên.
 */
public abstract class Employee extends Person {
    private double salary;
    private String passwordHash;
    private LocalDate hireDate;

    protected Employee() {
        this(0.0, "", null);
    }

    protected Employee(double salary, String passwordHash, LocalDate hireDate) {
        super();
        this.salary = salary;
        this.passwordHash = passwordHash;
        this.hireDate = hireDate;
    }

    protected Employee(String personId,
                       String fullName,
                       String phoneNumber,
                       boolean male,
                       LocalDate birthDate,
                       String email,
                       String address,
                       boolean deleted,
                       double salary,
                       String passwordHash,
                       LocalDate hireDate) {
        super(personId, fullName, phoneNumber, male, birthDate, email, address, deleted);
        this.salary = salary;
        this.passwordHash = passwordHash;
        this.hireDate = hireDate;
    }

    /**
     * Tính lương của nhân viên.
     *
     * @return số tiền lương
     */
    public abstract double calculatePay();

    public boolean checkPassword(String rawPassword) {
        return passwordHash != null && passwordHash.equals(rawPassword);
    }

    @Override
    public String getRole() {
        return "EMPLOYEE";
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}
