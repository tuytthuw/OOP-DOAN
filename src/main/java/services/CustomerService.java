package services;

import java.math.BigDecimal;
import java.time.LocalDate;

import collections.CustomerManager;
import enums.Tier;
import models.Customer;

/**
 * Service quản lý logic nghiệp vụ khách hàng.
 * Xử lý các thao tác như đăng ký, cập nhật, và quản lý tier membership.
 */
public class CustomerService {

    private CustomerManager customerManager;

    /**
     * Constructor khởi tạo CustomerService.
     *
     * @param customerManager Manager quản lý khách hàng
     */
    public CustomerService(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    /**
     * Đăng ký khách hàng mới.
     *
     * @param fullName    Tên đầy đủ
     * @param phoneNumber Số điện thoại
     * @param email       Email
     * @param address     Địa chỉ
     * @param isMale      Giới tính
     * @param birthDate   Ngày sinh
     * @return Khách hàng vừa tạo hoặc null nếu thất bại
     */
    public Customer registerNewCustomer(String fullName, String phoneNumber, String email,
            String address, boolean isMale, LocalDate birthDate) {
        // Kiểm tra duplicate số điện thoại
        if (customerManager.findByPhone(phoneNumber) != null) {
            System.out.println("❌ Số điện thoại đã tồn tại!");
            return null;
        }

        // Kiểm tra duplicate email
        if (!email.isEmpty() && customerManager.findByEmail(email) != null) {
            System.out.println("❌ Email đã tồn tại!");
            return null;
        }

        // Sinh ID mới
        String customerId = generateCustomerId();

        // Tạo khách hàng mới với tier mặc định BRONZE
        Customer newCustomer = new Customer(
                customerId,
                fullName,
                phoneNumber,
                isMale,
                birthDate,
                email,
                address,
                Tier.BRONZE,
                LocalDate.now(),
                null,
                true,
                BigDecimal.ZERO);

        // Thêm vào Manager
        if (customerManager.add(newCustomer)) {
            System.out.println("✅ Đăng ký khách hàng thành công!");
            return newCustomer;
        }

        System.out.println("❌ Lỗi khi thêm khách hàng!");
        return null;
    }

    /**
     * Cập nhật thông tin khách hàng.
     *
     * @param customerId  ID khách hàng
     * @param fullName    Tên đầy đủ
     * @param phoneNumber Số điện thoại
     * @param email       Email
     * @param address     Địa chỉ
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateCustomerInfo(String customerId, String fullName, String phoneNumber,
            String email, String address) {
        // Lấy khách hàng theo ID
        Customer customer = customerManager.getById(customerId);
        if (customer == null) {
            System.out.println("❌ Khách hàng không tồn tại!");
            return false;
        }

        // Kiểm tra số điện thoại không bị trùng (ngoại trừ chính khách hàng này)
        if (!customer.getPhoneNumber().equals(phoneNumber)) {
            if (customerManager.findByPhone(phoneNumber) != null) {
                System.out.println("❌ Số điện thoại đã tồn tại!");
                return false;
            }
        }

        // Kiểm tra email không bị trùng (ngoại trừ chính khách hàng này)
        if (!email.isEmpty() && !customer.getEmail().equals(email)) {
            if (customerManager.findByEmail(email) != null) {
                System.out.println("❌ Email đã tồn tại!");
                return false;
            }
        }

        // Cập nhật thông tin
        customer.setFullName(fullName);
        customer.setPhoneNumber(phoneNumber);
        customer.setEmail(email);
        customer.setAddress(address);

        // Cập nhật lại trong Manager
        if (customerManager.update(customer)) {
            System.out.println("✅ Cập nhật thông tin khách hàng thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi cập nhật khách hàng!");
        return false;
    }

    /**
     * Cập nhật tier khách hàng dựa trên tổng chi tiêu.
     *
     * @param customerId ID khách hàng
     */
    public void updateCustomerTier(String customerId) {
        Customer customer = customerManager.getById(customerId);
        if (customer == null) {
            System.out.println("❌ Khách hàng không tồn tại!");
            return;
        }

        // Gọi phương thức updateTier() của Customer (logic trong model)
        customer.updateTier();

        // Cập nhật lại trong Manager
        customerManager.update(customer);
    }

    /**
     * Cộng chi tiêu khi khách hàng thanh toán.
     *
     * @param customerId ID khách hàng
     * @param amount     Số tiền thanh toán
     */
    public void addSpendingToCustomer(String customerId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("❌ Số tiền không hợp lệ!");
            return;
        }

        Customer customer = customerManager.getById(customerId);
        if (customer == null) {
            System.out.println("❌ Khách hàng không tồn tại!");
            return;
        }

        // Cộng chi tiêu
        BigDecimal newTotalSpent = customer.getTotalSpent().add(amount);
        customer.setTotalSpent(newTotalSpent);

        // Cập nhật ngày ghé thăm cuối cùng
        customer.setLastValidDate(LocalDate.now());

        // Cập nhật lại trong Manager
        customerManager.update(customer);
    }

    /**
     * Vô hiệu hóa khách hàng.
     *
     * @param customerId ID khách hàng
     * @return true nếu vô hiệu hóa thành công, false nếu thất bại
     */
    public boolean deactivateCustomer(String customerId) {
        Customer customer = customerManager.getById(customerId);
        if (customer == null) {
            System.out.println("❌ Khách hàng không tồn tại!");
            return false;
        }

        customer.setActive(false);

        if (customerManager.update(customer)) {
            System.out.println("✅ Vô hiệu hóa khách hàng thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi vô hiệu hóa khách hàng!");
        return false;
    }

    /**
     * Kích hoạt lại khách hàng.
     *
     * @param customerId ID khách hàng
     * @return true nếu kích hoạt thành công, false nếu thất bại
     */
    public boolean reactivateCustomer(String customerId) {
        Customer customer = customerManager.getById(customerId);
        if (customer == null) {
            System.out.println("❌ Khách hàng không tồn tại!");
            return false;
        }

        customer.setActive(true);

        if (customerManager.update(customer)) {
            System.out.println("✅ Kích hoạt khách hàng thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi kích hoạt khách hàng!");
        return false;
    }

    /**
     * Sinh ID khách hàng mới theo định dạng CUST_XXXXXX.
     *
     * @return ID khách hàng mới
     */
    private String generateCustomerId() {
        return "CUST_" + System.currentTimeMillis();
    }
}
