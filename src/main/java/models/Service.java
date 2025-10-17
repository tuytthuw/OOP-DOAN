package models;

import Interfaces.Sellable;
import enums.ServiceCategory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public class Service implements Sellable {

    private String serviceId;
    private String serviceName;
    private BigDecimal basePrice;
    private int durationMinutes;
    private int bufferTime;
    private String description;
    private LocalDate createdDate;
    private boolean isActive;
    private ServiceCategory serviceCategory;

    public static final BigDecimal DEFAULT_MIN_PRICE = new BigDecimal("100000");
    public static final BigDecimal DEFAULT_MAX_PRICE = new BigDecimal("5000000");
    public static final int DEFAULT_MIN_DURATION = 15; // phút
    public static final int DEFAULT_MAX_DURATION = 240; // phút


    public Service(String serviceId, String serviceName, BigDecimal basePrice, int durationMinutes, int bufferTime, String description, LocalDate createdDate, boolean isActive, ServiceCategory serviceCategory) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.basePrice = basePrice;
        this.durationMinutes = durationMinutes;
        this.bufferTime = bufferTime;
        this.description = description;
        this.createdDate = LocalDate.now();
        this.isActive = true;
        this.serviceCategory = serviceCategory;
    }

    public String getServiceInfo() {
        return "ID Dịch Vụ: " + serviceId + "\n" +
                "Tên Dịch Vụ: " + serviceName + "\n" +
                "Giá Gốc: " + basePrice + "\n" +
                "Thời Lượng (phút): " + durationMinutes + "\n" +
                "Thời Gian Thực Hiện (phút): " + bufferTime + "\n" +
                "Mô Tả: " + description + "\n" +
                "Ngày Tạo: " + createdDate + "\n" +
                "Trạng Thái: " + (isActive ? "Hoạt Động" : "Không Hoạt Động") + "\n" +
                "Loại Dịch Vụ: " + serviceCategory;
    }

    public String getPriceFormatted() {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormatter.format(basePrice);
    }

    public String getDurationFormatted() {
        if (durationMinutes <= 0) {
            return "Không xác định";
        }
        long hours = durationMinutes / 60;
        long minutes = durationMinutes % 60;
        if (hours > 0 && minutes > 0) {
            return hours + " giờ " + minutes + " phút";
        } else if (hours > 0) {
            return hours + " giờ";
        } else {
            return minutes + " phút";
        }
    }

    @Override
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    @Override
    public BigDecimal calculateFinalPrice() {
        // Trong trường hợp của Service, giá cuối cùng chính là giá cơ bản.
        // Logic phức tạp hơn (ví dụ: phụ thu) có thể được thêm vào đây.
        return getBasePrice();
    }

    @Override
    public String getType() {
        return "SERVICE";
    }

    public String getServiceId() {
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

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
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
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ServiceCategory getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(ServiceCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(serviceId, service.serviceId);    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId);    }
}
