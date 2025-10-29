package com.spa.ui;

import com.spa.data.AppointmentStore;
import com.spa.data.CustomerStore;
import com.spa.data.EmployeeStore;
import com.spa.data.GoodsReceiptStore;
import com.spa.data.InvoiceStore;
import com.spa.data.PaymentStore;
import com.spa.data.ProductStore;
import com.spa.data.PromotionStore;
import com.spa.data.ServiceStore;
import com.spa.data.SupplierStore;
import com.spa.model.Appointment;
import com.spa.model.Customer;
import com.spa.model.Employee;
import com.spa.model.Invoice;
import com.spa.model.Payment;
import com.spa.model.Product;
import com.spa.model.Promotion;
import com.spa.model.Service;
import com.spa.model.Receptionist;
import com.spa.model.Technician;
import com.spa.model.Supplier;
import com.spa.model.enums.DiscountType;
import com.spa.model.enums.PaymentMethod;
import com.spa.model.enums.ServiceCategory;
import com.spa.model.enums.Tier;
import com.spa.service.AuthService;
import com.spa.service.Validation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Giao diện console chính của ứng dụng.
 */
public class MenuUI {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final AuthService authService;
    private final EmployeeStore employeeStore;
    private final CustomerStore customerStore;
    private final ServiceStore serviceStore;
    private final ProductStore productStore;
    private final AppointmentStore appointmentStore;
    private final InvoiceStore invoiceStore;
    private final PromotionStore promotionStore;
    private final SupplierStore supplierStore;
    private final PaymentStore paymentStore;
    private final GoodsReceiptStore goodsReceiptStore;
    private final MenuContext context;
    private final CustomerMenu customerMenu;
    private final ServiceMenu serviceMenu;
    private final ProductMenu productMenu;
    private final SupplierMenu supplierMenu;
    private final PromotionMenu promotionMenu;
    private final PaymentMenu paymentMenu;
    private final AppointmentMenu appointmentMenu;
    private final InvoiceMenu invoiceMenu;
    private final GoodsReceiptMenu goodsReceiptMenu;

    public MenuUI(AuthService authService,
                  EmployeeStore employeeStore,
                  CustomerStore customerStore,
                  ServiceStore serviceStore,
                  ProductStore productStore,
                  AppointmentStore appointmentStore,
                  InvoiceStore invoiceStore,
                  PromotionStore promotionStore,
                  SupplierStore supplierStore,
                  PaymentStore paymentStore,
                  GoodsReceiptStore goodsReceiptStore) {
        this.authService = authService;
        this.employeeStore = employeeStore;
        this.customerStore = customerStore;
        this.serviceStore = serviceStore;
        this.productStore = productStore;
        this.appointmentStore = appointmentStore;
        this.invoiceStore = invoiceStore;
        this.promotionStore = promotionStore;
        this.supplierStore = supplierStore;
        this.paymentStore = paymentStore;
        this.goodsReceiptStore = goodsReceiptStore;
        context = new MenuContext(authService, employeeStore, customerStore, serviceStore,
                productStore, appointmentStore, invoiceStore, promotionStore, supplierStore, paymentStore, goodsReceiptStore);
        customerMenu = new CustomerMenu(context);
        serviceMenu = new ServiceMenu(context);
        productMenu = new ProductMenu(context);
        supplierMenu = new SupplierMenu(context);
        promotionMenu = new PromotionMenu(context);
        paymentMenu = new PaymentMenu(context);
        appointmentMenu = new AppointmentMenu(context);
        invoiceMenu = new InvoiceMenu(context);
        goodsReceiptMenu = new GoodsReceiptMenu(context);
    }

    private void handleEmployeeMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ NHÂN SỰ ---");
            System.out.println("1. Xem danh sách nhân viên");
            System.out.println("2. Thêm kỹ thuật viên");
            System.out.println("3. Thêm lễ tân");
            System.out.println("4. Khóa/Mở khóa nhân viên");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listEmployees();
                    Validation.pause();
                    break;
                case 2:
                    addTechnician();
                    Validation.pause();
                    break;
                case 3:
                    addReceptionist();
                    Validation.pause();
                    break;
                case 4:
                    toggleEmployeeLock();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listEmployees() {
        System.out.println();
        System.out.println("--- DANH SÁCH NHÂN VIÊN ---");
        Employee[] employees = employeeStore.getAll();
        boolean hasData = false;
        for (Employee employee : employees) {
            if (employee == null) {
                continue;
            }
            hasData = true;
            System.out.printf("%s | %s | Vai trò: %s | Trạng thái: %s%n",
                    employee.getId(),
                    employee.getFullName(),
                    employee.getRole(),
                    employee.isDeleted() ? "Đã khóa" : "Hoạt động");
        }
        if (!hasData) {
            System.out.println("Chưa có nhân viên nào.");
        }
    }

    private void addTechnician() {
        System.out.println();
        System.out.println("--- THÊM KỸ THUẬT VIÊN ---");
        Technician technician = buildTechnician();
        if (technician == null) {
            return;
        }
        employeeStore.add(technician);
        employeeStore.writeFile();
        System.out.println("Đã thêm kỹ thuật viên thành công.");
    }

    private Technician buildTechnician() {
        Technician technician = new Technician();
        technician.setPersonId(employeeStore.generateNextId(Technician.class));
        technician.setFullName(Validation.getString("Họ tên: "));
        technician.setPhoneNumber(Validation.getString("Số điện thoại: "));
        technician.setEmail(Validation.getString("Email: "));
        technician.setAddress(Validation.getString("Địa chỉ: "));
        technician.setMale(Validation.getBoolean("Giới tính nam?"));
        technician.setBirthDate(Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT));
        technician.setHireDate(Validation.getDate("Ngày vào làm (yyyy-MM-dd): ", DATE_FORMAT));
        technician.setSalary(Validation.getDouble("Lương cơ bản: ", 0.0, 1_000_000_000.0));
        technician.setSkill(Validation.getString("Kỹ năng chính: "));
        technician.setCertifications(Validation.getString("Chứng chỉ: "));
        technician.setCommissionRate(Validation.getDouble("Hoa hồng (0-1): ", 0.0, 1.0));
        String password = Validation.getString("Mật khẩu khởi tạo: ");
        technician.setPasswordHash(encryptPassword(password));
        technician.setDeleted(false);
        return technician;
    }

    private void addReceptionist() {
        System.out.println();
        System.out.println("--- THÊM LỄ TÂN ---");
        Receptionist receptionist = buildReceptionist();
        if (receptionist == null) {
            return;
        }
        employeeStore.add(receptionist);
        employeeStore.writeFile();
        System.out.println("Đã thêm lễ tân thành công.");
    }

    private Receptionist buildReceptionist() {
        Receptionist receptionist = new Receptionist();
        receptionist.setPersonId(employeeStore.generateNextId(Receptionist.class));
        receptionist.setFullName(Validation.getString("Họ tên: "));
        receptionist.setPhoneNumber(Validation.getString("Số điện thoại: "));
        receptionist.setEmail(Validation.getString("Email: "));
        receptionist.setAddress(Validation.getString("Địa chỉ: "));
        receptionist.setMale(Validation.getBoolean("Giới tính nam?"));
        receptionist.setBirthDate(Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT));
        receptionist.setHireDate(Validation.getDate("Ngày vào làm (yyyy-MM-dd): ", DATE_FORMAT));
        receptionist.setSalary(Validation.getDouble("Lương cơ bản: ", 0.0, 1_000_000_000.0));
        receptionist.setMonthlyBonus(Validation.getDouble("Thưởng hàng tháng: ", 0.0, 1_000_000_000.0));
        String password = Validation.getString("Mật khẩu khởi tạo: ");
        receptionist.setPasswordHash(encryptPassword(password));
        receptionist.setDeleted(false);
        return receptionist;
    }

    private void toggleEmployeeLock() {
        String id = Validation.getString("Mã nhân viên: ");
        Employee employee = employeeStore.findById(id);
        if (employee == null) {
            System.out.println("Không tìm thấy nhân viên.");
            return;
        }
        employee.setDeleted(!employee.isDeleted());
        employeeStore.writeFile();
        System.out.println(employee.isDeleted() ? "Đã khóa nhân viên." : "Đã mở khóa nhân viên.");
    }

    /**
     * Khởi chạy vòng lặp hiển thị menu.
     */
    public void run() {
        ensureLoggedIn();
        boolean running = true;
        while (running) {
            printHeader();
            boolean canManageCore = canManageCoreData();
            boolean canManageEmployeesMenu = canManageEmployees();
            System.out.println("1. Quản lý khách hàng" + (canManageCore ? "" : " (không khả dụng)"));
            System.out.println("2. Quản lý dịch vụ" + (canManageCore ? "" : " (không khả dụng)"));
            System.out.println("3. Quản lý sản phẩm" + (canManageCore ? "" : " (không khả dụng)"));
            System.out.println("4. Quản lý lịch hẹn");
            System.out.println("5. Quản lý hóa đơn");
            System.out.println("6. Quản lý khuyến mãi" + (canManageCore ? "" : " (không khả dụng)"));
            System.out.println("7. Quản lý nhà cung cấp" + (canManageCore ? "" : " (không khả dụng)"));
            System.out.println("8. Quản lý thanh toán" + (canManageCore ? "" : " (không khả dụng)"));
            System.out.println("9. Quản lý phiếu nhập kho" + (canManageCore ? "" : " (không khả dụng)"));
            System.out.println("10. Tài khoản");
            if (canManageEmployeesMenu) {
                System.out.println("11. Quản lý nhân sự");
            } else {
                System.out.println("11. Quản lý nhân sự (không khả dụng)");
            }
            System.out.println("0. Thoát");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 11);
            switch (choice) {
                case 1:
                    if (canManageCore) {
                        customerMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 2:
                    if (canManageCore) {
                        serviceMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 3:
                    if (canManageCore) {
                        productMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 4:
                    appointmentMenu.show();
                    break;
                case 5:
                    invoiceMenu.show();
                    break;
                case 6:
                    if (canManageCore) {
                        promotionMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 7:
                    if (canManageCore) {
                        supplierMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 8:
                    if (canManageCore) {
                        paymentMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 9:
                    if (canManageCore) {
                        goodsReceiptMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 10:
                    handleAccountMenu();
                    break;
                case 11:
                    if (canManageEmployeesMenu) {
                        handleEmployeeMenu();
                    } else {
                        noPermission();
                    }
                    break;
                case 0:
                default:
                    running = false;
                    break;
            }
        }
        System.out.println("Cảm ơn bạn đã sử dụng hệ thống Spa Management!");
    }

    private void printHeader() {
        System.out.println();
        System.out.println("================== SPA MANAGEMENT ==================");
        if (authService.isLoggedIn()) {
            System.out.println("Người dùng: " + authService.getCurrentUser().getFullName());
            System.out.println("Vai trò : " + authService.getCurrentRole());
        } else {
            System.out.println("Người dùng: <chưa đăng nhập>");
        }
        System.out.println("====================================================");
    }

    private void ensureLoggedIn() {
        while (!authService.isLoggedIn()) {
            System.out.println();
            System.out.println("--- ĐĂNG NHẬP HỆ THỐNG ---");
            String id = Validation.getString("Mã nhân viên: ");
            String password = Validation.getString("Mật khẩu: ");
            if (authService.login(id, password)) {
                System.out.println("Đăng nhập thành công! Chào mừng " + authService.getCurrentUser().getFullName());
            } else {
                System.out.println("Sai thông tin đăng nhập hoặc tài khoản đã bị khóa.");
            }
        }
    }

    private void handleAccountMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- TÀI KHOẢN ---");
            System.out.println("1. Đổi mật khẩu");
            System.out.println("2. Đăng xuất");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    changePassword();
                    Validation.pause();
                    break;
                case 2:
                    authService.logout();
                    System.out.println("Bạn đã đăng xuất thành công.");
                    Validation.pause();
                    ensureLoggedIn();
                    back = true;
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void changePassword() {
        System.out.println();
        System.out.println("--- ĐỔI MẬT KHẨU ---");
        String oldPassword = Validation.getString("Mật khẩu hiện tại: ");
        String newPassword = Validation.getString("Mật khẩu mới: ");
        if (authService.changePassword(oldPassword, newPassword)) {
            System.out.println("Đổi mật khẩu thành công.");
        } else {
            System.out.println("Đổi mật khẩu thất bại. Vui lòng kiểm tra lại.");
        }
    }

    private void noPermission() {
        System.out.println("Bạn không có quyền truy cập chức năng này.");
        Validation.pause();
    }

    private boolean canManageCoreData() {
        String role = authService.getCurrentRole();
        return "RECEPTIONIST".equalsIgnoreCase(role)
                || "MANAGER".equalsIgnoreCase(role)
                || "ADMIN".equalsIgnoreCase(role);
    }

    private boolean canManageEmployees() {
        String role = authService.getCurrentRole();
        return "MANAGER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
    }

    private String encryptPassword(String raw) {
        if (raw == null) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte value : hash) {
                String hex = Integer.toHexString(0xff & value);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            return raw;
        }
    }
}
