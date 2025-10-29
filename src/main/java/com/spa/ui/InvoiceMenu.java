package com.spa.ui;

import com.spa.data.DataStore;
import com.spa.model.Admin;
import com.spa.model.Appointment;
import com.spa.model.Customer;
import com.spa.model.Employee;
import com.spa.model.Invoice;
import com.spa.model.Product;
import com.spa.model.Promotion;
import com.spa.model.Receptionist;
import com.spa.model.enums.AppointmentStatus;
import com.spa.service.Validation;
import java.math.BigDecimal;

/**
 * Menu quản lý hóa đơn và luồng tạo hóa đơn.
 */
public class InvoiceMenu implements MenuModule {
    private final MenuContext context;

    public InvoiceMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ HÓA ĐƠN ---");
            System.out.println("1. Xem danh sách hóa đơn");
            System.out.println("2. Tạo hóa đơn");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    listInvoices();
                    Validation.pause();
                    break;
                case 2:
                    createInvoice();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listInvoices() {
        System.out.println();
        System.out.println("--- DANH SÁCH HÓA ĐƠN ---");
        Invoice[] invoices = context.getInvoiceStore().getAll();
        boolean hasData = false;
        String header = invoiceHeader();
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        for (Invoice invoice : invoices) {
            if (invoice == null) {
                continue;
            }
            printInvoiceRow(invoice);
            hasData = true;
        }
        if (!hasData) {
            System.out.println("Chưa có hóa đơn nào.");
        }
    }

    private void createInvoice() {
        System.out.println();
        System.out.println("--- TẠO HÓA ĐƠN ---");
        String id = context.getInvoiceStore().generateNextId();
        System.out.println("Mã hóa đơn được cấp: " + id);
        Customer customer = selectCustomer();
        if (customer == null) {
            System.out.println("Không có khách hàng hợp lệ.");
            return;
        }
        Receptionist receptionist = resolveCurrentReceptionist();
        if (receptionist == null) {
            System.out.println("Vui lòng đăng nhập bằng tài khoản lễ tân để tạo hóa đơn.");
            return;
        }
        Appointment appointment = selectAppointment(customer);
        Promotion promotion = selectPromotion();
        double taxRate = Validation.getDouble("Thuế (0-1): ", 0.0, 1.0);
        double serviceCharge = Validation.getDouble("Phí phục vụ (0-1): ", 0.0, 1.0);

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(id);
        invoice.setCustomer(customer);
        invoice.setReceptionist(receptionist);
        invoice.setAppointment(appointment);
        invoice.setPromotion(promotion);
        invoice.setStatus(false);
        invoice.setTaxRate(taxRate);
        invoice.setServiceChargeRate(serviceCharge);
        invoice.setProductList(new DataStore<>(Product.class));

        addProductsToInvoice(invoice);
        if (invoice.getAppointment() == null && context.getInvoiceStore().findById(id) == null
                && invoice.getProductList().getAll().length == 0) {
            System.out.println("Hóa đơn phải có ít nhất một dịch vụ hoặc sản phẩm.");
            return;
        }

        double total = invoice.calculateTotal();
        double membershipDiscount = total * customer.getDiscountRate();
        invoice.setTotalAmount(Math.max(0.0, total - membershipDiscount));

        if (appointment != null && appointment.getStatus() != AppointmentStatus.COMPLETED) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
        }

        int earnedPoints = (int) Math.round(invoice.getTotalAmount() / 10);
        customer.earnPoints(earnedPoints);
        customer.upgradeTier();

        context.getInvoiceStore().add(invoice);
        context.getInvoiceStore().writeFile();
        context.getCustomerStore().writeFile();
        context.getAppointmentStore().writeFile();
        context.getProductStore().writeFile();

        System.out.printf("Đã tạo hóa đơn %s với tổng tiền %.2f. Điểm cộng: %d.%n",
                invoice.getId(), invoice.getTotalAmount(), earnedPoints);
    }

    private void addProductsToInvoice(Invoice invoice) {
        boolean adding = true;
        while (adding) {
            String productId = Validation.getString("Nhập mã sản phẩm (0 để kết thúc): ");
            if ("0".equals(productId)) {
                adding = false;
                continue;
            }
            Product product = context.getProductStore().findById(productId);
            if (product == null || product.isDeleted()) {
                System.out.println("Sản phẩm không tồn tại hoặc đã bị khóa.");
                continue;
            }
            int maxQuantity = product.getStockQuantity();
            if (maxQuantity <= 0) {
                System.out.println("Sản phẩm đã hết hàng.");
                continue;
            }
            int quantity = Validation.getInt("Số lượng: ", 1, maxQuantity);
            Product invoiceItem = new Product(product.getId(), product.getProductName(), product.getBrand(),
                    adjustPrice(product.getBasePrice(), quantity), product.getCostPrice() * quantity,
                    product.getUnit(), product.getSupplier(), quantity, product.getExpiryDate(), false,
                    product.getReorderLevel());
            invoice.addProduct(invoiceItem);
            product.updateStock(-quantity);
            if (product.checkReorderStatus()) {
                System.out.println("Cảnh báo: Sản phẩm " + product.getId() + " cần nhập thêm hàng.");
            }
        }
    }

    private Customer selectCustomer() {
        Customer[] customers = context.getCustomerStore().getAll();
        int count = 0;
        for (Customer customer : customers) {
            if (customer != null && !customer.isDeleted()) {
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        Customer[] active = new Customer[count];
        int index = 0;
        for (Customer customer : customers) {
            if (customer != null && !customer.isDeleted()) {
                active[index] = customer;
                index++;
            }
        }
        System.out.println("Chọn khách hàng:");
        for (int i = 0; i < active.length; i++) {
            System.out.printf("%d. %s - %s%n", i + 1, active[i].getId(), active[i].getFullName());
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, active.length);
        return context.getCustomerStore().findById(active[selected - 1].getId());
    }

    private Appointment selectAppointment(Customer customer) {
        Appointment[] appointments = context.getAppointmentStore().getAll();
        int count = 0;
        for (Appointment appointment : appointments) {
            if (appointment != null && appointment.getCustomer() != null
                    && customer.getId().equals(appointment.getCustomer().getId())) {
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Khách hàng chưa có lịch hẹn, bỏ qua bước liên kết.");
            return null;
        }
        Appointment[] related = new Appointment[count];
        int index = 0;
        for (Appointment appointment : appointments) {
            if (appointment != null && appointment.getCustomer() != null
                    && customer.getId().equals(appointment.getCustomer().getId())) {
                related[index] = appointment;
                index++;
            }
        }
        System.out.println("Chọn lịch hẹn để liên kết (0 để bỏ qua):");
        for (int i = 0; i < related.length; i++) {
            Appointment appointment = related[i];
            System.out.printf("%d. %s - %s%n", i + 1, appointment.getId(), appointment.getStatus());
        }
        int selected = Validation.getInt("Lựa chọn: ", 0, related.length);
        if (selected == 0) {
            return null;
        }
        return context.getAppointmentStore().findById(related[selected - 1].getId());
    }

    private Promotion selectPromotion() {
        Promotion[] promotions = context.getPromotionStore().getAll();
        int count = 0;
        for (Promotion promotion : promotions) {
            if (promotion != null && !promotion.isDeleted()) {
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Chưa có khuyến mãi nào, bỏ qua.");
            return null;
        }
        Promotion[] active = new Promotion[count];
        int index = 0;
        for (Promotion promotion : promotions) {
            if (promotion != null && !promotion.isDeleted()) {
                active[index] = promotion;
                index++;
            }
        }
        System.out.println("Chọn khuyến mãi áp dụng (0 để bỏ qua):");
        for (int i = 0; i < active.length; i++) {
            Promotion promotion = active[i];
            System.out.printf("%d. %s - %s%n", i + 1, promotion.getId(), promotion.getName());
        }
        int selected = Validation.getInt("Lựa chọn: ", 0, active.length);
        if (selected == 0) {
            return null;
        }
        return context.getPromotionStore().findById(active[selected - 1].getId());
    }

    private BigDecimal adjustPrice(BigDecimal basePrice, int quantity) {
        if (basePrice == null) {
            return BigDecimal.ZERO;
        }
        return basePrice.multiply(BigDecimal.valueOf(quantity));
    }

    private Receptionist resolveCurrentReceptionist() {
        if (context.getAuthService() == null || !context.getAuthService().isLoggedIn()) {
            return null;
        }
        Employee current = context.getAuthService().getCurrentUser();
        if (current instanceof Receptionist) {
            Receptionist stored = context.getEmployeeStore().findReceptionistById(current.getId());
            return stored != null ? stored : MenuHelper.toReceptionistView(current);
        }
        if (current instanceof Admin) {
            return MenuHelper.toReceptionistView(current);
        }
        return null;
    }

    private String invoiceHeader() {
        return String.format("%-8s | %-18s | %-18s | %-18s | %-12s | %-8s | %-10s | %-10s",
                "MÃ", "KHÁCH HÀNG", "LỄ TÂN", "LỊCH HẸN", "KHUYẾN MÃI", "TRẠNG", "THUẾ", "TỔNG");
    }

    private void printInvoiceRow(Invoice invoice) {
        String customerName = invoice.getCustomer() == null ? "" : invoice.getCustomer().getFullName();
        String receptionistName = invoice.getReceptionist() == null ? "" : invoice.getReceptionist().getFullName();
        String appointmentId = invoice.getAppointment() == null ? "" : invoice.getAppointment().getId();
        String promotionName = invoice.getPromotion() == null ? "" : invoice.getPromotion().getName();
        System.out.printf("%-8s | %-18s | %-18s | %-18s | %-12s | %-8s | %-10.2f | %-10.2f%n",
                nullToEmpty(invoice.getId()),
                nullToEmpty(customerName),
                nullToEmpty(receptionistName),
                nullToEmpty(appointmentId),
                nullToEmpty(limitLength(promotionName, 12)),
                invoice.isStatus() ? "Đã TT" : "Chưa TT",
                invoice.getTaxRate() * 100,
                invoice.getTotalAmount());
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
