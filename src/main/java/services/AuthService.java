package services;

import collections.AuthSessionManager;
import collections.EmployeeManager;
import enums.EmployeeRole;
import models.AuthSession;
import models.Employee;

import java.time.LocalDateTime;

public class AuthService {
    private static AuthService instance;

    private final EmployeeManager employeeManager;
    private final AuthSessionManager authSessionManager;
    private AuthSession currentSession;

    private AuthService(EmployeeManager employeeManager,
                        AuthSessionManager authSessionManager) {
        this.employeeManager = employeeManager;
        this.authSessionManager = authSessionManager;
    }

    public static AuthService getInstance(EmployeeManager employeeManager,
                                          AuthSessionManager authSessionManager) {
        if (instance == null) {
            instance = new AuthService(employeeManager, authSessionManager);
        }
        return instance;
    }

    public boolean login(String username, String password) {
        Employee employee = employeeManager.findByCode(username);
        if (employee == null || !employee.checkPassword(password)) {
            return false;
        }
        logout();
        currentSession = new AuthSession(employee.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), employee.getRole());
        authSessionManager.add(currentSession);
        return true;
    }

    public void logout() {
        if (currentSession != null) {
            authSessionManager.removeById(currentSession.getId());
            currentSession = null;
        }
    }

    public AuthSession getCurrentSession() {
        return currentSession;
    }

    public boolean hasRole(EmployeeRole role) {
        return currentSession != null && currentSession.getRole() == role;
    }

    public void refreshSession() {
        if (currentSession == null) {
            return;
        }
        LocalDateTime newExpiry = LocalDateTime.now().plusHours(2);
        currentSession.refresh(newExpiry);
        authSessionManager.update(currentSession);
    }
}
