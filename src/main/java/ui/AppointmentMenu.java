package ui;

import enums.AppointmentStatus;
import models.Appointment;
import services.AppointmentService;

import java.time.LocalDateTime;

public class AppointmentMenu {
    private final AppointmentService appointmentService;
    private final InputHandler inputHandler;
    private final OutputFormatter outputFormatter;

    public AppointmentMenu(AppointmentService appointmentService,
                           InputHandler inputHandler,
                           OutputFormatter outputFormatter) {
        this.appointmentService = appointmentService;
        this.inputHandler = inputHandler;
        this.outputFormatter = outputFormatter;
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("=== APPOINTMENT MENU ===");
            System.out.println("1. Tạo lịch hẹn");
            System.out.println("2. Gán kỹ thuật viên");
            System.out.println("3. Hủy lịch hẹn");
            System.out.println("4. Danh sách lịch hẹn");
            System.out.println("0. Quay lại");
            int choice = inputHandler.readInt("Chọn chức năng", 0, 4);
            switch (choice) {
                case 1:
                    createAppointment();
                    break;
                case 2:
                    assignTechnician();
                    break;
                case 3:
                    cancelAppointment();
                    break;
                case 4:
                    listAppointments();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    outputFormatter.printStatus("Lựa chọn không hợp lệ", false);
            }
        }
    }

    private void createAppointment() {
        String customerId = inputHandler.readString("Mã khách hàng");
        String serviceId = inputHandler.readString("Mã dịch vụ");
        String technicianId = inputHandler.readString("Mã kỹ thuật viên");
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Appointment appointment = new Appointment(customerId, serviceId, technicianId, start, end);
        appointmentService.schedule(appointment);
        outputFormatter.printStatus("Đã tạo lịch hẹn", true);
    }

    private void assignTechnician() {
        String appointmentId = inputHandler.readString("Mã lịch hẹn");
        String technicianId = inputHandler.readString("Mã kỹ thuật viên");
        boolean success = appointmentService.assignTechnician(appointmentId, technicianId);
        outputFormatter.printStatus(success ? "Đã gán kỹ thuật viên" : "Không gán được", success);
    }

    private void cancelAppointment() {
        String appointmentId = inputHandler.readString("Mã lịch hẹn");
        String reason = inputHandler.readString("Lý do");
        appointmentService.cancel(appointmentId, reason);
        outputFormatter.printStatus("Đã hủy lịch hẹn", true);
    }

    private void listAppointments() {
        Appointment[] appointments = appointmentService.listAppointments();
        if (appointments.length == 0) {
            outputFormatter.printStatus("Chưa có lịch hẹn", false);
            return;
        }
        String[][] rows = new String[appointments.length][5];
        for (int i = 0; i < appointments.length; i++) {
            Appointment appointment = appointments[i];
            rows[i][0] = appointment.getId();
            rows[i][1] = appointment.getCustomerId();
            rows[i][2] = appointment.getTechnicianId();
            rows[i][3] = appointment.getStartTime() == null ? "" : appointment.getStartTime().toString();
            rows[i][4] = appointment.getStatus().name();
        }
        outputFormatter.printTable(new String[]{"ID", "Khách", "KTV", "Bắt đầu", "Trạng thái"}, rows);
    }
}
