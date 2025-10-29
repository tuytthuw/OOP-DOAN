package com.spa.ui;

import com.spa.model.Employee;
import com.spa.model.Receptionist;
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
        Integer selected = Validation.getIntOrCancel("Lựa chọn", 1, tiers.length);
        if (selected == null) {
            return null;
        }
        return tiers[selected - 1];
    }

    public static ServiceCategory selectServiceCategory() {
        System.out.println("Chọn nhóm dịch vụ:");
        ServiceCategory[] categories = ServiceCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        Integer selected = Validation.getIntOrCancel("Lựa chọn", 1, categories.length);
        if (selected == null) {
            return null;
        }
        return categories[selected - 1];
    }

    public static DiscountType selectDiscountType() {
        System.out.println("Chọn kiểu khuyến mãi:");
        DiscountType[] types = DiscountType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        Integer selected = Validation.getIntOrCancel("Lựa chọn", 1, types.length);
        if (selected == null) {
            return null;
        }
        return types[selected - 1];
    }

    public static PaymentMethod selectPaymentMethod() {
        System.out.println("Chọn phương thức thanh toán:");
        PaymentMethod[] methods = PaymentMethod.values();
        for (int i = 0; i < methods.length; i++) {
            System.out.println((i + 1) + ". " + methods[i]);
        }
        Integer selected = Validation.getIntOrCancel("Lựa chọn", 1, methods.length);
        if (selected == null) {
            return null;
        }
        return methods[selected - 1];
    }

    public static Receptionist toReceptionistView(Employee employee) {
        if (employee == null) {
            return null;
        }
        Receptionist receptionist = new Receptionist();
        receptionist.setPersonId(employee.getId());
        receptionist.setFullName(employee.getFullName());
        receptionist.setPhoneNumber(employee.getPhoneNumber());
        receptionist.setEmail(employee.getEmail());
        receptionist.setAddress(employee.getAddress());
        receptionist.setMale(employee.isMale());
        receptionist.setBirthDate(employee.getBirthDate());
        receptionist.setHireDate(employee.getHireDate());
        receptionist.setSalary(employee.getSalary());
        receptionist.setDeleted(false);
        receptionist.setMonthlyBonus(0.0);
        return receptionist;
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
