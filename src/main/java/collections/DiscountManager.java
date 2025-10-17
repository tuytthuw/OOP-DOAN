package collections;

import java.time.LocalDate;

import enums.DiscountType;
import models.Discount;

/**
 * Manager quản lý danh sách chiết khấu.
 * Cung cấp các phương thức tìm kiếm cụ thể cho Discount.
 */
public class DiscountManager extends BaseManager<Discount> {

    /**
     * Constructor khởi tạo DiscountManager.
     */
    public DiscountManager() {
        super(Discount.class);
    }

    /**
     * Lấy tất cả chiết khấu.
     * 
     * @return Mảy chứa tất cả chiết khấu hiện tại
     */
    @Override
    public Discount[] getAll() {
        // Tạo mảy Discount mới và copy từng phần tử một
        Discount[] result = new Discount[size];
        for (int i = 0; i < size; i++) {
            // Các phần tử đã là Discount nên không cần cast
            result[i] = items[i];
        }
        return result;
    }

    /**
     * Tìm chiết khấu theo mã code.
     * 
     * @param discountCode Mã chiết khấu cần tìm
     * @return Chiết khấu nếu tìm thấy, null nếu không
     */
    public Discount findByCode(String discountCode) {
        if (discountCode == null || discountCode.isEmpty()) {
            return null;
        }

        for (int i = 0; i < size; i++) {
            if (items[i].getId().equals(discountCode)) {
                return items[i];
            }
        }
        return null;
    }

    /**
     * Lấy tất cả chiết khấu hoạt động (startDate <= hôm nay <= endDate).
     * 
     * @return Mảy chứa tất cả chiết khấu hoạt động
     */
    public Discount[] findActiveDiscounts() {
        LocalDate today = LocalDate.now();

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].isValidForDate(today)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Discount[] result = new Discount[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].isValidForDate(today)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm tất cả chiết khấu theo loại (PERCENTAGE hoặc FIXED_AMOUNT).
     * 
     * @param type Loại chiết khấu cần tìm
     * @return Mảy chứa tất cả chiết khấu có loại đó
     */
    public Discount[] findByType(DiscountType type) {
        if (type == null) {
            return new Discount[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getType() == type) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Discount[] result = new Discount[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getType() == type) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Lấy tất cả chiết khấu hợp lệ cho một ngày cụ thể.
     * 
     * @param date Ngày cần kiểm tra
     * @return Mảy chứa tất cả chiết khấu hợp lệ cho ngày đó
     */
    public Discount[] findValidDiscounts(LocalDate date) {
        if (date == null) {
            return new Discount[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].isValidForDate(date)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Discount[] result = new Discount[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].isValidForDate(date)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm chiết khấu theo pattern mã (tìm kiếm chứa chuỗi).
     * 
     * @param pattern Chuỗi tìm kiếm
     * @return Mảy chứa tất cả chiết khấu có mã chứa pattern
     */
    public Discount[] findDiscountsByCodePattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return new Discount[0];
        }

        String searchPattern = pattern.toLowerCase();

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getId().toLowerCase().contains(searchPattern)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Discount[] result = new Discount[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getId().toLowerCase().contains(searchPattern)) {
                result[index++] = items[i];
            }
        }

        return result;
    }
}
