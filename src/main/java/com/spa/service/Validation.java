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
    private static final String CANCEL_KEYWORD = "Q";
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

    public static String getStringOrCancel(String prompt) {
        while (true) {
            System.out.print(prompt + " (nhập '" + CANCEL_KEYWORD + "' để hủy): ");
            String value = SCANNER.nextLine().trim();
            if (isCancelKeyword(value)) {
                return null;
            }
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Giá trị không được để trống. Vui lòng nhập lại.");
        }
    }

    public static String getOptionalString(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static String getOptionalStringOrCancel(String prompt) {
        System.out.print(prompt);
        String value = SCANNER.nextLine().trim();
        if (isCancelKeyword(value)) {
            return null;
        }
        return value;
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

    public static Integer getIntOrCancel(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " (nhập '" + CANCEL_KEYWORD + "' để hủy): ");
            String raw = SCANNER.nextLine().trim();
            if (isCancelKeyword(raw)) {
                return null;
            }
            try {
                int value = Integer.parseInt(raw);
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

    public static Double getDoubleOrCancel(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt + " (nhập '" + CANCEL_KEYWORD + "' để hủy): ");
            String raw = SCANNER.nextLine().trim();
            if (isCancelKeyword(raw)) {
                return null;
            }
            try {
                double value = Double.parseDouble(raw);
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
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ex) {
                System.out.printf("Ngày không hợp lệ. Định dạng đúng: %s%n", formatter);
            }
        }
    }

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

    public static LocalDate getDateOrCancel(String prompt, DateTimeFormatter formatter) {
        while (true) {
            System.out.print(prompt + " (nhập '" + CANCEL_KEYWORD + "' để hủy): ");
            String value = SCANNER.nextLine().trim();
            if (isCancelKeyword(value)) {
                return null;
            }
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ex) {
                System.out.printf("Ngày không hợp lệ. Định dạng đúng: %s%n", formatter);
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
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException ex) {
                System.out.printf("Ngày giờ không hợp lệ. Định dạng đúng: %s%n", formatter);
            }
        }
    }

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

    public static LocalDateTime getDateTimeOrCancel(String prompt, DateTimeFormatter formatter) {
        while (true) {
            System.out.print(prompt + " (nhập '" + CANCEL_KEYWORD + "' để hủy): ");
            String value = SCANNER.nextLine().trim();
            if (isCancelKeyword(value)) {
                return null;
            }
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException ex) {
                System.out.printf("Ngày giờ không hợp lệ. Định dạng đúng: %s%n", formatter);
            }
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

    public static Boolean getBooleanOrCancel(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N, '" + CANCEL_KEYWORD + "' để hủy): ");
            String value = SCANNER.nextLine().trim().toUpperCase();
            if (isCancelKeyword(value)) {
                return null;
            }
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
     * Tạm dừng cho đến khi người dùng nhấn Enter.
     */
    public static void pause() {
        System.out.println("Nhấn Enter để tiếp tục...");
        SCANNER.nextLine();
    }

    public static String cancelKeyword() {
        return CANCEL_KEYWORD;
    }

    private static boolean isCancelKeyword(String value) {
        return value != null && value.equalsIgnoreCase(CANCEL_KEYWORD);
    }
}
