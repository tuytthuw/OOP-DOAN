package services;

import collections.InvoiceManager;
import collections.ProductManager;
import collections.PromotionManager;
import collections.ServiceManager;
import enums.DiscountType;
import interfaces.Sellable;
import models.Invoice;
import models.InvoiceItem;
import models.Product;
import models.Promotion;
import models.Service;

import java.math.BigDecimal;

public class InvoiceService {
    private final InvoiceManager invoiceManager;
    private final ProductManager productManager;
    private final ServiceManager serviceManager;
    private final PromotionManager promotionManager;

    public InvoiceService(InvoiceManager invoiceManager,
                          ProductManager productManager,
                          ServiceManager serviceManager,
                          PromotionManager promotionManager) {
        this.invoiceManager = invoiceManager;
        this.productManager = productManager;
        this.serviceManager = serviceManager;
        this.promotionManager = promotionManager;
    }

    public Invoice createInvoice(String customerId, String appointmentId) {
        Invoice invoice = new Invoice(customerId, appointmentId);
        invoiceManager.add(invoice);
        return invoice;
    }

    public boolean addSellable(String invoiceId, Sellable item, int quantity) {
        Invoice invoice = invoiceManager.getById(invoiceId);
        if (invoice == null || item == null || quantity <= 0) {
            return false;
        }
        BigDecimal price = item.getPrice();
        InvoiceItem invoiceItem = new InvoiceItem(item.getDescription(), item.getClass().getSimpleName(), quantity, price, BigDecimal.ZERO);
        invoice.addItem(invoiceItem);
        invoiceManager.update(invoice);
        return true;
    }

    public boolean addProductById(String invoiceId, String productId, int quantity) {
        Product product = productManager.getById(productId);
        if (product == null) {
            return false;
        }
        return addSellable(invoiceId, product, quantity);
    }

    public boolean addServiceById(String invoiceId, String serviceId, int quantity) {
        Service service = serviceManager.getById(serviceId);
        if (service == null) {
            return false;
        }
        return addSellable(invoiceId, service, quantity);
    }

    public boolean applyPromotion(String invoiceId, String code) {
        Invoice invoice = invoiceManager.getById(invoiceId);
        if (invoice == null) {
            return false;
        }
        Promotion promotion = promotionManager.findByCode(code);
        if (promotion == null) {
            return false;
        }
        invoice.applyPromotion(promotion.getType(), promotion.getValue());
        invoiceManager.update(invoice);
        return true;
    }

    public void removeItem(String invoiceId, String sellableId) {
        Invoice invoice = invoiceManager.getById(invoiceId);
        if (invoice == null) {
            return;
        }
        invoice.removeItem(sellableId);
        invoiceManager.update(invoice);
    }

    public void finalizeInvoice(String invoiceId) {
        Invoice invoice = invoiceManager.getById(invoiceId);
        if (invoice == null) {
            return;
        }
        invoice.calculateTotals();
        invoiceManager.update(invoice);
    }

    public Invoice getInvoice(String invoiceId) {
        return invoiceManager.getById(invoiceId);
    }

    public Invoice[] getAllInvoices() {
        return invoiceManager.getAll();
    }
}
