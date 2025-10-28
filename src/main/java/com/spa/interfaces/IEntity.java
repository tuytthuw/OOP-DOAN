package com.spa.interfaces;

/**
 * Định nghĩa hợp đồng cơ bản cho mọi thực thể trong hệ thống.
 */
public interface IEntity {

    /**
     * Lấy mã định danh duy nhất.
     *
     * @return mã đối tượng
     */
    String getId();

    /**
     * Hiển thị thông tin đối tượng.
     */
    void display();

    /**
     * Nhập thông tin đối tượng từ nguồn dữ liệu bên ngoài.
     */
    void input();

    /**
     * Lấy tiền tố chuẩn hóa dùng khi sinh mã tự động.
     *
     * @return tiền tố mã
     */
    String getPrefix();
}
