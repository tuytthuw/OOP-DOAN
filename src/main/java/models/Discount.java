package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import interfaces.IEntity;
import enums.DiscountType;

/**
 * Lớp Discount đại diện cho các chương trình khuyến mãi/chiết khấu.
 * Hỗ trợ hai loại chiết khấu: theo phần trăm (%) và theo số tiền cố định.
 * Implement IEntity để quản lý thông tin entity chung.
 */
public class Discount implements IEntity {
    // ============ Thuộc tính (Attributes) ============
    private String discountId; // ID duy nhất, định dạng: DISC_XXXXX
    private String discountCode; // Mã khuyến mãi (VD: SUMMER2024)
    private String description; // Mô tả chiết khấu
    private DiscountType type; // Loại: PERCENTAGE hoặc FIXED_AMOUNT
    private BigDecimal value; // Giá trị: nếu % thì 0-100, nếu tiền thì số tiền
    private BigDecimal minAmount; // Tối thiểu hóa đơn để áp dụng chiết khấu
    private BigDecimal maxDiscount; // Chiết khấu tối đa (tùy chọn)
    private LocalDate startDate; // Ngày bắt đầu có hiệu lực
    private LocalDate endDate; // Ngày kết thúc có hiệu lực
    private int usageLimit; // Giới hạn số lần sử dụng (-1 = không giới hạn)
    private int usageCount; // Số lần đã sử dụng
    private boolean isActive; // Chiết khấu có hoạt động không

    // ============ Hằng số (Constants) ============
    public static final int UNLIMITED = -1;

    // ============ Constructor ============
    public Discount() {
    }

    /**
     * Constructor khởi tạo Discount với đầy đủ thông tin.
     */
    public Discount(String discountId, String discountCode, String description, DiscountType type,
            BigDecimal value, LocalDate startDate, LocalDate endDate) {
        this.discountId = discountId;
        this.discountCode = discountCode;
        this.description = description;
        this.type = type;
        this.value = value;
        this.minAmount = BigDecimal.ZERO;
        this.maxDiscount = null;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = UNLIMITED;
        this.usageCount = 0;
        this.isActive = true;
    }

    // Tính toán số tiền giảm giá dựa trên loại chiết khấu và các điều kiện liên
    // quan.
    public BigDecimal calculateDiscount(BigDecimal originalAmount) {
        // Kiểm tra chiết khấu có hợp lệ không
        if (!canUse() || originalAmount.compareTo(minAmount == null ? BigDecimal.ZERO : minAmount) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountAmount = BigDecimal.ZERO;

        // Nếu là PERCENTAGE, tính toán dựa trên phần trăm và kiểm tra maxDiscount
        if (type == DiscountType.PERCENTAGE) {
            discountAmount = originalAmount.multiply(value.divide(new BigDecimal("100")));
            if (maxDiscount != null && discountAmount.compareTo(maxDiscount) > 0) {
                discountAmount = maxDiscount;
            }
        }
        // Nếu là FIXED_AMOUNT, trả về giá trị cố định
        else if (type == DiscountType.FIXED_AMOUNT) {
            discountAmount = value;
        }

        // Đảm bảo số tiền giảm giá không lớn hơn số tiền gốc
        return discountAmount.compareTo(originalAmount) > 0 ? originalAmount : discountAmount;
    }

    /**
     * Kiểm tra xem một ngày cụ thể có nằm trong khoảng thời gian hiệu lực.
     *
     * @param date Ngày cần kiểm tra
     * @return true nếu ngày hợp lệ, false nếu không
     */
    public boolean isValidForDate(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Kiểm tra xem chiết khấu có thể được sử dụng hay không.
     *
     * @return true nếu có thể sử dụng, false nếu không
     */
    public boolean canUse() {
        if (!isActive || !isValidForDate(LocalDate.now())) {
            return false;
        }
        if (usageLimit != UNLIMITED && usageCount >= usageLimit) {
            return false;
        }
        return true;
    }

    /**
     * Tăng số lần sử dụng chiết khấu lên 1.
     */
    public void incrementUsage() {
        if (usageLimit == UNLIMITED || usageCount < usageLimit) {
            usageCount++;
        }
    }

    // ============ IEntity Implementation ============

    @Override
    public String getId() {
        return discountId;
    }

    @Override
    public void display() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ ID: " + getId());
        System.out.println("│ Mã khuyến mãi: " + discountCode);
        System.out.println("│ Mô tả: " + description);
        System.out.println("│ Loại: " + type);
        System.out.println("│ Giá trị: " + value);
        System.out.println("│ Tối thiểu: " + (minAmount == null ? "Không" : minAmount));
        System.out.println("│ Tối đa: " + (maxDiscount == null ? "Không" : maxDiscount));
        System.out.println("│ Kỳ hạn: " + startDate + " đến " + endDate);
        System.out.println("│ Giới hạn: " + (usageLimit == UNLIMITED ? "Không giới hạn" : usageLimit));
        System.out.println("│ Lượt sử dụng: " + usageCount);
        System.out.println("│ Trạng thái: " + (isActive ? "Hoạt động" : "Không hoạt động"));
        System.out.println("└─────────────────────────────────────────────┘");
    }

    @Override
    public void input() {
        // TODO: Implement nhập thông tin chiết khấu từ người dùng
    }

    @Override
    public String getPrefix() {
        return "DISC";
    }

    // ============ Getter & Setter ============

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
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
        this.value = value;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // ============ equals() & hashCode() ============

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Discount))
            return false;
        Discount discount = (Discount) o;
        return Objects.equals(discountId, discount.discountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountId);
    }
}