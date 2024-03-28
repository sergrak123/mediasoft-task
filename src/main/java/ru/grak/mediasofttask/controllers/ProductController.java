package ru.grak.mediasofttask.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Product Controller", description = "APIs for managing products")
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get a list of all products")
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get product details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") UUID productId) {

        Product product = productService.getProductById(productId);
        return ResponseEntity.ok().body(product);
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    @Validated({ValidationMarker.OnCreate.class})
    public Product createProduct(@Valid @RequestBody Product product) {

        System.out.println(product);
        return productService.createProduct(product);
    }

    @Operation(summary = "Update an existing product by ID")
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

    @Operation(summary = "Delete product by ID")
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