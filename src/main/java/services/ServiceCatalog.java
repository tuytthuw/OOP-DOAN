package services;

import collections.ServiceManager;
import models.Service;

public class ServiceCatalog {
    private final ServiceManager serviceManager;

    public ServiceCatalog(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public Service[] listAll() {
        return serviceManager.getAll();
    }

    public Service[] listByCategory(enums.ServiceCategory category) {
        return serviceManager.listByCategory(category);
    }

    public Service[] search(String keyword) {
        return serviceManager.searchByName(keyword);
    }
}
