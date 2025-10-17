package models;

import java.time.LocalDateTime;
import java.util.Objects;

import Interfaces.IEntity;
import enums.AppointmentStatus;

/**
 * Lớp đại diện cho một lịch hẹn trong hệ thống Spa.
 * Implement IEntity để quản lý thông tin entity chung.
 */
public class Appointment implements IEntity {
    private String appointmentId; // Mã lịch hẹn (ví dụ: APT_01)
    private String customerId; // Mã khách hàng
    private String serviceId; // Mã dịch vụ
    private LocalDateTime appointmentDateTime; // Ngày giờ hẹn
    private AppointmentStatus status; // Trạng thái lịch hẹn
    private String staffId; // Nhân viên thực hiện (có thể null)
    private String notes; // Ghi chú
    private LocalDateTime createdDate; // Ngày tạo
    private LocalDateTime completedDate; // Ngày hoàn thành (có thể null)

    // Constructor cơ bản
    public Appointment(String appointmentId, String customerId, String serviceId, LocalDateTime appointmentDateTime) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.SCHEDULED;
        this.createdDate = LocalDateTime.now();
    }

    // Gán nhân viên thực hiện
    public void assignStaff(String staffId) {
        this.staffId = staffId;
    }

    // Cập nhật trạng thái lịch hẹn
    public void updateStatus(AppointmentStatus newStatus) {
        if (status == AppointmentStatus.SCHEDULED &&
                (newStatus == AppointmentStatus.SPENDING || newStatus == AppointmentStatus.CANCELLED)) {

            this.status = newStatus;
        } else if (status == AppointmentStatus.SPENDING &&
                (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED)) {

            this.status = newStatus;
            if (newStatus == AppointmentStatus.COMPLETED) {
                this.completedDate = LocalDateTime.now();
            }
        } else {
            System.out.printf("⚠️ Không thể chuyển trạng thái từ %s sang %s%n", status, newStatus);
        }
    }

    // Đánh dấu hoàn thành
    public void markAsCompleted() {
        if (status == AppointmentStatus.SPENDING) {
            status = AppointmentStatus.COMPLETED;
            completedDate = LocalDateTime.now();
        } else {
            System.out.println("⚠️ Không thể đánh dấu hoàn thành vì chưa ở trạng thái SPENDING!");
        }
    }

    // Đánh dấu hủy
    public void markAsCancelled() {
        if (status == AppointmentStatus.SCHEDULED || status == AppointmentStatus.SPENDING) {
            status = AppointmentStatus.CANCELLED;
        } else {
            System.out.println("⚠️ Không thể hủy lịch hẹn này!");
        }
    }

    // Kiểm tra xem lịch hẹn có bị quá hạn hay không
    public boolean isExpired() {
        return status == AppointmentStatus.SCHEDULED && appointmentDateTime.isBefore(LocalDateTime.now());
    }

    // Trả về thông tin cơ bản của lịch hẹn
    public String getAppointmentInfo() {
        return String.format("Mã: %s | Khách: %s | Dịch vụ: %s | Thời gian: %s | Trạng thái: %s",
                appointmentId, customerId, serviceId, appointmentDateTime, status);
    }

    // ============ IEntity Implementation ============

    @Override
    public String getId() {
        return appointmentId;
    }

    @Override
    public void display() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ ID: " + getId());
        System.out.println("│ Khách hàng: " + customerId);
        System.out.println("│ Dịch vụ: " + serviceId);
        System.out.println("│ Thời gian: " + appointmentDateTime);
        System.out.println("│ Trạng thái: " + status);
        System.out.println("│ Nhân viên: " + (staffId == null ? "Chưa gán" : staffId));
        System.out.println("│ Ghi chú: " + (notes == null ? "Không có" : notes));
        System.out.println("│ Ngày tạo: " + createdDate);
        System.out.println("│ Ngày hoàn thành: " + (completedDate == null ? "Chưa hoàn thành" : completedDate));
        System.out.println("└─────────────────────────────────────────────┘");
    }

    @Override
    public void input() {
        // TODO: Implement nhập thông tin lịch hẹn từ người dùng
    }

    @Override
    public String getPrefix() {
        return "APT";
    }

    // Getter & Setter
    public String getAppointmentId() {
        return appointmentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Appointment that))
            return false;
        return Objects.equals(appointmentId, that.appointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }

    @Override
    public String toString() {
        return getAppointmentInfo();
    }
}
