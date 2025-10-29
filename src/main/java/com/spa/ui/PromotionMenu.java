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
            System.out.println("1. Xem danh sách khuyến mãi");
            System.out.println("2. Thêm khuyến mãi");
            System.out.println("3. Cập nhật khuyến mãi");
            System.out.println("4. Xóa khuyến mãi");
            System.out.println("0. Quay lại");

            int choice = Validation.getInt("Chọn chức năng: ", 0, 4);
            switch (choice) {
                case 1:
                    listPromotions();
                    Validation.pause();
                    break;
                case 2:
                    addPromotion();
                    Validation.pause();
                    break;
                case 3:
                    updatePromotion();
                    Validation.pause();
                    break;
                case 4:
                    deletePromotion();
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
        for (Promotion promotion : promotions) {
            if (promotion == null || promotion.isDeleted()) {
                continue;
            }
            hasData = true;
            System.out.printf("%s | %s | %s | %.2f | %s -> %s%n",
                    promotion.getId(),
                    promotion.getName(),
                    promotion.getDiscountType(),
                    promotion.getDiscountValue(),
                    formatDate(promotion.getStartDate()),
                    formatDate(promotion.getEndDate()));
        }
        if (!hasData) {
            System.out.println("Chưa có khuyến mãi nào.");
        }
    }

    private void addPromotion() {
        System.out.println();
        System.out.println("--- THÊM KHUYẾN MÃI ---");
        String id = Validation.getString("Mã khuyến mãi: ");
        if (context.getPromotionStore().findById(id) != null) {
            System.out.println("Mã khuyến mãi đã tồn tại.");
            return;
        }
        Promotion promotion = promptPromotion(id, null);
        context.getPromotionStore().add(promotion);
        context.getPromotionStore().writeFile();
        System.out.println("Đã thêm khuyến mãi thành công.");
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
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setDiscountType(updated.getDiscountType());
        existing.setDiscountValue(updated.getDiscountValue());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setMinPurchaseAmount(updated.getMinPurchaseAmount());
        context.getPromotionStore().writeFile();
        System.out.println("Đã cập nhật khuyến mãi.");
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
        String name = Validation.getString("Tên khuyến mãi: ");
        String description = Validation.getString("Mô tả ngắn: ");
        DiscountType discountType = MenuHelper.selectDiscountType();
        double discountValue = Validation.getDouble("Giá trị giảm: ", 0.0, 1_000_000_000.0);
        LocalDate startDate = Validation.getDate("Ngày bắt đầu (yyyy-MM-dd): ", DATE_FORMAT);
        LocalDate endDate = Validation.getDate("Ngày kết thúc (yyyy-MM-dd): ", DATE_FORMAT);
        double minPurchase = Validation.getDouble("Mức chi tối thiểu: ", 0.0, 1_000_000_000.0);
        Promotion promotion = new Promotion(id, name, description, discountType, discountValue,
                startDate, endDate, minPurchase, false);
        if (base != null && base.isDeleted()) {
            promotion.setDeleted(true);
        }
        return promotion;
    }

    private String formatDate(LocalDate date) {
        return date == null ? "N/A" : date.toString();
    }
}
