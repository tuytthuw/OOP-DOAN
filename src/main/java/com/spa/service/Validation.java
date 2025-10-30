package com.spa.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Tiện ích kiểm tra và lấy dữ liệu nhập từ console.
 */
public final class Validation {
    private static final Scanner SCANNER = new Scanner(System.in);

    private Validation() {
    }

    /**
     * Đọc chuỗi không rỗng từ người dùng.
     *
     * @param prompt lời nhắc
     * @return chuỗi nhập hợp lệ
     */
    public static String getString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Giá trị không được để trống. Vui lòng nhập lại.");
        }
    }

    /**
     * Đọc chuỗi tùy chọn (có thể để trống).
     *
     * @param prompt lời nhắc
     * @return chuỗi nhập (có thể rỗng)
     */
    public static String getOptionalString(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    /**
     * Đọc số nguyên trong khoảng cho phép.
     *
     * @param prompt lời nhắc
     * @param min    giá trị nhỏ nhất
     * @param max    giá trị lớn nhất
     * @return số nguyên hợp lệ
     */
    public static int getInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(SCANNER.nextLine().trim());
                if (value < min || value > max) {
                    System.out.printf("Giá trị phải nằm trong khoảng %d - %d.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Không phải số nguyên hợp lệ. Vui lòng nhập lại.");
            }
        }
    }

    /**
     * Đọc số thực trong khoảng cho phép.
     *
     * @param prompt lời nhắc
     * @param min    giá trị nhỏ nhất
     * @param max    giá trị lớn nhất
     * @return số thực hợp lệ
     */
    public static double getDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(SCANNER.nextLine().trim());
                if (value < min || value > max) {
                    System.out.printf("Giá trị phải nằm trong khoảng %.2f - %.2f.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Không phải số thực hợp lệ. Vui lòng nhập lại.");
            }
        }
    }

    /**
     * Đọc giá trị ngày theo định dạng cho trước.
     *
     * @param prompt    lời nhắc
     * @param formatter định dạng ngày
     * @return ngày hợp lệ
     */
    public static LocalDate getDate(String prompt, DateTimeFormatter formatter) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Ngày không được để trống.");
                continue;
            }
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ex) {
                System.out.printf("Ngày không hợp lệ. Định dạng đúng: dd/MM/yyyy%n");
            }
        }
    }

    /**
     * Đọc giá trị ngày (phải từ hôm nay trở đi).
     *
     * @param prompt    lời nhắc
     * @param formatter định dạng ngày
     * @return ngày hợp lệ
     */
    public static LocalDate getFutureOrTodayDate(String prompt, DateTimeFormatter formatter) {
        while (true) {
            LocalDate date = getDate(prompt, formatter);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("Ngày không được nhỏ hơn hôm nay. Vui lòng nhập lại.");
                continue;
            }
            return date;
        }
    }

    /**
     * Đọc giá trị ngày tùy chọn (có thể để trống).
     *
     * @param prompt    lời nhắc
     * @param formatter định dạng ngày
     * @return ngày hợp lệ hoặc null nếu để trống
     */
    public static LocalDate getOptionalDate(String prompt, DateTimeFormatter formatter) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (value.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ex) {
                System.out.printf("Ngày không hợp lệ. Định dạng đúng: dd/MM/yyyy%n");
            }
        }
    }

    /**
     * Đọc giá trị ngày giờ theo định dạng cho trước.
     *
     * @param prompt    lời nhắc
     * @param formatter định dạng ngày giờ
     * @return thời điểm hợp lệ
     */
    public static LocalDateTime getDateTime(String prompt, DateTimeFormatter formatter) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Ngày giờ không được để trống.");
                continue;
            }
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException ex) {
                System.out.printf("Ngày giờ không hợp lệ. Định dạng đúng: dd/MM/yyyy HH:mm%n");
            }
        }
    }

    /**
     * Đọc giá trị ngày giờ (phải trong tương lai).
     *
     * @param prompt    lời nhắc
     * @param formatter định dạng ngày giờ
     * @return thời điểm hợp lệ
     */
    public static LocalDateTime getFutureDateTime(String prompt, DateTimeFormatter formatter) {
        while (true) {
            LocalDateTime dateTime = getDateTime(prompt, formatter);
            if (dateTime.isBefore(LocalDateTime.now())) {
                System.out.println("Thời gian không được ở quá khứ. Vui lòng nhập lại.");
                continue;
            }
            return dateTime;
        }
    }

    /**
     * Đọc giá trị boolean (Y/N).
     *
     * @param prompt lời nhắc
     * @return true nếu người dùng chọn Y
     */
    public static boolean getBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String value = SCANNER.nextLine().trim().toUpperCase();
            if ("Y".equals(value)) {
                return true;
            }
            if ("N".equals(value)) {
                return false;
            }
            System.out.println("Vui lòng nhập Y hoặc N.");
        }
    }

    /**
     * Đọc giá trị boolean tùy chọn (Y/N hoặc để trống).
     *
     * @param prompt lời nhắc
     * @return Boolean true nếu Y, false nếu N, null nếu để trống
     */
    public static Boolean getOptionalBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N hoặc để trống): ");
            String value = SCANNER.nextLine().trim().toUpperCase();
            if (value.isEmpty()) {
                return null;
            }
            if ("Y".equals(value)) {
                return true;
            }
            if ("N".equals(value)) {
                return false;
            }
            System.out.println("Vui lòng nhập Y, N hoặc để trống.");
        }
    }

    /**
     * Tạm dừng cho đến khi người dùng nhấn Enter.
     */
    public static void pause() {
        System.out.println("Nhấn Enter để tiếp tục...");
        SCANNER.nextLine();
    }
}
