package services;

import collections.PromotionManager;
import enums.DiscountType;
import models.Promotion;

import java.math.BigDecimal;

public class PromotionService {
    private final PromotionManager promotionManager;

    public PromotionService(PromotionManager promotionManager) {
        this.promotionManager = promotionManager;
    }

    public void create(Promotion promotion) {
        promotionManager.add(promotion);
    }

    public void update(Promotion promotion) {
        promotionManager.update(promotion);
    }

    public void deactivate(String id) {
        Promotion promotion = promotionManager.getById(id);
        if (promotion == null) {
            return;
        }
        promotion.deactivate();
        promotionManager.update(promotion);
    }

    public boolean validateCode(String code, BigDecimal subtotal) {
        Promotion promotion = promotionManager.findByCode(code);
        if (promotion == null) {
            return false;
        }
        return promotion.isApplicable(subtotal, java.time.LocalDateTime.now());
    }

    public void markUsage(String code) {
        Promotion promotion = promotionManager.findByCode(code);
        if (promotion == null) {
            return;
        }
        promotion.incrementUsage();
        promotionManager.update(promotion);
    }
}
