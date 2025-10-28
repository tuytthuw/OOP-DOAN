package collections;

import enums.AppointmentStatus;
import models.Appointment;

import java.time.LocalDate;

public class AppointmentManager extends BaseManager<Appointment> {
    private static final int DEFAULT_CAPACITY = 80;

    public AppointmentManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected Appointment[] createArray(int size) {
        return new Appointment[size];
    }

    public Appointment[] findByDate(LocalDate date) {
        if (date == null) {
            return new Appointment[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Appointment appointment = items[i];
            if (appointment != null && appointment.getStartTime() != null
                    && date.equals(appointment.getStartTime().toLocalDate())) {
                matches++;
            }
        }
        Appointment[] result = new Appointment[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Appointment appointment = items[i];
            if (appointment != null && appointment.getStartTime() != null
                    && date.equals(appointment.getStartTime().toLocalDate())) {
                result[index++] = appointment;
            }
        }
        return result;
    }

    public Appointment[] findByTechnician(String technicianId) {
        if (technicianId == null || technicianId.isEmpty()) {
            return new Appointment[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Appointment appointment = items[i];
            if (appointment != null && technicianId.equals(appointment.getTechnicianId())) {
                matches++;
            }
        }
        Appointment[] result = new Appointment[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Appointment appointment = items[i];
            if (appointment != null && technicianId.equals(appointment.getTechnicianId())) {
                result[index++] = appointment;
            }
        }
        return result;
    }

    public Appointment[] findByStatus(AppointmentStatus status) {
        if (status == null) {
            return new Appointment[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Appointment appointment = items[i];
            if (appointment != null && status == appointment.getStatus()) {
                matches++;
            }
        }
        Appointment[] result = new Appointment[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Appointment appointment = items[i];
            if (appointment != null && status == appointment.getStatus()) {
                result[index++] = appointment;
            }
        }
        return result;
    }
}
