package com.spa.main;

import com.spa.data.DataStore;
import com.spa.model.Appointment;
import com.spa.model.Customer;
import com.spa.model.Employee;
import com.spa.model.Invoice;
import com.spa.model.Payment;
import com.spa.model.Product;
import com.spa.model.Promotion;
import com.spa.model.Receptionist;
import com.spa.model.Service;
import com.spa.model.Supplier;
import com.spa.service.AuthService;
import com.spa.ui.MenuUI;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    private SpaManagementApp() {
    }

    public static void main(String[] args) {
        DataStore<Employee> employeeStore = new DataStore<>(EMPLOYEE_FILE);
        DataStore<Customer> customerStore = new DataStore<>(CUSTOMER_FILE);
        DataStore<Service> serviceStore = new DataStore<>(SERVICE_FILE);
        DataStore<Product> productStore = new DataStore<>(PRODUCT_FILE);
        DataStore<Appointment> appointmentStore = new DataStore<>(APPOINTMENT_FILE);
        DataStore<Invoice> invoiceStore = new DataStore<>(INVOICE_FILE);
        DataStore<Promotion> promotionStore = new DataStore<>(PROMOTION_FILE);
        DataStore<Supplier> supplierStore = new DataStore<>(SUPPLIER_FILE);
        DataStore<Payment> paymentStore = new DataStore<>(PAYMENT_FILE);

        customerStore.readFile();
        serviceStore.readFile();
        productStore.readFile();
        appointmentStore.readFile();
        invoiceStore.readFile();
        promotionStore.readFile();
        supplierStore.readFile();
        paymentStore.readFile();

        AuthService authService = AuthService.getInstance(employeeStore);
        Receptionist seed = new Receptionist("EMP001", "Quản trị viên", "0000000000", true,
                LocalDate.now(), "admin@spa.com", "Head Office", false,
                1000.0, hashPassword("admin123"), LocalDate.now(), 200.0);
        authService.ensureSeedEmployee(seed);

        MenuUI menu = new MenuUI(authService, employeeStore, customerStore, serviceStore, productStore,
                appointmentStore, invoiceStore, promotionStore, supplierStore, paymentStore);
        menu.run();
    }

    private static String hashPassword(String raw) {
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
