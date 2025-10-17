package exceptions;

/**
 * Exception khi logic nghiệp vụ không được phép hoặc thất bại.
 * Sử dụng khi: điều kiện kinh doanh không thỏa mãn hoặc thao tác vi phạm
 * business rules.
 * 
 * Ví dụ:
 * - Cập nhật khách hàng không tồn tại
 * - Hoàn thành lịch hẹn khi không ở trạng thái SPENDING
 * - Áp dụng discount không hợp lệ
 * 
 * Mã lỗi: ERR_003
 */
public class BusinessLogicException extends BaseException {

    /**
     * Constructor khởi tạo BusinessLogicException.
     *
     * @param operation Tên thao tác không được phép (VD: "hoàn thành lịch hẹn")
     * @param reason    Lý do tại sao không được phép (VD: "Chỉ lịch hẹn SPENDING
     *                  mới được hoàn thành")
     */
    public BusinessLogicException(String operation, String reason) {
        super("ERR_003",
                "Không thể " + operation + ": " + reason);
    }

    /**
     * Constructor khởi tạo BusinessLogicException với entity context.
     *
     * @param entityName Tên loại entity liên quan
     * @param operation  Tên thao tác
     * @param reason     Lý do
     */
    public BusinessLogicException(String entityName, String operation, String reason) {
        super("ERR_003",
                "Không thể " + operation + " " + entityName + ": " + reason);
    }

    /**
     * Constructor khởi tạo BusinessLogicException với cause.
     *
     * @param operation Tên thao tác không được phép
     * @param reason    Lý do tại sao không được phép
     * @param cause     Ngoại lệ gốc gây ra
     */
    public BusinessLogicException(String operation, String reason, Throwable cause) {
        super("ERR_003",
                "Không thể " + operation + ": " + reason,
                cause);
    }
}
