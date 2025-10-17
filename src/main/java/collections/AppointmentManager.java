package collections;

import java.time.LocalDate;
import java.time.LocalDateTime;

import enums.AppointmentStatus;
import models.Appointment;

/**
 * Manager quản lý danh sách lịch hẹn.
 * Cung cấp các phương thức tìm kiếm cụ thể cho Appointment.
 */
public class AppointmentManager extends BaseManager<Appointment> {

    /**
     * Lấy tất cả lịch hẹn.
     * 
     * @return Mảy chứa tất cả lịch hẹn hiện tại
     */
    @Override
    public Appointment[] getAll() {
        // Tạo mảy Appointment mới và copy từng phần tử một
        Appointment[] result = new Appointment[size];
        for (int i = 0; i < size; i++) {
            // Cast từng phần tử từ IEntity sang Appointment
            result[i] = (Appointment) items[i];
        }
        return result;
    }

    /**
     * Tìm tất cả lịch hẹn của một khách hàng.
     * 
     * @param customerId ID khách hàng cần tìm
     * @return Mảy chứa tất cả lịch hẹn của khách hàng
     */
    public Appointment[] findByCustomerId(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            return new Appointment[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getCustomerId().equals(customerId)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Appointment[] result = new Appointment[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getCustomerId().equals(customerId)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm tất cả lịch hẹn của một dịch vụ nhất định.
     * 
     * @param serviceId ID dịch vụ cần tìm
     * @return Mảy chứa tất cả lịch hẹn của dịch vụ đó
     */
    public Appointment[] findByServiceId(String serviceId) {
        if (serviceId == null || serviceId.isEmpty()) {
            return new Appointment[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getServiceId().equals(serviceId)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Appointment[] result = new Appointment[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getServiceId().equals(serviceId)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm tất cả lịch hẹn theo trạng thái.
     * 
     * @param status Trạng thái cần tìm
     * @return Mảy chứa tất cả lịch hẹn có trạng thái đó
     */
    public Appointment[] findByStatus(AppointmentStatus status) {
        if (status == null) {
            return new Appointment[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getStatus() == status) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Appointment[] result = new Appointment[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getStatus() == status) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Tìm lịch hẹn trong khoảng ngày nhất định.
     * 
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Mảy chứa tất cả lịch hẹn trong khoảng
     */
    public Appointment[] findByDateRange(LocalDate startDate,
            LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return new Appointment[0];
        }

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            LocalDate appointmentDate = items[i].getAppointmentDateTime().toLocalDate();
            if (!appointmentDate.isBefore(startDate) &&
                    !appointmentDate.isAfter(endDate)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Appointment[] result = new Appointment[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            LocalDate appointmentDate = items[i].getAppointmentDateTime().toLocalDate();
            if (!appointmentDate.isBefore(startDate) &&
                    !appointmentDate.isAfter(endDate)) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Lấy tất cả lịch hẹn sắp tới (status SCHEDULED).
     * 
     * @return Mảy chứa tất cả lịch hẹn sắp tới
     */
    public Appointment[] findUpcomingAppointments() {
        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getStatus() == AppointmentStatus.SCHEDULED) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Appointment[] result = new Appointment[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getStatus() == AppointmentStatus.SCHEDULED) {
                result[index++] = items[i];
            }
        }

        return result;
    }

    /**
     * Lấy tất cả lịch hẹn quá hạn (thời gian < hiện tại).
     * 
     * @return Mảy chứa tất cả lịch hẹn quá hạn
     */
    public Appointment[] getExpiredAppointments() {
        LocalDateTime now = LocalDateTime.now();

        // Đếm số lượng
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getAppointmentDateTime().isBefore(now)) {
                count++;
            }
        }

        // Tạo mảy kết quả
        Appointment[] result = new Appointment[count];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getAppointmentDateTime().isBefore(now)) {
                result[index++] = items[i];
            }
        }

        return result;
    }
}
