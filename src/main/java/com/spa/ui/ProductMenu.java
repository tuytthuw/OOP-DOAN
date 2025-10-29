package com.spa.ui;

import com.spa.model.Product;
import com.spa.model.Supplier;
import com.spa.service.Validation;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Menu thao tác với sản phẩm.
 */
public class ProductMenu implements MenuModule {
    private final MenuContext context;

    public ProductMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ SẢN PHẨM ---");
            System.out.println("1. Xem danh sách sản phẩm");
            System.out.println("2. Thêm sản phẩm mới");
            System.out.println("3. Cập nhật sản phẩm");
            System.out.println("4. Xóa sản phẩm");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listProducts();
                    Validation.pause();
                    break;
                case 2:
                    addProduct();
                    Validation.pause();
                    break;
                case 3:
                    updateProduct();
                    Validation.pause();
                    break;
                case 4:
                    deleteProduct();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listProducts() {
        System.out.println();
        System.out.println("--- DANH SÁCH SẢN PHẨM ---");
        Product[] products = context.getProductStore().getAll();
        boolean hasData = false;
        for (Product product : products) {
            if (product == null || product.isDeleted()) {
                continue;
            }
            hasData = true;
            System.out.printf("%s | %s | %s | %.2f | tồn: %d%n",
                    product.getId(),
                    product.getProductName(),
                    product.getBrand(),
                    product.getBasePrice().doubleValue(),
                    product.getStockQuantity());
        }
        if (!hasData) {
            System.out.println("Chưa có sản phẩm nào.");
        }
    }

    private void addProduct() {
        System.out.println();
        System.out.println("--- THÊM SẢN PHẨM ---");
        String id = context.getProductStore().generateNextId();
        System.out.println("Mã sản phẩm được cấp: " + id);
        Product product = promptProduct(id, null);
        context.getProductStore().add(product);
        context.getProductStore().writeFile();
        System.out.println("Đã thêm sản phẩm thành công.");
    }

    private void updateProduct() {
        System.out.println();
        System.out.println("--- CẬP NHẬT SẢN PHẨM ---");
        String id = Validation.getString("Mã sản phẩm: ");
        Product existing = context.getProductStore().findById(id);
        if (existing == null || existing.isDeleted()) {
            System.out.println("Không tìm thấy sản phẩm.");
            return;
        }
        Product updated = promptProduct(id, existing);
        existing.setProductName(updated.getProductName());
        existing.setBrand(updated.getBrand());
        existing.setBasePrice(updated.getBasePrice());
        existing.setCostPrice(updated.getCostPrice());
        existing.setUnit(updated.getUnit());
        existing.setStockQuantity(updated.getStockQuantity());
        existing.setReorderLevel(updated.getReorderLevel());
        existing.setExpiryDate(updated.getExpiryDate());
        existing.setSupplier(updated.getSupplier());
        context.getProductStore().writeFile();
        System.out.println("Đã cập nhật sản phẩm.");
    }

    private void deleteProduct() {
        System.out.println();
        System.out.println("--- XÓA SẢN PHẨM ---");
        String id = Validation.getString("Mã sản phẩm: ");
        if (context.getProductStore().delete(id)) {
            context.getProductStore().writeFile();
            System.out.println("Đã xóa sản phẩm.");
        } else {
            System.out.println("Không tìm thấy sản phẩm.");
        }
    }

    private Product promptProduct(String id, Product base) {
        String name = Validation.getString("Tên sản phẩm: ");
        String brand = Validation.getString("Thương hiệu: ");
        double basePrice = Validation.getDouble("Giá bán: ", 0.0, 1_000_000_000.0);
        double costPrice = Validation.getDouble("Giá vốn: ", 0.0, 1_000_000_000.0);
        String unit = Validation.getString("Đơn vị tính: ");
        int stock = Validation.getInt("Số lượng tồn: ", 0, 1_000_000);
        int reorder = Validation.getInt("Ngưỡng đặt hàng lại: ", 0, 1_000_000);
        LocalDate expiry = Validation.getDate("Ngày hết hạn (yyyy-MM-dd): ", DATE_FORMAT);
        Supplier supplier = pickSupplier();

        Product product = new Product(id, name, brand, BigDecimal.valueOf(basePrice), costPrice,
                unit, supplier, stock, expiry, false, reorder);
        if (base != null && base.isDeleted()) {
            product.setDeleted(true);
        }
        return product;
    }

    private Supplier pickSupplier() {
        Supplier[] suppliers = context.getSupplierStore().getAll();
        Supplier[] active = filterActiveSuppliers(suppliers);
        if (active.length == 0) {
            System.out.println("Chưa có nhà cung cấp, để trống thông tin nhà cung cấp.");
            return null;
        }
        System.out.println("Chọn nhà cung cấp:");
        for (int i = 0; i < active.length; i++) {
            Supplier supplier = active[i];
            System.out.printf("%d. %s - %s%n", i + 1, supplier.getId(), supplier.getSupplierName());
        }
        int selected = Validation.getInt("Lựa chọn (0 để bỏ qua): ", 0, active.length);
        if (selected == 0) {
            return null;
        }
        Supplier chosen = active[selected - 1];
        return context.getSupplierStore().findById(chosen.getId());
    }

    private Supplier[] filterActiveSuppliers(Supplier[] suppliers) {
        int count = 0;
        for (Supplier supplier : suppliers) {
            if (supplier != null && !supplier.isDeleted()) {
                count++;
            }
        }
        Supplier[] result = new Supplier[count];
        int index = 0;
        for (Supplier supplier : suppliers) {
            if (supplier != null && !supplier.isDeleted()) {
                result[index] = supplier;
                index++;
            }
        }
        return result;
    }
}
