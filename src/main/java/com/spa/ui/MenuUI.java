package com.spa.ui;

import com.spa.data.DataStore;
import com.spa.model.Appointment;
import com.spa.model.Customer;
import com.spa.model.Invoice;
import com.spa.model.Payment;
import com.spa.model.Product;
import com.spa.model.Promotion;
import com.spa.model.Service;
import com.spa.model.Supplier;
import com.spa.model.enums.ServiceCategory;
import com.spa.model.enums.Tier;
import com.spa.service.AuthService;
import com.spa.service.Validation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Giao diện console chính của ứng dụng.
 */
public class MenuUI {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final AuthService authService;
    private final DataStore<Customer> customerStore;
    private final DataStore<Service> serviceStore;
    private final DataStore<Product> productStore;
    private final DataStore<Appointment> appointmentStore;
    private final DataStore<Invoice> invoiceStore;
    private final DataStore<Promotion> promotionStore;
    private final DataStore<Supplier> supplierStore;
    private final DataStore<Payment> paymentStore;

    public MenuUI(AuthService authService,
                  DataStore<Customer> customerStore,
                  DataStore<Service> serviceStore,
                  DataStore<Product> productStore,
                  DataStore<Appointment> appointmentStore,
                  DataStore<Invoice> invoiceStore,
                  DataStore<Promotion> promotionStore,
                  DataStore<Supplier> supplierStore,
                  DataStore<Payment> paymentStore) {
        this.authService = authService;
        this.customerStore = customerStore;
        this.serviceStore = serviceStore;
        this.productStore = productStore;
        this.appointmentStore = appointmentStore;
        this.invoiceStore = invoiceStore;
        this.promotionStore = promotionStore;
        this.supplierStore = supplierStore;
        this.paymentStore = paymentStore;
    }

    /**
     * Khởi chạy vòng lặp hiển thị menu.
     */
    public void run() {
        ensureLoggedIn();
        boolean running = true;
        while (running) {
            printHeader();
            System.out.println("1. Quản lý khách hàng");
            System.out.println("2. Quản lý dịch vụ");
            System.out.println("3. Quản lý sản phẩm");
            System.out.println("4. Quản lý lịch hẹn");
            System.out.println("5. Quản lý hóa đơn");
            System.out.println("6. Quản lý khuyến mãi");
            System.out.println("7. Quản lý nhà cung cấp");
            System.out.println("8. Quản lý thanh toán");
            System.out.println("9. Tài khoản");
            System.out.println("0. Thoát");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 9);
            switch (choice) {
                case 1:
                    handleCustomerMenu();
                    break;
                case 2:
                    handleServiceMenu();
                    break;
                case 3:
                    handleProductMenu();
                    break;
                case 4:
                    handleAppointmentMenu();
                    break;
                case 5:
                    handleInvoiceMenu();
                    break;
                case 6:
                    handlePromotionMenu();
                    break;
                case 7:
                    handleSupplierMenu();
                    break;
                case 8:
                    handlePaymentMenu();
                    break;
                case 9:
                    handleAccountMenu();
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

    private void handleCustomerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ KHÁCH HÀNG ---");
            System.out.println("1. Xem danh sách khách hàng");
            System.out.println("2. Thêm khách hàng mới");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    listCustomers();
                    Validation.pause();
                    break;
                case 2:
                    addCustomer();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void handleServiceMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ DỊCH VỤ ---");
            System.out.println("1. Xem danh sách dịch vụ");
            System.out.println("2. Thêm dịch vụ mới");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    listServices();
                    Validation.pause();
                    break;
                case 2:
                    addService();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void handleProductMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ SẢN PHẨM ---");
            System.out.println("1. Xem danh sách sản phẩm");
            System.out.println("2. Thêm sản phẩm mới");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    listProducts();
                    Validation.pause();
                    break;
                case 2:
                    addProduct();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void handleAppointmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ LỊCH HẸN ---");
            System.out.println("1. Xem danh sách lịch hẹn");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 1);
            switch (choice) {
                case 1:
                    listAppointments();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void handleInvoiceMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ HÓA ĐƠN ---");
            System.out.println("1. Xem danh sách hóa đơn");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 1);
            switch (choice) {
                case 1:
                    listInvoices();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void handlePromotionMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ KHUYẾN MÃI ---");
            System.out.println("1. Xem danh sách khuyến mãi");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 1);
            switch (choice) {
                case 1:
                    listPromotions();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void handleSupplierMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ NHÀ CUNG CẤP ---");
            System.out.println("1. Xem danh sách nhà cung cấp");
            System.out.println("2. Thêm nhà cung cấp mới");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    listSuppliers();
                    Validation.pause();
                    break;
                case 2:
                    addSupplier();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void handlePaymentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ THANH TOÁN ---");
            System.out.println("1. Xem danh sách thanh toán");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 1);
            switch (choice) {
                case 1:
                    listPayments();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
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

    private void addCustomer() {
        System.out.println();
        System.out.println("--- THÊM KHÁCH HÀNG ---");
        String id = Validation.getString("Mã khách hàng: ");
        if (customerStore.findById(id) != null) {
            System.out.println("Mã khách hàng đã tồn tại.");
            return;
        }
        String fullName = Validation.getString("Họ tên: ");
        String phone = Validation.getString("Số điện thoại: ");
        String email = Validation.getString("Email: ");
        String address = Validation.getString("Địa chỉ: ");
        boolean male = Validation.getBoolean("Giới tính nam?");
        LocalDate birthDate = Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT);
        String notes = Validation.getString("Ghi chú: ");
        int points = Validation.getInt("Điểm tích lũy ban đầu: ", 0, 1_000_000);
        LocalDate lastVisitDate = Validation.getDate("Lần ghé gần nhất (yyyy-MM-dd): ", DATE_FORMAT);
        Tier tier = selectTier();

        Customer customer = new Customer(id, fullName, phone, male, birthDate, email, address,
                false, tier, notes, points, lastVisitDate);
        customerStore.add(customer);
        customerStore.writeFile();
        System.out.println("Đã thêm khách hàng thành công.");
    }

    private void addService() {
        System.out.println();
        System.out.println("--- THÊM DỊCH VỤ ---");
        String id = Validation.getString("Mã dịch vụ: ");
        if (serviceStore.findById(id) != null) {
            System.out.println("Mã dịch vụ đã tồn tại.");
            return;
        }
        String name = Validation.getString("Tên dịch vụ: ");
        double basePrice = Validation.getDouble("Giá gốc: ", 0.0, 1_000_000_000.0);
        int duration = Validation.getInt("Thời lượng (phút): ", 10, 600);
        int buffer = Validation.getInt("Thời gian đệm (phút): ", 0, 120);
        String description = Validation.getString("Mô tả ngắn: ");
        LocalDate createdDate = Validation.getDate("Ngày tạo (yyyy-MM-dd): ", DATE_FORMAT);
        boolean active = Validation.getBoolean("Kích hoạt dịch vụ?");
        ServiceCategory category = selectServiceCategory();

        Service service = new Service(id, name, BigDecimal.valueOf(basePrice), duration, buffer,
                description, createdDate, active, category);
        serviceStore.add(service);
        serviceStore.writeFile();
        System.out.println("Đã thêm dịch vụ thành công.");
    }

    private void addProduct() {
        System.out.println();
        System.out.println("--- THÊM SẢN PHẨM ---");
        String id = Validation.getString("Mã sản phẩm: ");
        if (productStore.findById(id) != null) {
            System.out.println("Mã sản phẩm đã tồn tại.");
            return;
        }
        String name = Validation.getString("Tên sản phẩm: ");
        String brand = Validation.getString("Thương hiệu: ");
        double basePrice = Validation.getDouble("Giá bán: ", 0.0, 1_000_000_000.0);
        double costPrice = Validation.getDouble("Giá vốn: ", 0.0, 1_000_000_000.0);
        String unit = Validation.getString("Đơn vị tính: ");
        int stock = Validation.getInt("Số lượng tồn: ", 0, 1_000_000);
        int reorderLevel = Validation.getInt("Ngưỡng đặt hàng lại: ", 0, 1_000_000);
        LocalDate expiryDate = Validation.getDate("Ngày hết hạn (yyyy-MM-dd): ", DATE_FORMAT);

        Product product = new Product(id, name, brand, BigDecimal.valueOf(basePrice), costPrice,
                unit, null, stock, expiryDate, false, reorderLevel);
        productStore.add(product);
        productStore.writeFile();
        System.out.println("Đã thêm sản phẩm thành công.");
    }

    private void addSupplier() {
        System.out.println();
        System.out.println("--- THÊM NHÀ CUNG CẤP ---");
        String id = Validation.getString("Mã nhà cung cấp: ");
        if (supplierStore.findById(id) != null) {
            System.out.println("Mã nhà cung cấp đã tồn tại.");
            return;
        }
        String name = Validation.getString("Tên nhà cung cấp: ");
        String contact = Validation.getString("Người liên hệ: ");
        String phone = Validation.getString("Số điện thoại: ");
        String address = Validation.getString("Địa chỉ: ");
        String email = Validation.getString("Email: ");
        String notes = Validation.getString("Ghi chú: ");

        Supplier supplier = new Supplier(id, name, contact, phone, address, email, notes, false);
        supplierStore.add(supplier);
        supplierStore.writeFile();
        System.out.println("Đã thêm nhà cung cấp thành công.");
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

    private Tier selectTier() {
        System.out.println("Chọn hạng thành viên:");
        Tier[] tiers = Tier.values();
        for (int i = 0; i < tiers.length; i++) {
            System.out.println((i + 1) + ". " + tiers[i]);
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, tiers.length);
        return tiers[selected - 1];
    }

    private ServiceCategory selectServiceCategory() {
        System.out.println("Chọn nhóm dịch vụ:");
        ServiceCategory[] categories = ServiceCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, categories.length);
        return categories[selected - 1];
    }

    private void listCustomers() {
        System.out.println();
        System.out.println("--- DANH SÁCH KHÁCH HÀNG ---");
        Customer[] customers = customerStore.getAll();
        if (customers.length == 0) {
            System.out.println("Chưa có khách hàng nào.");
            return;
        }
        for (Customer customer : customers) {
            if (customer == null) {
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

    private void listServices() {
        System.out.println();
        System.out.println("--- DANH SÁCH DỊCH VỤ ---");
        Service[] services = serviceStore.getAll();
        if (services.length == 0) {
            System.out.println("Chưa có dịch vụ nào.");
            return;
        }
        for (Service service : services) {
            if (service == null) {
                continue;
            }
            System.out.printf("%s | %s | %s | %s | %s%n",
                    service.getId(),
                    service.getServiceName(),
                    service.getCategory(),
                    service.getBasePrice(),
                    service.isActive() ? "Đang mở" : "Tạm khóa");
        }
    }

    private void listProducts() {
        System.out.println();
        System.out.println("--- DANH SÁCH SẢN PHẨM ---");
        Product[] products = productStore.getAll();
        if (products.length == 0) {
            System.out.println("Chưa có sản phẩm nào.");
            return;
        }
        for (Product product : products) {
            if (product == null) {
                continue;
            }
            System.out.printf("%s | %s | %s | SL: %d | Ngưỡng: %d%n",
                    product.getId(),
                    product.getProductName(),
                    product.getBrand(),
                    product.getStockQuantity(),
                    product.getReorderLevel());
        }
    }

    private void listAppointments() {
        System.out.println();
        System.out.println("--- DANH SÁCH LỊCH HẸN ---");
        Appointment[] appointments = appointmentStore.getAll();
        if (appointments.length == 0) {
            System.out.println("Chưa có lịch hẹn nào.");
            return;
        }
        for (Appointment appointment : appointments) {
            if (appointment == null) {
                continue;
            }
            String customerName = appointment.getCustomer() == null ? "<Trống>" : appointment.getCustomer().getFullName();
            String serviceName = appointment.getService() == null ? "<Trống>" : appointment.getService().getServiceName();
            LocalDateTime start = appointment.getStartTime();
            System.out.printf("%s | %s | %s | %s | %s%n",
                    appointment.getId(),
                    customerName,
                    serviceName,
                    appointment.getStatus(),
                    start == null ? "<Chưa đặt>" : start.format(DATE_TIME_FORMAT));
        }
    }

    private void listInvoices() {
        System.out.println();
        System.out.println("--- DANH SÁCH HÓA ĐƠN ---");
        Invoice[] invoices = invoiceStore.getAll();
        if (invoices.length == 0) {
            System.out.println("Chưa có hóa đơn nào.");
            return;
        }
        for (Invoice invoice : invoices) {
            if (invoice == null) {
                continue;
            }
            String customerName = invoice.getCustomer() == null ? "<Trống>" : invoice.getCustomer().getFullName();
            System.out.printf("%s | Khách: %s | Tổng tiền: %.2f | Trạng thái: %s%n",
                    invoice.getId(),
                    customerName,
                    invoice.getTotalAmount(),
                    invoice.isStatus() ? "Đã thanh toán" : "Chưa thanh toán");
        }
    }

    private void listPromotions() {
        System.out.println();
        System.out.println("--- DANH SÁCH KHUYẾN MÃI ---");
        Promotion[] promotions = promotionStore.getAll();
        if (promotions.length == 0) {
            System.out.println("Chưa có khuyến mãi nào.");
            return;
        }
        for (Promotion promotion : promotions) {
            if (promotion == null) {
                continue;
            }
            System.out.printf("%s | %s | %s | %.2f%n",
                    promotion.getId(),
                    promotion.getName(),
                    promotion.getDiscountType(),
                    promotion.getDiscountValue());
        }
    }

    private void listSuppliers() {
        System.out.println();
        System.out.println("--- DANH SÁCH NHÀ CUNG CẤP ---");
        Supplier[] suppliers = supplierStore.getAll();
        if (suppliers.length == 0) {
            System.out.println("Chưa có nhà cung cấp nào.");
            return;
        }
        for (Supplier supplier : suppliers) {
            if (supplier == null) {
                continue;
            }
            System.out.printf("%s | %s | Liên hệ: %s | Điện thoại: %s%n",
                    supplier.getId(),
                    supplier.getSupplierName(),
                    supplier.getContactPerson(),
                    supplier.getPhoneNumber());
        }
    }

    private void listPayments() {
        System.out.println();
        System.out.println("--- DANH SÁCH THANH TOÁN ---");
        Payment[] payments = paymentStore.getAll();
        if (payments.length == 0) {
            System.out.println("Chưa có thanh toán nào.");
            return;
        }
        for (Payment payment : payments) {
            if (payment == null) {
                continue;
            }
            String invoiceId = payment.getInvoice() == null ? "<Trống>" : payment.getInvoice().getId();
            System.out.printf("%s | Hóa đơn: %s | Số tiền: %.2f | Phương thức: %s%n",
                    payment.getId(),
                    invoiceId,
                    payment.getAmount(),
                    payment.getPaymentMethod());
        }
    }
}
