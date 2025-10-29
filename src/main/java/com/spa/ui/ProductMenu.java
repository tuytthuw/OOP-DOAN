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
            System.out.println("1. Thêm sản phẩm mới");
            System.out.println("2. Thêm nhiều sản phẩm");
            System.out.println("3. Xuất danh sách");
            System.out.println("4. Cập nhật sản phẩm");
            System.out.println("5. Xóa sản phẩm");
            System.out.println("6. Tìm kiếm sản phẩm");
            System.out.println("7. Thống kê sản phẩm");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 7);
            switch (choice) {
                case 1:
                    addProduct();
                    Validation.pause();
                    break;
                case 2:
                    addMultipleProducts();
                    Validation.pause();
                    break;
                case 3:
                    listProducts();
                    Validation.pause();
                    break;
                case 4:
                    updateProduct();
                    Validation.pause();
                    break;
                case 5:
                    deleteProduct();
                    Validation.pause();
                    break;
                case 6:
                    searchProducts();
                    Validation.pause();
                    break;
                case 7:
                    showProductStatistics();
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
            product.display();
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
        Product product = promptProduct(id, null, false);
        if (product == null) {
            System.out.println("Đã hủy thêm sản phẩm.");
            return;
        }
        context.getProductStore().add(product);
        context.getProductStore().writeFile();
        System.out.println("Đã thêm sản phẩm thành công.");
        product.display();
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
        Product updated = promptProduct(id, existing, true);
        if (updated == null) {
            System.out.println("Đã hủy cập nhật sản phẩm.");
            return;
        }
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
        existing.display();
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

    private Product promptProduct(String id, Product base, boolean allowStockInput) {
        String name = Validation.getStringOrCancel(buildPrompt("Tên sản phẩm", base == null ? null : base.getProductName()));
        if (name == null) {
            return null;
        }
        String brand = Validation.getStringOrCancel(buildPrompt("Thương hiệu", base == null ? null : base.getBrand()));
        if (brand == null) {
            return null;
        }
        Double basePrice = Validation.getDoubleOrCancel(buildPrompt("Giá bán", base == null || base.getBasePrice() == null ? null : base.getBasePrice().toString()), 0.0, 1_000_000_000.0);
        if (basePrice == null) {
            return null;
        }
        Double costPrice = Validation.getDoubleOrCancel(buildPrompt("Giá vốn", base == null ? null : Double.toString(base.getCostPrice())), 0.0, 1_000_000_000.0);
        if (costPrice == null) {
            return null;
        }
        String unit = Validation.getStringOrCancel(buildPrompt("Đơn vị tính", base == null ? null : base.getUnit()));
        if (unit == null) {
            return null;
        }
        int stock = 0;
        if (allowStockInput) {
            Integer stockInput = Validation.getIntOrCancel(buildPrompt("Số lượng tồn", Integer.toString(base.getStockQuantity())), 0, 1_000_000);
            if (stockInput == null) {
                return null;
            }
            stock = stockInput;
        } else {
            System.out.println("Tồn kho mặc định 0. Vui lòng nhập hàng qua phiếu nhập kho.");
        }
        Integer reorder = Validation.getIntOrCancel(buildPrompt("Ngưỡng đặt hàng lại", base == null ? null : Integer.toString(base.getReorderLevel())), 0, 1_000_000);
        if (reorder == null) {
            return null;
        }
        LocalDate expiry = Validation.getDateOrCancel(buildPrompt("Ngày hết hạn (yyyy-MM-dd)", base == null || base.getExpiryDate() == null ? null : base.getExpiryDate().toString()), DATE_FORMAT);
        if (expiry == null) {
            return null;
        }
        Supplier supplier = pickSupplier(base == null ? null : base.getSupplier());

        Product product = new Product(id, name, brand, BigDecimal.valueOf(basePrice), costPrice,
                unit, supplier, stock, expiry, false, reorder);
        if (base != null && base.isDeleted()) {
            product.setDeleted(true);
        }
        return product;
    }

    private void addMultipleProducts() {
        Integer total = Validation.getIntOrCancel("Số lượng sản phẩm cần thêm", 1, 1000);
        if (total == null) {
            System.out.println("Đã hủy thao tác.");
            return;
        }
        int added = 0;
        for (int i = 0; i < total; i++) {
            System.out.println("-- Sản phẩm thứ " + (i + 1));
            String id = context.getProductStore().generateNextId();
            Product product = promptProduct(id, null, false);
            if (product == null) {
                System.out.println("Dừng thêm sản phẩm.");
                break;
            }
            context.getProductStore().add(product);
            added++;
        }
        context.getProductStore().writeFile();
        System.out.printf("Đã thêm %d sản phẩm mới.%n", added);
    }

    private Supplier pickSupplier(Supplier current) {
        Supplier[] suppliers = context.getSupplierStore().getAll();
        Supplier[] active = filterActiveSuppliers(suppliers);
        if (active.length == 0) {
            System.out.println("Chưa có nhà cung cấp, để trống thông tin nhà cung cấp.");
            return null;
        }
        System.out.println("Chọn nhà cung cấp:");
        if (current != null) {
            System.out.println("Nhà cung cấp hiện tại: " + current.getSupplierName());
        }
        for (int i = 0; i < active.length; i++) {
            Supplier supplier = active[i];
            System.out.printf("%d. %s - %s%n", i + 1, supplier.getId(), supplier.getSupplierName());
        }
        int selected = Validation.getInt("Lựa chọn (0 để bỏ qua): ", 0, active.length);
        if (selected == 0) {
            return current;
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

    private void searchProducts() {
        String keywordsLine = Validation.getOptionalStringOrCancel("Nhập từ khóa (cách nhau bởi dấu cách) hoặc bỏ trống để xem tất cả: ");
        if (keywordsLine == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        String trimmedKeywords = keywordsLine.trim().toLowerCase();
        Boolean expiredFilter = Validation.getBooleanOrCancel("Chỉ hiển thị sản phẩm quá hạn?");
        Boolean reorderFilter = Validation.getBooleanOrCancel("Chỉ hiển thị sản phẩm dưới ngưỡng đặt hàng?");

        Product[] products = context.getProductStore().getAll();
        boolean foundAny = false;
        for (Product product : products) {
            if (product == null || product.isDeleted()) {
                continue;
            }
            if (expiredFilter != null && product.isExpired() != expiredFilter) {
                continue;
            }
            if (reorderFilter != null && product.checkReorderStatus() != reorderFilter) {
                continue;
            }
            if (!trimmedKeywords.isEmpty() && !matchesTokens(product, trimmedKeywords.split("\\s+"))) {
                continue;
            }
            product.display();
            foundAny = true;
        }
        if (!foundAny) {
            System.out.println("Không có sản phẩm phù hợp.");
        }
    }

    private void showProductStatistics() {
        Product[] products = context.getProductStore().getAll();
        if (products.length == 0) {
            System.out.println("Chưa có dữ liệu sản phẩm.");
            return;
        }
        int total = 0;
        int active = 0;
        int deleted = 0;
        int expired = 0;
        int reorderNeed = 0;
        BigDecimal totalBasePrice = BigDecimal.ZERO;
        double totalCostPrice = 0.0;

        for (Product product : products) {
            if (product == null) {
                continue;
            }
            total++;
            if (product.isDeleted()) {
                deleted++;
                continue;
            }
            active++;
            if (product.isExpired()) {
                expired++;
            }
            if (product.checkReorderStatus()) {
                reorderNeed++;
            }
            if (product.getBasePrice() != null) {
                totalBasePrice = totalBasePrice.add(product.getBasePrice());
            }
            totalCostPrice += product.getCostPrice();
        }
        System.out.printf("Tổng số sản phẩm: %d%n", total);
        System.out.printf("Hoạt động: %d | Đã khóa: %d%n", active, deleted);
        System.out.printf("Sản phẩm quá hạn: %d%n", expired);
        System.out.printf("Cần đặt hàng lại: %d%n", reorderNeed);
        System.out.printf("Tổng giá bán: %s%n", totalBasePrice);
        System.out.printf("Giá bán trung bình: %s%n", total == 0 ? BigDecimal.ZERO : totalBasePrice.divide(BigDecimal.valueOf(total), BigDecimal.ROUND_HALF_UP));
        System.out.printf("Giá vốn trung bình: %.2f%n", total == 0 ? 0.0 : totalCostPrice / total);
    }

    private String buildPrompt(String label, String current) {
        if (current == null || current.isEmpty()) {
            return label + ": ";
        }
        return String.format("%s (hiện tại: %s): ", label, current);
    }

    private boolean matchesTokens(Product product, String[] tokens) {
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            return true;
        }
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            String lower = token.toLowerCase();
            boolean match = containsIgnoreCase(product.getId(), lower)
                    || containsIgnoreCase(product.getProductName(), lower)
                    || containsIgnoreCase(product.getBrand(), lower)
                    || containsIgnoreCase(product.getUnit(), lower)
                    || (product.getSupplier() != null && containsIgnoreCase(product.getSupplier().getSupplierName(), lower));
            if (!match) {
                return false;
            }
        }
        return true;
    }

    private boolean containsIgnoreCase(String source, String token) {
        if (source == null || token == null || token.isEmpty()) {
            return false;
        }
        return source.toLowerCase().contains(token);
    }
}
