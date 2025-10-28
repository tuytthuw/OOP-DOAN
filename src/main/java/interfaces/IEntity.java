package interfaces;

/**
 * Đại diện cho thực thể có khóa định danh duy nhất.
 */
public interface IEntity {
    /**
     * @return Mã định danh duy nhất của thực thể.
     */
    String getId();
}
