package collections;

import models.Promotion;

public class PromotionManager extends BaseManager<Promotion> {
    private static final int DEFAULT_CAPACITY = 40;

    public PromotionManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected Promotion[] createArray(int size) {
        return new Promotion[size];
    }

    public Promotion[] findActive() {
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Promotion promotion = items[i];
            if (promotion != null && promotion.isActive()) {
                matches++;
            }
        }
        Promotion[] result = new Promotion[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Promotion promotion = items[i];
            if (promotion != null && promotion.isActive()) {
                result[index++] = promotion;
            }
        }
        return result;
    }

    public Promotion findByCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            Promotion promotion = items[i];
            if (promotion != null && code.equalsIgnoreCase(promotion.getCode())) {
                return promotion;
            }
        }
        return null;
    }

    public Promotion[] findUpcoming(int daysAhead) {
        if (daysAhead < 0) {
            daysAhead = 0;
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Promotion promotion = items[i];
            if (promotion != null && promotion.getValidFrom() != null) {
                matches++;
            }
        }
        Promotion[] result = new Promotion[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Promotion promotion = items[i];
            if (promotion != null && promotion.getValidFrom() != null) {
                result[index++] = promotion;
            }
        }
        return result;
    }
}
