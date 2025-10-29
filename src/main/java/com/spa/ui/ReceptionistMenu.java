package com.spa.ui;

import com.spa.model.Receptionist;
import com.spa.service.Validation;
import java.time.LocalDate;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Menu quản lý lễ tân.
 */
public class ReceptionistMenu implements MenuModule {
    private final MenuContext context;

    public ReceptionistMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ LỄ TÂN ---");
            System.out.println("1. Thêm lễ tân");
            System.out.println("2. Danh sách lễ tân");
            System.out.println("3. Tìm kiếm lễ tân");
            System.out.println("4. Khóa/Mở khóa lễ tân");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    addReceptionist();
                    Validation.pause();
                    break;
                case 2:
                    listReceptionists();
                    Validation.pause();
                    break;
                case 3:
                    searchReceptionists();
                    Validation.pause();
                    break;
                case 4:
                    toggleReceptionistStatus();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void addReceptionist() {
        Receptionist receptionist = new Receptionist();
        receptionist.setPersonId(context.getEmployeeStore().generateNextId(Receptionist.class));
        receptionist.setFullName(Validation.getString("Họ tên: "));
        receptionist.setPhoneNumber(Validation.getString("Số điện thoại: "));
        receptionist.setEmail(Validation.getString("Email: "));
        receptionist.setAddress(Validation.getString("Địa chỉ: "));
        receptionist.setMale(Validation.getBoolean("Giới tính nam?"));
        receptionist.setBirthDate(Validation.getDate("Ngày sinh (dd/MM/yyyy): ", DATE_FORMAT));
        receptionist.setHireDate(LocalDate.now());
        receptionist.setSalary(Validation.getDouble("Lương cơ bản: ", 0.0, 1_000_000_000.0));
        receptionist.setMonthlyBonus(Validation.getDouble("Thưởng hàng tháng: ", 0.0, 1_000_000_000.0));
        String password = Validation.getString("Mật khẩu khởi tạo: ");
        receptionist.setPasswordHash(MenuHelper.hashPassword(password));
        receptionist.setDeleted(false);

        context.getEmployeeStore().add(receptionist);
        context.getEmployeeStore().writeFile();
        System.out.println("Đã thêm lễ tân mới.");
    }

    private void listReceptionists() {
        Receptionist[] receptionists = context.getEmployeeStore().getAllReceptionists();
        System.out.println();
        System.out.println("--- DANH SÁCH LỄ TÂN ---");
        boolean hasData = false;
        String header = receptionistHeader();
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        for (Receptionist receptionist : receptionists) {
            if (receptionist == null || receptionist.isDeleted()) {
                continue;
            }
            printReceptionistRow(receptionist);
            hasData = true;
        }
        if (!hasData) {
            System.out.println("Chưa có lễ tân nào.");
        }
    }

    private void searchReceptionists() {
        String keyword = Validation.getString("Từ khóa tìm kiếm: ").toLowerCase();
        String[] tokens = keyword.split("\\s+");
        Receptionist[] receptionists = context.getEmployeeStore().getAllReceptionists();
        boolean found = false;
        boolean headerPrinted = false;
        for (Receptionist receptionist : receptionists) {
            if (receptionist == null || receptionist.isDeleted()) {
                continue;
            }
            String data = (receptionist.getFullName() + " "
                    + receptionist.getPhoneNumber() + " "
                    + receptionist.getEmail()).toLowerCase();
            if (matchesAllTokens(data, tokens)) {
                if (!headerPrinted) {
                    String header = receptionistHeader();
                    System.out.println(header);
                    System.out.println("-".repeat(header.length()));
                    headerPrinted = true;
                }
                found = true;
                printReceptionistRow(receptionist);
            }
        }
        if (!found) {
            System.out.println("Không tìm thấy lễ tân phù hợp.");
        }
    }

    private void toggleReceptionistStatus() {
        String id = Validation.getString("Mã lễ tân: ");
        Receptionist receptionist = context.getEmployeeStore().findReceptionistById(id);
        if (receptionist == null) {
            System.out.println("Không tìm thấy lễ tân.");
            return;
        }
        receptionist.setDeleted(!receptionist.isDeleted());
        context.getEmployeeStore().writeFile();
        System.out.println(receptionist.isDeleted() ? "Đã khóa lễ tân." : "Đã mở khóa lễ tân.");
    }

    private boolean matchesAllTokens(String data, String[] tokens) {
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            if (!data.contains(token)) {
                return false;
            }
        }
        return true;
    }

    private String receptionistHeader() {
        return String.format("%-8s | %-18s | %-12s | %-18s | %-18s | %-6s | %-10s | %-10s",
                "MÃ", "HỌ TÊN", "SĐT", "EMAIL", "ĐỊA CHỈ", "KHÓA", "THƯỞNG", "NGÀY VÀO");
    }

    private void printReceptionistRow(Receptionist receptionist) {
        System.out.printf("%-8s | %-18s | %-12s | %-18s | %-18s | %-6s | %-10.2f | %-10s%n",
                nullToEmpty(receptionist.getId()),
                nullToEmpty(receptionist.getFullName()),
                nullToEmpty(receptionist.getPhoneNumber()),
                nullToEmpty(limitLength(receptionist.getEmail(), 18)),
                nullToEmpty(limitLength(receptionist.getAddress(), 18)),
                receptionist.isDeleted() ? "Có" : "Không",
                receptionist.getMonthlyBonus(),
                receptionist.getHireDate() == null ? "" : receptionist.getHireDate().toString());
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
