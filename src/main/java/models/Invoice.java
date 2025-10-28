package models;

import enums.DiscountType;
import interfaces.IEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Invoice implements IEntity {
    private static final String ID_PREFIX = "INV";
    private static final int DEFAULT_CAPACITY = 4;
    private static int runningNumber = 1;

    private final String invoiceId;
    private String customerId;
    private String appointmentId;
    private InvoiceItem[] items;
    private int itemCount;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal subtotal;
    private BigDecimal totalAmount;
    private LocalDateTime issuedAt;
    private boolean paid;

    public Invoice(String customerId, String appointmentId) {
        this.invoiceId = generateId();
        this.customerId = customerId;
        this.appointmentId = appointmentId;
        this.items = new InvoiceItem[DEFAULT_CAPACITY];
        this.itemCount = 0;
        this.discountType = DiscountType.FIXED_AMOUNT;
        this.discountValue = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.issuedAt = LocalDateTime.now();
        this.paid = false;
    }

    private String generateId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    @Override
    public String getId() {
        return invoiceId;
    }

    public void addItem(InvoiceItem item) {
        if (item == null) {
            return;
        }
        ensureCapacity();
        items[itemCount] = item;
        itemCount++;
        calculateTotals();
    }

    public void removeItem(String sellableId) {
        if (sellableId == null || sellableId.isEmpty()) {
            return;
        }
        for (int i = 0; i < itemCount; i++) {
            InvoiceItem current = items[i];
            if (current != null && sellableId.equals(current.getSellableId())) {
                for (int j = i; j < itemCount - 1; j++) {
                    items[j] = items[j + 1];
                }
                items[itemCount - 1] = null;
                itemCount--;
                calculateTotals();
                break;
            }
        }
    }

    private void ensureCapacity() {
        if (itemCount < items.length) {
            return;
        }
        int newCapacity = items.length * 2;
        InvoiceItem[] expanded = new InvoiceItem[newCapacity];
        for (int i = 0; i < items.length; i++) {
            expanded[i] = items[i];
        }
        items = expanded;
    }

    public void applyPromotion(DiscountType type, BigDecimal value) {
        if (type == null || value == null || value.signum() < 0) {
            return;
        }
        this.discountType = type;
        this.discountValue = value;
        calculateTotals();
    }

    public void calculateTotals() {
        subtotal = BigDecimal.ZERO;
        for (int i = 0; i < itemCount; i++) {
            InvoiceItem item = items[i];
            if (item != null) {
                subtotal = subtotal.add(item.calculateSubtotal());
                subtotal = subtotal.subtract(item.calculateDiscount());
            }
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        if (discountType == DiscountType.PERCENTAGE) {
            discountAmount = subtotal.multiply(discountValue).divide(BigDecimal.valueOf(100));
        } else if (discountType == DiscountType.FIXED_AMOUNT) {
            discountAmount = discountValue;
        }

        if (discountAmount.compareTo(subtotal) > 0) {
            discountAmount = subtotal;
        }

        totalAmount = subtotal.subtract(discountAmount);
    }

    public void markPaid() {
        this.paid = true;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public InvoiceItem[] getItems() {
        InvoiceItem[] snapshot = new InvoiceItem[itemCount];
        for (int i = 0; i < itemCount; i++) {
            snapshot[i] = items[i];
        }
        return snapshot;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public boolean isPaid() {
        return paid;
    }
}
