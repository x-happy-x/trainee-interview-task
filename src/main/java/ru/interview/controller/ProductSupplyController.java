package ru.interview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.interview.entity.ProductSupply;
import ru.interview.response.ResponseBuilder;
import ru.interview.service.ProductSupplyService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/product-supply")
public class ProductSupplyController {

    private final ProductSupplyService productSupplyService;

    @Autowired
    public ProductSupplyController(ProductSupplyService productSupplyService) {
        this.productSupplyService = productSupplyService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProductSupplies(HttpServletRequest request) {
        List<ProductSupply> productSupplies = productSupplyService.getAllProductSupplies();
        return ResponseBuilder.ok(productSupplies, request.getRequestURI());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductSupplyById(@PathVariable Long id, HttpServletRequest request) {
        ProductSupply productSupply = productSupplyService.getProductSupplyById(id);
        return ResponseBuilder.ok(productSupply, request.getRequestURI());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductSupply(@RequestBody ProductSupply productSupply, HttpServletRequest request) {
        productSupply = productSupplyService.addProductSupply(productSupply);
        return ResponseBuilder.build(HttpStatus.CREATED, productSupply, request.getRequestURI());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProductSupply(@RequestBody ProductSupply productSupply, HttpServletRequest request) {
        productSupply = productSupplyService.updateProductSupply(productSupply);
        return ResponseBuilder.ok(productSupply, request.getRequestURI());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProductSupply(@PathVariable Long id, HttpServletRequest request) {
        productSupplyService.deleteProductSupply(id);
        return ResponseBuilder.ok(null, request.getRequestURI());
    }
}