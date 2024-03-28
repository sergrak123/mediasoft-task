package ru.grak.mediasofttask.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.grak.mediasofttask.entity.Product;
import ru.grak.mediasofttask.exceptions.ProductNotFoundException;
import ru.grak.mediasofttask.service.ProductService;
import ru.grak.mediasofttask.validation.ValidationMarker;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") UUID productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok().body(product);

        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Validated({ValidationMarker.OnCreate.class})
    public Product createProduct(@Valid @RequestBody Product product) {

        System.out.println(product);
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    @Validated(ValidationMarker.OnUpdate.class)
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") UUID productId,
                                                 @Valid @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(productId, productDetails);
            return ResponseEntity.ok().body(updatedProduct);

        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") UUID productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();

        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}