package com.spa.interfaces;

/**
 * Định nghĩa các thao tác đọc ghi dữ liệu.
 */
public interface IDataManager {

    /**
     * Đọc dữ liệu từ nguồn lưu trữ.
     */
    void readFile();

    /**
     * Ghi dữ liệu ra nguồn lưu trữ.
     */
    void writeFile();
}
