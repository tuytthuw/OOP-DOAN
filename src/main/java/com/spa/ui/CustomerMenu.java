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
            System.out.println("(Chọn 0 để quay lại menu trước)");
            System.out.println("1. Thêm khách hàng");
            System.out.println("2. Thêm nhiều khách hàng");
            System.out.println("3. Xuất danh sách");
            System.out.println("4. Cập nhật khách hàng");
            System.out.println("5. Xóa khách hàng");
            System.out.println("6. Tìm kiếm khách hàng");
            System.out.println("7. Thống kê khách hàng");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 7);
            switch (choice) {
                case 1:
                    addCustomer();
                    Validation.pause();
                    break;
                case 2:
                    addMultipleCustomers();
                    Validation.pause();
                    break;
                case 3:
                    listCustomers();
                    Validation.pause();
                    break;
                case 4:
                    updateCustomer();
                    Validation.pause();
                    break;
                case 5:
                    deleteCustomer();
                    Validation.pause();
                    break;
                case 6:
                    searchCustomers();
                    Validation.pause();
                    break;
                case 7:
                    showCustomerStatistics();
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
        boolean hasData = false;
        String header = String.format("%-10s | %-22s | %-12s | %-25s | %-10s | %-7s | %-12s | %-8s",
                "MÃ", "HỌ TÊN", "SĐT", "EMAIL", "HẠNG", "ĐIỂM", "LẦN GHÉ", "TRẠNG THÁI");
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        for (Customer customer : customers) {
            if (customer == null) {
                continue;
            }
            String status = customer.isDeleted() ? "Đã khóa" : "Hoạt động";
            System.out.printf("%-10s | %-22s | %-12s | %-25s | %-10s | %-7d | %-12s | %-8s%n",
                    nullToEmpty(customer.getId()),
                    nullToEmpty(customer.getFullName()),
                    nullToEmpty(customer.getPhoneNumber()),
                    nullToEmpty(customer.getEmail()),
                    customer.getMemberTier() == null ? "" : customer.getMemberTier().name(),
                    customer.getPoints(),
                    customer.getLastVisitDate() == null ? "" : customer.getLastVisitDate().toString(),
                    status);
            hasData = true;
        }
        if (!hasData) {
            System.out.println("Chưa có khách hàng nào.");
        }
    }

    private void addCustomer() {
        System.out.println();
        System.out.println("--- THÊM KHÁCH HÀNG ---");
        String id = context.getCustomerStore().generateNextId();
        System.out.println("Mã khách hàng được cấp: " + id);
        Customer customer = promptCustomer(id, null);
        if (customer == null) {
            System.out.println("Đã hủy thao tác thêm khách hàng.");
            return;
        }
        context.getCustomerStore().add(customer);
        context.getCustomerStore().writeFile();
        System.out.println("Đã thêm khách hàng thành công.");
        customer.display();
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
        Customer updated = promptCustomer(id, customer);
        if (updated == null) {
            System.out.println("Đã hủy cập nhật khách hàng.");
            return;
        }
        customer.setFullName(updated.getFullName());
        customer.setPhoneNumber(updated.getPhoneNumber());
        customer.setEmail(updated.getEmail());
        customer.setAddress(updated.getAddress());
        customer.setMale(updated.isMale());
        customer.setBirthDate(updated.getBirthDate());
        customer.setNotes(updated.getNotes());
        customer.setPoints(updated.getPoints());
        customer.setLastVisitDate(updated.getLastVisitDate());
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

    private void addMultipleCustomers() {
        int total = Validation.getInt("Số lượng khách hàng cần thêm: ", 1, 1000);
        int added = 0;
        for (int i = 0; i < total; i++) {
            System.out.println("-- Khách hàng thứ " + (i + 1));
            System.out.println("(Để dừng thêm khách hàng, hãy chọn 0 ở menu chính)");
            String id = context.getCustomerStore().generateNextId();
            Customer customer = promptCustomer(id, null);
            if (customer != null) {
                context.getCustomerStore().add(customer);
                added++;
            }
        }
        context.getCustomerStore().writeFile();
        System.out.printf("Đã thêm %d khách hàng mới.%n", added);
    }

    private void searchCustomers() {
        String keywordsLine = Validation.getOptionalString("Nhập từ khóa tìm kiếm (cách nhau bởi dấu cách) hoặc bỏ trống để xem tất cả: ");
        String trimmedKeywords = keywordsLine.trim().toLowerCase();

        Customer[] customers = context.getCustomerStore().getAll();
        boolean foundAny = false;
        for (Customer customer : customers) {
            if (customer == null || customer.isDeleted()) {
                continue;
            }
            if (trimmedKeywords.isEmpty()) {
                System.out.printf("%s | %s | %s | %s%n",
                        customer.getId(),
                        customer.getFullName(),
                        customer.getPhoneNumber(),
                        customer.getEmail());
                foundAny = true;
                continue;
            }
            if (matchesTokens(customer, trimmedKeywords.split("\\s+"))) {
                System.out.printf("%s | %s | %s | %s%n",
                        customer.getId(),
                        customer.getFullName(),
                        customer.getPhoneNumber(),
                        customer.getEmail());
                foundAny = true;
            }
        }
        if (!foundAny) {
            System.out.println("Không có khách hàng phù hợp.");
        }
    }

    private void showCustomerStatistics() {
        Customer[] customers = context.getCustomerStore().getAll();
        if (customers.length == 0) {
            System.out.println("Chưa có dữ liệu khách hàng.");
            return;
        }
        int total = 0;
        int active = 0;
        int deleted = 0;
        int[] tierCounts = new int[Tier.values().length];
        int totalPoints = 0;
        for (Customer customer : customers) {
            if (customer == null) {
                continue;
            }
            total++;
            if (customer.isDeleted()) {
                deleted++;
            } else {
                active++;
            }
            Tier tier = customer.getMemberTier();
            if (tier != null) {
                tierCounts[tier.ordinal()]++;
            }
            totalPoints += customer.getPoints();
        }
        System.out.printf("Tổng khách hàng: %d%n", total);
        System.out.printf("Hoạt động: %d | Đã khóa: %d%n", active, deleted);
        Tier[] tiers = Tier.values();
        for (int i = 0; i < tiers.length; i++) {
            System.out.printf("- %s: %d%n", tiers[i], tierCounts[i]);
        }
        System.out.printf("Tổng điểm tích lũy: %d | Điểm trung bình: %.2f%n",
                totalPoints,
                total == 0 ? 0.0 : (double) totalPoints / total);
    }

    private Customer promptCustomer(String id, Customer base) {
        String fullName = Validation.getString(buildPrompt("Họ tên", base == null ? null : base.getFullName()));
        String phone = Validation.getString(buildPrompt("Số điện thoại", base == null ? null : base.getPhoneNumber()));
        String email = Validation.getString(buildPrompt("Email", base == null ? null : base.getEmail()));
        String address = Validation.getString(buildPrompt("Địa chỉ", base == null ? null : base.getAddress()));
        boolean male = Validation.getBoolean(buildPrompt("Giới tính nam?", base == null ? null : (base.isMale() ? "Y" : "N")));
        LocalDate birthDate = Validation.getDate(buildPrompt("Ngày sinh (dd/MM/yyyy)",
                base == null || base.getBirthDate() == null ? null : base.getBirthDate().format(DATE_FORMAT)), DATE_FORMAT);
        String notes = Validation.getString(buildPrompt("Ghi chú", base == null ? null : base.getNotes()));

        int points;
        LocalDate lastVisit;
        Tier tier;
        if (base == null) {
            points = 0;
            lastVisit = LocalDate.now();
            tier = Tier.STANDARD;
        } else {
            points = Validation.getInt(buildPrompt("Điểm tích lũy", Integer.toString(base.getPoints())), 0, 1_000_000);
            lastVisit = Validation.getDate(buildPrompt("Lần ghé gần nhất (dd/MM/yyyy)",
                    base.getLastVisitDate() == null ? null : base.getLastVisitDate().format(DATE_FORMAT)), DATE_FORMAT);
            System.out.println("Hạng hiện tại: " + base.getMemberTier());
            Tier selectedTier = MenuHelper.selectTier();
            if (selectedTier == null) {
                return null;
            }
            tier = selectedTier;
        }
        // Constructor: (id, fullName, phone, male, birthDate, email, address, deleted, tier, notes, points, lastVisit)
        return new Customer(id, fullName, phone, male, birthDate, email, address, false, tier, notes, points, lastVisit);
    }

    private String buildPrompt(String label, String current) {
        if (current == null || current.isEmpty()) {
            return label + ": ";
        }
        return String.format("%s (hiện tại: %s): ", label, current);
    }

    private boolean matchesTokens(Customer customer, String[] tokens) {
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            return true;
        }
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            String lowerToken = token.toLowerCase();
            boolean tokenMatch = containsIgnoreCase(customer.getId(), lowerToken)
                    || containsIgnoreCase(customer.getFullName(), lowerToken)
                    || containsIgnoreCase(customer.getPhoneNumber(), lowerToken)
                    || containsIgnoreCase(customer.getEmail(), lowerToken)
                    || containsIgnoreCase(customer.getNotes(), lowerToken);
            if (!tokenMatch) {
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

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
