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
import com.spa.model.Employee;
import com.spa.service.AuthService;
import com.spa.service.Validation;
import java.time.format.DateTimeFormatter;

/**
 * Giao diện console chính của ứng dụng.
 */
public class MenuUI {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String MODULE_CUSTOMER = "CUSTOMER";
    private static final String MODULE_SERVICE = "SERVICE";
    private static final String MODULE_PRODUCT = "PRODUCT";
    private static final String MODULE_APPOINTMENT = "APPOINTMENT";
    private static final String MODULE_INVOICE = "INVOICE";
    private static final String MODULE_PROMOTION = "PROMOTION";
    private static final String MODULE_SUPPLIER = "SUPPLIER";
    private static final String MODULE_PAYMENT = "PAYMENT";
    private static final String MODULE_GOODS_RECEIPT = "GOODS_RECEIPT";
    private static final String MODULE_EMPLOYEE = "EMPLOYEE";

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
    private final EmployeeManagementMenu employeeManagementMenu;

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
        employeeManagementMenu = new EmployeeManagementMenu(context);
    }

    /**
     * Khởi chạy vòng lặp hiển thị menu.
     */
    public void run() {
        ensureLoggedIn();
        boolean running = true;
        while (running) {
            printHeader();
            boolean canManageCustomers = hasPermission(MODULE_CUSTOMER);
            boolean canManageServices = hasPermission(MODULE_SERVICE);
            boolean canManageProducts = hasPermission(MODULE_PRODUCT);
            boolean canManageAppointments = hasPermission(MODULE_APPOINTMENT);
            boolean canManageInvoices = hasPermission(MODULE_INVOICE);
            boolean canManagePromotions = hasPermission(MODULE_PROMOTION);
            boolean canManageSuppliers = hasPermission(MODULE_SUPPLIER);
            boolean canManagePayments = hasPermission(MODULE_PAYMENT);
            boolean canManageGoodsReceipt = hasPermission(MODULE_GOODS_RECEIPT);
            boolean canManageEmployeesMenu = hasPermission(MODULE_EMPLOYEE);

            System.out.println("1. Quản lý khách hàng" + (canManageCustomers ? "" : " (không khả dụng)"));
            System.out.println("2. Quản lý dịch vụ" + (canManageServices ? "" : " (không khả dụng)"));
            System.out.println("3. Quản lý sản phẩm" + (canManageProducts ? "" : " (không khả dụng)"));
            System.out.println("4. Quản lý lịch hẹn" + (canManageAppointments ? "" : " (không khả dụng)"));
            System.out.println("5. Quản lý hóa đơn" + (canManageInvoices ? "" : " (không khả dụng)"));
            System.out.println("6. Quản lý khuyến mãi" + (canManagePromotions ? "" : " (không khả dụng)"));
            System.out.println("7. Quản lý nhà cung cấp" + (canManageSuppliers ? "" : " (không khả dụng)"));
            System.out.println("8. Quản lý thanh toán" + (canManagePayments ? "" : " (không khả dụng)"));
            System.out.println("9. Quản lý phiếu nhập kho" + (canManageGoodsReceipt ? "" : " (không khả dụng)"));
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
                    if (canManageCustomers) {
                        customerMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 2:
                    if (canManageServices) {
                        serviceMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 3:
                    if (canManageProducts) {
                        productMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 4:
                    if (canManageAppointments) {
                        appointmentMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 5:
                    if (canManageInvoices) {
                        invoiceMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 6:
                    if (canManagePromotions) {
                        promotionMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 7:
                    if (canManageSuppliers) {
                        supplierMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 8:
                    if (canManagePayments) {
                        paymentMenu.show();
                    } else {
                        noPermission();
                    }
                    break;
                case 9:
                    if (canManageGoodsReceipt) {
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
                        employeeManagementMenu.show();
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

    private boolean hasPermission(String moduleKey) {
        Employee current = authService.getCurrentUser();
        if (current == null) {
            return false;
        }
        if (current instanceof com.spa.model.Admin) {
            if (MODULE_CUSTOMER.equalsIgnoreCase(moduleKey)
                    || MODULE_SERVICE.equalsIgnoreCase(moduleKey)
                    || MODULE_PRODUCT.equalsIgnoreCase(moduleKey)
                    || MODULE_APPOINTMENT.equalsIgnoreCase(moduleKey)
                    || MODULE_INVOICE.equalsIgnoreCase(moduleKey)
                    || MODULE_PROMOTION.equalsIgnoreCase(moduleKey)
                    || MODULE_SUPPLIER.equalsIgnoreCase(moduleKey)
                    || MODULE_PAYMENT.equalsIgnoreCase(moduleKey)
                    || MODULE_GOODS_RECEIPT.equalsIgnoreCase(moduleKey)) {
                return true;
            }
            if (MODULE_EMPLOYEE.equalsIgnoreCase(moduleKey)) {
                return ((com.spa.model.Admin) current).canAccess("HR_MANAGEMENT");
            }
            return ((com.spa.model.Admin) current).canAccess(moduleKey);
        }
        if (current instanceof com.spa.model.Receptionist) {
            return MODULE_CUSTOMER.equalsIgnoreCase(moduleKey)
                    || MODULE_APPOINTMENT.equalsIgnoreCase(moduleKey)
                    || MODULE_INVOICE.equalsIgnoreCase(moduleKey)
                    || MODULE_PAYMENT.equalsIgnoreCase(moduleKey)
                    || MODULE_PROMOTION.equalsIgnoreCase(moduleKey);
        }
        if (current instanceof com.spa.model.Technician) {
            return MODULE_APPOINTMENT.equalsIgnoreCase(moduleKey);
        }
        return false;
    }

}
