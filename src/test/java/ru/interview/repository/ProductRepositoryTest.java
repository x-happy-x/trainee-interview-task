package ru.interview.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.interview.entity.Product;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
    }

    @Test
    public void findAll_WhenNoProducts_ReturnsEmptyList() {
        List<Product> products = productRepository.findAll();
        Assertions.assertTrue(products.isEmpty());
    }

    @Test
    public void findAll_WhenProductsExist_ReturnsProductList() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(10.0);
        product1.setInStock(true);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(20.0);
        product2.setInStock(true);

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();
        Assertions.assertEquals(2, products.size());
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertTrue(products.contains(product2));
    }

    @Test
    public void findById_WhenProductExists_ReturnsProduct() {
        Product product = new Product();
        product.setName("Product");
        product.setDescription("Description");
        product.setPrice(10.0);
        product.setInStock(true);
        productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(product.getId());
        Assertions.assertTrue(foundProduct.isPresent());
        Assertions.assertEquals(product, foundProduct.get());
    }

    @Test
    public void findById_WhenProductDoesNotExist_ReturnEmptyOptional() {
        Assertions.assertTrue(productRepository.findById(999L).isEmpty());
    }

    @Test
    public void save_WhenProductIsNew_AssignsIdAndSaves() {
        Product product = new Product();
        product.setName("Product");
        product.setDescription("Description");
        product.setPrice(10.0);
        product.setInStock(true);
        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Optional<Product> foundProduct = productRepository.findById(product.getId());
        Assertions.assertTrue(foundProduct.isPresent());
        Assertions.assertEquals(product, foundProduct.get());
    }

    @Test
    public void save_WhenProductHasId_UpdatesProduct() {
        Product product = new Product();
        product.setName("Product");
        product.setDescription("Description");
        product.setPrice(10.0);
        product.setInStock(true);
        product = productRepository.save(product);
        Long id = product.getId();

        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(20.0);
        updatedProduct.setInStock(false);
        productRepository.save(updatedProduct);

        Optional<Product> foundProduct = productRepository.findById(id);
        Assertions.assertTrue(foundProduct.isPresent());
        Assertions.assertEquals(updatedProduct, foundProduct.get());
    }

    @Test
    public void delete_WhenProductExists_RemovesProduct() {
        Product product = new Product();
        product.setName("Product");
        product.setDescription("Description");
        product.setPrice(10.0);
        product.setInStock(true);
        product = productRepository.save(product);

        productRepository.delete(product);

        Optional<Product> foundProduct = productRepository.findById(product.getId());
        Assertions.assertTrue(foundProduct.isEmpty());
    }

    @Test
    public void findByFiltersAndSort_ReturnsProductPage() {
        Product product1 = new Product();
        product1.setName("Товар1");
        product1.setPrice(100.0);
        product1.setInStock(true);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Товар2");
        product2.setPrice(200.0);
        product2.setInStock(true);
        productRepository.save(product2);

        Page<Product> page = productRepository.findByFiltersAndSort(
                "Товар", 50.0, 250.0, true, PageRequest.of(0, 10));

        Assertions.assertEquals(2, page.getTotalElements());
        Assertions.assertTrue(page.getContent().contains(product1));
        Assertions.assertTrue(page.getContent().contains(product2));
    }

    @Test
    public void findByFilters_ReturnsProductList() {
        Product product1 = new Product();
        product1.setName("Товар1");
        product1.setPrice(100.0);
        product1.setInStock(true);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Товар2");
        product2.setPrice(200.0);
        product2.setInStock(true);
        productRepository.save(product2);

        List<Product> products = productRepository.findByFilters("Товар", 50.0, 250.0, true);

        Assertions.assertEquals(2, products.size());
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertTrue(products.contains(product2));
    }

}