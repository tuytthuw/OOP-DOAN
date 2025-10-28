package ui;

public class OutputFormatter {
    public void printStatus(String message, boolean success) {
        String prefix = success ? "[OK] " : "[FAIL] ";
        System.out.println(prefix + message);
    }

    public void printTable(String[] headers, String[][] rows) {
        if (headers == null || rows == null) {
            return;
        }
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i] == null ? 0 : headers[i].length();
        }
        for (String[] row : rows) {
            if (row == null) {
                continue;
            }
            for (int i = 0; i < row.length && i < widths.length; i++) {
                String cell = row[i] == null ? "" : row[i];
                if (cell.length() > widths[i]) {
                    widths[i] = cell.length();
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i] == null ? "" : headers[i];
            builder.append(padRight(header, widths[i])).append("  ");
        }
        System.out.println(builder.toString());
        for (String[] row : rows) {
            if (row == null) {
                continue;
            }
            builder.setLength(0);
            for (int i = 0; i < headers.length; i++) {
                String cell = i < row.length && row[i] != null ? row[i] : "";
                builder.append(padRight(cell, widths[i])).append("  ");
            }
            System.out.println(builder.toString());
        }
    }

    public void printInvoice(String[][] items, String total) {
        printTable(new String[]{"Mục", "Số lượng", "Đơn giá"}, items);
        System.out.println("Tổng: " + total);
    }

    private String padRight(String value, int width) {
        StringBuilder builder = new StringBuilder(value);
        while (builder.length() < width) {
            builder.append(' ');
        }
        return builder.toString();
    }
}
