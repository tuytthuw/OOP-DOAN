package ui;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " (" + min + "-" + max + "): ");
            String line = scanner.nextLine();
            try {
                int value = Integer.parseInt(line.trim());
                if (value < min || value > max) {
                    System.out.println("Giá trị nằm ngoài phạm vi.");
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Vui lòng nhập số nguyên hợp lệ.");
            }
        }
    }

    public BigDecimal readDecimal(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String line = scanner.nextLine();
            try {
                BigDecimal value = new BigDecimal(line.trim());
                if (value.signum() < 0) {
                    System.out.println("Giá trị phải không âm.");
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Vui lòng nhập số thập phân hợp lệ.");
            }
        }
    }

    public boolean confirm(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String line = scanner.nextLine().trim().toLowerCase();
            if ("y".equals(line)) {
                return true;
            }
            if ("n".equals(line)) {
                return false;
            }
            System.out.println("Vui lòng nhập y hoặc n.");
        }
    }
}
