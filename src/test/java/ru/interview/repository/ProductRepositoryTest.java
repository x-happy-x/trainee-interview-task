package ru.interview.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.interview.entity.Product;
import ru.interview.exception.ProductNotFoundException;

import java.util.List;

public class ProductRepositoryTest {

    private ProductRepository productRepository;

    @Before
    public void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    public void findAll_WhenNoProducts_ReturnsEmptyList() {
        List<Product> products = productRepository.findAll();
        Assert.assertTrue(products.isEmpty());
    }

    @Test
    public void findAll_WhenProductsExist_ReturnsProductList() {
        Product product1 = new Product();
        Product product2 = new Product();
        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();
        Assert.assertEquals(2, products.size());
        Assert.assertTrue(products.contains(product1));
        Assert.assertTrue(products.contains(product2));
    }

    @Test
    public void findById_WhenProductExists_ReturnsProduct() throws ProductNotFoundException {
        Product product = new Product();
        productRepository.save(product);

        Product foundProduct = productRepository.findById(product.getId());
        Assert.assertEquals(product, foundProduct);
    }

    @Test(expected = ProductNotFoundException.class)
    public void findById_WhenProductDoesNotExist_ThrowsProductNotFoundException() throws ProductNotFoundException {
        productRepository.findById(999L);
    }

    @Test
    public void save_WhenProductIsNew_AssignsIdAndSaves() throws ProductNotFoundException {
        Product product = new Product();
        productRepository.save(product);

        Assert.assertNotNull(product.getId());
        Assert.assertEquals(product, productRepository.findById(product.getId()));
    }

    @Test
    public void save_WhenProductHasId_UpdatesProduct() throws ProductNotFoundException {
        Product product = new Product();
        productRepository.save(product);
        Long id = product.getId();

        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        productRepository.save(updatedProduct);

        Product foundProduct = productRepository.findById(id);
        Assert.assertEquals(updatedProduct, foundProduct);
    }

    @Test
    public void delete_WhenProductExists_RemovesProduct() throws ProductNotFoundException {
        Product product = new Product();
        productRepository.save(product);

        productRepository.delete(product);
        Assert.assertThrows(ProductNotFoundException.class, () -> productRepository.findById(product.getId()));
    }

    @Test(expected = ProductNotFoundException.class)
    public void delete_WhenProductDoesNotExist_ThrowsProductNotFoundException() throws ProductNotFoundException {
        Product product = new Product();
        product.setId(999L);

        productRepository.delete(product);
    }
}