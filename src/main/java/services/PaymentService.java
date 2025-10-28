package services;

import collections.InvoiceManager;
import collections.TransactionManager;
import enums.TransactionStatus;
import models.Invoice;
import models.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentService {
    private final TransactionManager transactionManager;
    private final InvoiceManager invoiceManager;

    public PaymentService(TransactionManager transactionManager,
                          InvoiceManager invoiceManager) {
        this.transactionManager = transactionManager;
        this.invoiceManager = invoiceManager;
    }

    public Transaction processPayment(String invoiceId, enums.PaymentMethod method, BigDecimal amount) {
        Invoice invoice = invoiceManager.getById(invoiceId);
        if (invoice == null) {
            return null;
        }
        Transaction transaction = new Transaction(invoiceId, amount, method);
        transactionManager.add(transaction);
        invoice.markPaid();
        invoiceManager.update(invoice);
        transaction.markSuccess("REF" + LocalDateTime.now().toString());
        transactionManager.update(transaction);
        return transaction;
    }

    public void recordTransaction(Transaction transaction) {
        transactionManager.add(transaction);
    }

    public boolean refund(String transactionId, BigDecimal amount) {
        Transaction transaction = transactionManager.getById(transactionId);
        if (transaction == null) {
            return false;
        }
        transaction.refund(amount);
        transactionManager.update(transaction);
        return true;
    }

    public Transaction[] listTransactionsByStatus(TransactionStatus status) {
        return transactionManager.findByStatus(status);
    }
}
