package models;

import interfaces.IEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GoodsReceipt implements IEntity {
    private static final String ID_PREFIX = "GR";
    private static final int DEFAULT_CAPACITY = 5;
    private static int runningNumber = 1;

    private final String receiptId;
    private String supplierName;
    private LocalDate receivedDate;
    private GoodsReceiptItem[] items;
    private int itemCount;
    private boolean processed;
    private String note;

    public GoodsReceipt(String supplierName,
                        LocalDate receivedDate,
                        String note) {
        this.receiptId = generateReceiptId();
        this.supplierName = supplierName;
        this.receivedDate = receivedDate;
        this.note = note;
        this.processed = false;
        this.items = new GoodsReceiptItem[DEFAULT_CAPACITY];
        this.itemCount = 0;
    }

    private String generateReceiptId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    @Override
    public String getId() {
        return receiptId;
    }

    public void addItem(GoodsReceiptItem item) {
        if (item == null) {
            return;
        }
        ensureCapacity();
        items[itemCount] = item;
        itemCount++;
    }

    private void ensureCapacity() {
        if (itemCount < items.length) {
            return;
        }
        int newCapacity = items.length * 2;
        GoodsReceiptItem[] expanded = new GoodsReceiptItem[newCapacity];
        for (int i = 0; i < items.length; i++) {
            expanded[i] = items[i];
        }
        items = expanded;
    }

    public void removeItem(int index) {
        if (index < 0 || index >= itemCount) {
            return;
        }
        for (int i = index; i < itemCount - 1; i++) {
            items[i] = items[i + 1];
        }
        items[itemCount - 1] = null;
        itemCount--;
    }

    public int totalQuantity() {
        int total = 0;
        for (int i = 0; i < itemCount; i++) {
            GoodsReceiptItem item = items[i];
            if (item != null) {
                total += item.getQuantity();
            }
        }
        return total;
    }

    public BigDecimal totalCost() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < itemCount; i++) {
            GoodsReceiptItem item = items[i];
            if (item != null) {
                total = total.add(item.calculateLineTotal());
            }
        }
        return total;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void markProcessed() {
        processed = true;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public GoodsReceiptItem[] getItems() {
        GoodsReceiptItem[] snapshot = new GoodsReceiptItem[itemCount];
        for (int i = 0; i < itemCount; i++) {
            snapshot[i] = items[i];
        }
        return snapshot;
    }
}
