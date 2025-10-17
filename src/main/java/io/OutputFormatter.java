package io;

import java.math.BigDecimal;

import models.Appointment;
import models.Customer;
import models.Discount;
import models.Invoice;
import models.Service;
import models.Transaction;

/**
 * OutputFormatter định dạng và hiển thị dữ liệu ra console.
 * Cung cấp các phương thức để in bảng, thông tin entity, và thông báo.
 * Hỗ trợ in mảy các items mà không dùng List.
 */
public class OutputFormatter {

    // ============ HẰNG SỐ ============
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    /**
     * In tiêu đề section.
     *
     * @param title Tiêu đề
     */
    public void printHeader(String title) {
        System.out.println();
        System.out.println(BLUE + "╔" + "═".repeat(Math.max(0, title.length() + 4)) + "╗" + RESET);
        System.out.println(BLUE + "║  " + title + "  ║" + RESET);
        System.out.println(BLUE + "╚" + "═".repeat(Math.max(0, title.length() + 4)) + "╝" + RESET);
    }

    /**
     * In đường phân cách.
     */
    public void printSeparator() {
        System.out.println("─".repeat(80));
    }

    /**
     * In thông báo thành công.
     *
     * @param message Nội dung thông báo
     */
    public void printSuccess(String message) {
        System.out.println(GREEN + "✅ " + message + RESET);
    }

    /**
     * In thông báo lỗi.
     *
     * @param message Nội dung thông báo
     */
    public void printError(String message) {
        System.out.println(RED + "❌ " + message + RESET);
    }

    /**
     * In cảnh báo.
     *
     * @param message Nội dung cảnh báo
     */
    public void printWarning(String message) {
        System.out.println(YELLOW + "⚠️  " + message + RESET);
    }

    /**
     * In thông tin thành công với đường khung.
     *
     * @param message Nội dung
     */
    public void printBox(String message) {
        System.out.println(GREEN + "┌" + "─".repeat(Math.max(0, message.length() + 2)) + "┐" + RESET);
        System.out.println(GREEN + "│ " + message + " │" + RESET);
        System.out.println(GREEN + "└" + "─".repeat(Math.max(0, message.length() + 2)) + "┘" + RESET);
    }

    /**
     * In thông tin khách hàng.
     *
     * @param customer Khách hàng cần in
     */
    public void printCustomerInfo(Customer customer) {
        if (customer == null) {
            printError("Khách hàng không tồn tại!");
            return;
        }

        System.out.println();
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ " + formatCellLeft("ID: " + customer.getId(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Tên: " + customer.getFullName(), 43) + " │");
        System.out.println("│ " + formatCellLeft("SĐT: " + customer.getPhoneNumber(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Email: " + customer.getEmail(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Địa chỉ: " + customer.getAddress(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Tier: " + customer.getMemberTier(), 43) + " │");
        System.out.println(
                "│ " + formatCellLeft("Tổng chi tiêu: " + formatCurrency(customer.getTotalSpent()), 43) + " │");
        System.out.println("│ "
                + formatCellLeft("Trạng thái: " + (customer.isActive() ? "Hoạt động" : "Không hoạt động"), 43) + " │");
        System.out.println("└─────────────────────────────────────────────┘");
    }

    /**
     * In thông tin dịch vụ.
     *
     * @param service Dịch vụ cần in
     */
    public void printServiceInfo(Service service) {
        if (service == null) {
            printError("Dịch vụ không tồn tại!");
            return;
        }

        System.out.println();
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ " + formatCellLeft("ID: " + service.getId(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Tên: " + service.getServiceName(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Giá: " + formatCurrency(service.getPrice()), 43) + " │");
        System.out.println("│ " + formatCellLeft("Thời gian: " + service.getDurationFormatted(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Loại: " + service.getServiceCategory(), 43) + " │");
        System.out.println(
                "│ " + formatCellLeft("Trạng thái: " + (service.isAvailable() ? "Hoạt động" : "Không hoạt động"), 43)
                        + " │");
        System.out.println("└─────────────────────────────────────────────┘");
    }

    /**
     * In thông tin lịch hẹn.
     *
     * @param appointment Lịch hẹn cần in
     */
    public void printAppointmentInfo(Appointment appointment) {
        if (appointment == null) {
            printError("Lịch hẹn không tồn tại!");
            return;
        }

        System.out.println();
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ " + formatCellLeft("ID: " + appointment.getId(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Khách: " + appointment.getCustomerId(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Dịch vụ: " + appointment.getServiceId(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Thời gian: " + appointment.getAppointmentDateTime(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Trạng thái: " + appointment.getStatus(), 43) + " │");
        System.out.println("│ "
                + formatCellLeft(
                        "Nhân viên: " + (appointment.getStaffId() == null ? "Chưa gán" : appointment.getStaffId()), 43)
                + " │");
        System.out.println("└─────────────────────────────────────────────┘");
    }

    /**
     * In thông tin hóa đơn.
     *
     * @param invoice Hóa đơn cần in
     */
    public void printInvoiceInfo(Invoice invoice) {
        if (invoice == null) {
            printError("Hóa đơn không tồn tại!");
            return;
        }

        System.out.println();
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║           HÓA ĐƠN DỊCH VỤ SPA              ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║ Số hóa đơn: " + formatCellLeft(invoice.getId(), 33) + "║");
        System.out.println("║ Khách hàng: " + formatCellLeft(invoice.getCustomerId(), 33) + "║");
        System.out.println("║ Lịch hẹn:   " + formatCellLeft(invoice.getAppointmentId(), 33) + "║");
        System.out.println("║ Ngày phát hành: " + formatCellLeft(invoice.getIssueDate().toString(), 29) + "║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║ Tổng tiền:     " + formatCurrencyRight(invoice.getSubtotal(), 23) + "║");
        System.out.println("║ Chiết khấu:   -" + formatCurrencyRight(invoice.getDiscountAmount(), 22) + "║");
        System.out.println("║ Thuế (10%):    " + formatCurrencyRight(invoice.getTaxAmount(), 23) + "║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║ Tổng cộng:     " + formatCurrencyRight(invoice.getTotalAmount(), 23) + "║");
        System.out.println(
                "║ Trạng thái: " + formatCellLeft(invoice.isPaid() ? "Đã thanh toán" : "Chưa thanh toán", 33) + "║");
        System.out.println("║ Phương thức: " + formatCellLeft(invoice.getPaymentMethod().toString(), 32) + "║");
        System.out.println("╚═════════════════════════════════════════════╝");
    }

    /**
     * In thông tin giao dịch.
     *
     * @param transaction Giao dịch cần in
     */
    public void printTransactionInfo(Transaction transaction) {
        if (transaction == null) {
            printError("Giao dịch không tồn tại!");
            return;
        }

        System.out.println();
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ " + formatCellLeft("ID: " + transaction.getId(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Khách: " + transaction.getCustomerId(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Số tiền: " + formatCurrency(transaction.getAmount()), 43) + " │");
        System.out.println("│ " + formatCellLeft("Phương thức: " + transaction.getPaymentMethod(), 43) + " │");
        System.out.println("│ " + formatCellLeft("Trạng thái: " + transaction.getStatus(), 43) + " │");
        System.out.println("└─────────────────────────────────────────────┘");
    }

    /**
     * In danh sách khách hàng từ mảy.
     *
     * @param customers Mảy khách hàng
     */
    public void printCustomerList(Customer[] customers) {
        if (customers == null || customers.length == 0) {
            printWarning("Danh sách khách hàng trống!");
            return;
        }

        System.out.println();
        System.out.println("┌────────────────┬──────────────────────┬──────────────────┬──────────────┐");
        System.out.println("│ ID             │ Tên                  │ Số Điện Thoại    │ Tier         │");
        System.out.println("├────────────────┼──────────────────────┼──────────────────┼──────────────┤");

        for (Customer c : customers) {
            System.out.printf("│ %-14s │ %-20s │ %-16s │ %-12s │%n",
                    c.getId(),
                    truncate(c.getFullName(), 20),
                    c.getPhoneNumber(),
                    c.getMemberTier());
        }

        System.out.println("└────────────────┴──────────────────────┴──────────────────┴──────────────┘");
    }

    /**
     * In danh sách dịch vụ từ mảy.
     *
     * @param services Mảy dịch vụ
     */
    public void printServiceList(Service[] services) {
        if (services == null || services.length == 0) {
            printWarning("Danh sách dịch vụ trống!");
            return;
        }

        System.out.println();
        System.out.println("┌────────────────┬──────────────────────┬──────────────┬────────────┐");
        System.out.println("│ ID             │ Tên                  │ Giá          │ Thời gian  │");
        System.out.println("├────────────────┼──────────────────────┼──────────────┼────────────┤");

        for (Service s : services) {
            System.out.printf("│ %-14s │ %-20s │ %12s │ %-10s │%n",
                    s.getId(),
                    truncate(s.getServiceName(), 20),
                    formatCurrency(s.getPrice()),
                    s.getDurationFormatted());
        }

        System.out.println("└────────────────┴──────────────────────┴──────────────┴────────────┘");
    }

    /**
     * In danh sách lịch hẹn từ mảy.
     *
     * @param appointments Mảy lịch hẹn
     */
    public void printAppointmentList(Appointment[] appointments) {
        if (appointments == null || appointments.length == 0) {
            printWarning("Danh sách lịch hẹn trống!");
            return;
        }

        System.out.println();
        System.out.println("┌────────────────┬──────────────┬──────────────┬──────────────┬──────────────┐");
        System.out.println("│ ID             │ Khách        │ Dịch vụ      │ Thời gian    │ Trạng thái   │");
        System.out.println("├────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤");

        for (Appointment a : appointments) {
            System.out.printf("│ %-14s │ %-12s │ %-12s │ %-12s │ %-12s │%n",
                    a.getId(),
                    truncate(a.getCustomerId(), 12),
                    truncate(a.getServiceId(), 12),
                    truncate(a.getAppointmentDateTime().toString(), 12),
                    a.getStatus());
        }

        System.out.println("└────────────────┴──────────────┴──────────────┴──────────────┴──────────────┘");
    }

    /**
     * In danh sách chiết khấu từ mảy.
     *
     * @param discounts Mảy chiết khấu
     */
    public void printDiscountList(Discount[] discounts) {
        if (discounts == null || discounts.length == 0) {
            printWarning("Danh sách chiết khấu trống!");
            return;
        }

        System.out.println();
        System.out.println("┌──────────────┬──────────────┬────────────┬──────────────────────┐");
        System.out.println("│ Mã Khuyến Mãi│ Loại         │ Giá Trị    │ Ngày Kết Thúc       │");
        System.out.println("├──────────────┼──────────────┼────────────┼──────────────────────┤");

        for (Discount d : discounts) {
            System.out.printf("│ %-12s │ %-12s │ %-10s │ %-20s │%n",
                    truncate(d.getDiscountCode(), 12),
                    d.getType(),
                    truncate(d.getValue().toString(), 10),
                    d.getEndDate());
        }

        System.out.println("└──────────────┴──────────────┴────────────┴──────────────────────┘");
    }

    // ============ PHƯƠNG THỨC TIỆN ÍCH (UTILITY) ============

    /**
     * Format tiền tệ VND.
     *
     * @param amount Số tiền
     * @return Chuỗi format tiền tệ (vd: "500.000 VND")
     */
    public String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0 VND";
        }
        return String.format("%,.0f VND", amount);
    }

    /**
     * Format tiền tệ align phải (cho bảng).
     *
     * @param amount Số tiền
     * @param width  Chiều rộng cột
     * @return Chuỗi format tiền tệ align phải
     */
    private String formatCurrencyRight(BigDecimal amount, int width) {
        String formatted = formatCurrency(amount);
        return String.format("%" + width + "s", formatted);
    }

    /**
     * Format cell align trái.
     *
     * @param text  Văn bản
     * @param width Chiều rộng
     * @return Văn bản align trái
     */
    private String formatCellLeft(String text, int width) {
        if (text == null) {
            text = "";
        }
        return String.format("%-" + width + "s", truncate(text, width));
    }

    /**
     * Cắt ngắn chuỗi nếu vượt quá độ dài cho phép.
     *
     * @param text      Chuỗi
     * @param maxLength Độ dài tối đa
     * @return Chuỗi đã cắt ngắn (nếu cần)
     */
    private String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }
}
