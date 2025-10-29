package com.spa.model;

import com.spa.data.DataStore;
import com.spa.interfaces.IEntity;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Hóa đơn cho dịch vụ và sản phẩm.
 */
public class Invoice implements IEntity {
    private static final String PREFIX = "INV";

    private String invoiceId;
    private Customer customer;
    private Receptionist receptionist;
    private Appointment appointment;
    private Promotion promotion;
    private DataStore<Product> productList;
    private double totalAmount;
    private boolean status;
    private double taxRate;
    private double serviceChargeRate;

    public Invoice() {
        this("", null, null, null, null, new DataStore<>(Product.class),
                0.0, false, 0.0, 0.0);
    }

    public Invoice(String invoiceId,
                   Customer customer,
                   Receptionist receptionist,
                   Appointment appointment,
                   Promotion promotion,
                   DataStore<Product> productList,
                   double totalAmount,
                   boolean status,
                   double taxRate,
                   double serviceChargeRate) {
        this.invoiceId = invoiceId;
        this.customer = customer;
        this.receptionist = receptionist;
        this.appointment = appointment;
        this.promotion = promotion;
        this.productList = productList;
        this.totalAmount = totalAmount;
        this.status = status;
        this.taxRate = taxRate;
        this.serviceChargeRate = serviceChargeRate;
    }

    @Override
    public String getId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Receptionist getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Receptionist receptionist) {
        this.receptionist = receptionist;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public DataStore<Product> getProductList() {
        return productList;
    }

    public void setProductList(DataStore<Product> productList) {
        this.productList = productList;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getServiceChargeRate() {
        return serviceChargeRate;
    }

    public void setServiceChargeRate(double serviceChargeRate) {
        this.serviceChargeRate = serviceChargeRate;
    }

    @Override
    public void display() {
        System.out.println("---------------- HÓA ĐƠN DỊCH VỤ ----------------");
        System.out.printf("Mã hóa đơn     : %s%n", invoiceId);
        System.out.printf("Khách hàng     : %s%n", customer == null ? "" : customer.getFullName());
        if (customer != null) {
            System.out.printf("SĐT khách      : %s%n", customer.getPhoneNumber() == null ? "" : customer.getPhoneNumber());
        }
        System.out.printf("Lễ tân lập     : %s%n", receptionist == null ? "" : receptionist.getFullName());
        System.out.printf("Lịch hẹn       : %s%n", appointment == null ? "" : appointment.getId());
        System.out.printf("Khuyến mãi     : %s%n", promotion == null ? "(không)" : promotion.getName());
        System.out.printf("Thuế (%%)       : %.2f%n", taxRate * 100);
        System.out.printf("Phí dịch vụ(%%) : %.2f%n", serviceChargeRate * 100);
        if (appointment != null && appointment.getService() != null) {
            System.out.println("--- Dịch vụ ---");
            System.out.printf("%s | %s | Giá: %.2f%n",
                    appointment.getService().getId(),
                    appointment.getService().getServiceName(),
                    appointment.getService().calculateFinalPrice());
        }
        System.out.println("--- Sản phẩm ---");
        if (productList == null || productListSize() == 0) {
            System.out.println("(không có sản phẩm)");
        } else {
            Product[] products = productList.getAll();
            for (Product product : products) {
                if (product == null) {
                    continue;
                }
                System.out.printf("%s | %s | SL: %d | Thành tiền: %.2f%n",
                        product.getId(),
                        product.getProductName(),
                        product.getStockQuantity(),
                        product.calculateFinalPrice());
            }
        }
        double subtotal = calculateSubtotal();
        double discount = promotion == null ? 0.0 : promotion.calculateDiscount(subtotal);
        double taxed = (subtotal - discount) * taxRate;
        double serviceCharge = (subtotal - discount) * serviceChargeRate;
        double grandTotal = subtotal - discount + taxed + serviceCharge;
        System.out.println("--- Tóm tắt ---");
        System.out.printf("Tạm tính       : %.2f%n", subtotal);
        System.out.printf("Giảm giá       : %.2f%n", discount);
        System.out.printf("Thuế           : %.2f%n", taxed);
        System.out.printf("Phí dịch vụ    : %.2f%n", serviceCharge);
        System.out.printf("Tổng cộng      : %.2f%n", grandTotal);
        System.out.printf("Trạng thái     : %s%n", status ? "ĐÃ THANH TOÁN" : "CHƯA THANH TOÁN");
        System.out.println("-------------------------------------------------");
    }

    private double calculateSubtotal() {
        double serviceAmount = appointment == null || appointment.getService() == null
                ? 0.0
                : appointment.getService().calculateFinalPrice().doubleValue();
        double productAmount = 0.0;
        if (productList != null) {
            Product[] items = productList.getAll();
            for (Product item : items) {
                if (item != null) {
                    productAmount += item.calculateFinalPrice().doubleValue();
                }
            }
        }
        return serviceAmount + productAmount;
    }

    private int productListSize() {
        Product[] items = productList == null ? null : productList.getAll();
        if (items == null) {
            return 0;
        }
        int size = 0;
        for (Product item : items) {
            if (item != null) {
                size++;
            }
        }
        return size;
    }

    @Override
    public void input() {
        // Sẽ được xử lý ở tầng UI.
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Thêm sản phẩm vào hóa đơn.
     *
     * @param item sản phẩm cần thêm
     */
    public void addProduct(Product item) {
        if (productList == null) {
            productList = new DataStore<>(Product.class);
        }
        productList.add(item);
    }

    /**
     * Tính tổng tiền dựa trên dịch vụ và sản phẩm.
     *
     * @return tổng tiền sau thuế và phí
     */
    public double calculateTotal() {
        double serviceAmount = appointment == null || appointment.getService() == null
                ? 0.0
                : appointment.getService().calculateFinalPrice().doubleValue();
        double productAmount = 0.0;
        if (productList != null) {
            Product[] items = productList.getAll();
            for (Product item : items) {
                if (item != null) {
                    productAmount += item.calculateFinalPrice().doubleValue();
                }
            }
        }
        double subtotal = serviceAmount + productAmount;
        double discount = promotion != null ? promotion.calculateDiscount(subtotal) : 0.0;
        double afterDiscount = subtotal - discount;
        double taxAmount = afterDiscount * taxRate;
        double serviceCharge = afterDiscount * serviceChargeRate;
        totalAmount = afterDiscount + taxAmount + serviceCharge;
        return totalAmount;
    }

    /**
     * Áp dụng lại thuế và phí cho tổng tiền hiện tại.
     */
    public void applyTaxAndCharge() {
        double afterDiscount = totalAmount;
        double taxAmount = afterDiscount * taxRate;
        double serviceCharge = afterDiscount * serviceChargeRate;
        totalAmount = afterDiscount + taxAmount + serviceCharge;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id='" + invoiceId + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) obj;
        return Objects.equals(invoiceId, other.invoiceId);
    }
}
