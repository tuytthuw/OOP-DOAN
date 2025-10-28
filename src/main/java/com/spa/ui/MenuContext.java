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

/**
 * Gom nhóm các phụ thuộc dùng chung giữa các menu.
 */
public final class MenuContext {
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

    public MenuContext(AuthService authService,
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
    }

    public AuthService getAuthService() {
        return authService;
    }

    public EmployeeStore getEmployeeStore() {
        return employeeStore;
    }

    public CustomerStore getCustomerStore() {
        return customerStore;
    }

    public ServiceStore getServiceStore() {
        return serviceStore;
    }

    public ProductStore getProductStore() {
        return productStore;
    }

    public AppointmentStore getAppointmentStore() {
        return appointmentStore;
    }

    public InvoiceStore getInvoiceStore() {
        return invoiceStore;
    }

    public PromotionStore getPromotionStore() {
        return promotionStore;
    }

    public SupplierStore getSupplierStore() {
        return supplierStore;
    }

    public PaymentStore getPaymentStore() {
        return paymentStore;
    }

    public GoodsReceiptStore getGoodsReceiptStore() {
        return goodsReceiptStore;
    }
}
