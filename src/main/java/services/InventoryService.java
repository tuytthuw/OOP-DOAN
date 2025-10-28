package services;

import collections.GoodsReceiptManager;
import collections.ProductManager;
import models.GoodsReceipt;
import models.GoodsReceiptItem;
import models.Product;

public class InventoryService {
    private final GoodsReceiptManager goodsReceiptManager;
    private final ProductManager productManager;

    public InventoryService(GoodsReceiptManager goodsReceiptManager,
                            ProductManager productManager) {
        this.goodsReceiptManager = goodsReceiptManager;
        this.productManager = productManager;
    }

    public void recordGoodsReceipt(GoodsReceipt receipt) {
        goodsReceiptManager.add(receipt);
    }

    public boolean applyReceipt(String receiptId) {
        GoodsReceipt receipt = goodsReceiptManager.getById(receiptId);
        if (receipt == null || receipt.isProcessed()) {
            return false;
        }
        GoodsReceiptItem[] items = receipt.getItems();
        for (GoodsReceiptItem item : items) {
            if (item == null) {
                continue;
            }
            productManager.updateStock(item.getProductId(), item.getQuantity());
        }
        receipt.markProcessed();
        goodsReceiptManager.update(receipt);
        return true;
    }

    public Product[] getLowStockProducts(int threshold) {
        return productManager.findLowStock(threshold);
    }

    public GoodsReceipt[] getUnprocessedReceipts() {
        return goodsReceiptManager.findUnprocessed();
    }

    public Product[] reconcileStock() {
        return productManager.getAll();
    }

    public Product[] listProducts() {
        return productManager.getAll();
    }

    public GoodsReceipt[] listGoodsReceipts() {
        return goodsReceiptManager.getAll();
    }
}
