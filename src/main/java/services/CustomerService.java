package services;

import collections.CustomerManager;
import enums.Tier;
import models.Customer;

import java.math.BigDecimal;

public class CustomerService {
    private final CustomerManager customerManager;

    public CustomerService(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    public void register(Customer customer) {
        customerManager.add(customer);
    }

    public void updateProfile(Customer customer) {
        customerManager.update(customer);
    }

    public void remove(String id) {
        customerManager.removeById(id);
    }

    public Customer[] getByTier(Tier tier) {
        return customerManager.findByTier(tier);
    }

    public BigDecimal calculateLifetimeValue(String id) {
        Customer customer = customerManager.getById(id);
        if (customer == null) {
            return BigDecimal.ZERO;
        }
        return customer.getTotalSpent();
    }

    public boolean redeemPoints(String id, int value) {
        Customer customer = customerManager.getById(id);
        if (customer == null) {
            return false;
        }
        int before = customer.getLoyaltyPoints();
        customer.redeemPoints(value);
        customerManager.update(customer);
        return customer.getLoyaltyPoints() < before;
    }

    public Customer[] getAllCustomers() {
        return customerManager.getAll();
    }
}
