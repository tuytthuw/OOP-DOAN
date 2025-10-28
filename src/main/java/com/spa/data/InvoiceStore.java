package com.spa.data;

import com.spa.model.Appointment;
import com.spa.model.Customer;
import com.spa.model.Invoice;
import com.spa.model.Product;
import com.spa.model.Promotion;
import com.spa.model.Receptionist;
/**
 * Kho dữ liệu cho hóa đơn.
 */
public class InvoiceStore extends DataStore<Invoice> {
    private static final String SEPARATOR = "|";

    public InvoiceStore(String dataFilePath) {
        super(Invoice.class, dataFilePath);
    }

    @Override
    protected String convertToLine(Invoice item) {
        if (item == null) {
            return "";
        }
        return item.getId() + SEPARATOR
                + (item.getCustomer() == null ? "" : item.getCustomer().getId()) + SEPARATOR
                + (item.getReceptionist() == null ? "" : item.getReceptionist().getId()) + SEPARATOR
                + (item.getAppointment() == null ? "" : item.getAppointment().getId()) + SEPARATOR
                + (item.getPromotion() == null ? "" : item.getPromotion().getId()) + SEPARATOR
                + item.getTotalAmount() + SEPARATOR
                + item.isStatus() + SEPARATOR
                + item.getTaxRate() + SEPARATOR
                + item.getServiceChargeRate();
    }

    @Override
    protected Invoice parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 9) {
            return null;
        }
        String id = parts[0];
        String customerId = parts[1];
        String receptionistId = parts[2];
        String appointmentId = parts[3];
        String promotionId = parts[4];
        double total = parseDouble(parts[5]);
        boolean status = Boolean.parseBoolean(parts[6]);
        double taxRate = parseDouble(parts[7]);
        double serviceCharge = parseDouble(parts[8]);

        Invoice invoice = new Invoice(id, null, null, null, null, new DataStore<>(Product.class), total, status, taxRate, serviceCharge);
        if (!customerId.isEmpty()) {
            Customer customer = new Customer();
            customer.setPersonId(customerId);
            invoice.setCustomer(customer);
        }
        if (!receptionistId.isEmpty()) {
            Receptionist receptionist = new Receptionist();
            receptionist.setPersonId(receptionistId);
            invoice.setReceptionist(receptionist);
        }
        if (!appointmentId.isEmpty()) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(appointmentId);
            invoice.setAppointment(appointment);
        }
        if (!promotionId.isEmpty()) {
            Promotion promotion = new Promotion();
            promotion.setPromotionId(promotionId);
            invoice.setPromotion(promotion);
        }
        return invoice;
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
