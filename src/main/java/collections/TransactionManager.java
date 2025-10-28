package collections;

import enums.TransactionStatus;
import models.Transaction;

public class TransactionManager extends BaseManager<Transaction> {
    private static final int DEFAULT_CAPACITY = 100;

    public TransactionManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected Transaction[] createArray(int size) {
        return new Transaction[size];
    }

    public Transaction[] findByInvoice(String invoiceId) {
        if (invoiceId == null || invoiceId.isEmpty()) {
            return new Transaction[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Transaction transaction = items[i];
            if (transaction != null && invoiceId.equals(transaction.getInvoiceId())) {
                matches++;
            }
        }
        Transaction[] result = new Transaction[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Transaction transaction = items[i];
            if (transaction != null && invoiceId.equals(transaction.getInvoiceId())) {
                result[index++] = transaction;
            }
        }
        return result;
    }

    public Transaction[] findByStatus(TransactionStatus status) {
        if (status == null) {
            return new Transaction[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Transaction transaction = items[i];
            if (transaction != null && status == transaction.getStatus()) {
                matches++;
            }
        }
        Transaction[] result = new Transaction[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Transaction transaction = items[i];
            if (transaction != null && status == transaction.getStatus()) {
                result[index++] = transaction;
            }
        }
        return result;
    }

    public Transaction[] findRecent(java.time.LocalDateTime from) {
        if (from == null) {
            return new Transaction[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Transaction transaction = items[i];
            if (transaction != null && transaction.getProcessedAt() != null
                    && !transaction.getProcessedAt().isBefore(from)) {
                matches++;
            }
        }
        Transaction[] result = new Transaction[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Transaction transaction = items[i];
            if (transaction != null && transaction.getProcessedAt() != null
                    && !transaction.getProcessedAt().isBefore(from)) {
                result[index++] = transaction;
            }
        }
        return result;
    }
}
