package com.spa.interfaces;

/**
 * Định nghĩa các thao tác quản lý chung cho danh sách đối tượng.
 *
 * @param <T> kiểu phần tử
 */
public interface IActionManager<T> {

    /**
     * Hiển thị danh sách đối tượng hiện có.
     */
    void displayList();

    /**
     * Thêm đối tượng mới vào danh sách.
     *
     * @param item đối tượng cần thêm
     */
    void add(T item);

    /**
     * Cập nhật thông tin đối tượng theo mã.
     *
     * @param id mã đối tượng
     */
    void update(String id);

    /**
     * Xóa đối tượng theo mã.
     *
     * @param id mã đối tượng
     * @return true nếu xóa thành công
     */
    boolean delete(String id);

    /**
     * Tìm đối tượng theo mã.
     *
     * @param id mã đối tượng
     * @return đối tượng tìm thấy hoặc null
     */
    T findById(String id);

    /**
     * Lấy toàn bộ phần tử hiện có.
     *
     * @return mảng phần tử hiện có
     */
    T[] getAll();

    /**
     * Sinh số liệu thống kê cơ bản.
     */
    void generateStatistics();
}
