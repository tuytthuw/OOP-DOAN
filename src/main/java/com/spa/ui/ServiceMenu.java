package com.spa.ui;

import com.spa.model.Service;
import com.spa.model.enums.ServiceCategory;
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
            System.out.println("1. Thêm dịch vụ mới");
            System.out.println("2. Thêm nhiều dịch vụ");
            System.out.println("3. Xuất danh sách");
            System.out.println("4. Cập nhật dịch vụ");
            System.out.println("5. Xóa dịch vụ");
            System.out.println("6. Tìm kiếm dịch vụ");
            System.out.println("7. Thống kê dịch vụ");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 7);
            switch (choice) {
                case 1:
                    addService();
                    Validation.pause();
                    break;
                case 2:
                    addMultipleServices();
                    Validation.pause();
                    break;
                case 3:
                    listServices();
                    Validation.pause();
                    break;
                case 4:
                    updateService();
                    Validation.pause();
                    break;
                case 5:
                    deleteService();
                    Validation.pause();
                    break;
                case 6:
                    searchServices();
                    Validation.pause();
                    break;
                case 7:
                    showServiceStatistics();
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
            service.display();
        }
    }

    private void addService() {
        System.out.println();
        System.out.println("--- THÊM DỊCH VỤ ---");
        String id = context.getServiceStore().generateNextId();
        System.out.println("Mã dịch vụ được cấp: " + id);
        Service service = promptService(id, null);
        if (service == null) {
            System.out.println("Đã hủy thêm dịch vụ.");
            return;
        }
        context.getServiceStore().add(service);
        context.getServiceStore().writeFile();
        System.out.println("Đã thêm dịch vụ thành công.");
        service.display();
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
        Service updated = promptService(id, service);
        if (updated == null) {
            System.out.println("Đã hủy cập nhật dịch vụ.");
            return;
        }
        service.setServiceName(updated.getServiceName());
        service.setBasePrice(updated.getBasePrice());
        service.setDurationMinutes(updated.getDurationMinutes());
        service.setBufferTime(updated.getBufferTime());
        service.setDescription(updated.getDescription());
        service.setCreatedDate(updated.getCreatedDate());
        service.setActive(updated.isActive());
        service.setCategory(updated.getCategory());
        context.getServiceStore().writeFile();
        System.out.println("Đã cập nhật dịch vụ.");
        service.display();
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

    private void addMultipleServices() {
        Integer total = Validation.getIntOrCancel("Số lượng dịch vụ cần thêm", 1, 1000);
        if (total == null) {
            System.out.println("Đã hủy thao tác.");
            return;
        }
        int added = 0;
        for (int i = 0; i < total; i++) {
            System.out.println("-- Dịch vụ thứ " + (i + 1));
            String id = context.getServiceStore().generateNextId();
            Service service = promptService(id, null);
            if (service == null) {
                System.out.println("Dừng thêm dịch vụ.");
                break;
            }
            context.getServiceStore().add(service);
            added++;
        }
        context.getServiceStore().writeFile();
        System.out.printf("Đã thêm %d dịch vụ mới.%n", added);
    }

    private void searchServices() {
        String keywordLine = Validation.getOptionalStringOrCancel("Nhập từ khóa (cách nhau bởi dấu cách) hoặc bỏ trống để xem tất cả: ");
        if (keywordLine == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        String trimmedKeywords = keywordLine.trim().toLowerCase();
        ServiceCategory selectedCategory = MenuHelper.selectServiceCategory();
        Boolean activeFilter = Validation.getBooleanOrCancel("Chỉ lấy dịch vụ đang mở?");

        Service[] services = context.getServiceStore().getAll();
        boolean foundAny = false;
        for (Service service : services) {
            if (service == null || service.isDeleted()) {
                continue;
            }
            if (selectedCategory != null && service.getCategory() != selectedCategory) {
                continue;
            }
            if (activeFilter != null && service.isActive() != activeFilter) {
                continue;
            }
            if (!matchesTokens(service, trimmedKeywords.split("\\s+"))) {
                continue;
            }
            service.display();
            foundAny = true;
        }
        if (!foundAny) {
            System.out.println("Không có dịch vụ phù hợp.");
        }
    }

    private void showServiceStatistics() {
        Service[] services = context.getServiceStore().getAll();
        if (services.length == 0) {
            System.out.println("Chưa có dữ liệu dịch vụ.");
            return;
        }
        int total = 0;
        int activeCount = 0;
        int inactiveCount = 0;
        int[] categoryCounts = new int[com.spa.model.enums.ServiceCategory.values().length];
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalDuration = 0;
        for (Service service : services) {
            if (service == null) {
                continue;
            }
            total++;
            if (service.isDeleted()) {
                inactiveCount++;
                continue;
            }
            if (service.isActive()) {
                activeCount++;
            } else {
                inactiveCount++;
            }
            ServiceCategory category = service.getCategory();
            if (category != null) {
                categoryCounts[category.ordinal()]++;
            }
            if (service.getBasePrice() != null) {
                totalPrice = totalPrice.add(service.getBasePrice());
            }
            totalDuration += service.getDurationMinutes();
        }
        System.out.printf("Tổng số dịch vụ: %d%n", total);
        System.out.printf("Đang mở: %d | Tạm khóa/Đã xóa: %d%n", activeCount, inactiveCount);
        ServiceCategory[] categories = ServiceCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.printf("- %s: %d%n", categories[i], categoryCounts[i]);
        }
        System.out.printf("Tổng giá gốc: %s%n", totalPrice);
        System.out.printf("Giá trung bình: %s%n", total == 0 ? BigDecimal.ZERO : totalPrice.divide(BigDecimal.valueOf(total), BigDecimal.ROUND_HALF_UP));
        System.out.printf("Thời lượng trung bình: %.2f phút%n", total == 0 ? 0.0 : (double) totalDuration / total);
    }

    private Service promptService(String id, Service base) {
        String name = Validation.getStringOrCancel(buildPrompt("Tên dịch vụ", base == null ? null : base.getServiceName()));
        if (name == null) {
            return null;
        }
        Double price = Validation.getDoubleOrCancel(buildPrompt("Giá gốc", base == null || base.getBasePrice() == null ? null : base.getBasePrice().toString()), 0.0, 1_000_000_000.0);
        if (price == null) {
            return null;
        }
        Integer duration = Validation.getIntOrCancel(buildPrompt("Thời lượng (phút)", base == null ? null : Integer.toString(base.getDurationMinutes())), 10, 600);
        if (duration == null) {
            return null;
        }
        Integer buffer = Validation.getIntOrCancel(buildPrompt("Thời gian đệm (phút)", base == null ? null : Integer.toString(base.getBufferTime())), 0, 120);
        if (buffer == null) {
            return null;
        }
        String description = Validation.getStringOrCancel(buildPrompt("Mô tả", base == null ? null : base.getDescription()));
        if (description == null) {
            return null;
        }
        LocalDate createdDate = Validation.getDateOrCancel(buildPrompt("Ngày tạo (yyyy-MM-dd)", base == null || base.getCreatedDate() == null ? null : base.getCreatedDate().toString()), DATE_FORMAT);
        if (createdDate == null) {
            return null;
        }
        Boolean active = Validation.getBooleanOrCancel(buildPrompt("Kích hoạt dịch vụ?", base == null ? null : (base.isActive() ? "Y" : "N")));
        if (active == null) {
            return null;
        }
        if (base != null) {
            System.out.println("Nhóm dịch vụ hiện tại: " + base.getCategory());
        }
        ServiceCategory category = MenuHelper.selectServiceCategory();
        if (category == null) {
            return null;
        }
        return new Service(id, name, BigDecimal.valueOf(price), duration, buffer,
                description, createdDate, active, category, base != null && base.isDeleted());
    }

    private String buildPrompt(String label, String current) {
        if (current == null || current.isEmpty()) {
            return label + ": ";
        }
        return String.format("%s (hiện tại: %s): ", label, current);
    }

    private boolean matchesTokens(Service service, String[] tokens) {
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            return true;
        }
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            String lower = token.toLowerCase();
            boolean match = containsIgnoreCase(service.getId(), lower)
                    || containsIgnoreCase(service.getServiceName(), lower)
                    || containsIgnoreCase(service.getDescription(), lower)
                    || (service.getCategory() != null && service.getCategory().name().toLowerCase().contains(lower));
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
}
