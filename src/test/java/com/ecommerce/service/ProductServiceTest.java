package com.ecommerce.service;


import com.ecommerce.client.InventoryClient;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleProduct = new Product(1L, "Phone", "Electronics", BigDecimal.valueOf(299.99), 10);
    }

    @Test
    void testCreate_shouldSaveProductAndUpdateInventory() {
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        Product created = productService.create(sampleProduct);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(1L);
        verify(productRepository, times(1)).save(sampleProduct);
        verify(inventoryClient, times(1)).createOrUpdateInventory(1L, 10);
    }

    @Test
    void testGet_shouldReturnProductIfExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Optional<Product> result = productService.get(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Phone");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGet_shouldReturnEmptyIfNotExists() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.get(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void testDelete_shouldReturnTrueIfExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean deleted = productService.delete(1L);

        assertThat(deleted).isTrue();
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDelete_shouldReturnFalseIfNotExists() {
        when(productRepository.existsById(2L)).thenReturn(false);

        boolean deleted = productService.delete(2L);

        assertThat(deleted).isFalse();
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFilterProducts_shouldReturnPagedResult() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(sampleProduct));
        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);

        Page<Product> result = productService.filterProducts("Electronics", BigDecimal.valueOf(100), BigDecimal.valueOf(400), pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Phone");
    }
}
