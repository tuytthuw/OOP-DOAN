package interfaces;

import java.math.BigDecimal;

/**
 * Interface Sellable định nghĩa contract cho các đối tượng có thể bán được
 * trong hệ thống spa.
 * 
 * Các lớp implement interface này phải cung cấp các phương thức để quản lý giá,
 * kiểm tra khả dụng và lấy mô tả chi tiết của item có thể bán.
 * 
 * Lưu ý: Các phương thức getId(), display(), input() đã được định nghĩa trong
 * IEntity,
 * nên Sellable chỉ tập trung vào các phương thức riêng cho việc bán hàng.
 */
public interface Sellable {

    /**
     * Lấy giá bán của item.
     * 
     * @return Giá bán dưới dạng BigDecimal
     */
    BigDecimal getPrice();

    /**
     * Lấy giá bán dưới dạng chuỗi định dạng với đơn vị tiền tệ.
     * Định dạng: "500.000 VND" (với dấu phân cách hàng nghìn là dấu chấm)
     * 
     * @return Giá dạng chuỗi định dạng
     */
    String getPriceFormatted();

    /**
     * Kiểm tra xem item có khả dụng (có thể bán) không.
     * 
     * Logic khác nhau tùy theo loại item:
     * - Service: kiểm tra isActive
     * - Product: kiểm tra (stockQuantity > 0 && !isExpired() && isActive)
     * 
     * @return true nếu item khả dụng, false nếu không
     */
    boolean isAvailable();

    /**
     * Lấy mô tả chi tiết của item.
     * 
     * @return Chuỗi mô tả chi tiết
     */
    String getDescription();
}
