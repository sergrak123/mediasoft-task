package ru.grak.mediasofttask.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.grak.mediasofttask.entity.Product;
import ru.grak.mediasofttask.exceptions.ProductNotFoundException;
import ru.grak.mediasofttask.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Валидация в controller
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public Product getProductById(UUID productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Данный продукт не найден"));
    }

    //у product мб null поля(кроме валидированных)
    //при добавлении задаем ldt создания и ldt изменения количества
    public Product createProduct(Product product) {

        product.setCreationDateTime(LocalDateTime.now());
        product.setLastQuantityChangeDateTime(LocalDateTime.now());

        return productRepository.save(product);
    }

    //передаем полностью
    public Product updateProduct(UUID productId, Product productDetails) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Данный продукт не найден"));

        //дату создания не трогаем, меняем только дату изменения количества (если количество изменилось)
        boolean wasChangedQuantity = !product.getQuantity().equals(productDetails.getQuantity());

        product.setArticle(productDetails.getArticle());
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setCreationDateTime(product.getCreationDateTime());

        LocalDateTime modifiedDateTime = wasChangedQuantity
                ? LocalDateTime.now()
                : product.getLastQuantityChangeDateTime();

        product.setLastQuantityChangeDateTime(modifiedDateTime);

        return productRepository.save(product);
    }

    public void deleteProduct(UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Данный продукт не найден"));

        productRepository.delete(product);
    }
}
