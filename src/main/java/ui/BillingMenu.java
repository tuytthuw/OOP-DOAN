package ui;

import enums.PaymentMethod;
import enums.TransactionStatus;
import models.Invoice;
import models.Transaction;
import services.AuthService;
import services.InvoiceService;
import services.PaymentService;
import services.PromotionService;

import java.math.BigDecimal;

public class BillingMenu {
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;
    private final PromotionService promotionService;
    private final AuthService authService;
    private final InputHandler inputHandler;
    private final OutputFormatter outputFormatter;

    public BillingMenu(InvoiceService invoiceService,
                       PaymentService paymentService,
                       PromotionService promotionService,
                       AuthService authService,
                       InputHandler inputHandler,
                       OutputFormatter outputFormatter) {
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        this.promotionService = promotionService;
        this.authService = authService;
        this.inputHandler = inputHandler;
        this.outputFormatter = outputFormatter;
    }

    public void show() {
        if (authService.getCurrentSession() == null) {
            outputFormatter.printStatus("Cần đăng nhập trước", false);
            return;
        }
        boolean running = true;
        while (running) {
            System.out.println("=== BILLING MENU ===");
            System.out.println("1. Thanh toán hóa đơn");
            System.out.println("2. Xem giao dịch theo trạng thái");
            System.out.println("0. Quay lại");
            int choice = inputHandler.readInt("Chọn chức năng", 0, 2);
            switch (choice) {
                case 1:
                    processInvoice();
                    break;
                case 2:
                    listTransactions();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    outputFormatter.printStatus("Lựa chọn không hợp lệ", false);
            }
        }
    }

    private void processInvoice() {
        String invoiceId = inputHandler.readString("Mã hóa đơn");
        Invoice invoice = invoiceService.getInvoice(invoiceId);
        if (invoice == null) {
            outputFormatter.printStatus("Không tìm thấy hóa đơn", false);
            return;
        }
        boolean applyPromo = inputHandler.confirm("Áp dụng mã khuyến mãi?");
        if (applyPromo) {
            String code = inputHandler.readString("Nhập mã");
            boolean valid = promotionService.validateCode(code, invoice.getSubtotal());
            if (valid) {
                invoiceService.applyPromotion(invoiceId, code);
                promotionService.markUsage(code);
                outputFormatter.printStatus("Đã áp dụng khuyến mãi", true);
            } else {
                outputFormatter.printStatus("Mã không hợp lệ", false);
            }
        }
        invoiceService.finalizeInvoice(invoiceId);
        invoice = invoiceService.getInvoice(invoiceId);
        String methodInput = inputHandler.readString("Phương thức (CASH/CARD/TRANSFER/E_WALLET)");
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(methodInput.toUpperCase());
        } catch (IllegalArgumentException ex) {
            outputFormatter.printStatus("Phương thức không hợp lệ", false);
            return;
        }
        BigDecimal amount = invoice.getTotalAmount();
        Transaction transaction = paymentService.processPayment(invoiceId, method, amount);
        if (transaction == null) {
            outputFormatter.printStatus("Thanh toán thất bại", false);
            return;
        }
        String[][] rows = {{invoice.getId(), invoice.getCustomerId(), amount.toPlainString()}};
        outputFormatter.printTable(new String[]{"Hóa đơn", "Khách hàng", "Tổng"}, rows);
        outputFormatter.printStatus("Thanh toán thành công", true);
    }

    private void listTransactions() {
        String statusInput = inputHandler.readString("Trạng thái (PENDING/SUCCESS/FAILED/REFUNDED)");
        TransactionStatus status;
        try {
            status = TransactionStatus.valueOf(statusInput.toUpperCase());
        } catch (IllegalArgumentException ex) {
            outputFormatter.printStatus("Trạng thái không hợp lệ", false);
            return;
        }
        Transaction[] transactions = paymentService.listTransactionsByStatus(status);
        String[][] rows = new String[transactions.length][4];
        for (int i = 0; i < transactions.length; i++) {
            Transaction t = transactions[i];
            rows[i][0] = t.getId();
            rows[i][1] = t.getInvoiceId();
            rows[i][2] = t.getStatus().name();
            rows[i][3] = t.getAmount().toPlainString();
        }
        outputFormatter.printTable(new String[]{"ID", "Hóa đơn", "Trạng thái", "Số tiền"}, rows);
    }
}
