package ru.interview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.interview.entity.ProductSale;
import ru.interview.response.ResponseBuilder;
import ru.interview.service.ProductSaleService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/product-sale")
public class ProductSaleController {

    private final ProductSaleService productSaleService;

    @Autowired
    public ProductSaleController(ProductSaleService productSaleService) {
        this.productSaleService = productSaleService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProductSales(HttpServletRequest request) {
        List<ProductSale> productSales = productSaleService.getAllProductSales();
        return ResponseBuilder.ok(productSales, request.getRequestURI());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductSaleById(@PathVariable Long id, HttpServletRequest request) {
        ProductSale productSale = productSaleService.getProductSaleById(id);
        return ResponseBuilder.ok(productSale, request.getRequestURI());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductSale(@RequestBody ProductSale productSale, HttpServletRequest request) {
        productSale = productSaleService.addProductSale(productSale);
        return ResponseBuilder.build(HttpStatus.CREATED, productSale, request.getRequestURI());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProductSale(@RequestBody ProductSale productSale, HttpServletRequest request) {
        productSale = productSaleService.updateProductSale(productSale);
        return ResponseBuilder.ok(productSale, request.getRequestURI());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProductSale(@PathVariable Long id, HttpServletRequest request) {
        productSaleService.deleteProductSale(id);
        return ResponseBuilder.ok(null, request.getRequestURI());
    }
}
