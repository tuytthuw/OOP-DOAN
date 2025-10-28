package com.spa.model;

import com.spa.interfaces.IEntity;
import com.spa.interfaces.Sellable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Sản phẩm bán kèm tại spa.
 */
public class Product implements IEntity, Sellable {
    private static final String PREFIX = "PRO";

    private String productId;
    private String productName;
    private String brand;
    private BigDecimal basePrice;
    private double costPrice;
    private String unit;
    private Supplier supplier;
    private int stockQuantity;
    private LocalDate expiryDate;
    private boolean deleted;
    private int reorderLevel;

    public Product() {
        this("", "", "", BigDecimal.ZERO, 0.0, "", null, 0, null, false, 0);
    }

    public Product(String productId,
                   String productName,
                   String brand,
                   BigDecimal basePrice,
                   double costPrice,
                   String unit,
                   Supplier supplier,
                   int stockQuantity,
                   LocalDate expiryDate,
                   boolean deleted,
                   int reorderLevel) {
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.basePrice = basePrice;
        this.costPrice = costPrice;
        this.unit = unit;
        this.supplier = supplier;
        this.stockQuantity = stockQuantity;
        this.expiryDate = expiryDate;
        this.deleted = deleted;
        this.reorderLevel = reorderLevel;
    }

    @Override
    public String getId() {
        return productId;
    }

    /**
     * Thiết lập mã sản phẩm.
     *
     * @param productId mã sản phẩm
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Lấy tên sản phẩm.
     *
     * @return tên sản phẩm
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Thiết lập tên sản phẩm.
     *
     * @param productName tên sản phẩm
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Lấy thương hiệu sản phẩm.
     *
     * @return thương hiệu
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Thiết lập thương hiệu sản phẩm.
     *
     * @param brand thương hiệu
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public void display() {
        // Xử lý ở tầng UI.
    }

    @Override
    public void input() {
        // Xử lý ở tầng UI.
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    /**
     * Thiết lập giá bán cơ bản.
     *
     * @param basePrice giá bán cơ bản
     */
    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public BigDecimal calculateFinalPrice() {
        return basePrice;
    }

    @Override
    public String getType() {
        return "PRODUCT";
    }

    /**
     * Lấy giá vốn.
     *
     * @return giá vốn
     */
    public double getCostPrice() {
        return costPrice;
    }

    /**
     * Thiết lập giá vốn.
     *
     * @param costPrice giá vốn
     */
    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    /**
     * Lấy đơn vị tính.
     *
     * @return đơn vị
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Thiết lập đơn vị tính.
     *
     * @param unit đơn vị
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Lấy nhà cung cấp.
     *
     * @return nhà cung cấp
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Thiết lập nhà cung cấp.
     *
     * @param supplier nhà cung cấp
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     * Lấy số lượng tồn kho.
     *
     * @return số lượng tồn
     */
    public int getStockQuantity() {
        return stockQuantity;
    }

    /**
     * Thiết lập số lượng tồn kho.
     *
     * @param stockQuantity số lượng tồn
     */
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Lấy ngày hết hạn.
     *
     * @return ngày hết hạn
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * Thiết lập ngày hết hạn.
     *
     * @param expiryDate ngày hết hạn
     */
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Kiểm tra trạng thái xóa mềm.
     *
     * @return true nếu đã xóa mềm
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Thiết lập trạng thái xóa mềm.
     *
     * @param deleted trạng thái xóa
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Lấy ngưỡng tái đặt hàng.
     *
     * @return ngưỡng tồn kho
     */
    public int getReorderLevel() {
        return reorderLevel;
    }

    /**
     * Thiết lập ngưỡng tái đặt hàng.
     *
     * @param reorderLevel ngưỡng tồn kho
     */
    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    /**
     * Cập nhật số lượng tồn kho.
     *
     * @param delta biến động số lượng
     */
    public void updateStock(int delta) {
        stockQuantity += delta;
    }

    /**
     * Kiểm tra sản phẩm đã hết hạn.
     *
     * @return true nếu đã quá hạn
     */
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    /**
     * Kiểm tra trạng thái cần đặt hàng.
     *
     * @return true nếu tồn kho dưới ngưỡng
     */
    public boolean checkReorderStatus() {
        return stockQuantity <= reorderLevel;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + productId + '\'' +
                ", name='" + productName + '\'' +
                ", stock=" + stockQuantity +
                '}';
    }
}
