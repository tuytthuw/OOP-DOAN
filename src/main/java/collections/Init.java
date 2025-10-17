package collections;

/**
 * Lớp khởi tạo (Init) cho collections package.
 * Chứa các phương thức để khởi tạo tất cả các Manager instances.
 */
public class Init {

    /**
     * Khởi tạo tất cả các manager instances.
     * Phương thức này sẽ được gọi khi ứng dụng khởi động.
     * 
     * @return Mảy chứa tất cả các manager (trong order: Customer, Service,
     *         Appointment, Transaction, Discount, Invoice, Employee)
     */
    public static BaseManager<?>[] initializeAllManagers() {
        // Khởi tạo 7 managers
        CustomerManager customerManager = new CustomerManager();
        ServiceManager serviceManager = new ServiceManager();
        AppointmentManager appointmentManager = new AppointmentManager();
        TransactionManager transactionManager = new TransactionManager();
        DiscountManager discountManager = new DiscountManager();
        InvoiceManager invoiceManager = new InvoiceManager();

        // Trả về mảy chứa tất cả managers (để dễ quản lý)
        BaseManager<?>[] managers = new BaseManager[6];
        managers[0] = customerManager;
        managers[1] = serviceManager;
        managers[2] = appointmentManager;
        managers[3] = transactionManager;
        managers[4] = discountManager;
        managers[5] = invoiceManager;

        return managers;
    }

    /**
     * Khởi tạo individual managers nếu cần.
     * Mỗi phương thức dưới đây tạo ra một manager instance riêng.
     */

    public static CustomerManager initCustomerManager() {
        return new CustomerManager();
    }

    public static ServiceManager initServiceManager() {
        return new ServiceManager();
    }

    public static AppointmentManager initAppointmentManager() {
        return new AppointmentManager();
    }

    public static TransactionManager initTransactionManager() {
        return new TransactionManager();
    }

    public static DiscountManager initDiscountManager() {
        return new DiscountManager();
    }

    public static InvoiceManager initInvoiceManager() {
        return new InvoiceManager();
    }
}
