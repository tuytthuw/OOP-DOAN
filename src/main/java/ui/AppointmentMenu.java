package ui;

import java.time.LocalDateTime;

import collections.AppointmentManager;
import enums.AppointmentStatus;
import exceptions.BusinessLogicException;
import exceptions.EntityNotFoundException;
import io.InputHandler;
import io.OutputFormatter;
import models.Appointment;
import services.AppointmentService;

/**
 * Menu quản lý lịch hẹn.
 * Cung cấp các chức năng: đặt, xem, bắt đầu, hoàn thành, hủy lịch hẹn.
 */
public class AppointmentMenu extends MenuBase {

    // ============ THUỘC TÍNH (ATTRIBUTES) ============

    private AppointmentService appointmentService;
    private AppointmentManager appointmentManager;

    // ============ CONSTRUCTOR ============

    /**
     * Constructor khởi tạo AppointmentMenu.
     *
     * @param inputHandler       Bộ xử lý input
     * @param outputFormatter    Bộ định dạng output
     * @param appointmentService Dịch vụ quản lý lịch hẹn
     * @param appointmentManager Manager lịch hẹn
     */
    public AppointmentMenu(InputHandler inputHandler, OutputFormatter outputFormatter,
            AppointmentService appointmentService, AppointmentManager appointmentManager) {
        super(inputHandler, outputFormatter);
        this.appointmentService = appointmentService;
        this.appointmentManager = appointmentManager;
    }

    // ============ PHƯƠNG THỨC OVERRIDE (OVERRIDE METHODS) ============

    @Override
    protected void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   QUẢN LÝ LỊCH HẸN                     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Đặt lịch hẹn mới");
        System.out.println("2. Xem lịch hẹn sắp tới");
        System.out.println("3. Xem lịch hẹn của khách hàng");
        System.out.println("4. Bắt đầu dịch vụ");
        System.out.println("5. Hoàn thành dịch vụ");
        System.out.println("6. Hủy lịch hẹn");
        System.out.println("7. Cập nhật lịch hẹn");
        System.out.println("8. Quay lại");
        System.out.println();
    }

    @Override
    protected boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                bookAppointment();
                return true;

            case 2:
                viewUpcomingAppointments();
                return true;

            case 3:
                viewCustomerAppointments();
                return true;

            case 4:
                startAppointment();
                return true;

            case 5:
                completeAppointment();
                return true;

            case 6:
                cancelAppointment();
                return true;

            case 7:
                rescheduleAppointment();
                return true;

            case 8:
                // Quay lại menu chính
                return false;

            default:
                System.out.println("❌ Lựa chọn không hợp lệ! Vui lòng chọn lại.");
                return true;
        }
    }

    // ============ PHƯƠNG THỨC CHỨC NĂNG (FUNCTIONAL METHODS) ============

    /**
     * Đặt lịch hẹn mới.
     */
    private void bookAppointment() {
        System.out.println("\n--- Đặt Lịch Hẹn Mới ---");

        try {
            String customerId = inputHandler.readString("Nhập ID khách hàng: ");
            String dateTimeStr = inputHandler.readString("Nhập ngày giờ hẹn (YYYY-MM-DD HH:mm): ");
            String serviceId = inputHandler.readString("Nhập ID dịch vụ: ");

            // Convert string to LocalDateTime
            LocalDateTime appointmentDateTime = parseDateTime(dateTimeStr);

            Appointment appointment = appointmentService.bookAppointment(customerId, serviceId, appointmentDateTime);

            System.out.println("\n✓ Lịch hẹn đã được tạo thành công!");
            System.out.println("  ID: " + appointment.getAppointmentId());
            System.out.println("  Ngày giờ: " + appointment.getAppointmentDateTime());
            System.out.println("  Trạng thái: " + appointment.getStatus());

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy: " + e.getFormattedError());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getFormattedError());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xem lịch hẹn sắp tới.
     */
    private void viewUpcomingAppointments() {
        System.out.println("\n--- Lịch Hẹn Sắp Tới ---");

        try {
            Appointment[] appointments = appointmentManager.getAll();

            if (appointments == null || appointments.length == 0) {
                System.out.println("ℹ Không có lịch hẹn nào.");
            } else {
                // Lọc lịch hẹn còn hiệu lực (trạng thái không phải Cancel/Complete)
                Appointment[] activeAppointments = filterActiveAppointments(appointments);

                if (activeAppointments.length == 0) {
                    System.out.println("ℹ Không có lịch hẹn nào còn hiệu lực.");
                } else {
                    System.out.println();
                    for (Appointment apt : activeAppointments) {
                        if (apt != null) {
                            System.out.println(apt.getAppointmentInfo());
                        }
                    }
                }
            }

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Xem lịch hẹn của khách hàng.
     */
    private void viewCustomerAppointments() {
        System.out.println("\n--- Lịch Hẹn Của Khách Hàng ---");

        try {
            String customerId = inputHandler.readString("Nhập ID khách hàng: ");

            Appointment[] appointments = appointmentManager.getAll();
            Appointment[] customerAppointments = filterAppointmentsByCustomer(appointments, customerId);

            if (customerAppointments.length == 0) {
                System.out.println("ℹ Khách hàng này chưa có lịch hẹn nào.");
            } else {
                System.out.println();
                for (Appointment apt : customerAppointments) {
                    if (apt != null) {
                        System.out.println(apt.getAppointmentInfo());
                    }
                }
            }

            pauseForContinue();

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Bắt đầu dịch vụ.
     */
    private void startAppointment() {
        System.out.println("\n--- Bắt Đầu Dịch Vụ ---");

        try {
            String appointmentId = inputHandler.readString("Nhập ID lịch hẹn: ");

            appointmentService.startAppointment(appointmentId);

            System.out.println("✓ Dịch vụ đã bắt đầu!");

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy lịch hẹn: " + e.getMessage());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Hoàn thành dịch vụ.
     */
    private void completeAppointment() {
        System.out.println("\n--- Hoàn Thành Dịch Vụ ---");

        try {
            String appointmentId = inputHandler.readString("Nhập ID lịch hẹn: ");

            appointmentService.completeAppointment(appointmentId);

            System.out.println("✓ Dịch vụ đã hoàn thành!");

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy lịch hẹn: " + e.getMessage());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Hủy lịch hẹn.
     */
    private void cancelAppointment() {
        System.out.println("\n--- Hủy Lịch Hẹn ---");

        try {
            String appointmentId = inputHandler.readString("Nhập ID lịch hẹn cần hủy: ");

            appointmentService.cancelAppointment(appointmentId);

            System.out.println("✓ Lịch hẹn đã được hủy!");

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy lịch hẹn: " + e.getFormattedError());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getFormattedError());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    /**
     * Cập nhật lịch hẹn (Reschedule).
     */
    private void rescheduleAppointment() {
        System.out.println("\n--- Cập Nhật Lịch Hẹn ---");

        try {
            String appointmentId = inputHandler.readString("Nhập ID lịch hẹn: ");
            String newDateStr = inputHandler.readString("Nhập ngày giờ mới (YYYY-MM-DD HH:mm): ");

            LocalDateTime newDateTime = parseDateTime(newDateStr);

            appointmentService.rescheduleAppointment(appointmentId, newDateTime);

            System.out.println("✓ Lịch hẹn đã được cập nhật!");

            pauseForContinue();

        } catch (EntityNotFoundException e) {
            System.out.println("❌ Không tìm thấy lịch hẹn: " + e.getFormattedError());
            pauseForContinue();
        } catch (BusinessLogicException e) {
            System.out.println("❌ Lỗi: " + e.getFormattedError());
            pauseForContinue();
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định: " + e.getMessage());
            pauseForContinue();
        }
    }

    // ============ PHƯƠNG THỨC HỖ TRỢ (HELPER METHODS) ============

    /**
     * Lọc lịch hẹn còn hiệu lực (không bị hủy hay hoàn thành).
     *
     * @param appointments Mảng lịch hẹn
     * @return Mảng lịch hẹn còn hiệu lực
     */
    private Appointment[] filterActiveAppointments(Appointment[] appointments) {
        if (appointments == null) {
            return new Appointment[0];
        }

        // Đếm lịch hẹn còn hiệu lực
        int count = 0;
        for (Appointment appointment : appointments) {
            if (appointment != null &&
                    appointment.getStatus() != AppointmentStatus.CANCELLED &&
                    appointment.getStatus() != AppointmentStatus.COMPLETED) {
                count++;
            }
        }

        // Tạo mảng kết quả
        Appointment[] result = new Appointment[count];
        int index = 0;
        for (Appointment appointment : appointments) {
            if (appointment != null &&
                    appointment.getStatus() != AppointmentStatus.CANCELLED &&
                    appointment.getStatus() != AppointmentStatus.COMPLETED) {
                result[index++] = appointment;
            }
        }

        return result;
    }

    /**
     * Lọc lịch hẹn theo khách hàng.
     *
     * @param appointments Mảng lịch hẹn
     * @param customerId   ID khách hàng
     * @return Mảng lịch hẹn của khách hàng
     */
    private Appointment[] filterAppointmentsByCustomer(Appointment[] appointments, String customerId) {
        if (appointments == null) {
            return new Appointment[0];
        }

        // Đếm lịch hẹn của khách hàng
        int count = 0;
        for (Appointment appointment : appointments) {
            if (appointment != null && appointment.getCustomerId().equals(customerId)) {
                count++;
            }
        }

        // Tạo mảng kết quả
        Appointment[] result = new Appointment[count];
        int index = 0;
        for (Appointment appointment : appointments) {
            if (appointment != null && appointment.getCustomerId().equals(customerId)) {
                result[index++] = appointment;
            }
        }

        return result;
    }

    /**
     * Parse chuỗi ngày giờ thành LocalDateTime.
     * Format: YYYY-MM-DD HH:mm
     *
     * @param dateTimeStr Chuỗi ngày giờ
     * @return LocalDateTime
     * @throws Exception nếu format không hợp lệ
     */
    private LocalDateTime parseDateTime(String dateTimeStr) throws Exception {
        try {
            return LocalDateTime.parse(dateTimeStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            throw new Exception("Format ngày giờ không hợp lệ! Sử dụng: YYYY-MM-DD HH:mm");
        }
    }
}
