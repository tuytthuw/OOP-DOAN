package com.spa.service;

import com.spa.data.DataStore;
import com.spa.model.Employee;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Dịch vụ xác thực người dùng theo mẫu Singleton.
 */
public final class AuthService {
    private static AuthService instance;

    private final DataStore<Employee> employeeStore;
    private Employee currentUser;

    private AuthService(DataStore<Employee> employeeStore) {
        this.employeeStore = employeeStore;
        this.employeeStore.readFile();
    }

    /**
     * Lấy thể hiện duy nhất của dịch vụ.
     *
     * @param employeeStore kho nhân viên
     * @return thể hiện duy nhất
     */
    public static synchronized AuthService getInstance(DataStore<Employee> employeeStore) {
        if (instance == null) {
            instance = new AuthService(employeeStore);
        }
        return instance;
    }

    /**
     * Thực hiện đăng nhập với mã và mật khẩu.
     *
     * @param id mã nhân viên
     * @param password mật khẩu thô
     * @return true nếu đăng nhập thành công
     */
    public boolean login(String id, String password) {
        if (id == null || password == null) {
            return false;
        }
        Employee employee = employeeStore.findById(id);
        if (employee == null || employee.isDeleted()) {
            return false;
        }
        String hashedInput = encryptPassword(password);
        if (!hashedInput.equals(employee.getPasswordHash())) {
            return false;
        }
        currentUser = employee;
        return true;
    }

    /**
     * Đăng xuất khỏi hệ thống.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Kiểm tra trạng thái đăng nhập.
     *
     * @return true nếu đang đăng nhập
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Lấy người dùng hiện tại.
     *
     * @return nhân viên đang đăng nhập hoặc null
     */
    public Employee getCurrentUser() {
        return currentUser;
    }

    /**
     * Lấy vai trò của người dùng hiện tại.
     *
     * @return tên vai trò hoặc chuỗi rỗng nếu chưa đăng nhập
     */
    public String getCurrentRole() {
        if (currentUser == null) {
            return "";
        }
        return currentUser.getRole();
    }

    /**
     * Đổi mật khẩu cho người dùng hiện tại.
     *
     * @param oldPassword mật khẩu cũ
     * @param newPassword mật khẩu mới
     * @return true nếu đổi thành công
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }
        if (!encryptPassword(oldPassword).equals(currentUser.getPasswordHash())) {
            return false;
        }
        currentUser.setPasswordHash(encryptPassword(newPassword));
        employeeStore.writeFile();
        return true;
    }

    /**
     * Tạo tài khoản quản trị mặc định nếu kho rỗng.
     *
     * @param seedEmployee nhân viên sẽ được thêm khi kho rỗng
     */
    public void ensureSeedEmployee(Employee seedEmployee) {
        if (seedEmployee == null) {
            return;
        }
        if (employeeStore.getCount() == 0) {
            seedEmployee.setPasswordHash(encryptPassword(seedEmployee.getPasswordHash()));
            employeeStore.add(seedEmployee);
            employeeStore.writeFile();
        }
    }

    private String encryptPassword(String raw) {
        if (raw == null) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte value : hash) {
                String hex = Integer.toHexString(0xff & value);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            return raw;
        }
    }
}
