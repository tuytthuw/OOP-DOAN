package models;

import java.math.BigDecimal;

public class InvoiceItem {
    private String sellableId;
    private String sellableType;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountPerUnit;

    public InvoiceItem(String sellableId,
                       String sellableType,
                       int quantity,
                       BigDecimal unitPrice,
                       BigDecimal discountPerUnit) {
        this.sellableId = sellableId;
        this.sellableType = sellableType;
        this.quantity = Math.max(0, quantity);
        this.unitPrice = unitPrice == null ? BigDecimal.ZERO : unitPrice;
        this.discountPerUnit = discountPerUnit == null ? BigDecimal.ZERO : discountPerUnit;
    }

    public BigDecimal calculateSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal calculateDiscount() {
        return discountPerUnit.multiply(BigDecimal.valueOf(quantity));
    }

    public String getSellableId() {
        return sellableId;
    }

    public void setSellableId(String sellableId) {
        this.sellableId = sellableId;
    }

    public String getSellableType() {
        return sellableType;
    }

    public void setSellableType(String sellableType) {
        this.sellableType = sellableType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice != null && unitPrice.signum() >= 0) {
            this.unitPrice = unitPrice;
        }
    }

    public BigDecimal getDiscountPerUnit() {
        return discountPerUnit;
    }

    public void setDiscountPerUnit(BigDecimal discountPerUnit) {
        if (discountPerUnit != null && discountPerUnit.signum() >= 0) {
            this.discountPerUnit = discountPerUnit;
        }
    }
}
