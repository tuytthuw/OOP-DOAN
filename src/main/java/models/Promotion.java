package models;

import enums.DiscountType;
import interfaces.IEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Promotion implements IEntity {
    private static final String ID_PREFIX = "PRO_M";
    private static int runningNumber = 1;

    private final String promotionId;
    private String code;
    private String description;
    private DiscountType type;
    private BigDecimal value;
    private BigDecimal minOrderTotal;
    private int usageLimit;
    private int usedCount;
    private LocalDate validFrom;
    private LocalDate validTo;
    private boolean active;

    public Promotion(String code,
                     String description,
                     DiscountType type,
                     BigDecimal value,
                     BigDecimal minOrderTotal,
                     LocalDate validFrom,
                     LocalDate validTo,
                     int usageLimit) {
        this.promotionId = generateId();
        this.code = code;
        this.description = description;
        this.type = type;
        this.value = value == null ? BigDecimal.ZERO : value;
        this.minOrderTotal = minOrderTotal == null ? BigDecimal.ZERO : minOrderTotal;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.usageLimit = Math.max(0, usageLimit);
        this.usedCount = 0;
        this.active = true;
    }

    private String generateId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    @Override
    public String getId() {
        return promotionId;
    }

    public boolean isApplicable(BigDecimal subtotal, LocalDateTime at) {
        if (!active || subtotal == null || at == null) {
            return false;
        }
        if (subtotal.compareTo(minOrderTotal) < 0) {
            return false;
        }
        LocalDate date = at.toLocalDate();
        boolean withinDate = (validFrom == null || !date.isBefore(validFrom))
                && (validTo == null || !date.isAfter(validTo));
        if (!withinDate) {
            return false;
        }
        return usageLimit == 0 || usedCount < usageLimit;
    }

    public void incrementUsage() {
        usedCount++;
    }

    public void deactivate() {
        active = false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        if (value != null && value.signum() >= 0) {
            this.value = value;
        }
    }

    public BigDecimal getMinOrderTotal() {
        return minOrderTotal;
    }

    public void setMinOrderTotal(BigDecimal minOrderTotal) {
        if (minOrderTotal != null && minOrderTotal.signum() >= 0) {
            this.minOrderTotal = minOrderTotal;
        }
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public boolean isActive() {
        return active;
    }
}
