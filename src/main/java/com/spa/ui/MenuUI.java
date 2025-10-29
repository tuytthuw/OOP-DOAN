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

    private void handleEmployeeMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ NHÂN SỰ ---");
            System.out.println("1. Xem danh sách nhân viên");
            System.out.println("2. Thêm kỹ thuật viên");
            System.out.println("3. Thêm lễ tân");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 3);
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
                case 0:
                default:
                    back = true;
                    break;
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

    private void handleServiceMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ DỊCH VỤ ---");
            System.out.println("1. Xem danh sách dịch vụ");
            System.out.println("2. Thêm dịch vụ mới");
            System.out.println("3. Cập nhật dịch vụ");
            System.out.println("4. Xóa dịch vụ");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listServices();
                    Validation.pause();
                    break;
                case 2:
                    addService();
                    Validation.pause();
                    break;
                case 3:
                    updateService();
                    Validation.pause();
                    break;
                case 4:
                    deleteService();
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
            System.out.println("3. Cập nhật sản phẩm");
            System.out.println("4. Xóa sản phẩm");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listProducts();
                    Validation.pause();
                    break;
                case 2:
                    addProduct();
                    Validation.pause();
                    break;
                case 3:
                    updateProduct();
                    Validation.pause();
                    break;
                case 4:
                    deleteProduct();
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
            System.out.println("2. Thêm khuyến mãi");
            System.out.println("3. Cập nhật khuyến mãi");
            System.out.println("4. Xóa khuyến mãi");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listPromotions();
                    Validation.pause();
                    break;
                case 2:
                    addPromotion();
                    Validation.pause();
                    break;
                case 3:
                    updatePromotion();
                    Validation.pause();
                    break;
                case 4:
                    deletePromotion();
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
            System.out.println("3. Cập nhật nhà cung cấp");
            System.out.println("4. Xóa nhà cung cấp");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listSuppliers();
                    Validation.pause();
                    break;
                case 2:
                    addSupplier();
                    Validation.pause();
                    break;
                case 3:
                    updateSupplier();
                    Validation.pause();
                    break;
                case 4:
                    deleteSupplier();
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
            System.out.println("2. Ghi nhận thanh toán");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    listPayments();
                    Validation.pause();
                    break;
                case 2:
                    addPayment();
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

    private void updateCustomer() {
        System.out.println();
        System.out.println("--- CẬP NHẬT KHÁCH HÀNG ---");
        String id = Validation.getString("Mã khách hàng: ");
        Customer customer = customerStore.findById(id);
        if (customer == null) {
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
        Tier tier = selectTier();

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
        customerStore.writeFile();
        System.out.println("Đã cập nhật khách hàng.");
    }

    private void deleteCustomer() {
        System.out.println();
        System.out.println("--- XÓA KHÁCH HÀNG ---");
        String id = Validation.getString("Mã khách hàng: ");
        if (customerStore.delete(id)) {
            customerStore.writeFile();
            System.out.println("Đã xóa khách hàng.");
        } else {
            System.out.println("Không tìm thấy khách hàng.");
        }
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
                description, createdDate, active, category, false);
        serviceStore.add(service);
        serviceStore.writeFile();
        System.out.println("Đã thêm dịch vụ thành công.");
    }

    private void updateService() {
        System.out.println();
        System.out.println("--- CẬP NHẬT DỊCH VỤ ---");
        String id = Validation.getString("Mã dịch vụ: ");
        Service service = serviceStore.findById(id);
        if (service == null) {
            System.out.println("Không tìm thấy dịch vụ.");
            return;
        }
        String name = Validation.getString("Tên dịch vụ: ");
        double basePrice = Validation.getDouble("Giá gốc: ", 0.0, 1_000_000_000.0);
        int duration = Validation.getInt("Thời lượng (phút): ", 10, 600);
        int buffer = Validation.getInt("Thời gian đệm (phút): ", 0, 120);
        String description = Validation.getString("Mô tả: ");
        LocalDate created = Validation.getDate("Ngày tạo (yyyy-MM-dd): ", DATE_FORMAT);
        boolean active = Validation.getBoolean("Kích hoạt dịch vụ?");
        ServiceCategory category = selectServiceCategory();

        service.setServiceName(name);
        service.setBasePrice(BigDecimal.valueOf(basePrice));
        service.setDurationMinutes(duration);
        service.setBufferTime(buffer);
        service.setDescription(description);
        service.setCreatedDate(created);
        service.setActive(active);
        service.setCategory(category);
        serviceStore.writeFile();
        System.out.println("Đã cập nhật dịch vụ.");
    }

    private void deleteService() {
        System.out.println();
        System.out.println("--- XÓA DỊCH VỤ ---");
        String id = Validation.getString("Mã dịch vụ: ");
        if (serviceStore.delete(id)) {
            serviceStore.writeFile();
            System.out.println("Đã xóa dịch vụ.");
        } else {
            System.out.println("Không tìm thấy dịch vụ.");
        }
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

    private void updateProduct() {
        System.out.println();
        System.out.println("--- CẬP NHẬT SẢN PHẨM ---");
        String id = Validation.getString("Mã sản phẩm: ");
        Product product = productStore.findById(id);
        if (product == null) {
            System.out.println("Không tìm thấy sản phẩm.");
            return;
        }
        String name = Validation.getString("Tên sản phẩm: ");
        String brand = Validation.getString("Thương hiệu: ");
        double basePrice = Validation.getDouble("Giá bán: ", 0.0, 1_000_000_000.0);
        double costPrice = Validation.getDouble("Giá vốn: ", 0.0, 1_000_000_000.0);
        String unit = Validation.getString("Đơn vị tính: ");
        int stock = Validation.getInt("Số lượng tồn: ", 0, 1_000_000);
        int reorder = Validation.getInt("Ngưỡng đặt hàng lại: ", 0, 1_000_000);
        LocalDate expiry = Validation.getDate("Ngày hết hạn (yyyy-MM-dd): ", DATE_FORMAT);

        product.setProductName(name);
        product.setBrand(brand);
        product.setBasePrice(BigDecimal.valueOf(basePrice));
        product.setCostPrice(costPrice);
        product.setUnit(unit);
        product.setStockQuantity(stock);
        product.setReorderLevel(reorder);
        product.setExpiryDate(expiry);
        productStore.writeFile();
        System.out.println("Đã cập nhật sản phẩm.");
    }

    private void deleteProduct() {
        System.out.println();
        System.out.println("--- XÓA SẢN PHẨM ---");
        String id = Validation.getString("Mã sản phẩm: ");
        if (productStore.delete(id)) {
            productStore.writeFile();
            System.out.println("Đã xóa sản phẩm.");
        } else {
            System.out.println("Không tìm thấy sản phẩm.");
        }
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

    private void updateSupplier() {
        System.out.println();
        System.out.println("--- CẬP NHẬT NHÀ CUNG CẤP ---");
        String id = Validation.getString("Mã nhà cung cấp: ");
        Supplier supplier = supplierStore.findById(id);
        if (supplier == null) {
            System.out.println("Không tìm thấy nhà cung cấp.");
            return;
        }
        String name = Validation.getString("Tên nhà cung cấp: ");
        String contact = Validation.getString("Người liên hệ: ");
        String phone = Validation.getString("Số điện thoại: ");
        String address = Validation.getString("Địa chỉ: ");
        String email = Validation.getString("Email: ");
        String notes = Validation.getString("Ghi chú: ");

        supplier.setSupplierName(name);
        supplier.setContactPerson(contact);
        supplier.setPhoneNumber(phone);
        supplier.setAddress(address);
        supplier.setEmail(email);
        supplier.setNotes(notes);
        supplierStore.writeFile();
        System.out.println("Đã cập nhật nhà cung cấp.");
    }

    private void deleteSupplier() {
        System.out.println();
        System.out.println("--- XÓA NHÀ CUNG CẤP ---");
        String id = Validation.getString("Mã nhà cung cấp: ");
        if (supplierStore.delete(id)) {
            supplierStore.writeFile();
            System.out.println("Đã xóa nhà cung cấp.");
        } else {
            System.out.println("Không tìm thấy nhà cung cấp.");
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

    private DiscountType selectDiscountType() {
        System.out.println("Chọn kiểu khuyến mãi:");
        DiscountType[] types = DiscountType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, types.length);
        return types[selected - 1];
    }

    private PaymentMethod selectPaymentMethod() {
        System.out.println("Chọn phương thức thanh toán:");
        PaymentMethod[] methods = PaymentMethod.values();
        for (int i = 0; i < methods.length; i++) {
            System.out.println((i + 1) + ". " + methods[i]);
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, methods.length);
        return methods[selected - 1];
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

    private void listServices() {
        System.out.println();
        System.out.println("--- DANH SÁCH DỊCH VỤ ---");
        Service[] services = serviceStore.getAll();
        if (services.length == 0) {
            System.out.println("Chưa có dịch vụ nào.");
            return;
        }
        for (Service service : services) {
            if (service == null || service.isDeleted()) {
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

    private void listEmployees() {
        System.out.println();
        System.out.println("--- DANH SÁCH NHÂN VIÊN ---");
        Employee[] employees = employeeStore.getAll();
        if (employees.length == 0) {
            System.out.println("Chưa có nhân viên nào.");
            return;
        }
        for (Employee employee : employees) {
            if (employee == null || employee.isDeleted()) {
                continue;
            }
            System.out.printf("%s | %s | Vai trò: %s%n",
                    employee.getId(),
                    employee.getFullName(),
                    employee.getRole());
        }
    }

    private void addTechnician() {
        System.out.println();
        System.out.println("--- THÊM KỸ THUẬT VIÊN ---");
        String id = Validation.getString("Mã nhân viên: ");
        if (employeeStore.findById(id) != null) {
            System.out.println("Mã nhân viên đã tồn tại.");
            return;
        }
        Technician technician = createTechnician(id);
        employeeStore.add(technician);
        employeeStore.writeFile();
        System.out.println("Đã thêm kỹ thuật viên thành công.");
    }

    private void addReceptionist() {
        System.out.println();
        System.out.println("--- THÊM LỄ TÂN ---");
        String id = Validation.getString("Mã nhân viên: ");
        if (employeeStore.findById(id) != null) {
            System.out.println("Mã nhân viên đã tồn tại.");
            return;
        }
        Receptionist receptionist = createReceptionist(id);
        employeeStore.add(receptionist);
        employeeStore.writeFile();
        System.out.println("Đã thêm lễ tân thành công.");
    }

    private Technician createTechnician(String id) {
        String fullName = Validation.getString("Họ tên: ");
        String phone = Validation.getString("Số điện thoại: ");
        boolean male = Validation.getBoolean("Giới tính nam?");
        LocalDate birthDate = Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT);
        String email = Validation.getString("Email: ");
        String address = Validation.getString("Địa chỉ: ");
        double salary = Validation.getDouble("Lương cơ bản: ", 0.0, 1_000_000_000.0);
        String password = Validation.getString("Mật khẩu khởi tạo: ");
        LocalDate hireDate = Validation.getDate("Ngày vào làm (yyyy-MM-dd): ", DATE_FORMAT);
        String skill = Validation.getString("Kỹ năng chính: ");
        String certifications = Validation.getString("Chứng chỉ: ");
        double commission = Validation.getDouble("Tỷ lệ hoa hồng (% dạng 0-1): ", 0.0, 1.0);

        String hashedPassword = encryptPassword(password);
        Technician technician = new Technician(id, fullName, phone, male, birthDate, email, address,
                false, salary, hashedPassword, hireDate, skill, certifications, commission);
        return technician;
    }

    private Receptionist createReceptionist(String id) {
        String fullName = Validation.getString("Họ tên: ");
        String phone = Validation.getString("Số điện thoại: ");
        boolean male = Validation.getBoolean("Giới tính nam?");
        LocalDate birthDate = Validation.getDate("Ngày sinh (yyyy-MM-dd): ", DATE_FORMAT);
        String email = Validation.getString("Email: ");
        String address = Validation.getString("Địa chỉ: ");
        double salary = Validation.getDouble("Lương cơ bản: ", 0.0, 1_000_000_000.0);
        String password = Validation.getString("Mật khẩu khởi tạo: ");
        LocalDate hireDate = Validation.getDate("Ngày vào làm (yyyy-MM-dd): ", DATE_FORMAT);
        double monthlyBonus = Validation.getDouble("Thưởng hàng tháng: ", 0.0, 1_000_000_000.0);

        String hashedPassword = encryptPassword(password);
        Receptionist receptionist = new Receptionist(id, fullName, phone, male, birthDate, email, address,
                false, salary, hashedPassword, hireDate, monthlyBonus);
        return receptionist;
    }

    private boolean canManageCoreData() {
        String role = authService.getCurrentRole();
        return "RECEPTIONIST".equalsIgnoreCase(role) || "MANAGER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
    }

    private boolean canManageEmployees() {
        String role = authService.getCurrentRole();
        return "MANAGER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
    }

    private void noPermission() {
        System.out.println("Bạn không có quyền truy cập chức năng này.");
        Validation.pause();
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

    private void listProducts() {
        System.out.println();
        System.out.println("--- DANH SÁCH SẢN PHẨM ---");
        Product[] products = productStore.getAll();
        if (products.length == 0) {
            System.out.println("Chưa có sản phẩm nào.");
            return;
        }
        for (Product product : products) {
            if (product == null || product.isDeleted()) {
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
            if (promotion == null || promotion.isDeleted()) {
                continue;
            }
            System.out.printf("%s | %s | %s | %.2f%n",
                    promotion.getId(),
                    promotion.getName(),
                    promotion.getDiscountType(),
                    promotion.getDiscountValue());
        }
    }

    private void addPromotion() {
        System.out.println();
        System.out.println("--- THÊM KHUYẾN MÃI ---");
        String id = Validation.getString("Mã khuyến mãi: ");
        if (promotionStore.findById(id) != null) {
            System.out.println("Mã khuyến mãi đã tồn tại.");
            return;
        }
        String name = Validation.getString("Tên chương trình: ");
        String description = Validation.getString("Mô tả ngắn: ");
        DiscountType type = selectDiscountType();
        double value = Validation.getDouble("Giá trị giảm: ", 0.0, 1_000_000_000.0);
        LocalDate start = Validation.getDate("Ngày bắt đầu (yyyy-MM-dd): ", DATE_FORMAT);
        LocalDate end = Validation.getDate("Ngày kết thúc (yyyy-MM-dd): ", DATE_FORMAT);
        double minAmount = Validation.getDouble("Giá trị đơn tối thiểu: ", 0.0, 1_000_000_000.0);

        Promotion promotion = new Promotion(id, name, description, type, value, start, end, minAmount, false);
        promotionStore.add(promotion);
        promotionStore.writeFile();
        System.out.println("Đã thêm khuyến mãi.");
    }

    private void updatePromotion() {
        System.out.println();
        System.out.println("--- CẬP NHẬT KHUYẾN MÃI ---");
        String id = Validation.getString("Mã khuyến mãi: ");
        Promotion promotion = promotionStore.findById(id);
        if (promotion == null) {
            System.out.println("Không tìm thấy khuyến mãi.");
            return;
        }
        String name = Validation.getString("Tên chương trình: ");
        String description = Validation.getString("Mô tả ngắn: ");
        DiscountType type = selectDiscountType();
        double value = Validation.getDouble("Giá trị giảm: ", 0.0, 1_000_000_000.0);
        LocalDate start = Validation.getDate("Ngày bắt đầu (yyyy-MM-dd): ", DATE_FORMAT);
        LocalDate end = Validation.getDate("Ngày kết thúc (yyyy-MM-dd): ", DATE_FORMAT);
        double minAmount = Validation.getDouble("Giá trị đơn tối thiểu: ", 0.0, 1_000_000_000.0);

        promotion.setName(name);
        promotion.setDescription(description);
        promotion.setDiscountType(type);
        promotion.setDiscountValue(value);
        promotion.setStartDate(start);
        promotion.setEndDate(end);
        promotion.setMinPurchaseAmount(minAmount);
        promotionStore.writeFile();
        System.out.println("Đã cập nhật khuyến mãi.");
    }

    private void deletePromotion() {
        System.out.println();
        System.out.println("--- XÓA KHUYẾN MÃI ---");
        String id = Validation.getString("Mã khuyến mãi: ");
        if (promotionStore.delete(id)) {
            promotionStore.writeFile();
            System.out.println("Đã xóa khuyến mãi.");
        } else {
            System.out.println("Không tìm thấy khuyến mãi.");
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
            if (supplier == null || supplier.isDeleted()) {
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

    private void addPayment() {
        System.out.println();
        System.out.println("--- GHI NHẬN THANH TOÁN ---");
        String id = Validation.getString("Mã thanh toán: ");
        if (paymentStore.findById(id) != null) {
            System.out.println("Mã thanh toán đã tồn tại.");
            return;
        }
        String invoiceId = Validation.getString("Mã hóa đơn: ");
        Invoice invoice = invoiceStore.findById(invoiceId);
        if (invoice == null) {
            System.out.println("Không tìm thấy hóa đơn.");
            return;
        }
        double amount = Validation.getDouble("Số tiền thanh toán: ", 0.0, 1_000_000_000.0);
        PaymentMethod method = selectPaymentMethod();
        LocalDate paymentDate = Validation.getDate("Ngày thanh toán (yyyy-MM-dd): ", DATE_FORMAT);
        Receptionist receptionist = null;
        if (authService.getCurrentUser() instanceof Receptionist) {
            receptionist = (Receptionist) authService.getCurrentUser();
        }

        Payment payment = new Payment(id, invoice, amount, method, receptionist, paymentDate);
        if (payment.processPayment()) {
            paymentStore.add(payment);
            paymentStore.writeFile();
            invoiceStore.writeFile();
            System.out.println("Đã ghi nhận thanh toán.");
        } else {
            System.out.println("Thanh toán thất bại. Số tiền chưa đủ.");
        }
    }
}
