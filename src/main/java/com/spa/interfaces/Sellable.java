package com.spa.interfaces;

import java.math.BigDecimal;

/**
 * Đại diện cho bất kỳ đối tượng nào có thể bán được.
 */
public interface Sellable {

    /**
     * Lấy giá gốc của mặt hàng.
     *
     * @return giá gốc
     */
    BigDecimal getBasePrice();

    /**
     * Tính giá cuối cùng sau ưu đãi.
     *
     * @return giá đã điều chỉnh
     */
    BigDecimal calculateFinalPrice();

    /**
     * Lấy tên loại mặt hàng.
     *
     * @return loại mặt hàng
     */
    String getType();
}
