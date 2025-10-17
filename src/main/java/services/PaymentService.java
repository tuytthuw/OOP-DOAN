package services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import collections.InvoiceManager;
import collections.TransactionManager;
import enums.PaymentMethod;
import enums.TransactionStatus;
import exceptions.BusinessLogicException;
import exceptions.EntityNotFoundException;
import exceptions.PaymentException;
import exceptions.ValidationException;
import models.Invoice;
import models.Transaction;

/**
 * Service quản lý logic nghiệp vụ thanh toán.
 * Xử lý thanh toán hóa đơn, ghi nhận giao dịch, hoàn tiền và kiểm tra trạng
 * thái.
 * Throws exceptions thay vì return false hoặc in error message.
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
     * @return Giao dịch vừa tạo
     * @throws EntityNotFoundException nếu hóa đơn không tồn tại
     * @throws BusinessLogicException  nếu hóa đơn đã thanh toán
     * @throws PaymentException        nếu xử lý thanh toán thất bại
     */
    public Transaction processPaymentForInvoice(String invoiceId, PaymentMethod paymentMethod)
            throws EntityNotFoundException, BusinessLogicException, PaymentException {

        try {
            // Lấy hóa đơn
            Invoice invoice = invoiceManager.getById(invoiceId);

            // Kiểm tra hóa đơn chưa thanh toán
            if (invoice.isPaid()) {
                throw new BusinessLogicException(
                        "xử lý thanh toán",
                        "Hóa đơn '" + invoiceId + "' đã được thanh toán");
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
            try {
                transactionManager.add(transaction);

                // Đánh dấu hóa đơn đã thanh toán
                invoice.markAsPaid(java.time.LocalDate.now());
                invoiceManager.update(invoice);

                // Cộng chi tiêu vào khách hàng
                try {
                    customerService.addSpendingToCustomer(invoice.getCustomerId(), amount);
                } catch (ValidationException e) {
                    throw new PaymentException("Lỗi cộng chi tiêu: " + e.getErrorMessage());
                }

                // Cập nhật tier khách hàng
                customerService.updateCustomerTier(invoice.getCustomerId());

                return transaction;
            } catch (exceptions.InvalidEntityException | exceptions.EntityAlreadyExistsException e) {
                throw new PaymentException("Lỗi khi ghi nhận giao dịch: " + e.getErrorMessage());
            }

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (exceptions.InvalidEntityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ghi nhận giao dịch từ appointment.
     *
     * @param appointmentId ID appointment
     * @param customerId    ID khách hàng
     * @param amount        Số tiền giao dịch
     * @param paymentMethod Phương thức thanh toán
     * @return Giao dịch vừa tạo
     * @throws ValidationException nếu số tiền không hợp lệ
     * @throws PaymentException    nếu ghi nhận thất bại
     */
    public Transaction recordTransaction(String appointmentId, String customerId, BigDecimal amount,
            PaymentMethod paymentMethod)
            throws ValidationException, PaymentException {

        // Kiểm tra dữ liệu
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Số tiền",
                    "Phải lớn hơn 0");
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
        try {
            transactionManager.add(transaction);
            return transaction;
        } catch (exceptions.InvalidEntityException | exceptions.EntityAlreadyExistsException e) {
            throw new PaymentException("Lỗi khi ghi nhận giao dịch: " + e.getErrorMessage());
        }
    }

    /**
     * Hoàn tiền cho giao dịch.
     *
     * @param transactionId ID giao dịch
     * @param refundAmount  Số tiền hoàn lại
     * @return true nếu hoàn tiền thành công
     * @throws EntityNotFoundException nếu giao dịch không tồn tại
     * @throws BusinessLogicException  nếu không thể hoàn tiền
     * @throws ValidationException     nếu số tiền không hợp lệ
     */
    public boolean refundTransaction(String transactionId, BigDecimal refundAmount)
            throws EntityNotFoundException, BusinessLogicException, ValidationException {

        // Kiểm tra số tiền
        if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Số tiền hoàn",
                    "Phải lớn hơn 0");
        }

        try {
            // Lấy giao dịch
            Transaction transaction = transactionManager.getById(transactionId);

            // Chỉ có thể hoàn tiền giao dịch thành công
            if (transaction.getStatus() != TransactionStatus.SUCCESS) {
                throw new BusinessLogicException(
                        "hoàn tiền",
                        "Chỉ có thể hoàn tiền giao dịch ở trạng thái SUCCESS");
            }

            // Kiểm tra số tiền hoàn không vượt quá số tiền gốc
            if (refundAmount.compareTo(transaction.getAmount()) > 0) {
                throw new ValidationException("Số tiền hoàn",
                        "Không được vượt quá số tiền gốc (" + transaction.getAmount() + ")");
            }

            // Hoàn tiền
            transaction.refundPayment(refundAmount);

            return transactionManager.update(transaction);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (exceptions.InvalidEntityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Kiểm tra trạng thái thanh toán của giao dịch.
     *
     * @param transactionId ID giao dịch
     * @return Chuỗi mô tả trạng thái
     * @throws EntityNotFoundException nếu giao dịch không tồn tại
     */
    public String getPaymentStatus(String transactionId)
            throws EntityNotFoundException {

        try {
            Transaction transaction = transactionManager.getById(transactionId);
            return transaction.getStatus().toString();
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (exceptions.InvalidEntityException e) {
            throw new RuntimeException(e);
        }
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
