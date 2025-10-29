package com.spa.data;

import com.spa.model.Customer;
import com.spa.model.enums.Tier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Kho dữ liệu cho khách hàng.
 */
public class CustomerStore extends DataStore<Customer> {
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CustomerStore(String dataFilePath) {
        super(Customer.class, dataFilePath);
    }

    @Override
    protected String convertToLine(Customer item) {
        if (item == null) {
            return "";
        }
        String birth = item.getBirthDate() == null ? "" : item.getBirthDate().format(DATE_FORMAT);
        String lastVisit = item.getLastVisitDate() == null ? "" : item.getLastVisitDate().format(DATE_FORMAT);
        return item.getId() + SEPARATOR
                + safe(item.getFullName()) + SEPARATOR
                + safe(item.getPhoneNumber()) + SEPARATOR
                + item.isMale() + SEPARATOR
                + birth + SEPARATOR
                + safe(item.getEmail()) + SEPARATOR
                + safe(item.getAddress()) + SEPARATOR
                + item.isDeleted() + SEPARATOR
                + item.getMemberTier() + SEPARATOR
                + safe(item.getNotes()) + SEPARATOR
                + item.getPoints() + SEPARATOR
                + lastVisit;
    }

    @Override
    protected Customer parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 12) {
            return null;
        }
        String id = parts[0];
        String fullName = restore(parts[1]);
        String phone = restore(parts[2]);
        boolean male = Boolean.parseBoolean(parts[3]);
        LocalDate birthDate = parseDate(parts[4]);
        String email = restore(parts[5]);
        String address = restore(parts[6]);
        boolean deleted = Boolean.parseBoolean(parts[7]);
        Tier tier = Tier.valueOf(parts[8]);
        String notes = restore(parts[9]);
        int points = Integer.parseInt(parts[10]);
        LocalDate lastVisit = parseDate(parts[11]);
        return new Customer(id, fullName, phone, male, birthDate, email, address, deleted,
                tier, notes, points, lastVisit);
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(SEPARATOR, "/");
    }

    private String restore(String value) {
        return value == null ? "" : value;
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(value, DATE_FORMAT);
        } catch (Exception ex) {
            return LocalDate.parse(value);
        }
    }
}
