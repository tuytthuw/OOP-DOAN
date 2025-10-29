package com.spa.ui;

import com.spa.model.Admin;
import com.spa.model.Employee;
import com.spa.model.Receptionist;
import com.spa.model.Technician;
import com.spa.service.Validation;

/**
 * Menu quản lý nhân sự, tách riêng theo nhóm vai trò.
 */
public class EmployeeManagementMenu implements MenuModule {
    private final MenuContext context;

    public EmployeeManagementMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ NHÂN SỰ ---");
            System.out.println("1. Kỹ thuật viên");
            System.out.println("2. Lễ tân");
            System.out.println("3. Quản trị viên");
            System.out.println("4. Thống kê nhân sự");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    handleTechnicianMenu();
                    Validation.pause();
                    break;
                case 2:
                    handleReceptionistMenu();
                    Validation.pause();
                    break;
                case 3:
                    handleAdminMenu();
                    Validation.pause();
                    break;
                case 4:
                    showEmployeeStatistics();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void handleTechnicianMenu() {
        TechnicianMenu menu = new TechnicianMenu(context);
        menu.show();
    }

    private void handleReceptionistMenu() {
        ReceptionistMenu menu = new ReceptionistMenu(context);
        menu.show();
    }

    private void handleAdminMenu() {
        AdminMenu menu = new AdminMenu(context);
        menu.show();
    }

    private void showEmployeeStatistics() {
        Employee[] employees = context.getEmployeeStore().getAll();
        int technicianCount = 0;
        int receptionistCount = 0;
        int adminCount = 0;
        for (Employee employee : employees) {
            if (employee == null || employee.isDeleted()) {
                continue;
            }
            if (employee instanceof Technician) {
                technicianCount++;
            } else if (employee instanceof Receptionist) {
                receptionistCount++;
            } else if (employee instanceof Admin) {
                adminCount++;
            }
        }
        System.out.println();
        System.out.println("--- THỐNG KÊ NHÂN SỰ ---");
        System.out.printf("Kỹ thuật viên : %d%n", technicianCount);
        System.out.printf("Lễ tân        : %d%n", receptionistCount);
        System.out.printf("Quản trị viên : %d%n", adminCount);
    }
}
