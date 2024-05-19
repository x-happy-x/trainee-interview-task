package ru.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.interview.entity.Product;
import ru.interview.exception.ProductNotFoundException;
import ru.interview.exception.ProductValidationException;
import ru.interview.repository.ProductRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final Validator validator;

    @Autowired
    public ProductService(ProductRepository productRepository, Validator validator) {
        this.productRepository = productRepository;
        this.validator = validator;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    public Product addProduct(Product product) {
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        if (!violations.isEmpty()) {
            throw new ProductValidationException(violations);
        }
        product.setId(null);
        if (product.getPrice() == null) product.setPrice(0.0);
        if (product.getInStock() == null) product.setInStock(false);
        if (product.getDescription() == null) product.setDescription("");
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(
                () -> new ProductNotFoundException("Product with id " + product.getId() + " not found"));
        if (product.getName() != null) existingProduct.setName(product.getName());
        if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) existingProduct.setPrice(product.getPrice());
        if (product.getInStock() != null) existingProduct.setInStock(product.getInStock());
        Set<ConstraintViolation<Product>> violations = validator.validate(existingProduct);
        if (!violations.isEmpty()) {
            throw new ProductValidationException(violations);
        }
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String name, Double minPrice, Double maxPrice, Boolean inStock, Pageable pageable) {
        if (pageable == null) {
            return productRepository.findByFilters(name, minPrice, maxPrice, inStock);
        }
        Page<Product> page = productRepository.findByFiltersAndSort(name, minPrice, maxPrice, inStock, pageable);
        return page.getContent();
    }
}