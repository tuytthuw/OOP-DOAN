package com.spa.data;

import com.spa.model.Invoice;
import com.spa.model.Payment;
import com.spa.model.Receptionist;
import com.spa.model.enums.PaymentMethod;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Kho dữ liệu cho thanh toán.
 */
public class PaymentStore extends DataStore<Payment> {
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PaymentStore(String dataFilePath) {
        super(Payment.class, dataFilePath);
    }

    @Override
    protected String convertToLine(Payment item) {
        if (item == null) {
            return "";
        }
        String date = item.getPaymentDate() == null ? "" : item.getPaymentDate().format(DATE_FORMAT);
        return item.getId() + SEPARATOR
                + (item.getInvoice() == null ? "" : item.getInvoice().getId()) + SEPARATOR
                + item.getAmount() + SEPARATOR
                + (item.getPaymentMethod() == null ? "" : item.getPaymentMethod().name()) + SEPARATOR
                + (item.getReceptionist() == null ? "" : item.getReceptionist().getId()) + SEPARATOR
                + date;
    }

    @Override
    protected Payment parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 6) {
            return null;
        }
        String id = parts[0];
        String invoiceId = parts[1];
        double amount = parseDouble(parts[2]);
        String methodToken = parts[3];
        String receptionistId = parts[4];
        LocalDate paymentDate = parts[5].isEmpty() ? null : LocalDate.parse(parts[5], DATE_FORMAT);

        Payment payment = new Payment(id, null, amount,
                methodToken.isEmpty() ? PaymentMethod.CASH : PaymentMethod.valueOf(methodToken),
                null, paymentDate);
        if (!invoiceId.isEmpty()) {
            Invoice invoice = new Invoice();
            invoice.setInvoiceId(invoiceId);
            payment.setInvoice(invoice);
        }
        if (!receptionistId.isEmpty()) {
            Receptionist receptionist = new Receptionist();
            receptionist.setPersonId(receptionistId);
            receptionist.setFullName("PENDING");
            payment.setReceptionist(receptionist);
        }
        return payment;
    }

    private double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }
}
