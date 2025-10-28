package collections;

import enums.ServiceCategory;
import models.Service;

public class ServiceManager extends BaseManager<Service> {
    private static final int DEFAULT_CAPACITY = 40;

    public ServiceManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected Service[] createArray(int size) {
        return new Service[size];
    }

    public Service[] listByCategory(ServiceCategory category) {
        if (category == null) {
            return new Service[0];
        }
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Service service = items[i];
            if (service != null && category == service.getCategory()) {
                matches++;
            }
        }
        Service[] result = new Service[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Service service = items[i];
            if (service != null && category == service.getCategory()) {
                result[index++] = service;
            }
        }
        return result;
    }

    public Service[] searchByName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new Service[0];
        }
        String lowerKeyword = keyword.toLowerCase();
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Service service = items[i];
            if (service != null && service.getServiceName() != null
                    && service.getServiceName().toLowerCase().contains(lowerKeyword)) {
                matches++;
            }
        }
        Service[] result = new Service[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Service service = items[i];
            if (service != null && service.getServiceName() != null
                    && service.getServiceName().toLowerCase().contains(lowerKeyword)) {
                result[index++] = service;
            }
        }
        return result;
    }

    public Service[] findActiveServices() {
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Service service = items[i];
            if (service != null && service.isActive()) {
                matches++;
            }
        }
        Service[] result = new Service[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Service service = items[i];
            if (service != null && service.isActive()) {
                result[index++] = service;
            }
        }
        return result;
    }
}
