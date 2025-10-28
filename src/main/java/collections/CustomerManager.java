package collections;

import enums.Tier;
import models.Customer;

public class CustomerManager extends BaseManager<Customer> {
    private static final int DEFAULT_CAPACITY = 50;

    public CustomerManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected Customer[] createArray(int size) {
        return new Customer[size];
    }

    public Customer[] findByTier(Tier tier) {
        if (tier == null) {
            return new Customer[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Customer customer = items[i];
            if (customer != null && tier == customer.getTier()) {
                matches++;
            }
        }
        Customer[] result = new Customer[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Customer customer = items[i];
            if (customer != null && tier == customer.getTier()) {
                result[index++] = customer;
            }
        }
        return result;
    }

    public Customer findByPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            Customer customer = items[i];
            if (customer != null && phone.equals(customer.getPhoneNumber())) {
                return customer;
            }
        }
        return null;
    }

    public Customer[] findActiveLoyaltyCustomers(int minPoints) {
        if (minPoints <= 0) {
            minPoints = 1;
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Customer customer = items[i];
            if (customer != null && !customer.isDeleted() && customer.getLoyaltyPoints() >= minPoints) {
                matches++;
            }
        }
        Customer[] result = new Customer[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Customer customer = items[i];
            if (customer != null && !customer.isDeleted() && customer.getLoyaltyPoints() >= minPoints) {
                result[index++] = customer;
            }
        }
        return result;
    }
}
