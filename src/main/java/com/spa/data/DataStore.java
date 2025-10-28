package com.spa.data;

import com.spa.interfaces.IActionManager;
import com.spa.interfaces.IDataManager;
import com.spa.interfaces.IEntity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Kho dữ liệu dùng mảng thuần để quản lý thực thể.
 *
 * @param <T> kiểu thực thể
 */
public class DataStore<T extends IEntity> implements IActionManager<T>, IDataManager {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROWTH_FACTOR = 2;

    private T[] list;
    private int count;
    protected String dataFilePath;

    public DataStore() {
        this(DEFAULT_CAPACITY);
    }

    public DataStore(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = DEFAULT_CAPACITY;
        }
        list = createArray(initialCapacity);
        count = 0;
        dataFilePath = "";
    }

    public DataStore(String dataFilePath) {
        this();
        this.dataFilePath = dataFilePath;
    }

    @Override
    public void displayList() {
        for (int i = 0; i < count; i++) {
            list[i].display();
        }
    }

    @Override
    public void add(T item) {
        if (item == null) {
            return;
        }
        ensureCapacity();
        list[count] = item;
        count++;
    }

    @Override
    public void update(String id) {
        int index = indexOf(id);
        if (index >= 0) {
            list[index].input();
        }
    }

    @Override
    public boolean delete(String id) {
        int index = indexOf(id);
        if (index < 0) {
            return false;
        }
        shiftLeftFrom(index);
        return true;
    }

    @Override
    public T findById(String id) {
        int index = indexOf(id);
        if (index < 0) {
            return null;
        }
        return list[index];
    }

    @Override
    public T[] getAll() {
        T[] result = createArray(count);
        for (int i = 0; i < count; i++) {
            result[i] = createCopy(list[i]);
        }
        return result;
    }

    @Override
    public void generateStatistics() {
        // Giai đoạn sau sẽ hoàn thiện thống kê.
    }

    @Override
    public void readFile() {
        if (dataFilePath == null || dataFilePath.trim().isEmpty()) {
            return;
        }
        File file = new File(dataFilePath);
        if (!file.exists()) {
            return;
        }
        list = createArray(DEFAULT_CAPACITY);
        count = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line == null) {
                    continue;
                }
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                T item = parseLine(trimmed);
                if (item == null) {
                    continue;
                }
                ensureCapacity();
                list[count] = item;
                count++;
            }
        } catch (IOException ex) {
            System.err.println("Không thể đọc file: " + dataFilePath + " - " + ex.getMessage());
            list = createArray(DEFAULT_CAPACITY);
            count = 0;
        }
    }

    @Override
    public void writeFile() {
        if (dataFilePath == null || dataFilePath.trim().isEmpty()) {
            return;
        }
        File file = new File(dataFilePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            boolean created = parent.mkdirs();
            if (!created && !parent.exists()) {
                System.err.println("Không thể tạo thư mục lưu file: " + parent.getAbsolutePath());
                return;
            }
        }
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (int i = 0; i < count; i++) {
                T item = list[i];
                if (item == null || isDeleted(item)) {
                    continue;
                }
                String line = convertToLine(item);
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                writer.write(line);
                writer.write(System.lineSeparator());
            }
        } catch (IOException ex) {
            System.err.println("Không thể ghi file: " + dataFilePath + " - " + ex.getMessage());
        }
    }

    public int getCount() {
        return count;
    }

    /**
     * Thiết lập đường dẫn file dữ liệu cho kho.
     *
     * @param dataFilePath đường dẫn file
     */
    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    /**
     * Lấy đường dẫn file dữ liệu hiện tại.
     *
     * @return đường dẫn file
     */
    public String getDataFilePath() {
        return dataFilePath;
    }

    private void ensureCapacity() {
        if (count < list.length) {
            return;
        }
        int newSize = list.length * GROWTH_FACTOR;
        T[] newList = createArray(newSize);
        System.arraycopy(list, 0, newList, 0, list.length);
        list = newList;
    }

    /**
     * Tìm vị trí phần tử theo mã định danh.
     *
     * @param id mã cần tìm
     * @return chỉ số phần tử hoặc -1 nếu không có
     */
    protected int indexOf(String id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < count; i++) {
            if (id.equals(list[i].getId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Dồn các phần tử về bên trái từ vị trí cho trước.
     *
     * @param index vị trí bắt đầu dồn
     */
    protected void shiftLeftFrom(int index) {
        for (int i = index; i < count - 1; i++) {
            list[i] = list[i + 1];
        }
        list[count - 1] = null;
        count--;
    }

    @SuppressWarnings("unchecked")
    protected final T[] createArray(int size) {
        return (T[]) new IEntity[size];
    }

    /**
     * Tạo bản ghi dạng chuỗi để ghi ra file.
     *
     * @param item đối tượng nguồn
     * @return chuỗi dữ liệu đã chuẩn hóa
     */
    protected String convertToLine(T item) {
        return item == null ? "" : item.getId();
    }

    /**
     * Chuyển một dòng dữ liệu từ file thành đối tượng.
     *
     * @param line dòng dữ liệu
     * @return đối tượng được tạo hoặc null nếu không hợp lệ
     */
    protected T parseLine(String line) {
        return null;
    }

    /**
     * Kiểm tra phần tử đã bị đánh dấu xóa hay chưa.
     *
     * @param item phần tử cần kiểm tra
     * @return true nếu phần tử cần bỏ qua khi ghi file
     */
    protected boolean isDeleted(T item) {
        return false;
    }

    /**
     * Tạo bản sao đối tượng trước khi trả ra ngoài.
     * Các lớp con có thể override để cung cấp sao chép sâu hơn.
     *
     * @param item đối tượng nguồn
     * @return bản sao hoặc chính đối tượng nếu không cần sao chép
     */
    protected T createCopy(T item) {
        return item;
    }
}
