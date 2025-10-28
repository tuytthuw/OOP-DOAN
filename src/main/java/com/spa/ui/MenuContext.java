package com.spa.ui;

import com.spa.data.DataStore;
import com.spa.data.EmployeeStore;
import com.spa.model.Appointment;
import com.spa.model.Customer;
import com.spa.model.Employee;
import com.spa.model.Invoice;
import com.spa.model.Payment;
import com.spa.model.Product;
import com.spa.model.Promotion;
import com.spa.model.Service;
import com.spa.model.Supplier;
import com.spa.service.AuthService;

/**
 * Gom nhóm các phụ thuộc dùng chung giữa các menu.
 */
public final class MenuContext {
    private final AuthService authService;
    private final EmployeeStore employeeStore;
    private final DataStore<Customer> customerStore;
    private final DataStore<Service> serviceStore;
    private final DataStore<Product> productStore;
    private final DataStore<Appointment> appointmentStore;
    private final DataStore<Invoice> invoiceStore;
    private final DataStore<Promotion> promotionStore;
    private final DataStore<Supplier> supplierStore;
    private final DataStore<Payment> paymentStore;

    public MenuContext(AuthService authService,
                       EmployeeStore employeeStore,
                       DataStore<Customer> customerStore,
                       DataStore<Service> serviceStore,
                       DataStore<Product> productStore,
                       DataStore<Appointment> appointmentStore,
                       DataStore<Invoice> invoiceStore,
                       DataStore<Promotion> promotionStore,
                       DataStore<Supplier> supplierStore,
                       DataStore<Payment> paymentStore) {
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
    }

    public AuthService getAuthService() {
        return authService;
    }

    public EmployeeStore getEmployeeStore() {
        return employeeStore;
    }

    public DataStore<Customer> getCustomerStore() {
        return customerStore;
    }

    public DataStore<Service> getServiceStore() {
        return serviceStore;
    }

    public DataStore<Product> getProductStore() {
        return productStore;
    }

    public DataStore<Appointment> getAppointmentStore() {
        return appointmentStore;
    }

    public DataStore<Invoice> getInvoiceStore() {
        return invoiceStore;
    }

    public DataStore<Promotion> getPromotionStore() {
        return promotionStore;
    }

    public DataStore<Supplier> getSupplierStore() {
        return supplierStore;
    }

    public DataStore<Payment> getPaymentStore() {
        return paymentStore;
    }
}
