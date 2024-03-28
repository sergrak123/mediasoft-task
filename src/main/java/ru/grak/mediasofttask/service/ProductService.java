package ru.grak.mediasofttask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public Product getProductById(UUID productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Продукт по id: {0} не найден", productId));
    }

    public Product createProduct(Product product) {

        product.setCreationDateTime(LocalDateTime.now());
        product.setLastQuantityChangeDateTime(LocalDateTime.now());

        return productRepository.save(product);
    }

    public Product updateProduct(UUID productId, Product productDetails) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Продукт по id: {0} не найден", productId));

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
                .orElseThrow(() -> new ProductNotFoundException("Продукт по id: {0} не найден", productId));

        productRepository.delete(product);
    }
}
