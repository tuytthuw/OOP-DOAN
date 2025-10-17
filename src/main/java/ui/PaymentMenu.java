package ui;

import java.math.BigDecimal;

import collections.TransactionManager;
import enums.PaymentMethod;
import exceptions.BusinessLogicException;
import exceptions.EntityNotFoundException;
import exceptions.PaymentException;
import io.InputHandler;
import io.OutputFormatter;
import models.Invoice;
import models.Transaction;
import services.InvoiceService;
import services.PaymentService;

/**
 * Menu quản lý thanh toán.
 * Cung cấp các chức năng: tạo hóa đơn, áp dụng giảm giá, xử lý thanh toán, hoàn
 * lại tiền.
 */
public class PaymentMenu extends MenuBase {

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    private PaymentService paymentService;
    private InvoiceService invoiceService;
    private TransactionManager transactionManager;

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo PaymentMenu.
     *
     * @param inputHandler       Bộ xử lý input
     * @param outputFormatter    Bộ định dạng output
     * @param paymentService     Dịch vụ thanh toán
     * @param invoiceService     Dịch vụ hóa đơn
     * @param transactionManager Manager giao dịch
     */
    public PaymentMenu(InputHandler inputHandler, OutputFormatter outputFormatter,
            PaymentService paymentService, InvoiceService invoiceService,
            TransactionManager transactionManager) {
        super(inputHandler, outputFormatter);
        this.paymentService = paymentService;
        this.invoiceService = invoiceService;
        this.transactionManager = transactionManager;
    }

    // ============ PHƯƠNG THỨC OVERRIDE (OVERRIDE METHODS) ============

    @Override
    protected void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   QUẢN LÝ THANH TOÁN                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Tạo hóa đơn mới");
        System.out.println("2. Áp dụng mã giảm giá");
        System.out.println("3. Xử lý thanh toán");
        System.out.println("4. Xem lịch sử thanh toán");
        System.out.println("5. Hoàn lại tiền");
        System.out.println("6. Quay lại");
        System.out.println();
    }

    @Override
    protected boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                createInvoice();
                return true;

            case 2:
                applyDiscount();
                return true;

            case 3:
                processPayment();
                return true;

            case 4:
                viewPaymentHistory();
                return true;

            case 5:
                refundPayment();
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
     * Tạo hóa đơn mới.
     */
    private void createInvoice() {
        System.out.println("\n--- Tạo Hóa Đơn Mới ---");

        try {
            String appointmentId = inputHandler.readString("Nhập ID lịch hẹn: ");
            String paymentMethodStr = inputHandler.readString("Nhập phương thức thanh toán (CASH, CARD, TRANSFER): ");

            PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentMethodStr.toUpperCase());

            Invoice invoice = invoiceService.createInvoiceForAppointment(appointmentId, paymentMethod);

            System.out.println("\n✓ Hóa đơn đã được tạo thành công!");
            System.out.println("  ID: " + invoice.getId());
            System.out.println("  Số tiền: " + invoice.getTotalAmount());

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy: " + e.getFormattedError());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getFormattedError());
            pauseForContinue();
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Phương thức thanh toán không hợp lệ!");
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Áp dụng mã giảm giá cho hóa đơn.
     */
    private void applyDiscount() {
        System.out.println("\n--- Áp Dụng Mã Giảm Giá ---");

        try {
            String invoiceId = inputHandler.readString("Nhập ID hóa đơn: ");
            String discountId = inputHandler.readString("Nhập ID mã giảm giá: ");

            invoiceService.applyDiscountToInvoice(invoiceId, discountId);

            System.out.println("✓ Mã giảm giá đã được áp dụng!");

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy: " + e.getFormattedError());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getFormattedError());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xử lý thanh toán cho hóa đơn.
     */
    private void processPayment() {
        System.out.println("\n--- Xử Lý Thanh Toán ---");

        try {
            String invoiceId = inputHandler.readString("Nhập ID hóa đơn: ");
            String paymentMethodStr = inputHandler.readString("Nhập phương thức thanh toán (CASH, CARD, TRANSFER): ");

            PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentMethodStr.toUpperCase());

            paymentService.processPaymentForInvoice(invoiceId, paymentMethod);

            System.out.println("✓ Thanh toán đã được xử lý thành công!");

            pauseForContinue();

        } catch (PaymentException e) {
            System.out.println("❌ Lỗi thanh toán: " + e.getFormattedError());
            pauseForContinue();
        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy: " + e.getFormattedError());
            pauseForContinue();
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Phương thức thanh toán không hợp lệ!");
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xem lịch sử thanh toán.
     */
    private void viewPaymentHistory() {
        System.out.println("\n--- Lịch Sử Thanh Toán ---");

        try {
            Transaction[] transactions = transactionManager.getAll();

            if (transactions == null || transactions.length == 0) {
                System.out.println("ℹ Không có giao dịch nào.");
            } else {
                System.out.println();
                for (Transaction t : transactions) {
                    if (t != null) {
                        System.out.println("  • " + t.getId() + " - " + t.getAppointmentId() +
                                " - " + t.getAmount() + " VND - " + t.getPaymentMethod());
                    }
                }
            }

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Hoàn lại tiền cho giao dịch.
     */
    private void refundPayment() {
        System.out.println("\n--- Hoàn Lại Tiền ---");

        try {
            String transactionId = inputHandler.readString("Nhập ID giao dịch cần hoàn lại: ");
            BigDecimal refundAmount = inputHandler.readBigDecimal("Nhập số tiền cần hoàn lại: ");

            paymentService.refundTransaction(transactionId, refundAmount);

            System.out.println("✓ Giao dịch đã được hoàn lại!");

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy giao dịch: " + e.getFormattedError());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getFormattedError());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }
}
