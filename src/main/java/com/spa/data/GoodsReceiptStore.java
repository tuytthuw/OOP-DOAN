package com.spa.data;

import com.spa.model.Employee;
import com.spa.model.GoodsReceipt;
import com.spa.model.Product;
import com.spa.model.Receptionist;
import com.spa.model.Supplier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Kho dữ liệu cho phiếu nhập kho.
 */
public class GoodsReceiptStore extends DataStore<GoodsReceipt> {
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public GoodsReceiptStore(String dataFilePath) {
        super(GoodsReceipt.class, dataFilePath);
    }

    @Override
    protected String convertToLine(GoodsReceipt item) {
        if (item == null) {
            return "";
        }
        String receiptDate = item.getReceiptDate() == null ? "" : item.getReceiptDate().format(DATE_FORMAT);
        return item.getId() + SEPARATOR
                + receiptDate + SEPARATOR
                + (item.getEmployee() == null ? "" : item.getEmployee().getId()) + SEPARATOR
                + (item.getSupplier() == null ? "" : item.getSupplier().getId()) + SEPARATOR
                + item.getTotalCost() + SEPARATOR
                + safe(item.getNotes()) + SEPARATOR
                + safe(item.getWarehouseLocation());
    }

    @Override
    protected GoodsReceipt parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 7) {
            return null;
        }
        String id = parts[0];
        LocalDate receiptDate = parts[1].isEmpty() ? null : LocalDate.parse(parts[1], DATE_FORMAT);
        String employeeId = parts[2];
        String supplierId = parts[3];
        double totalCost = parseDouble(parts[4]);
        String notes = restore(parts[5]);
        String warehouse = restore(parts[6]);

        GoodsReceipt receipt = new GoodsReceipt(id, receiptDate, null, null, new DataStore<>(Product.class),
                totalCost, notes, warehouse);
        if (!employeeId.isEmpty()) {
            Employee employee = new Receptionist();
            employee.setPersonId(employeeId);
            receipt.setEmployee(employee);
        }
        if (!supplierId.isEmpty()) {
            Supplier supplier = new Supplier();
            supplier.setSupplierId(supplierId);
            receipt.setSupplier(supplier);
        }
        return receipt;
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(SEPARATOR, "/");
    }

    private String restore(String value) {
        return value == null ? "" : value;
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
