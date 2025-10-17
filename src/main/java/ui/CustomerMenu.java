package ui;

import java.time.LocalDate;

import collections.CustomerManager;
import exceptions.BusinessLogicException;
import exceptions.EntityNotFoundException;
import exceptions.ValidationException;
import io.InputHandler;
import io.OutputFormatter;
import models.Customer;
import services.CustomerService;

/**
 * Menu quản lý khách hàng.
 * Cung cấp các chức năng: thêm, xem, tìm kiếm, cập nhật, vô hiệu hóa khách
 * hàng.
 */
public class CustomerMenu extends MenuBase {

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    private CustomerService customerService;
    private CustomerManager customerManager;

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo CustomerMenu.
     *
     * @param inputHandler    Bộ xử lý input
     * @param outputFormatter Bộ định dạng output
     * @param customerService Dịch vụ quản lý khách hàng
     * @param customerManager Manager khách hàng
     */
    public CustomerMenu(InputHandler inputHandler, OutputFormatter outputFormatter,
            CustomerService customerService, CustomerManager customerManager) {
        super(inputHandler, outputFormatter);
        this.customerService = customerService;
        this.customerManager = customerManager;
    }

    // ============ PHƯƠNG THỨC OVERRIDE (OVERRIDE METHODS) ============

    @Override
    protected void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   QUẢN LÝ KHÁCH HÀNG                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Thêm khách hàng mới");
        System.out.println("2. Xem tất cả khách hàng");
        System.out.println("3. Tìm kiếm khách hàng");
        System.out.println("4. Cập nhật thông tin khách hàng");
        System.out.println("5. Vô hiệu hóa khách hàng");
        System.out.println("6. Kích hoạt lại khách hàng");
        System.out.println("7. Xem khách hàng theo Tier");
        System.out.println("8. Quay lại");
        System.out.println();
    }

    @Override
    protected boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                addNewCustomer();
                return true;

            case 2:
                viewAllCustomers();
                return true;

            case 3:
                searchCustomer();
                return true;

            case 4:
                updateCustomerInfo();
                return true;

            case 5:
                deactivateCustomer();
                return true;

            case 6:
                reactivateCustomer();
                return true;

            case 7:
                viewCustomerByTier();
                return true;

            case 8:
                // Quay lại menu chính
                return false;

            default:
                System.out.println("❌ Lựa chọn không hợp lệ! Vui lòng chọn lại.");
                return true;
        }
    }

    // ============ PHƯƠNG THỨC CHỨC NĂNG (FUNCTIONAL METHODS) ============

    /**
     * Thêm khách hàng mới.
     */
    private void addNewCustomer() {
        System.out.println("\n--- Thêm Khách Hàng Mới ---");

        try {
            String fullName = inputHandler.readString("Nhập họ tên: ");
            String phone = inputHandler.readPhoneNumber("Nhập số điện thoại: ");
            String email = inputHandler.readEmail("Nhập email: ");
            String address = inputHandler.readString("Nhập địa chỉ: ");
            boolean isMale = inputHandler.readBoolean("Là nam giới? (true/false): ");
            LocalDate birthDate = inputHandler.readLocalDate("Nhập ngày sinh (dd/MM/yyyy): ");

            Customer customer = customerService.registerNewCustomer(fullName, phone, email, address, isMale, birthDate);

            System.out.println("\n✓ Khách hàng đã được thêm thành công!");
            System.out.println("  ID: " + customer.getId());
            System.out.println("  Tên: " + customer.getFullName());
            System.out.println("  Phone: " + customer.getPhoneNumber());

            pauseForContinue();

        } catch (ValidationException e) {
            System.out.println("❌ Lỗi nhập liệu: " + e.getFormattedError());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getFormattedError());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xem tất cả khách hàng.
     */
    private void viewAllCustomers() {
        System.out.println("\n--- Danh Sách Khách Hàng ---");

        try {
            Customer[] customers = customerManager.getAll();

            if (customers == null || customers.length == 0) {
                System.out.println("ℹ Không có khách hàng nào.");
            } else {
                System.out.println();
                for (Customer c : customers) {
                    if (c != null) {
                        System.out.println("  • " + c.getId() + " - " + c.getFullName() +
                                " (" + c.getPhoneNumber() + ") - " + c.getMemberTier());
                    }
                }
            }

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Tìm kiếm khách hàng theo ID.
     */
    private void searchCustomer() {
        System.out.println("\n--- Tìm Kiếm Khách Hàng ---");

        try {
            String customerId = inputHandler.readString("Nhập ID khách hàng: ");
            Customer customer = customerManager.getById(customerId);

            System.out.println("\n✓ Tìm thấy khách hàng:");
            customer.display();

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy khách hàng: " + e.getFormattedError());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi tìm kiếm: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Cập nhật thông tin khách hàng.
     */
    private void updateCustomerInfo() {
        System.out.println("\n--- Cập Nhật Thông Tin Khách Hàng ---");

        try {
            String customerId = inputHandler.readString("Nhập ID khách hàng: ");

            // Lấy khách hàng hiện tại
            Customer customer = customerManager.getById(customerId);
            System.out.println("\n✓ Khách hàng hiện tại: " + customer.getFullName());

            // Nhập thông tin mới
            String newName = inputHandler.readString("Nhập họ tên mới (hoặc Enter để bỏ qua): ");
            String newPhone = inputHandler.readPhoneNumber("Nhập số điện thoại mới (hoặc Enter để bỏ qua): ");
            String newEmail = inputHandler.readEmail("Nhập email mới (hoặc Enter để bỏ qua): ");
            String newAddress = inputHandler.readString("Nhập địa chỉ mới (hoặc Enter để bỏ qua): ");

            // Cập nhật thông tin
            customerService.updateCustomerInfo(customerId, newName, newPhone, newEmail, newAddress);

            System.out.println("\n✓ Cập nhật thông tin khách hàng thành công!");
            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy khách hàng: " + e.getFormattedError());
            pauseForContinue();
        } catch (ValidationException e) {
            System.out.println("❌ Lỗi nhập liệu: " + e.getFormattedError());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getFormattedError());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Vô hiệu hóa khách hàng.
     */
    private void deactivateCustomer() {
        System.out.println("\n--- Vô Hiệu Hóa Khách Hàng ---");

        try {
            String customerId = inputHandler.readString("Nhập ID khách hàng cần vô hiệu hóa: ");

            boolean success = customerService.deactivateCustomer(customerId);
            if (success) {
                System.out.println("✓ Khách hàng đã được vô hiệu hóa!");
            } else {
                System.out.println("❌ Không thể vô hiệu hóa khách hàng!");
            }

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy khách hàng: " + e.getMessage());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Kích hoạt lại khách hàng.
     */
    private void reactivateCustomer() {
        System.out.println("\n--- Kích Hoạt Lại Khách Hàng ---");

        try {
            String customerId = inputHandler.readString("Nhập ID khách hàng cần kích hoạt lại: ");

            boolean success = customerService.reactivateCustomer(customerId);
            if (success) {
                System.out.println("✓ Khách hàng đã được kích hoạt lại!");
            } else {
                System.out.println("❌ Không thể kích hoạt lại khách hàng!");
            }

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy khách hàng: " + e.getMessage());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xem khách hàng theo Tier.
     */
    private void viewCustomerByTier() {
        System.out.println("\n--- Xem Khách Hàng Theo Tier ---");
        System.out.println("1. PLATINUM");
        System.out.println("2. GOLD");
        System.out.println("3. SILVER");
        System.out.println("4. BRONZE");

        try {
            int tierChoice = inputHandler.readInt("Chọn Tier: ");

            String tier = switch (tierChoice) {
                case 1 -> "PLATINUM";
                case 2 -> "GOLD";
                case 3 -> "SILVER";
                case 4 -> "BRONZE";
                default -> null;
            };

            if (tier == null) {
                System.out.println("❌ Lựa chọn Tier không hợp lệ!");
                pauseForContinue();
                return;
            }

            Customer[] customers = customerManager.getAll();
            Customer[] filteredCustomers = filterCustomersByTier(customers, tier);

            if (filteredCustomers.length == 0) {
                System.out.println("ℹ Không có khách hàng Tier " + tier);
            } else {
                System.out.println("\n--- Khách Hàng Tier " + tier + " ---");
                for (Customer c : filteredCustomers) {
                    if (c != null) {
                        System.out.println("  • " + c.getId() + " - " + c.getFullName() +
                                " (" + c.getPhoneNumber() + ")");
                    }
                }
            }

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Lọc khách hàng theo Tier.
     *
     * @param customers Mảng khách hàng
     * @param tier      Tier cần lọc
     * @return Mảng khách hàng có tier tương ứng
     */
    private Customer[] filterCustomersByTier(Customer[] customers, String tier) {
        if (customers == null) {
            return new Customer[0];
        }

        // Đếm số khách hàng có tier tương ứng
        int count = 0;
        for (Customer customer : customers) {
            if (customer != null && customer.getMemberTier().toString().equals(tier)) {
                count++;
            }
        }

        // Tạo mảng mới chứa các khách hàng
        Customer[] result = new Customer[count];
        int index = 0;
        for (Customer customer : customers) {
            if (customer != null && customer.getMemberTier().toString().equals(tier)) {
                result[index++] = customer;
            }
        }

        return result;
    }
}
