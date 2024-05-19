package ru.interview.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.interview.entity.Product;
import ru.interview.exception.ProductNotFoundException;
import ru.interview.exception.ProductValidationException;
import ru.interview.service.ProductService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Before
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
                .andExpect(jsonPath("$.status").value(1))
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
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.response.id").value(product.getId()));
    }

    @Test
    public void getProductById_WhenProductDoesNotExist_ReturnsNotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/product/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(-1));
    }

    @Test
    public void addProduct_ValidProduct_ReturnsCreated() throws Exception {
        doNothing().when(productService).addProduct(any(Product.class));

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Product\",\"description\":\"Description\",\"price\":100.0,\"inStock\":10}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    public void addProduct_InvalidProduct_ReturnsBadRequest() throws Exception {
        doThrow(new ProductValidationException("Validation failed")).when(productService).addProduct(any(Product.class));

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Invalid Product\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(-1));
    }

    @Test
    public void updateProduct_ValidProduct_ReturnsOk() throws Exception {
        Product product = new Product();
        product.setId(1L);
        doNothing().when(productService).updateProduct(any(Product.class));

        mockMvc.perform(put("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Product\",\"description\":\"Updated Description\",\"price\":150.0,\"inStock\":5}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(1))
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
                .andExpect(jsonPath("$.status").value(-1));
    }

    @Test
    public void updateProduct_ProductNotFound_ReturnsNotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(productService).updateProduct(any(Product.class));

        mockMvc.perform(put("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Product\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(-1));
    }

    @Test
    public void deleteProduct_ExistingProduct_ReturnsOk() throws Exception {
        Product product = new Product();
        product.setId(1L);
        doNothing().when(productService).deleteProduct(any(Product.class));

        mockMvc.perform(delete("/product/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    public void deleteProduct_ProductNotFound_ReturnsNotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(productService).deleteProduct(any(Product.class));

        mockMvc.perform(delete("/product/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(-1));
    }
}