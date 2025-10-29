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
        receptionist.setBirthDate(Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT));
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
        if (receptionists.length == 0) {
            System.out.println("Chưa có lễ tân nào.");
            return;
        }
        for (Receptionist receptionist : receptionists) {
            if (receptionist != null && !receptionist.isDeleted()) {
                receptionist.display();
            }
        }
    }

    private void searchReceptionists() {
        String keyword = Validation.getString("Từ khóa tìm kiếm: ").toLowerCase();
        String[] tokens = keyword.split("\\s+");
        Receptionist[] receptionists = context.getEmployeeStore().getAllReceptionists();
        boolean found = false;
        for (Receptionist receptionist : receptionists) {
            if (receptionist == null || receptionist.isDeleted()) {
                continue;
            }
            String data = (receptionist.getFullName() + " "
                    + receptionist.getPhoneNumber() + " "
                    + receptionist.getEmail()).toLowerCase();
            if (matchesAllTokens(data, tokens)) {
                found = true;
                receptionist.display();
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
}
