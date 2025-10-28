package models;

import interfaces.IEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Cung cấp thuộc tính chung cho mọi cá nhân trong hệ thống.
 */
public abstract class Person implements IEntity {
    protected static final String ID_PREFIX = "PER";
    protected static int runningNumber = 1;

    protected final String personId;
    protected String fullName;
    protected String phoneNumber;
    protected String email;
    protected LocalDate birthDate;
    protected final LocalDateTime createdAt;
    protected boolean isDeleted;

    protected Person(String fullName,
                     String phoneNumber,
                     String email,
                     LocalDate birthDate) {
        this.personId = generatePersonId();
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthDate = birthDate;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    /**
     * {@inheritDoc}
     *
     * Returns:
     *     Mã định danh duy nhất của cá nhân.
     */
    @Override
    public String getId() {
        return personId;
    }

    /**
     * Tính tuổi hiện tại dựa trên ngày sinh.
     *
     * Returns:
     *     Tuổi tính bằng năm (0 nếu chưa khai báo ngày sinh).
     */
    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return Math.max(0, Period.between(birthDate, LocalDate.now()).getYears());
    }

    /**
     * Đánh dấu đối tượng đã bị xóa mềm.
     *
     * Returns:
     *     Không trả về.
     */
    public void softDelete() {
        isDeleted = true;
    }

    /**
     * Khôi phục trạng thái hoạt động của đối tượng.
     *
     * Returns:
     *     Không trả về.
     */
    public void restore() {
        isDeleted = false;
    }

    protected String generatePersonId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    /**
     * Lấy họ tên đầy đủ.
     *
     * Returns:
     *     Họ tên hiện tại.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Cập nhật họ tên đầy đủ.
     *
     * Args:
     *     fullName: Họ tên mới.
     * Returns:
     *     Không trả về.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Lấy số điện thoại liên hệ.
     *
     * Returns:
     *     Chuỗi số điện thoại.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Cập nhật số điện thoại liên hệ.
     *
     * Args:
     *     phoneNumber: Số điện thoại mới.
     * Returns:
     *     Không trả về.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Lấy email hiện tại.
     *
     * Returns:
     *     Địa chỉ email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Cập nhật email.
     *
     * Args:
     *     email: Địa chỉ email mới.
     * Returns:
     *     Không trả về.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Lấy ngày sinh.
     *
     * Returns:
     *     Ngày sinh đã lưu.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Cập nhật ngày sinh.
     *
     * Args:
     *     birthDate: Ngày sinh mới.
     * Returns:
     *     Không trả về.
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Lấy thời điểm tạo bản ghi.
     *
     * Returns:
     *     Thời điểm tạo trong hệ thống.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Kiểm tra trạng thái xóa mềm.
     *
     * Returns:
     *     true nếu đã bị xóa mềm, ngược lại false.
     */
    public boolean isDeleted() {
        return isDeleted;
    }
}
