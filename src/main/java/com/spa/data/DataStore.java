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
        int index = findIndexById(id);
        if (index >= 0) {
            list[index].input();
        }
    }

    @Override
    public boolean delete(String id) {
        int index = findIndexById(id);
        if (index < 0) {
            return false;
        }
        shiftLeftFrom(index);
        return true;
    }

    @Override
    public T findById(String id) {
        int index = findIndexById(id);
        if (index < 0) {
            return null;
        }
        return list[index];
    }

    @Override
    public T[] getAll() {
        T[] result = createArray(count);
        System.arraycopy(list, 0, result, 0, count);
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
        int newSize = list.length * 2;
        T[] newList = createArray(newSize);
        System.arraycopy(list, 0, newList, 0, list.length);
        list = newList;
    }

    private int findIndexById(String id) {
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

    private void shiftLeftFrom(int index) {
        for (int i = index; i < count - 1; i++) {
            list[i] = list[i + 1];
        }
        list[count - 1] = null;
        count--;
    }

    @SuppressWarnings("unchecked")
    private T[] createArray(int size) {
        return (T[]) new IEntity[size];
    }
}
