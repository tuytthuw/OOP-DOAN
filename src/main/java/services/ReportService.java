package services;

import java.math.BigDecimal;
import java.time.LocalDate;

import collections.AppointmentManager;
import collections.CustomerManager;
import collections.InvoiceManager;
import collections.ServiceManager;
import collections.TransactionManager;
import enums.AppointmentStatus;
import enums.PaymentMethod;
import enums.Tier;
import models.Appointment;
import models.Customer;
import models.Invoice;
import models.Service;
import models.Transaction;

/**
 * Service quản lý logic báo cáo và thống kê.
 * Cung cấp các phương thức tính toán doanh thu, thống kê khách hàng, và phân
 * tích dịch vụ.
 */
public class ReportService {

    private InvoiceManager invoiceManager;
    private AppointmentManager appointmentManager;
    private CustomerManager customerManager;
    private ServiceManager serviceManager;
    private TransactionManager transactionManager;

    /**
     * Constructor khởi tạo ReportService.
     *
     * @param invoiceManager     Manager quản lý hóa đơn
     * @param appointmentManager Manager quản lý lịch hẹn
     * @param customerManager    Manager quản lý khách hàng
     * @param serviceManager     Manager quản lý dịch vụ
     * @param transactionManager Manager quản lý giao dịch
     */
    public ReportService(InvoiceManager invoiceManager, AppointmentManager appointmentManager,
            CustomerManager customerManager, ServiceManager serviceManager,
            TransactionManager transactionManager) {
        this.invoiceManager = invoiceManager;
        this.appointmentManager = appointmentManager;
        this.customerManager = customerManager;
        this.serviceManager = serviceManager;
        this.transactionManager = transactionManager;
    }

    /**
     * Tính tổng doanh thu trong khoảng ngày cho trước.
     * Chỉ tính những hóa đơn đã thanh toán.
     *
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Tổng doanh thu (VND)
     */
    public BigDecimal getTotalRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        // Lấy tất cả hóa đơn
        Invoice[] allInvoices = invoiceManager.getAll();

        BigDecimal totalRevenue = BigDecimal.ZERO;

        // Duyệt từng hóa đơn
        for (Invoice invoice : allInvoices) {
            // Kiểm tra ngày phát hành nằm trong khoảng
            if (!invoice.getIssueDate().isBefore(startDate) && !invoice.getIssueDate().isAfter(endDate)) {
                // Chỉ tính những hóa đơn đã thanh toán
                if (invoice.isPaid()) {
                    totalRevenue = totalRevenue.add(invoice.getTotalAmount());
                }
            }
        }

        return totalRevenue;
    }

    /**
     * Lấy số lượng khách hàng theo tier.
     *
     * @param tier Loại tier (PLATINUM, GOLD, SILVER, BRONZE)
     * @return Số lượng khách hàng có tier đó
     */
    public int getCustomerCountByTier(Tier tier) {
        Customer[] customersByTier = customerManager.findByTier(tier);
        return customersByTier.length;
    }

    /**
     * Lấy số lượng lịch hẹn theo trạng thái.
     *
     * @param status Trạng thái lịch hẹn
     * @return Số lượng lịch hẹn có trạng thái đó
     */
    public int getAppointmentCountByStatus(AppointmentStatus status) {
        // Lấy tất cả lịch hẹn
        Appointment[] allAppointments = appointmentManager.getAll();

        int count = 0;

        // Đếm những lịch hẹn có trạng thái này
        for (Appointment apt : allAppointments) {
            if (apt.getStatus() == status) {
                count++;
            }
        }

        return count;
    }

    /**
     * Lấy top N khách hàng chi tiêu nhiều nhất.
     * Trả về mảy các khách hàng được sắp xếp theo chi tiêu giảm dần.
     *
     * @param limit Số lượng khách hàng top cần lấy
     * @return Mảy chứa top khách hàng (mảy con của Customer[])
     */
    public Customer[] getTopCustomersBySpending(int limit) {
        // Lấy tất cả khách hàng
        Customer[] allCustomers = customerManager.getAll();

        if (allCustomers.length == 0) {
            return new Customer[0];
        }

        // Sắp xếp theo chi tiêu giảm dần (simple bubble sort)
        for (int i = 0; i < allCustomers.length - 1; i++) {
            for (int j = 0; j < allCustomers.length - 1 - i; j++) {
                if (allCustomers[j].getTotalSpent().compareTo(allCustomers[j + 1].getTotalSpent()) < 0) {
                    // Hoán đổi
                    Customer temp = allCustomers[j];
                    allCustomers[j] = allCustomers[j + 1];
                    allCustomers[j + 1] = temp;
                }
            }
        }

        // Lấy top limit
        int resultSize = Math.min(limit, allCustomers.length);
        Customer[] result = new Customer[resultSize];
        for (int i = 0; i < resultSize; i++) {
            result[i] = allCustomers[i];
        }

        return result;
    }

    /**
     * Lấy dịch vụ được yêu thích nhất (được sử dụng nhiều nhất).
     *
     * @return Dịch vụ được yêu thích nhất hoặc null nếu không có
     */
    public Service getMostPopularService() {
        // Lấy tất cả dịch vụ
        Service[] allServices = serviceManager.getAll();

        if (allServices.length == 0) {
            return null;
        }

        // Tính số lần sử dụng mỗi dịch vụ
        int[] usageCounts = new int[allServices.length];

        // Duyệt tất cả lịch hẹn để đếm
        Appointment[] allAppointments = appointmentManager.getAll();
        for (Appointment apt : allAppointments) {
            for (int i = 0; i < allServices.length; i++) {
                if (allServices[i].getId().equals(apt.getServiceId())) {
                    usageCounts[i]++;
                    break;
                }
            }
        }

        // Tìm dịch vụ có số lần sử dụng cao nhất
        int maxIndex = 0;
        for (int i = 1; i < usageCounts.length; i++) {
            if (usageCounts[i] > usageCounts[maxIndex]) {
                maxIndex = i;
            }
        }

        return allServices[maxIndex];
    }

    /**
     * Tính doanh thu theo tháng trong năm cho trước.
     *
     * @param year  Năm
     * @param month Tháng (1-12)
     * @return Doanh thu của tháng đó (VND)
     */
    public BigDecimal getMonthlyRevenue(int year, int month) {
        // Xác định ngày đầu và ngày cuối của tháng
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return getTotalRevenueByDateRange(startDate, endDate);
    }

    /**
     * Lấy thống kê phương thức thanh toán.
     * Tính tổng doanh thu theo từng phương thức thanh toán.
     * Trả về mảy chuỗi theo định dạng: "PHƯƠNG_THỨC: DOANH_THU".
     *
     * @return Mảy chuỗi chứa thống kê phương thức thanh toán
     */
    public String[] getPaymentMethodStatistics() {
        // Lấy tất cả giao dịch
        Transaction[] allTransactions = transactionManager.getAll();

        // Tính doanh thu theo phương thức thanh toán
        // Sử dụng mảy để lưu tổng doanh thu cho từng phương thức
        BigDecimal[] revenueByMethod = new BigDecimal[PaymentMethod.values().length];

        // Khởi tạo tất cả thành ZERO
        for (int i = 0; i < revenueByMethod.length; i++) {
            revenueByMethod[i] = BigDecimal.ZERO;
        }

        // Duyệt tất cả giao dịch thành công
        for (Transaction txn : allTransactions) {
            if (txn.getStatus().toString().equals("SUCCESS")) {
                PaymentMethod method = txn.getPaymentMethod();
                int methodIndex = method.ordinal();
                revenueByMethod[methodIndex] = revenueByMethod[methodIndex].add(txn.getAmount());
            }
        }

        // Tạo mảy kết quả
        PaymentMethod[] methods = PaymentMethod.values();
        String[] result = new String[methods.length];

        for (int i = 0; i < methods.length; i++) {
            result[i] = methods[i].toString() + ": " + revenueByMethod[i].toPlainString() + " VND";
        }

        return result;
    }
}
