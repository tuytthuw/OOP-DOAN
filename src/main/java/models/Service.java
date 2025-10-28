package models;

import interfaces.IEntity;
import interfaces.Sellable;
import enums.ServiceCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Service implements IEntity, Sellable {
    private static final String ID_PREFIX = "SER";
    private static int runningNumber = 1;

    private final String serviceId;
    private String serviceName;
    private String description;
    private BigDecimal basePrice;
    private int durationMinutes;
    private ServiceCategory category;
    private boolean isActive;
    private final LocalDateTime createdAt;

    public Service(String serviceName,
                   String description,
                   BigDecimal basePrice,
                   int durationMinutes,
                   ServiceCategory category) {
        this.serviceId = generateServiceId();
        this.serviceName = serviceName;
        this.description = description;
        this.basePrice = basePrice == null ? BigDecimal.ZERO : basePrice;
        this.durationMinutes = Math.max(0, durationMinutes);
        this.category = category;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    private String generateServiceId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    @Override
    public String getId() {
        return serviceId;
    }

    @Override
    public BigDecimal getPrice() {
        return basePrice;
    }

    @Override
    public String getDescription() {
        return description == null ? serviceName : description;
    }

    @Override
    public boolean isAvailable() {
        return isActive;
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice != null && newPrice.signum() >= 0) {
            this.basePrice = newPrice;
        }
    }

    public void toggleActive(boolean active) {
        this.isActive = active;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRawDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = Math.max(0, durationMinutes);
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }
}
