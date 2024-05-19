package ru.interview.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.interview.entity.Product;
import ru.interview.exception.ProductNotFoundException;
import ru.interview.exception.ProductValidationException;
import ru.interview.service.ProductService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllProducts_ReturnsProductList() throws Exception {
        Product product1 = new Product();
        Product product2 = new Product();
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/product/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.response.length()").value(2))
                .andExpect(jsonPath("$.count").value(2));
    }

    @Test
    public void getProductById_WhenProductExists_ReturnsProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        when(productService.getProductById(anyLong())).thenReturn(product);

        mockMvc.perform(get("/product/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.response.id").value(product.getId()));
    }

    @Test
    public void getProductById_WhenProductDoesNotExist_ReturnsNotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/product/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void addProduct_ValidProduct_ReturnsCreated() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(100.0);
        product.setInStock(true);
        when(productService.addProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Product\",\"description\":\"Description\",\"price\":100.0,\"inStock\":true}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()));
    }

    @Test
    public void addProduct_InvalidProduct_ReturnsBadRequest() throws Exception {
        doThrow(new ProductValidationException("Validation failed")).when(productService).addProduct(any(Product.class));

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Invalid Product\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void updateProduct_ValidProduct_ReturnsOk() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");
        product.setDescription("Updated Description");
        product.setPrice(150.0);
        product.setInStock(false);
        when(productService.updateProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Product\",\"description\":\"Updated Description\",\"price\":150.0,\"inStock\":false}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.response.id").value(product.getId()));
    }

    @Test
    public void updateProduct_InvalidProduct_ReturnsBadRequest() throws Exception {
        doThrow(new ProductValidationException("Validation failed")).when(productService).updateProduct(any(Product.class));

        mockMvc.perform(put("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Invalid Product\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void updateProduct_ProductNotFound_ReturnsNotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(productService).updateProduct(any(Product.class));

        mockMvc.perform(put("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Product\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void deleteProduct_ExistingProduct_ReturnsOk() throws Exception {
        doNothing().when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/product/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()));
    }

    @Test
    public void deleteProduct_ProductNotFound_ReturnsNotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/product/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void searchProducts_ReturnsProductList() throws Exception {
        Product product1 = new Product();
        product1.setName("Товар1");
        product1.setPrice(100.0);
        product1.setInStock(true);

        Product product2 = new Product();
        product2.setName("Товар2");
        product2.setPrice(200.0);
        product2.setInStock(true);

        when(productService.searchProducts(anyString(), anyDouble(), anyDouble(), anyBoolean(), any()))
                .thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/product/search")
                        .param("name", "Товар")
                        .param("inStock", "true")
                        .param("minPrice", "100.0")
                        .param("maxPrice", "100.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.response.length()").value(2))
                .andExpect(jsonPath("$.count").value(2));
    }
}