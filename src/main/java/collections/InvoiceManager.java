package collections;

import models.Invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InvoiceManager extends BaseManager<Invoice> {
    private static final int DEFAULT_CAPACITY = 80;

    public InvoiceManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected Invoice[] createArray(int size) {
        return new Invoice[size];
    }

    public Invoice[] findByCustomer(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            return new Invoice[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Invoice invoice = items[i];
            if (invoice != null && customerId.equals(invoice.getCustomerId())) {
                matches++;
            }
        }
        Invoice[] result = new Invoice[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Invoice invoice = items[i];
            if (invoice != null && customerId.equals(invoice.getCustomerId())) {
                result[index++] = invoice;
            }
        }
        return result;
    }

    public Invoice[] findUnpaid() {
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Invoice invoice = items[i];
            if (invoice != null && !invoice.isPaid()) {
                matches++;
            }
        }
        Invoice[] result = new Invoice[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Invoice invoice = items[i];
            if (invoice != null && !invoice.isPaid()) {
                result[index++] = invoice;
            }
        }
        return result;
    }

    public Invoice[] findByDateRange(LocalDate start, LocalDate end) {
        LocalDate from = start != null ? start : LocalDate.MIN;
        LocalDate to = end != null ? end : LocalDate.MAX;
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Invoice invoice = items[i];
            if (invoice != null && invoice.getIssuedAt() != null) {
                LocalDate date = invoice.getIssuedAt().toLocalDate();
                if (!date.isBefore(from) && !date.isAfter(to)) {
                    matches++;
                }
            }
        }
        Invoice[] result = new Invoice[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Invoice invoice = items[i];
            if (invoice != null && invoice.getIssuedAt() != null) {
                LocalDate date = invoice.getIssuedAt().toLocalDate();
                if (!date.isBefore(from) && !date.isAfter(to)) {
                    result[index++] = invoice;
                }
            }
        }
        return result;
    }
}
