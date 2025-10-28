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
            System.out.println("0. Quay lại");
            int choice = inputHandler.readInt("Chọn chức năng", 0, 3);
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
}
