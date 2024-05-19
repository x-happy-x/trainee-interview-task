package ru.interview.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.interview.entity.Product;
import ru.interview.exception.ProductNotFoundException;
import ru.interview.exception.ProductValidationException;
import ru.interview.repository.ProductRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllProducts_ReturnsProductList() {
        Product product1 = new Product();
        Product product2 = new Product();
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
    }

    @Test
    public void getProductById_WhenProductExists_ReturnsProduct() {
        Product product = new Product();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);
        assertEquals(product, foundProduct);
    }

    @Test
    public void getProductById_WhenProductDoesNotExist_ThrowsProductNotFoundException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    public void addProduct_ValidProduct_SavesProduct() {
        Product product = new Product();
        product.setName("Товар");
        product.setPrice(100.0);
        when(validator.validate(any(Product.class))).thenReturn(Collections.emptySet());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.addProduct(product);
        verify(productRepository, times(1)).save(any(Product.class));
        assertEquals(product, savedProduct);
    }

    @Test
    public void addProduct_InvalidProduct_ThrowsProductValidationException() {
        Product product = new Product();
        Set<ConstraintViolation<Product>> violations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(any(Product.class))).thenReturn(violations);

        assertThrows(ProductValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    public void updateProduct_ValidProduct_UpdatesProduct() {
        Product existingProduct = new Product();
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Товар");
        updatedProduct.setPrice(100.0);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));
        when(validator.validate(any(Product.class))).thenReturn(Collections.emptySet());
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.updateProduct(updatedProduct);
        verify(productRepository, times(1)).save(existingProduct);
        assertEquals(existingProduct, result);
    }

    @Test
    public void updateProduct_InvalidProduct_ThrowsProductValidationException() {
        Product existingProduct = new Product();
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setPrice(-100.0);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));
        Set<ConstraintViolation<Product>> violations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(any(Product.class))).thenReturn(violations);

        assertThrows(ProductValidationException.class, () -> productService.updateProduct(updatedProduct));
    }

    @Test
    public void updateProduct_ProductNotFound_ThrowsProductNotFoundException() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Товар");
        product.setPrice(0.0);
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(product));
    }

    @Test
    public void deleteProduct_ExistingProduct_DeletesProduct() {
        when(productRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(productRepository).deleteById(anyLong());

        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteProduct_ProductNotFound_ThrowsProductNotFoundException() {
        when(productRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
    }
}