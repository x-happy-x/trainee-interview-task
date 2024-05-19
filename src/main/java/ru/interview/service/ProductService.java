package ru.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.interview.entity.Product;
import ru.interview.entity.validator.ProductValidator;
import ru.interview.exception.ProductNotFoundException;
import ru.interview.repository.ProductRepository;
import ru.interview.exception.ProductValidationException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id);
    }

    public void addProduct(Product product) throws ProductValidationException {
        product.setId(null);
        if (product.getPrice() == null)
            product.setPrice(0.0);
        if (product.getInStock() == null)
            product.setInStock(false);
        if (product.getDescription() == null)
            product.setDescription("");
        ProductValidator.validate(product);
        productRepository.save(product);
    }

    public void updateProduct(Product product) throws ProductValidationException, ProductNotFoundException {
        ProductValidator.validate(product);
        Product existingProduct = productRepository.findById(product.getId());
        if (product.getName() != null)
            existingProduct.setName(product.getName());
        if (product.getDescription() != null)
            existingProduct.setDescription(product.getDescription());
        if (product.getPrice() != null)
            existingProduct.setPrice(product.getPrice());
        if (product.getInStock() != null)
            existingProduct.setInStock(product.getInStock());
        productRepository.save(existingProduct);
    }

    public void deleteProduct(Product product) throws ProductNotFoundException {
        productRepository.delete(product);
    }
}

