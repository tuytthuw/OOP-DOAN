package collections;

import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.InvalidEntityException;
import interfaces.IEntity;

/**
 * Lớp trừu tượng generic cơ sở cho tất cả các Manager classes.
 * Cung cấp các phương thức CRUD chung (Create, Read, Update, Delete) để quản lý
 * mảy động.
 * 
 * @param <T> Kiểu dữ liệu của các phần tử được quản lý (phải implement IEntity)
 */
public abstract class BaseManager<T extends IEntity> {

    // ========== THUỘC TÍNH (ATTRIBUTES) ==========

    /** Mảy động lưu trữ các phần tử */
    protected T[] items;

    /** Số lượng phần tử hiện tại */
    protected int size;

    /** Dung lượng hiện tại của mảy */
    protected int capacity;

    /** Dung lượng ban đầu mặc định */
    protected static final int DEFAULT_CAPACITY = 10;

    // ========== CONSTRUCTOR ==========

    /**
     * Khởi tạo Manager với dung lượng mặc định.
     */
    @SuppressWarnings("unchecked")
    public BaseManager() {
        this.capacity = DEFAULT_CAPACITY;
        // Tạo mảy IEntity vì tất cả phần tử đều implement IEntity
        this.items = (T[]) new IEntity[capacity];
        this.size = 0;
    }

    // ========== PHƯƠNG THỨC CRUD (CREATE, READ, UPDATE, DELETE) ==========

    /**
     * Thêm một phần tử mới vào danh sách.
     * 
     * @param item Phần tử cần thêm
     * @return true nếu thêm thành công
     * @throws InvalidEntityException       nếu item là null
     * @throws EntityAlreadyExistsException nếu ID đã tồn tại
     */
    public boolean add(T item) throws InvalidEntityException, EntityAlreadyExistsException {
        if (item == null) {
            throw new InvalidEntityException(
                    item != null ? item.getClass().getSimpleName() : "Entity",
                    "Đối tượng không được null");
        }

        // Nếu ID đã tồn tại
        if (exists(item.getId())) {
            throw new EntityAlreadyExistsException(
                    item.getClass().getSimpleName(),
                    "ID",
                    item.getId());
        }

        // Nếu mảy đầy, resize
        if (size >= capacity) {
            resize();
        }

        items[size] = item;
        size++;
        return true;
    }

    /**
     * Cập nhật một phần tử trong danh sách.
     * 
     * @param item Phần tử cần cập nhật
     * @return true nếu cập nhật thành công
     * @throws InvalidEntityException  nếu item là null
     * @throws EntityNotFoundException nếu không tìm thấy phần tử cần cập nhật
     */
    public boolean update(T item) throws InvalidEntityException, EntityNotFoundException {
        if (item == null) {
            throw new InvalidEntityException(
                    "Entity",
                    "Đối tượng không được null");
        }

        int index = findIndex(item.getId());
        if (index == -1) {
            throw new EntityNotFoundException(
                    item.getClass().getSimpleName(),
                    item.getId());
        }

        items[index] = item;
        return true;
    }

    /**
     * Xóa một phần tử từ danh sách theo ID.
     * 
     * @param id ID của phần tử cần xóa
     * @return true nếu xóa thành công
     * @throws InvalidEntityException  nếu ID là null hoặc rỗng
     * @throws EntityNotFoundException nếu không tìm thấy phần tử cần xóa
     */
    public boolean delete(String id) throws InvalidEntityException, EntityNotFoundException {
        if (id == null || id.isEmpty()) {
            throw new InvalidEntityException(
                    "Entity",
                    "ID không được null hoặc rỗng");
        }

        int index = findIndex(id);
        if (index == -1) {
            throw new EntityNotFoundException(
                    "Entity",
                    id);
        }

        // Dịch chuyển các phần tử sau lên
        for (int i = index; i < size - 1; i++) {
            items[i] = items[i + 1];
        }

        items[size - 1] = null;
        size--;
        return true;
    }

    /**
     * Lấy phần tử theo ID.
     * 
     * @param id ID của phần tử cần tìm
     * @return Phần tử nếu tìm thấy
     * @throws InvalidEntityException  nếu ID là null hoặc rỗng
     * @throws EntityNotFoundException nếu không tìm thấy phần tử
     */
    public T getById(String id) throws InvalidEntityException, EntityNotFoundException {
        if (id == null || id.isEmpty()) {
            throw new InvalidEntityException(
                    "Entity",
                    "ID không được null hoặc rỗng");
        }

        int index = findIndex(id);
        if (index == -1) {
            throw new EntityNotFoundException(
                    "Entity",
                    id);
        }

        return items[index];
    }

    /**
     * Kiểm tra phần tử có tồn tại hay không theo ID.
     * 
     * @param id ID của phần tử
     * @return true nếu tồn tại, false nếu không
     */
    public boolean exists(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        return findIndex(id) != -1;
    }

    /**
     * Lấy số lượng phần tử hiện tại.
     * 
     * @return Số lượng phần tử
     */
    public int size() {
        return size;
    }

    /**
     * Xóa tất cả phần tử khỏi danh sách.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            items[i] = null;
        }
        size = 0;
    }

    // ========== PHƯƠNG THỨC BẢO VỆ (PROTECTED HELPER METHODS) ==========

    /**
     * Tìm chỉ số (index) của phần tử theo ID.
     * 
     * @param id ID của phần tử
     * @return Chỉ số nếu tìm thấy, -1 nếu không
     */
    protected int findIndex(String id) {
        for (int i = 0; i < size; i++) {
            if (items[i].getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Mở rộng dung lượng mảy gấp đôi khi đầy.
     * Logic: Tạo mảy mới với capacity * 2, copy tất cả phần tử, rồi gán mảy mới cho
     * items.
     */
    @SuppressWarnings("unchecked")
    protected void resize() {
        int newCapacity = capacity * 2;
        // Tạo mảy IEntity vì tất cả phần tử đều implement IEntity
        T[] newItems = (T[]) new IEntity[newCapacity];

        // Copy tất cả phần tử từ mảy cũ sang mảy mới
        for (int i = 0; i < size; i++) {
            newItems[i] = items[i];
        }

        items = newItems;
        capacity = newCapacity;
    }

    /**
     * Lấy tất cả phần tử trong danh sách.
     * Phương thức trừu tượng - các lớp con phải cài đặt.
     * 
     * @return Mảy chứa tất cả phần tử hiện tại
     */
    public abstract T[] getAll();

    /**
     * Lấy dung lượng hiện tại của mảy.
     * 
     * @return Dung lượng hiện tại
     */
    public int getCapacity() {
        return capacity;
    }
}
