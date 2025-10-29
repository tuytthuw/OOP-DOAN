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
            System.out.println("1. Tạo phiếu nhập kho");
            System.out.println("2. Xuất danh sách");
            System.out.println("3. Tìm kiếm phiếu nhập");
            System.out.println("4. Thống kê phiếu nhập");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    createGoodsReceipt();
                    Validation.pause();
                    break;
                case 2:
                    listGoodsReceipts();
                    Validation.pause();
                    break;
                case 3:
                    searchGoodsReceipts();
                    Validation.pause();
                    break;
                case 4:
                    showGoodsReceiptStatistics();
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
        String header = goodsReceiptHeader();
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        for (GoodsReceipt receipt : receipts) {
            if (receipt == null) {
                continue;
            }
            printGoodsReceiptRow(receipt);
            hasData = true;
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
        LocalDate receiptDate = LocalDate.now();
        Employee employee = resolveCurrentEmployee();
        if (employee == null) {
            System.out.println("Không xác định được nhân viên đang đăng nhập. Vui lòng đăng nhập lại.");
            return;
        }
        Supplier supplier = selectSupplier();
        if (supplier == null) {
            System.out.println("Không có nhà cung cấp hợp lệ.");
            return;
        }
        String notes = Validation.getOptionalString("Ghi chú: ");
        String warehouse = Validation.getOptionalString("Vị trí kho: ");

        GoodsReceipt receipt = new GoodsReceipt(id, receiptDate, employee, supplier,
                new DataStore<>(Product.class), 0.0, notes, warehouse);
        addProducts(receipt);
        if (receipt.getReceivedProducts().getAll().length == 0) {
            System.out.println("Phiếu nhập phải có ít nhất một sản phẩm.");
            return;
        }
        receipt.calculateTotalCost();

        context.getGoodsReceiptStore().add(receipt);
        context.getGoodsReceiptStore().writeFile();
        context.getProductStore().writeFile();

        System.out.printf("Đã tạo phiếu nhập %s với tổng chi phí %.2f.%n", receipt.getId(), receipt.getTotalCost());
        displayReceipt(receipt);
    }

    private void addProducts(GoodsReceipt receipt) {
        boolean adding = true;
        while (adding) {
            System.out.println();
            System.out.println("1. Thêm bằng mã sản phẩm");
            System.out.println("2. Tìm kiếm sản phẩm");
            System.out.println("0. Hoàn tất");
            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    Product byId = selectProductById();
                    if (byId != null) {
                        appendReceiptItem(receipt, byId);
                    }
                    break;
                case 2:
                    Product searched = searchProduct();
                    if (searched != null) {
                        appendReceiptItem(receipt, searched);
                    }
                    break;
                case 0:
                default:
                    adding = false;
                    break;
            }
        }
    }

    private void appendReceiptItem(GoodsReceipt receipt, Product baseProduct) {
        Integer quantity = Validation.getIntOrCancel("Số lượng nhập", 1, 1_000_000);
        if (quantity == null) {
            return;
        }
        Double costPrice = Validation.getDoubleOrCancel("Giá vốn mỗi đơn vị", 0.0, 1_000_000_000.0);
        if (costPrice == null) {
            return;
        }
        Product receiptItem = new Product(baseProduct.getId(), baseProduct.getProductName(), baseProduct.getBrand(),
                baseProduct.getBasePrice(), costPrice * quantity, baseProduct.getUnit(), baseProduct.getSupplier(),
                quantity, baseProduct.getExpiryDate(), false, baseProduct.getReorderLevel());
        receipt.addProduct(receiptItem);
        baseProduct.setCostPrice(costPrice);
        baseProduct.updateStock(quantity);
    }

    private Product selectProductById() {
        String productId = Validation.getStringOrCancel("Nhập mã sản phẩm (nhập '" + Validation.cancelKeyword() + "' để hủy)");
        if ("0".equals(productId)) {
            return null;
        }
        if (productId == null) {
            return null;
        }
        Product product = context.getProductStore().findById(productId);
        if (product == null || product.isDeleted()) {
            System.out.println("Sản phẩm không tồn tại hoặc đã bị khóa.");
            return null;
        }
        return product;
    }

    private Product searchProduct() {
        String keyword = Validation.getStringOrCancel("Từ khóa tìm kiếm (nhập '" + Validation.cancelKeyword() + "' để hủy)");
        if (keyword == null) {
            return null;
        }
        if (keyword.isEmpty()) {
            System.out.println("Từ khóa không được bỏ trống.");
            return null;
        }
        Product[] products = context.getProductStore().getAll();
        String lowerKeyword = keyword.toLowerCase();
        int matchCount = 0;
        for (Product product : products) {
            if (isMatchingProduct(product, lowerKeyword)) {
                matchCount++;
            }
        }
        if (matchCount == 0) {
            System.out.println("Không tìm thấy sản phẩm phù hợp.");
            return null;
        }
        Product[] matches = new Product[matchCount];
        int index = 0;
        for (Product product : products) {
            if (isMatchingProduct(product, lowerKeyword)) {
                matches[index] = product;
                index++;
            }
        }
        System.out.println("Kết quả tìm kiếm:");
        for (int i = 0; i < matches.length; i++) {
            Product item = matches[i];
            System.out.printf("%d. %s | %s | tồn: %d%n",
                    i + 1,
                    item.getId(),
                    item.getProductName(),
                    item.getStockQuantity());
        }
        int selected = Validation.getInt("Chọn sản phẩm (0 để bỏ qua): ", 0, matches.length);
        if (selected == 0) {
            return null;
        }
        Product chosen = context.getProductStore().findById(matches[selected - 1].getId());
        if (chosen == null || chosen.isDeleted()) {
            System.out.println("Sản phẩm không còn khả dụng.");
            return null;
        }
        return chosen;
    }

    private boolean isMatchingProduct(Product product, String keyword) {
        if (product == null || product.isDeleted()) {
            return false;
        }
        String id = product.getId() == null ? "" : product.getId().toLowerCase();
        String name = product.getProductName() == null ? "" : product.getProductName().toLowerCase();
        String brand = product.getBrand() == null ? "" : product.getBrand().toLowerCase();
        return id.contains(keyword) || name.contains(keyword) || brand.contains(keyword);
    }

    private void searchGoodsReceipts() {
        String exactId = Validation.getStringOrCancel("Nhập mã phiếu nhập để tìm nhanh (nhập '" + Validation.cancelKeyword() + "' để bỏ qua)");
        if (exactId == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        boolean foundAny = false;
        boolean headerPrinted = false;
        if (!exactId.isEmpty()) {
            GoodsReceipt receipt = context.getGoodsReceiptStore().findById(exactId);
            if (receipt != null) {
                if (!headerPrinted) {
                    String header = goodsReceiptHeader();
                    System.out.println(header);
                    System.out.println("-".repeat(header.length()));
                    headerPrinted = true;
                }
                printGoodsReceiptRow(receipt);
                foundAny = true;
            } else {
                System.out.println("Không tìm thấy phiếu nhập theo mã.");
            }
        }

        LocalDate fromDate = Validation.getDateOrCancel("Ngày bắt đầu (yyyy-MM-dd, nhập '" + Validation.cancelKeyword() + "' để bỏ qua)", DATE_FORMAT);
        if (fromDate == null && !Validation.cancelKeyword().equalsIgnoreCase("Q")) {
            // null do hủy
        }
        LocalDate toDate = Validation.getDateOrCancel("Ngày kết thúc (yyyy-MM-dd, nhập '" + Validation.cancelKeyword() + "' để bỏ qua)", DATE_FORMAT);
        if (toDate == null && !Validation.cancelKeyword().equalsIgnoreCase("Q")) {
            // null do hủy
        }
        String supplierId = Validation.getStringOrCancel("Nhập mã nhà cung cấp (bỏ qua nếu trống)");
        if (supplierId == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        String employeeId = Validation.getStringOrCancel("Nhập mã nhân viên (bỏ qua nếu trống)");
        if (employeeId == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }

        GoodsReceipt[] receipts = context.getGoodsReceiptStore().getAll();
        for (GoodsReceipt receipt : receipts) {
            if (receipt == null) {
                continue;
            }
            if (!matchesReceiptFilters(receipt, fromDate, toDate, supplierId, employeeId)) {
                continue;
            }
            if (!headerPrinted) {
                String header = goodsReceiptHeader();
                System.out.println(header);
                System.out.println("-".repeat(header.length()));
                headerPrinted = true;
            }
            printGoodsReceiptRow(receipt);
            foundAny = true;
        }
        if (!foundAny) {
            System.out.println("Không có phiếu nhập phù hợp.");
        }
    }

    private void showGoodsReceiptStatistics() {
        GoodsReceipt[] receipts = context.getGoodsReceiptStore().getAll();
        if (receipts.length == 0) {
            System.out.println("Chưa có dữ liệu phiếu nhập.");
            return;
        }
        int total = 0;
        double totalCost = 0.0;
        double maxCost = 0.0;
        double minCost = Double.MAX_VALUE;
        GoodsReceipt maxReceipt = null;
        GoodsReceipt minReceipt = null;
        for (GoodsReceipt receipt : receipts) {
            if (receipt == null) {
                continue;
            }
            total++;
            double cost = receipt.getTotalCost();
            totalCost += cost;
            if (cost > maxCost) {
                maxCost = cost;
                maxReceipt = receipt;
            }
            if (cost < minCost) {
                minCost = cost;
                minReceipt = receipt;
            }
        }
        System.out.printf("Tổng số phiếu nhập: %d%n", total);
        System.out.printf("Tổng chi phí: %.2f%n", totalCost);
        System.out.printf("Chi phí trung bình: %.2f%n", total == 0 ? 0.0 : totalCost / total);
        if (maxReceipt != null) {
            System.out.printf("Phiếu nhập cao nhất: %s (%.2f)%n", maxReceipt.getId(), maxCost);
        }
        if (minReceipt != null) {
            System.out.printf("Phiếu nhập thấp nhất: %s (%.2f)%n", minReceipt.getId(), minCost);
        }
    }

    private boolean matchesReceiptFilters(GoodsReceipt receipt,
                                          LocalDate fromDate,
                                          LocalDate toDate,
                                          String supplierId,
                                          String employeeId) {
        LocalDate date = receipt.getReceiptDate();
        if (fromDate != null && (date == null || date.isBefore(fromDate))) {
            return false;
        }
        if (toDate != null && (date == null || date.isAfter(toDate))) {
            return false;
        }
        if (supplierId != null && !supplierId.trim().isEmpty()) {
            if (receipt.getSupplier() == null || !supplierId.equalsIgnoreCase(receipt.getSupplier().getId())) {
                return false;
            }
        }
        if (employeeId != null && !employeeId.trim().isEmpty()) {
            if (receipt.getEmployee() == null || !employeeId.equalsIgnoreCase(receipt.getEmployee().getId())) {
                return false;
            }
        }
        return true;
    }

    private void displayReceipt(GoodsReceipt receipt) {
        System.out.println("---------------- PHIẾU NHẬP ----------------");
        System.out.printf("Mã phiếu     : %s%n", receipt.getId());
        System.out.printf("Ngày nhập    : %s%n", receipt.getReceiptDate());
        System.out.printf("Nhân viên    : %s%n", receipt.getEmployee() == null ? "" : receipt.getEmployee().getFullName());
        System.out.printf("Nhà cung cấp : %s%n", receipt.getSupplier() == null ? "" : receipt.getSupplier().getSupplierName());
        System.out.printf("Vị trí kho   : %s%n", receipt.getWarehouseLocation());
        System.out.printf("Ghi chú      : %s%n", receipt.getNotes());
        System.out.printf("Tổng chi phí : %.2f%n", receipt.getTotalCost());
        System.out.println("Danh sách sản phẩm:");
        Product[] products = receipt.getReceivedProducts().getAll();
        for (Product product : products) {
            if (product == null) {
                continue;
            }
            System.out.printf("- %s | %s | SL: %d | Giá vốn tổng: %.2f%n",
                    product.getId(),
                    product.getProductName(),
                    product.getStockQuantity(),
                    product.getCostPrice());
        }
        System.out.println("---------------------------------------------");
    }

    private String goodsReceiptHeader() {
        return String.format("%-8s | %-12s | %-18s | %-18s | %-12s | %-10s | %-15s",
                "MÃ", "NGÀY", "NHÂN VIÊN", "NHÀ CUNG CẤP", "KHO", "TỔNG", "GHI CHÚ");
    }

    private void printGoodsReceiptRow(GoodsReceipt receipt) {
        String employeeName = receipt.getEmployee() == null ? "" : receipt.getEmployee().getFullName();
        String supplierName = receipt.getSupplier() == null ? "" : receipt.getSupplier().getSupplierName();
        System.out.printf("%-8s | %-12s | %-18s | %-18s | %-12s | %-10.2f | %-15s%n",
                nullToEmpty(receipt.getId()),
                formatDate(receipt.getReceiptDate()),
                nullToEmpty(limitLength(employeeName, 18)),
                nullToEmpty(limitLength(supplierName, 18)),
                nullToEmpty(limitLength(receipt.getWarehouseLocation(), 12)),
                receipt.getTotalCost(),
                nullToEmpty(limitLength(receipt.getNotes(), 15)));
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private String limitLength(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    private String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMAT);
    }

    private Employee resolveCurrentEmployee() {
        if (context.getAuthService() == null || !context.getAuthService().isLoggedIn()) {
            return null;
        }
        return context.getAuthService().getCurrentUser();
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
