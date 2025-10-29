package com.spa.data;

import com.spa.interfaces.IActionManager;
import com.spa.interfaces.IDataManager;
import com.spa.interfaces.IEntity;
import com.spa.model.Person;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * Kho dữ liệu dùng mảng thuần để quản lý thực thể.
 *
 * @param <T> kiểu thực thể
 */
public class DataStore<T extends IEntity> implements IActionManager<T>, IDataManager {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROWTH_FACTOR = 2;

    private static final int DEFAULT_ID_PADDING = 3;

    private final Class<T> componentType;
    private T[] list;
    private int count;
    protected String dataFilePath;

    public DataStore(Class<T> componentType) {
        this(componentType, DEFAULT_CAPACITY);
    }

    public DataStore(Class<T> componentType, int initialCapacity) {
        if (componentType == null) {
            throw new IllegalArgumentException("componentType không được null");
        }
        if (initialCapacity <= 0) {
            initialCapacity = DEFAULT_CAPACITY;
        }
        this.componentType = componentType;
        list = createArray(initialCapacity);
        count = 0;
        dataFilePath = "";
    }

    public DataStore(Class<T> componentType, String dataFilePath) {
        this(componentType);
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
        assignIdIfNeeded(item);
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
        T item = list[index];
        if (markAsDeleted(item)) {
            return true;
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

    public String generateNextId() {
        return generateNextId(determinePrefix(componentType));
    }

    public String generateNextId(Class<?> type) {
        if (type == null) {
            return generateNextId();
        }
        return generateNextId(determinePrefix(type));
    }

    public String generateNextId(String prefix) {
        String effectivePrefix = prefix;
        if (effectivePrefix == null || effectivePrefix.trim().isEmpty()) {
            effectivePrefix = determinePrefix(componentType);
        }
        int maxNumber = 0;
        int maxDigits = 0;
        for (int i = 0; i < count; i++) {
            T item = list[i];
            if (item == null) {
                continue;
            }
            String id = item.getId();
            if (id == null || id.isEmpty() || !id.startsWith(effectivePrefix)) {
                continue;
            }
            int numeric = extractNumericSuffix(id, effectivePrefix);
            if (numeric > maxNumber) {
                maxNumber = numeric;
            }
            int digits = id.length() - effectivePrefix.length();
            if (digits > maxDigits) {
                maxDigits = digits;
            }
        }
        int nextNumber = maxNumber + 1;
        int width = Math.max(maxDigits, DEFAULT_ID_PADDING);
        return effectivePrefix + formatWithPadding(nextNumber, width);
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
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(componentType, size);
        return array;
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
        if (item instanceof Person) {
            return ((Person) item).isDeleted();
        }
        try {
            Method method = item.getClass().getMethod("isDeleted");
            Object result = method.invoke(item);
            if (result instanceof Boolean) {
                return (Boolean) result;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            return false;
        }
        return false;
    }

    private boolean markAsDeleted(T item) {
        if (item instanceof Person) {
            ((Person) item).setDeleted(true);
            return true;
        }
        try {
            Method method = item.getClass().getMethod("setDeleted", boolean.class);
            method.invoke(item, true);
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            return false;
        }
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

    private void assignIdIfNeeded(T item) {
        if (item == null) {
            return;
        }
        String currentId = item.getId();
        if (currentId != null && !currentId.trim().isEmpty()) {
            return;
        }
        String newId = generateNextId(item.getClass());
        if (item instanceof Person) {
            ((Person) item).setPersonId(newId);
            return;
        }
        if (invokeSetter(item, "setId", newId)) {
            return;
        }
        String simpleName = item.getClass().getSimpleName();
        if (simpleName != null && !simpleName.isEmpty()) {
            String specificSetter = "set" + simpleName + "Id";
            if (invokeSetter(item, specificSetter, newId)) {
                return;
            }
        }
        Method[] methods = item.getClass().getMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("set")) {
                continue;
            }
            if (!method.getName().toLowerCase().endsWith("id")) {
                continue;
            }
            if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != String.class) {
                continue;
            }
            try {
                method.invoke(item, newId);
                return;
            } catch (IllegalAccessException | InvocationTargetException ex) {
                // bỏ qua và thử phương thức khác
            }
        }
    }

    private boolean invokeSetter(T item, String methodName, String id) {
        if (item == null || methodName == null || methodName.trim().isEmpty()) {
            return false;
        }
        try {
            Method method = item.getClass().getMethod(methodName, String.class);
            method.invoke(item, id);
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            return false;
        }
    }

    private String determinePrefix(Class<?> type) {
        if (type == null) {
            return "ID";
        }
        try {
            Object sampleObj = type.getDeclaredConstructor().newInstance();
            if (sampleObj instanceof IEntity) {
                String prefix = ((IEntity) sampleObj).getPrefix();
                if (prefix != null && !prefix.trim().isEmpty()) {
                    return prefix.trim();
                }
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            // Bỏ qua lỗi, dùng fallback bên dưới.
        }
        String simpleName = type.getSimpleName();
        if (simpleName == null || simpleName.isEmpty()) {
            return "ID";
        }
        if (simpleName.length() <= 3) {
            return simpleName.toUpperCase();
        }
        return simpleName.substring(0, 3).toUpperCase();
    }

    private int extractNumericSuffix(String id, String prefix) {
        if (id == null || prefix == null || !id.startsWith(prefix)) {
            return 0;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = prefix.length(); i < id.length(); i++) {
            char current = id.charAt(i);
            if (Character.isDigit(current)) {
                builder.append(current);
            } else {
                break;
            }
        }
        if (builder.length() == 0) {
            return 0;
        }
        try {
            return Integer.parseInt(builder.toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String formatWithPadding(int number, int width) {
        if (width <= 0) {
            return Integer.toString(number);
        }
        String value = Integer.toString(number);
        if (value.length() >= width) {
            return value;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = value.length(); i < width; i++) {
            builder.append('0');
        }
        builder.append(value);
        return builder.toString();
    }
}
