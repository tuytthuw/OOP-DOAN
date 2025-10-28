package com.spa.model;

import com.spa.interfaces.IEntity;
import java.time.LocalDate;

/**
 * Lớp trừu tượng cơ sở cho mọi đối tượng người trong hệ thống.
 */
public abstract class Person implements IEntity {
    private static final String DEFAULT_PREFIX = "PER";

    private String personId;
    private String fullName;
    private String phoneNumber;
    private boolean male;
    private LocalDate birthDate;
    private String email;
    private String address;
    private boolean deleted;

    protected Person() {
        this("", "", "", true, null, "", "", false);
    }

    protected Person(String personId,
                     String fullName,
                     String phoneNumber,
                     boolean male,
                     LocalDate birthDate,
                     String email,
                     String address,
                     boolean deleted) {
        this.personId = personId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.male = male;
        this.birthDate = birthDate;
        this.email = email;
        this.address = address;
        this.deleted = deleted;
    }

    @Override
    public String getId() {
        return personId;
    }

    @Override
    public void display() {
        // Giai đoạn này chưa hiển thị trực tiếp.
    }

    @Override
    public void input() {
        // Giai đoạn này chưa nhập liệu trực tiếp.
    }

    @Override
    public String getPrefix() {
        return DEFAULT_PREFIX;
    }

    public abstract String getRole();

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Person{"
                + "personId='" + personId + '\''
                + ", fullName='" + fullName + '\''
                + ", phoneNumber='" + phoneNumber + '\''
                + ", male=" + male
                + ", birthDate=" + birthDate
                + ", email='" + email + '\''
                + ", address='" + address + '\''
                + ", deleted=" + deleted
                + '}';
    }
}
