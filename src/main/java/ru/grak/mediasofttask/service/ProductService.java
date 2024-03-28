package ru.grak.mediasofttask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.grak.mediasofttask.entity.Product;
import ru.grak.mediasofttask.exceptions.ProductNotFoundException;
import ru.grak.mediasofttask.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing products.
 */
@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products.
     *
     * @return the list of all products
     */
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }


    /**
     * Retrieves a product by ID.
     *
     * @param productId the product ID
     * @return the product with the specified ID
     * @throws ProductNotFoundException if the product with the given ID does not exist
     */
    public Product getProductById(UUID productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: {0} doesn't exist", productId));
    }

    /**
     * Creates a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    public Product createProduct(Product product) {

        product.setCreationDateTime(LocalDateTime.now());
        product.setLastQuantityChangeDateTime(LocalDateTime.now());

        return productRepository.save(product);
    }

    /**
     * Updates an existing product.
     *
     * @param productId       the ID of the product to update
     * @param productDetails the updated product details
     * @return the updated product
     * @throws ProductNotFoundException if the product with the given ID does not exist
     */
    public Product updateProduct(UUID productId, Product productDetails) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: {0} doesn't exist", productId));

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

    /**
     * Deletes a product by ID.
     *
     * @param productId the ID of the product to delete
     * @throws ProductNotFoundException if the product with the given ID does not exist
     */
    public void deleteProduct(UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: {0} doesn't exist", productId));

        productRepository.delete(product);
    }
}
