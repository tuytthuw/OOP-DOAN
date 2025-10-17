package exceptions;

/**
 * Exception khi entity đã tồn tại trong database/manager.
 * Sử dụng khi: thêm entity mới nhưng ID hoặc unique field đã tồn tại.
 * 
 * Mã lỗi: ERR_002
 */
public class EntityAlreadyExistsException extends BaseException {

    /**
     * Constructor khởi tạo EntityAlreadyExistsException.
     *
     * @param entityName Tên loại entity (VD: "Customer", "Service")
     * @param fieldName  Tên field unique (VD: "ID", "Email", "Phone Number")
     * @param fieldValue Giá trị của field đã tồn tại
     */
    public EntityAlreadyExistsException(String entityName, String fieldName, String fieldValue) {
        super("ERR_002",
                entityName + " với " + fieldName + " '" + fieldValue + "' đã tồn tại");
    }

    /**
     * Constructor khởi tạo EntityAlreadyExistsException với ID.
     *
     * @param entityName Tên loại entity
     * @param id         ID đã tồn tại
     */
    public EntityAlreadyExistsException(String entityName, String id) {
        super("ERR_002",
                entityName + " với ID '" + id + "' đã tồn tại");
    }

    /**
     * Constructor khởi tạo EntityAlreadyExistsException với cause.
     *
     * @param entityName Tên loại entity
     * @param id         ID đã tồn tại
     * @param cause      Ngoại lệ gốc gây ra
     */
    public EntityAlreadyExistsException(String entityName, String id, Throwable cause) {
        super("ERR_002",
                entityName + " với ID '" + id + "' đã tồn tại",
                cause);
    }
}
