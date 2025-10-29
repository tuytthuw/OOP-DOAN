package com.spa.ui;

import com.spa.model.Invoice;
import com.spa.model.Payment;
import com.spa.model.Receptionist;
import com.spa.model.enums.PaymentMethod;
import com.spa.service.Validation;
import java.time.LocalDate;

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
        for (Payment payment : payments) {
            if (payment == null) {
                continue;
            }
            hasData = true;
            String invoiceId = payment.getInvoice() == null ? "" : payment.getInvoice().getId();
            String receptionistId = payment.getReceptionist() == null ? "" : payment.getReceptionist().getId();
            System.out.printf("%s | Hóa đơn: %s | %.2f | %s | %s%n",
                    payment.getId(),
                    invoiceId,
                    payment.getAmount(),
                    payment.getPaymentMethod(),
                    receptionistId);
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
        Receptionist receptionist = pickReceptionist();
        LocalDate date = Validation.getDate("Ngày thanh toán (yyyy-MM-dd): ", DATE_FORMAT);

        Payment payment = new Payment(id, invoice, amount, method, receptionist, date);
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

    private Receptionist pickReceptionist() {
        Receptionist[] employees = context.getEmployeeStore().getAllReceptionists();
        if (employees.length == 0) {
            System.out.println("Không có lễ tân nào, thanh toán sẽ không gắn người thực hiện.");
            return null;
        }
        System.out.println("Chọn lễ tân thực hiện thanh toán:");
        for (int i = 0; i < employees.length; i++) {
            Receptionist receptionist = employees[i];
            System.out.printf("%d. %s - %s%n", i + 1, receptionist.getId(), receptionist.getFullName());
        }
        int selected = Validation.getInt("Lựa chọn (0 để bỏ qua): ", 0, employees.length);
        if (selected == 0) {
            return null;
        }
        return context.getEmployeeStore().findReceptionistById(employees[selected - 1].getId());
    }
}
