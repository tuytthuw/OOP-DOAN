package collections;

import java.math.BigDecimal;
import java.time.LocalDate;

import models.Invoice;

/**
 * Manager quản lý danh sách hóa đơn.
 * Cung cấp các phương thức tìm kiếm và thống kê cho Invoice.
 */
public class InvoiceManager extends BaseManager<Invoice> {

    /**
     * Lấy tất cả hóa đơn.
     * 
     * @return Mảy chứa tất cả hóa đơn hiện tại
     */
    @Override
    public Invoice[] getAll() {
        // Tạo mảy Invoice mới và copy từng phần tử một
        Invoice[] result = new Invoice[size];
        for (int i = 0; i < size; i++) {
            // Cast từng phần tử từ IEntity sang Invoice
            result[i] = (Invoice) items[i];
        }
        return result;
    }

    /**
     * Tìm tất cả hóa đơn của một khách hàng.
     * 
     * @param customerId ID khách hàng cần tìm
     * @return Mảy chứa tất cả hóa đơn của khách hàng
     */
    public Invoice[] findByCustomerId(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            return new Invoice[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getCustomerId().equals(customerId)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Invoice[] result = new Invoice[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getCustomerId().equals(customerId)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm hóa đơn theo ID lịch hẹn.
     * 
     * @param appointmentId ID lịch hẹn cần tìm
     * @return Hóa đơn nếu tìm thấy, null nếu không
     */
    public Invoice findByAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.isEmpty()) {
            return null;
        }

        for (int i = 0; i < size; i++) {
            if (items[i].getAppointmentId().equals(appointmentId)) {
                return items[i];
            }
        }
        return null;
    }

    /**
     * Tìm tất cả hóa đơn theo trạng thái thanh toán.
     * 
     * @param isPaid true để lấy hóa đơn đã thanh toán, false để lấy chưa thanh toán
     * @return Mảy chứa tất cả hóa đơn có trạng thái thanh toán đó
     */
    public Invoice[] findByPaymentStatus(boolean isPaid) {
        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].isPaid() == isPaid) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Invoice[] result = new Invoice[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].isPaid() == isPaid) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm hóa đơn trong khoảng ngày nhất định.
     * 
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Mảy chứa tất cả hóa đơn trong khoảng
     */
    public Invoice[] findByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return new Invoice[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            LocalDate invoiceDate = items[i].getIssueDate();
            if (!invoiceDate.isBefore(startDate) &&
                    !invoiceDate.isAfter(endDate)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Invoice[] result = new Invoice[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            LocalDate invoiceDate = items[i].getIssueDate();
            if (!invoiceDate.isBefore(startDate) &&
                    !invoiceDate.isAfter(endDate)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tính tổng doanh thu từ các hóa đơn đã thanh toán trong khoảng ngày.
     * 
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Tổng doanh thu từ hóa đơn đã thanh toán
     */
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < size; i++) {
            LocalDate invoiceDate = items[i].getIssueDate();

            // Chỉ tính hóa đơn đã thanh toán trong khoảng ngày
            if (!invoiceDate.isBefore(startDate) &&
                    !invoiceDate.isAfter(endDate) &&
                    items[i].isPaid()) {
                total = total.add(items[i].getTotalAmount());
            }
        }

        return total;
    }
}
