package exceptions;

/**
 * Bộ xử lý exception toàn cục (Global Exception Handler) - Singleton.
 * Cung cấp các phương thức để xử lý, log, và hiển thị exception cho người dùng.
 * 
 * Đây là nơi tập trung xử lý tất cả các exception trong hệ thống.
 */
public class ExceptionHandler {

    // ============ SINGLETON PATTERN ============

    /** Instance duy nhất của ExceptionHandler */
    private static ExceptionHandler instance;

    /**
     * Lấy instance duy nhất của ExceptionHandler (Singleton).
     *
     * @return Instance của ExceptionHandler
     */
    public static synchronized ExceptionHandler getInstance() {
        if (instance == null) {
            instance = new ExceptionHandler();
        }
        return instance;
    }

    /**
     * Constructor private để tránh khởi tạo từ bên ngoài.
     */
    private ExceptionHandler() {
    }

    // ============ PHƯƠNG THỨC XỬ LÝ EXCEPTION ============

    /**
     * Xử lý exception toàn cục.
     * Thực hiện các bước:
     * 1. Ghi log exception
     * 2. Phân loại exception
     * 3. Trả về error message phù hợp
     *
     * @param exception Exception cần xử lý
     * @return Error message để hiển thị cho người dùng
     */
    public String handleException(Exception exception) {
        // Ghi log exception
        logException(exception);

        // Phân loại và xử lý
        if (exception instanceof BaseException) {
            BaseException baseEx = (BaseException) exception;
            return baseEx.getFormattedError();
        } else if (exception instanceof NullPointerException) {
            return formatSystemError("Lỗi Null Pointer",
                    "Dữ liệu bị thiếu hoặc không tồn tại");
        } else if (exception instanceof IllegalArgumentException) {
            return formatSystemError("Lỗi Tham Số",
                    exception.getMessage());
        } else if (exception instanceof NumberFormatException) {
            return formatSystemError("Lỗi Định Dạng Số",
                    "Vui lòng nhập số hợp lệ");
        } else {
            // Generic exception
            return formatSystemError("Lỗi Không Xác Định",
                    exception.getMessage() != null
                            ? exception.getMessage()
                            : "Đã xảy ra lỗi không mong đợi");
        }
    }

    /**
     * Ghi log exception ra console và file (tùy chọn).
     *
     * @param exception Exception cần ghi log
     */
    public void logException(Exception exception) {
        if (exception instanceof BaseException) {
            BaseException baseEx = (BaseException) exception;
            baseEx.logError("[EXCEPTION_HANDLER]");
        } else {
            // Log generic exception
            System.err.println("[EXCEPTION_HANDLER] " + exception.getClass().getSimpleName());
            exception.printStackTrace(System.err);
        }
    }

    /**
     * Hiển thị error message cho người dùng ra console.
     *
     * @param exception Exception cần hiển thị
     */
    public void displayErrorToUser(Exception exception) {
        String errorMessage = handleException(exception);
        System.out.println(errorMessage);
    }

    /**
     * Lấy error message theo mã lỗi (để mapping error code → message).
     *
     * @param errorCode Mã lỗi (VD: "ERR_001")
     * @return Mô tả error code
     */
    public String getErrorMessageByCode(String errorCode) {
        switch (errorCode) {
            case "ERR_001":
                return "Không tìm thấy dữ liệu";
            case "ERR_002":
                return "Dữ liệu đã tồn tại hoặc không hợp lệ";
            case "ERR_003":
                return "Thao tác không được phép";
            case "ERR_004":
                return "Lỗi trong quá trình thanh toán";
            case "ERR_005":
                return "Lỗi liên quan lịch hẹn";
            case "ERR_006":
                return "Dữ liệu nhập vào không hợp lệ";
            case "ERR_999":
                return "Lỗi hệ thống không xác định";
            default:
                return "Lỗi: " + errorCode;
        }
    }

    /**
     * Kiểm tra xem exception có phải critical (cần ghi vào error log) không.
     *
     * @param exception Exception cần kiểm tra
     * @return true nếu là critical exception
     */
    public boolean isCriticalException(Exception exception) {
        if (exception instanceof PaymentException) {
            return true; // Payment errors là critical
        }
        if (exception instanceof BaseException) {
            String errorCode = ((BaseException) exception).getErrorCode();
            return errorCode.equals("ERR_004") || errorCode.equals("ERR_005");
        }
        return false;
    }

    // ============ PHƯƠNG THỨC PRIVATE (PRIVATE METHODS) ============

    /**
     * Format system error message (cho các exception không phải BaseException).
     *
     * @param errorTitle   Tiêu đề lỗi
     * @param errorDetails Chi tiết lỗi
     * @return Error message được định dạng
     */
    private String formatSystemError(String errorTitle, String errorDetails) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════\n");
        sb.append("❌ ").append(errorTitle).append("\n");
        sb.append("═══════════════════════════════════════\n");
        sb.append("Chi Tiết: ").append(errorDetails).append("\n");
        sb.append("═══════════════════════════════════════");
        return sb.toString();
    }
}
