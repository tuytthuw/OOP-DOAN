package com.spa.main;

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
import com.spa.model.Admin;
import com.spa.service.AuthService;
import com.spa.ui.MenuHelper;
import com.spa.ui.MenuUI;
import java.time.LocalDate;

/**
 * Điểm khởi đầu của ứng dụng quản lý spa.
 */
public final class SpaManagementApp {
    private static final String DATA_FOLDER = "data";
    private static final String EMPLOYEE_FILE = DATA_FOLDER + "/employees.txt";
    private static final String CUSTOMER_FILE = DATA_FOLDER + "/customers.txt";
    private static final String SERVICE_FILE = DATA_FOLDER + "/services.txt";
    private static final String PRODUCT_FILE = DATA_FOLDER + "/products.txt";
    private static final String APPOINTMENT_FILE = DATA_FOLDER + "/appointments.txt";
    private static final String INVOICE_FILE = DATA_FOLDER + "/invoices.txt";
    private static final String PROMOTION_FILE = DATA_FOLDER + "/promotions.txt";
    private static final String SUPPLIER_FILE = DATA_FOLDER + "/suppliers.txt";
    private static final String PAYMENT_FILE = DATA_FOLDER + "/payments.txt";
    private static final String GOODS_RECEIPT_FILE = DATA_FOLDER + "/goods_receipts.txt";

    private SpaManagementApp() {
    }

    public static void main(String[] args) {
        EmployeeStore employeeStore = new EmployeeStore(EMPLOYEE_FILE);
        CustomerStore customerStore = new CustomerStore(CUSTOMER_FILE);
        ServiceStore serviceStore = new ServiceStore(SERVICE_FILE);
        ProductStore productStore = new ProductStore(PRODUCT_FILE);
        AppointmentStore appointmentStore = new AppointmentStore(APPOINTMENT_FILE);
        InvoiceStore invoiceStore = new InvoiceStore(INVOICE_FILE);
        PromotionStore promotionStore = new PromotionStore(PROMOTION_FILE);
        SupplierStore supplierStore = new SupplierStore(SUPPLIER_FILE);
        PaymentStore paymentStore = new PaymentStore(PAYMENT_FILE);
        GoodsReceiptStore goodsReceiptStore = new GoodsReceiptStore(GOODS_RECEIPT_FILE);

        customerStore.readFile();
        serviceStore.readFile();
        productStore.readFile();
        appointmentStore.readFile();
        invoiceStore.readFile();
        promotionStore.readFile();
        supplierStore.readFile();
        paymentStore.readFile();
        goodsReceiptStore.readFile();
        employeeStore.readFile();

        AuthService authService = AuthService.getInstance(employeeStore);
        String seedPassword = "admin123";
        Admin seedAdmin = new Admin("ADM001", "Super Admin", "0000000000", true,
                LocalDate.now(), "admin@spa.com", "Head Office", false,
                2000.0, MenuHelper.hashPassword(seedPassword), LocalDate.now(), "CORE_DATA,HR_MANAGEMENT", true);
        authService.ensureSeedEmployee(seedAdmin, seedPassword);

        MenuUI menu = new MenuUI(authService, employeeStore, customerStore, serviceStore, productStore,
                appointmentStore, invoiceStore, promotionStore, supplierStore, paymentStore, goodsReceiptStore);
        menu.run();
    }
}
