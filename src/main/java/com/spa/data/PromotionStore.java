package com.spa.data;

import com.spa.model.Promotion;
import com.spa.model.enums.DiscountType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Kho dữ liệu cho chương trình khuyến mãi.
 */
public class PromotionStore extends DataStore<Promotion> {
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PromotionStore(String dataFilePath) {
        super(Promotion.class, dataFilePath);
    }

    @Override
    protected String convertToLine(Promotion item) {
        if (item == null) {
            return "";
        }
        String start = item.getStartDate() == null ? "" : item.getStartDate().format(DATE_FORMAT);
        String end = item.getEndDate() == null ? "" : item.getEndDate().format(DATE_FORMAT);
        return item.getId() + SEPARATOR
                + safe(item.getName()) + SEPARATOR
                + safe(item.getDescription()) + SEPARATOR
                + item.getDiscountType() + SEPARATOR
                + item.getDiscountValue() + SEPARATOR
                + start + SEPARATOR
                + end + SEPARATOR
                + item.getMinPurchaseAmount() + SEPARATOR
                + item.isDeleted();
    }

    @Override
    protected Promotion parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 9) {
            return null;
        }
        String id = parts[0];
        String name = restore(parts[1]);
        String description = restore(parts[2]);
        DiscountType discountType = DiscountType.valueOf(parts[3]);
        double discountValue = parseDouble(parts[4]);
        LocalDate startDate = parseDate(parts[5]);
        LocalDate endDate = parseDate(parts[6]);
        double minAmount = parseDouble(parts[7]);
        boolean deleted = Boolean.parseBoolean(parts[8]);
        return new Promotion(id, name, description, discountType, discountValue, startDate, endDate, minAmount, deleted);
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
