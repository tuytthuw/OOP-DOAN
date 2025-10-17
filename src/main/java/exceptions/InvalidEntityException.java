package exceptions;

/**
 * Exception khi entity không hợp lệ (validation thất bại).
 * Sử dụng khi: dữ liệu entity không đáp ứng yêu cầu (NULL, rỗng, format sai,
 * v.v.).
 * 
 * Mã lỗi: ERR_002 (Overlaps with EntityAlreadyExistsException, có thể dùng
 * ERR_002a)
 * Lưu ý: Có thể gộp với EntityAlreadyExistsException hoặc tách thành ERR_002a
 */
public class InvalidEntityException extends BaseException {

    /**
     * Constructor khởi tạo InvalidEntityException.
     *
     * @param entityName Tên loại entity (VD: "Customer", "Service")
     * @param reason     Lý do entity không hợp lệ (VD: "Tên không được để trống")
     */
    public InvalidEntityException(String entityName, String reason) {
        super("ERR_002",
                "Entity " + entityName + " không hợp lệ: " + reason);
    }

    /**
     * Constructor khởi tạo InvalidEntityException với trường và giá trị.
     *
     * @param entityName Tên loại entity
     * @param fieldName  Tên trường không hợp lệ
     * @param fieldValue Giá trị của trường
     * @param reason     Lý do tại sao không hợp lệ
     */
    public InvalidEntityException(String entityName, String fieldName, String fieldValue, String reason) {
        super("ERR_002",
                "Entity " + entityName + " - Trường '" + fieldName + "' (giá trị: '" + fieldValue + "'): " + reason);
    }

    /**
     * Constructor khởi tạo InvalidEntityException với cause.
     *
     * @param entityName Tên loại entity
     * @param reason     Lý do không hợp lệ
     * @param cause      Ngoại lệ gốc gây ra
     */
    public InvalidEntityException(String entityName, String reason, Throwable cause) {
        super("ERR_002",
                "Entity " + entityName + " không hợp lệ: " + reason,
                cause);
    }
}
