package exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp exception cơ sở trừu tượng cho tất cả custom exceptions trong hệ thống.
 * Cung cấp các thông tin chi tiết về lỗi: mã lỗi, thông báo, thời gian, và
 * stack trace.
 * 
 * Tất cả custom exceptions phải extend lớp này để có cấu trúc thống nhất.
 */
public abstract class BaseException extends Exception {

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    /** Mã lỗi riêng, định dạng: ERR_XXX */
    private String errorCode;

    /** Thông báo lỗi chi tiết */
    private String errorMessage;

    /** Thời gian xảy ra lỗi */
    private LocalDateTime timestamp;

    /** Stack trace cho debug */
    private String stackTraceInfo;

    // ============ HẰNG SỐ (CONSTANTS) ============

    /** Định dạng ngày giờ chuẩn */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo BaseException với mã lỗi và thông báo.
     *
     * @param errorCode    Mã lỗi (VD: "ERR_001")
     * @param errorMessage Thông báo lỗi chi tiết
     */
    public BaseException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
        this.stackTraceInfo = captureStackTrace();
    }

    /**
     * Constructor khởi tạo BaseException với mã lỗi, thông báo, và nguyên nhân.
     *
     * @param errorCode    Mã lỗi
     * @param errorMessage Thông báo lỗi chi tiết
     * @param cause        Ngoại lệ gốc (cause) gây ra lỗi này
     */
    public BaseException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
        this.stackTraceInfo = captureStackTrace();
    }

    // ============ GETTER METHODS ============

    /**
     * Lấy mã lỗi.
     *
     * @return Mã lỗi (VD: "ERR_001")
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Lấy thông báo lỗi chi tiết.
     *
     * @return Thông báo lỗi
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Lấy thời gian xảy ra lỗi.
     *
     * @return Thời gian dưới dạng LocalDateTime
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Lấy stack trace thông tin.
     *
     * @return Stack trace dưới dạng chuỗi
     */
    public String getStackTraceInfo() {
        return stackTraceInfo;
    }

    // ============ PHƯƠNG THỨC CÔNG CỘNG (PUBLIC METHODS) ============

    /**
     * Trả về thông báo lỗi được định dạng đẹp để hiển thị cho người dùng.
     * 
     * Định dạng:
     * ═══════════════════════════════════════
     * ❌ LỖI CHƯƠNG TRÌNH
     * ═══════════════════════════════════════
     * Mã Lỗi: ERR_001
     * Thời Gian: 2025-10-17 14:30:45
     * Thông Báo: [thông báo lỗi chi tiết]
     * ═══════════════════════════════════════
     *
     * @return Chuỗi thông báo lỗi được định dạng
     */
    public String getFormattedError() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════\n");
        sb.append("❌ LỖI CHƯƠNG TRÌNH\n");
        sb.append("═══════════════════════════════════════\n");
        sb.append("Mã Lỗi: ").append(errorCode).append("\n");
        sb.append("Thời Gian: ").append(timestamp.format(DATE_FORMATTER)).append("\n");
        sb.append("Thông Báo: ").append(errorMessage).append("\n");
        sb.append("═══════════════════════════════════════");
        return sb.toString();
    }

    /**
     * Ghi log lỗi ra console.
     * Được sử dụng cho mục đích debug và theo dõi lỗi.
     */
    public void logError() {
        System.err.println("[" + errorCode + "] " + errorMessage);
        if (stackTraceInfo != null && !stackTraceInfo.isEmpty()) {
            System.err.println("Stack Trace: " + stackTraceInfo);
        }
    }

    /**
     * Ghi log lỗi với prefix tuỳ chọn.
     *
     * @param prefix Prefix để thêm vào log (VD: "[SERVICE]", "[MANAGER]")
     */
    public void logError(String prefix) {
        System.err.println(prefix + " [" + errorCode + "] " + errorMessage);
        if (stackTraceInfo != null && !stackTraceInfo.isEmpty()) {
            System.err.println("Stack Trace: " + stackTraceInfo);
        }
    }

    // ============ PHƯƠNG THỨC PRIVATE (PRIVATE METHODS) ============

    /**
     * Bắt được stack trace thông tin từ exception hiện tại.
     *
     * @return Stack trace dưới dạng chuỗi
     */
    private String captureStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length < 3) {
            return "No stack trace";
        }
        // Lấy 2 dòng đầu tiên (bỏ qua method này)
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < Math.min(stackTrace.length, 5); i++) {
            sb.append(stackTrace[i].toString()).append(" -> ");
        }
        return sb.toString();
    }

    // ============ OVERRIDE METHODS ============

    /**
     * Trả về chuỗi đại diện cho exception.
     *
     * @return Chuỗi mô tả exception
     */
    @Override
    public String toString() {
        return "[" + errorCode + "] " + errorMessage;
    }
}
