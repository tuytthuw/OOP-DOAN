package ui;

public class MainMenu {
    private final InputHandler inputHandler;
    private final OutputFormatter outputFormatter;
    private final ui.CustomerMenu customerMenu;
    private final ui.AppointmentMenu appointmentMenu;
    private final ui.InventoryMenu inventoryMenu;
    private final ui.BillingMenu billingMenu;
    private final services.AuthService authService;

    public MainMenu(InputHandler inputHandler,
                    OutputFormatter outputFormatter,
                    ui.CustomerMenu customerMenu,
                    ui.AppointmentMenu appointmentMenu,
                    ui.InventoryMenu inventoryMenu,
                    ui.BillingMenu billingMenu,
                    services.AuthService authService) {
        this.inputHandler = inputHandler;
        this.outputFormatter = outputFormatter;
        this.customerMenu = customerMenu;
        this.appointmentMenu = appointmentMenu;
        this.inventoryMenu = inventoryMenu;
        this.billingMenu = billingMenu;
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
            System.out.println("0. Thoát");
            int choice = inputHandler.readInt("Chọn chức năng", 0, 6);
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
}
