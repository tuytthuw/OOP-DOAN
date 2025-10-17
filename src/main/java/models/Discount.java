package models;

import Interfaces.IEntity;
import enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class Discount implements IEntity {
    private  String discountId;
    private  String discountCode;
    private  String description;
    private DiscountType type;
    private BigDecimal value;
    private BigDecimal minAmount;
    private BigDecimal maxDiscount;
    private LocalDate startDate;
    private LocalDate endDate;
    private int usageLimit;
    private int usageCount;
    private boolean isActive;

    @Override
    public String getId() {
        return discountId;
    }

    @Override
    public void display() {
        System.out.println("ID Khuyến Mãi: " + discountId);
        System.out.println("Mã Khuyến Mãi: " + discountCode);
        System.out.println("Mô Tả: " + description);
        System.out.println("Loại Khuyến Mãi: " + type);
        System.out.println("Giá Trị: " + value);
        System.out.println("Số Tiền Tối Thiểu Áp Dụng: " + minAmount);
        System.out.println("Số Tiền Giảm Tối Đa: " + maxDiscount);
        System.out.println("Ngày Bắt Đầu: " + startDate);
        System.out.println("Ngày Kết Thúc: " + endDate);
        System.out.println("Giới Hạn Sử Dụng: " + (usageLimit == -1 ? "Không Giới Hạn" : usageLimit));
        System.out.println("Số Lần Đã Sử Dụng: " + usageCount);
        System.out.println("Trạng Thái Hoạt Động: " + (isActive ? "Hoạt Động" : "Không Hoạt Động"));
    }

    @Override
    public void input() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập Mã Khuyến Mãi: ");
        discountCode = scanner.nextLine();
        System.out.print("Nhập Mô Tả: ");
        description = scanner.nextLine();
        System.out.print("Nhập Loại Khuyến Mãi (PERCENTAGE/FIXED_AMOUNT): ");
        type = DiscountType.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Nhập Giá Trị: ");
        value = new BigDecimal(scanner.nextLine());
        System.out.print("Nhập Số Tiền Tối Thiểu Áp Dụng: ");
        minAmount = new BigDecimal(scanner.nextLine());
        System.out.print("Nhập Số Tiền Giảm Tối Đa (hoặc để trống nếu không có): ");
        String maxDiscountInput = scanner.nextLine();
        maxDiscount = maxDiscountInput.isEmpty() ? null : new BigDecimal(maxDiscountInput);
        System.out.print("Nhập Ngày Bắt Đầu (YYYY-MM-DD): ");
        startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Nhập Ngày Kết Thúc (YYYY-MM-DD): ");
        endDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Nhập Giới Hạn Sử Dụng (-1 cho không giới hạn): ");
        usageLimit = Integer.parseInt(scanner.nextLine());
        usageCount = 0;
        isActive = true;
    }

    @Override
    public String getPrefix(){
        return "DSC";
    }

    public Discount() {
    }

    public Discount(String discountId, String discountCode, String description, DiscountType type, BigDecimal value, BigDecimal minAmount, BigDecimal maxDiscount, LocalDate startDate, LocalDate endDate, int usageLimit, int usageCount, boolean isActive) {
        this.discountId = discountId;
        this.discountCode = discountCode;
        this.description = description;
        this.type = type;
        this.value = value;
        this.minAmount = BigDecimal.ZERO;
        this.maxDiscount = null;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = -1;
        this.usageCount = 0;
        this.isActive = true;
    }

    // Tính toán số tiền giảm giá dựa trên loại chiết khấu và các điều kiện liên quan.
    public BigDecimal calculateDiscount(BigDecimal originalAmount) {
        //nếu chiết khấu không thể sử dụng hoặc số tiền gốc nhỏ hơn số tiền tối thiểu để áp dụng chiết khấu, trả về 0
        if (!canUse() || originalAmount.compareTo(minAmount) < 0) {
            return BigDecimal.ZERO;
        }

        // Tính toán số tiền giảm giá dựa trên loại chiết khấu
        BigDecimal discountAmount = BigDecimal.ZERO;

        //Nếu là PERCENTAGE, tính toán dựa trên phần trăm và kiểm tra maxDiscount.
        //Nếu là FIXED_AMOUNT, trả về giá trị cố định nhưng phải đảm bảo số tiền gốc lớn hơn minAmount.
        if (type == DiscountType.PERCENTAGE) {
            discountAmount = originalAmount.multiply(value.divide(new BigDecimal("100")));
            if (maxDiscount != null && discountAmount.compareTo(maxDiscount) > 0) {
                discountAmount = maxDiscount;
            }
        } else if (type == DiscountType.FIXED_AMOUNT) {
            discountAmount = value;
        }

        // Đảm bảo số tiền giảm giá không lớn hơn số tiền gốc
        return discountAmount.compareTo(originalAmount) > 0 ? originalAmount : discountAmount;
    }

    //Kiểm tra xem một ngày cụ thể có nằm trong khoảng thời gian hiệu lực của chiết khấu hay không.
    public boolean isValidForDate(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    //Kiểm tra xem chiết khấu có thể được sử dụng hay không dựa trên trạng thái hoạt động và giới hạn sử dụng.
    public boolean canUse() {
        if (!isActive || !isValidForDate(LocalDate.now())) {
            return false;
        }
        if (usageLimit != -1 && usageCount >= usageLimit) {
            return false;
        }
        return true;
    }

    //Tăng số lần sử dụng chiết khấu lên 1 nếu chưa đạt đến giới hạn sử dụng.
    public void  incrementUsageCount() {
        if (usageLimit == -1 || usageCount < usageLimit) {
            usageCount++;
        }
    }




}
