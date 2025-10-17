package ui;

import exceptions.ExceptionHandler;
import io.InputHandler;
import io.OutputFormatter;

/**
 * Lớp cơ sở trừu tượng cho tất cả các Menu classes.
 * Cung cấp các phương thức chung như displayMenu(), run(), handleChoice().
 */
public abstract class MenuBase {

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    /** Bộ xử lý input từ người dùng */
    protected InputHandler inputHandler;

    /** Bộ định dạng output cho console */
    protected OutputFormatter outputFormatter;

    /** Bộ xử lý exception toàn cục */
    protected ExceptionHandler exceptionHandler;

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo MenuBase với các handlers.
     *
     * @param inputHandler    Bộ xử lý input
     * @param outputFormatter Bộ định dạng output
     */
    public MenuBase(InputHandler inputHandler, OutputFormatter outputFormatter) {
        this.inputHandler = inputHandler;
        this.outputFormatter = outputFormatter;
        this.exceptionHandler = ExceptionHandler.getInstance();
    }

    // ============ PHƯƠNG THỨC CÔNG CỘNG (PUBLIC METHODS) ============

    /**
     * Chạy menu loop chính.
     * Hiển thị menu, đọc lựa chọn từ người dùng, xử lý cho đến khi thoát.
     */
    public void run() {
        boolean shouldExit = false;

        while (!shouldExit) {
            try {
                // Hiển thị menu
                displayMenu();

                // Đọc lựa chọn từ người dùng
                int choice = inputHandler.readInt("Vui lòng chọn (nhập số): ");

                // Xử lý lựa chọn
                if (!handleChoice(choice)) {
                    shouldExit = true;
                }

            } catch (Exception e) {
                exceptionHandler.displayErrorToUser(e);
                // Cho phép retry
            }
        }
    }

    /**
     * Hiển thị menu ra console.
     * Phương thức này phải được override trong các lớp con.
     */
    protected abstract void displayMenu();

    /**
     * Xử lý lựa chọn từ người dùng.
     * Phương thức này phải được override trong các lớp con.
     *
     * @param choice Lựa chọn từ người dùng
     * @return true nếu tiếp tục menu, false nếu thoát
     */
    protected abstract boolean handleChoice(int choice);

    // ============ PHƯƠNG THỨC BẢO VỆ (PROTECTED HELPER METHODS) ============

    /**
     * Tạm dừng và chờ người dùng nhấn Enter.
     * Giúp người dùng nhìn kết quả trước khi quay lại menu.
     */
    protected void pauseForContinue() {
        System.out.print("\nNhấn Enter để tiếp tục...");
        inputHandler.readString("");
    }

    /**
     * Xóa màn hình console.
     */
    protected void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Không thực hiện gì nếu không thể xóa màn hình
        }
    }
}
