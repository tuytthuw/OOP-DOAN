package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import Interfaces.IEntity;
import Interfaces.Sellable;
import enums.ServiceCategory;

/**
 * Lớp Service đại diện cho các dịch vụ spa (massage, chăm sóc da, trang điểm,
 * v.v.).
 * Lớp này quản lý tên, mô tả, giá cơ bản, thời gian thực hiện và loại dịch vụ.
 * 
 * Service implement cả IEntity (để quản lý entity chung) và Sellable (để quản
 * lý logic bán hàng).
 */
public class Service implements IEntity, Sellable {

    // ============ Thuộc tính (Attributes) ============
    private String serviceId; // ID duy nhất, định dạng: SVC_XXXXX
    private String serviceName; // Tên dịch vụ
    private BigDecimal basePrice; // Giá cơ bản (VND)
    private int durationMinutes; // Thời gian thực hiện (phút)
    private int bufferTime; // Thời gian đệm giữa các appointment (phút)
    private String description; // Mô tả chi tiết
    private ServiceCategory serviceCategory;// Loại dịch vụ
    private LocalDate createdDate; // Ngày thêm vào
    private boolean isActive; // Dịch vụ còn cung cấp không

    // ============ Hằng số ============
    public static final BigDecimal DEFAULT_MIN_PRICE = new BigDecimal("100000");
    public static final BigDecimal DEFAULT_MAX_PRICE = new BigDecimal("5000000");
    public static final int DEFAULT_MIN_DURATION = 15; // phút
    public static final int DEFAULT_MAX_DURATION = 240; // phút

    /**
     * Constructor khởi tạo Service với các thông tin đầy đủ.
     *
     * @param serviceId       ID duy nhất
     * @param serviceName     Tên dịch vụ
     * @param basePrice       Giá cơ bản
     * @param durationMinutes Thời gian thực hiện (phút)
     * @param bufferTime      Thời gian đệm (phút)
     * @param description     Mô tả chi tiết
     * @param serviceCategory Loại dịch vụ
     */
    public Service(String serviceId, String serviceName, BigDecimal basePrice, int durationMinutes,
            int bufferTime, String description, ServiceCategory serviceCategory) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.basePrice = basePrice;
        this.durationMinutes = durationMinutes;
        this.bufferTime = bufferTime;
        this.description = description;
        this.serviceCategory = serviceCategory;
        this.createdDate = LocalDate.now();
        this.isActive = true;
    }

    /**
     * Lấy thông tin chi tiết dịch vụ.
     *
     * @return Chuỗi chứa thông tin chi tiết
     */
    public String getServiceInfo() {
        return "ID Dịch Vụ: " + serviceId + "\n" +
                "Tên Dịch Vụ: " + serviceName + "\n" +
                "Giá Gốc: " + basePrice + "\n" +
                "Thời Lượng (phút): " + durationMinutes + "\n" +
                "Thời Gian Đệm (phút): " + bufferTime + "\n" +
                "Mô Tả: " + description + "\n" +
                "Ngày Tạo: " + createdDate + "\n" +
                "Trạng Thái: " + (isActive ? "Hoạt Động" : "Không Hoạt Động") + "\n" +
                "Loại Dịch Vụ: " + serviceCategory;
    }

    /**
     * Format thời gian dịch vụ dưới dạng chuỗi (vd: "1h 30m").
     *
     * @return Chuỗi thời gian được format
     */
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

    // ============ Implement IEntity ============

    /**
     * Lấy ID duy nhất của dịch vụ.
     *
     * @return ID dịch vụ
     */
    @Override
    public String getId() {
        return this.serviceId;
    }

    /**
     * Hiển thị thông tin dịch vụ ra console.
     */
    @Override
    public void display() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ ID: " + getId());
        System.out.println("│ Tên: " + serviceName);
        System.out.println("│ Giá: " + getPriceFormatted());
        System.out.println("│ Thời gian: " + getDurationFormatted());
        System.out.println("│ Loại: " + serviceCategory);
        System.out.println("│ Mô tả: " + getDescription());
        System.out.println("│ Trạng thái: " + (isActive ? "Hoạt động" : "Không hoạt động"));
        System.out.println("└─────────────────────────────────────────────┘");
    }

    /**
     * Nhập thông tin dịch vụ từ người dùng (để cài đặt trong tương lai).
     */
    @Override
    public void input() {
        // TODO: Implement nhập thông tin dịch vụ từ người dùng
    }

    /**
     * Lấy prefix cho ID (dùng để sinh ID tự động).
     *
     * @return Prefix cho Service là "SVC"
     */
    @Override
    public String getPrefix() {
        return "SVC";
    }

    // ============ Implement Sellable ============

    /**
     * Lấy giá bán của dịch vụ.
     *
     * @return Giá bán dưới dạng BigDecimal
     */
    @Override
    public BigDecimal getPrice() {
        return this.basePrice;
    }

    /**
     * Lấy giá bán dưới dạng chuỗi định dạng với đơn vị tiền tệ.
     * Định dạng: "500.000 VND" (dấu phân cách hàng nghìn là dấu chấm)
     *
     * @return Giá dạng chuỗi định dạng
     */
    @Override
    public String getPriceFormatted() {
        // Format: "500.000 VND"
        return String.format("%,d VND", this.basePrice.longValue()).replace(",", ".");
    }

    /**
     * Kiểm tra xem dịch vụ có khả dụng (có thể bán) không.
     * Dịch vụ khả dụng nếu isActive = true.
     *
     * @return true nếu dịch vụ hoạt động, false nếu không
     */
    @Override
    public boolean isAvailable() {
        return this.isActive;
    }

    /**
     * Lấy mô tả chi tiết của dịch vụ.
     *
     * @return Chuỗi mô tả
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    // ============ Getter / Setter ============

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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Service service = (Service) o;
        return Objects.equals(serviceId, service.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId);
    }
}
