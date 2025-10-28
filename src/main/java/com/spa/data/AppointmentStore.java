package com.spa.data;

import com.spa.model.Appointment;
import com.spa.model.Customer;
import com.spa.model.Service;
import com.spa.model.Technician;
import com.spa.model.enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Kho dữ liệu cho lịch hẹn.
 */
public class AppointmentStore extends DataStore<Appointment> {
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AppointmentStore(String dataFilePath) {
        super(Appointment.class, dataFilePath);
    }

    @Override
    protected String convertToLine(Appointment item) {
        if (item == null) {
            return "";
        }
        String start = item.getStartTime() == null ? "" : item.getStartTime().format(DATE_TIME_FORMAT);
        String end = item.getEndTime() == null ? "" : item.getEndTime().format(DATE_TIME_FORMAT);
        return item.getId() + SEPARATOR
                + (item.getCustomer() == null ? "" : item.getCustomer().getId()) + SEPARATOR
                + (item.getTechnician() == null ? "" : item.getTechnician().getId()) + SEPARATOR
                + (item.getService() == null ? "" : item.getService().getId()) + SEPARATOR
                + start + SEPARATOR
                + end + SEPARATOR
                + safe(item.getNotes()) + SEPARATOR
                + item.getStatus() + SEPARATOR
                + item.getRating() + SEPARATOR
                + safe(item.getFeedback());
    }

    @Override
    protected Appointment parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 10) {
            return null;
        }
        String id = parts[0];
        String customerId = parts[1];
        String technicianId = parts[2];
        String serviceId = parts[3];
        LocalDateTime start = parts[4].isEmpty() ? null : LocalDateTime.parse(parts[4], DATE_TIME_FORMAT);
        LocalDateTime end = parts[5].isEmpty() ? null : LocalDateTime.parse(parts[5], DATE_TIME_FORMAT);
        String notes = restore(parts[6]);
        AppointmentStatus status = AppointmentStatus.valueOf(parts[7]);
        int rating = parseInt(parts[8]);
        String feedback = restore(parts[9]);

        Appointment appointment = new Appointment(id, null, null, null, start, end, notes, status, rating, feedback);
        if (!customerId.isEmpty()) {
            Customer placeholderCustomer = new Customer();
            placeholderCustomer.setPersonId(customerId);
            placeholderCustomer.setFullName("PENDING");
            appointment.setCustomer(placeholderCustomer);
        }
        if (!technicianId.isEmpty()) {
            Technician placeholderTechnician = new Technician();
            placeholderTechnician.setPersonId(technicianId);
            placeholderTechnician.setFullName("PENDING");
            appointment.setTechnician(placeholderTechnician);
        }
        if (!serviceId.isEmpty()) {
            Service placeholderService = new Service();
            placeholderService.setServiceId(serviceId);
            appointment.setService(placeholderService);
        }
        return appointment;
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(SEPARATOR, "/");
    }

    private String restore(String value) {
        return value == null ? "" : value;
    }

    private int parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
