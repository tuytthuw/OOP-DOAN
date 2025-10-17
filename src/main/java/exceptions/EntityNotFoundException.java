package exceptions;

/**
 * Exception khi không tìm thấy entity trong database/manager.
 * Sử dụng khi: getById() trả về null hoặc entity không tồn tại.
 * 
 * Mã lỗi: ERR_001
 */
public class EntityNotFoundException extends BaseException {

    /**
     * Constructor khởi tạo EntityNotFoundException.
     *
     * @param entityName Tên loại entity (VD: "Customer", "Service", "Appointment")
     * @param id         ID của entity không tìm thấy
     */
    public EntityNotFoundException(String entityName, String id) {
        super("ERR_001",
                "Không tìm thấy " + entityName + " với ID: " + id);
    }

    /**
     * Constructor khởi tạo EntityNotFoundException với thông báo tùy chỉnh.
     *
     * @param entityName     Tên loại entity
     * @param searchCriteria Tiêu chí tìm kiếm (có thể là ID, email, phone, v.v.)
     * @param searchValue    Giá trị tìm kiếm
     */
    public EntityNotFoundException(String entityName, String searchCriteria, String searchValue) {
        super("ERR_001",
                "Không tìm thấy " + entityName + " theo " + searchCriteria + ": " + searchValue);
    }

    /**
     * Constructor khởi tạo EntityNotFoundException với cause (nguyên nhân).
     *
     * @param entityName Tên loại entity
     * @param id         ID của entity
     * @param cause      Ngoại lệ gốc gây ra
     */
    public EntityNotFoundException(String entityName, String id, Throwable cause) {
        super("ERR_001",
                "Không tìm thấy " + entityName + " với ID: " + id,
                cause);
    }
}
