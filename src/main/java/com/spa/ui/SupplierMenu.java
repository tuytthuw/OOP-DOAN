package com.spa.ui;

import com.spa.model.Supplier;
import com.spa.service.Validation;

/**
 * Menu quản lý nhà cung cấp.
 */
public class SupplierMenu implements MenuModule {
    private final MenuContext context;

    public SupplierMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ NHÀ CUNG CẤP ---");
            System.out.println("1. Xem danh sách nhà cung cấp");
            System.out.println("2. Thêm nhà cung cấp");
            System.out.println("3. Cập nhật nhà cung cấp");
            System.out.println("4. Xóa nhà cung cấp");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listSuppliers();
                    Validation.pause();
                    break;
                case 2:
                    addSupplier();
                    Validation.pause();
                    break;
                case 3:
                    updateSupplier();
                    Validation.pause();
                    break;
                case 4:
                    deleteSupplier();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listSuppliers() {
        System.out.println();
        System.out.println("--- DANH SÁCH NHÀ CUNG CẤP ---");
        Supplier[] suppliers = context.getSupplierStore().getAll();
        boolean hasData = false;
        for (Supplier supplier : suppliers) {
            if (supplier == null || supplier.isDeleted()) {
                continue;
            }
            hasData = true;
            System.out.printf("%s | %s | %s | %s%n",
                    supplier.getId(),
                    supplier.getSupplierName(),
                    supplier.getContactPerson(),
                    supplier.getPhoneNumber());
        }
        if (!hasData) {
            System.out.println("Chưa có nhà cung cấp nào.");
        }
    }

    private void addSupplier() {
        System.out.println();
        System.out.println("--- THÊM NHÀ CUNG CẤP ---");
        String id = context.getSupplierStore().generateNextId();
        System.out.println("Mã nhà cung cấp được cấp: " + id);
        Supplier supplier = promptSupplier(id, null);
        context.getSupplierStore().add(supplier);
        context.getSupplierStore().writeFile();
        System.out.println("Đã thêm nhà cung cấp thành công.");
    }

    private void updateSupplier() {
        System.out.println();
        System.out.println("--- CẬP NHẬT NHÀ CUNG CẤP ---");
        String id = Validation.getString("Mã nhà cung cấp: ");
        Supplier supplier = context.getSupplierStore().findById(id);
        if (supplier == null || supplier.isDeleted()) {
            System.out.println("Không tìm thấy nhà cung cấp.");
            return;
        }
        Supplier updated = promptSupplier(id, supplier);
        supplier.setSupplierName(updated.getSupplierName());
        supplier.setContactPerson(updated.getContactPerson());
        supplier.setPhoneNumber(updated.getPhoneNumber());
        supplier.setAddress(updated.getAddress());
        supplier.setEmail(updated.getEmail());
        supplier.setNotes(updated.getNotes());
        context.getSupplierStore().writeFile();
        System.out.println("Đã cập nhật nhà cung cấp.");
    }

    private void deleteSupplier() {
        System.out.println();
        System.out.println("--- XÓA NHÀ CUNG CẤP ---");
        String id = Validation.getString("Mã nhà cung cấp: ");
        if (context.getSupplierStore().delete(id)) {
            context.getSupplierStore().writeFile();
            System.out.println("Đã xóa nhà cung cấp.");
        } else {
            System.out.println("Không tìm thấy nhà cung cấp.");
        }
    }

    private Supplier promptSupplier(String id, Supplier base) {
        String name = Validation.getString("Tên nhà cung cấp: ");
        String contact = Validation.getString("Người liên hệ: ");
        String phone = Validation.getString("Số điện thoại: ");
        String address = Validation.getString("Địa chỉ: ");
        String email = Validation.getString("Email: ");
        String notes = Validation.getString("Ghi chú: ");
        Supplier supplier = new Supplier(id, name, contact, phone, address, email, notes, false);
        if (base != null && base.isDeleted()) {
            supplier.setDeleted(true);
        }
        return supplier;
    }
}
