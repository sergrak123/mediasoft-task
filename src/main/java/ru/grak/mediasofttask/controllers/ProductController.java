package ru.grak.mediasofttask.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.grak.mediasofttask.dao.ProductDAO;
import ru.grak.mediasofttask.entity.Product;
import ru.grak.mediasofttask.exceptions.ProductNotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") UUID productId) {

        Product product = productDAO.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Данный пользователь не найден"));

        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
        return productDAO.saveProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") UUID productId,
                                                 @Valid @RequestBody Product productDetails) {

        Product product = productDAO.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Данный пользователь не найден"));

        product.setArticle(productDetails.getArticle());
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setLastQuantityChangeDateTime(productDetails.getLastQuantityChangeDateTime());
        final Product updatedProduct = productDAO.saveProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") UUID productId) {

        Product product = productDAO.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Данный пользователь не найден"));

        productDAO.deleteProduct(product);
        return ResponseEntity.ok().build();
    }
}