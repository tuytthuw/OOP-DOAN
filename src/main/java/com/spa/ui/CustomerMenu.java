package com.spa.ui;

import com.spa.model.Customer;
import com.spa.model.enums.Tier;
import com.spa.service.Validation;
import java.time.LocalDate;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Xử lý menu khách hàng.
 */
public class CustomerMenu implements MenuModule {
    private final MenuContext context;

    public CustomerMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ KHÁCH HÀNG ---");
            System.out.println("1. Xem danh sách khách hàng");
            System.out.println("2. Thêm khách hàng mới");
            System.out.println("3. Cập nhật thông tin khách hàng");
            System.out.println("4. Xóa khách hàng");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listCustomers();
                    Validation.pause();
                    break;
                case 2:
                    addCustomer();
                    Validation.pause();
                    break;
                case 3:
                    updateCustomer();
                    Validation.pause();
                    break;
                case 4:
                    deleteCustomer();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listCustomers() {
        System.out.println();
        System.out.println("--- DANH SÁCH KHÁCH HÀNG ---");
        Customer[] customers = context.getCustomerStore().getAll();
        if (customers.length == 0) {
            System.out.println("Chưa có khách hàng nào.");
            return;
        }
        for (Customer customer : customers) {
            if (customer == null || customer.isDeleted()) {
                continue;
            }
            System.out.printf("%s | %s | %s | %s | %s%n",
                    customer.getId(),
                    customer.getFullName(),
                    customer.getMemberTier(),
                    customer.getPhoneNumber(),
                    customer.getEmail());
        }
    }

    private void addCustomer() {
        System.out.println();
        System.out.println("--- THÊM KHÁCH HÀNG ---");
        String id = context.getCustomerStore().generateNextId();
        System.out.println("Mã khách hàng được cấp: " + id);
        String fullName = Validation.getString("Họ tên: ");
        String phone = Validation.getString("Số điện thoại: ");
        String email = Validation.getString("Email: ");
        String address = Validation.getString("Địa chỉ: ");
        boolean male = Validation.getBoolean("Giới tính nam?");
        LocalDate birthDate = Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT);
        String notes = Validation.getString("Ghi chú: ");
        int points = Validation.getInt("Điểm tích lũy ban đầu: ", 0, 1_000_000);
        LocalDate lastVisitDate = Validation.getDate("Lần ghé gần nhất (yyyy-MM-dd): ", DATE_FORMAT);
        Tier tier = MenuHelper.selectTier();

        Customer customer = new Customer(id, fullName, phone, male, birthDate, email, address,
                false, tier, notes, points, lastVisitDate);
        context.getCustomerStore().add(customer);
        context.getCustomerStore().writeFile();
        System.out.println("Đã thêm khách hàng thành công.");
    }

    private void updateCustomer() {
        System.out.println();
        System.out.println("--- CẬP NHẬT KHÁCH HÀNG ---");
        String id = Validation.getString("Mã khách hàng: ");
        Customer customer = context.getCustomerStore().findById(id);
        if (customer == null || customer.isDeleted()) {
            System.out.println("Không tìm thấy khách hàng.");
            return;
        }
        String fullName = Validation.getString("Họ tên mới: ");
        String phone = Validation.getString("Số điện thoại mới: ");
        String email = Validation.getString("Email mới: ");
        String address = Validation.getString("Địa chỉ mới: ");
        boolean male = Validation.getBoolean("Giới tính nam?");
        LocalDate birthDate = Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT);
        String notes = Validation.getString("Ghi chú: ");
        int points = Validation.getInt("Điểm tích lũy: ", 0, 1_000_000);
        LocalDate lastVisit = Validation.getDate("Lần ghé gần nhất (yyyy-MM-dd): ", DATE_FORMAT);
        Tier tier = MenuHelper.selectTier();

        customer.setFullName(fullName);
        customer.setPhoneNumber(phone);
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setMale(male);
        customer.setBirthDate(birthDate);
        customer.setNotes(notes);
        customer.setPoints(points);
        customer.setLastVisitDate(lastVisit);
        customer.setMemberTier(tier);
        customer.upgradeTier();
        context.getCustomerStore().writeFile();
        System.out.println("Đã cập nhật khách hàng.");
    }

    private void deleteCustomer() {
        System.out.println();
        System.out.println("--- XÓA KHÁCH HÀNG ---");
        String id = Validation.getString("Mã khách hàng: ");
        if (context.getCustomerStore().delete(id)) {
            context.getCustomerStore().writeFile();
            System.out.println("Đã xóa khách hàng.");
        } else {
            System.out.println("Không tìm thấy khách hàng.");
        }
    }
}
