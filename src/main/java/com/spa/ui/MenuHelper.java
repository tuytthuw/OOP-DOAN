package com.spa.ui;

import com.spa.model.enums.DiscountType;
import com.spa.model.enums.PaymentMethod;
import com.spa.model.enums.ServiceCategory;
import com.spa.model.enums.Tier;
import com.spa.service.Validation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Cung cấp các hàm tiện ích dùng chung cho menu.
 */
public final class MenuHelper {
    private MenuHelper() {
    }

    public static Tier selectTier() {
        System.out.println("Chọn hạng thành viên:");
        Tier[] tiers = Tier.values();
        for (int i = 0; i < tiers.length; i++) {
            System.out.println((i + 1) + ". " + tiers[i]);
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, tiers.length);
        return tiers[selected - 1];
    }

    public static ServiceCategory selectServiceCategory() {
        System.out.println("Chọn nhóm dịch vụ:");
        ServiceCategory[] categories = ServiceCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, categories.length);
        return categories[selected - 1];
    }

    public static DiscountType selectDiscountType() {
        System.out.println("Chọn kiểu khuyến mãi:");
        DiscountType[] types = DiscountType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, types.length);
        return types[selected - 1];
    }

    public static PaymentMethod selectPaymentMethod() {
        System.out.println("Chọn phương thức thanh toán:");
        PaymentMethod[] methods = PaymentMethod.values();
        for (int i = 0; i < methods.length; i++) {
            System.out.println((i + 1) + ". " + methods[i]);
        }
        int selected = Validation.getInt("Lựa chọn: ", 1, methods.length);
        return methods[selected - 1];
    }

    public static String hashPassword(String raw) {
        if (raw == null) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte value : hash) {
                String hex = Integer.toHexString(0xff & value);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            return raw;
        }
    }
}
