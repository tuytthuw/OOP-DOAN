package com.spa.data;

import com.spa.model.Service;
import com.spa.model.enums.ServiceCategory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Kho dữ liệu cho dịch vụ spa.
 */
public class ServiceStore extends DataStore<Service> {
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ServiceStore(String dataFilePath) {
        super(Service.class, dataFilePath);
    }

    @Override
    protected String convertToLine(Service item) {
        if (item == null) {
            return "";
        }
        String created = item.getCreatedDate() == null ? "" : item.getCreatedDate().format(DATE_FORMAT);
        return item.getId() + SEPARATOR
                + safe(item.getServiceName()) + SEPARATOR
                + item.getBasePrice() + SEPARATOR
                + item.getDurationMinutes() + SEPARATOR
                + item.getBufferTime() + SEPARATOR
                + safe(item.getDescription()) + SEPARATOR
                + created + SEPARATOR
                + item.isActive() + SEPARATOR
                + item.getCategory() + SEPARATOR
                + item.isDeleted();
    }

    @Override
    protected Service parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 10) {
            return null;
        }
        String id = parts[0];
        String name = restore(parts[1]);
        BigDecimal basePrice = parts[2].isEmpty() ? BigDecimal.ZERO : new BigDecimal(parts[2]);
        int duration = parseInt(parts[3]);
        int buffer = parseInt(parts[4]);
        String description = restore(parts[5]);
        LocalDate created = parts[6].isEmpty() ? null : LocalDate.parse(parts[6], DATE_FORMAT);
        boolean active = Boolean.parseBoolean(parts[7]);
        ServiceCategory category = ServiceCategory.valueOf(parts[8]);
        boolean deleted = Boolean.parseBoolean(parts[9]);
        return new Service(id, name, basePrice, duration, buffer, description, created, active, category, deleted);
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
