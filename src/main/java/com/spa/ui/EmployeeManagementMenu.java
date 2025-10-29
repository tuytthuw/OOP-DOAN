package com.spa.ui;

import com.spa.model.Admin;
import com.spa.model.Employee;
import com.spa.model.Receptionist;
import com.spa.model.Technician;
import com.spa.service.Validation;
import java.time.LocalDate;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Menu quản lý nhân sự hợp nhất.
 */
public class EmployeeManagementMenu implements MenuModule {
    private static final int MAX_NOTES_LENGTH = 18;
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
            System.out.println("1. Thêm nhân viên");
            System.out.println("2. Danh sách nhân viên");
            System.out.println("3. Tìm kiếm nhân viên");
            System.out.println("4. Thống kê nhân sự");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    addEmployee();
                    Validation.pause();
                    break;
                case 2:
                    listEmployees();
                    Validation.pause();
                    break;
                case 3:
                    searchEmployees();
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

    private void addEmployee() {
        System.out.println();
        System.out.println("--- THÊM NHÂN VIÊN ---");
        System.out.println("1. Kỹ thuật viên");
        System.out.println("2. Lễ tân");
        System.out.println("3. Quản trị viên");
        int roleChoice = Validation.getInt("Chọn vai trò: ", 1, 3);

        String fullName = Validation.getString("Họ tên: ");
        String phone = Validation.getString("Số điện thoại: ");
        String email = Validation.getString("Email: ");
        String address = Validation.getString("Địa chỉ: ");
        boolean male = Validation.getBoolean("Giới tính nam?");
        LocalDate birthDate = Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT);
        double salary = Validation.getDouble("Lương cơ bản: ", 0.0, 1_000_000_000.0);
        String password = Validation.getString("Mật khẩu khởi tạo: ");

        Employee employee;
        if (roleChoice == 1) {
            employee = buildTechnician(fullName, phone, email, address, male, birthDate, salary, password);
        } else if (roleChoice == 2) {
            employee = buildReceptionist(fullName, phone, email, address, male, birthDate, salary, password);
        } else {
            employee = buildAdmin(fullName, phone, email, address, male, birthDate, salary, password);
        }

        context.getEmployeeStore().add(employee);
        context.getEmployeeStore().writeFile();
        System.out.println("Đã thêm nhân viên mới: " + employee.getFullName());
    }

    private Technician buildTechnician(String fullName, String phone, String email, String address,
                                       boolean male, LocalDate birthDate, double salary, String password) {
        Technician technician = new Technician();
        technician.setPersonId(context.getEmployeeStore().generateNextId(Technician.class));
        technician.setFullName(fullName);
        technician.setPhoneNumber(phone);
        technician.setEmail(email);
        technician.setAddress(address);
        technician.setMale(male);
        technician.setBirthDate(birthDate);
        technician.setHireDate(LocalDate.now());
        technician.setSalary(salary);
        technician.setSkill(Validation.getString("Kỹ năng chính: "));
        technician.setCertifications(Validation.getOptionalString("Chứng chỉ: "));
        technician.setCommissionRate(Validation.getDouble("Hoa hồng (0-1): ", 0.0, 1.0));
        technician.setPasswordHash(MenuHelper.hashPassword(password));
        technician.setDeleted(false);
        return technician;
    }

    private Receptionist buildReceptionist(String fullName, String phone, String email, String address,
                                           boolean male, LocalDate birthDate, double salary, String password) {
        Receptionist receptionist = new Receptionist();
        receptionist.setPersonId(context.getEmployeeStore().generateNextId(Receptionist.class));
        receptionist.setFullName(fullName);
        receptionist.setPhoneNumber(phone);
        receptionist.setEmail(email);
        receptionist.setAddress(address);
        receptionist.setMale(male);
        receptionist.setBirthDate(birthDate);
        receptionist.setHireDate(LocalDate.now());
        receptionist.setSalary(salary);
        receptionist.setMonthlyBonus(Validation.getDouble("Thưởng hàng tháng: ", 0.0, 1_000_000_000.0));
        receptionist.setPasswordHash(MenuHelper.hashPassword(password));
        receptionist.setDeleted(false);
        return receptionist;
    }

    private Admin buildAdmin(String fullName, String phone, String email, String address,
                              boolean male, LocalDate birthDate, double salary, String password) {
        Admin admin = new Admin();
        admin.setPersonId(context.getEmployeeStore().generateNextId(Admin.class));
        admin.setFullName(fullName);
        admin.setPhoneNumber(phone);
        admin.setEmail(email);
        admin.setAddress(address);
        admin.setMale(male);
        admin.setBirthDate(birthDate);
        admin.setHireDate(LocalDate.now());
        admin.setSalary(salary);
        admin.setPermissionGroup(Validation.getString("Nhóm quyền (cách nhau bằng dấu phẩy): "));
        admin.setSuperAdmin(Validation.getBoolean("Gán super admin?"));
        admin.setPasswordHash(MenuHelper.hashPassword(password));
        admin.setDeleted(false);
        return admin;
    }

    private void listEmployees() {
        Employee[] employees = context.getEmployeeStore().getAll();
        System.out.println();
        System.out.println("--- DANH SÁCH NHÂN SỰ ---");
        String header = String.format("%-8s | %-18s | %-12s | %-15s | %-10s | %-10s",
                "MÃ", "HỌ TÊN", "VAI TRÒ", "LIÊN HỆ", "TRẠNG THÁI", "GHI CHÚ");
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        boolean hasData = false;
        for (Employee employee : employees) {
            if (employee == null) {
                continue;
            }
            String role = employee.getRole();
            String status = employee.isDeleted() ? "Khóa" : "Hoạt động";
            String note = buildRoleNote(employee);
            System.out.printf("%-8s | %-18s | %-12s | %-15s | %-10s | %-10s%n",
                    nullToEmpty(employee.getId()),
                    nullToEmpty(employee.getFullName()),
                    nullToEmpty(role),
                    nullToEmpty(limitLength(employee.getPhoneNumber(), 15)),
                    status,
                    nullToEmpty(limitLength(note, MAX_NOTES_LENGTH)));
            hasData = true;
        }
        if (!hasData) {
            System.out.println("Chưa có nhân sự nào.");
        }
    }

    private void searchEmployees() {
        String keyword = Validation.getString("Từ khóa tìm kiếm: ").toLowerCase();
        Employee[] employees = context.getEmployeeStore().getAll();
        boolean found = false;
        for (Employee employee : employees) {
            if (employee == null) {
                continue;
            }
            String data = (nullToEmpty(employee.getFullName()) + " "
                    + nullToEmpty(employee.getPhoneNumber()) + " "
                    + nullToEmpty(employee.getRole())).toLowerCase();
            if (!data.contains(keyword)) {
                continue;
            }
            if (!found) {
                String header = String.format("%-8s | %-18s | %-12s | %-15s | %-10s | %-10s",
                        "MÃ", "HỌ TÊN", "VAI TRÒ", "LIÊN HỆ", "TRẠNG THÁI", "GHI CHÚ");
                System.out.println(header);
                System.out.println("-".repeat(header.length()));
            }
            found = true;
            String status = employee.isDeleted() ? "Khóa" : "Hoạt động";
            String note = buildRoleNote(employee);
            System.out.printf("%-8s | %-18s | %-12s | %-15s | %-10s | %-10s%n",
                    nullToEmpty(employee.getId()),
                    nullToEmpty(employee.getFullName()),
                    nullToEmpty(employee.getRole()),
                    nullToEmpty(limitLength(employee.getPhoneNumber(), 15)),
                    status,
                    nullToEmpty(limitLength(note, MAX_NOTES_LENGTH)));
        }
        if (!found) {
            System.out.println("Không tìm thấy nhân viên phù hợp.");
        }
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

    private String buildRoleNote(Employee employee) {
        if (employee instanceof Technician) {
            Technician technician = (Technician) employee;
            return "Skill: " + nullToEmpty(technician.getSkill());
        }
        if (employee instanceof Receptionist) {
            Receptionist receptionist = (Receptionist) employee;
            return "Bonus: " + receptionist.getMonthlyBonus();
        }
        if (employee instanceof Admin) {
            Admin admin = (Admin) employee;
            return "Perm: " + nullToEmpty(admin.getPermissionGroup());
        }
        return "";
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private String limitLength(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
