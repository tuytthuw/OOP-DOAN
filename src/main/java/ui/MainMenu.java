package ui;

import io.InputHandler;
import io.OutputFormatter;

/**
 * Menu chính của ứng dụng Spa Management System.
 * Cho phép người dùng điều hướng đến các menu con khác.
 */
public class MainMenu extends MenuBase {

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    /** Các submenu */
    private CustomerMenu customerMenu;
    private AppointmentMenu appointmentMenu;
    private PaymentMenu paymentMenu;
    private ReportMenu reportMenu;

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo MainMenu.
     *
     * @param inputHandler    Bộ xử lý input
     * @param outputFormatter Bộ định dạng output
     */
    public MainMenu(InputHandler inputHandler, OutputFormatter outputFormatter) {
        super(inputHandler, outputFormatter);
    }

    /**
     * Thiết lập các submenu (được gọi sau khi tất cả menu được tạo).
     *
     * @param customerMenu    Menu quản lý khách hàng
     * @param appointmentMenu Menu quản lý lịch hẹn
     * @param paymentMenu     Menu thanh toán
     * @param reportMenu      Menu báo cáo
     */
    public void setSubmenus(CustomerMenu customerMenu, AppointmentMenu appointmentMenu,
            PaymentMenu paymentMenu, ReportMenu reportMenu) {
        this.customerMenu = customerMenu;
        this.appointmentMenu = appointmentMenu;
        this.paymentMenu = paymentMenu;
        this.reportMenu = reportMenu;
    }

    // ============ PHƯƠNG THỨC OVERRIDE (OVERRIDE METHODS) ============

    @Override
    protected void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   SPA MANAGEMENT SYSTEM v1.0           ║");
        System.out.println("║   Quản Lý Spa Online                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Quản lý Khách hàng");
        System.out.println("2. Quản lý Lịch hẹn");
        System.out.println("3. Quản lý Thanh toán");
        System.out.println("4. Xem Báo cáo");
        System.out.println("5. Thoát");
        System.out.println();
    }

    @Override
    protected boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                // Quản lý khách hàng
                if (customerMenu != null) {
                    customerMenu.run();
                } else {
                    System.out.println("❌ Menu khách hàng chưa được khởi tạo!");
                }
                return true;

            case 2:
                // Quản lý lịch hẹn
                if (appointmentMenu != null) {
                    appointmentMenu.run();
                } else {
                    System.out.println("❌ Menu lịch hẹn chưa được khởi tạo!");
                }
                return true;

            case 3:
                // Quản lý thanh toán
                if (paymentMenu != null) {
                    paymentMenu.run();
                } else {
                    System.out.println("❌ Menu thanh toán chưa được khởi tạo!");
                }
                return true;

            case 4:
                // Xem báo cáo
                if (reportMenu != null) {
                    reportMenu.run();
                } else {
                    System.out.println("❌ Menu báo cáo chưa được khởi tạo!");
                }
                return true;

            case 5:
                // Thoát chương trình
                System.out.println("\n✓ Cảm ơn bạn đã sử dụng ứng dụng. Tạm biệt!");
                return false;

            default:
                System.out.println("❌ Lựa chọn không hợp lệ! Vui lòng chọn lại.");
                return true;
        }
    }
}
