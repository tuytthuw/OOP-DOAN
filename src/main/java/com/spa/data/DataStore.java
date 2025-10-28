package com.spa.data;

import com.spa.interfaces.IActionManager;
import com.spa.interfaces.IDataManager;
import com.spa.interfaces.IEntity;

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

    public DataStore() {
        this(DEFAULT_CAPACITY);
    }

    public DataStore(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = DEFAULT_CAPACITY;
        }
        list = createArray(initialCapacity);
        count = 0;
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
        // Giai đoạn sau sẽ triển khai đọc file.
    }

    @Override
    public void writeFile() {
        // Giai đoạn sau sẽ triển khai ghi file.
    }

    public int getCount() {
        return count;
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
