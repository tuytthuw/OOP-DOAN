package models;

import enums.PaymentMethod;
import enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Transaction {
    private String transactionId;
    private String appointmentId;
    private String customerId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
    private String referenceCode;
    private String notes;
    private BigDecimal refundedAmount;
    //Constructor
    public Transaction(String transactionId, String appointmentId, String customerId, BigDecimal amount, PaymentMethod paymentMethod, LocalDateTime transactionDate, TransactionStatus status , String referenceCode, String notes, BigDecimal refundedAmount ){
        this.transactionId = transactionId;
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionDate = transactionDate;
        this.status = status;
        this.referenceCode = referenceCode;
        this.notes = notes;
        this.refundedAmount = refundedAmount;
    }
    //Xử  lý thanh toán
    public void processPayment(){
        this.status = TransactionStatus.SUCCESS;
    }
    //Thanh toán thất bại
    public void failPayment(String reason){
        this.status = TransactionStatus.FAILED;
        this.notes = reason;
    }
    //Hoàn tiền
    public void refundPayment(BigDecimal amount){
        if (this.status == TransactionStatus.SUCCESS){
            this.refundedAmount = this.refundedAmount.add(amount);
            this.status = TransactionStatus.REFUNDED;
        }
    }
    //Lấy thông tin chi tiết
    public String getTransactionInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("""
                Giao dịch: %s
                Khách hàng: %s
                Lịch hẹn: %s
                Số tiền: %s VND
                Phương thức: %s
                Ngày: %s
                Trạng thái: %s
                Ghi chú: %s
                Hoàn lại: %s VND
                """,
                transactionId,
                customerId,
                appointmentId,
                getAmountFormatted(),
                paymentMethod,
                transactionDate.format(formatter),
                status,
                (notes == null ? "Không" : notes),
                refundedAmount);
    }
    //Định dạng tiền
    public String getAmountFormatted() {
        return String.format("%,.0f", amount);
    }
    //Kiểm tra giao dịch thành công
    public boolean isSuccessful() {
        return this.status == TransactionStatus.SUCCESS;
    }
    //Getter/Setter
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public String getReferenceCode() { return referenceCode; }
    public void setReferenceCode(String referenceCode) { this.referenceCode = referenceCode; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public BigDecimal getRefundedAmount() { return refundedAmount; }
    public void setRefundedAmount(BigDecimal refundedAmount) { this.refundedAmount = refundedAmount; }
    //Equals & hashCode theo transactionId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
