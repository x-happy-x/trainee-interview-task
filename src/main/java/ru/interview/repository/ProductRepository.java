package ru.interview.repository;

import org.springframework.stereotype.Repository;
import ru.interview.entity.Product;
import ru.interview.exception.ProductNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class ProductRepository {

    private final AtomicReference<Long> lastKey = new AtomicReference<>(1L);
    private final Map<Long, Product> products;

    public ProductRepository() {
        this.products = new HashMap<>();
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Product findById(Long id) throws ProductNotFoundException {
        if (!products.containsKey(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        return products.get(id);
    }

    public void save(Product product) {
        if (product.getId() == null) {
            product.setId(lastKey.getAndSet(lastKey.get() + 1));
        }
        products.put(product.getId(), product);
    }

    public void delete(Product product) throws ProductNotFoundException {
        if (!products.containsKey(product.getId())) {
            throw new ProductNotFoundException("Product with id " + product.getId() + " not found");
        }
        products.remove(product.getId());
    }
}
