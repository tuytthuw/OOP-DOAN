package services;

import java.math.BigDecimal;
import java.time.LocalDate;

import collections.EmployeeManager;
import enums.EmployeeRole;
import enums.EmployeeStatus;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.ExceptionHandler;
import exceptions.InvalidEntityException;
import models.Employee;
import models.Receptionist;
import models.Technician;

/**
 * Lớp EmployeeService cung cấp các dịch vụ quản lý nhân viên cho hệ thống Spa.
 * Xử lý tất cả logic kinh doanh liên quan đến nhân viên bao gồm:
 * - Thêm, cập nhật, xóa nhân viên
 * - Tìm kiếm nhân viên
 * - Quản lý lương và hoa hồng
 * - Xác thực thông tin nhân viên
 * Sử dụng EmployeeManager để quản lý dữ liệu.
 */
public class EmployeeService {

    // ============ THUỘC TÍNH ============
    private EmployeeManager employeeManager;

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo EmployeeService.
     */
    public EmployeeService() {
        this.employeeManager = new EmployeeManager();
    }

    /**
     * Constructor khởi tạo EmployeeService với một EmployeeManager có sẵn.
     *
     * @param employeeManager EmployeeManager được truyền vào
     */
    public EmployeeService(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    // ============ CÁC PHƯƠNG THỨC QUẢN LÝ ============

    /**
     * Thêm một nhân viên mới vào hệ thống.
     *
     * @param employee Đối tượng nhân viên cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addEmployee(Employee employee) {
        try {
            return employeeManager.add(employee);
        } catch (InvalidEntityException | EntityAlreadyExistsException e) {
            ExceptionHandler.getInstance().displayErrorToUser(e);
            return false;
        }
    }

    /**
     * Thêm một tiếp tân mới vào hệ thống.
     *
     * @param employeeId   Mã nhân viên
     * @param fullName     Họ tên đầy đủ
     * @param phoneNumber  Số điện thoại
     * @param email        Email
     * @param address      Địa chỉ
     * @param isMale       Giới tính
     * @param birthDate    Ngày sinh
     * @param salary       Lương cơ bản
     * @param hireDate     Ngày tuyển dụng
     * @param bonusPerSale Hoa hồng trên mỗi đơn bán
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addReceptionist(String employeeId, String fullName, String phoneNumber,
            String email, String address, boolean isMale,
            LocalDate birthDate, BigDecimal salary, LocalDate hireDate,
            BigDecimal bonusPerSale) {
        try {
            Receptionist receptionist = new Receptionist(
                    employeeId, fullName, phoneNumber, email, address,
                    isMale, birthDate, salary, hireDate, bonusPerSale);
            return employeeManager.add(receptionist);
        } catch (InvalidEntityException | EntityAlreadyExistsException e) {
            ExceptionHandler.getInstance().displayErrorToUser(e);
            return false;
        }
    }

    /**
     * Thêm một kỹ thuật viên mới vào hệ thống.
     *
     * @param employeeId     Mã nhân viên
     * @param fullName       Họ tên đầy đủ
     * @param phoneNumber    Số điện thoại
     * @param email          Email
     * @param address        Địa chỉ
     * @param isMale         Giới tính
     * @param birthDate      Ngày sinh
     * @param salary         Lương cơ bản
     * @param hireDate       Ngày tuyển dụng
     * @param commissionRate Tỷ lệ hoa hồng
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addTechnician(String employeeId, String fullName, String phoneNumber,
            String email, String address, boolean isMale,
            LocalDate birthDate, BigDecimal salary, LocalDate hireDate,
            double commissionRate) {
        try {
            Technician technician = new Technician(
                    employeeId, fullName, phoneNumber, email, address,
                    isMale, birthDate, salary, hireDate, commissionRate);
            return employeeManager.add(technician);
        } catch (InvalidEntityException | EntityAlreadyExistsException e) {
            ExceptionHandler.getInstance().displayErrorToUser(e);
            return false;
        }
    }

    /**
     * Cập nhật thông tin nhân viên.
     *
     * @param employee Đối tượng nhân viên với thông tin cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateEmployee(Employee employee) {
        try {
            return employeeManager.update(employee);
        } catch (InvalidEntityException | EntityNotFoundException e) {
            ExceptionHandler.getInstance().displayErrorToUser(e);
            return false;
        }
    }

    /**
     * Xóa một nhân viên khỏi hệ thống.
     *
     * @param employeeId Mã nhân viên cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteEmployee(String employeeId) {
        try {
            return employeeManager.delete(employeeId);
        } catch (InvalidEntityException | EntityNotFoundException e) {
            ExceptionHandler.getInstance().displayErrorToUser(e);
            return false;
        }
    }

    /**
     * Lấy thông tin chi tiết của một nhân viên theo ID.
     *
     * @param employeeId Mã nhân viên
     * @return Đối tượng Employee nếu tìm thấy, null nếu không
     */
    public Employee getEmployeeById(String employeeId) {
        try {
            return employeeManager.getById(employeeId);
        } catch (InvalidEntityException | EntityNotFoundException e) {
            ExceptionHandler.getInstance().displayErrorToUser(e);
            return null;
        }
    }

    /**
     * Lấy danh sách tất cả nhân viên.
     *
     * @return Mảy các nhân viên
     */
    public Employee[] getAllEmployees() {
        return employeeManager.getAll();
    }

    /**
     * Tìm nhân viên theo vai trò.
     *
     * @param role Vai trò cần tìm
     * @return Mảy các nhân viên có vai trò tương ứng
     */
    public Employee[] findByRole(EmployeeRole role) {
        return employeeManager.findByRole(role);
    }

    /**
     * Tìm nhân viên theo trạng thái.
     *
     * @param status Trạng thái cần tìm
     * @return Mảy các nhân viên có trạng thái tương ứng
     */
    public Employee[] findByStatus(EmployeeStatus status) {
        return employeeManager.findByStatus(status);
    }

    /**
     * Tìm nhân viên đang hoạt động.
     *
     * @return Mảy các nhân viên có trạng thái ACTIVE
     */
    public Employee[] findActiveEmployees() {
        return employeeManager.findActiveEmployees();
    }

    /**
     * Tìm kỹ thuật viên có kỹ năng nhất định.
     *
     * @param skill Kỹ năng cần tìm
     * @return Mảy các kỹ thuật viên có kỹ năng tương ứng
     */
    public Employee[] findTechnicianWithSkill(String skill) {
        return employeeManager.findTechnicianWithSkill(skill);
    }

    /**
     * Tìm nhân viên theo tên (tìm kiếm gần đúng).
     *
     * @param nameKeyword Từ khóa tên cần tìm
     * @return Mảy các nhân viên có tên chứa từ khóa
     */
    public Employee[] findByName(String nameKeyword) {
        return employeeManager.findByName(nameKeyword);
    }

    /**
     * Thay đổi trạng thái của nhân viên.
     *
     * @param employeeId Mã nhân viên
     * @param newStatus  Trạng thái mới
     * @return true nếu thay đổi thành công, false nếu thất bại
     */
    public boolean changeEmployeeStatus(String employeeId, EmployeeStatus newStatus) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null) {
            return false;
        }
        emp.changeStatus(newStatus);
        return updateEmployee(emp);
    }

    /**
     * Cập nhật lương cơ bản của nhân viên.
     *
     * @param employeeId Mã nhân viên
     * @param newSalary  Lương cơ bản mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateEmployeeSalary(String employeeId, BigDecimal newSalary) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null || newSalary == null || newSalary.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        emp.setSalary(newSalary);
        return updateEmployee(emp);
    }

    /**
     * Thêm doanh số cho một tiếp tân.
     *
     * @param employeeId Mã nhân viên (phải là tiếp tân)
     * @param amount     Số tiền doanh số
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean addSaleForReceptionist(String employeeId, BigDecimal amount) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null || !(emp instanceof Receptionist)) {
            return false;
        }
        Receptionist receptionist = (Receptionist) emp;
        receptionist.addSale(amount);
        return updateEmployee(receptionist);
    }

    /**
     * Thêm hoa hồng cho một kỹ thuật viên.
     *
     * @param employeeId Mã nhân viên (phải là kỹ thuật viên)
     * @param amount     Số tiền doanh số để tính hoa hồng
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean addCommissionForTechnician(String employeeId, BigDecimal amount) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null || !(emp instanceof Technician)) {
            return false;
        }
        Technician technician = (Technician) emp;
        technician.addCommission(amount);
        return updateEmployee(technician);
    }

    /**
     * Thêm kỹ năng cho một kỹ thuật viên.
     *
     * @param employeeId Mã nhân viên (phải là kỹ thuật viên)
     * @param skill      Kỹ năng cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addSkillForTechnician(String employeeId, String skill) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null || !(emp instanceof Technician)) {
            return false;
        }
        Technician technician = (Technician) emp;
        boolean result = technician.addSkill(skill);
        if (result) {
            updateEmployee(technician);
        }
        return result;
    }

    /**
     * Thêm chứng chỉ cho một kỹ thuật viên.
     *
     * @param employeeId    Mã nhân viên (phải là kỹ thuật viên)
     * @param certification Chứng chỉ cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addCertificationForTechnician(String employeeId, String certification) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null || !(emp instanceof Technician)) {
            return false;
        }
        Technician technician = (Technician) emp;
        boolean result = technician.addCertification(certification);
        if (result) {
            updateEmployee(technician);
        }
        return result;
    }

    /**
     * Cập nhật đánh giá hiệu suất của kỹ thuật viên.
     *
     * @param employeeId Mã nhân viên (phải là kỹ thuật viên)
     * @param rating     Đánh giá hiệu suất mới (0.0 - 5.0)
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTechnicianRating(String employeeId, double rating) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null || !(emp instanceof Technician)) {
            return false;
        }
        Technician technician = (Technician) emp;
        technician.updatePerformanceRating(rating);
        return updateEmployee(technician);
    }

    /**
     * Tính tổng lương của tất cả nhân viên.
     *
     * @return Tổng lương phải trả
     */
    public BigDecimal getTotalPayroll() {
        return employeeManager.getTotalPayroll();
    }

    /**
     * Tính tổng lương cho một vai trò cụ thể.
     *
     * @param role Vai trò cần tính
     * @return Tổng lương của nhân viên có vai trò này
     */
    public BigDecimal getPayrollByRole(EmployeeRole role) {
        return employeeManager.getPayrollByRole(role);
    }

    /**
     * Hiển thị danh sách tất cả nhân viên.
     */
    public void displayAll() {
        employeeManager.displayAll();
    }

    /**
     * Hiển thị thống kê nhân viên theo vai trò.
     */
    public void displayStatisticsByRole() {
        employeeManager.displayStatisticsByRole();
    }

    /**
     * Hiển thị thống kê nhân viên theo trạng thái.
     */
    public void displayStatisticsByStatus() {
        employeeManager.displayStatisticsByStatus();
    }

    /**
     * Lấy EmployeeManager để truy cập trực tiếp nếu cần.
     *
     * @return EmployeeManager của dịch vụ
     */
    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }
}
