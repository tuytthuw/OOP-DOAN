package io;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * InputHandler xử lý đọc dữ liệu từ người dùng qua console.
 * Cung cấp các phương thức để đọc các loại dữ liệu khác nhau với validation.
 * Cho phép retry tối đa 3 lần nếu input không hợp lệ.
 */
public class InputHandler {

    // ============ HẰNG SỐ ============
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DEFAULT_DATETIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final String PHONE_REGEX = "^(0|\\+84)[0-9]{9,10}$";
    private static final int MAX_RETRIES = 3;

    private Scanner scanner;

    /**
     * Constructor khởi tạo InputHandler với Scanner mặc định.
     */
    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Constructor khởi tạo InputHandler với Scanner tùy chỉnh (cho testing).
     *
     * @param scanner Scanner tùy chỉnh
     */
    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Đọc chuỗi từ người dùng.
     *
     * @param prompt Thông báo yêu cầu nhập
     * @return Chuỗi được nhập vào
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Đọc số nguyên từ người dùng với retry.
     *
     * @param prompt Thông báo yêu cầu nhập
     * @return Số nguyên được nhập vào
     */
    public int readInt(String prompt) {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập một số nguyên hợp lệ!");
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("❌ Đã vượt quá số lần nhập. Trả về 0.");
                    return 0;
                }
            }
        }
        return 0;
    }

    /**
     * Đọc số thực từ người dùng với retry.
     *
     * @param prompt Thông báo yêu cầu nhập
     * @return Số thực được nhập vào
     */
    public double readDouble(String prompt) {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập một số thực hợp lệ!");
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("❌ Đã vượt quá số lần nhập. Trả về 0.0.");
                    return 0.0;
                }
            }
        }
        return 0.0;
    }

    /**
     * Đọc số tiền (BigDecimal) từ người dùng với retry.
     *
     * @param prompt Thông báo yêu cầu nhập
     * @return Số tiền được nhập vào
     */
    public BigDecimal readBigDecimal(String prompt) {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập một số tiền hợp lệ!");
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("❌ Đã vượt quá số lần nhập. Trả về 0.");
                    return BigDecimal.ZERO;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Đọc ngày từ người dùng theo định dạng dd/MM/yyyy với retry.
     *
     * @param prompt Thông báo yêu cầu nhập
     * @return Ngày được nhập vào
     */
    public LocalDate readLocalDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                System.out.print(prompt + " (" + DEFAULT_DATE_FORMAT + "): ");
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Định dạng ngày không hợp lệ! Vui lòng nhập theo " + DEFAULT_DATE_FORMAT);
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("❌ Đã vượt quá số lần nhập. Trả về ngày hôm nay.");
                    return LocalDate.now();
                }
            }
        }
        return LocalDate.now();
    }

    /**
     * Đọc ngày giờ từ người dùng theo định dạng dd/MM/yyyy HH:mm với retry.
     *
     * @param prompt Thông báo yêu cầu nhập
     * @return Ngày giờ được nhập vào
     */
    public LocalDateTime readLocalDateTime(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                System.out.print(prompt + " (" + DEFAULT_DATETIME_FORMAT + "): ");
                String input = scanner.nextLine().trim();
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Định dạng ngày giờ không hợp lệ! Vui lòng nhập theo " + DEFAULT_DATETIME_FORMAT);
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("❌ Đã vượt quá số lần nhập. Trả về bây giờ.");
                    return LocalDateTime.now();
                }
            }
        }
        return LocalDateTime.now();
    }

    /**
     * Đọc giá trị boolean (yes/no, y/n, true/false) từ người dùng.
     *
     * @param prompt Thông báo yêu cầu nhập
     * @return true nếu nhập yes/y/true, false nếu no/n/false
     */
    public boolean readBoolean(String prompt) {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes") || input.equals("true")) {
                return true;
            } else if (input.equals("n") || input.equals("no") || input.equals("false")) {
                return false;
            } else {
                System.out.println("❌ Vui lòng nhập y/yes/true hoặc n/no/false!");
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("❌ Đã vượt quá số lần nhập. Mặc định false.");
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Đọc lựa chọn từ danh sách tùy chọn.
     * Hiển thị danh sách các tùy chọn và yêu cầu người dùng chọn.
     *
     * @param prompt  Thông báo yêu cầu nhập
     * @param options Mảy các tùy chọn
     * @return Tùy chọn được chọn hoặc "" nếu không hợp lệ
     */
    public String readOption(String prompt, String[] options) {
        if (options == null || options.length == 0) {
            System.out.println("❌ Danh sách tùy chọn trống!");
            return "";
        }

        // Hiển thị danh sách tùy chọn
        System.out.println(prompt);
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ". " + options[i]);
        }

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                System.out.print("Chọn (1-" + options.length + "): ");
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= options.length) {
                    return options[choice - 1];
                } else {
                    System.out.println("❌ Vui lòng chọn số từ 1 đến " + options.length + "!");
                    if (attempt == MAX_RETRIES - 1) {
                        System.out.println("❌ Đã vượt quá số lần nhập. Trả về lựa chọn đầu tiên.");
                        return options[0];
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập một số nguyên hợp lệ!");
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("❌ Đã vượt quá số lần nhập. Trả về lựa chọn đầu tiên.");
                    return options[0];
                }
            }
        }
        return options[0];
    }

    /**
     * Đọc email từ người dùng với validation.
     *
     * @param prompt Thông báo yêu cầu nhập
     * @return Email hợp lệ hoặc "" nếu không hợp lệ
     */
    public String readEmail(String prompt) {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            System.out.print(prompt);
            String email = scanner.nextLine().trim();

            // Cho phép email trống
            if (email.isEmpty()) {
                return "";
            }

            // Validate email format
            if (email.matches(EMAIL_REGEX)) {
                return email;
            } else {
                System.out.println("❌ Email không hợp lệ! Vui lòng nhập theo định dạng: user@domain.com");
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("❌ Đã vượt quá số lần nhập. Trả về email rỗng.");
                    return "";
                }
            }
        }
        return "";
    }

    /**
     * Đọc số điện thoại từ người dùng với validation.
     * Format: 0XXXXXXXXX hoặc +84XXXXXXXXX (9-10 chữ số sau mã quốc gia).
     *
     * @param prompt Thông báo yêu cầu nhập
     * @return Số điện thoại hợp lệ
     */
    public String readPhoneNumber(String prompt) {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            System.out.print(prompt);
            String phone = scanner.nextLine().trim();

            // Validate phone format
            if (phone.matches(PHONE_REGEX)) {
                return phone;
            } else {
                System.out.println(
                        "❌ Số điện thoại không hợp lệ! Vui lòng nhập theo định dạng: 0912345678 hoặc +84912345678");
                if (attempt == MAX_RETRIES - 1) {
                    System.out.println("❌ Đã vượt quá số lần nhập. Trả về số rỗng.");
                    return "";
                }
            }
        }
        return "";
    }

    /**
     * Đóng Scanner (gọi khi kết thúc chương trình).
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
