package models;

import java.time.LocalDate;

/**
 * Lớp cơ sở (superclass) đại diện cho một người (khách hàng, nhân viên, v.v.).
 * Chứa các thông tin cá nhân chung.
 */
public abstract class Person {

    // Thuộc tính (Attributes)
    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    private String gender; // "Nam", "Nữ", "Khác"
    private LocalDate createdDate;

    /**
     * Constructor khởi tạo Person với các thông tin cơ bản.
     *
     * @param id          Mã định danh duy nhất
     * @param fullName    Tên đầy đủ
     * @param phoneNumber Số điện thoại
     * @param email       Email
     * @param address     Địa chỉ
     */
    public Person(String id, String fullName, String phoneNumber, String email, String address) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.createdDate = LocalDate.now();
    }

    // Getter Methods

    /**
     * Lấy mã định danh của người.
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Lấy tên đầy đủ của người.
     *
     * @return Tên đầy đủ
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Lấy số điện thoại của người.
     *
     * @return Số điện thoại
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Lấy email của người.
     *
     * @return Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Lấy địa chỉ của người.
     *
     * @return Địa chỉ
     */
    public String getAddress() {
        return address;
    }

    /**
     * Lấy ngày sinh của người.
     *
     * @return Ngày sinh
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Lấy giới tính của người.
     *
     * @return Giới tính
     */
    public String getGender() {
        return gender;
    }

    /**
     * Lấy ngày tạo hồ sơ của người.
     *
     * @return Ngày tạo
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    // Setter Methods

    /**
     * Cập nhật tên đầy đủ của người.
     *
     * @param fullName Tên đầy đủ
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Cập nhật số điện thoại của người.
     *
     * @param phoneNumber Số điện thoại
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Cập nhật email của người.
     *
     * @param email Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Cập nhật địa chỉ của người.
     *
     * @param address Địa chỉ
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Cập nhật ngày sinh của người.
     *
     * @param dateOfBirth Ngày sinh
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Cập nhật giới tính của người.
     *
     * @param gender Giới tính
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    // Phương thức chung (Common Methods)

    /**
     * Lấy thông tin chi tiết của người dưới dạng chuỗi.
     *
     * @return Thông tin chi tiết
     */
    public String getPersonInfo() {
        return String.format(
                "ID: %s\n" +
                        "Tên: %s\n" +
                        "SĐT: %s\n" +
                        "Email: %s\n" +
                        "Địa chỉ: %s\n" +
                        "Ngày sinh: %s\n" +
                        "Giới tính: %s\n" +
                        "Ngày tạo: %s",
                id, fullName, phoneNumber, email, address, dateOfBirth, gender, createdDate);
    }

    /**
     * So sánh hai đối tượng Person dựa trên ID.
     *
     * @param obj Đối tượng cần so sánh
     * @return true nếu cùng ID, false nếu khác
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Person person = (Person) obj;
        return id != null && id.equals(person.id);
    }

    /**
     * Lấy mã hash dựa trên ID.
     *
     * @return Mã hash
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Trả về chuỗi đại diện cho Person.
     *
     * @return Chuỗi đại diện
     */
    @Override
    public String toString() {
        return String.format(
                "Person{id='%s', fullName='%s', phoneNumber='%s', email='%s'}",
                id, fullName, phoneNumber, email);
    }
}
