package models;

import enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Lớp đại diện cho một lịch hẹn trong hệ thống Spa.
 */
public class Appointment {
    private String appointmentId;          // Mã lịch hẹn (ví dụ: APT_00001)
    private String customerId;             // Mã khách hàng
    private String serviceId;              // Mã dịch vụ
    private LocalDateTime appointmentDateTime; // Ngày giờ hẹn
    private AppointmentStatus status;      // Trạng thái lịch hẹn
    private String staffId;                // Nhân viên thực hiện (có thể null)
    private String notes;                  // Ghi chú
    private LocalDateTime createdDate;     // Ngày tạo
    private LocalDateTime completedDate;   // Ngày hoàn thành (có thể null)

    // Constructor cơ bản
    public Appointment(String appointmentId, String customerId, String serviceId, LocalDateTime appointmentDateTime) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.SCHEDULED;
        this.createdDate = LocalDateTime.now();
    }

    // Gán nhân viên
    public void assignStaff(String staffId) {
        this.staffId = staffId;
    }

    // Cập nhật trạng thái
    public void updateStatus(AppointmentStatus newStatus) {
        if (status == AppointmentStatus.SCHEDULED && (newStatus == AppointmentStatus.SPENDING || newStatus == AppointmentStatus.CANCELLED)) {
            this.status = newStatus;
        } else if (status == AppointmentStatus.SPENDING && (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED)) {
            this.status = newStatus;
            if (newStatus == AppointmentStatus.COMPLETED) {
                this.completedDate = LocalDateTime.now();
            }
        } else {
            System.out.println(" Không thể chuyển trạng thái từ " + status + " sang " + newStatus);
        }
    }

    // Đánh dấu hoàn thành
    public void markAsCompleted() {
        if (status == AppointmentStatus.SPENDING) {
            status = AppointmentStatus.COMPLETED;
            completedDate = LocalDateTime.now();
        } else {
            System.out.println(" Không thể đánh dấu hoàn thành vì chưa ở trạng thái SPENDING!");
        }
    }

    // Đánh dấu hủy
    public void markAsCancelled() {
        if (status == AppointmentStatus.SCHEDULED || status == AppointmentStatus.SPENDING) {
            status = AppointmentStatus.CANCELLED;
        } else {
            System.out.println(" Không thể hủy lịch hẹn này!");
        }
    }

    // Kiểm tra hết hạn
    public boolean isExpired() {
        return status == AppointmentStatus.SCHEDULED && appointmentDateTime.isBefore(LocalDateTime.now());
    }

    // Trả về thông tin
    public String getAppointmentInfo() {
        return String.format("Mã: %s | Khách: %s | Dịch vụ: %s | Thời gian: %s | Trạng thái: %s",
                appointmentId, customerId, serviceId, appointmentDateTime, status);
    }

    // Getter & Setter
    public String getAppointmentId() { return appointmentId; }
    public String getCustomerId() { return customerId; }
    public String getServiceId() { return serviceId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public AppointmentStatus getStatus() { return status; }
    public String getStaffId() { return staffId; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getCompletedDate() { return completedDate; }

    public void setNotes(String notes) { this.notes = notes; }

    // equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(appointmentId, that.appointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }
}
