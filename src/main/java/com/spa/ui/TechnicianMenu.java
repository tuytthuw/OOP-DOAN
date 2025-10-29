  package com.spa.ui;

import com.spa.model.Appointment;
import com.spa.model.Technician;
import com.spa.model.enums.AppointmentStatus;
import com.spa.service.Validation;
import java.time.LocalDate;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Menu quản lý kỹ thuật viên.
 */
public class TechnicianMenu implements MenuModule {
    private final MenuContext context;

    public TechnicianMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ KỸ THUẬT VIÊN ---");
            System.out.println("1. Thêm kỹ thuật viên");
            System.out.println("2. Danh sách kỹ thuật viên");
            System.out.println("3. Tìm kiếm kỹ thuật viên");
            System.out.println("4. Thống kê lịch hẹn đã hoàn thành");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    addTechnician();
                    Validation.pause();
                    break;
                case 2:
                    listTechnicians();
                    Validation.pause();
                    break;
                case 3:
                    searchTechnicians();
                    Validation.pause();
                    break;
                case 4:
                    showCompletedAppointments();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void addTechnician() {
        Technician technician = new Technician();
        technician.setPersonId(context.getEmployeeStore().generateNextId(Technician.class));
        technician.setFullName(Validation.getString("Họ tên: "));
        technician.setPhoneNumber(Validation.getString("Số điện thoại: "));
        technician.setEmail(Validation.getString("Email: "));
        technician.setAddress(Validation.getString("Địa chỉ: "));
        technician.setMale(Validation.getBoolean("Giới tính nam?"));
        technician.setBirthDate(Validation.getDate("Ngày sinh (dd/MM/yyyy): ", DATE_FORMAT));
        technician.setHireDate(LocalDate.now());
        technician.setSalary(Validation.getDouble("Lương cơ bản: ", 0.0, 1_000_000_000.0));
        technician.setSkill(Validation.getString("Kỹ năng chính: "));
        technician.setCertifications(Validation.getOptionalString("Chứng chỉ: "));
        technician.setCommissionRate(Validation.getDouble("Hoa hồng (0-1): ", 0.0, 1.0));
        String password = Validation.getString("Mật khẩu khởi tạo: ");
        technician.setPasswordHash(MenuHelper.hashPassword(password));
        technician.setDeleted(false);

        context.getEmployeeStore().add(technician);
        context.getEmployeeStore().writeFile();
        System.out.println("Đã thêm kỹ thuật viên mới.");
    }

    private void listTechnicians() {
        System.out.println();
        System.out.println("--- DANH SÁCH KỸ THUẬT VIÊN ---");
        Technician[] technicians = context.getEmployeeStore().getAllTechnicians();
        boolean hasData = false;
        String header = technicianHeader();
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        for (Technician technician : technicians) {
            if (technician == null || technician.isDeleted()) {
                continue;
            }
            printTechnicianRow(technician);
            hasData = true;
        }
        if (!hasData) {
            System.out.println("Chưa có kỹ thuật viên nào.");
        }
    }

    private void searchTechnicians() {
        String keyword = Validation.getString("Từ khóa tìm kiếm: ").toLowerCase();
        Technician[] technicians = context.getEmployeeStore().getAllTechnicians();
        boolean found = false;
        boolean headerPrinted = false;
        for (Technician technician : technicians) {
            if (technician == null || technician.isDeleted()) {
                continue;
            }
            String data = (nullToEmpty(technician.getFullName()) + " "
                    + nullToEmpty(technician.getSkill()) + " "
                    + nullToEmpty(technician.getCertifications())).toLowerCase();
            if (!data.contains(keyword)) {
                continue;
            }
            if (!headerPrinted) {
                String header = technicianHeader();
                System.out.println(header);
                System.out.println("-".repeat(header.length()));
                headerPrinted = true;
            }
            printTechnicianRow(technician);
            found = true;
        }
        if (!found) {
            System.out.println("Không tìm thấy kỹ thuật viên phù hợp.");
        }
    }

    private void showCompletedAppointments() {
        Technician[] technicians = context.getEmployeeStore().getAllTechnicians();
        if (technicians.length == 0) {
            System.out.println("Chưa có kỹ thuật viên nào.");
            return;
        }
        for (Technician technician : technicians) {
            if (technician == null || technician.isDeleted()) {
                continue;
            }
            Appointment[] appointments = context.getAppointmentStore().getAll();
            int completed = 0;
            for (Appointment appointment : appointments) {
                if (appointment == null) {
                    continue;
                }
                if (appointment.getTechnician() != null
                        && technician.getId().equals(appointment.getTechnician().getId())
                        && appointment.getStatus() == AppointmentStatus.COMPLETED) {
                    completed++;
                }
            }
            System.out.printf("%s - %s | Lịch hẹn hoàn thành: %d%n",
                    technician.getId(), technician.getFullName(), completed);
        }
    }

    private String technicianHeader() {
        return String.format("%-8s | %-18s | %-12s | %-12s | %-10s | %-6s | %-12s | %-12s | %-6s",
                "MÃ", "HỌ TÊN", "SĐT", "EMAIL", "KỸ NĂNG", "KHÓA", "CHỨNG CHỈ", "NGÀY VÀO", "HOA HỒNG");
    }

    private void printTechnicianRow(Technician technician) {
        System.out.printf("%-8s | %-18s | %-12s | %-12s | %-10s | %-6s | %-12s | %-12s | %-6.2f%n",
                nullToEmpty(technician.getId()),
                nullToEmpty(technician.getFullName()),
                nullToEmpty(technician.getPhoneNumber()),
                nullToEmpty(limitLength(technician.getEmail(), 12)),
                nullToEmpty(limitLength(technician.getSkill(), 10)),
                technician.isDeleted() ? "Có" : "Không",
                nullToEmpty(limitLength(technician.getCertifications(), 12)),
                technician.getHireDate() == null ? "" : technician.getHireDate().toString(),
                technician.getCommissionRate());
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
