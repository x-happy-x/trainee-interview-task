package ru.interview.service;

import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.interview.entity.Product;
import ru.interview.exception.ProductNotFoundException;
import ru.interview.exception.ProductValidationException;
import ru.interview.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllProducts_ReturnsProductList() {
        Product product1 = new Product();
        Product product2 = new Product();
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();
        Assert.assertEquals(2, products.size());
        Assert.assertTrue(products.contains(product1));
        Assert.assertTrue(products.contains(product2));
    }

    @Test
    public void getProductById_WhenProductExists_ReturnsProduct() throws ProductNotFoundException {
        Product product = new Product();
        when(productRepository.findById(anyLong())).thenReturn(product);

        Product foundProduct = productService.getProductById(1L);
        Assert.assertEquals(product, foundProduct);
    }

    @Test(expected = ProductNotFoundException.class)
    public void getProductById_WhenProductDoesNotExist_ThrowsProductNotFoundException() throws ProductNotFoundException {
        when(productRepository.findById(anyLong())).thenThrow(new ProductNotFoundException("Product not found"));
        productService.getProductById(1L);
    }

    @Test
    public void addProduct_ValidProduct_SavesProduct() throws ProductValidationException {
        Product product = new Product();
        product.setId(1L);
        product.setName("Товар");
        product.setPrice(100.0);
        doNothing().when(productRepository).save(any(Product.class));

        productService.addProduct(product);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void addProduct_InvalidProduct_ThrowsProductValidationException() {
        Product product = new Product();

        Assert.assertThrows(ProductValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    public void updateProduct_ValidProduct_UpdatesProduct() throws ProductValidationException, ProductNotFoundException {
        Product existingProduct = new Product();
        when(productRepository.findById(anyLong())).thenReturn(existingProduct);

        Product product = new Product();
        product.setId(1L);
        product.setName("Товар");
        product.setPrice(100.0);

        productService.updateProduct(product);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test(expected = ProductValidationException.class)
    public void updateProduct_InvalidProduct_ThrowsProductValidationException() throws ProductNotFoundException, ProductValidationException {
        Product product = new Product();
        product.setPrice(-100.0);
        productService.updateProduct(product);
    }

    @Test(expected = ProductNotFoundException.class)
    public void updateProduct_ProductNotFound_ThrowsProductNotFoundException() throws ProductNotFoundException, ProductValidationException {
        when(productRepository.findById(anyLong())).thenThrow(new ProductNotFoundException("Product not found"));

        Product product = new Product();
        product.setId(1L);
        product.setName("Товар");
        product.setPrice(0.0);
        productService.updateProduct(product);
    }

    @Test
    public void deleteProduct_ExistingProduct_DeletesProduct() throws ProductNotFoundException {
        Product product = new Product();
        doNothing().when(productRepository).delete(any(Product.class));

        productService.deleteProduct(product);
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    public void deleteProduct_ProductNotFound_ThrowsProductNotFoundException() throws ProductNotFoundException {
        Product product = new Product();
        doThrow(new ProductNotFoundException("Product not found")).when(productRepository).delete(any(Product.class));

        Assert.assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(product));
    }
}