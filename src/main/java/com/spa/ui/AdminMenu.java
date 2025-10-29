package com.spa.ui;

import com.spa.model.Admin;
import com.spa.service.Validation;
import java.time.LocalDate;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Menu quản lý quản trị viên.
 */
public class AdminMenu implements MenuModule {
    private final MenuContext context;

    public AdminMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ QUẢN TRỊ VIÊN ---");
            System.out.println("1. Thêm quản trị viên");
            System.out.println("2. Danh sách quản trị viên");
            System.out.println("3. Tìm kiếm theo nhóm quyền");
            System.out.println("4. Cập nhật nhóm quyền");
            System.out.println("5. Bật/Tắt super admin");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 5);
            switch (choice) {
                case 1:
                    addAdmin();
                    Validation.pause();
                    break;
                case 2:
                    listAdmins();
                    Validation.pause();
                    break;
                case 3:
                    searchByModule();
                    Validation.pause();
                    break;
                case 4:
                    updatePermissionGroup();
                    Validation.pause();
                    break;
                case 5:
                    toggleSuperAdmin();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void addAdmin() {
        Admin admin = new Admin();
        admin.setPersonId(context.getEmployeeStore().generateNextId(Admin.class));
        admin.setFullName(Validation.getString("Họ tên: "));
        admin.setPhoneNumber(Validation.getString("Số điện thoại: "));
        admin.setEmail(Validation.getString("Email: "));
        admin.setAddress(Validation.getString("Địa chỉ: "));
        admin.setMale(Validation.getBoolean("Giới tính nam?"));
        admin.setBirthDate(Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT));
        admin.setHireDate(LocalDate.now());
        admin.setSalary(Validation.getDouble("Lương cơ bản: ", 0.0, 1_000_000_000.0));
        admin.setPermissionGroup(Validation.getString("Nhóm quyền (cách nhau bằng dấu phẩy): "));
        admin.setSuperAdmin(Validation.getBoolean("Gán super admin?"));
        String password = Validation.getString("Mật khẩu khởi tạo: ");
        admin.setPasswordHash(MenuHelper.hashPassword(password));
        admin.setDeleted(false);

        context.getEmployeeStore().add(admin);
        context.getEmployeeStore().writeFile();
        System.out.println("Đã thêm quản trị viên mới.");
    }

    private void listAdmins() {
        Admin[] admins = context.getEmployeeStore().getAllAdmins();
        if (admins.length == 0) {
            System.out.println("Chưa có quản trị viên nào.");
            return;
        }
        for (Admin admin : admins) {
            if (admin != null && !admin.isDeleted()) {
                admin.display();
            }
        }
    }

    private void searchByModule() {
        String moduleKey = Validation.getString("Nhập module cần tìm: ");
        Admin[] admins = context.getEmployeeStore().getAllAdmins();
        boolean found = false;
        for (Admin admin : admins) {
            if (admin == null || admin.isDeleted()) {
                continue;
            }
            if (admin.canAccess(moduleKey)) {
                found = true;
                admin.display();
            }
        }
        if (!found) {
            System.out.println("Không có quản trị viên nào có quyền truy cập module này.");
        }
    }

    private void updatePermissionGroup() {
        String id = Validation.getString("Mã quản trị viên: ");
        Admin admin = context.getEmployeeStore().findAdminById(id);
        if (admin == null) {
            System.out.println("Không tìm thấy quản trị viên.");
            return;
        }
        String newGroup = Validation.getString("Nhập nhóm quyền mới: ");
        admin.setPermissionGroup(newGroup);
        context.getEmployeeStore().writeFile();
        System.out.println("Đã cập nhật nhóm quyền.");
    }

    private void toggleSuperAdmin() {
        String id = Validation.getString("Mã quản trị viên: ");
        Admin admin = context.getEmployeeStore().findAdminById(id);
        if (admin == null) {
            System.out.println("Không tìm thấy quản trị viên.");
            return;
        }
        admin.setSuperAdmin(!admin.isSuperAdmin());
        context.getEmployeeStore().writeFile();
        System.out.println(admin.isSuperAdmin() ? "Đã bật chế độ super admin." : "Đã tắt chế độ super admin.");
    }
}
