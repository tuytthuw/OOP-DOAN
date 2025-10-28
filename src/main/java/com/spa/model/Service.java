package com.spa.model;

import com.spa.interfaces.IEntity;
import com.spa.interfaces.Sellable;
import com.spa.model.enums.ServiceCategory;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Dịch vụ được cung cấp tại spa.
 */
public class Service implements IEntity, Sellable {
    private static final String PREFIX = "SER";

    private String serviceId;
    private String serviceName;
    private BigDecimal basePrice;
    private int durationMinutes;
    private int bufferTime;
    private String description;
    private LocalDate createdDate;
    private boolean active;
    private ServiceCategory category;
    private boolean deleted;

    public Service() {
        this("", "", BigDecimal.ZERO, 0, 0, "", null, true, ServiceCategory.MASSAGE, false);
    }

    public Service(String serviceId,
                   String serviceName,
                   BigDecimal basePrice,
                   int durationMinutes,
                   int bufferTime,
                   String description,
                   LocalDate createdDate,
                   boolean active,
                   ServiceCategory category,
                   boolean deleted) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.basePrice = basePrice;
        this.durationMinutes = durationMinutes;
        this.bufferTime = bufferTime;
        this.description = description;
        this.createdDate = createdDate;
        this.active = active;
        this.category = category;
        this.deleted = deleted;
    }

    @Override
    public String getId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public void display() {
        // Sẽ hiển thị ở tầng UI.
    }

    @Override
    public void input() {
        // Sẽ nhập liệu ở tầng UI.
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public BigDecimal calculateFinalPrice() {
        return basePrice;
    }

    @Override
    public String getType() {
        return "SERVICE";
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getBufferTime() {
        return bufferTime;
    }

    public void setBufferTime(int bufferTime) {
        this.bufferTime = bufferTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
