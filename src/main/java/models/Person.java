package models;

import java.time.LocalDate;

import interfaces.IEntity;

/**
 * Lớp cơ sở (superclass) đại diện cho một người (khách hàng, nhân viên, v.v.).
 * Chứa các thông tin cá nhân chung.
 */
public abstract class Person implements IEntity {

    // Thuộc tính (Attributes) theo UML
    private String personId;
    private String fullName;
    private String phoneNumber;
    private boolean isMale;
    private LocalDate birthDate;
    private String email;
    private String address;
    private boolean isDeleted;
    private LocalDate createdDate;

    /**
     * Constructor khởi tạo Person với các thông tin cơ bản.
     *
     * @param personId    Mã định danh duy nhất
     * @param fullName    Tên đầy đủ
     * @param phoneNumber Số điện thoại
     * @param isMale      Giới tính (true = Nam, false = Nữ)
     * @param birthDate   Ngày sinh
     * @param email       Email
     * @param address     Địa chỉ
     */
    public Person(String personId, String fullName, String phoneNumber, boolean isMale, LocalDate birthDate,
            String email, String address) {
        this.personId = personId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.isMale = isMale;
        this.birthDate = birthDate;
        this.email = email;
        this.address = address;
        this.isDeleted = false;
        this.createdDate = LocalDate.now();
    }

    /**
     * Constructor tiện lợi khi chưa có một số thông tin (email, address).
     *
     * @param personId    Mã định danh
     * @param fullName    Tên đầy đủ
     * @param phoneNumber Số điện thoại
     * @param isMale      Giới tính
     * @param birthDate   Ngày sinh
     */
    public Person(String personId, String fullName, String phoneNumber, boolean isMale, LocalDate birthDate) {
        this(personId, fullName, phoneNumber, isMale, birthDate, "", "");
    }

    // Getter Methods

    /**
     * Lấy mã định danh của người.
     *
     * @return ID
     */
    @Override
    public String getId() {
        return personId;
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
     * Trả về tuổi hiện tại tính từ birthDate.
     * Nếu birthDate là null, trả về -1 để biểu thị không xác định.
     *
     * @return tuổi (số nguyên >= 0) hoặc -1 nếu không có birthDate
     */
    public int getAge() {
        if (birthDate == null) {
            return -1;
        }
        LocalDate now = LocalDate.now();
        int age = now.getYear() - birthDate.getYear();
        // Nếu chưa tới sinh nhật trong năm hiện tại thì trừ 1
        if (now.getMonthValue() < birthDate.getMonthValue()
                || (now.getMonthValue() == birthDate.getMonthValue()
                        && now.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age -= 1;
        }
        return age;
    }

    /**
     * Lấy ngày sinh của người.
     *
     * @return Ngày sinh
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Lấy giới tính của người.
     *
     * @return Giới tính
     */
    public boolean isMale() {
        return isMale;
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
     * @param birthDate Ngày sinh
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Cập nhật giới tính (true = Nam, false = Nữ)
     *
     * @param isMale Giới tính
     */
    public void setMale(boolean isMale) {
        this.isMale = isMale;
    }

    /**
     * Trả về chuỗi mô tả giới tính: "Nam" hoặc "Nữ".
     * Nếu không rõ, trả về "Không xác định".
     *
     * @return chuỗi giới tính
     */
    public String getGenderString() {
        return isMale ? "Nam" : "Nữ";
    }

    /**
     * Kiểm tra trạng thái xóa mềm (soft delete).
     *
     * @return true nếu đã bị xóa
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Đánh dấu (hoặc bỏ đánh dấu) xóa mềm.
     *
     * @param deleted trạng thái xóa
     */
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    /**
     * Cập nhật thông tin liên hệ: số điện thoại, email và địa chỉ.
     * Các tham số null sẽ được bỏ qua (không cập nhật trường tương ứng).
     *
     * @param phoneNumber số điện thoại mới (hoặc null)
     * @param email       email mới (hoặc null)
     * @param address     địa chỉ mới (hoặc null)
     */
    public void updateContactInfo(String phoneNumber, String email, String address) {
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
        if (email != null) {
            this.email = email;
        }
        if (address != null) {
            this.address = address;
        }
    }

    /**
     * Đánh dấu xóa mềm (soft delete).
     */
    public void softDelete() {
        this.isDeleted = true;
    }

    /**
     * Khôi phục từ trạng thái xóa mềm.
     */
    public void restore() {
        this.isDeleted = false;
    }

    // Phương thức trừu tượng (Abstract Methods)

    /**
     * Lấy vai trò của người (khách hàng, nhân viên, quản lý, v.v.).
     *
     * @return Vai trò của người
     */
    public abstract String getRole();

    // Phương thức implement từ IEntity

    /**
     * Hiển thị thông tin của Person.
     */
    @Override
    public void display() {
        System.out.println("=== THÔNG TIN NGƯỜI ===");
        System.out.println("ID: " + personId);
        System.out.println("Tên: " + fullName);
        System.out.println("SĐT: " + phoneNumber);
        System.out.println("Email: " + email);
        System.out.println("Địa chỉ: " + address);
        System.out.println("Ngày sinh: " + birthDate);
        System.out.println("Giới tính: " + (isMale ? "Nam" : "Nữ"));
        System.out.println("Vai trò: " + getRole());
        System.out.println("Ngày tạo: " + createdDate);
        System.out.println("========================");
    }

    /**
     * Nhập thông tin cho Person từ bàn phím.
     */
    @Override
    public void input() {
        // Phương thức này sẽ được implement trong các lớp con cụ thể
        // vì mỗi loại Person có thể cần nhập thông tin khác nhau
    }

    /**
     * Lấy tiền tố cho ID của Person.
     *
     * @return Tiền tố ID
     */
    @Override
    public String getPrefix() {
        return "PER"; // Tiền tố chung cho Person
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
                personId, fullName, phoneNumber, email, address, birthDate, (isMale ? "Nam" : "Nữ"), createdDate);
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
        return personId != null && personId.equals(person.personId);
    }

    /**
     * Lấy mã hash dựa trên ID.
     *
     * @return Mã hash
     */
    @Override
    public int hashCode() {
        return personId != null ? personId.hashCode() : 0;
    }

    /**
     * Trả về chuỗi đại diện cho Person.
     *
     * @return Chuỗi đại diện
     */
    @Override
    public String toString() {
        return String.format(
                "Person{personId='%s', fullName='%s', phoneNumber='%s', email='%s'}",
                personId, fullName, phoneNumber, email);
    }
}
