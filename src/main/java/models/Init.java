package models;

import java.math.BigDecimal;
import java.time.LocalDate;

import enums.ServiceCategory;
import enums.Tier;

/**
 * Lớp Init dùng để khởi tạo dữ liệu mẫu cho Customer và Service.
 * Dùng cho testing và demo tính năng của project.
 */
public class Init {

    /**
     * Main method - khởi tạo và hiển thị dữ liệu mẫu.
     *
     * @param args Tham số dòng lệnh (không sử dụng)
     */
    public static void main(String[] args) {
        // Khởi tạo dữ liệu mẫu cho Customer
        initSampleCustomers();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Khởi tạo dữ liệu mẫu cho Service
        initSampleServices();
    }

    /**
     * Khởi tạo và hiển thị các khách hàng mẫu.
     */
    private static void initSampleCustomers() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        KHÁCH HÀNG MẪU (SAMPLE)         ║");
        System.out.println("╚════════════════════════════════════════╝");

        // Customer 1: PLATINUM (tổng chi tiêu >= 10,000,000)
        Customer customer1 = new Customer(
                "CUST_001",
                "Nguyễn Văn A",
                "0901234567",
                true,
                LocalDate.of(1990, 5, 15),
                "nguyenvana@email.com",
                "123 Đường Lê Lợi, TP.HCM",
                Tier.BRONZE,
                LocalDate.of(2022, 1, 10),
                LocalDate.now(),
                true,
                new BigDecimal("12000000"));
        customer1.display();

        // Customer 2: GOLD (tổng chi tiêu >= 5,000,000)
        Customer customer2 = new Customer(
                "CUST_002",
                "Trần Thị B",
                "0912345678",
                false,
                LocalDate.of(1995, 8, 22),
                "tranthib@email.com",
                "456 Đường Nguyễn Huệ, TP.HCM",
                Tier.BRONZE,
                LocalDate.of(2023, 3, 20),
                LocalDate.now(),
                true,
                new BigDecimal("6500000"));
        customer2.display();

        // Customer 3: SILVER (tổng chi tiêu >= 1,000,000)
        Customer customer3 = new Customer(
                "CUST_003",
                "Lê Hoàng C",
                "0923456789",
                true,
                LocalDate.of(1988, 12, 3),
                "lehoangc@email.com",
                "789 Đường Hàm Nghi, TP.HCM",
                Tier.BRONZE,
                LocalDate.of(2023, 6, 15),
                LocalDate.now(),
                true,
                new BigDecimal("2500000"));
        customer3.display();

        // Customer 4: BRONZE (tổng chi tiêu < 1,000,000)
        Customer customer4 = new Customer(
                "CUST_004",
                "Phạm Minh D",
                "0934567890",
                false,
                LocalDate.of(2000, 2, 28),
                "phamminhd@email.com",
                "321 Đường Pasteur, TP.HCM",
                Tier.BRONZE,
                LocalDate.of(2024, 1, 5),
                LocalDate.now(),
                true,
                new BigDecimal("500000"));
        customer4.display();

        // Kiểm tra cộng chi tiêu
        System.out.println("\n--- Test: Cộng thêm chi tiêu cho Customer 1 ---");
        customer1.addToTotalSpent(new BigDecimal("3000000"));
        System.out.println("Sau khi thêm 3,000,000 VND:");
        System.out.println("Tổng chi tiêu mới: " + customer1.getTotalSpent() + " VND");
        System.out.println("Tier hiện tại: " + customer1.getMemberTier());
    }

    /**
     * Khởi tạo và hiển thị các dịch vụ mẫu.
     */
    private static void initSampleServices() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         DỊCH VỤ MẪU (SAMPLE)           ║");
        System.out.println("╚════════════════════════════════════════╝");

        // Service 1: Massage toàn thân
        Service service1 = new Service(
                "SVC_001",
                "Massage toàn thân",
                new BigDecimal("500000"),
                60,
                15,
                "Xoa bóp toàn thân giúp thư giãn, giảm stress",
                ServiceCategory.MASSAGE);
        service1.display();

        // Service 2: Chăm sóc mặt
        Service service2 = new Service(
                "SVC_002",
                "Chăm sóc mặt Premium",
                new BigDecimal("350000"),
                45,
                10,
                "Chăm sóc mặt sâu với các sản phẩm cao cấp, giúp da sáng mịn",
                ServiceCategory.FACIAL);
        service2.display();

        // Service 3: Chăm sóc cơ thể
        Service service3 = new Service(
                "SVC_003",
                "Tắm khoáng nóng với tinh dầu",
                new BigDecimal("600000"),
                90,
                20,
                "Tắm khoáng nóng kết hợp tinh dầu thiên nhiên, giúp cơ thể khỏe mạnh",
                ServiceCategory.BODY_TREATMENT);
        service3.display();

        // Service 4: Chăm sóc tóc
        Service service4 = new Service(
                "SVC_004",
                "Gội đầu và cắt tóc chuyên nghiệp",
                new BigDecimal("200000"),
                30,
                10,
                "Gội đầu với các sản phẩm cao cấp, cắt tóc theo kiểu hiện đại",
                ServiceCategory.HAIR_CARE);
        service4.display();

        // Service 5: Chăm sóc móng
        Service service5 = new Service(
                "SVC_005",
                "Chăm sóc móng tay và chân",
                new BigDecimal("250000"),
                45,
                10,
                "Làm sạch, cắt tỉa, sơn móng với màu sắc theo lựa chọn",
                ServiceCategory.NAIL_CARE);
        service5.display();

        // Service 6: Trang điểm
        Service service6 = new Service(
                "SVC_006",
                "Trang điểm cô dâu",
                new BigDecimal("1000000"),
                120,
                30,
                "Trang điểm toàn diện cho cô dâu, với công nghệ trang điểm hiện đại",
                ServiceCategory.MAKEUP);
        service6.display();

        // Service 7: Gói kết hợp
        Service service7 = new Service(
                "SVC_007",
                "Gói spa toàn diện",
                new BigDecimal("1500000"),
                180,
                30,
                "Gói spa kết hợp: massage, chăm sóc mặt, tắm khoáng, chăm sóc móng",
                ServiceCategory.COMBINATION);
        service7.display();

        // Kiểm tra phương thức
        System.out.println("\n--- Test: Kiểm tra khả dụng của dịch vụ ---");
        System.out.println("Service 1 khả dụng: " + service1.isAvailable());
        System.out.println("Giá định dạng: " + service1.getPriceFormatted());
        System.out.println("Thời gian: " + service1.getDurationFormatted());
    }
}
