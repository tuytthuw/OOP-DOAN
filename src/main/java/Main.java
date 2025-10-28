import collections.AppointmentManager;
import collections.AuthSessionManager;
import collections.CustomerManager;
import collections.EmployeeManager;
import collections.GoodsReceiptManager;
import collections.InvoiceManager;
import collections.ProductManager;
import collections.PromotionManager;
import collections.ServiceManager;
import collections.TransactionManager;
import io.FileHandler;
import services.AppointmentService;
import services.AuthService;
import services.CustomerService;
import services.EmployeeService;
import services.InventoryService;
import services.InvoiceService;
import services.PaymentService;
import services.PromotionService;
import services.ServiceCatalog;
import services.TechnicianAvailabilityService;
import ui.AppointmentMenu;
import ui.BillingMenu;
import ui.CustomerMenu;
import ui.InputHandler;
import ui.InventoryMenu;
import ui.MainMenu;
import ui.OutputFormatter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext();
        context.loadAllData();
        try {
            context.run();
        } finally {
            context.saveAllData();
        }
    }

    private static class ApplicationContext {
        private final CustomerManager customerManager = new CustomerManager();
        private final EmployeeManager employeeManager = new EmployeeManager();
        private final ServiceManager serviceManager = new ServiceManager();
        private final ProductManager productManager = new ProductManager();
        private final GoodsReceiptManager goodsReceiptManager = new GoodsReceiptManager();
        private final AppointmentManager appointmentManager = new AppointmentManager();
        private final InvoiceManager invoiceManager = new InvoiceManager();
        private final PromotionManager promotionManager = new PromotionManager();
        private final TransactionManager transactionManager = new TransactionManager();
        private final AuthSessionManager authSessionManager = new AuthSessionManager();

        private final CustomerService customerService = new CustomerService(customerManager);
        private final EmployeeService employeeService = new EmployeeService(employeeManager);
        private final TechnicianAvailabilityService technicianAvailabilityService =
                new TechnicianAvailabilityService(appointmentManager);
        private final AppointmentService appointmentService = new AppointmentService(appointmentManager, technicianAvailabilityService);
        private final ServiceCatalog serviceCatalog = new ServiceCatalog(serviceManager);
        private final InventoryService inventoryService = new InventoryService(goodsReceiptManager, productManager);
        private final InvoiceService invoiceService = new InvoiceService(invoiceManager, productManager, serviceManager, promotionManager);
        private final PromotionService promotionService = new PromotionService(promotionManager);
        private final PaymentService paymentService = new PaymentService(transactionManager, invoiceManager);
        private final AuthService authService = AuthService.getInstance(employeeManager, authSessionManager);

        private final Scanner scanner = new Scanner(System.in);
        private final InputHandler inputHandler = new InputHandler(scanner);
        private final OutputFormatter outputFormatter = new OutputFormatter();

        private final CustomerMenu customerMenu = new CustomerMenu(customerService, inputHandler, outputFormatter);
        private final InventoryMenu inventoryMenu = new InventoryMenu(inventoryService, inputHandler, outputFormatter);
        private final AppointmentMenu appointmentMenu = new AppointmentMenu(appointmentService, inputHandler, outputFormatter);
        private final BillingMenu billingMenu = new BillingMenu(invoiceService, paymentService, promotionService, authService, inputHandler, outputFormatter);
        private final MainMenu mainMenu = new MainMenu(inputHandler, outputFormatter, customerMenu, appointmentMenu, inventoryMenu, billingMenu, employeeService, serviceCatalog, authService);

        public void run() {
            seedDemoData();
            mainMenu.display();
        }

        public void loadAllData() {
            // Persistence factories chưa được cài đặt, giữ chỗ cho bước mở rộng.
            FileHandler.load("customers", line -> null);
            FileHandler.load("employees", line -> null);
            FileHandler.load("services", line -> null);
            FileHandler.load("products", line -> null);
            FileHandler.load("goods_receipts", line -> null);
            FileHandler.load("appointments", line -> null);
            FileHandler.load("invoices", line -> null);
            FileHandler.load("promotions", line -> null);
            FileHandler.load("transactions", line -> null);
            FileHandler.load("auth_sessions", line -> null);
        }

        public void saveAllData() {
            FileHandler.save("customers", customerManager.getAll());
            FileHandler.save("employees", employeeManager.getAll());
            FileHandler.save("services", serviceManager.getAll());
            FileHandler.save("products", productManager.getAll());
            FileHandler.save("goods_receipts", goodsReceiptManager.getAll());
            FileHandler.save("appointments", appointmentManager.getAll());
            FileHandler.save("invoices", invoiceManager.getAll());
            FileHandler.save("promotions", promotionManager.getAll());
            FileHandler.save("transactions", transactionManager.getAll());
            FileHandler.save("auth_sessions", authSessionManager.getAll());
        }

        private void seedDemoData() {
            if (serviceManager.getAll().length == 0) {
                serviceManager.add(new models.Service("Massage Thái", "Thư giãn toàn thân", BigDecimal.valueOf(500000), 90, enums.ServiceCategory.MASSAGE));
            }
            if (productManager.getAll().length == 0) {
                productManager.add(new models.Product("Tinh dầu oải hương", "Thư giãn", BigDecimal.valueOf(150000), 20, 5));
            }
        }
    }
}
