package services;

import collections.AppointmentManager;
import models.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TechnicianAvailabilityService {
    private final AppointmentManager appointmentManager;

    public TechnicianAvailabilityService(AppointmentManager appointmentManager) {
        this.appointmentManager = appointmentManager;
    }

    public boolean isAvailable(String technicianId, LocalDateTime start, int durationMinutes) {
        if (technicianId == null || technicianId.isEmpty() || start == null || durationMinutes <= 0) {
            return false;
        }
        LocalDateTime end = start.plusMinutes(durationMinutes);
        Appointment[] appointments = appointmentManager.findByTechnician(technicianId);
        for (Appointment appointment : appointments) {
            if (appointment == null || appointment.getStartTime() == null || appointment.getEndTime() == null) {
                continue;
            }
            boolean overlaps = start.isBefore(appointment.getEndTime()) && end.isAfter(appointment.getStartTime());
            if (overlaps) {
                return false;
            }
        }
        return true;
    }

    public LocalDateTime nextAvailableSlot(String technicianId, LocalDateTime from) {
        if (technicianId == null || technicianId.isEmpty() || from == null) {
            return null;
        }
        LocalDateTime candidate = from;
        for (int day = 0; day < 14; day++) {
            LocalDate date = candidate.toLocalDate();
            Appointment[] dayAppointments = appointmentManager.findByTechnician(technicianId);
            boolean free = true;
            for (Appointment appointment : dayAppointments) {
                if (appointment == null || appointment.getStartTime() == null || appointment.getEndTime() == null) {
                    continue;
                }
                if (date.equals(appointment.getStartTime().toLocalDate())
                        && candidate.isBefore(appointment.getEndTime())
                        && candidate.plusHours(1).isAfter(appointment.getStartTime())) {
                    free = false;
                    candidate = appointment.getEndTime();
                    break;
                }
            }
            if (free) {
                return candidate;
            }
            candidate = candidate.plusHours(1);
        }
        return null;
    }

    public int countDailyAppointments(String technicianId, LocalDate date) {
        if (technicianId == null || technicianId.isEmpty() || date == null) {
            return 0;
        }
        Appointment[] appointments = appointmentManager.findByTechnician(technicianId);
        int count = 0;
        for (Appointment appointment : appointments) {
            if (appointment != null && appointment.getStartTime() != null
                    && date.equals(appointment.getStartTime().toLocalDate())) {
                count++;
            }
        }
        return count;
    }
}
