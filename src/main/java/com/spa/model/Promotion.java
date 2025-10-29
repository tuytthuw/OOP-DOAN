package com.spa.model;

import com.spa.interfaces.IEntity;
import com.spa.model.enums.DiscountType;
import java.time.LocalDate;

/**
 * Khuyến mãi áp dụng cho hóa đơn.
 */
public class Promotion implements IEntity {
    private static final String PREFIX = "PROF";

    private String promotionId;
    private String name;
    private String description;
    private DiscountType discountType;
    private double discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private double minPurchaseAmount;
    private boolean deleted;

    public Promotion() {
        this("", "", "", DiscountType.PERCENTAGE, 0.0,
                null, null, 0.0, false);
    }

    public Promotion(String promotionId,
                     String name,
                     String description,
                     DiscountType discountType,
                     double discountValue,
                     LocalDate startDate,
                     LocalDate endDate,
                     double minPurchaseAmount,
                     boolean deleted) {
        this.promotionId = promotionId;
        this.name = name;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minPurchaseAmount = minPurchaseAmount;
        this.deleted = deleted;
    }

    @Override
    public String getId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
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

    public double getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(double minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public void display() {
        System.out.println("---------------- THÔNG TIN KHUYẾN MÃI ----------------");
        System.out.printf("Mã khuyến mãi : %s%n", promotionId);
        System.out.printf("Tên chương trình: %s%n", name);
        System.out.printf("Mô tả          : %s%n", description == null || description.isEmpty() ? "(trống)" : description);
        System.out.printf("Loại giảm giá  : %s%n", discountType);
        System.out.printf("Giá trị giảm   : %.2f%n", discountValue);
        System.out.printf("Yêu cầu tối thiểu: %.2f%n", minPurchaseAmount);
        System.out.printf("Bắt đầu        : %s%n", startDate == null ? "N/A" : startDate);
        System.out.printf("Kết thúc       : %s%n", endDate == null ? "N/A" : endDate);
        System.out.println("Trạng thái     : " + (deleted ? "Đã khóa" : (isValid() ? "Đang hiệu lực" : "Hết hiệu lực")));
        System.out.println("-------------------------------------------------------");
    }

    @Override
    public void input() {
        // Nhập liệu ở tầng UI.
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Kiểm tra khuyến mãi còn hiệu lực tại thời điểm hiện tại.
     *
     * @return true nếu hợp lệ
     */
    public boolean isValid() {
        LocalDate now = LocalDate.now();
        boolean started = startDate == null || !now.isBefore(startDate);
        boolean notEnded = endDate == null || !now.isAfter(endDate);
        return started && notEnded && !deleted;
    }

    /**
     * Tính số tiền giảm áp dụng cho tổng hóa đơn.
     *
     * @param totalAmount tổng hóa đơn
     * @return giá trị giảm
     */
    public double calculateDiscount(double totalAmount) {
        if (totalAmount < minPurchaseAmount || !isValid()) {
            return 0.0;
        }
        if (discountType == DiscountType.PERCENTAGE) {
            return totalAmount * discountValue;
        }
        return discountValue;
    }
}
