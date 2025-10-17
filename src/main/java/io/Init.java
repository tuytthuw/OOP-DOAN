package io;

/**
 * Lớp khởi tạo các IO Handlers (InputHandler, OutputFormatter, FileHandler).
 * Cung cấp các phương thức factory để tạo instance của các handlers.
 */
public class Init {

    /**
     * Khởi tạo InputHandler.
     *
     * @return Instance của InputHandler
     */
    public static InputHandler initInputHandler() {
        return new InputHandler();
    }

    /**
     * Khởi tạo OutputFormatter.
     *
     * @return Instance của OutputFormatter
     */
    public static OutputFormatter initOutputFormatter() {
        return new OutputFormatter();
    }

    /**
     * FileHandler không cần khởi tạo vì tất cả phương thức là static.
     * Phương thức này chỉ dùng để kiểm tra FileHandler có sẵn.
     *
     * @return Lớp FileHandler (static utility class)
     */
    public static Class<?> getFileHandlerClass() {
        return FileHandler.class;
    }

    /**
     * Khởi tạo tất cả IO Handlers cùng lúc.
     *
     * @return Mảy chứa tất cả IO handlers:
     *         [0] = InputHandler
     *         [1] = OutputFormatter
     *         [2] = null (FileHandler không cần khởi tạo)
     */
    public static Object[] initAllIOHandlers() {
        return new Object[] {
                initInputHandler(),
                initOutputFormatter(),
                null
        };
    }
}
