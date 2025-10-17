package services;

import java.time.LocalDateTime;

import collections.AppointmentManager;
import collections.CustomerManager;
import collections.ServiceManager;
import enums.AppointmentStatus;
import models.Appointment;
import models.Customer;
import models.Service;

/**
 * Service quản lý logic nghiệp vụ lịch hẹn.
 * Xử lý các thao tác như đặt lịch, hủy, sắp xếp lại và hoàn thành lịch hẹn.
 */
public class AppointmentService {

    private AppointmentManager appointmentManager;
    private CustomerManager customerManager;
    private ServiceManager serviceManager;

    /**
     * Constructor khởi tạo AppointmentService.
     *
     * @param appointmentManager Manager quản lý lịch hẹn
     * @param customerManager    Manager quản lý khách hàng
     * @param serviceManager     Manager quản lý dịch vụ
     */
    public AppointmentService(AppointmentManager appointmentManager, CustomerManager customerManager,
            ServiceManager serviceManager) {
        this.appointmentManager = appointmentManager;
        this.customerManager = customerManager;
        this.serviceManager = serviceManager;
    }

    /**
     * Đặt lịch hẹn mới.
     *
     * @param customerId          ID khách hàng
     * @param serviceId           ID dịch vụ
     * @param appointmentDateTime Thời gian hẹn
     * @return Lịch hẹn vừa tạo hoặc null nếu thất bại
     */
    public Appointment bookAppointment(String customerId, String serviceId, LocalDateTime appointmentDateTime) {
        // Kiểm tra khách hàng tồn tại
        Customer customer = customerManager.getById(customerId);
        if (customer == null) {
            System.out.println("❌ Khách hàng không tồn tại!");
            return null;
        }

        // Kiểm tra dịch vụ tồn tại
        Service service = serviceManager.getById(serviceId);
        if (service == null) {
            System.out.println("❌ Dịch vụ không tồn tại!");
            return null;
        }

        // Kiểm tra thời gian hẹn không trong quá khứ
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            System.out.println("❌ Thời gian hẹn không được trong quá khứ!");
            return null;
        }

        // Sinh ID mới
        String appointmentId = generateAppointmentId();

        // Tạo lịch hẹn mới
        Appointment appointment = new Appointment(appointmentId, customerId, serviceId, appointmentDateTime);

        // Thêm vào Manager
        if (appointmentManager.add(appointment)) {
            System.out.println("✅ Đặt lịch hẹn thành công!");
            return appointment;
        }

        System.out.println("❌ Lỗi khi tạo lịch hẹn!");
        return null;
    }

    /**
     * Hủy lịch hẹn.
     *
     * @param appointmentId ID lịch hẹn
     * @return true nếu hủy thành công, false nếu thất bại
     */
    public boolean cancelAppointment(String appointmentId) {
        Appointment appointment = appointmentManager.getById(appointmentId);
        if (appointment == null) {
            System.out.println("❌ Lịch hẹn không tồn tại!");
            return false;
        }

        // Chỉ có thể hủy lịch ở trạng thái SCHEDULED hoặc SPENDING
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED &&
                appointment.getStatus() != AppointmentStatus.SPENDING) {
            System.out.println("❌ Không thể hủy lịch hẹn ở trạng thái hiện tại!");
            return false;
        }

        appointment.markAsCancelled();

        if (appointmentManager.update(appointment)) {
            System.out.println("✅ Hủy lịch hẹn thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi hủy lịch hẹn!");
        return false;
    }

    /**
     * Sắp xếp lại lịch hẹn với thời gian mới.
     * Xóa lịch cũ và tạo lịch mới với thời gian được cập nhật.
     *
     * @param appointmentId ID lịch hẹn
     * @param newDateTime   Thời gian mới
     * @return true nếu sắp xếp thành công, false nếu thất bại
     */
    public boolean rescheduleAppointment(String appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = appointmentManager.getById(appointmentId);
        if (appointment == null) {
            System.out.println("❌ Lịch hẹn không tồn tại!");
            return false;
        }

        // Chỉ có thể sắp xếp lại lịch ở trạng thái SCHEDULED
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            System.out.println("❌ Chỉ có thể sắp xếp lại lịch ở trạng thái SCHEDULED!");
            return false;
        }

        // Kiểm tra thời gian mới không trong quá khứ
        if (newDateTime.isBefore(LocalDateTime.now())) {
            System.out.println("❌ Thời gian hẹn mới không được trong quá khứ!");
            return false;
        }

        // Xóa lịch cũ
        appointmentManager.delete(appointmentId);

        // Tạo lịch hẹn mới với thời gian được cập nhật
        Appointment newAppointment = new Appointment(
                generateAppointmentId(),
                appointment.getCustomerId(),
                appointment.getServiceId(),
                newDateTime);

        // Gán lại nhân viên nếu có
        if (appointment.getStaffId() != null) {
            newAppointment.assignStaff(appointment.getStaffId());
        }

        if (appointmentManager.add(newAppointment)) {
            System.out.println("✅ Sắp xếp lại lịch hẹn thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi sắp xếp lại lịch hẹn!");
        return false;
    }

    /**
     * Bắt đầu dịch vụ (cập nhật trạng thái từ SCHEDULED sang SPENDING).
     *
     * @param appointmentId ID lịch hẹn
     * @return true nếu bắt đầu thành công, false nếu thất bại
     */
    public boolean startAppointment(String appointmentId) {
        Appointment appointment = appointmentManager.getById(appointmentId);
        if (appointment == null) {
            System.out.println("❌ Lịch hẹn không tồn tại!");
            return false;
        }

        // Chỉ có thể bắt đầu từ trạng thái SCHEDULED
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            System.out.println("❌ Lịch hẹn phải ở trạng thái SCHEDULED!");
            return false;
        }

        appointment.updateStatus(AppointmentStatus.SPENDING);

        if (appointmentManager.update(appointment)) {
            System.out.println("✅ Bắt đầu dịch vụ thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi bắt đầu dịch vụ!");
        return false;
    }

    /**
     * Hoàn thành dịch vụ (cập nhật trạng thái từ SPENDING sang COMPLETED).
     *
     * @param appointmentId ID lịch hẹn
     * @return true nếu hoàn thành thành công, false nếu thất bại
     */
    public boolean completeAppointment(String appointmentId) {
        Appointment appointment = appointmentManager.getById(appointmentId);
        if (appointment == null) {
            System.out.println("❌ Lịch hẹn không tồn tại!");
            return false;
        }

        // Chỉ có thể hoàn thành từ trạng thái SPENDING
        if (appointment.getStatus() != AppointmentStatus.SPENDING) {
            System.out.println("❌ Lịch hẹn phải ở trạng thái SPENDING!");
            return false;
        }

        appointment.markAsCompleted();

        // Cập nhật ngày ghé thăm cuối cùng của khách hàng
        Customer customer = customerManager.getById(appointment.getCustomerId());
        if (customer != null) {
            customer.setLastValidDate(java.time.LocalDate.now());
            customerManager.update(customer);
        }

        if (appointmentManager.update(appointment)) {
            System.out.println("✅ Hoàn thành dịch vụ thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi hoàn thành dịch vụ!");
        return false;
    }

    /**
     * Gán nhân viên cho lịch hẹn.
     *
     * @param appointmentId ID lịch hẹn
     * @param staffId       ID nhân viên
     * @return true nếu gán thành công, false nếu thất bại
     */
    public boolean assignStaffToAppointment(String appointmentId, String staffId) {
        Appointment appointment = appointmentManager.getById(appointmentId);
        if (appointment == null) {
            System.out.println("❌ Lịch hẹn không tồn tại!");
            return false;
        }

        appointment.assignStaff(staffId);

        if (appointmentManager.update(appointment)) {
            System.out.println("✅ Gán nhân viên thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi gán nhân viên!");
        return false;
    }

    /**
     * Lấy lịch sử lịch hẹn của khách hàng.
     * Trả về mảy chứa tất cả lịch hẹn của khách hàng đó.
     *
     * @param customerId ID khách hàng
     * @return Mảy Appointment của khách hàng (mảy rỗng nếu không có)
     */
    public Appointment[] getCustomerAppointmentHistory(String customerId) {
        // Lấy tất cả lịch hẹn từ Manager
        Appointment[] allAppointments = appointmentManager.getAll();

        // Đếm số lượng lịch hẹn của khách hàng
        int count = 0;
        for (Appointment apt : allAppointments) {
            if (apt.getCustomerId().equals(customerId)) {
                count++;
            }
        }

        // Tạo mảy kết quả với kích thước đúng
        Appointment[] result = new Appointment[count];
        int index = 0;
        for (Appointment apt : allAppointments) {
            if (apt.getCustomerId().equals(customerId)) {
                result[index++] = apt;
            }
        }

        return result;
    }

    /**
     * Sinh ID lịch hẹn mới theo định dạng APT_XXXXXX.
     *
     * @return ID lịch hẹn mới
     */
    private String generateAppointmentId() {
        return "APT_" + System.currentTimeMillis();
    }
}
