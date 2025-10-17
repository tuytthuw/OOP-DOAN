package services;

import java.math.BigDecimal;
import java.time.LocalDate;

import collections.CustomerManager;
import enums.Tier;
import exceptions.BusinessLogicException;
import exceptions.EntityNotFoundException;
import exceptions.InvalidEntityException;
import exceptions.ValidationException;
import models.Customer;

/**
 * Service quản lý logic nghiệp vụ khách hàng.
 * Xử lý các thao tác như đăng ký, cập nhật, và quản lý tier membership.
 * Throws exceptions thay vì return false hoặc in error message.
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
     * @return Khách hàng vừa tạo
     * @throws ValidationException    nếu thông tin không hợp lệ
     * @throws BusinessLogicException nếu vi phạm business rule (duplicate
     *                                phone/email)
     */
    public Customer registerNewCustomer(String fullName, String phoneNumber, String email,
            String address, boolean isMale, LocalDate birthDate)
            throws ValidationException, BusinessLogicException {

        // Kiểm tra input validation
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ValidationException("Tên khách hàng", "Không được để trống");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new ValidationException("Số điện thoại", "Không được để trống");
        }

        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Ngày sinh", "Không được là ngày trong tương lai");
        }

        // Kiểm tra duplicate số điện thoại
        if (customerManager.findByPhone(phoneNumber) != null) {
            throw new BusinessLogicException(
                    "đăng ký khách hàng",
                    "Số điện thoại '" + phoneNumber + "' đã tồn tại");
        }

        // Kiểm tra duplicate email
        if (!email.isEmpty() && customerManager.findByEmail(email) != null) {
            throw new BusinessLogicException(
                    "đăng ký khách hàng",
                    "Email '" + email + "' đã tồn tại");
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

        // Thêm vào Manager - có thể throw exception từ Manager
        try {
            customerManager.add(newCustomer);
            return newCustomer;
        } catch (InvalidEntityException | exceptions.EntityAlreadyExistsException e) {
            throw new BusinessLogicException(
                    "đăng ký khách hàng",
                    e.getErrorMessage());
        }
    }

    /**
     * Cập nhật thông tin khách hàng.
     *
     * @param customerId  ID khách hàng
     * @param fullName    Tên đầy đủ
     * @param phoneNumber Số điện thoại
     * @param email       Email
     * @param address     Địa chỉ
     * @return true nếu cập nhật thành công
     * @throws EntityNotFoundException nếu khách hàng không tồn tại
     * @throws ValidationException     nếu thông tin không hợp lệ
     * @throws BusinessLogicException  nếu vi phạm business rule
     */
    public boolean updateCustomerInfo(String customerId, String fullName, String phoneNumber,
            String email, String address)
            throws EntityNotFoundException, ValidationException, BusinessLogicException {

        // Kiểm tra input validation
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ValidationException("Tên khách hàng", "Không được để trống");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new ValidationException("Số điện thoại", "Không được để trống");
        }

        try {
            // Lấy khách hàng theo ID
            Customer customer = customerManager.getById(customerId);

            // Kiểm tra số điện thoại không bị trùng (ngoại trừ chính khách hàng này)
            if (!customer.getPhoneNumber().equals(phoneNumber)) {
                if (customerManager.findByPhone(phoneNumber) != null) {
                    throw new BusinessLogicException(
                            "cập nhật thông tin",
                            "Số điện thoại '" + phoneNumber + "' đã tồn tại");
                }
            }

            // Kiểm tra email không bị trùng (ngoại trừ chính khách hàng này)
            if (!email.isEmpty() && !customer.getEmail().equals(email)) {
                if (customerManager.findByEmail(email) != null) {
                    throw new BusinessLogicException(
                            "cập nhật thông tin",
                            "Email '" + email + "' đã tồn tại");
                }
            }

            // Cập nhật thông tin
            customer.setFullName(fullName);
            customer.setPhoneNumber(phoneNumber);
            customer.setEmail(email);
            customer.setAddress(address);

            // Cập nhật lại trong Manager
            return customerManager.update(customer);
        } catch (InvalidEntityException | EntityNotFoundException e) {
            if (e instanceof EntityNotFoundException) {
                throw (EntityNotFoundException) e;
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Cập nhật tier khách hàng dựa trên tổng chi tiêu.
     *
     * @param customerId ID khách hàng
     * @throws EntityNotFoundException nếu khách hàng không tồn tại
     */
    public void updateCustomerTier(String customerId) throws EntityNotFoundException {
        try {
            Customer customer = customerManager.getById(customerId);

            // Gọi phương thức updateTier() của Customer (logic trong model)
            customer.updateTier();

            // Cập nhật lại trong Manager
            customerManager.update(customer);
        } catch (InvalidEntityException | EntityNotFoundException e) {
            if (e instanceof EntityNotFoundException) {
                throw (EntityNotFoundException) e;
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Cộng chi tiêu khi khách hàng thanh toán.
     *
     * @param customerId ID khách hàng
     * @param amount     Số tiền thanh toán
     * @throws EntityNotFoundException nếu khách hàng không tồn tại
     * @throws ValidationException     nếu số tiền không hợp lệ
     */
    public void addSpendingToCustomer(String customerId, BigDecimal amount)
            throws EntityNotFoundException, ValidationException {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Số tiền thanh toán", "Không được âm");
        }

        try {
            Customer customer = customerManager.getById(customerId);

            // Cộng chi tiêu
            BigDecimal newTotalSpent = customer.getTotalSpent().add(amount);
            customer.setTotalSpent(newTotalSpent);

            // Cập nhật ngày ghé thăm cuối cùng
            customer.setLastValidDate(LocalDate.now());

            // Cập nhật lại trong Manager
            customerManager.update(customer);
        } catch (InvalidEntityException | EntityNotFoundException e) {
            if (e instanceof EntityNotFoundException) {
                throw (EntityNotFoundException) e;
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Vô hiệu hóa khách hàng.
     *
     * @param customerId ID khách hàng
     * @return true nếu vô hiệu hóa thành công
     * @throws EntityNotFoundException nếu khách hàng không tồn tại
     */
    public boolean deactivateCustomer(String customerId) throws EntityNotFoundException {
        try {
            Customer customer = customerManager.getById(customerId);

            customer.setActive(false);

            return customerManager.update(customer);
        } catch (InvalidEntityException | EntityNotFoundException e) {
            if (e instanceof EntityNotFoundException) {
                throw (EntityNotFoundException) e;
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Kích hoạt lại khách hàng.
     *
     * @param customerId ID khách hàng
     * @return true nếu kích hoạt thành công
     * @throws EntityNotFoundException nếu khách hàng không tồn tại
     */
    public boolean reactivateCustomer(String customerId) throws EntityNotFoundException {
        try {
            Customer customer = customerManager.getById(customerId);

            customer.setActive(true);

            return customerManager.update(customer);
        } catch (InvalidEntityException | EntityNotFoundException e) {
            if (e instanceof EntityNotFoundException) {
                throw (EntityNotFoundException) e;
            }
            throw new RuntimeException(e);
        }
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
