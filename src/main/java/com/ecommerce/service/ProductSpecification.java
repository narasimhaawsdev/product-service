package com.ecommerce.service;

import com.ecommerce.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> hasCategory(String category) {
        return (root, query, builder) -> builder.equal(root.get("category"), category);
    }
    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, builder) -> builder.between(root.get("price"), min, max);
    }
}