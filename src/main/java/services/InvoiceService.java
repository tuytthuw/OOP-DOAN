package services;

import java.math.BigDecimal;

import collections.AppointmentManager;
import collections.DiscountManager;
import collections.InvoiceManager;
import collections.ServiceManager;
import enums.PaymentMethod;
import exceptions.BusinessLogicException;
import exceptions.EntityNotFoundException;
import exceptions.InvalidEntityException;
import models.Appointment;
import models.Discount;
import models.Invoice;
import models.Service;

/**
 * Service quản lý logic nghiệp vụ hóa đơn.
 * Xử lý tạo hóa đơn, áp dụng chiết khấu, tính toán tiền và chi tiết hóa đơn.
 * Throws exceptions thay vì return false hoặc in error message.
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
     * @return Hóa đơn vừa tạo
     * @throws EntityNotFoundException nếu lịch hẹn hoặc dịch vụ không tồn tại
     * @throws BusinessLogicException  nếu vi phạm business rule
     */
    public Invoice createInvoiceForAppointment(String appointmentId, PaymentMethod paymentMethod)
            throws EntityNotFoundException, BusinessLogicException {

        try {
            // Lấy lịch hẹn
            Appointment appointment = appointmentManager.getById(appointmentId);

            // Lấy dịch vụ từ lịch hẹn
            Service service = serviceManager.getById(appointment.getServiceId());

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
            invoiceManager.add(invoice);
            return invoice;

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (InvalidEntityException | exceptions.EntityAlreadyExistsException e) {
            throw new BusinessLogicException(
                    "tạo hóa đơn",
                    e.getErrorMessage());
        }
    }

    /**
     * Áp dụng chiết khấu cho hóa đơn.
     *
     * @param invoiceId    ID hóa đơn
     * @param discountCode Mã chiết khấu
     * @return true nếu áp dụng thành công
     * @throws EntityNotFoundException nếu hóa đơn không tồn tại
     * @throws BusinessLogicException  nếu vi phạm business rule
     */
    public boolean applyDiscountToInvoice(String invoiceId, String discountCode)
            throws EntityNotFoundException, BusinessLogicException {

        try {
            // Lấy hóa đơn
            Invoice invoice = invoiceManager.getById(invoiceId);

            // Tìm chiết khấu theo mã
            Discount discount = discountManager.findByCode(discountCode);
            if (discount == null) {
                throw new BusinessLogicException(
                        "áp dụng chiết khấu",
                        "Mã chiết khấu '" + discountCode + "' không tồn tại");
            }

            // Kiểm tra chiết khấu có hợp lệ không
            if (!discount.canUse()) {
                throw new BusinessLogicException(
                        "áp dụng chiết khấu",
                        "Mã chiết khấu '" + discountCode + "' không còn hiệu lực");
            }

            // Tính tiền chiết khấu
            BigDecimal discountAmount = discount.calculateDiscount(invoice.getSubtotal());

            // Áp dụng chiết khấu vào hóa đơn
            invoice.applyDiscount(discountAmount, discountCode);

            // Cập nhật hóa đơn
            invoiceManager.update(invoice);

            // Tăng lượt sử dụng chiết khấu
            discount.incrementUsage();
            discountManager.update(discount);

            return true;

        } catch (EntityNotFoundException | BusinessLogicException e) {
            throw e;
        } catch (InvalidEntityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tính tổng tiền cuối cùng của hóa đơn.
     *
     * @param invoiceId ID hóa đơn
     * @return Tổng tiền cuối cùng
     * @throws EntityNotFoundException nếu hóa đơn không tồn tại
     */
    public BigDecimal calculateFinalAmount(String invoiceId) throws EntityNotFoundException {
        try {
            Invoice invoice = invoiceManager.getById(invoiceId);

            // Tính lại tổng tiền
            invoice.calculateTotal();
            return invoice.getTotalAmount();

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (InvalidEntityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lấy chi tiết hóa đơn dưới dạng chuỗi định dạng.
     *
     * @param invoiceId ID hóa đơn
     * @return Chuỗi chứa chi tiết hóa đơn đã định dạng
     * @throws EntityNotFoundException nếu hóa đơn không tồn tại
     */
    public String getInvoiceDetails(String invoiceId) throws EntityNotFoundException {
        try {
            Invoice invoice = invoiceManager.getById(invoiceId);

            return invoice.getFormattedInvoice();

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (InvalidEntityException e) {
            throw new RuntimeException(e);
        }
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
