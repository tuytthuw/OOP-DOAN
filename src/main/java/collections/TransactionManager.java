package collections;

import java.math.BigDecimal;
import java.time.LocalDate;

import enums.PaymentMethod;
import enums.TransactionStatus;
import models.Transaction;

/**
 * Manager quản lý danh sách giao dịch thanh toán.
 * Cung cấp các phương thức tìm kiếm và thống kê cho Transaction.
 */
public class TransactionManager extends BaseManager<Transaction> {

    /**
     * Lấy tất cả giao dịch.
     * 
     * @return Mảy chứa tất cả giao dịch hiện tại
     */
    @Override
    public Transaction[] getAll() {
        // Tạo mảy Transaction mới và copy từng phần tử một
        Transaction[] result = new Transaction[size];
        for (int i = 0; i < size; i++) {
            // Cast từng phần tử từ IEntity sang Transaction
            result[i] = (Transaction) items[i];
        }
        return result;
    }

    /**
     * Tìm tất cả giao dịch của một khách hàng.
     * 
     * @param customerId ID khách hàng cần tìm
     * @return Mảy chứa tất cả giao dịch của khách hàng
     */
    public Transaction[] findByCustomerId(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            return new Transaction[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getCustomerId().equals(customerId)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Transaction[] result = new Transaction[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getCustomerId().equals(customerId)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm tất cả giao dịch theo trạng thái.
     * 
     * @param status Trạng thái cần tìm (PENDING, SUCCESS, FAILED, REFUNDED)
     * @return Mảy chứa tất cả giao dịch có trạng thái đó
     */
    public Transaction[] findByStatus(TransactionStatus status) {
        if (status == null) {
            return new Transaction[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getStatus() == status) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Transaction[] result = new Transaction[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getStatus() == status) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm tất cả giao dịch theo phương thức thanh toán.
     * 
     * @param method Phương thức thanh toán cần tìm
     * @return Mảy chứa tất cả giao dịch dùng phương thức đó
     */
    public Transaction[] findByPaymentMethod(PaymentMethod method) {
        if (method == null) {
            return new Transaction[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getPaymentMethod() == method) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Transaction[] result = new Transaction[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getPaymentMethod() == method) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm giao dịch trong khoảng ngày nhất định.
     * 
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Mảy chứa tất cả giao dịch trong khoảng
     */
    public Transaction[] findByDateRange(LocalDate startDate,
            LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return new Transaction[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            LocalDate transactionDate = items[i].getTransactionDate().toLocalDate();
            if (!transactionDate.isBefore(startDate) &&
                    !transactionDate.isAfter(endDate)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Transaction[] result = new Transaction[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            LocalDate transactionDate = items[i].getTransactionDate().toLocalDate();
            if (!transactionDate.isBefore(startDate) &&
                    !transactionDate.isAfter(endDate)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tính tổng doanh thu từ các giao dịch thành công trong khoảng ngày.
     * Chỉ tính những giao dịch có status SUCCESS.
     * 
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Tổng doanh thu
     */
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < size; i++) {
            LocalDate transactionDate = items[i].getTransactionDate().toLocalDate();

            // Chỉ tính giao dịch thành công trong khoảng ngày
            if (!transactionDate.isBefore(startDate) &&
                    !transactionDate.isAfter(endDate) &&
                    items[i].getStatus() == TransactionStatus.SUCCESS) {
                total = total.add(items[i].getAmount());
            }
        }

        return total;
    }
}
