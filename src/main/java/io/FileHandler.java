package io;

import interfaces.IEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FileHandler {
    private static final String DATA_FOLDER = "data";
    private static final String[] KEYS = {
            "customers",
            "employees",
            "services",
            "products",
            "goods_receipts",
            "appointments",
            "invoices",
            "promotions",
            "transactions",
            "auth_sessions"
    };
    private static final String[] FILENAMES = {
            "customers.csv",
            "employees.csv",
            "services.csv",
            "products.csv",
            "goods_receipts.csv",
            "appointments.csv",
            "invoices.csv",
            "promotions.csv",
            "transactions.csv",
            "auth_sessions.csv"
    };

    public static void ensureDataFolder() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static void save(String key, IEntity[] data) {
        ensureDataFolder();
        String filename = resolveFilename(key);
        if (filename == null) {
            return;
        }
        File file = new File(DATA_FOLDER, filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (IEntity entity : data) {
                if (entity == null) {
                    continue;
                }
                writer.write(serialize(entity));
                writer.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Không thể ghi file: " + file.getAbsolutePath());
        }
    }

    public static IEntity[] load(String key, IEntityFactory factory) {
        ensureDataFolder();
        String filename = resolveFilename(key);
        if (filename == null) {
            return new IEntity[0];
        }
        File file = new File(DATA_FOLDER, filename);
        if (!file.exists()) {
            return new IEntity[0];
        }
        IEntity[] buffer = new IEntity[32];
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                IEntity entity = factory.fromLine(line);
                if (entity == null) {
                    continue;
                }
                if (count == buffer.length) {
                    IEntity[] expanded = new IEntity[buffer.length * 2];
                    System.arraycopy(buffer, 0, expanded, 0, buffer.length);
                    buffer = expanded;
                }
                buffer[count++] = entity;
            }
        } catch (IOException ex) {
            System.out.println("Không thể đọc file: " + file.getAbsolutePath());
        }
        IEntity[] result = new IEntity[count];
        System.arraycopy(buffer, 0, result, 0, count);
        return result;
    }

    private static String resolveFilename(String key) {
        if (key == null) {
            return null;
        }
        for (int i = 0; i < KEYS.length; i++) {
            if (key.equals(KEYS[i])) {
                return FILENAMES[i];
            }
        }
        return null;
    }

    private static String serialize(IEntity entity) {
        String value = entity.toString();
        return value == null ? "" : value;
    }

    public interface IEntityFactory {
        IEntity fromLine(String line);
    }

    public static LocalDateTime parseDateTime(String text) {
        return text == null || text.isEmpty() ? null : LocalDateTime.parse(text);
    }

    public static LocalDate parseDate(String text) {
        return text == null || text.isEmpty() ? null : LocalDate.parse(text);
    }

    public static BigDecimal parseDecimal(String text) {
        return text == null || text.isEmpty() ? BigDecimal.ZERO : new BigDecimal(text);
    }
}
