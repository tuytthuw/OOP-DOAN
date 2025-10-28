package com.spa.model;

import com.spa.interfaces.IEntity;
import com.spa.model.enums.AppointmentStatus;
import java.time.LocalDateTime;

/**
 * Cuộc hẹn giữa khách hàng và kỹ thuật viên.
 */
public class Appointment implements IEntity {
    private static final String PREFIX = "APP";

    private String appointmentId;
    private Customer customer;
    private Technician technician;
    private Service service;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
    private AppointmentStatus status;
    private int rating;
    private String feedback;

    public Appointment() {
        this("", null, null, null, null, null, "",
                AppointmentStatus.SCHEDULED, 0, "");
    }

    public Appointment(String appointmentId,
                       Customer customer,
                       Technician technician,
                       Service service,
                       LocalDateTime startTime,
                       LocalDateTime endTime,
                       String notes,
                       AppointmentStatus status,
                       int rating,
                       String feedback) {
        this.appointmentId = appointmentId;
        this.customer = customer;
        this.technician = technician;
        this.service = service;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
        this.status = status;
        this.rating = rating;
        this.feedback = feedback;
    }

    @Override
    public String getId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public void display() {
        // Xử lý ở tầng UI.
    }

    @Override
    public void input() {
        // Xử lý ở tầng UI.
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Đặt lịch hẹn lại với trạng thái SCHEDULED.
     */
    public void schedule(LocalDateTime newStartTime, LocalDateTime newEndTime) {
        this.startTime = newStartTime;
        this.endTime = newEndTime;
        this.status = AppointmentStatus.SCHEDULED;
    }

    /**
     * Bắt đầu cuộc hẹn.
     */
    public void start() {
        this.status = AppointmentStatus.SPENDING;
    }

    /**
     * Hủy cuộc hẹn hiện tại.
     */
    public void cancel(String reason) {
        this.status = AppointmentStatus.CANCELLED;
        this.notes = reason;
    }

    /**
     * Hoàn tất cuộc hẹn và lưu đánh giá nếu có.
     */
    public void complete(int rating, String feedback) {
        this.status = AppointmentStatus.COMPLETED;
        this.rating = rating;
        this.feedback = feedback;
    }

    /**
     * Cập nhật phản hồi sau khi hoàn thành dịch vụ.
     */
    public void submitFeedback(int rating, String feedback) {
        this.rating = rating;
        this.feedback = feedback;
    }
}
