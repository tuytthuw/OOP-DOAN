package ui;

public class MainMenu {
    private final InputHandler inputHandler;
    private final OutputFormatter outputFormatter;
    private final ui.CustomerMenu customerMenu;
    private final ui.AppointmentMenu appointmentMenu;
    private final ui.InventoryMenu inventoryMenu;
    private final ui.BillingMenu billingMenu;
    private final services.EmployeeService employeeService;
    private final services.ServiceCatalog serviceCatalog;
    private final services.AuthService authService;

    public MainMenu(InputHandler inputHandler,
                    OutputFormatter outputFormatter,
                    ui.CustomerMenu customerMenu,
                    ui.AppointmentMenu appointmentMenu,
                    ui.InventoryMenu inventoryMenu,
                    ui.BillingMenu billingMenu,
                    services.EmployeeService employeeService,
                    services.ServiceCatalog serviceCatalog,
                    services.AuthService authService) {
        this.inputHandler = inputHandler;
        this.outputFormatter = outputFormatter;
        this.customerMenu = customerMenu;
        this.appointmentMenu = appointmentMenu;
        this.inventoryMenu = inventoryMenu;
        this.billingMenu = billingMenu;
        this.employeeService = employeeService;
        this.serviceCatalog = serviceCatalog;
        this.authService = authService;
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("===== SPA MANAGEMENT =====");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng xuất");
            System.out.println("3. Quản lý khách hàng");
            System.out.println("4. Quản lý lịch hẹn");
            System.out.println("5. Quản lý kho");
            System.out.println("6. Thanh toán hóa đơn");
            System.out.println("7. Danh sách nhân viên");
            System.out.println("8. Danh sách dịch vụ");
            System.out.println("0. Thoát");
            int choice = inputHandler.readInt("Chọn chức năng", 0, 8);
            switch (choice) {
                case 1:
                    loginFlow();
                    break;
                case 2:
                    authService.logout();
                    outputFormatter.printStatus("Đã đăng xuất", true);
                    break;
                case 3:
                    customerMenu.showMenu();
                    break;
                case 4:
                    appointmentMenu.showMenu();
                    break;
                case 5:
                    if (requireRole(enums.EmployeeRole.MANAGER)) {
                        inventoryMenu.showMenu();
                    }
                    break;
                case 6:
                    if (requireRole(enums.EmployeeRole.MANAGER)) {
                        billingMenu.show();
                    }
                    break;
                case 7:
                    listEmployees();
                    break;
                case 8:
                    listServices();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    outputFormatter.printStatus("Lựa chọn không hợp lệ", false);
            }
        }
    }

    private void loginFlow() {
        String username = inputHandler.readString("Mã nhân viên");
        String password = inputHandler.readString("Mật khẩu");
        boolean success = authService.login(username, password);
        outputFormatter.printStatus(success ? "Đăng nhập thành công" : "Sai thông tin đăng nhập", success);
    }

    private boolean requireRole(enums.EmployeeRole role) {
        if (authService.getCurrentSession() == null) {
            outputFormatter.printStatus("Cần đăng nhập", false);
            return false;
        }
        if (!authService.hasRole(role) && !authService.hasRole(enums.EmployeeRole.ADMIN)) {
            outputFormatter.printStatus("Không đủ quyền", false);
            return false;
        }
        authService.refreshSession();
        return true;
    }

    private void listEmployees() {
        if (!requireRole(enums.EmployeeRole.MANAGER)) {
            return;
        }
        models.Employee[] employees = employeeService.getAllEmployees();
        if (employees.length == 0) {
            outputFormatter.printStatus("Chưa có nhân viên", false);
            return;
        }
        String[][] rows = new String[employees.length][4];
        for (int i = 0; i < employees.length; i++) {
            models.Employee employee = employees[i];
            rows[i][0] = employee.getEmployeeCode();
            rows[i][1] = employee.getFullName();
            rows[i][2] = employee.getRole().name();
            rows[i][3] = employee.getStatus().name();
        }
        outputFormatter.printTable(new String[]{"Mã", "Tên", "Vai trò", "Trạng thái"}, rows);
    }

    private void listServices() {
        models.Service[] services = serviceCatalog.listAll();
        if (services.length == 0) {
            outputFormatter.printStatus("Chưa có dịch vụ", false);
            return;
        }
        String[][] rows = new String[services.length][4];
        for (int i = 0; i < services.length; i++) {
            models.Service service = services[i];
            rows[i][0] = service.getId();
            rows[i][1] = service.getServiceName();
            rows[i][2] = service.getCategory() == null ? "" : service.getCategory().name();
            rows[i][3] = service.getPrice().toPlainString();
        }
        outputFormatter.printTable(new String[]{"ID", "Tên", "Loại", "Giá"}, rows);
    }
}
