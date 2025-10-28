package services;

import collections.AppointmentManager;
import models.Appointment;

import java.time.LocalDateTime;

public class AppointmentService {
    private final AppointmentManager appointmentManager;
    private final TechnicianAvailabilityService technicianAvailabilityService;

    public AppointmentService(AppointmentManager appointmentManager,
                              TechnicianAvailabilityService technicianAvailabilityService) {
        this.appointmentManager = appointmentManager;
        this.technicianAvailabilityService = technicianAvailabilityService;
    }

    public void schedule(Appointment appointment) {
        appointmentManager.add(appointment);
    }

    public boolean reschedule(String id, LocalDateTime start, LocalDateTime end) {
        Appointment appointment = appointmentManager.getById(id);
        if (appointment == null || start == null || end == null || !end.isAfter(start)) {
            return false;
        }
        if (appointment.getTechnicianId() != null
                && !technicianAvailabilityService.isAvailable(appointment.getTechnicianId(), start,
                (int) java.time.Duration.between(appointment.getStartTime(), appointment.getEndTime()).toMinutes())) {
            return false;
        }
        appointment.reschedule(start, end);
        appointmentManager.update(appointment);
        return true;
    }

    public boolean assignTechnician(String id, String technicianId) {
        Appointment appointment = appointmentManager.getById(id);
        if (appointment == null) {
            return false;
        }
        int duration = (int) java.time.Duration.between(appointment.getStartTime(), appointment.getEndTime()).toMinutes();
        if (!technicianAvailabilityService.isAvailable(technicianId, appointment.getStartTime(), duration)) {
            return false;
        }
        appointment.assignTechnician(technicianId);
        appointmentManager.update(appointment);
        return true;
    }

    public void cancel(String id, String reason) {
        Appointment appointment = appointmentManager.getById(id);
        if (appointment == null) {
            return;
        }
        appointment.cancel(reason);
        appointmentManager.update(appointment);
    }

    public void complete(String id) {
        Appointment appointment = appointmentManager.getById(id);
        if (appointment == null) {
            return;
        }
        appointment.updateStatus(enums.AppointmentStatus.COMPLETED);
        appointmentManager.update(appointment);
    }
}
