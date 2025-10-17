package services;

import java.math.BigDecimal;

import collections.AppointmentManager;
import collections.DiscountManager;
import collections.InvoiceManager;
import collections.ServiceManager;
import enums.PaymentMethod;
import models.Appointment;
import models.Discount;
import models.Invoice;
import models.Service;

/**
 * Service quản lý logic nghiệp vụ hóa đơn.
 * Xử lý tạo hóa đơn, áp dụng chiết khấu, tính toán tiền và chi tiết hóa đơn.
 */
public class InvoiceService {

    private InvoiceManager invoiceManager;
    private AppointmentManager appointmentManager;
    private ServiceManager serviceManager;
    private DiscountManager discountManager;

    /**
     * Constructor khởi tạo InvoiceService.
     *
     * @param invoiceManager     Manager quản lý hóa đơn
     * @param appointmentManager Manager quản lý lịch hẹn
     * @param serviceManager     Manager quản lý dịch vụ
     * @param discountManager    Manager quản lý chiết khấu
     */
    public InvoiceService(InvoiceManager invoiceManager, AppointmentManager appointmentManager,
            ServiceManager serviceManager, DiscountManager discountManager) {
        this.invoiceManager = invoiceManager;
        this.appointmentManager = appointmentManager;
        this.serviceManager = serviceManager;
        this.discountManager = discountManager;
    }

    /**
     * Tạo hóa đơn cho lịch hẹn.
     *
     * @param appointmentId ID lịch hẹn
     * @param paymentMethod Phương thức thanh toán
     * @return Hóa đơn vừa tạo hoặc null nếu thất bại
     */
    public Invoice createInvoiceForAppointment(String appointmentId, PaymentMethod paymentMethod) {
        // Lấy lịch hẹn
        Appointment appointment = appointmentManager.getById(appointmentId);
        if (appointment == null) {
            System.out.println("❌ Lịch hẹn không tồn tại!");
            return null;
        }

        // Lấy dịch vụ từ lịch hẹn
        Service service = serviceManager.getById(appointment.getServiceId());
        if (service == null) {
            System.out.println("❌ Dịch vụ không tồn tại!");
            return null;
        }

        // Lấy giá từ dịch vụ
        BigDecimal basePrice = service.getPrice();

        // Sinh ID mới
        String invoiceId = generateInvoiceId();

        // Tạo hóa đơn mới
        Invoice invoice = new Invoice(
                invoiceId,
                appointmentId,
                appointment.getCustomerId(),
                basePrice,
                paymentMethod);

        // Tính thuế (10%)
        invoice.calculateTax(new BigDecimal("10"));

        // Thêm vào Manager
        if (invoiceManager.add(invoice)) {
            System.out.println("✅ Tạo hóa đơn thành công!");
            return invoice;
        }

        System.out.println("❌ Lỗi khi tạo hóa đơn!");
        return null;
    }

    /**
     * Áp dụng chiết khấu cho hóa đơn.
     *
     * @param invoiceId    ID hóa đơn
     * @param discountCode Mã chiết khấu
     * @return true nếu áp dụng thành công, false nếu thất bại
     */
    public boolean applyDiscountToInvoice(String invoiceId, String discountCode) {
        // Lấy hóa đơn
        Invoice invoice = invoiceManager.getById(invoiceId);
        if (invoice == null) {
            System.out.println("❌ Hóa đơn không tồn tại!");
            return false;
        }

        // Tìm chiết khấu theo mã
        Discount discount = discountManager.findByCode(discountCode);
        if (discount == null) {
            System.out.println("❌ Mã chiết khấu không tồn tại!");
            return false;
        }

        // Kiểm tra chiết khấu có hợp lệ không
        if (!discount.canUse()) {
            System.out.println("❌ Mã chiết khấu không còn hiệu lực!");
            return false;
        }

        // Tính tiền chiết khấu
        BigDecimal discountAmount = discount.calculateDiscount(invoice.getSubtotal());

        // Áp dụng chiết khấu vào hóa đơn
        invoice.applyDiscount(discountAmount, discountCode);

        // Cập nhật hóa đơn
        if (invoiceManager.update(invoice)) {
            // Tăng lượt sử dụng chiết khấu
            discount.incrementUsage();
            discountManager.update(discount);

            System.out.println("✅ Áp dụng chiết khấu thành công!");
            return true;
        }

        System.out.println("❌ Lỗi khi áp dụng chiết khấu!");
        return false;
    }

    /**
     * Tính tổng tiền cuối cùng của hóa đơn.
     *
     * @param invoiceId ID hóa đơn
     * @return Tổng tiền cuối cùng hoặc null nếu hóa đơn không tồn tại
     */
    public BigDecimal calculateFinalAmount(String invoiceId) {
        Invoice invoice = invoiceManager.getById(invoiceId);
        if (invoice == null) {
            System.out.println("❌ Hóa đơn không tồn tại!");
            return null;
        }

        // Tính lại tổng tiền
        invoice.calculateTotal();
        return invoice.getTotalAmount();
    }

    /**
     * Lấy chi tiết hóa đơn dưới dạng chuỗi định dạng.
     *
     * @param invoiceId ID hóa đơn
     * @return Chuỗi chứa chi tiết hóa đơn đã định dạng
     */
    public String getInvoiceDetails(String invoiceId) {
        Invoice invoice = invoiceManager.getById(invoiceId);
        if (invoice == null) {
            System.out.println("❌ Hóa đơn không tồn tại!");
            return "";
        }

        return invoice.getFormattedInvoice();
    }

    /**
     * Sinh ID hóa đơn mới theo định dạng INV_XXXXXX.
     *
     * @return ID hóa đơn mới
     */
    private String generateInvoiceId() {
        return "INV_" + System.currentTimeMillis();
    }
}
