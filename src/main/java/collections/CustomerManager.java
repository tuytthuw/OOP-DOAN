package collections;

import enums.Tier;
import models.Customer;

/**
 * Manager quản lý danh sách khách hàng.
 * Cung cấp các phương thức tìm kiếm cụ thể cho Customer.
 */
public class CustomerManager extends BaseManager<Customer> {

    /**
     * Tìm khách hàng theo số điện thoại.
     * 
     * @param phoneNumber Số điện thoại cần tìm
     * @return Khách hàng nếu tìm thấy, null nếu không
     */
    public Customer findByPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return null;
        }

        for (int i = 0; i < size; i++) {
            if (items[i].getPhoneNumber().equals(phoneNumber)) {
                return items[i];
            }
        }
        return null;
    }

    /**
     * Tìm khách hàng theo email.
     * 
     * @param email Email cần tìm
     * @return Khách hàng nếu tìm thấy, null nếu không
     */
    public Customer findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }

        for (int i = 0; i < size; i++) {
            if (items[i].getEmail().equals(email)) {
                return items[i];
            }
        }
        return null;
    }

    /**
     * Tìm tất cả khách hàng thuộc một tier nhất định.
     * 
     * @param tier Tier cần tìm
     * @return Mảy chứa tất cả khách hàng có tier đó (mảy trống nếu không tìm thấy)
     */
    public Customer[] findByTier(Tier tier) {
        if (tier == null) {
            return new Customer[0];
        }

        // Đếm số lượng khách hàng có tier này
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getMemberTier() == tier) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Customer[] result = new Customer[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getMemberTier() == tier) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Lấy tất cả khách hàng hoạt động (chưa xóa).
     * 
     * @return Mảy chứa tất cả khách hàng hoạt động
     */
    public Customer[] findActiveCustomers() {
        // Đếm số lượng khách hàng hoạt động
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (!items[i].isDeleted()) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Customer[] result = new Customer[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (!items[i].isDeleted()) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Lấy top N khách hàng chi tiêu nhiều nhất (theo totalSpent).
     * 
     * @param limit Số lượng khách hàng muốn lấy
     * @return Mảy chứa top N khách hàng (sắp xếp giảm dần theo chi tiêu)
     */
    public Customer[] getTopSpenders(int limit) {
        if (size == 0 || limit <= 0) {
            return new Customer[0];
        }

        // Sao chép mảy hiện tại
        Customer[] temp = new Customer[size];
        for (int i = 0; i < size; i++) {
            temp[i] = items[i];
        }

        // Sắp xếp giảm dần theo chi tiêu (Bubble Sort)
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (temp[j].getTotalSpent().compareTo(
                        temp[j + 1].getTotalSpent()) < 0) {
                    // Hoán đổi
                    Customer tempCustomer = temp[j];
                    temp[j] = temp[j + 1];
                    temp[j + 1] = tempCustomer;
                }
            }
        }

        // Lấy top N
        int resultSize = Math.min(limit, size);
        Customer[] result = new Customer[resultSize];
        for (int i = 0; i < resultSize; i++) {
            result[i] = temp[i];
        }

        return result;
    }
}
