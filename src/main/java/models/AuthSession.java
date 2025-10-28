package models;

import enums.EmployeeRole;
import interfaces.IEntity;

import java.time.LocalDateTime;

public class AuthSession implements IEntity {
    private static final String ID_PREFIX = "AUTH";
    private static int runningNumber = 1;

    private final String sessionId;
    private String employeeId;
    private LocalDateTime loginAt;
    private LocalDateTime expiresAt;
    private EmployeeRole role;

    public AuthSession(String employeeId,
                       LocalDateTime loginAt,
                       LocalDateTime expiresAt,
                       EmployeeRole role) {
        this.sessionId = generateId();
        this.employeeId = employeeId;
        this.loginAt = loginAt;
        this.expiresAt = expiresAt;
        this.role = role;
    }

    private String generateId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    public boolean isExpired(LocalDateTime now) {
        if (expiresAt == null || now == null) {
            return true;
        }
        return now.isAfter(expiresAt);
    }

    public void refresh(LocalDateTime newExpiry) {
        if (newExpiry != null && (expiresAt == null || newExpiry.isAfter(expiresAt))) {
            this.expiresAt = newExpiry;
        }
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDateTime getLoginAt() {
        return loginAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public EmployeeRole getRole() {
        return role;
    }
}
