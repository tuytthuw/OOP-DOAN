package models;

import interfaces.IEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Cung cấp thuộc tính chung cho mọi cá nhân trong hệ thống.
 */
public abstract class Person implements IEntity {
    protected static final String ID_PREFIX = "PER";
    protected static int runningNumber = 1;

    protected final String personId;
    protected String fullName;
    protected String phoneNumber;
    protected String email;
    protected LocalDate birthDate;
    protected final LocalDateTime createdAt;
    protected boolean isDeleted;

    protected Person(String fullName,
                     String phoneNumber,
                     String email,
                     LocalDate birthDate) {
        this.personId = generatePersonId();
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthDate = birthDate;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    @Override
    public String getId() {
        return personId;
    }

    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return Math.max(0, Period.between(birthDate, LocalDate.now()).getYears());
    }

    public void softDelete() {
        isDeleted = true;
    }

    public void restore() {
        isDeleted = false;
    }

    protected String generatePersonId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
