package ru.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.interview.entity.ProductSupply;
import ru.interview.exception.ProductValidationException;
import ru.interview.repository.ProductSupplyRepository;

import java.util.List;
import java.util.Set;

import ru.interview.entity.Product;
import ru.interview.exception.ProductNotFoundException;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Service
public class ProductSupplyService {

    private final ProductSupplyRepository productSupplyRepository;
    private final ProductService productService;
    private final Validator validator;

    @Autowired
    public ProductSupplyService(
            ProductSupplyRepository productSupplyRepository,
            ProductService productService,
            Validator validator
    ) {
        this.productSupplyRepository = productSupplyRepository;
        this.productService = productService;
        this.validator = validator;
    }

    public List<ProductSupply> getAllProductSupplies() {
        return productSupplyRepository.findAll();
    }

    public ProductSupply getProductSupplyById(Long id) {
        return productSupplyRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product supply with id " + id + " not found"));
    }

    @Transactional
    public ProductSupply addProductSupply(ProductSupply productSupply) {
        Set<ConstraintViolation<ProductSupply>> violations = validator.validate(productSupply);
        if (!violations.isEmpty()) {
            throw new ProductValidationException(violations);
        }
        productSupply.setId(null);

        Product product = productService.getProductById(productSupply.getProduct().getId());
        product.setInStock(true);
        productSupply.setProduct(product);
        return productSupplyRepository.save(productSupply);
    }

    @Transactional
    public ProductSupply updateProductSupply(ProductSupply productSupply) {
        ProductSupply existingProduct = productSupplyRepository.findById(productSupply.getId()).orElseThrow(
                () -> new ProductNotFoundException("Product supply with id " + productSupply.getId() + " not found"));

        if (productSupply.getDocumentName() != null) existingProduct.setDocumentName(productSupply.getDocumentName());
        if (productSupply.getQuantity() != null) existingProduct.setQuantity(productSupply.getQuantity());
        if (productSupply.getProduct().getId() != null) {
            Product product = productService.getProductById(productSupply.getProduct().getId());
            existingProduct.setProduct(product);
        }

        Set<ConstraintViolation<ProductSupply>> violations = validator.validate(existingProduct);
        if (!violations.isEmpty()) {
            throw new ProductValidationException(violations);
        }

        return productSupplyRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProductSupply(Long id) {
        if (!productSupplyRepository.existsById(id)) {
            throw new ProductNotFoundException("Product supply with id " + id + " not found");
        }
        productSupplyRepository.deleteById(id);
    }
}
