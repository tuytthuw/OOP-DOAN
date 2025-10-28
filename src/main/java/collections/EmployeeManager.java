package collections;

import enums.EmployeeRole;
import enums.EmployeeStatus;
import models.Employee;
import models.Technician;

public class EmployeeManager extends BaseManager<Employee> {
    private static final int DEFAULT_CAPACITY = 30;

    public EmployeeManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected Employee[] createArray(int size) {
        return new Employee[size];
    }

    public Employee[] findByRole(EmployeeRole role) {
        if (role == null) {
            return new Employee[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Employee employee = items[i];
            if (employee != null && role == employee.getRole()) {
                matches++;
            }
        }
        Employee[] result = new Employee[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Employee employee = items[i];
            if (employee != null && role == employee.getRole()) {
                result[index++] = employee;
            }
        }
        return result;
    }

    public Employee findByCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            Employee employee = items[i];
            if (employee != null && code.equals(employee.getEmployeeCode())) {
                return employee;
            }
        }
        return null;
    }

    public Employee[] findAvailableTechnicians() {
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Employee employee = items[i];
            if (employee instanceof Technician && employee.getStatus() == EmployeeStatus.ACTIVE) {
                matches++;
            }
        }
        Employee[] result = new Employee[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Employee employee = items[i];
            if (employee instanceof Technician && employee.getStatus() == EmployeeStatus.ACTIVE) {
                result[index++] = employee;
            }
        }
        return result;
    }
}
