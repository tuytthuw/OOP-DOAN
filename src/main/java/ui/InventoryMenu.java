package ui;

import models.GoodsReceipt;
import models.GoodsReceiptItem;
import services.InventoryService;

import java.time.LocalDate;

public class InventoryMenu {
    private final InventoryService inventoryService;
    private final InputHandler inputHandler;
    private final OutputFormatter outputFormatter;

    public InventoryMenu(InventoryService inventoryService,
                         InputHandler inputHandler,
                         OutputFormatter outputFormatter) {
        this.inventoryService = inventoryService;
        this.inputHandler = inputHandler;
        this.outputFormatter = outputFormatter;
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("=== INVENTORY MENU ===");
            System.out.println("1. Ghi nhận phiếu nhập");
            System.out.println("2. Áp dụng phiếu nhập" );
            System.out.println("3. Xem sản phẩm tồn thấp");
            System.out.println("0. Quay lại");
            int choice = inputHandler.readInt("Chọn chức năng", 0, 3);
            switch (choice) {
                case 1:
                    recordReceipt();
                    break;
                case 2:
                    applyReceipt();
                    break;
                case 3:
                    viewLowStock();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    outputFormatter.printStatus("Lựa chọn không hợp lệ", false);
            }
        }
    }

    private void recordReceipt() {
        String supplier = inputHandler.readString("Nhà cung cấp");
        GoodsReceipt receipt = new GoodsReceipt(supplier, LocalDate.now(), null);
        boolean addMore = true;
        while (addMore) {
            String productId = inputHandler.readString("Mã sản phẩm");
            String name = inputHandler.readString("Tên sản phẩm");
            int quantity = inputHandler.readInt("Số lượng", 1, 1_000_000);
            java.math.BigDecimal price = inputHandler.readDecimal("Giá mua");
            GoodsReceiptItem item = new GoodsReceiptItem(productId, name, quantity, price);
            receipt.addItem(item);
            addMore = inputHandler.confirm("Thêm dòng hàng khác?");
        }
        inventoryService.recordGoodsReceipt(receipt);
        outputFormatter.printStatus("Đã ghi nhận phiếu nhập", true);
    }

    private void applyReceipt() {
        String id = inputHandler.readString("Mã phiếu nhập");
        boolean success = inventoryService.applyReceipt(id);
        outputFormatter.printStatus(success ? "Đã áp dụng phiếu" : "Không tìm thấy hoặc đã áp dụng", success);
    }

    private void viewLowStock() {
        int threshold = inputHandler.readInt("Ngưỡng tồn kho", 0, 1_000_000);
        models.Product[] products = inventoryService.getLowStockProducts(threshold);
        String[][] rows = new String[products.length][3];
        for (int i = 0; i < products.length; i++) {
            models.Product product = products[i];
            rows[i][0] = product.getId();
            rows[i][1] = product.getProductName();
            rows[i][2] = String.valueOf(product.getStockQuantity());
        }
        outputFormatter.printTable(new String[]{"ID", "Tên", "Tồn"}, rows);
    }
}
