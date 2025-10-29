package com.spa.ui;

import com.spa.data.DataStore;
import com.spa.model.Employee;
import com.spa.model.GoodsReceipt;
import com.spa.model.Product;
import com.spa.model.Supplier;
import com.spa.service.Validation;
import java.time.LocalDate;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Menu quản lý phiếu nhập kho và luồng nhập hàng.
 */
public class GoodsReceiptMenu implements MenuModule {
    private final MenuContext context;

    public GoodsReceiptMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ PHIẾU NHẬP KHO ---");
            System.out.println("1. Xem danh sách phiếu nhập");
            System.out.println("2. Tạo phiếu nhập kho");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    listGoodsReceipts();
                    Validation.pause();
                    break;
                case 2:
                    createGoodsReceipt();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listGoodsReceipts() {
        System.out.println();
        System.out.println("--- DANH SÁCH PHIẾU NHẬP ---");
        GoodsReceipt[] receipts = context.getGoodsReceiptStore().getAll();
        boolean hasData = false;
        for (GoodsReceipt receipt : receipts) {
            if (receipt == null) {
                continue;
            }
            hasData = true;
            System.out.printf("%s | Ngày: %s | NV: %s | NCC: %s | Tổng chi phí: %.2f%n",
                    receipt.getId(),
                    receipt.getReceiptDate(),
                    receipt.getEmployee() == null ? "" : receipt.getEmployee().getId(),
                    receipt.getSupplier() == null ? "" : receipt.getSupplier().getId(),
                    receipt.getTotalCost());
        }
        if (!hasData) {
            System.out.println("Chưa có phiếu nhập nào.");
        }
    }

    private void createGoodsReceipt() {
        System.out.println();
        System.out.println("--- TẠO PHIẾU NHẬP KHO ---");
        String id = context.getGoodsReceiptStore().generateNextId();
        System.out.println("Mã phiếu nhập được cấp: " + id);
        LocalDate receiptDate = Validation.getDate("Ngày nhập (yyyy-MM-dd): ", DATE_FORMAT);
        Employee employee = selectEmployee();
        if (employee == null) {
            System.out.println("Không có nhân viên hợp lệ.");
            return;
        }
        Supplier supplier = selectSupplier();
        if (supplier == null) {
            System.out.println("Không có nhà cung cấp hợp lệ.");
            return;
        }
        String notes = Validation.getString("Ghi chú: ");
        String warehouse = Validation.getString("Vị trí kho: ");

        GoodsReceipt receipt = new GoodsReceipt(id, receiptDate, employee, supplier,
                new DataStore<>(Product.class), 0.0, notes, warehouse);
        addProducts(receipt);
        if (receipt.getReceivedProducts().getAll().length == 0) {
            System.out.println("Phiếu nhập phải có ít nhất một sản phẩm.");
            return;
        }
        receipt.calculateTotalCost();
        receipt.processReceipt();

        context.getGoodsReceiptStore().add(receipt);
        context.getGoodsReceiptStore().writeFile();
        context.getProductStore().writeFile();

        System.out.printf("Đã tạo phiếu nhập %s với tổng chi phí %.2f.%n", receipt.getId(), receipt.getTotalCost());
    }

    private void addProducts(GoodsReceipt receipt) {
        boolean adding = true;
        while (adding) {
            String productId = Validation.getString("Nhập mã sản phẩm (0 để kết thúc): ");
            if ("0".equals(productId)) {
                adding = false;
                continue;
            }
            Product product = context.getProductStore().findById(productId);
            if (product == null || product.isDeleted()) {
                System.out.println("Sản phẩm không tồn tại hoặc đã bị khóa.");
                continue;
            }
            int quantity = Validation.getInt("Số lượng nhập: ", 1, 1_000_000);
            double costPrice = Validation.getDouble("Giá vốn mỗi đơn vị: ", 0.0, 1_000_000_000.0);
            Product receiptItem = new Product(product.getId(), product.getProductName(), product.getBrand(),
                    product.getBasePrice(), costPrice * quantity, product.getUnit(), product.getSupplier(),
                    quantity, product.getExpiryDate(), false, product.getReorderLevel());
            receipt.addProduct(receiptItem);
            product.updateStock(quantity);
        }
    }

    private Employee selectEmployee() {
        Employee[] employees = context.getEmployeeStore().getAll();
        int count = 0;
        for (Employee employee : employees) {
            if (employee != null && !employee.isDeleted()) {
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        Employee[] active = new Employee[count];
        int index = 0;
        for (Employee employee : employees) {
            if (employee != null && !employee.isDeleted()) {
                active[index] = employee;
                index++;
            }
        }
        System.out.println("Chọn nhân viên phụ trách:");
        for (int i = 0; i < active.length; i++) {
            Employee employee = active[i];
            System.out.printf("%d. %s - %s%n", i + 1, employee.getId(), employee.getFullName());
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, active.length);
        return context.getEmployeeStore().findById(active[selected - 1].getId());
    }

    private Supplier selectSupplier() {
        Supplier[] suppliers = context.getSupplierStore().getAll();
        int count = 0;
        for (Supplier supplier : suppliers) {
            if (supplier != null && !supplier.isDeleted()) {
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        Supplier[] active = new Supplier[count];
        int index = 0;
        for (Supplier supplier : suppliers) {
            if (supplier != null && !supplier.isDeleted()) {
                active[index] = supplier;
                index++;
            }
        }
        System.out.println("Chọn nhà cung cấp:");
        for (int i = 0; i < active.length; i++) {
            Supplier supplier = active[i];
            System.out.printf("%d. %s - %s%n", i + 1, supplier.getId(), supplier.getSupplierName());
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, active.length);
        return context.getSupplierStore().findById(active[selected - 1].getId());
    }
}
