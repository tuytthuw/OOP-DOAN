package com.spa.data;

import com.spa.model.Supplier;

/**
 * Kho dữ liệu dành cho nhà cung cấp.
 */
public class SupplierStore extends DataStore<Supplier> {
    private static final String SEPARATOR = "|";

    public SupplierStore(String dataFilePath) {
        super(Supplier.class, dataFilePath);
    }

    @Override
    protected String convertToLine(Supplier item) {
        if (item == null) {
            return "";
        }
        return item.getId() + SEPARATOR
                + safe(item.getSupplierName()) + SEPARATOR
                + safe(item.getContactPerson()) + SEPARATOR
                + safe(item.getPhoneNumber()) + SEPARATOR
                + safe(item.getAddress()) + SEPARATOR
                + safe(item.getEmail()) + SEPARATOR
                + safe(item.getNotes()) + SEPARATOR
                + item.isDeleted();
    }

    @Override
    protected Supplier parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 8) {
            return null;
        }
        return new Supplier(parts[0], restore(parts[1]), restore(parts[2]), restore(parts[3]),
                restore(parts[4]), restore(parts[5]), restore(parts[6]), Boolean.parseBoolean(parts[7]));
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(SEPARATOR, "/");
    }

    private String restore(String value) {
        return value == null ? "" : value;
    }
}
