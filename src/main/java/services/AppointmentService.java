package services;

import java.time.LocalDateTime;

import collections.AppointmentManager;
import collections.CustomerManager;
import collections.ServiceManager;
import enums.AppointmentStatus;
import exceptions.BusinessLogicException;
import exceptions.EntityNotFoundException;
import exceptions.ValidationException;
import models.Appointment;
import models.Customer;

/**
 * Service quản lý logic nghiệp vụ lịch hẹn.
 * Xử lý các thao tác như đặt lịch, hủy, sắp xếp lại và hoàn thành lịch hẹn.
 * Throws exceptions thay vì return false hoặc in error message.
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
     * @return Lịch hẹn vừa tạo
     * @throws EntityNotFoundException nếu khách hàng hoặc dịch vụ không tồn tại
     * @throws ValidationException     nếu thời gian hẹn không hợp lệ
     * @throws BusinessLogicException  nếu vi phạm business rule
     */
    public Appointment bookAppointment(String customerId, String serviceId, LocalDateTime appointmentDateTime)
            throws EntityNotFoundException, ValidationException, BusinessLogicException {

        // Kiểm tra thời gian hẹn không trong quá khứ
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Thời gian hẹn",
                    "Không được là thời gian trong quá khứ");
        }

        try {
            // Kiểm tra khách hàng tồn tại
            customerManager.getById(customerId);

            // Kiểm tra dịch vụ tồn tại
            serviceManager.getById(serviceId);

            // Sinh ID mới
            String appointmentId = generateAppointmentId();

            // Tạo lịch hẹn mới
            Appointment appointment = new Appointment(appointmentId, customerId, serviceId, appointmentDateTime);

            // Thêm vào Manager
            appointmentManager.add(appointment);
            return appointment;

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (exceptions.InvalidEntityException | exceptions.EntityAlreadyExistsException e) {
            throw new BusinessLogicException(
                    "đặt lịch hẹn",
                    e.getErrorMessage());
        }
    }

    /**
     * Hủy lịch hẹn.
     *
     * @param appointmentId ID lịch hẹn
     * @return true nếu hủy thành công
     * @throws EntityNotFoundException nếu lịch hẹn không tồn tại
     * @throws BusinessLogicException  nếu không thể hủy ở trạng thái hiện tại
     */
    public boolean cancelAppointment(String appointmentId)
            throws EntityNotFoundException, BusinessLogicException {

        try {
            Appointment appointment = appointmentManager.getById(appointmentId);

            // Chỉ có thể hủy lịch ở trạng thái SCHEDULED hoặc SPENDING
            if (appointment.getStatus() != AppointmentStatus.SCHEDULED &&
                    appointment.getStatus() != AppointmentStatus.SPENDING) {
                throw new BusinessLogicException(
                        "hủy lịch hẹn",
                        "Lịch hẹn ở trạng thái " + appointment.getStatus() + " không thể hủy");
            }

            appointment.markAsCancelled();

            appointmentManager.update(appointment);
            return true;

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (exceptions.InvalidEntityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sắp xếp lại lịch hẹn với thời gian mới.
     * Xóa lịch cũ và tạo lịch mới với thời gian được cập nhật.
     *
     * @param appointmentId ID lịch hẹn
     * @param newDateTime   Thời gian mới
     * @return true nếu sắp xếp thành công
     * @throws EntityNotFoundException nếu lịch hẹn không tồn tại
     * @throws ValidationException     nếu thời gian không hợp lệ
     * @throws BusinessLogicException  nếu không thể sắp xếp lại ở trạng thái hiện
     *                                 tại
     */
    public boolean rescheduleAppointment(String appointmentId, LocalDateTime newDateTime)
            throws EntityNotFoundException, ValidationException, BusinessLogicException {

        // Kiểm tra thời gian mới không trong quá khứ
        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Thời gian hẹn mới",
                    "Không được là thời gian trong quá khứ");
        }

        try {
            Appointment appointment = appointmentManager.getById(appointmentId);

            // Chỉ có thể sắp xếp lại lịch ở trạng thái SCHEDULED
            if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
                throw new BusinessLogicException(
                        "sắp xếp lại lịch hẹn",
                        "Lịch hẹn phải ở trạng thái SCHEDULED");
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

            appointmentManager.add(newAppointment);
            return true;

        } catch (EntityNotFoundException | BusinessLogicException e) {
            throw e;
        } catch (exceptions.InvalidEntityException | exceptions.EntityAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Bắt đầu dịch vụ (cập nhật trạng thái từ SCHEDULED sang SPENDING).
     *
     * @param appointmentId ID lịch hẹn
     * @return true nếu bắt đầu thành công
     * @throws EntityNotFoundException nếu lịch hẹn không tồn tại
     * @throws BusinessLogicException  nếu lịch hẹn không ở trạng thái SCHEDULED
     */
    public boolean startAppointment(String appointmentId)
            throws EntityNotFoundException, BusinessLogicException {

        try {
            Appointment appointment = appointmentManager.getById(appointmentId);

            // Chỉ có thể bắt đầu từ trạng thái SCHEDULED
            if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
                throw new BusinessLogicException(
                        "bắt đầu dịch vụ",
                        "Lịch hẹn phải ở trạng thái SCHEDULED");
            }

            appointment.updateStatus(AppointmentStatus.SPENDING);

            appointmentManager.update(appointment);
            return true;

        } catch (EntityNotFoundException | BusinessLogicException e) {
            throw e;
        } catch (exceptions.InvalidEntityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hoàn thành dịch vụ (cập nhật trạng thái từ SPENDING sang COMPLETED).
     *
     * @param appointmentId ID lịch hẹn
     * @return true nếu hoàn thành thành công
     * @throws EntityNotFoundException nếu lịch hẹn không tồn tại
     * @throws BusinessLogicException  nếu lịch hẹn không ở trạng thái SPENDING
     */
    public boolean completeAppointment(String appointmentId)
            throws EntityNotFoundException, BusinessLogicException {

        try {
            Appointment appointment = appointmentManager.getById(appointmentId);

            // Chỉ có thể hoàn thành từ trạng thái SPENDING
            if (appointment.getStatus() != AppointmentStatus.SPENDING) {
                throw new BusinessLogicException(
                        "hoàn thành dịch vụ",
                        "Lịch hẹn phải ở trạng thái SPENDING");
            }

            appointment.markAsCompleted();

            // Cập nhật ngày ghé thăm cuối cùng của khách hàng
            try {
                Customer customer = customerManager.getById(appointment.getCustomerId());
                customer.setLastValidDate(java.time.LocalDate.now());
                customerManager.update(customer);
            } catch (exceptions.InvalidEntityException | exceptions.EntityNotFoundException e) {
                // Log nhưng không throw - cập nhật appointment vẫn tiếp tục
                System.err.println("Cảnh báo: Không thể cập nhật ngày ghé thăm khách hàng");
            }

            appointmentManager.update(appointment);
            return true;

        } catch (EntityNotFoundException | BusinessLogicException e) {
            throw e;
        } catch (exceptions.InvalidEntityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gán nhân viên cho lịch hẹn.
     *
     * @param appointmentId ID lịch hẹn
     * @param staffId       ID nhân viên
     * @return true nếu gán thành công
     * @throws EntityNotFoundException nếu lịch hẹn không tồn tại
     */
    public boolean assignStaffToAppointment(String appointmentId, String staffId)
            throws EntityNotFoundException {

        try {
            Appointment appointment = appointmentManager.getById(appointmentId);

            appointment.assignStaff(staffId);

            appointmentManager.update(appointment);
            return true;

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (exceptions.InvalidEntityException e) {
            throw new RuntimeException(e);
        }
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
