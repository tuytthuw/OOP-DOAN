package ui;

import java.time.LocalDate;

import enums.AppointmentStatus;
import enums.Tier;
import io.InputHandler;
import io.OutputFormatter;
import services.ReportService;

/**
 * Menu xem báo cáo.
 * Cung cấp các chức năng: xem doanh thu, thống kê khách hàng, thống kê lịch
 * hẹn, dịch vụ phổ biến.
 */
public class ReportMenu extends MenuBase {

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    private ReportService reportService;

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo ReportMenu.
     *
     * @param inputHandler    Bộ xử lý input
     * @param outputFormatter Bộ định dạng output
     * @param reportService   Dịch vụ báo cáo
     */
    public ReportMenu(InputHandler inputHandler, OutputFormatter outputFormatter,
            ReportService reportService) {
        super(inputHandler, outputFormatter);
        this.reportService = reportService;
    }

    // ============ PHƯƠNG THỨC OVERRIDE (OVERRIDE METHODS) ============

    @Override
    protected void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   XEM BÁO CÁO                          ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Doanh thu theo khoảng thời gian");
        System.out.println("2. Thống kê khách hàng");
        System.out.println("3. Thống kê lịch hẹn");
        System.out.println("4. Dịch vụ phổ biến nhất");
        System.out.println("5. Phương thức thanh toán phổ biến");
        System.out.println("6. Quay lại");
        System.out.println();
    }

    @Override
    protected boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                viewRevenueByDateRange();
                return true;

            case 2:
                viewCustomerStatistics();
                return true;

            case 3:
                viewAppointmentStatistics();
                return true;

            case 4:
                viewMostPopularService();
                return true;

            case 5:
                viewPaymentMethodStatistics();
                return true;

            case 6:
                // Quay lại menu chính
                return false;

            default:
                System.out.println("❌ Lựa chọn không hợp lệ! Vui lòng chọn lại.");
                return true;
        }
    }

    // ============ PHƯƠNG THỨC CHỨC NĂNG (FUNCTIONAL METHODS) ============

    /**
     * Xem doanh thu theo khoảng thời gian.
     */
    private void viewRevenueByDateRange() {
        System.out.println("\n--- Doanh Thu Theo Khoảng Thời Gian ---");

        try {
            LocalDate startDate = inputHandler.readLocalDate("Nhập ngày bắt đầu (dd/MM/yyyy): ");
            LocalDate endDate = inputHandler.readLocalDate("Nhập ngày kết thúc (dd/MM/yyyy): ");

            var revenue = reportService.getTotalRevenueByDateRange(startDate, endDate);

            System.out.println("\n✓ Doanh thu từ " + startDate + " đến " + endDate);
            System.out.println("  Tổng tiền: " + revenue + " VND");

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xem thống kê khách hàng.
     */
    private void viewCustomerStatistics() {
        System.out.println("\n--- Thống Kê Khách Hàng ---");

        try {
            int platinum = reportService.getCustomerCountByTier(Tier.PLATINUM);
            int gold = reportService.getCustomerCountByTier(Tier.GOLD);
            int silver = reportService.getCustomerCountByTier(Tier.SILVER);
            int bronze = reportService.getCustomerCountByTier(Tier.BRONZE);

            System.out.println("\n✓ Thống kê khách hàng:");
            System.out.println("  PLATINUM: " + platinum);
            System.out.println("  GOLD: " + gold);
            System.out.println("  SILVER: " + silver);
            System.out.println("  BRONZE: " + bronze);
            System.out.println("  TỔNG CỘNG: " + (platinum + gold + silver + bronze));

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xem thống kê lịch hẹn.
     */
    private void viewAppointmentStatistics() {
        System.out.println("\n--- Thống Kê Lịch Hẹn ---");

        try {
            int scheduled = reportService.getAppointmentCountByStatus(AppointmentStatus.SCHEDULED);
            int spending = reportService.getAppointmentCountByStatus(AppointmentStatus.SPENDING);
            int completed = reportService.getAppointmentCountByStatus(AppointmentStatus.COMPLETED);
            int cancelled = reportService.getAppointmentCountByStatus(AppointmentStatus.CANCELLED);

            System.out.println("\n✓ Thống kê lịch hẹn:");
            System.out.println("  Đã lên lịch: " + scheduled);
            System.out.println("  Đang thực hiện: " + spending);
            System.out.println("  Hoàn thành: " + completed);
            System.out.println("  Hủy bỏ: " + cancelled);
            System.out.println("  TỔNG CỘNG: " + (scheduled + spending + completed + cancelled));

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xem dịch vụ phổ biến nhất.
     */
    private void viewMostPopularService() {
        System.out.println("\n--- Dịch Vụ Phổ Biến Nhất ---");

        try {
            var service = reportService.getMostPopularService();

            System.out.println("\n✓ Dịch vụ phổ biến nhất:");
            System.out.println("  " + service);

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xem phương thức thanh toán phổ biến.
     */
    private void viewPaymentMethodStatistics() {
        System.out.println("\n--- Phương Thức Thanh Toán Phổ Biến ---");

        try {
            String[] stats = reportService.getPaymentMethodStatistics();

            System.out.println("\n✓ Phương thức thanh toán phổ biến:");
            for (String stat : stats) {
                if (stat != null) {
                    System.out.println("  • " + stat);
                }
            }

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }
}
