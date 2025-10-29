package com.spa.model;

import com.spa.data.DataStore;
import com.spa.interfaces.IEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Phiếu nhập kho sản phẩm từ nhà cung cấp.
 */
public class GoodsReceipt implements IEntity {
    private static final String PREFIX = "GR";

    private String receiptId;
    private LocalDate receiptDate;
    private Employee employee;
    private Supplier supplier;
    private DataStore<Product> receivedProducts;
    private double totalCost;
    private String notes;
    private String warehouseLocation;

    public GoodsReceipt() {
        this("", null, null, null, new DataStore<>(Product.class), 0.0, "", "");
    }

    public GoodsReceipt(String receiptId,
                        LocalDate receiptDate,
                        Employee employee,
                        Supplier supplier,
                        DataStore<Product> receivedProducts,
                        double totalCost,
                        String notes,
                        String warehouseLocation) {
        this.receiptId = receiptId;
        this.receiptDate = receiptDate;
        this.employee = employee;
        this.supplier = supplier;
        this.receivedProducts = receivedProducts;
        this.totalCost = totalCost;
        this.notes = notes;
        this.warehouseLocation = warehouseLocation;
    }

    @Override
    public String getId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public DataStore<Product> getReceivedProducts() {
        return receivedProducts;
    }

    public void setReceivedProducts(DataStore<Product> receivedProducts) {
        this.receivedProducts = receivedProducts;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    @Override
    public void display() {
        System.out.println("---------------- PHIẾU NHẬP KHO ----------------");
        System.out.printf("Mã phiếu     : %s%n", receiptId);
        System.out.printf("Ngày nhập    : %s%n", formatDate(receiptDate));
        System.out.printf("Nhân viên    : %s%n", employee == null ? "" : employee.getFullName());
        System.out.printf("Nhà cung cấp : %s%n", supplier == null ? "" : supplier.getSupplierName());
        System.out.printf("Vị trí kho   : %s%n", warehouseLocation == null || warehouseLocation.isEmpty() ? "(trống)" : warehouseLocation);
        System.out.printf("Ghi chú      : %s%n", notes == null || notes.isEmpty() ? "(trống)" : notes);
        System.out.printf("Tổng chi phí : %.2f%n", totalCost);
        System.out.println("Danh sách sản phẩm:");
        if (receivedProducts == null) {
            System.out.println("(không có sản phẩm)");
        } else {
            Product[] products = receivedProducts.getAll();
            boolean hasProduct = false;
            for (Product product : products) {
                if (product == null) {
                    continue;
                }
                hasProduct = true;
                System.out.printf("- %s | %s | SL: %d | Giá vốn tổng: %.2f%n",
                        product.getId(),
                        product.getProductName(),
                        product.getStockQuantity(),
                        product.getCostPrice());
            }
            if (!hasProduct) {
                System.out.println("(không có sản phẩm)");
            }
        }
        System.out.println("------------------------------------------------");
    }

    private String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public void input() {
        // Xử lý ở tầng UI.
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Thêm sản phẩm vào phiếu nhập và cập nhật tổng chi phí.
     *
     * @param product sản phẩm nhập
     */
    public void addProduct(Product product) {
        if (receivedProducts == null) {
            receivedProducts = new DataStore<>(Product.class);
        }
        receivedProducts.add(product);
        totalCost += product.getCostPrice();
    }

    /**
     * Tính tổng chi phí dựa trên danh sách sản phẩm.
     *
     * @return tổng chi phí
     */
    public double calculateTotalCost() {
        totalCost = 0.0;
        if (receivedProducts != null) {
            Product[] items = receivedProducts.getAll();
            for (Product item : items) {
                if (item != null) {
                    totalCost += item.getCostPrice();
                }
            }
        }
        return totalCost;
    }

    /**
     * Xử lý phiếu nhập: cập nhật tồn kho cho từng sản phẩm.
     */
    public void processReceipt() {
        if (receivedProducts == null) {
            return;
        }
        Product[] items = receivedProducts.getAll();
        for (Product item : items) {
            if (item != null) {
                item.updateStock(1);
            }
        }
    }
}
