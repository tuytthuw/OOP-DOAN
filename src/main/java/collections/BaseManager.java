package collections;

import interfaces.IEntity;

/**
 * Quản lý mảng thực thể theo hợp đồng chung cho các lớp *Manager.
 *
 * @param <T> Kiểu thực thể triển khai {@link IEntity}
 */
public abstract class BaseManager<T extends IEntity> {
    protected static final int DEFAULT_CAPACITY = 10;

    protected T[] items;
    protected int count;
    protected int capacity;

    protected BaseManager(int initialCapacity) {
        int safeCapacity = initialCapacity < DEFAULT_CAPACITY ? DEFAULT_CAPACITY : initialCapacity;
        this.capacity = safeCapacity;
        this.items = createArray(this.capacity);
        this.count = 0;
    }

    protected BaseManager() {
        this(DEFAULT_CAPACITY);
    }

    protected abstract T[] createArray(int size);

    protected void ensureCapacity() {
        if (count < capacity) {
            return;
        }
        int newCapacity = capacity * 2;
        T[] expanded = createArray(newCapacity);
        for (int i = 0; i < count; i++) {
            expanded[i] = items[i];
        }
        items = expanded;
        capacity = newCapacity;
    }

    protected void shiftLeftFrom(int index) {
        for (int i = index; i < count - 1; i++) {
            items[i] = items[i + 1];
        }
        if (count > 0) {
            items[count - 1] = null;
        }
    }

    protected void shiftRightFrom(int index) {
        if (index < 0 || index > count) {
            throw new IllegalArgumentException("Chỉ số chèn không hợp lệ.");
        }
        ensureCapacity();
        for (int i = count; i > index; i--) {
            items[i] = items[i - 1];
        }
        items[index] = null;
    }

    /**
     * Thêm thực thể mới vào mảng quản lý.
     *
     * @param item Thực thể cần thêm.
     * @throws IllegalArgumentException Khi dữ liệu không hợp lệ hoặc trùng ID.
     */
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Thực thể không được null.");
        }
        String id = item.getId();
        validateId(id);
        if (findIndexById(id) >= 0) {
            throw new IllegalArgumentException("ID đã tồn tại: " + id);
        }
        ensureCapacity();
        items[count] = item;
        count++;
    }

    /**
     * Cập nhật thực thể có sẵn theo ID.
     *
     * @param item Thực thể mang dữ liệu cập nhật.
     * @throws IllegalArgumentException Khi dữ liệu không hợp lệ hoặc không tìm thấy ID.
     */
    public void update(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Thực thể không được null.");
        }
        String id = item.getId();
        validateId(id);
        int index = findIndexById(id);
        if (index < 0) {
            throw new IllegalArgumentException("Không tìm thấy thực thể với ID: " + id);
        }
        items[index] = item;
    }

    /**
     * Xóa thực thể theo ID.
     *
     * @param id Mã định danh cần xóa.
     * @return true nếu xóa thành công, ngược lại false.
     */
    public boolean removeById(String id) {
        validateId(id);
        int index = findIndexById(id);
        if (index < 0) {
            return false;
        }
        shiftLeftFrom(index);
        count--;
        return true;
    }

    /**
     * Tìm một thực thể theo ID.
     *
     * @param id Mã định danh cần tìm.
     * @return Thực thể phù hợp hoặc null nếu không có.
     */
    public T getById(String id) {
        validateId(id);
        int index = findIndexById(id);
        return index >= 0 ? items[index] : null;
    }

    /**
     * Sao chép tất cả thực thể hiện có.
     *
     * @return Mảng mới chứa các thực thể hiện hữu.
     */
    public T[] getAll() {
        T[] snapshot = createArray(count);
        for (int i = 0; i < count; i++) {
            snapshot[i] = items[i];
        }
        return snapshot;
    }

    /**
     * Tìm vị trí thực thể theo ID.
     *
     * @param id Mã định danh cần tìm.
     * @return Chỉ số phần tử hoặc -1 nếu không tìm thấy.
     */
    public int findIndexById(String id) {
        validateId(id);
        for (int i = 0; i < count; i++) {
            T current = items[i];
            if (current != null && id.equals(current.getId())) {
                return i;
            }
        }
        return -1;
    }

    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID không được để trống.");
        }
    }
}
