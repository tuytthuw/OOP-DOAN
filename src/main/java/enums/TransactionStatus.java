package enums;

public enum TransactionStatus {
    PENDING, // Đang chờ xử lý
    SUCCESS, // Thanh toán thành công
    FAILED, // Thanh toán thất bại
    REFUNDED // Đã hoàn tiền
}
