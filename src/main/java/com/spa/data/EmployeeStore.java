package com.spa.data;

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

    @Override
    protected String convertToLine(Employee item) {
        if (item == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(item.getId()).append(SEPARATOR)
                .append(item.getFullName()).append(SEPARATOR)
                .append(item.getRole()).append(SEPARATOR)
                .append(item.isDeleted()).append(SEPARATOR)
                .append(item.getPasswordHash()).append(SEPARATOR)
                .append(item.getPhoneNumber()).append(SEPARATOR)
                .append(item.isMale()).append(SEPARATOR)
                .append(item.getBirthDate() == null ? "" : item.getBirthDate().format(DATE_FORMAT)).append(SEPARATOR)
                .append(item.getEmail()).append(SEPARATOR)
                .append(item.getAddress()).append(SEPARATOR)
                .append(item.getHireDate() == null ? "" : item.getHireDate().format(DATE_FORMAT)).append(SEPARATOR)
                .append(item.getSalary());
        if (item instanceof Technician) {
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
