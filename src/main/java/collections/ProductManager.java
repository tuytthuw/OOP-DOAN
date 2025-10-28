package collections;

import models.Product;

public class ProductManager extends BaseManager<Product> {
    private static final int DEFAULT_CAPACITY = 60;

    public ProductManager() {
        super(DEFAULT_CAPACITY);
    }

    @Override
    protected Product[] createArray(int size) {
        return new Product[size];
    }

    public Product[] findLowStock(int threshold) {
        int limit = threshold < 0 ? 0 : threshold;
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Product product = items[i];
            if (product != null && product.getStockQuantity() <= limit) {
                matches++;
            }
        }
        Product[] result = new Product[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Product product = items[i];
            if (product != null && product.getStockQuantity() <= limit) {
                result[index++] = product;
            }
        }
        return result;
    }

    public Product[] findByName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new Product[0];
        }
        String lowerKeyword = keyword.toLowerCase();
        int matches = 0;
        for (int i = 0; i < count; i++) {
            Product product = items[i];
            if (product != null && product.getProductName() != null
                    && product.getProductName().toLowerCase().contains(lowerKeyword)) {
                matches++;
            }
        }
        Product[] result = new Product[matches];
        int index = 0;
        for (int i = 0; i < count; i++) {
            Product product = items[i];
            if (product != null && product.getProductName() != null
                    && product.getProductName().toLowerCase().contains(lowerKeyword)) {
                result[index++] = product;
            }
        }
        return result;
    }

    public boolean updateStock(String productId, int delta) {
        if (productId == null || productId.isEmpty()) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            Product product = items[i];
            if (product != null && productId.equals(product.getId())) {
                product.adjustStock(delta);
                return true;
            }
        }
        return false;
    }
}
