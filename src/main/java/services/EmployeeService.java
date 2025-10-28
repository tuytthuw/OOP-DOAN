package services;

import collections.EmployeeManager;
import enums.EmployeeRole;
import enums.EmployeeStatus;
import models.Employee;

public class EmployeeService {
    private final EmployeeManager employeeManager;

    public EmployeeService(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public void create(Employee employee) {
        employeeManager.add(employee);
    }

    public void update(Employee employee) {
        employeeManager.update(employee);
    }

    public void deactivate(String id) {
        Employee employee = employeeManager.getById(id);
        if (employee == null) {
            return;
        }
        employee.setStatus(EmployeeStatus.RESIGNED);
        employeeManager.update(employee);
    }

    public boolean changePassword(String id, String newRaw) {
        Employee employee = employeeManager.getById(id);
        if (employee == null) {
            return false;
        }
        employee.setPasswordHash(newRaw);
        employeeManager.update(employee);
        return true;
    }

    public Employee authenticate(String username, String password) {
        Employee employee = employeeManager.findByCode(username);
        if (employee != null && employee.checkPassword(password)) {
            return employee;
        }
        return null;
    }

    public Employee[] findByRole(EmployeeRole role) {
        return employeeManager.findByRole(role);
    }
}
