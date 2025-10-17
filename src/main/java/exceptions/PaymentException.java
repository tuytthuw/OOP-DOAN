package exceptions;

/**
 * Exception khi xử lý thanh toán thất bại.
 * Sử dụng khi: payment processing error, không đủ số tiền, phương thức thanh
 * toán không hợp lệ.
 * 
 * Ví dụ:
 * - Số tiền thanh toán không đủ
 * - Phương thức thanh toán không hỗ trợ
 * - Invoice chưa được tạo
 * 
 * Mã lỗi: ERR_004
 */
public class PaymentException extends BaseException {

    /**
     * Constructor khởi tạo PaymentException.
     *
     * @param reason Lý do thanh toán thất bại (VD: "Số tiền thanh toán không đủ")
     */
    public PaymentException(String reason) {
        super("ERR_004", "Lỗi thanh toán: " + reason);
    }

    /**
     * Constructor khởi tạo PaymentException với invoice context.
     *
     * @param invoiceId ID của hóa đơn
     * @param reason    Lý do thanh toán thất bại
     */
    public PaymentException(String invoiceId, String reason) {
        super("ERR_004",
                "Lỗi thanh toán (Hóa đơn: " + invoiceId + "): " + reason);
    }

    /**
     * Constructor khởi tạo PaymentException với cause.
     *
     * @param reason Lý do thanh toán thất bại
     * @param cause  Ngoại lệ gốc gây ra
     */
    public PaymentException(String reason, Throwable cause) {
        super("ERR_004", "Lỗi thanh toán: " + reason, cause);
    }
}
