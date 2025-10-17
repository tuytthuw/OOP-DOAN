import collections.AppointmentManager;
import collections.CustomerManager;
import collections.DiscountManager;
import collections.EmployeeManager;
import collections.InvoiceManager;
import collections.ServiceManager;
import collections.TransactionManager;
import io.InputHandler;
import io.OutputFormatter;
import services.AppointmentService;
import services.CustomerService;
import services.EmployeeService;
import services.InvoiceService;
import services.PaymentService;
import services.ReportService;
import ui.AppointmentMenu;
import ui.CustomerMenu;
import ui.EmployeeMenu;
import ui.MainMenu;
import ui.PaymentMenu;
import ui.ReportMenu;

/**
 * Lớp Main - Điểm khởi động của ứng dụng Spa Management System.
 * Chịu trách nhiệm khởi tạo tất cả các lớp cần thiết và chạy chương trình
 * chính.
 */
public class Main {

    public static void main(String[] args) {
        try {
            // ============ KHỞI TẠO IO HANDLERS ============
            InputHandler inputHandler = new InputHandler();
            OutputFormatter outputFormatter = new OutputFormatter();

            // ============ KHỞI TẠO MANAGERS ============
            CustomerManager customerManager = new CustomerManager();
            ServiceManager serviceManager = new ServiceManager();
            AppointmentManager appointmentManager = new AppointmentManager();
            TransactionManager transactionManager = new TransactionManager();
            DiscountManager discountManager = new DiscountManager();
            InvoiceManager invoiceManager = new InvoiceManager();
            EmployeeManager employeeManager = new EmployeeManager();

            // ============ KHỞI TẠO SERVICES ============
            CustomerService customerService = new CustomerService(customerManager);
            AppointmentService appointmentService = new AppointmentService(
                    appointmentManager, customerManager, serviceManager);
            InvoiceService invoiceService = new InvoiceService(
                    invoiceManager, appointmentManager, serviceManager, discountManager);
            PaymentService paymentService = new PaymentService(
                    transactionManager, invoiceManager, customerService);
            ReportService reportService = new ReportService(
                    invoiceManager, appointmentManager, customerManager,
                    serviceManager, transactionManager);
            EmployeeService employeeService = new EmployeeService(employeeManager);

            // ============ KHỞI TẠO MENUS ============
            // Tạo MainMenu
            MainMenu mainMenu = new MainMenu(inputHandler, outputFormatter);

            // Tạo các submenu với đầy đủ tham số cần thiết
            CustomerMenu customerMenu = new CustomerMenu(inputHandler, outputFormatter, customerService,
                    customerManager);
            AppointmentMenu appointmentMenu = new AppointmentMenu(inputHandler, outputFormatter, appointmentService,
                    appointmentManager);
            PaymentMenu paymentMenu = new PaymentMenu(inputHandler, outputFormatter, paymentService, invoiceService,
                    transactionManager);
            ReportMenu reportMenu = new ReportMenu(inputHandler, outputFormatter, reportService);
            EmployeeMenu employeeMenu = new EmployeeMenu(inputHandler, outputFormatter, employeeService);

            // Thiết lập submenu cho MainMenu
            mainMenu.setSubmenus(customerMenu, appointmentMenu, paymentMenu, reportMenu, employeeMenu);

            // ============ KHỞI TẠO DỮ LIỆU BAN ĐẦU ============
            initializeSampleData(customerManager, serviceManager, employeeManager);

            // ============ CHẠY CHƯƠNG TRÌNH CHÍNH ============
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   SPA MANAGEMENT SYSTEM v1.0           ║");
            System.out.println("║   Chào mừng đến với hệ thống quản lý   ║");
            System.out.println("╚════════════════════════════════════════╝");

            mainMenu.run();

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   Cảm ơn bạn đã sử dụng dịch vụ!      ║");
            System.out.println("║   Tạm biệt và hẹn gặp lại!             ║");
            System.out.println("╚════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi khởi động ứng dụng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Khởi tạo dữ liệu mẫu cho ứng dụng.
     * Thêm một số khách hàng, dịch vụ và nhân viên mẫu để có thể test.
     *
     * @param customerManager Manager quản lý khách hàng
     * @param serviceManager  Manager quản lý dịch vụ
     * @param employeeManager Manager quản lý nhân viên
     */
    private static void initializeSampleData(CustomerManager customerManager,
            ServiceManager serviceManager,
            EmployeeManager employeeManager) {
        // Có thể thêm dữ liệu mẫu ở đây nếu cần thiết
        // Ví dụ: tạo một số khách hàng mặc định, dịch vụ, nhân viên
        // Hiện tại, hệ thống sẽ hoạt động với dữ liệu trống
    }
}
