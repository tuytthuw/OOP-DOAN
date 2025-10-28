package collections;

import models.GoodsReceipt;
import java.time.LocalDate;

public class GoodsReceiptManager extends BaseManager<GoodsReceipt> {
    private static final int DEFAULT_CAPACITY = 25;

    public GoodsReceiptManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected GoodsReceipt[] createArray(int size) {
        return new GoodsReceipt[size];
    }

    public GoodsReceipt[] findUnprocessed() {
        int matches = 0;
        for (int i = 0; i < count; i++) {
            GoodsReceipt receipt = items[i];
            if (receipt != null && !receipt.isProcessed()) {
                matches++;
            }
        }
        GoodsReceipt[] result = new GoodsReceipt[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            GoodsReceipt receipt = items[i];
            if (receipt != null && !receipt.isProcessed()) {
                result[index++] = receipt;
            }
        }
        return result;
    }

    public GoodsReceipt[] findByDate(LocalDate date) {
        if (date == null) {
            return new GoodsReceipt[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            GoodsReceipt receipt = items[i];
            if (receipt != null && date.equals(receipt.getReceivedDate())) {
                matches++;
            }
        }
        GoodsReceipt[] result = new GoodsReceipt[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            GoodsReceipt receipt = items[i];
            if (receipt != null && date.equals(receipt.getReceivedDate())) {
                result[index++] = receipt;
            }
        }
        return result;
    }

    public GoodsReceipt[] findBySupplier(String supplier) {
        if (supplier == null || supplier.isEmpty()) {
            return new GoodsReceipt[0];
        }
        String lower = supplier.toLowerCase();
        int matches = 0;
        for (int i = 0; i < count; i++) {
            GoodsReceipt receipt = items[i];
            if (receipt != null && receipt.getSupplierName() != null
                    && receipt.getSupplierName().toLowerCase().contains(lower)) {
                matches++;
            }
        }
        GoodsReceipt[] result = new GoodsReceipt[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            GoodsReceipt receipt = items[i];
            if (receipt != null && receipt.getSupplierName() != null
                    && receipt.getSupplierName().toLowerCase().contains(lower)) {
                result[index++] = receipt;
            }
        }
        return result;
    }
}
