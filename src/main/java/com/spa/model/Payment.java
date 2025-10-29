package com.spa.model;

import com.spa.interfaces.IEntity;
import com.spa.model.enums.PaymentMethod;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private LocalDateTime paymentDate;
    private String note;
    private boolean refunded;

    public Payment() {
        this("", null, 0.0, PaymentMethod.CASH, null, null, "", false);
    }

    public Payment(String paymentId,
                   Invoice invoice,
                   double amount,
                   PaymentMethod paymentMethod,
                   Receptionist receptionist,
                   LocalDateTime paymentDate,
                   String note,
                   boolean refunded) {
        this.paymentId = paymentId;
        this.invoice = invoice;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.receptionist = receptionist;
        this.paymentDate = paymentDate;
        this.note = note;
        this.refunded = refunded;
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

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    @Override
    public void display() {
        System.out.println("---------------- THÔNG TIN THANH TOÁN ----------------");
        System.out.printf("Mã thanh toán : %s%n", paymentId);
        System.out.printf("Hóa đơn       : %s%n", invoice == null ? "" : invoice.getId());
        System.out.printf("Số tiền       : %.2f%n", amount);
        System.out.printf("Phương thức   : %s%n", paymentMethod);
        System.out.printf("Thời điểm     : %s%n", formatDateTime(paymentDate));
        System.out.printf("Lễ tân thu     : %s%n", receptionist == null ? "" : receptionist.getFullName());
        System.out.printf("Ghi chú        : %s%n", note == null || note.isEmpty() ? "(trống)" : note);
        System.out.printf("Đã hoàn       : %s%n", refunded ? "Có" : "Không");
        System.out.println("-------------------------------------------------------");
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
        double requiredAmount = invoice.getTotalAmount();
        if (requiredAmount <= 0) {
            requiredAmount = invoice.calculateTotal();
            invoice.setTotalAmount(requiredAmount);
        }
        if (amount < requiredAmount) {
            return false;
        }
        invoice.setStatus(true);
        return true;
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
