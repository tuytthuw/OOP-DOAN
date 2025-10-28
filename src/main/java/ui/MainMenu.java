package ui;

public class MainMenu {
    private final InputHandler inputHandler;
    private final OutputFormatter outputFormatter;

    public MainMenu(InputHandler inputHandler, OutputFormatter outputFormatter) {
        this.inputHandler = inputHandler;
        this.outputFormatter = outputFormatter;
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("===== SPA MANAGEMENT =====");
            System.out.println("1. Quản lý khách hàng");
            System.out.println("2. Quản lý lịch hẹn");
            System.out.println("3. Quản lý kho" );
            System.out.println("0. Thoát");
            int choice = inputHandler.readInt("Chọn chức năng", 0, 3);
            switch (choice) {
                case 1:
                    outputFormatter.printStatus("Đi tới menu khách hàng", true);
                    break;
                case 2:
                    outputFormatter.printStatus("Đi tới menu lịch hẹn", true);
                    break;
                case 3:
                    outputFormatter.printStatus("Đi tới menu kho", true);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    outputFormatter.printStatus("Lựa chọn không hợp lệ", false);
            }
        }
    }
}
