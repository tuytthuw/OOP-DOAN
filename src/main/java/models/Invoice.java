package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import interfaces.IEntity;
import enums.PaymentMethod;

/**
 * Lớp Invoice đại diện cho hóa đơn chi tiết cho các lịch hẹn.
 * Tổng hợp thông tin dịch vụ, chiết khấu, thuế và tính toán tổng tiền cuối
 * cùng.
 * Implement IEntity để quản lý thông tin entity chung.
 */
public class Invoice implements IEntity {
    // ============ Thuộc tính (Attributes) ============
    private String invoiceId; // ID duy nhất, định dạng: INV_XXXXX
    private String appointmentId; // ID lịch hẹn liên quan
    private String customerId; // ID khách hàng
    private LocalDate issueDate; // Ngày phát hành hóa đơn
    private BigDecimal subtotal; // Tổng tiền trước chiết khấu và thuế
    private BigDecimal discountAmount; // Số tiền chiết khấu áp dụng
    private BigDecimal taxAmount; // Số tiền thuế
    private BigDecimal totalAmount; // Tổng tiền cuối cùng
    private boolean isPaid; // Đã thanh toán hay chưa
    private LocalDate paidDate; // Ngày thanh toán (có thể null)
    private PaymentMethod paymentMethod; // Phương thức thanh toán
    private String discountCode; // Mã chiết khấu áp dụng (có thể null)
    private String notes; // Ghi chú bổ sung

    // ============ Hằng số (Constants) ============
    public static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal("10"); // 10%

    // ============ Constructor ============
    public Invoice() {
    }

    /**
     * Constructor khởi tạo Invoice với đầy đủ thông tin.
     */
    public Invoice(String invoiceId, String appointmentId, String customerId,
            BigDecimal subtotal, PaymentMethod paymentMethod) {
        this.invoiceId = invoiceId;
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.issueDate = LocalDate.now();
        this.subtotal = subtotal;
        this.discountAmount = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.totalAmount = subtotal;
        this.isPaid = false;
        this.paidDate = null;
        this.paymentMethod = paymentMethod;
        this.discountCode = null;
        this.notes = null;
    }

    // ============ Phương thức chính ============

    /**
     * Áp dụng chiết khấu cho hóa đơn.
     *
     * @param discountAmount Số tiền chiết khấu
     * @param discountCode   Mã chiết khấu
     */
    public void applyDiscount(BigDecimal discountAmount, String discountCode) {
        // Kiểm tra số tiền chiết khấu không vượt quá subtotal
        if (discountAmount.compareTo(subtotal) > 0) {
            System.out.println("⚠️ Số tiền chiết khấu không được vượt quá tổng tiền hàng!");
            return;
        }
        this.discountAmount = discountAmount;
        this.discountCode = discountCode;
        calculateTotal();
    }

    /**
     * Tính thuế dựa trên tỷ lệ thuế.
     *
     * @param taxRate Tỷ lệ thuế (%)
     */
    public void calculateTax(BigDecimal taxRate) {
        // Cơ sở tính thuế = subtotal - discountAmount
        BigDecimal taxBase = subtotal.subtract(discountAmount);
        // Tính thuế = taxBase * (taxRate / 100)
        this.taxAmount = taxBase.multiply(taxRate.divide(new BigDecimal("100")));
        calculateTotal();
    }

    /**
     * Tính tổng tiền cuối cùng.
     */
    public void calculateTotal() {
        // totalAmount = subtotal - discountAmount + taxAmount
        this.totalAmount = subtotal.subtract(discountAmount).add(taxAmount);
        // Đảm bảo tổng tiền không âm
        if (this.totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            this.totalAmount = BigDecimal.ZERO;
        }
    }

    /**
     * Đánh dấu hóa đơn đã thanh toán.
     *
     * @param paidDate Ngày thanh toán
     */
    public void markAsPaid(LocalDate paidDate) {
        if (paidDate.isAfter(LocalDate.now())) {
            System.out.println("⚠️ Ngày thanh toán không được trong tương lai!");
            return;
        }
        this.isPaid = true;
        this.paidDate = paidDate;
    }

    /**
     * Trả về hóa đơn định dạng để in.
     */
    public String getFormattedInvoice() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(
                "╔════════════════════════════════════════════╗\n" +
                        "║           HÓA ĐƠN DỊCH VỤ SPA            ║\n" +
                        "╠════════════════════════════════════════════╣\n" +
                        "║ Số hóa đơn:    %-32s║\n" +
                        "║ Khách hàng:    %-32s║\n" +
                        "║ Lịch hẹn:      %-32s║\n" +
                        "║ Ngày phát hành:%-32s║\n" +
                        "╠════════════════════════════════════════════╣\n" +
                        "║ Tổng tiền:     %15,.0f VND    ║\n" +
                        "║ Chiết khấu:   -%15,.0f VND    ║\n" +
                        "║ Thuế (10%):    %15,.0f VND    ║\n" +
                        "╠════════════════════════════════════════════╣\n" +
                        "║ Tổng cộng:     %15,.0f VND    ║\n" +
                        "║ Trạng thái:    %-32s║\n" +
                        "║ Phương thức:   %-32s║\n" +
                        (paidDate != null ? "║ Ngày thanh toán:%-31s║\n" : "") +
                        "╚════════════════════════════════════════════╝\n",
                invoiceId,
                customerId,
                appointmentId,
                issueDate.format(formatter),
                subtotal,
                discountAmount,
                taxAmount,
                totalAmount,
                isPaid ? "Đã thanh toán" : "Chưa thanh toán",
                paymentMethod,
                paidDate != null ? paidDate.format(formatter) : "");
    }

    // ============ IEntity Implementation ============

    @Override
    public String getId() {
        return invoiceId;
    }

    @Override
    public void display() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ ID: " + getId());
        System.out.println("│ Lịch hẹn: " + appointmentId);
        System.out.println("│ Khách hàng: " + customerId);
        System.out.println("│ Ngày phát hành: " + issueDate);
        System.out.println("│ Tổng tiền: " + String.format("%,.0f", subtotal) + " VND");
        System.out.println("│ Chiết khấu: " + String.format("%,.0f", discountAmount) + " VND");
        System.out.println("│ Thuế: " + String.format("%,.0f", taxAmount) + " VND");
        System.out.println("│ Tổng cộng: " + String.format("%,.0f", totalAmount) + " VND");
        System.out.println("│ Trạng thái: " + (isPaid ? "Đã thanh toán" : "Chưa thanh toán"));
        System.out.println("│ Phương thức: " + paymentMethod);
        System.out.println("│ Ngày thanh toán: " + (paidDate == null ? "Chưa thanh toán" : paidDate));
        System.out.println("└─────────────────────────────────────────────┘");
    }

    @Override
    public void input() {
        // TODO: Implement nhập thông tin hóa đơn từ người dùng
    }

    @Override
    public String getPrefix() {
        return "INV";
    }

    // ============ Getter & Setter ============

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ============ equals() & hashCode() ============

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Invoice))
            return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceId, invoice.invoiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId);
    }
}
