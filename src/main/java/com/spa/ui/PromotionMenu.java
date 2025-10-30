package com.spa.ui;

import com.spa.model.Promotion;
import com.spa.model.enums.DiscountType;
import com.spa.service.Validation;
import java.time.LocalDate;

import static com.spa.ui.MenuConstants.DATE_FORMAT;

/**
 * Menu quản lý chương trình khuyến mãi.
 */
public class PromotionMenu implements MenuModule {
    private final MenuContext context;

    public PromotionMenu(MenuContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- QUẢN LÝ KHUYẾN MÃI ---");
            System.out.println("1. Thêm khuyến mãi");
            System.out.println("2. Thêm nhiều khuyến mãi");
            System.out.println("3. Xuất danh sách");
            System.out.println("4. Cập nhật khuyến mãi");
            System.out.println("5. Xóa khuyến mãi");
            System.out.println("6. Tìm kiếm khuyến mãi");
            System.out.println("7. Thống kê khuyến mãi");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 7);
            switch (choice) {
                case 1:
                    addPromotion();
                    Validation.pause();
                    break;
                case 2:
                    addMultiplePromotions();
                    Validation.pause();
                    break;
                case 3:
                    listPromotions();
                    Validation.pause();
                    break;
                case 4:
                    updatePromotion();
                    Validation.pause();
                    break;
                case 5:
                    deletePromotion();
                    Validation.pause();
                    break;
                case 6:
                    searchPromotions();
                    Validation.pause();
                    break;
                case 7:
                    showPromotionStatistics();
                    Validation.pause();
                    break;
                case 0:
                default:
                    back = true;
                    break;
            }
        }
    }

    private void listPromotions() {
        System.out.println();
        System.out.println("--- DANH SÁCH KHUYẾN MÃI ---");
        Promotion[] promotions = context.getPromotionStore().getAll();
        boolean hasData = false;
        String header = promotionHeader();
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        for (Promotion promotion : promotions) {
            if (promotion == null) {
                continue;
            }
            printPromotionRow(promotion);
            hasData = true;
        }
        if (!hasData) {
            System.out.println("Chưa có khuyến mãi nào.");
        }
    }

    private void addPromotion() {
        System.out.println();
        System.out.println("--- THÊM KHUYẾN MÃI ---");
        String id = context.getPromotionStore().generateNextId();
        System.out.println("Mã khuyến mãi được cấp: " + id);
        Promotion promotion = promptPromotion(id, null);
        if (promotion == null) {
            System.out.println("Đã hủy thêm khuyến mãi.");
            return;
        }
        context.getPromotionStore().add(promotion);
        context.getPromotionStore().writeFile();
        System.out.println("Đã thêm khuyến mãi thành công.");
        promotion.display();
    }

    private void updatePromotion() {
        System.out.println();
        System.out.println("--- CẬP NHẬT KHUYẾN MÃI ---");
        String id = Validation.getString("Mã khuyến mãi: ");
        Promotion existing = context.getPromotionStore().findById(id);
        if (existing == null || existing.isDeleted()) {
            System.out.println("Không tìm thấy khuyến mãi.");
            return;
        }
        Promotion updated = promptPromotion(id, existing);
        if (updated == null) {
            System.out.println("Đã hủy cập nhật khuyến mãi.");
            return;
        }
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setDiscountType(updated.getDiscountType());
        existing.setDiscountValue(updated.getDiscountValue());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setMinPurchaseAmount(updated.getMinPurchaseAmount());
        context.getPromotionStore().writeFile();
        System.out.println("Đã cập nhật khuyến mãi.");
        existing.display();
    }

    private void deletePromotion() {
        System.out.println();
        System.out.println("--- XÓA KHUYẾN MÃI ---");
        String id = Validation.getString("Mã khuyến mãi: ");
        if (context.getPromotionStore().delete(id)) {
            context.getPromotionStore().writeFile();
            System.out.println("Đã xóa khuyến mãi.");
        } else {
            System.out.println("Không tìm thấy khuyến mãi.");
        }
    }

    private Promotion promptPromotion(String id, Promotion base) {
        String name = Validation.getStringOrCancel(buildPrompt("Tên khuyến mãi", base == null ? null : base.getName()));
        if (name == null) {
            return null;
        }
        String description = Validation.getStringOrCancel(buildPrompt("Mô tả ngắn", base == null ? null : base.getDescription()));
        if (description == null) {
            return null;
        }
        DiscountType discountType = MenuHelper.selectDiscountType();
        if (discountType == null) {
            return null;
        }
        Double discountValue;
        if (discountType == DiscountType.PERCENTAGE) {
            String currentPercent = null;
            if (base != null && base.getDiscountType() == DiscountType.PERCENTAGE) {
                currentPercent = Double.toString(base.getDiscountValue() * 100.0);
            }
            Double percent = Validation.getDoubleOrCancel(buildPrompt("Giá trị giảm (%)", currentPercent), 0.0, 100.0);
            if (percent == null) {
                return null;
            }
            discountValue = percent / 100.0;
        } else {
            String currentAmount = base == null ? null : Double.toString(base.getDiscountValue());
            Double amount = Validation.getDoubleOrCancel(buildPrompt("Giá trị giảm (VNĐ)", currentAmount), 0.0, 1_000_000_000.0);
            if (amount == null) {
                return null;
            }
            discountValue = amount;
        }
        LocalDate startDate = Validation.getFutureOrTodayDateOrCancel(buildPrompt("Ngày bắt đầu (dd/MM/yyyy)",
                base == null || base.getStartDate() == null ? null : base.getStartDate().format(DATE_FORMAT)), DATE_FORMAT);
        if (startDate == null) {
            return null;
        }
        LocalDate endDate = Validation.getFutureOrTodayDateOrCancel(buildPrompt("Ngày kết thúc (dd/MM/yyyy)",
                base == null || base.getEndDate() == null ? null : base.getEndDate().format(DATE_FORMAT)), DATE_FORMAT);
        if (endDate == null) {
            return null;
        }
        if (endDate.isBefore(startDate)) {
            System.out.println("Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.");
            return null;
        }
        Double minPurchase = Validation.getDoubleOrCancel(buildPrompt("Mức chi tối thiểu", base == null ? null : Double.toString(base.getMinPurchaseAmount())), 0.0, 1_000_000_000.0);
        if (minPurchase == null) {
            return null;
        }
        Promotion promotion = new Promotion(id, name, description, discountType, discountValue,
                startDate, endDate, minPurchase, false);
        if (base != null && base.isDeleted()) {
            promotion.setDeleted(true);
        }
        return promotion;
    }

    private void addMultiplePromotions() {
        Integer total = Validation.getIntOrCancel("Số lượng khuyến mãi cần thêm", 1, 1000);
        if (total == null) {
            System.out.println("Đã hủy thao tác.");
            return;
        }
        int added = 0;
        for (int i = 0; i < total; i++) {
            System.out.println("-- Khuyến mãi thứ " + (i + 1));
            String id = context.getPromotionStore().generateNextId();
            Promotion promotion = promptPromotion(id, null);
            if (promotion == null) {
                System.out.println("Dừng thêm khuyến mãi.");
                break;
            }
            context.getPromotionStore().add(promotion);
            added++;
        }
        context.getPromotionStore().writeFile();
        System.out.printf("Đã thêm %d khuyến mãi mới.%n", added);
    }

    private void searchPromotions() {
        String exactId = Validation.getStringOrCancel("Nhập mã khuyến mãi để tìm nhanh (nhập '" + Validation.cancelKeyword() + "' để bỏ qua)");
        if (exactId == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        if (!exactId.isEmpty()) {
            Promotion promotion = context.getPromotionStore().findById(exactId);
            if (promotion != null && !promotion.isDeleted()) {
                promotion.display();
            } else {
                System.out.println("Không tìm thấy khuyến mãi theo mã.");
            }
        }

        String keywordsLine = Validation.getStringOrCancel("Nhập từ khóa (cách nhau bởi dấu cách, nhập '" + Validation.cancelKeyword() + "' để bỏ qua)");
        if (keywordsLine == null) {
            System.out.println("Đã hủy tìm kiếm.");
            return;
        }
        Boolean activeFilter = Validation.getBooleanOrCancel("Chỉ hiển thị khuyến mãi còn hiệu lực?");
        String[] tokens = keywordsLine.toLowerCase().trim().split("\\s+");

        Promotion[] promotions = context.getPromotionStore().getAll();
        boolean foundAny = false;
        boolean headerPrinted = false;
        for (Promotion promotion : promotions) {
            if (promotion == null || promotion.isDeleted()) {
                continue;
            }
            if (activeFilter != null && promotion.isValid() != activeFilter) {
                continue;
            }
            if (!matchesTokens(promotion, tokens)) {
                continue;
            }
            if (!headerPrinted) {
                String header = promotionHeader();
                System.out.println(header);
                System.out.println("-".repeat(header.length()));
                headerPrinted = true;
            }
            printPromotionRow(promotion);
            foundAny = true;
        }
        if (!foundAny) {
            System.out.println("Không có khuyến mãi phù hợp.");
        }
    }

    private void showPromotionStatistics() {
        Promotion[] promotions = context.getPromotionStore().getAll();
        if (promotions.length == 0) {
            System.out.println("Chưa có dữ liệu khuyến mãi.");
            return;
        }
        int total = 0;
        int active = 0;
        int expired = 0;
        int deleted = 0;
        double totalDiscountValue = 0.0;
        for (Promotion promotion : promotions) {
            if (promotion == null) {
                continue;
            }
            total++;
            if (promotion.isDeleted()) {
                deleted++;
                continue;
            }
            if (promotion.isValid()) {
                active++;
            } else {
                expired++;
            }
            totalDiscountValue += promotion.getDiscountValue();
        }
        System.out.printf("Tổng số khuyến mãi: %d%n", total);
        System.out.printf("Đang hiệu lực: %d | Hết hạn: %d | Đã khóa: %d%n", active, expired, deleted);
        System.out.printf("Tổng giá trị giảm: %.2f%n", totalDiscountValue);
        System.out.printf("Giá trị giảm trung bình: %.2f%n", total == 0 ? 0.0 : totalDiscountValue / total);
    }

    private String buildPrompt(String label, String current) {
        if (current == null || current.isEmpty()) {
            return label + ": ";
        }
        return String.format("%s (hiện tại: %s): ", label, current);
    }

    private boolean matchesTokens(Promotion promotion, String[] tokens) {
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            return true;
        }
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            String lower = token.toLowerCase();
            boolean match = containsIgnoreCase(promotion.getId(), lower)
                    || containsIgnoreCase(promotion.getName(), lower)
                    || containsIgnoreCase(promotion.getDescription(), lower)
                    || (promotion.getDiscountType() != null && promotion.getDiscountType().name().toLowerCase().contains(lower));
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

    private String formatDate(LocalDate date) {
        return date == null ? "N/A" : date.toString();
    }

    private String promotionHeader() {
        return String.format("%-8s | %-22s | %-12s | %-10s | %-10s | %-12s | %-12s | %-10s | %-8s",
                "MÃ", "TÊN CHƯƠNG TRÌNH", "LOẠI", "GIÁ TRỊ", "TỐI THIỂU", "BẮT ĐẦU", "KẾT THÚC", "HIỆU LỰC", "TRẠNG");
    }

    private void printPromotionRow(Promotion promotion) {
        String effectiveness = promotion.isValid() ? "Còn" : "Hết";
        String status = promotion.isDeleted() ? "Đã khóa" : "Hoạt động";
        System.out.printf("%-8s | %-22s | %-12s | %-10.2f | %-10.2f | %-12s | %-12s | %-10s | %-8s%n",
                nullToEmpty(promotion.getId()),
                nullToEmpty(promotion.getName()),
                promotion.getDiscountType() == null ? "" : promotion.getDiscountType().name(),
                promotion.getDiscountValue(),
                promotion.getMinPurchaseAmount(),
                promotion.getStartDate() == null ? "" : promotion.getStartDate().toString(),
                promotion.getEndDate() == null ? "" : promotion.getEndDate().toString(),
                effectiveness,
                status);
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
