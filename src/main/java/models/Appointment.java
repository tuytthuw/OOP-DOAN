package models;

import enums.AppointmentStatus;
import interfaces.IEntity;

import java.time.LocalDateTime;

public class Appointment implements IEntity {
    private static final String ID_PREFIX = "APP";
    private static int runningNumber = 1;

    private final String appointmentId;
    private String customerId;
    private String serviceId;
    private String technicianId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String notes;
    private final LocalDateTime createdAt;

    public Appointment(String customerId,
                       String serviceId,
                       String technicianId,
                       LocalDateTime startTime,
                       LocalDateTime endTime) {
        this.appointmentId = generateId();
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.technicianId = technicianId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = AppointmentStatus.SCHEDULED;
        this.createdAt = LocalDateTime.now();
    }

    private String generateId() {
        String id = ID_PREFIX + String.format("%05d", runningNumber);
        runningNumber++;
        return id;
    }

    @Override
    public String getId() {
        return appointmentId;
    }

    public void reschedule(LocalDateTime newStart, LocalDateTime newEnd) {
        if (newStart == null || newEnd == null || !newEnd.isAfter(newStart)) {
            return;
        }
        this.startTime = newStart;
        this.endTime = newEnd;
    }

    public void assignTechnician(String technicianId) {
        this.technicianId = technicianId;
    }

    public void updateStatus(AppointmentStatus status) {
        if (status != null) {
            this.status = status;
        }
    }

    public void cancel(String reason) {
        this.status = AppointmentStatus.CANCELLED;
        this.notes = reason;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
