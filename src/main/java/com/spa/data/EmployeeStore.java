package com.spa.data;

import com.spa.model.Admin;
import com.spa.model.Employee;
import com.spa.model.Receptionist;
import com.spa.model.Technician;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Kho dữ liệu chuyên biệt cho nhân viên, xử lý đọc/ghi đầy đủ thông tin.
 */
public class EmployeeStore extends DataStore<Employee> {
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public EmployeeStore(String dataFilePath) {
        super(Employee.class, dataFilePath);
    }

    public Admin[] getAllAdmins() {
        Employee[] employees = getAll();
        int count = 0;
        for (Employee employee : employees) {
            if (employee instanceof Admin && !employee.isDeleted()) {
                count++;
            }
        }
        Admin[] result = new Admin[count];
        int index = 0;
        for (Employee employee : employees) {
            if (employee instanceof Admin && !employee.isDeleted()) {
                result[index] = (Admin) employee;
                index++;
            }
        }
        return result;
    }

    public Admin findAdminById(String id) {
        Employee employee = findById(id);
        if (employee instanceof Admin && !employee.isDeleted()) {
            return (Admin) employee;
        }
        return null;
    }

    public Receptionist[] getAllReceptionists() {
        Employee[] employees = getAll();
        int count = 0;
        for (Employee employee : employees) {
            if (employee instanceof Receptionist && !employee.isDeleted()) {
                count++;
            }
        }
        Receptionist[] result = new Receptionist[count];
        int index = 0;
        for (Employee employee : employees) {
            if (employee instanceof Receptionist && !employee.isDeleted()) {
                result[index] = (Receptionist) employee;
                index++;
            }
        }
        return result;
    }

    public Receptionist findReceptionistById(String id) {
        Employee employee = findById(id);
        if (employee instanceof Receptionist && !employee.isDeleted()) {
            return (Receptionist) employee;
        }
        return null;
    }

    public Technician[] getAllTechnicians() {
        Employee[] employees = getAll();
        int count = 0;
        for (Employee employee : employees) {
            if (employee instanceof Technician && !employee.isDeleted()) {
                count++;
            }
        }
        Technician[] result = new Technician[count];
        int index = 0;
        for (Employee employee : employees) {
            if (employee instanceof Technician && !employee.isDeleted()) {
                result[index] = (Technician) employee;
                index++;
            }
        }
        return result;
    }

    public Technician findTechnicianById(String id) {
        Employee employee = findById(id);
        if (employee instanceof Technician && !employee.isDeleted()) {
            return (Technician) employee;
        }
        return null;
    }

    @Override
    protected String convertToLine(Employee item) {
        if (item == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String roleToken = item instanceof Admin ? "ADMIN" : item.getRole();
        builder.append(item.getId()).append(SEPARATOR)
                .append(item.getFullName()).append(SEPARATOR)
                .append(roleToken).append(SEPARATOR)
                .append(item.isDeleted()).append(SEPARATOR)
                .append(item.getPasswordHash()).append(SEPARATOR)
                .append(item.getPhoneNumber()).append(SEPARATOR)
                .append(item.isMale()).append(SEPARATOR)
                .append(item.getBirthDate() == null ? "" : item.getBirthDate().format(DATE_FORMAT)).append(SEPARATOR)
                .append(item.getEmail()).append(SEPARATOR)
                .append(item.getAddress()).append(SEPARATOR)
                .append(item.getHireDate() == null ? "" : item.getHireDate().format(DATE_FORMAT)).append(SEPARATOR)
                .append(item.getSalary());
        if (item instanceof Admin) {
            Admin admin = (Admin) item;
            builder.append(SEPARATOR).append("ADM")
                    .append(SEPARATOR).append(admin.getPermissionGroup())
                    .append(SEPARATOR).append(admin.isSuperAdmin());
        } else if (item instanceof Technician) {
            Technician technician = (Technician) item;
            builder.append(SEPARATOR).append("TECH")
                    .append(SEPARATOR).append(technician.getSkill())
                    .append(SEPARATOR).append(technician.getCertifications())
                    .append(SEPARATOR).append(technician.getCommissionRate());
        } else if (item instanceof Receptionist) {
            Receptionist receptionist = (Receptionist) item;
            builder.append(SEPARATOR).append("REC")
                    .append(SEPARATOR).append(receptionist.getMonthlyBonus());
        } else {
            builder.append(SEPARATOR).append("EMP");
        }
        return builder.toString();
    }

    @Override
    protected Employee parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|");
        if (parts.length < 12) {
            return null;
        }
        String id = parts[0];
        String fullName = parts[1];
        String roleToken = parts[2];
        boolean deleted = Boolean.parseBoolean(parts[3]);
        String passwordHash = parts[4];
        String phone = parts[5];
        boolean male = Boolean.parseBoolean(parts[6]);
        LocalDate birthDate = parts[7].isEmpty() ? null : LocalDate.parse(parts[7], DATE_FORMAT);
        String email = parts[8];
        String address = parts[9];
        LocalDate hireDate = parts[10].isEmpty() ? null : LocalDate.parse(parts[10], DATE_FORMAT);
        double salary = Double.parseDouble(parts[11]);

        if ("ADM".equalsIgnoreCase(parts[12])) {
            String permissionGroup = parts.length > 13 ? parts[13] : "";
            boolean superAdmin = parts.length > 14 && Boolean.parseBoolean(parts[14]);
            Admin admin = new Admin(id, fullName, phone, male, birthDate, email, address,
                    deleted, salary, passwordHash, hireDate, permissionGroup, superAdmin);
            return admin;
        }
        if ("TECH".equalsIgnoreCase(parts[12])) {
            String skill = parts.length > 13 ? parts[13] : "";
            String certifications = parts.length > 14 ? parts[14] : "";
            double commission = parts.length > 15 ? Double.parseDouble(parts[15]) : 0.0;
            Technician technician = new Technician(id, fullName, phone, male, birthDate, email, address,
                    deleted, salary, passwordHash, hireDate, skill, certifications, commission);
            return technician;
        }
        if ("REC".equalsIgnoreCase(parts[12])) {
            double monthlyBonus = parts.length > 13 ? Double.parseDouble(parts[13]) : 0.0;
            Receptionist receptionist = new Receptionist(id, fullName, phone, male, birthDate, email, address,
                    deleted, salary, passwordHash, hireDate, monthlyBonus);
            return receptionist;
        }
        return null;
    }
}
