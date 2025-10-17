package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FileHandler xử lý lưu/tải dữ liệu từ file.
 * Hỗ trợ CSV format để đơn giản (không dùng JSON để tránh phức tạp).
 * Cung cấp các phương thức để export dữ liệu ra file.
 */
public class FileHandler {

    // ============ HẰNG SỐ ============
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String CSV_SEPARATOR = ",";
    public static final String CSV_QUOTE = "\"";

    /**
     * Tạo đường dẫn file nếu chưa tồn tại.
     *
     * @param filename Tên file
     * @return true nếu file được tạo hoặc đã tồn tại, false nếu tạo thất bại
     */
    public static boolean ensureFileExists(String filename) {
        try {
            File file = new File(filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            return true;
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi tạo file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Viết dữ liệu vào file text.
     *
     * @param filename Tên file
     * @param content  Nội dung cần viết
     * @param append   true để append, false để ghi đè
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean writeToFile(String filename, String content, boolean append) {
        try {
            ensureFileExists(filename);

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(filename, append));
            writer.write(content);
            writer.newLine();
            writer.close();

            return true;
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi viết file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Đọc dữ liệu từ file text.
     *
     * @param filename Tên file
     * @return Nội dung file hoặc "" nếu lỗi
     */
    public static String readFromFile(String filename) {
        StringBuilder content = new StringBuilder();

        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("⚠️  File không tồn tại: " + filename);
                return "";
            }

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            return content.toString().trim();
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi đọc file: " + e.getMessage());
            return "";
        }
    }

    /**
     * Xóa file.
     *
     * @param filename Tên file
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean deleteFile(String filename) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                return file.delete();
            }
            return true;
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi xóa file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Kiểm tra file có tồn tại không.
     *
     * @param filename Tên file
     * @return true nếu file tồn tại, false nếu không
     */
    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    /**
     * Backup file bằng cách rename thành filename.bak.
     *
     * @param filename Tên file
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean backupFile(String filename) {
        try {
            File originalFile = new File(filename);
            if (!originalFile.exists()) {
                return false;
            }

            File backupFile = new File(filename + ".bak");
            return originalFile.renameTo(backupFile);
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi backup file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa nội dung file (xóa sạch).
     *
     * @param filename Tên file
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean clearFile(String filename) {
        try {
            ensureFileExists(filename);

            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write("");
            writer.close();

            return true;
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi xóa nội dung file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Escape CSV field (thêm quote nếu chứa dấu phẩy hoặc quote).
     *
     * @param field Trường dữ liệu
     * @return Trường đã escape
     */
    public static String escapeCSVField(String field) {
        if (field == null) {
            return "";
        }

        // Nếu chứa dấu phẩy, newline, hoặc quote -> đặt trong quote
        if (field.contains(CSV_SEPARATOR) || field.contains("\n") || field.contains(CSV_QUOTE)) {
            // Escape quote bằng cách nhân đôi
            String escaped = field.replace(CSV_QUOTE, CSV_QUOTE + CSV_QUOTE);
            return CSV_QUOTE + escaped + CSV_QUOTE;
        }

        return field;
    }

    /**
     * Tạo CSV header từ mảy chuỗi column names.
     *
     * @param columnNames Mảy tên cột
     * @return Header CSV
     */
    public static String createCSVHeader(String[] columnNames) {
        if (columnNames == null || columnNames.length == 0) {
            return "";
        }

        StringBuilder header = new StringBuilder();
        for (int i = 0; i < columnNames.length; i++) {
            header.append(escapeCSVField(columnNames[i]));
            if (i < columnNames.length - 1) {
                header.append(CSV_SEPARATOR);
            }
        }

        return header.toString();
    }

    /**
     * Tạo CSV row từ mảy chuỗi dữ liệu.
     *
     * @param values Mảy giá trị
     * @return Row CSV
     */
    public static String createCSVRow(String[] values) {
        if (values == null || values.length == 0) {
            return "";
        }

        StringBuilder row = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            row.append(escapeCSVField(values[i]));
            if (i < values.length - 1) {
                row.append(CSV_SEPARATOR);
            }
        }

        return row.toString();
    }

    /**
     * Export data thành file CSV.
     * Dòng đầu là header, các dòng tiếp theo là dữ liệu.
     *
     * @param filename Tên file
     * @param headers  Mảy tên cột
     * @param dataRows Mảy mảy dữ liệu (mỗi mảy con là một hàng)
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean exportToCSV(String filename, String[] headers, String[][] dataRows) {
        try {
            ensureFileExists(filename);

            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            // Viết header
            if (headers != null && headers.length > 0) {
                writer.write(createCSVHeader(headers));
                writer.newLine();
            }

            // Viết dữ liệu
            if (dataRows != null) {
                for (String[] row : dataRows) {
                    writer.write(createCSVRow(row));
                    writer.newLine();
                }
            }

            writer.close();
            System.out.println("✅ Export CSV thành công: " + filename);
            return true;
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi export CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tạo tên file backup với timestamp.
     *
     * @param baseFilename Tên file gốc
     * @return Tên file backup
     */
    public static String generateBackupFilename(String baseFilename) {
        long timestamp = System.currentTimeMillis();
        int lastDot = baseFilename.lastIndexOf('.');

        if (lastDot == -1) {
            return baseFilename + "_" + timestamp + ".bak";
        } else {
            String name = baseFilename.substring(0, lastDot);
            String ext = baseFilename.substring(lastDot);
            return name + "_" + timestamp + ext + ".bak";
        }
    }
}
