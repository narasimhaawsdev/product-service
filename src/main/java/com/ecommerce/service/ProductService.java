package com.ecommerce.service;

import com.ecommerce.client.InventoryClient;
import com.ecommerce.entity.Product;
import com.ecommerce.model.Inventory;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private InventoryClient inventoryClient;


    public Page<Product> filterProducts(String category, BigDecimal min, BigDecimal max, Pageable pageable) {
        Specification<Product> spec = Specification.where(null);
        if (category != null) spec = spec.and(ProductSpecification.hasCategory(category));
        if (min != null && max != null) spec = spec.and(ProductSpecification.priceBetween(min, max));
        return repository.findAll(spec, pageable);
    }

    public Product create(Product product) {
        Product p=repository.save(product);
        inventoryClient.createOrUpdateInventory(p.getId(),p.getDelta());
        return p;
    }

    public Optional<Product> get(Long id) {
        Optional<Product> product = repository.findById(id);
        System.out.println(product.get().getCategory());
        return product;
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
