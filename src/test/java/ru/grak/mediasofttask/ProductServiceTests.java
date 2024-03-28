package ru.grak.mediasofttask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.grak.mediasofttask.entity.Product;
import ru.grak.mediasofttask.exceptions.ProductNotFoundException;
import ru.grak.mediasofttask.repository.ProductRepository;
import ru.grak.mediasofttask.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTests {

    @Mock
    private ProductRepository productMockRepository;

    @InjectMocks
    private ProductService productMockService;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetProductByIdThrowsNotFoundException() {

        UUID productId = UUID.randomUUID();
        when(productMockRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productMockService.getProductById(productId));
    }

    @Test
    void testDeleteNotExistProduct() {

        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        when(productMockRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productMockService.deleteProduct(productId));
    }

    //edit?
    @Test
    void testCreateProduct() {

        Product product = getRandomProduct();
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);

        Product result = productService.createProduct(product);
        productService.deleteProduct(result.getId());

        assertNotNull(result.getId());
        assertNotNull(result.getCreationDateTime());
        assertTrue(result.getCreationDateTime().isAfter(beforeCreation));
    }

    //edit?
    @Test
    void testUpdateProduct() {

        Product existingProductDetails = getRandomProduct();
        existingProductDetails.setQuantity(10);

        Product updatedProductDetails = getRandomProduct();
        updatedProductDetails.setQuantity(20);

        Product savedProduct = productService.createProduct(existingProductDetails);
        UUID productId = savedProduct.getId();

        System.out.println(updatedProductDetails);
        Product result = productService.updateProduct(productId, updatedProductDetails);
        productService.deleteProduct(productId);

        //Обновилось количество -> дата изменения количества также обновилась
        assertEquals(updatedProductDetails.getQuantity(), result.getQuantity());
        assertNotEquals(savedProduct.getLastQuantityChangeDateTime(), result.getLastQuantityChangeDateTime());
    }

    Product getRandomProduct() {
        Product product = new Product();

        product.setName("Example Product");
        product.setArticle("123456");
        product.setDescription("Example description");
        product.setCategory("Example category");
        product.setPrice(new BigDecimal("199.90"));
        product.setQuantity(100);

        return product;
    }

}