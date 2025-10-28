package ui;

import models.Customer;
import services.CustomerService;

import java.time.LocalDate;

public class CustomerMenu {
    private final CustomerService customerService;
    private final InputHandler inputHandler;
    private final OutputFormatter outputFormatter;

    public CustomerMenu(CustomerService customerService,
                        InputHandler inputHandler,
                        OutputFormatter outputFormatter) {
        this.customerService = customerService;
        this.inputHandler = inputHandler;
        this.outputFormatter = outputFormatter;
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("=== CUSTOMER MENU ===");
            System.out.println("1. Thêm khách hàng");
            System.out.println("2. Tìm theo tier");
            System.out.println("3. Danh sách khách hàng");
            System.out.println("0. Quay lại");
            int choice = inputHandler.readInt("Chọn chức năng", 0, 3);
            switch (choice) {
                case 1:
                    createCustomer();
                    break;
                case 2:
                    listByTier();
                    break;
                case 3:
                    listAllCustomers();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    outputFormatter.printStatus("Lựa chọn không hợp lệ", false);
            }
        }
    }

    private void createCustomer() {
        String name = inputHandler.readString("Tên khách hàng");
        String phone = inputHandler.readString("Điện thoại");
        String email = inputHandler.readString("Email");
        LocalDate birthDate = LocalDate.now();
        Customer customer = new Customer(name, phone, email, birthDate, enums.Tier.BRONZE);
        customerService.register(customer);
        outputFormatter.printStatus("Đã thêm khách hàng", true);
    }

    private void listByTier() {
        String tierInput = inputHandler.readString("Nhập tier (PLATINUM/GOLD/SILVER/BRONZE)");
        enums.Tier tier;
        try {
            tier = enums.Tier.valueOf(tierInput.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            outputFormatter.printStatus("Tier không hợp lệ", false);
            return;
        }
        Customer[] customers = customerService.getByTier(tier);
        String[][] rows = new String[customers.length][3];
        for (int i = 0; i < customers.length; i++) {
            Customer customer = customers[i];
            rows[i][0] = customer.getId();
            rows[i][1] = customer.getFullName();
            rows[i][2] = customer.getTier().name();
        }
        outputFormatter.printTable(new String[]{"ID", "Tên", "Tier"}, rows);
    }

    private void listAllCustomers() {
        Customer[] customers = customerService.getAllCustomers();
        if (customers.length == 0) {
            outputFormatter.printStatus("Chưa có khách hàng", false);
            return;
        }
        String[][] rows = new String[customers.length][4];
        for (int i = 0; i < customers.length; i++) {
            Customer customer = customers[i];
            rows[i][0] = customer.getId();
            rows[i][1] = customer.getFullName();
            rows[i][2] = customer.getPhoneNumber();
            rows[i][3] = customer.getTier().name();
        }
        outputFormatter.printTable(new String[]{"ID", "Tên", "Điện thoại", "Tier"}, rows);
    }
}
