package models;

import enums.PaymentMethod;
import enums.TransactionStatus;
import interfaces.IEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements IEntity {
    private static final String ID_PREFIX = "TRX";
    private static int runningNumber = 1;

    private final String transactionId;
    private String invoiceId;
    private BigDecimal amount;
    private PaymentMethod method;
    private TransactionStatus status;
    private String referenceCode;
    private LocalDateTime processedAt;
    private String failureReason;

    public Transaction(String invoiceId,
                       BigDecimal amount,
                       PaymentMethod method) {
        this.transactionId = generateId();
        this.invoiceId = invoiceId;
        this.amount = amount == null ? BigDecimal.ZERO : amount;
        this.method = method;
        this.status = TransactionStatus.PENDING;
        this.processedAt = LocalDateTime.now();
    }

    private String generateId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    @Override
    public String getId() {
        return transactionId;
    }

    public void markSuccess(String referenceCode) {
        this.status = TransactionStatus.SUCCESS;
        this.referenceCode = referenceCode;
        this.failureReason = null;
        this.processedAt = LocalDateTime.now();
    }

    public void markFailed(String reason) {
        this.status = TransactionStatus.FAILED;
        this.failureReason = reason;
        this.referenceCode = null;
        this.processedAt = LocalDateTime.now();
    }

    public void refund(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return;
        }
        this.status = TransactionStatus.REFUNDED;
        this.amount = amount;
        this.processedAt = LocalDateTime.now();
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
