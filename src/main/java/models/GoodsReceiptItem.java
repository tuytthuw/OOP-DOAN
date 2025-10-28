package models;

import java.math.BigDecimal;

public class GoodsReceiptItem {
    private String productId;
    private String productName;
    private int quantity;
    private BigDecimal purchasePrice;

    public GoodsReceiptItem(String productId,
                            String productName,
                            int quantity,
                            BigDecimal purchasePrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = Math.max(0, quantity);
        this.purchasePrice = purchasePrice == null ? BigDecimal.ZERO : purchasePrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        if (purchasePrice != null && purchasePrice.signum() >= 0) {
            this.purchasePrice = purchasePrice;
        }
    }

    public BigDecimal calculateLineTotal() {
        return purchasePrice.multiply(BigDecimal.valueOf(quantity));
    }
}
