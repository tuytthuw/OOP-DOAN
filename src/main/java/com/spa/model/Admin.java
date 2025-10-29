package com.spa.model;

import com.spa.model.enums.EmployeeType;
import java.time.LocalDate;

/**
 * Quản trị viên hệ thống với quyền truy cập cao nhất.
 */
public class Admin extends Employee {
    private static final String PREFIX = "ADM";

    private String permissionGroup;
    private boolean superAdmin;

    public Admin() {
        this("", "", "", true, null, "", "", false,
                0.0, "", null, "DEFAULT", false);
    }

    public Admin(String personId,
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
                 String permissionGroup,
                 boolean superAdmin) {
        super(personId, fullName, phoneNumber, male, birthDate, email, address, deleted,
                salary, passwordHash, hireDate);
        setEmployeeType(EmployeeType.ADMIN);
        this.permissionGroup = permissionGroup;
        this.superAdmin = superAdmin;
    }

    @Override
    public double calculatePay() {
        return getSalary();
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public void display() {
        System.out.println("---------------- THÔNG TIN ADMIN ----------------");
        System.out.printf("Mã nhân viên : %s%n", getId());
        System.out.printf("Họ tên       : %s%n", getFullName());
        System.out.printf("Nhóm quyền   : %s%n", permissionGroup);
        System.out.printf("Super admin  : %s%n", superAdmin ? "Có" : "Không");
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void input() {
        // Nhập liệu ở tầng UI.
    }

    /**
     * Kiểm tra quyền truy cập với mã module.
     *
     * @param moduleKey khóa module cần kiểm tra
     * @return true nếu admin có thể truy cập
     */
    public boolean canAccess(String moduleKey) {
        if (superAdmin) {
            return true;
        }
        if (moduleKey == null || moduleKey.trim().isEmpty() || permissionGroup == null) {
            return false;
        }
        String[] tokens = permissionGroup.split(",");
        for (String token : tokens) {
            if (moduleKey.equalsIgnoreCase(token.trim())) {
                return true;
            }
        }
        return false;
    }

    public String getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(String permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }
}
