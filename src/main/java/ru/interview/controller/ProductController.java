package ru.interview.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.interview.entity.Product;
import ru.interview.exception.ProductNotFoundException;
import ru.interview.exception.ProductValidationException;
import ru.interview.response.ResponseBuilder;
import ru.interview.service.ProductService;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseBuilder.build(HttpStatus.OK, products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) throws ProductNotFoundException {
        Product product = productService.getProductById(id);
        return ResponseBuilder.build(HttpStatus.OK, product);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) throws ProductValidationException {
        productService.addProduct(product);
        return ResponseBuilder.build(HttpStatus.CREATED, product);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody Product product) throws ProductValidationException, ProductNotFoundException {
        productService.updateProduct(product);
        return ResponseBuilder.build(HttpStatus.OK, product);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestBody Product product) throws ProductNotFoundException {
        productService.deleteProduct(product);
        return ResponseBuilder.build(HttpStatus.OK, null);
    }

    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<?> handleProductValidationException(ProductValidationException exception) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException exception) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, exception);
    }
}
