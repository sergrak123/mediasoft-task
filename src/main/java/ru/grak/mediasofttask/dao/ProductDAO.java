package ru.grak.mediasofttask.dao;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.grak.mediasofttask.entity.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductDAO {

    @Autowired
    private final SessionFactory sessionFactory;

    public ProductDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public Product saveProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        product.setCreationDateTime(LocalDateTime.now());
        Product savedProduct = (Product) session.save(product);

        return savedProduct;
    }

    @Transactional
    public void updateProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        product.setLastQuantityChangeDateTime(LocalDateTime.now());
        session.update(product);
    }

    @Transactional
    public void deleteProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(product);
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.of(session.get(Product.class, id));
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Product", Product.class).getResultList();
    }
}
