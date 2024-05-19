package ru.interview.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.interview.entity.Product;
import ru.interview.response.ResponseBuilder;
import ru.interview.service.ProductService;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> getAllProducts(HttpServletRequest request) {
        List<Product> products = productService.getAllProducts();
        return ResponseBuilder.build(HttpStatus.OK, products, request.getRequestURI());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id, HttpServletRequest request) {
        Product product = productService.getProductById(id);
        return ResponseBuilder.build(HttpStatus.OK, product, request.getRequestURI());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product, HttpServletRequest request) {
        product = productService.addProduct(product);
        return ResponseBuilder.build(HttpStatus.CREATED, product, request.getRequestURI());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody Product product, HttpServletRequest request) {
        product = productService.updateProduct(product);
        return ResponseBuilder.build(HttpStatus.OK, product, request.getRequestURI());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestBody Product product, HttpServletRequest request) {
        productService.deleteProduct(product.getId());
        return ResponseBuilder.build(HttpStatus.OK, null, request.getRequestURI());
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) Double minPrice,
                                            @RequestParam(required = false) Double maxPrice,
                                            @RequestParam(required = false) Double price,
                                            @RequestParam(required = false) Boolean inStock,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "0") int size,
                                            @RequestParam(defaultValue = "name") String sortBy,
                                            HttpServletRequest request) {

        if (!sortBy.equalsIgnoreCase("name") && !sortBy.equalsIgnoreCase("price")) {
            return ResponseBuilder.error(HttpStatus.BAD_REQUEST, new BadRequestException(
                            "Sorting can only be done by 'name' or 'price'. The current value is '" + sortBy + "'."),
                    request.getRequestURI());
        }

        if (price != null) {
            minPrice = price;
            maxPrice = price;
        }

        List<Product> products = productService.searchProducts(
                name, minPrice, maxPrice, inStock, size > 0 ? PageRequest.of(page, size, Sort.by(sortBy)) : null);

        return ResponseBuilder.build(HttpStatus.OK, products, request.getRequestURI());
    }


}
