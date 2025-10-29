package com.spa.ui;

import com.spa.model.Appointment;
import com.spa.model.Customer;
import com.spa.model.Service;
import com.spa.model.Technician;
import com.spa.model.enums.AppointmentStatus;
import com.spa.service.Validation;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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
            System.out.println("1. Tạo lịch hẹn");
            System.out.println("2. Thêm nhiều lịch hẹn");
            System.out.println("3. Xuất danh sách");
            System.out.println("4. Tìm kiếm lịch hẹn");
            System.out.println("5. Thống kê lịch hẹn");
            System.out.println("6. Cập nhật trạng thái lịch hẹn");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 6);
            switch (choice) {
                case 1:
                    createAppointment();
                    Validation.pause();
                    break;
                case 2:
                    addMultipleAppointments();
                    Validation.pause();
                    break;
                case 3:
                    listAppointments();
                    Validation.pause();
                    break;
                case 4:
                    searchAppointments();
                    Validation.pause();
                    break;
                case 5:
                    showAppointmentStatistics();
                    Validation.pause();
                    break;
                case 6:
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
            appointment.display();
        }
        if (!hasData) {
            System.out.println("Chưa có lịch hẹn nào.");
        }
    }

    private void createAppointment() {
        if (!createAppointmentInternal()) {
            System.out.println("Không tạo được lịch hẹn.");
        }
    }

    private void addMultipleAppointments() {
        Integer total = Validation.getIntOrCancel("Số lượng lịch hẹn cần thêm", 1, 1000);
        if (total == null) {
            System.out.println("Đã hủy thao tác.");
            return;
        }
        int added = 0;
        for (int i = 0; i < total; i++) {
            System.out.println("-- Lịch hẹn thứ " + (i + 1));
            if (!createAppointmentInternal()) {
                System.out.println("Dừng thêm lịch hẹn.");
                break;
            }
            added++;
        }
        System.out.printf("Đã thêm %d lịch hẹn.%n", added);
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
        appointment.display();
    }

    private boolean createAppointmentInternal() {
        System.out.println();
        System.out.println("--- TẠO LỊCH HẸN ---");
        String id = context.getAppointmentStore().generateNextId();
        System.out.println("Mã lịch hẹn được cấp: " + id);
        Customer customer = selectCustomer();
        if (customer == null) {
            System.out.println("Đã hủy tạo lịch hẹn.");
            return false;
        }
        Technician technician = selectTechnician();
        if (technician == null) {
            System.out.println("Đã hủy tạo lịch hẹn.");
            return false;
        }
        Service service = selectService();
        if (service == null) {
            System.out.println("Đã hủy tạo lịch hẹn.");
            return false;
        }
        LocalDateTime start = Validation.getDateTime("Thời gian bắt đầu (yyyy-MM-dd HH:mm): ", DATE_TIME_FORMAT);
        int totalMinutes = service.getDurationMinutes() + service.getBufferTime();
        LocalDateTime end = start.plusMinutes(totalMinutes);
        if (!isTechnicianAvailable(technician, start, end)) {
            System.out.println("Kỹ thuật viên đã có lịch trong khung giờ này.");
            return false;
        }
        String notes = Validation.getOptionalString("Ghi chú: ");
        Appointment appointment = new Appointment(id, customer, technician, service, start, end,
                notes, AppointmentStatus.SCHEDULED, 0, "");
        context.getAppointmentStore().add(appointment);
        context.getAppointmentStore().writeFile();
        System.out.println("Đã tạo lịch hẹn thành công.");
        appointment.display();
        return true;
    }

    private void searchAppointments() {
        String keywordLine = Validation.getOptionalStringOrCancel("Nhập từ khóa (cách nhau bởi dấu cách) hoặc bỏ trống để xem tất cả: ");
        if (keywordLine == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        String trimmedKeywords = keywordLine.trim().toLowerCase();
        AppointmentStatus[] statuses = AppointmentStatus.values();
        Integer statusChoice = Validation.getIntOrCancel("Chọn trạng thái (0 để bỏ qua): ", 0, statuses.length);
        if (statusChoice == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        AppointmentStatus statusFilter = statusChoice == 0 ? null : statuses[statusChoice - 1];

        String technicianInput = Validation.getOptionalStringOrCancel("Nhập mã/ tên kỹ thuật viên (bỏ trống nếu không lọc): ");
        if (technicianInput == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        String technicianFilter = technicianInput.trim().toLowerCase();

        String customerInput = Validation.getOptionalStringOrCancel("Nhập mã/ tên khách hàng (bỏ trống nếu không lọc): ");
        if (customerInput == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        String customerFilter = customerInput.trim().toLowerCase();

        LocalDateTime fromDate = readOptionalDateTime("Thời gian bắt đầu (yyyy-MM-dd HH:mm)");
        if (fromDate == CANCELLED_MARK) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        LocalDateTime toDate = readOptionalDateTime("Thời gian kết thúc (yyyy-MM-dd HH:mm)");
        if (toDate == CANCELLED_MARK) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }

        Appointment[] appointments = context.getAppointmentStore().getAll();
        boolean foundAny = false;
        String[] tokens = trimmedKeywords.isEmpty() ? new String[]{""} : trimmedKeywords.split("\\s+");
        for (Appointment appointment : appointments) {
            if (appointment == null) {
                continue;
            }
            if (statusFilter != null && appointment.getStatus() != statusFilter) {
                continue;
            }
            if (!technicianFilter.isEmpty()) {
                if (appointment.getTechnician() == null
                        || (!containsIgnoreCase(appointment.getTechnician().getId(), technicianFilter)
                        && !containsIgnoreCase(appointment.getTechnician().getFullName(), technicianFilter))) {
                    continue;
                }
            }
            if (!customerFilter.isEmpty()) {
                if (appointment.getCustomer() == null
                        || (!containsIgnoreCase(appointment.getCustomer().getId(), customerFilter)
                        && !containsIgnoreCase(appointment.getCustomer().getFullName(), customerFilter))) {
                    continue;
                }
            }
            LocalDateTime start = appointment.getStartTime();
            if (fromDate != null && (start == null || start.isBefore(fromDate))) {
                continue;
            }
            if (toDate != null && (start == null || start.isAfter(toDate))) {
                continue;
            }
            if (!matchesTokens(appointment, tokens)) {
                continue;
            }
            appointment.display();
            foundAny = true;
        }
        if (!foundAny) {
            System.out.println("Không có lịch hẹn phù hợp.");
        }
    }

    private void showAppointmentStatistics() {
        Appointment[] appointments = context.getAppointmentStore().getAll();
        if (appointments.length == 0) {
            System.out.println("Chưa có dữ liệu lịch hẹn.");
            return;
        }
        AppointmentStatus[] statuses = AppointmentStatus.values();
        int[] statusCounts = new int[statuses.length];
        int total = 0;
        int ratingSum = 0;
        int ratingCount = 0;
        double totalDurationMinutes = 0.0;
        int durationCount = 0;
        int upcoming = 0;
        LocalDateTime now = LocalDateTime.now();
        for (Appointment appointment : appointments) {
            if (appointment == null) {
                continue;
            }
            total++;
            AppointmentStatus status = appointment.getStatus();
            if (status != null) {
                statusCounts[status.ordinal()]++;
            }
            LocalDateTime start = appointment.getStartTime();
            LocalDateTime end = appointment.getEndTime();
            if (start != null && end != null && end.isAfter(start)) {
                totalDurationMinutes += Duration.between(start, end).toMinutes();
                durationCount++;
            }
            if (status == AppointmentStatus.COMPLETED && appointment.getRating() > 0) {
                ratingSum += appointment.getRating();
                ratingCount++;
            }
            if (status == AppointmentStatus.SCHEDULED && start != null && start.isAfter(now)) {
                upcoming++;
            }
        }
        System.out.printf("Tổng số lịch hẹn: %d%n", total);
        for (AppointmentStatus status : statuses) {
            System.out.printf("- %s: %d%n", status, statusCounts[status.ordinal()]);
        }
        System.out.printf("Đánh giá trung bình: %.2f%n", ratingCount == 0 ? 0.0 : (double) ratingSum / ratingCount);
        System.out.printf("Thời lượng trung bình: %.2f phút%n", durationCount == 0 ? 0.0 : totalDurationMinutes / durationCount);
        System.out.printf("Lịch hẹn sắp diễn ra: %d%n", upcoming);
    }

    private Customer selectCustomer() {
        Customer[] customers = context.getCustomerStore().getAll();
        int count = countActiveCustomers(customers);
        if (count == 0) {
            System.out.println("Không có khách hàng hoạt động.");
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
        int choice = Validation.getInt("Lựa chọn (0 để hủy): ", 0, active.length);
        if (choice == 0) {
            return null;
        }
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
            System.out.println("Không có kỹ thuật viên hoạt động.");
            return null;
        }
        System.out.println("Chọn kỹ thuật viên:");
        for (int i = 0; i < technicians.length; i++) {
            Technician technician = technicians[i];
            System.out.printf("%d. %s - %s%n", i + 1, technician.getId(), technician.getFullName());
        }
        int choice = Validation.getInt("Lựa chọn (0 để hủy): ", 0, technicians.length);
        if (choice == 0) {
            return null;
        }
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
            System.out.println("Không có dịch vụ hoạt động.");
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
        int choice = Validation.getInt("Lựa chọn (0 để hủy): ", 0, active.length);
        if (choice == 0) {
            return null;
        }
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

    private LocalDateTime readOptionalDateTime(String prompt) {
        while (true) {
            String input = Validation.getOptionalStringOrCancel(prompt + " (bỏ trống nếu không lọc): ");
            if (input == null) {
                return CANCELLED_MARK;
            }
            if (input.trim().isEmpty()) {
                return null;
            }
            try {
                return LocalDateTime.parse(input.trim(), DATE_TIME_FORMAT);
            } catch (DateTimeParseException ex) {
                System.out.println("Thời gian không hợp lệ. Vui lòng nhập lại.");
            }
        }
    }

    private boolean matchesTokens(Appointment appointment, String[] tokens) {
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            return true;
        }
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            String lower = token.toLowerCase();
            boolean match = containsIgnoreCase(appointment.getId(), lower)
                    || (appointment.getCustomer() != null && containsIgnoreCase(appointment.getCustomer().getFullName(), lower))
                    || (appointment.getTechnician() != null && containsIgnoreCase(appointment.getTechnician().getFullName(), lower))
                    || (appointment.getService() != null && containsIgnoreCase(appointment.getService().getServiceName(), lower))
                    || containsIgnoreCase(appointment.getNotes(), lower)
                    || (appointment.getStatus() != null && appointment.getStatus().name().toLowerCase().contains(lower));
            if (!match) {
                return false;
            }
        }
        return true;
    }

    private boolean containsIgnoreCase(String source, String token) {
        if (source == null || token == null || token.isEmpty()) {
            return false;
        }
        return source.toLowerCase().contains(token);
    }

    private static final LocalDateTime CANCELLED_MARK = LocalDateTime.MIN;
}
