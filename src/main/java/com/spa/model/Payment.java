package com.spa.model;

import com.spa.interfaces.IEntity;
import com.spa.model.enums.PaymentMethod;
import java.time.LocalDate;

/**
 * Thanh toán cho hóa đơn.
 */
public class Payment implements IEntity {
    private static final String PREFIX = "PAY";

    private String paymentId;
    private Invoice invoice;
    private double amount;
    private PaymentMethod paymentMethod;
    private Receptionist receptionist;
    private LocalDate paymentDate;

    public Payment() {
        this("", null, 0.0, PaymentMethod.CASH, null, null);
    }

    public Payment(String paymentId,
                   Invoice invoice,
                   double amount,
                   PaymentMethod paymentMethod,
                   Receptionist receptionist,
                   LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.invoice = invoice;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.receptionist = receptionist;
        this.paymentDate = paymentDate;
    }

    @Override
    public String getId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Receptionist getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Receptionist receptionist) {
        this.receptionist = receptionist;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public void display() {
        // Xử lý ở tầng UI.
    }

    @Override
    public void input() {
        // Xử lý ở tầng UI.
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Xử lý thanh toán và cập nhật trạng thái hóa đơn.
     *
     * @return true nếu thành công
     */
    public boolean processPayment() {
        if (invoice == null) {
            return false;
        }
        if (amount < invoice.calculateTotal()) {
            return false;
        }
        invoice.setStatus(true);
        return true;
    }
}
