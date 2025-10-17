package exceptions;

/**
 * Exception khi validation input từ người dùng thất bại.
 * Sử dụng khi: InputHandler validate input không hợp lệ.
 * 
 * Ví dụ:
 * - Email không hợp lệ
 * - Số điện thoại format sai
 * - Tuổi nhập vào không hợp lệ
 * - Giá nhập vào âm
 * 
 * Mã lỗi: ERR_006
 */
public class ValidationException extends BaseException {

    /**
     * Constructor khởi tạo ValidationException.
     *
     * @param fieldName   Tên trường validation thất bại (VD: "Email", "Số điện
     *                    thoại")
     * @param requirement Yêu cầu hoặc format (VD: "Email phải là định dạng
     *                    abc@xyz.com")
     */
    public ValidationException(String fieldName, String requirement) {
        super("ERR_006",
                "Validation thất bại - " + fieldName + ": " + requirement);
    }

    /**
     * Constructor khởi tạo ValidationException với giá trị đã nhập.
     *
     * @param fieldName   Tên trường
     * @param inputValue  Giá trị người dùng nhập
     * @param requirement Yêu cầu
     */
    public ValidationException(String fieldName, String inputValue, String requirement) {
        super("ERR_006",
                "Validation thất bại - " + fieldName + " (giá trị: '" + inputValue + "'): " + requirement);
    }

    /**
     * Constructor khởi tạo ValidationException với cause.
     *
     * @param fieldName   Tên trường
     * @param requirement Yêu cầu
     * @param cause       Ngoại lệ gốc gây ra
     */
    public ValidationException(String fieldName, String requirement, Throwable cause) {
        super("ERR_006",
                "Validation thất bại - " + fieldName + ": " + requirement,
                cause);
    }
}
