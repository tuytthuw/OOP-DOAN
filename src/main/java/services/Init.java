package services;

import collections.AppointmentManager;
import collections.CustomerManager;
import collections.DiscountManager;
import collections.InvoiceManager;
import collections.ServiceManager;
import collections.TransactionManager;

/**
 * Lớp khởi tạo tất cả các Service layers.
 * Cung cấp các phương thức để tạo instance của tất cả services cần thiết.
 */
public class Init {

    /**
     * Khởi tạo CustomerService.
     *
     * @param customerManager Manager quản lý khách hàng
     * @return Instance của CustomerService
     */
    public static CustomerService initCustomerService(CustomerManager customerManager) {
        return new CustomerService(customerManager);
    }

    /**
     * Khởi tạo AppointmentService.
     *
     * @param appointmentManager Manager quản lý lịch hẹn
     * @param customerManager    Manager quản lý khách hàng
     * @param serviceManager     Manager quản lý dịch vụ
     * @return Instance của AppointmentService
     */
    public static AppointmentService initAppointmentService(AppointmentManager appointmentManager,
            CustomerManager customerManager,
            ServiceManager serviceManager) {
        return new AppointmentService(appointmentManager, customerManager, serviceManager);
    }

    /**
     * Khởi tạo InvoiceService.
     *
     * @param invoiceManager     Manager quản lý hóa đơn
     * @param appointmentManager Manager quản lý lịch hẹn
     * @param serviceManager     Manager quản lý dịch vụ
     * @param discountManager    Manager quản lý chiết khấu
     * @return Instance của InvoiceService
     */
    public static InvoiceService initInvoiceService(InvoiceManager invoiceManager,
            AppointmentManager appointmentManager,
            ServiceManager serviceManager,
            DiscountManager discountManager) {
        return new InvoiceService(invoiceManager, appointmentManager, serviceManager, discountManager);
    }

    /**
     * Khởi tạo PaymentService.
     *
     * @param transactionManager Manager quản lý giao dịch
     * @param invoiceManager     Manager quản lý hóa đơn
     * @param customerService    Service quản lý khách hàng
     * @return Instance của PaymentService
     */
    public static PaymentService initPaymentService(TransactionManager transactionManager,
            InvoiceManager invoiceManager,
            CustomerService customerService) {
        return new PaymentService(transactionManager, invoiceManager, customerService);
    }

    /**
     * Khởi tạo ReportService.
     *
     * @param invoiceManager     Manager quản lý hóa đơn
     * @param appointmentManager Manager quản lý lịch hẹn
     * @param customerManager    Manager quản lý khách hàng
     * @param serviceManager     Manager quản lý dịch vụ
     * @param transactionManager Manager quản lý giao dịch
     * @return Instance của ReportService
     */
    public static ReportService initReportService(InvoiceManager invoiceManager,
            AppointmentManager appointmentManager,
            CustomerManager customerManager,
            ServiceManager serviceManager,
            TransactionManager transactionManager) {
        return new ReportService(invoiceManager, appointmentManager, customerManager,
                serviceManager, transactionManager);
    }

    /**
     * Khởi tạo TẤT CẢ services cùng lúc.
     * Phương thức tiện lợi khi cần khởi tạo toàn bộ service layer.
     *
     * @param appointmentManager Manager quản lý lịch hẹn
     * @param customerManager    Manager quản lý khách hàng
     * @param discountManager    Manager quản lý chiết khấu
     * @param invoiceManager     Manager quản lý hóa đơn
     * @param serviceManager     Manager quản lý dịch vụ
     * @param transactionManager Manager quản lý giao dịch
     * @return Mảy chứa tất cả services theo thứ tự:
     *         [0] = CustomerService
     *         [1] = AppointmentService
     *         [2] = InvoiceService
     *         [3] = PaymentService
     *         [4] = ReportService
     */
    public static Object[] initAllServices(AppointmentManager appointmentManager,
            CustomerManager customerManager,
            DiscountManager discountManager,
            InvoiceManager invoiceManager,
            ServiceManager serviceManager,
            TransactionManager transactionManager) {
        // Khởi tạo các services theo thứ tự
        CustomerService customerService = initCustomerService(customerManager);
        AppointmentService appointmentService = initAppointmentService(appointmentManager,
                customerManager, serviceManager);
        InvoiceService invoiceService = initInvoiceService(invoiceManager, appointmentManager,
                serviceManager, discountManager);
        PaymentService paymentService = initPaymentService(transactionManager, invoiceManager,
                customerService);
        ReportService reportService = initReportService(invoiceManager, appointmentManager,
                customerManager, serviceManager,
                transactionManager);

        // Trả về mảy các services
        return new Object[] {
                customerService,
                appointmentService,
                invoiceService,
                paymentService,
                reportService
        };
    }
}
