package services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import collections.InvoiceManager;
import collections.TransactionManager;
import enums.PaymentMethod;
import enums.TransactionStatus;
import models.Invoice;
import models.Transaction;

/**
 * Service quản lý logic nghiệp vụ thanh toán.
 * Xử lý thanh toán hóa đơn, ghi nhận giao dịch, hoàn tiền và kiểm tra trạng
 * thái.
 */
public class PaymentService {

    private TransactionManager transactionManager;
    private InvoiceManager invoiceManager;
    private CustomerService customerService;

    /**
     * Constructor khởi tạo PaymentService.
     *
     * @param transactionManager Manager quản lý giao dịch
     * @param invoiceManager     Manager quản lý hóa đơn
     * @param customerService    Service quản lý khách hàng
     */
    public PaymentService(TransactionManager transactionManager, InvoiceManager invoiceManager,
            CustomerService customerService) {
        this.transactionManager = transactionManager;
        this.invoiceManager = invoiceManager;
        this.customerService = customerService;
    }

    /**
     * Xử lý thanh toán cho hóa đơn.
     * Tạo giao dịch, cập nhật trạng thái hóa đơn, và cộng chi tiêu khách hàng.
     *
     * @param invoiceId     ID hóa đơn
     * @param paymentMethod Phương thức thanh toán
     * @return Giao dịch vừa tạo hoặc null nếu thất bại
     */
    public Transaction processPaymentForInvoice(String invoiceId, PaymentMethod paymentMethod) {
        // Lấy hóa đơn
        Invoice invoice = invoiceManager.getById(invoiceId);
        if (invoice == null) {
            System.out.println("❌ Hóa đơn không tồn tại!");
            return null;
        }

        // Kiểm tra hóa đơn chưa thanh toán
        if (invoice.isPaid()) {
            System.out.println("❌ Hóa đơn đã được thanh toán!");
            return null;
        }

        // Lấy số tiền thanh toán từ hóa đơn
        BigDecimal amount = invoice.getTotalAmount();

        // Sinh ID mới
        String transactionId = generateTransactionId();

        // Tạo giao dịch mới
        Transaction transaction = new Transaction(
                transactionId,
                invoice.getAppointmentId(),
                invoice.getCustomerId(),
                amount,
                paymentMethod,
                LocalDateTime.now(),
                TransactionStatus.PENDING,
                null,
                null,
                BigDecimal.ZERO);

        // Xử lý thanh toán
        transaction.processPayment();

        // Thêm vào Manager
        if (transactionManager.add(transaction)) {
            // Đánh dấu hóa đơn đã thanh toán
            invoice.markAsPaid(java.time.LocalDate.now());
            invoiceManager.update(invoice);

            // Cộng chi tiêu vào khách hàng
            customerService.addSpendingToCustomer(invoice.getCustomerId(), amount);

            // Cập nhật tier khách hàng
            customerService.updateCustomerTier(invoice.getCustomerId());

            System.out.println("✅ Xử lý thanh toán thành công!");
            return transaction;
        }

        System.out.println("❌ Lỗi khi xử lý thanh toán!");
        return null;
    }

    /**
     * Ghi nhận giao dịch từ appointment.
     *
     * @param appointmentId ID appointment
     * @param customerId    ID khách hàng
     * @param amount        Số tiền giao dịch
     * @param paymentMethod Phương thức thanh toán
     * @return Giao dịch vừa tạo hoặc null nếu thất bại
     */
    public Transaction recordTransaction(String appointmentId, String customerId, BigDecimal amount,
            PaymentMethod paymentMethod) {
        // Kiểm tra dữ liệu
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ Số tiền không hợp lệ!");
            return null;
        }

        // Sinh ID mới
        String transactionId = generateTransactionId();

        // Tạo giao dịch mới
        Transaction transaction = new Transaction(
                transactionId,
                appointmentId,
                customerId,
                amount,
                paymentMethod,
                LocalDateTime.now(),
                TransactionStatus.SUCCESS,
                null,
                null,
                BigDecimal.ZERO);

        // Thêm vào Manager
        if (transactionManager.add(transaction)) {
            System.out.println("✅ Ghi nhận giao dịch thành công!");
            return transaction;
        }

        System.out.println("❌ Lỗi khi ghi nhận giao dịch!");
        return null;
    }

    /**
     * Hoàn tiền cho giao dịch.
     *
     * @param transactionId ID giao dịch
     * @param refundAmount  Số tiền hoàn lại
     * @return true nếu hoàn tiền thành công, false nếu thất bại
     */
    public boolean refundTransaction(String transactionId, BigDecimal refundAmount) {
        // Lấy giao dịch
        Transaction transaction = transactionManager.getById(transactionId);
        if (transaction == null) {
            System.out.println("❌ Giao dịch không tồn tại!");
            return false;
        }

        // Chỉ có thể hoàn tiền giao dịch thành công
        if (transaction.getStatus() != TransactionStatus.SUCCESS) {
            System.out.println("❌ Chỉ có thể hoàn tiền giao dịch thành công!");
            return false;
        }

        // Kiểm tra số tiền hoàn không vượt quá số tiền gốc
        if (refundAmount.compareTo(transaction.getAmount()) > 0) {
            System.out.println("❌ Số tiền hoàn không được vượt quá số tiền gốc!");
            return false;
        }

        // Hoàn tiền
        transaction.refundPayment(refundAmount);

        if (transactionManager.update(transaction)) {
            System.out.println("✅ Hoàn tiền thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi hoàn tiền!");
        return false;
    }

    /**
     * Kiểm tra trạng thái thanh toán của giao dịch.
     *
     * @param transactionId ID giao dịch
     * @return Chuỗi mô tả trạng thái hoặc chuỗi rỗng nếu giao dịch không tồn tại
     */
    public String getPaymentStatus(String transactionId) {
        Transaction transaction = transactionManager.getById(transactionId);
        if (transaction == null) {
            System.out.println("❌ Giao dịch không tồn tại!");
            return "";
        }

        return transaction.getStatus().toString();
    }

    /**
     * Sinh ID giao dịch mới theo định dạng TXN_XXXXXX.
     *
     * @return ID giao dịch mới
     */
    private String generateTransactionId() {
        return "TXN_" + System.currentTimeMillis();
    }
}
