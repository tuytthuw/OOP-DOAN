package collections;

import models.AuthSession;

import java.time.LocalDateTime;

public class AuthSessionManager extends BaseManager<AuthSession> {
    private static final int DEFAULT_CAPACITY = 50;

    public AuthSessionManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected AuthSession[] createArray(int size) {
        return new AuthSession[size];
    }

    public AuthSession findActiveSession(String employeeId) {
        if (employeeId == null || employeeId.isEmpty()) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            AuthSession session = items[i];
            if (session != null && employeeId.equals(session.getEmployeeId())
                    && !session.isExpired(LocalDateTime.now())) {
                return session;
            }
        }
        return null;
    }

    public AuthSession[] findExpired(LocalDateTime now) {
        if (now == null) {
            return new AuthSession[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            AuthSession session = items[i];
            if (session != null && session.isExpired(now)) {
                matches++;
            }
        }
        AuthSession[] result = new AuthSession[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            AuthSession session = items[i];
            if (session != null && session.isExpired(now)) {
                result[index++] = session;
            }
        }
        return result;
    }

    public void terminateAll() {
        for (int i = 0; i < count; i++) {
            items[i] = null;
        }
        count = 0;
    }
}
