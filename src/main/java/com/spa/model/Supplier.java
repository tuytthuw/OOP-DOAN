package com.spa.model;

import com.spa.interfaces.IEntity;

/**
 * Nhà cung cấp sản phẩm cho spa.
 */
public class Supplier implements IEntity {
    private static final String PREFIX = "SUP";

    private String supplierId;
    private String supplierName;
    private String contactPerson;
    private String phoneNumber;
    private String address;
    private String email;
    private String notes;
    private boolean deleted;

    public Supplier() {
        this("", "", "", "", "", "", "", false);
    }

    public Supplier(String supplierId,
                    String supplierName,
                    String contactPerson,
                    String phoneNumber,
                    String address,
                    String email,
                    String notes,
                    boolean deleted) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.notes = notes;
        this.deleted = deleted;
    }

    @Override
    public String getId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public void display() {
        // Hiển thị ở tầng UI.
    }

    @Override
    public void input() {
        // Nhập liệu ở tầng UI.
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id='" + supplierId + '\'' +
                ", name='" + supplierName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                "}";
    }
}
