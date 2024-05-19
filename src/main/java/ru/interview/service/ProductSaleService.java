package ru.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.interview.entity.Product;
import ru.interview.entity.ProductSale;
import ru.interview.exception.ProductNotFoundException;
import ru.interview.exception.ProductValidationException;
import ru.interview.repository.ProductSaleRepository;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class ProductSaleService {

    private final ProductSaleRepository productSaleRepository;
    private final ProductService productService;
    private final Validator validator;

    @Autowired
    public ProductSaleService(
            ProductSaleRepository productSaleRepository,
            ProductService productService,
            Validator validator
    ) {
        this.productSaleRepository = productSaleRepository;
        this.productService = productService;
        this.validator = validator;
    }

    public List<ProductSale> getAllProductSales() {
        return productSaleRepository.findAll();
    }

    public ProductSale getProductSaleById(Long id) {
        return productSaleRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product sale with id " + id + " not found"));
    }

    @Transactional
    public ProductSale addProductSale(ProductSale productSale) {
        Set<ConstraintViolation<ProductSale>> violations = validator.validate(productSale);
        if (!violations.isEmpty()) {
            throw new ProductValidationException(violations);
        }
        productSale.setId(null);

        Product product = productService.getProductById(productSale.getProduct().getId());
        product.setInStock(false);
        productSale.setProduct(product);
        return productSaleRepository.save(productSale);
    }

    @Transactional
    public ProductSale updateProductSale(ProductSale productSale) {
        ProductSale existingProduct = productSaleRepository.findById(productSale.getId()).orElseThrow(
                () -> new ProductNotFoundException("Product sale with id " + productSale.getId() + " not found"));

        if (productSale.getDocumentName() != null) existingProduct.setDocumentName(productSale.getDocumentName());
        if (productSale.getPurchasePrice() != null) existingProduct.setPurchasePrice(productSale.getPurchasePrice());
        if (productSale.getQuantity() != null) existingProduct.setQuantity(productSale.getQuantity());
        if (productSale.getProduct().getId() != null) {
            Product product = productService.getProductById(productSale.getProduct().getId());
            existingProduct.setProduct(product);
        }

        Set<ConstraintViolation<ProductSale>> violations = validator.validate(existingProduct);
        if (!violations.isEmpty()) {
            throw new ProductValidationException(violations);
        }
        return productSaleRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProductSale(Long id) {
        if (!productSaleRepository.existsById(id)) {
            throw new ProductNotFoundException("Product sale with id " + id + " not found");
        }
        productSaleRepository.deleteById(id);
    }
}