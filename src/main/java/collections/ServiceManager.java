package collections;

import java.math.BigDecimal;

import enums.ServiceCategory;
import models.Service;

/**
 * Manager quản lý danh sách dịch vụ.
 * Cung cấp các phương thức tìm kiếm cụ thể cho Service.
 */
public class ServiceManager extends BaseManager<Service> {

    /**
     * Lấy tất cả dịch vụ.
     * 
     * @return Mảy chứa tất cả dịch vụ hiện tại
     */
    @Override
    public Service[] getAll() {
        // Tạo mảy Service mới và copy từng phần tử một
        Service[] result = new Service[size];
        for (int i = 0; i < size; i++) {
            // Cast từng phần tử từ IEntity sang Service
            result[i] = (Service) items[i];
        }
        return result;
    }

    /**
     * Tìm tất cả dịch vụ thuộc một category nhất định.
     * 
     * @param category Category cần tìm
     * @return Mảy chứa tất cả dịch vụ có category đó
     */
    public Service[] findByCategory(ServiceCategory category) {
        if (category == null) {
            return new Service[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getServiceCategory() == category) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Service[] result = new Service[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getServiceCategory() == category) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm dịch vụ theo tên (chứa chuỗi, không phân biệt hoa thường).
     * 
     * @param name Tên dịch vụ cần tìm
     * @return Mảy chứa tất cả dịch vụ chứa chuỗi này
     */
    public Service[] findByName(String name) {
        if (name == null || name.isEmpty()) {
            return new Service[0];
        }

        String searchName = name.toLowerCase();

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getServiceName().toLowerCase().contains(searchName)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Service[] result = new Service[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getServiceName().toLowerCase().contains(searchName)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Lấy tất cả dịch vụ hoạt động (isActive = true).
     * 
     * @return Mảy chứa tất cả dịch vụ hoạt động
     */
    public Service[] findActiveServices() {
        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].isActive()) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Service[] result = new Service[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].isActive()) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm dịch vụ trong khoảng giá nhất định.
     * 
     * @param minPrice Giá tối thiểu
     * @param maxPrice Giá tối đa
     * @return Mảy chứa tất cả dịch vụ trong khoảng giá
     */
    public Service[] findByPriceRange(BigDecimal minPrice,
            BigDecimal maxPrice) {
        if (minPrice == null || maxPrice == null) {
            return new Service[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            BigDecimal price = items[i].getPrice();
            if (price.compareTo(minPrice) >= 0 &&
                    price.compareTo(maxPrice) <= 0) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Service[] result = new Service[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            BigDecimal price = items[i].getPrice();
            if (price.compareTo(minPrice) >= 0 &&
                    price.compareTo(maxPrice) <= 0) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm dịch vụ theo khoảng thời gian (duration tính bằng phút).
     * 
     * @param minDuration Thời gian tối thiểu (phút)
     * @param maxDuration Thời gian tối đa (phút)
     * @return Mảy chứa tất cả dịch vụ trong khoảng thời gian
     */
    public Service[] findByDurationRange(int minDuration, int maxDuration) {
        if (minDuration < 0 || maxDuration < 0 || minDuration > maxDuration) {
            return new Service[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            int duration = items[i].getDurationMinutes();
            if (duration >= minDuration && duration <= maxDuration) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Service[] result = new Service[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            int duration = items[i].getDurationMinutes();
            if (duration >= minDuration && duration <= maxDuration) {
                result[index++] = items[i];
            }
        }

        return result;
    }
}
