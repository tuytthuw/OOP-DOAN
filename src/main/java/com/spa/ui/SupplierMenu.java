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
            System.out.println("1. Thêm nhà cung cấp");
            System.out.println("2. Thêm nhiều nhà cung cấp");
            System.out.println("3. Xuất danh sách");
            System.out.println("4. Cập nhật nhà cung cấp");
            System.out.println("5. Xóa nhà cung cấp");
            System.out.println("6. Tìm kiếm nhà cung cấp");
            System.out.println("7. Thống kê nhà cung cấp");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 7);
            switch (choice) {
                case 1:
                    addSupplier();
                    Validation.pause();
                    break;
                case 2:
                    addMultipleSuppliers();
                    Validation.pause();
                    break;
                case 3:
                    listSuppliers();
                    Validation.pause();
                    break;
                case 4:
                    updateSupplier();
                    Validation.pause();
                    break;
                case 5:
                    deleteSupplier();
                    Validation.pause();
                    break;
                case 6:
                    searchSuppliers();
                    Validation.pause();
                    break;
                case 7:
                    showSupplierStatistics();
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
            supplier.display();
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
        if (supplier == null) {
            System.out.println("Đã hủy thêm nhà cung cấp.");
            return;
        }
        context.getSupplierStore().add(supplier);
        context.getSupplierStore().writeFile();
        System.out.println("Đã thêm nhà cung cấp thành công.");
        supplier.display();
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
        if (updated == null) {
            System.out.println("Đã hủy cập nhật nhà cung cấp.");
            return;
        }
        supplier.setSupplierName(updated.getSupplierName());
        supplier.setContactPerson(updated.getContactPerson());
        supplier.setPhoneNumber(updated.getPhoneNumber());
        supplier.setAddress(updated.getAddress());
        supplier.setEmail(updated.getEmail());
        supplier.setNotes(updated.getNotes());
        context.getSupplierStore().writeFile();
        System.out.println("Đã cập nhật nhà cung cấp.");
        supplier.display();
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
        String name = Validation.getStringOrCancel(buildPrompt("Tên nhà cung cấp", base == null ? null : base.getSupplierName()));
        if (name == null) {
            return null;
        }
        String contact = Validation.getStringOrCancel(buildPrompt("Người liên hệ", base == null ? null : base.getContactPerson()));
        if (contact == null) {
            return null;
        }
        String phone = Validation.getStringOrCancel(buildPrompt("Số điện thoại", base == null ? null : base.getPhoneNumber()));
        if (phone == null) {
            return null;
        }
        String address = Validation.getStringOrCancel(buildPrompt("Địa chỉ", base == null ? null : base.getAddress()));
        if (address == null) {
            return null;
        }
        String email = Validation.getStringOrCancel(buildPrompt("Email", base == null ? null : base.getEmail()));
        if (email == null) {
            return null;
        }
        String notes = Validation.getStringOrCancel(buildPrompt("Ghi chú", base == null ? null : base.getNotes()));
        if (notes == null) {
            return null;
        }
        Supplier supplier = new Supplier(id, name, contact, phone, address, email, notes, false);
        if (base != null && base.isDeleted()) {
            supplier.setDeleted(true);
        }
        return supplier;
    }

    private void addMultipleSuppliers() {
        Integer total = Validation.getIntOrCancel("Số lượng nhà cung cấp cần thêm", 1, 1000);
        if (total == null) {
            System.out.println("Đã hủy thao tác.");
            return;
        }
        int added = 0;
        for (int i = 0; i < total; i++) {
            System.out.println("-- Nhà cung cấp thứ " + (i + 1));
            String id = context.getSupplierStore().generateNextId();
            Supplier supplier = promptSupplier(id, null);
            if (supplier == null) {
                System.out.println("Dừng thêm nhà cung cấp.");
                break;
            }
            context.getSupplierStore().add(supplier);
            added++;
        }
        context.getSupplierStore().writeFile();
        System.out.printf("Đã thêm %d nhà cung cấp mới.%n", added);
    }

    private void searchSuppliers() {
        String keywordsLine = Validation.getOptionalStringOrCancel("Nhập từ khóa (cách nhau bởi dấu cách) hoặc bỏ trống để xem tất cả: ");
        if (keywordsLine == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        String trimmedKeywords = keywordsLine.trim().toLowerCase();
        Boolean activeFilter = Validation.getBooleanOrCancel("Chỉ hiển thị nhà cung cấp đang hoạt động?");

        Supplier[] suppliers = context.getSupplierStore().getAll();
        boolean foundAny = false;
        for (Supplier supplier : suppliers) {
            if (supplier == null) {
                continue;
            }
            if (activeFilter != null && supplier.isDeleted() == activeFilter.booleanValue()) {
                continue;
            }
            if (!matchesTokens(supplier, trimmedKeywords.split("\\s+"))) {
                continue;
            }
            supplier.display();
            foundAny = true;
        }
        if (!foundAny) {
            System.out.println("Không có nhà cung cấp phù hợp.");
        }
    }

    private void showSupplierStatistics() {
        Supplier[] suppliers = context.getSupplierStore().getAll();
        if (suppliers.length == 0) {
            System.out.println("Chưa có dữ liệu nhà cung cấp.");
            return;
        }
        int total = 0;
        int active = 0;
        int deleted = 0;
        int withNotes = 0;
        int withEmail = 0;
        int noContact = 0;
        for (Supplier supplier : suppliers) {
            if (supplier == null) {
                continue;
            }
            total++;
            if (supplier.isDeleted()) {
                deleted++;
            } else {
                active++;
            }
            if (supplier.getNotes() != null && !supplier.getNotes().trim().isEmpty()) {
                withNotes++;
            }
            if (supplier.getEmail() != null && !supplier.getEmail().trim().isEmpty()) {
                withEmail++;
            }
            if (supplier.getContactPerson() == null || supplier.getContactPerson().trim().isEmpty()) {
                noContact++;
            }
        }
        System.out.printf("Tổng số nhà cung cấp: %d%n", total);
        System.out.printf("Đang hoạt động: %d | Đã khóa: %d%n", active, deleted);
        System.out.printf("Có ghi chú: %d%n", withNotes);
        System.out.printf("Có email: %d%n", withEmail);
        System.out.printf("Chưa thiết lập người liên hệ: %d%n", noContact);
    }

    private String buildPrompt(String label, String current) {
        if (current == null || current.isEmpty()) {
            return label + ": ";
        }
        return String.format("%s (hiện tại: %s): ", label, current);
    }

    private boolean matchesTokens(Supplier supplier, String[] tokens) {
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            return true;
        }
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            String lower = token.toLowerCase();
            boolean match = containsIgnoreCase(supplier.getId(), lower)
                    || containsIgnoreCase(supplier.getSupplierName(), lower)
                    || containsIgnoreCase(supplier.getContactPerson(), lower)
                    || containsIgnoreCase(supplier.getPhoneNumber(), lower)
                    || containsIgnoreCase(supplier.getAddress(), lower)
                    || containsIgnoreCase(supplier.getEmail(), lower)
                    || containsIgnoreCase(supplier.getNotes(), lower);
            if (!match) {
                return false;
            }
        }
        return true;
    }

    private boolean containsIgnoreCase(String source, String token) {
        if (source == null || token == null || token.isEmpty()) {
            return false;
        }
        return source.toLowerCase().contains(token);
    }
}
