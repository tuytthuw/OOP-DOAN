package com.spa.data;

import com.spa.model.Product;
import com.spa.model.Supplier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Kho dữ liệu cho sản phẩm.
 */
public class ProductStore extends DataStore<Product> {
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ProductStore(String dataFilePath) {
        super(Product.class, dataFilePath);
    }

    @Override
    protected String convertToLine(Product item) {
        if (item == null) {
            return "";
        }
        String expiry = item.getExpiryDate() == null ? "" : item.getExpiryDate().format(DATE_FORMAT);
        return item.getId() + SEPARATOR
                + safe(item.getProductName()) + SEPARATOR
                + safe(item.getBrand()) + SEPARATOR
                + item.getBasePrice() + SEPARATOR
                + item.getCostPrice() + SEPARATOR
                + safe(item.getUnit()) + SEPARATOR
                + (item.getSupplier() == null ? "" : item.getSupplier().getId()) + SEPARATOR
                + item.getStockQuantity() + SEPARATOR
                + expiry + SEPARATOR
                + item.isDeleted() + SEPARATOR
                + item.getReorderLevel();
    }

    @Override
    protected Product parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 11) {
            return null;
        }
        String id = parts[0];
        String name = restore(parts[1]);
        String brand = restore(parts[2]);
        BigDecimal basePrice = parts[3].isEmpty() ? BigDecimal.ZERO : new BigDecimal(parts[3]);
        double costPrice = parseDouble(parts[4]);
        String unit = restore(parts[5]);
        String supplierId = restore(parts[6]);
        int stock = parseInt(parts[7]);
        LocalDate expiry = parts[8].isEmpty() ? null : LocalDate.parse(parts[8], DATE_FORMAT);
        boolean deleted = Boolean.parseBoolean(parts[9]);
        int reorder = parseInt(parts[10]);
        Product product = new Product(id, name, brand, basePrice, costPrice, unit, null, stock, expiry, deleted, reorder);
        if (!supplierId.isEmpty()) {
            product.setSupplier(new Supplier(supplierId, "", "", "", "", "", "", false));
        }
        return product;
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
