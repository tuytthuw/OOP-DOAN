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
            System.out.println("3. Danh sách hóa đơn");
            System.out.println("4. Danh sách giao dịch");
            System.out.println("5. Danh sách khuyến mãi");
            System.out.println("0. Quay lại");
            int choice = inputHandler.readInt("Chọn chức năng", 0, 5);
            switch (choice) {
                case 1:
                    processInvoice();
                    break;
                case 2:
                    listTransactions();
                    break;
                case 3:
                    listInvoices();
                    break;
                case 4:
                    listAllTransactions();
                    break;
                case 5:
                    listPromotions();
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

    private void listInvoices() {
        Invoice[] invoices = invoiceService.getAllInvoices();
        if (invoices.length == 0) {
            outputFormatter.printStatus("Chưa có hóa đơn", false);
            return;
        }
        String[][] rows = new String[invoices.length][5];
        for (int i = 0; i < invoices.length; i++) {
            Invoice invoice = invoices[i];
            rows[i][0] = invoice.getId();
            rows[i][1] = invoice.getCustomerId();
            rows[i][2] = invoice.getAppointmentId();
            rows[i][3] = invoice.getTotalAmount().toPlainString();
            rows[i][4] = invoice.isPaid() ? "Đã thanh toán" : "Chưa";
        }
        outputFormatter.printTable(new String[]{"ID", "Khách", "Lịch hẹn", "Tổng", "Trạng thái"}, rows);
    }

    private void listAllTransactions() {
        Transaction[] transactions = paymentService.listAllTransactions();
        if (transactions.length == 0) {
            outputFormatter.printStatus("Chưa có giao dịch", false);
            return;
        }
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

    private void listPromotions() {
        models.Promotion[] promotions = promotionService.getAllPromotions();
        if (promotions.length == 0) {
            outputFormatter.printStatus("Chưa có khuyến mãi", false);
            return;
        }
        String[][] rows = new String[promotions.length][5];
        for (int i = 0; i < promotions.length; i++) {
            models.Promotion promotion = promotions[i];
            rows[i][0] = promotion.getId();
            rows[i][1] = promotion.getCode();
            rows[i][2] = promotion.getType().name();
            rows[i][3] = promotion.getValue().toPlainString();
            rows[i][4] = promotion.isActive() ? "Đang hoạt động" : "Ngưng";
        }
        outputFormatter.printTable(new String[]{"ID", "Mã", "Loại", "Giá trị", "Trạng thái"}, rows);
    }
}
