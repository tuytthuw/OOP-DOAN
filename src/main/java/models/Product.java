package models;

import interfaces.IEntity;
import interfaces.Sellable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product implements IEntity, Sellable {
    private static final String ID_PREFIX = "PRO";
    private static int runningNumber = 1;

    private final String productId;
    private String productName;
    private String description;
    private BigDecimal basePrice;
    private int stockQuantity;
    private int reorderLevel;
    private boolean isActive;
    private LocalDateTime lastUpdated;

    public Product(String productName,
                   String description,
                   BigDecimal basePrice,
                   int initialStock,
                   int reorderLevel) {
        this.productId = generateProductId();
        this.productName = productName;
        this.description = description;
        this.basePrice = basePrice == null ? BigDecimal.ZERO : basePrice;
        this.stockQuantity = Math.max(0, initialStock);
        this.reorderLevel = Math.max(0, reorderLevel);
        this.isActive = true;
        this.lastUpdated = LocalDateTime.now();
    }

    private String generateProductId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    @Override
    public String getId() {
        return productId;
    }

    @Override
    public BigDecimal getPrice() {
        return basePrice;
    }

    @Override
    public String getDescription() {
        return description == null ? productName : description;
    }

    @Override
    public boolean isAvailable() {
        return isActive && stockQuantity > 0;
    }

    public void adjustStock(int delta) {
        int next = stockQuantity + delta;
        if (next < 0) {
            return;
        }
        stockQuantity = next;
        lastUpdated = LocalDateTime.now();
    }

    public boolean needsRestock() {
        return stockQuantity <= reorderLevel;
    }

    public void toggleActive(boolean active) {
        this.isActive = active;
        lastUpdated = LocalDateTime.now();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
        lastUpdated = LocalDateTime.now();
    }

    public String getRawDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        lastUpdated = LocalDateTime.now();
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int quantity) {
        if (quantity >= 0) {
            this.stockQuantity = quantity;
            lastUpdated = LocalDateTime.now();
        }
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        if (reorderLevel >= 0) {
            this.reorderLevel = reorderLevel;
            lastUpdated = LocalDateTime.now();
        }
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setBasePrice(BigDecimal newPrice) {
        if (newPrice != null && newPrice.signum() >= 0) {
            this.basePrice = newPrice;
            lastUpdated = LocalDateTime.now();
        }
    }

    public boolean isActive() {
        return isActive;
    }
}
