package com.spa.ui;

import com.spa.model.Employee;
import com.spa.model.Invoice;
import com.spa.model.Payment;
import com.spa.model.Receptionist;
import com.spa.model.enums.PaymentMethod;
import com.spa.service.Validation;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Menu quản lý thanh toán.
 */
public class PaymentMenu implements MenuModule {
    private final MenuContext context;

    public PaymentMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ THANH TOÁN ---");
            System.out.println("1. Xem danh sách thanh toán");
            System.out.println("2. Ghi nhận thanh toán");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 2);
            switch (choice) {
                case 1:
                    listPayments();
                    Validation.pause();
                    break;
                case 2:
                    recordPayment();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listPayments() {
        System.out.println();
        System.out.println("--- DANH SÁCH THANH TOÁN ---");
        Payment[] payments = context.getPaymentStore().getAll();
        boolean hasData = false;
        String header = paymentHeader();
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        for (Payment payment : payments) {
            if (payment == null) {
                continue;
            }
            printPaymentRow(payment);
            hasData = true;
        }
        if (!hasData) {
            System.out.println("Chưa có thanh toán nào.");
        }
    }

    private void recordPayment() {
        System.out.println();
        System.out.println("--- GHI NHẬN THANH TOÁN ---");
        String id = context.getPaymentStore().generateNextId();
        System.out.println("Mã thanh toán được cấp: " + id);
        Invoice invoice = pickInvoice();
        if (invoice == null) {
            System.out.println("Chưa chọn hóa đơn hợp lệ.");
            return;
        }
        double amount = Validation.getDouble("Số tiền thanh toán: ", 0.0, 1_000_000_000.0);
        PaymentMethod method = MenuHelper.selectPaymentMethod();
        Receptionist receptionist = resolveCurrentReceptionist();
        if (receptionist == null) {
            System.out.println("Vui lòng đăng nhập để ghi nhận thanh toán.");
            return;
        }
        String note = Validation.getOptionalString("Ghi chú thanh toán: ");

        Payment payment = new Payment(id, invoice, amount, method, receptionist,
                LocalDateTime.now(), note, false);
        if (!payment.processPayment()) {
            System.out.println("Thanh toán chưa đủ số tiền yêu cầu hoặc hóa đơn không hợp lệ.");
            return;
        }
        context.getPaymentStore().add(payment);
        context.getPaymentStore().writeFile();
        context.getInvoiceStore().writeFile();
        System.out.println("Đã ghi nhận thanh toán thành công.");
    }

    private Invoice pickInvoice() {
        Invoice[] invoices = context.getInvoiceStore().getAll();
        Invoice[] unpaid = filterUnpaidInvoices(invoices);
        if (unpaid.length == 0) {
            System.out.println("Không còn hóa đơn nào cần thanh toán.");
            return null;
        }
        System.out.println("Chọn hóa đơn cần thanh toán:");
        for (int i = 0; i < unpaid.length; i++) {
            Invoice invoice = unpaid[i];
            System.out.printf("%d. %s - Tổng tiền: %.2f%n", i + 1, invoice.getId(), invoice.getTotalAmount());
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, unpaid.length);
        return context.getInvoiceStore().findById(unpaid[selected - 1].getId());
    }

    private Invoice[] filterUnpaidInvoices(Invoice[] invoices) {
        int count = 0;
        for (Invoice invoice : invoices) {
            if (invoice != null && !invoice.isStatus()) {
                count++;
            }
        }
        Invoice[] result = new Invoice[count];
        int index = 0;
        for (Invoice invoice : invoices) {
            if (invoice != null && !invoice.isStatus()) {
                result[index] = invoice;
                index++;
            }
        }
        return result;
    }

    private Receptionist resolveCurrentReceptionist() {
        if (context.getAuthService() == null || !context.getAuthService().isLoggedIn()) {
            return null;
        }
        Employee current = context.getAuthService().getCurrentUser();
        if (current == null) {
            return null;
        }
        Employee stored = context.getEmployeeStore().findById(current.getId());
        return MenuHelper.toReceptionistView(stored != null ? stored : current);
    }

    private String paymentHeader() {
        return String.format("%-8s | %-18s | %-10s | %-12s | %-18s | %-16s | %-7s | %-20s",
                "MÃ", "HÓA ĐƠN", "SỐ TIỀN", "PHƯƠNG THỨC", "LỄ TÂN", "THỜI GIAN", "HOÀN", "GHI CHÚ");
    }

    private void printPaymentRow(Payment payment) {
        String invoiceId = payment.getInvoice() == null ? "" : payment.getInvoice().getId();
        String receptionistName = payment.getReceptionist() == null ? "" : payment.getReceptionist().getFullName();
        String note = limitLength(payment.getNote(), 20);
        System.out.printf("%-8s | %-18s | %-10.2f | %-12s | %-18s | %-16s | %-7s | %-20s%n",
                nullToEmpty(payment.getId()),
                nullToEmpty(invoiceId),
                payment.getAmount(),
                payment.getPaymentMethod() == null ? "" : payment.getPaymentMethod().name(),
                nullToEmpty(receptionistName),
                formatDateTime(payment.getPaymentDate()),
                payment.isRefunded() ? "Có" : "Không",
                nullToEmpty(note));
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private String limitLength(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    private String formatDateTime(LocalDateTime time) {
        if (time == null) {
            return "";
        }
        return time.format(MenuConstants.DATE_TIME_FORMAT);
    }
}
