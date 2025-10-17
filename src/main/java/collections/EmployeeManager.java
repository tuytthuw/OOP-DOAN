package collections;

import java.math.BigDecimal;

import enums.EmployeeRole;
import enums.EmployeeStatus;
import models.Employee;

/**
 * Lớp EmployeeManager quản lý tập hợp các nhân viên trong hệ thống Spa.
 * Kế thừa từ BaseManager<Employee> và cung cấp các phương thức quản lý nhân
 * viên.
 * Sử dụng mảng để lưu trữ dữ liệu (không dùng List).
 */
public class EmployeeManager extends BaseManager<Employee> {

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo EmployeeManager với dung lượng mặc định.
     */
    public EmployeeManager() {
        super();
    }

    // ============ CÁC PHƯƠNG THỨC TÌM KIẾM ============

    /**
     * Tìm tất cả nhân viên theo vai trò.
     *
     * @param role Vai trò cần tìm
     * @return Mảng các nhân viên có vai trò tương ứng
     */
    public Employee[] findByRole(EmployeeRole role) {
        if (role == null) {
            return new Employee[0];
        }

        // Đếm số lượng nhân viên có vai trò này
        int count = 0;
        for (int i = 0; i < size(); i++) {
            Employee emp = getAll()[i];
            if (emp != null && emp.getEmployeeRole() == role) {
                count++;
            }
        }

        // Tạo mảng kết quả
        if (count == 0) {
            return new Employee[0];
        }

        Employee[] result = new Employee[count];
        int index = 0;
        for (int i = 0; i < size(); i++) {
            Employee emp = getAll()[i];
            if (emp != null && emp.getEmployeeRole() == role) {
                result[index] = emp;
                index++;
            }
        }
        return result;
    }

    /**
     * Tìm tất cả nhân viên theo trạng thái.
     *
     * @param status Trạng thái cần tìm
     * @return Mảng các nhân viên có trạng thái tương ứng
     */
    public Employee[] findByStatus(EmployeeStatus status) {
        if (status == null) {
            return new Employee[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size(); i++) {
            Employee emp = getAll()[i];
            if (emp != null && emp.getStatus() == status) {
                count++;
            }
        }

        // Tạo mảng kết quả
        if (count == 0) {
            return new Employee[0];
        }

        Employee[] result = new Employee[count];
        int index = 0;
        for (int i = 0; i < size(); i++) {
            Employee emp = getAll()[i];
            if (emp != null && emp.getStatus() == status) {
                result[index] = emp;
                index++;
            }
        }
        return result;
    }

    /**
     * Tìm tất cả nhân viên đang hoạt động (trạng thái ACTIVE).
     *
     * @return Mảng các nhân viên đang hoạt động
     */
    public Employee[] findActiveEmployees() {
        return findByStatus(EmployeeStatus.ACTIVE);
    }

    /**
     * Tìm kỹ thuật viên có kỹ năng nhất định.
     * Chỉ tìm trong các nhân viên có vai trò TECHNICIAN.
     *
     * @param skill Tên kỹ năng cần tìm
     * @return Mảng các kỹ thuật viên có kỹ năng tương ứng
     */
    public Employee[] findTechnicianWithSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return new Employee[0];
        }

        Employee[] technicians = findByRole(EmployeeRole.TECHNICIAN);
        int count = 0;

        // Đếm kỹ thuật viên có kỹ năng này
        for (Employee emp : technicians) {
            if (emp != null) {
                // Kiểm tra nếu emp là Technician và có kỹ năng
                // Vì kỹ năng được quản lý trong lớp Technician
                // Cần cast hoặc check type
                try {
                    models.Technician tech = (models.Technician) emp;
                    if (tech.hasSkill(skill)) {
                        count++;
                    }
                } catch (ClassCastException e) {
                    // Bỏ qua nếu không phải Technician
                }
            }
        }

        // Tạo mảng kết quả
        if (count == 0) {
            return new Employee[0];
        }

        Employee[] result = new Employee[count];
        int index = 0;
        for (Employee emp : technicians) {
            if (emp != null) {
                try {
                    models.Technician tech = (models.Technician) emp;
                    if (tech.hasSkill(skill)) {
                        result[index] = emp;
                        index++;
                    }
                } catch (ClassCastException e) {
                    // Bỏ qua
                }
            }
        }
        return result;
    }

    /**
     * Tính tổng lương phải trả cho tất cả nhân viên.
     *
     * @return Tổng lương (BigDecimal)
     */
    public BigDecimal getTotalPayroll() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < size(); i++) {
            Employee emp = getAll()[i];
            if (emp != null) {
                total = total.add(emp.calculatePay());
            }
        }
        return total;
    }

    /**
     * Tính tổng lương cho một vai trò cụ thể.
     *
     * @param role Vai trò cần tính
     * @return Tổng lương của tất cả nhân viên có vai trò này
     */
    public BigDecimal getPayrollByRole(EmployeeRole role) {
        Employee[] employees = findByRole(role);
        BigDecimal total = BigDecimal.ZERO;
        for (Employee emp : employees) {
            if (emp != null) {
                total = total.add(emp.calculatePay());
            }
        }
        return total;
    }

    /**
     * Lấy danh sách nhân viên theo tên (tìm kiếm gần đúng).
     *
     * @param nameKeyword Từ khóa tên cần tìm
     * @return Mảng các nhân viên có tên chứa từ khóa
     */
    public Employee[] findByName(String nameKeyword) {
        if (nameKeyword == null || nameKeyword.trim().isEmpty()) {
            return new Employee[0];
        }

        String keyword = nameKeyword.toLowerCase().trim();
        int count = 0;

        // Đếm nhân viên có tên chứa từ khóa
        for (int i = 0; i < size(); i++) {
            Employee emp = getAll()[i];
            if (emp != null && emp.getFullName().toLowerCase().contains(keyword)) {
                count++;
            }
        }

        // Tạo mảng kết quả
        if (count == 0) {
            return new Employee[0];
        }

        Employee[] result = new Employee[count];
        int index = 0;
        for (int i = 0; i < size(); i++) {
            Employee emp = getAll()[i];
            if (emp != null && emp.getFullName().toLowerCase().contains(keyword)) {
                result[index] = emp;
                index++;
            }
        }
        return result;
    }

    /**
     * Hiển thị danh sách tất cả nhân viên.
     */
    public void displayAll() {
        if (size() == 0) {
            System.out.println("Không có nhân viên nào trong hệ thống.");
            return;
        }

        System.out.println("\n========== DANH SÁCH NHÂN VIÊN ==========");
        System.out.printf("%-10s %-20s %-15s %-15s %-10s %-15s\n",
                "Mã NV", "Tên", "Vai trò", "Lương cơ bản", "Trạng thái", "Năm kinh nghiệm");
        System.out.println("----------------------------------------");

        for (int i = 0; i < size(); i++) {
            Employee emp = getAll()[i];
            if (emp != null) {
                System.out.printf("%-10s %-20s %-15s %-15s %-10s %-15d\n",
                        emp.getEmployeeId(),
                        emp.getFullName(),
                        emp.getRole(),
                        emp.getSalary() + " VND",
                        emp.getStatus(),
                        emp.getYearsOfService());
            }
        }
        System.out.println("========================================\n");
    }

    /**
     * Hiển thị thống kê nhân viên theo vai trò.
     */
    public void displayStatisticsByRole() {
        System.out.println("\n========== THỐNG KÊ NHÂN VIÊN THEO VAI TRÒ ==========");

        for (EmployeeRole role : EmployeeRole.values()) {
            Employee[] employees = findByRole(role);
            BigDecimal payroll = getPayrollByRole(role);
            System.out.println("Vai trò: " + role + " - Số lượng: " + employees.length +
                    " - Tổng lương: " + payroll + " VND");
        }

        System.out.println("Tổng cộng tất cả nhân viên: " + size());
        System.out.println("Tổng lương phải trả: " + getTotalPayroll() + " VND");
        System.out.println("=================================================\n");
    }

    /**
     * Hiển thị thống kê nhân viên theo trạng thái.
     */
    public void displayStatisticsByStatus() {
        System.out.println("\n========== THỐNG KÊ NHÂN VIÊN THEO TRẠNG THÁI ==========");

        for (EmployeeStatus status : EmployeeStatus.values()) {
            Employee[] employees = findByStatus(status);
            System.out.println("Trạng thái: " + status + " - Số lượng: " + employees.length);
        }

        System.out.println("Tổng cộng tất cả nhân viên: " + size());
        System.out.println("=====================================================\n");
    }
}
