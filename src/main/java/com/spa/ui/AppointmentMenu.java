package com.spa.ui;

import com.spa.model.Appointment;
import com.spa.model.Customer;
import com.spa.model.Service;
import com.spa.model.Technician;
import com.spa.model.enums.AppointmentStatus;
import com.spa.service.Validation;
import java.time.LocalDateTime;

import static com.spa.ui.MenuConstants.DATE_TIME_FORMAT;

/**
 * Menu quản lý lịch hẹn và luồng tạo lịch hẹn.
 */
public class AppointmentMenu implements MenuModule {
    private static final int MAX_RATING = 5;
    private final MenuContext context;

    public AppointmentMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ LỊCH HẸN ---");
            System.out.println("1. Xem danh sách lịch hẹn");
            System.out.println("2. Tạo lịch hẹn");
            System.out.println("3. Cập nhật trạng thái lịch hẹn");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 3);
            switch (choice) {
                case 1:
                    listAppointments();
                    Validation.pause();
                    break;
                case 2:
                    createAppointment();
                    Validation.pause();
                    break;
                case 3:
                    updateAppointmentStatus();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listAppointments() {
        System.out.println();
        System.out.println("--- DANH SÁCH LỊCH HẸN ---");
        Appointment[] appointments = context.getAppointmentStore().getAll();
        boolean hasData = false;
        for (Appointment appointment : appointments) {
            if (appointment == null) {
                continue;
            }
            hasData = true;
            System.out.printf("%s | KH: %s | KTV: %s | DV: %s | %s -> %s | %s%n",
                    appointment.getId(),
                    appointment.getCustomer() == null ? "" : appointment.getCustomer().getId(),
                    appointment.getTechnician() == null ? "" : appointment.getTechnician().getId(),
                    appointment.getService() == null ? "" : appointment.getService().getId(),
                    formatDateTime(appointment.getStartTime()),
                    formatDateTime(appointment.getEndTime()),
                    appointment.getStatus());
        }
        if (!hasData) {
            System.out.println("Chưa có lịch hẹn nào.");
        }
    }

    private void createAppointment() {
        System.out.println();
        System.out.println("--- TẠO LỊCH HẸN ---");
        String id = Validation.getString("Mã lịch hẹn: ");
        if (context.getAppointmentStore().findById(id) != null) {
            System.out.println("Mã lịch hẹn đã tồn tại.");
            return;
        }
        Customer customer = selectCustomer();
        if (customer == null) {
            System.out.println("Không có khách hàng hợp lệ.");
            return;
        }
        Technician technician = selectTechnician();
        if (technician == null) {
            System.out.println("Không có kỹ thuật viên hợp lệ.");
            return;
        }
        Service service = selectService();
        if (service == null) {
            System.out.println("Không có dịch vụ hợp lệ.");
            return;
        }
        LocalDateTime start = Validation.getDateTime("Thời gian bắt đầu (yyyy-MM-dd HH:mm): ", DATE_TIME_FORMAT);
        int totalMinutes = service.getDurationMinutes() + service.getBufferTime();
        LocalDateTime end = start.plusMinutes(totalMinutes);
        if (!isTechnicianAvailable(technician, start, end)) {
            System.out.println("Kỹ thuật viên đã có lịch trong khung giờ này.");
            return;
        }
        String notes = Validation.getString("Ghi chú: ");

        Appointment appointment = new Appointment(id, customer, technician, service, start, end,
                notes, AppointmentStatus.SCHEDULED, 0, "");
        context.getAppointmentStore().add(appointment);
        context.getAppointmentStore().writeFile();
        System.out.println("Đã tạo lịch hẹn thành công.");
    }

    private void updateAppointmentStatus() {
        System.out.println();
        System.out.println("--- CẬP NHẬT TRẠNG THÁI LỊCH HẸN ---");
        String id = Validation.getString("Mã lịch hẹn: ");
        Appointment appointment = context.getAppointmentStore().findById(id);
        if (appointment == null) {
            System.out.println("Không tìm thấy lịch hẹn.");
            return;
        }
        AppointmentStatus[] statuses = AppointmentStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i]);
        }
        int selected = Validation.getInt("Chọn trạng thái: ", 1, statuses.length);
        AppointmentStatus status = statuses[selected - 1];
        switch (status) {
            case SCHEDULED:
                appointment.setStatus(AppointmentStatus.SCHEDULED);
                break;
            case SPENDING:
                appointment.start();
                break;
            case COMPLETED:
                int rating = Validation.getInt("Đánh giá (0-" + MAX_RATING + "): ", 0, MAX_RATING);
                String feedback = Validation.getString("Góp ý: ");
                appointment.complete(rating, feedback);
                break;
            case CANCELLED:
                String reason = Validation.getString("Lý do hủy: ");
                appointment.cancel(reason);
                break;
            default:
                appointment.setStatus(status);
                break;
        }
        context.getAppointmentStore().writeFile();
        System.out.println("Đã cập nhật trạng thái lịch hẹn.");
    }

    private Customer selectCustomer() {
        Customer[] customers = context.getCustomerStore().getAll();
        int count = countActiveCustomers(customers);
        if (count == 0) {
            return null;
        }
        Customer[] active = new Customer[count];
        int index = 0;
        for (Customer customer : customers) {
            if (customer != null && !customer.isDeleted()) {
                active[index] = customer;
                index++;
            }
        }
        System.out.println("Chọn khách hàng:");
        for (int i = 0; i < active.length; i++) {
            System.out.printf("%d. %s - %s%n", i + 1, active[i].getId(), active[i].getFullName());
        }
        int choice = Validation.getInt("Lựa chọn: ", 1, active.length);
        return context.getCustomerStore().findById(active[choice - 1].getId());
    }

    private int countActiveCustomers(Customer[] customers) {
        int count = 0;
        for (Customer customer : customers) {
            if (customer != null && !customer.isDeleted()) {
                count++;
            }
        }
        return count;
    }

    private Technician selectTechnician() {
        Technician[] technicians = context.getEmployeeStore().getAllTechnicians();
        if (technicians.length == 0) {
            return null;
        }
        System.out.println("Chọn kỹ thuật viên:");
        for (int i = 0; i < technicians.length; i++) {
            Technician technician = technicians[i];
            System.out.printf("%d. %s - %s%n", i + 1, technician.getId(), technician.getFullName());
        }
        int choice = Validation.getInt("Lựa chọn: ", 1, technicians.length);
        return context.getEmployeeStore().findTechnicianById(technicians[choice - 1].getId());
    }

    private Service selectService() {
        Service[] services = context.getServiceStore().getAll();
        int count = 0;
        for (Service service : services) {
            if (service != null && !service.isDeleted() && service.isActive()) {
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        Service[] active = new Service[count];
        int index = 0;
        for (Service service : services) {
            if (service != null && !service.isDeleted() && service.isActive()) {
                active[index] = service;
                index++;
            }
        }
        System.out.println("Chọn dịch vụ:");
        for (int i = 0; i < active.length; i++) {
            System.out.printf("%d. %s - %s%n", i + 1, active[i].getId(), active[i].getServiceName());
        }
        int choice = Validation.getInt("Lựa chọn: ", 1, active.length);
        return context.getServiceStore().findById(active[choice - 1].getId());
    }

    private boolean isTechnicianAvailable(Technician technician, LocalDateTime start, LocalDateTime end) {
        Appointment[] appointments = context.getAppointmentStore().getAll();
        for (Appointment existing : appointments) {
            if (existing == null || existing.getTechnician() == null) {
                continue;
            }
            if (!technician.getId().equals(existing.getTechnician().getId())) {
                continue;
            }
            if (existing.getStatus() == AppointmentStatus.CANCELLED) {
                continue;
            }
            LocalDateTime existingStart = existing.getStartTime();
            LocalDateTime existingEnd = existing.getEndTime();
            if (existingStart == null || existingEnd == null) {
                continue;
            }
            boolean overlap = start.isBefore(existingEnd) && end.isAfter(existingStart);
            if (overlap) {
                return false;
            }
        }
        return true;
    }

    private String formatDateTime(LocalDateTime time) {
        if (time == null) {
            return "";
        }
        return time.format(DATE_TIME_FORMAT);
    }
}
