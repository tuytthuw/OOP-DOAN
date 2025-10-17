package ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import enums.EmployeeRole;
import enums.EmployeeStatus;
import io.InputHandler;
import io.OutputFormatter;
import models.Employee;
import models.Receptionist;
import models.Technician;
import services.EmployeeService;

/**
 * Lớp EmployeeMenu cung cấp giao diện menu cho quản lý nhân viên.
 * Cho phép người dùng thực hiện các thao tác CRUD và quản lý nhân viên.
 * Kế thừa từ MenuBase và triển khai Template Method Pattern.
 */
public class EmployeeMenu extends MenuBase {

    // ============ THUỘC TÍNH ============
    private EmployeeService employeeService;
    private DateTimeFormatter dateFormatter;

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo EmployeeMenu.
     *
     * @param inputHandler    Bộ xử lý input
     * @param outputFormatter Bộ định dạng output
     * @param employeeService EmployeeService cung cấp logic quản lý nhân viên
     */
    public EmployeeMenu(InputHandler inputHandler, OutputFormatter outputFormatter,
            EmployeeService employeeService) {
        super(inputHandler, outputFormatter);
        this.employeeService = employeeService;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    // ============ OVERRIDE PHƯƠNG THỨC TRỪU TƯỢNG ============

    @Override
    protected void displayMenu() {
        System.out.println("\n========== MENU QUẢN LÝ NHÂN VIÊN ==========");
        System.out.println("1. Xem danh sách tất cả nhân viên");
        System.out.println("2. Thêm nhân viên mới");
        System.out.println("3. Cập nhật thông tin nhân viên");
        System.out.println("4. Xóa nhân viên");
        System.out.println("5. Tìm kiếm nhân viên");
        System.out.println("6. Quản lý doanh số và hoa hồng");
        System.out.println("7. Quản lý kỹ năng kỹ thuật viên");
        System.out.println("8. Xem thống kê nhân viên");
        System.out.println("9. Thay đổi trạng thái nhân viên");
        System.out.println("0. Quay lại");
        System.out.println("==========================================");
    }

    @Override
    protected boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                displayAllEmployees();
                break;
            case 2:
                addNewEmployee();
                break;
            case 3:
                updateEmployee();
                break;
            case 4:
                deleteEmployee();
                break;
            case 5:
                searchEmployee();
                break;
            case 6:
                manageSalesAndCommission();
                break;
            case 7:
                manageTechnicianSkills();
                break;
            case 8:
                displayStatistics();
                break;
            case 9:
                changeEmployeeStatus();
                break;
            case 0:
                return false;
            default:
                System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
        }
        return true;
    }

    // ============ CÁC PHƯƠNG THỨC XỬ LÝ THAO TÁC ============

    /**
     * Hiển thị danh sách tất cả nhân viên.
     */
    private void displayAllEmployees() {
        employeeService.displayAll();
    }

    /**
     * Thêm nhân viên mới (tiếp tân hoặc kỹ thuật viên).
     */
    private void addNewEmployee() {
        System.out.println("\n========== THÊM NHÂN VIÊN MỚI ==========");
        System.out.println("1. Thêm Tiếp tân");
        System.out.println("2. Thêm Kỹ thuật viên");
        System.out.println("0. Quay lại");

        int choice = inputHandler.readInt("Chọn loại nhân viên: ");
        switch (choice) {
            case 1:
                addReceptionist();
                break;
            case 2:
                addTechnician();
                break;
            case 0:
                return;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
        }
    }

    /**
     * Thêm tiếp tân mới.
     */
    private void addReceptionist() {
        System.out.println("\n--- Thông tin Tiếp tân ---");
        String employeeId = inputHandler.readString("Mã nhân viên: ");
        String fullName = inputHandler.readString("Họ tên: ");
        String phoneNumber = inputHandler.readString("Điện thoại: ");
        String email = inputHandler.readString("Email: ");
        String address = inputHandler.readString("Địa chỉ: ");
        boolean isMale = inputHandler.readBoolean("Nam/Nữ (true/false): ");

        String birthDateStr = inputHandler.readString("Ngày sinh (yyyy-MM-dd): ");
        LocalDate birthDate = LocalDate.parse(birthDateStr, dateFormatter);

        String salaryStr = inputHandler.readString("Lương cơ bản (VND): ");
        BigDecimal salary = new BigDecimal(salaryStr);

        String hireDateStr = inputHandler.readString("Ngày tuyển dụng (yyyy-MM-dd): ");
        LocalDate hireDate = LocalDate.parse(hireDateStr, dateFormatter);

        String bonusStr = inputHandler.readString("Hoa hồng/đơn (VND): ");
        BigDecimal bonusPerSale = new BigDecimal(bonusStr);

        if (employeeService.addReceptionist(employeeId, fullName, phoneNumber,
                email, address, isMale, birthDate, salary, hireDate, bonusPerSale)) {
            System.out.println("✓ Thêm tiếp tân thành công.");
        } else {
            System.out.println("✗ Thêm tiếp tân thất bại.");
        }
        pauseForContinue();
    }

    /**
     * Thêm kỹ thuật viên mới.
     */
    private void addTechnician() {
        System.out.println("\n--- Thông tin Kỹ thuật viên ---");
        String employeeId = inputHandler.readString("Mã nhân viên: ");
        String fullName = inputHandler.readString("Họ tên: ");
        String phoneNumber = inputHandler.readString("Điện thoại: ");
        String email = inputHandler.readString("Email: ");
        String address = inputHandler.readString("Địa chỉ: ");
        boolean isMale = inputHandler.readBoolean("Nam/Nữ (true/false): ");

        String birthDateStr = inputHandler.readString("Ngày sinh (yyyy-MM-dd): ");
        LocalDate birthDate = LocalDate.parse(birthDateStr, dateFormatter);

        String salaryStr = inputHandler.readString("Lương cơ bản (VND): ");
        BigDecimal salary = new BigDecimal(salaryStr);

        String hireDateStr = inputHandler.readString("Ngày tuyển dụng (yyyy-MM-dd): ");
        LocalDate hireDate = LocalDate.parse(hireDateStr, dateFormatter);

        String commissionStr = inputHandler.readString("Tỷ lệ hoa hồng (0.05 = 5%): ");
        double commissionRate = Double.parseDouble(commissionStr);

        if (employeeService.addTechnician(employeeId, fullName, phoneNumber,
                email, address, isMale, birthDate, salary, hireDate, commissionRate)) {
            System.out.println("✓ Thêm kỹ thuật viên thành công.");
        } else {
            System.out.println("✗ Thêm kỹ thuật viên thất bại.");
        }
        pauseForContinue();
    }

    /**
     * Cập nhật thông tin nhân viên.
     */
    private void updateEmployee() {
        System.out.println("\n========== CẬP NHẬT NHÂN VIÊN ==========");
        String employeeId = inputHandler.readString("Nhập mã nhân viên: ");
        Employee emp = employeeService.getEmployeeById(employeeId);

        if (emp == null) {
            System.out.println("✗ Nhân viên không tìm thấy.");
            return;
        }

        emp.display();
        System.out.println("1. Cập nhật lương");
        System.out.println("2. Thay đổi trạng thái");
        System.out.println("3. Cập nhật số điện thoại");
        System.out.println("0. Quay lại");
        System.out.print("Chọn thao tác: ");

        int choice = inputHandler.readInt("Chọn thao tác: ");
        switch (choice) {
            case 1:
                String salaryStr = inputHandler.readString("Lương mới (VND): ");
                BigDecimal newSalary = new BigDecimal(salaryStr);
                if (employeeService.updateEmployeeSalary(employeeId, newSalary)) {
                    System.out.println("✓ Cập nhật lương thành công.");
                }
                break;
            case 2:
                System.out.println("Trạng thái hiện tại: " + emp.getStatus());
                System.out.println("1. ACTIVE");
                System.out.println("2. ON_LEAVE");
                System.out.println("3. SUSPENDED");
                System.out.println("4. RESIGNED");
                String statusChoiceStr = inputHandler.readString("Chọn trạng thái: ");
                int statusChoice = Integer.parseInt(statusChoiceStr);
                EmployeeStatus newStatus = null;
                switch (statusChoice) {
                    case 1:
                        newStatus = EmployeeStatus.ACTIVE;
                        break;
                    case 2:
                        newStatus = EmployeeStatus.ON_LEAVE;
                        break;
                    case 3:
                        newStatus = EmployeeStatus.SUSPENDED;
                        break;
                    case 4:
                        newStatus = EmployeeStatus.RESIGNED;
                        break;
                }
                if (newStatus != null && employeeService.changeEmployeeStatus(employeeId, newStatus)) {
                    System.out.println("✓ Thay đổi trạng thái thành công.");
                }
                break;
            case 3:
                String newPhone = inputHandler.readString("Điện thoại mới: ");
                emp.setPhoneNumber(newPhone);
                if (employeeService.updateEmployee(emp)) {
                    System.out.println("✓ Cập nhật số điện thoại thành công.");
                }
                break;
            case 0:
                return;
        }
        pauseForContinue();
    }

    /**
     * Xóa nhân viên.
     */
    private void deleteEmployee() {
        System.out.println("\n========== XÓA NHÂN VIÊN ==========");
        String employeeId = inputHandler.readString("Nhập mã nhân viên cần xóa: ");
        String confirm = inputHandler.readString("Xác nhận xóa? (yes/no): ");

        if ("yes".equalsIgnoreCase(confirm.trim())) {
            if (employeeService.deleteEmployee(employeeId)) {
                System.out.println("✓ Xóa nhân viên thành công.");
            } else {
                System.out.println("✗ Xóa nhân viên thất bại.");
            }
        } else {
            System.out.println("Hủy bỏ thao tác xóa.");
        }
        pauseForContinue();
    }

    /**
     * Tìm kiếm nhân viên.
     */
    private void searchEmployee() {
        System.out.println("\n========== TÌM KIẾM NHÂN VIÊN ==========");
        System.out.println("1. Tìm theo tên");
        System.out.println("2. Tìm theo vai trò");
        System.out.println("3. Tìm theo trạng thái");
        System.out.println("4. Tìm kỹ thuật viên có kỹ năng");
        System.out.println("0. Quay lại");
        System.out.print("Chọn tiêu chí tìm kiếm: ");

        String choiceStr = inputHandler.readString("Chọn tiêu chí tìm kiếm: ");
        int choice = Integer.parseInt(choiceStr);
        Employee[] results = null;

        switch (choice) {
            case 1:
                String name = inputHandler.readString("Nhập tên cần tìm: ");
                results = employeeService.findByName(name);
                break;
            case 2:
                System.out.println("1. RECEPTIONIST\n2. TECHNICIAN\n3. MANAGER\n4. ADMIN");
                String roleChoiceStr = inputHandler.readString("Chọn vai trò: ");
                int roleChoice = Integer.parseInt(roleChoiceStr);
                EmployeeRole role = null;
                switch (roleChoice) {
                    case 1:
                        role = EmployeeRole.RECEPTIONIST;
                        break;
                    case 2:
                        role = EmployeeRole.TECHNICIAN;
                        break;
                    case 3:
                        role = EmployeeRole.MANAGER;
                        break;
                    case 4:
                        role = EmployeeRole.ADMIN;
                        break;
                }
                if (role != null) {
                    results = employeeService.findByRole(role);
                }
                break;
            case 3:
                System.out.println("1. ACTIVE\n2. ON_LEAVE\n3. SUSPENDED\n4. RESIGNED");
                String statusChoiceStr = inputHandler.readString("Chọn trạng thái: ");
                int statusChoice = Integer.parseInt(statusChoiceStr);
                EmployeeStatus status = null;
                switch (statusChoice) {
                    case 1:
                        status = EmployeeStatus.ACTIVE;
                        break;
                    case 2:
                        status = EmployeeStatus.ON_LEAVE;
                        break;
                    case 3:
                        status = EmployeeStatus.SUSPENDED;
                        break;
                    case 4:
                        status = EmployeeStatus.RESIGNED;
                        break;
                }
                if (status != null) {
                    results = employeeService.findByStatus(status);
                }
                break;
            case 4:
                String skill = inputHandler.readString("Nhập tên kỹ năng: ");
                results = employeeService.findTechnicianWithSkill(skill);
                break;
            case 0:
                return;
        }

        if (results != null && results.length > 0) {
            System.out.println("\n✓ Tìm thấy " + results.length + " nhân viên:");
            for (Employee emp : results) {
                if (emp != null) {
                    emp.display();
                }
            }
        } else {
            System.out.println("✗ Không tìm thấy nhân viên nào.");
        }
        pauseForContinue();
    }

    /**
     * Quản lý doanh số tiếp tân và hoa hồng kỹ thuật viên.
     */
    private void manageSalesAndCommission() {
        System.out.println("\n========== QUẢN LÝ DOANH SỐ & HOA HỒNG ==========");
        String employeeId = inputHandler.readString("Nhập mã nhân viên: ");
        Employee emp = employeeService.getEmployeeById(employeeId);

        if (emp == null) {
            System.out.println("✗ Nhân viên không tìm thấy.");
            return;
        }

        if (emp instanceof Receptionist) {
            System.out.println("--- Quản lý doanh số Tiếp tân ---");
            String amountStr = inputHandler.readString("Nhập doanh số bán hàng (VND): ");
            BigDecimal amount = new BigDecimal(amountStr);
            if (employeeService.addSaleForReceptionist(employeeId, amount)) {
                System.out.println("✓ Thêm doanh số thành công.");
            }
        } else if (emp instanceof Technician) {
            System.out.println("--- Quản lý hoa hồng Kỹ thuật viên ---");
            String amountStr = inputHandler.readString("Nhập doanh số dịch vụ (VND): ");
            BigDecimal amount = new BigDecimal(amountStr);
            if (employeeService.addCommissionForTechnician(employeeId, amount)) {
                System.out.println("✓ Thêm hoa hồng thành công.");
            }
        } else {
            System.out.println("✗ Nhân viên này không phải Tiếp tân hoặc Kỹ thuật viên.");
        }
        pauseForContinue();
    }

    /**
     * Quản lý kỹ năng của kỹ thuật viên.
     */
    private void manageTechnicianSkills() {
        System.out.println("\n========== QUẢN LÝ KỸ NĂNG KỸ THUẬT VIÊN ==========");
        String employeeId = inputHandler.readString("Nhập mã kỹ thuật viên: ");
        Employee emp = employeeService.getEmployeeById(employeeId);

        if (emp == null || !(emp instanceof Technician)) {
            System.out.println("✗ Kỹ thuật viên không tìm thấy.");
            return;
        }

        Technician tech = (Technician) emp;
        System.out.println("1. Thêm kỹ năng");
        System.out.println("2. Xóa kỹ năng");
        System.out.println("3. Xem danh sách kỹ năng");
        System.out.println("4. Thêm chứng chỉ");
        System.out.println("5. Cập nhật đánh giá hiệu suất");
        System.out.println("0. Quay lại");
        System.out.print("Chọn thao tác: ");

        String choiceStr = inputHandler.readString("Chọn thao tác: ");
        int choice = Integer.parseInt(choiceStr);
        switch (choice) {
            case 1:
                String skill = inputHandler.readString("Nhập tên kỹ năng: ");
                if (employeeService.addSkillForTechnician(employeeId, skill)) {
                    System.out.println("✓ Thêm kỹ năng thành công.");
                }
                break;
            case 2:
                skill = inputHandler.readString("Nhập tên kỹ năng cần xóa: ");
                if (tech.removeSkill(skill)) {
                    employeeService.updateEmployee(tech);
                    System.out.println("✓ Xóa kỹ năng thành công.");
                }
                break;
            case 3:
                String[] skills = tech.getSkills();
                if (skills.length > 0) {
                    System.out.println("Danh sách kỹ năng:");
                    for (int i = 0; i < skills.length; i++) {
                        System.out.println("  " + (i + 1) + ". " + skills[i]);
                    }
                } else {
                    System.out.println("Chưa có kỹ năng nào.");
                }
                break;
            case 4:
                String cert = inputHandler.readString("Nhập tên chứng chỉ: ");
                if (employeeService.addCertificationForTechnician(employeeId, cert)) {
                    System.out.println("✓ Thêm chứng chỉ thành công.");
                }
                break;
            case 5:
                String ratingStr = inputHandler.readString("Nhập đánh giá (0.0-5.0): ");
                double rating = Double.parseDouble(ratingStr);
                if (employeeService.updateTechnicianRating(employeeId, rating)) {
                    System.out.println("✓ Cập nhật đánh giá thành công.");
                }
                break;
            case 0:
                return;
        }
        pauseForContinue();
    }

    /**
     * Hiển thị thống kê nhân viên.
     */
    private void displayStatistics() {
        System.out.println("\n========== THỐNG KÊ NHÂN VIÊN ==========");
        System.out.println("1. Thống kê theo vai trò");
        System.out.println("2. Thống kê theo trạng thái");
        System.out.println("0. Quay lại");
        System.out.print("Chọn loại thống kê: ");

        String choiceStr = inputHandler.readString("Chọn loại thống kê: ");
        int choice = Integer.parseInt(choiceStr);
        switch (choice) {
            case 1:
                employeeService.displayStatisticsByRole();
                break;
            case 2:
                employeeService.displayStatisticsByStatus();
                break;
            case 0:
                return;
        }
        pauseForContinue();
    }

    /**
     * Thay đổi trạng thái nhân viên.
     */
    private void changeEmployeeStatus() {
        System.out.println("\n========== THAY ĐỔI TRẠNG THÁI NHÂN VIÊN ==========");
        String employeeId = inputHandler.readString("Nhập mã nhân viên: ");
        System.out.println("1. ACTIVE (Đang làm việc)");
        System.out.println("2. ON_LEAVE (Đang nghỉ)");
        System.out.println("3. SUSPENDED (Tạm ngừng)");
        System.out.println("4. RESIGNED (Đã nghỉ)");
        System.out.print("Chọn trạng thái mới: ");

        String choiceStr = inputHandler.readString("Chọn trạng thái mới: ");
        int choice = Integer.parseInt(choiceStr);
        EmployeeStatus newStatus = null;

        switch (choice) {
            case 1:
                newStatus = EmployeeStatus.ACTIVE;
                break;
            case 2:
                newStatus = EmployeeStatus.ON_LEAVE;
                break;
            case 3:
                newStatus = EmployeeStatus.SUSPENDED;
                break;
            case 4:
                newStatus = EmployeeStatus.RESIGNED;
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                return;
        }

        if (employeeService.changeEmployeeStatus(employeeId, newStatus)) {
            System.out.println("✓ Thay đổi trạng thái thành công.");
        } else {
            System.out.println("✗ Thay đổi trạng thái thất bại.");
        }
        pauseForContinue();
    }
}
