package com.spa.ui;

import com.spa.model.Service;
import com.spa.service.Validation;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Menu thao tác với dịch vụ.
 */
public class ServiceMenu implements MenuModule {
    private final MenuContext context;

    public ServiceMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ DỊCH VỤ ---");
            System.out.println("1. Xem danh sách dịch vụ");
            System.out.println("2. Thêm dịch vụ mới");
            System.out.println("3. Cập nhật dịch vụ");
            System.out.println("4. Xóa dịch vụ");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listServices();
                    Validation.pause();
                    break;
                case 2:
                    addService();
                    Validation.pause();
                    break;
                case 3:
                    updateService();
                    Validation.pause();
                    break;
                case 4:
                    deleteService();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listServices() {
        System.out.println();
        System.out.println("--- DANH SÁCH DỊCH VỤ ---");
        Service[] services = context.getServiceStore().getAll();
        if (services.length == 0) {
            System.out.println("Chưa có dịch vụ nào.");
            return;
        }
        for (Service service : services) {
            if (service == null || service.isDeleted()) {
                continue;
            }
            System.out.printf("%s | %s | %s | %s | %s%n",
                    service.getId(),
                    service.getServiceName(),
                    service.getCategory(),
                    service.getBasePrice(),
                    service.isActive() ? "Đang mở" : "Tạm khóa");
        }
    }

    private void addService() {
        System.out.println();
        System.out.println("--- THÊM DỊCH VỤ ---");
        String id = Validation.getString("Mã dịch vụ: ");
        if (context.getServiceStore().findById(id) != null) {
            System.out.println("Mã dịch vụ đã tồn tại.");
            return;
        }
        String name = Validation.getString("Tên dịch vụ: ");
        double basePrice = Validation.getDouble("Giá gốc: ", 0.0, 1_000_000_000.0);
        int duration = Validation.getInt("Thời lượng (phút): ", 10, 600);
        int buffer = Validation.getInt("Thời gian đệm (phút): ", 0, 120);
        String description = Validation.getString("Mô tả ngắn: ");
        LocalDate createdDate = Validation.getDate("Ngày tạo (yyyy-MM-dd): ", DATE_FORMAT);
        boolean active = Validation.getBoolean("Kích hoạt dịch vụ?");

        Service service = new Service(id, name, BigDecimal.valueOf(basePrice), duration, buffer,
                description, createdDate, active, MenuHelper.selectServiceCategory(), false);
        context.getServiceStore().add(service);
        context.getServiceStore().writeFile();
        System.out.println("Đã thêm dịch vụ thành công.");
    }

    private void updateService() {
        System.out.println();
        System.out.println("--- CẬP NHẬT DỊCH VỤ ---");
        String id = Validation.getString("Mã dịch vụ: ");
        Service service = context.getServiceStore().findById(id);
        if (service == null || service.isDeleted()) {
            System.out.println("Không tìm thấy dịch vụ.");
            return;
        }
        String name = Validation.getString("Tên dịch vụ: ");
        double basePrice = Validation.getDouble("Giá gốc: ", 0.0, 1_000_000_000.0);
        int duration = Validation.getInt("Thời lượng (phút): ", 10, 600);
        int buffer = Validation.getInt("Thời gian đệm (phút): ", 0, 120);
        String description = Validation.getString("Mô tả: ");
        LocalDate created = Validation.getDate("Ngày tạo (yyyy-MM-dd): ", DATE_FORMAT);
        boolean active = Validation.getBoolean("Kích hoạt dịch vụ?");

        service.setServiceName(name);
        service.setBasePrice(BigDecimal.valueOf(basePrice));
        service.setDurationMinutes(duration);
        service.setBufferTime(buffer);
        service.setDescription(description);
        service.setCreatedDate(created);
        service.setActive(active);
        service.setCategory(MenuHelper.selectServiceCategory());
        context.getServiceStore().writeFile();
        System.out.println("Đã cập nhật dịch vụ.");
    }

    private void deleteService() {
        System.out.println();
        System.out.println("--- XÓA DỊCH VỤ ---");
        String id = Validation.getString("Mã dịch vụ: ");
        if (context.getServiceStore().delete(id)) {
            context.getServiceStore().writeFile();
            System.out.println("Đã xóa dịch vụ.");
        } else {
            System.out.println("Không tìm thấy dịch vụ.");
        }
    }
}
