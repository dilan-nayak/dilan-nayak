package com.shoplite.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shoplite.catalog.domain.Product;
import com.shoplite.catalog.dto.ProductRequest;
import com.shoplite.catalog.dto.ProductResponse;
import com.shoplite.catalog.exception.DuplicateResourceException;
import com.shoplite.catalog.exception.ResourceNotFoundException;
import com.shoplite.catalog.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequest request;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Pen")
                .price(BigDecimal.TEN)
                .stockQty(100)
                .build();
        request = new ProductRequest("Pen", BigDecimal.TEN, 100);
    }

    @Test
    void shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponse> responses = productService.getAllProducts();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).isEqualTo("Pen");
    }

    @Test
    void shouldReturnProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProduct(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Pen");
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProduct(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldCreateProduct() {
        when(productRepository.findByNameIgnoreCase("Pen")).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(request);

        assertThat(response.id()).isEqualTo(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldPreventDuplicateProductCreation() {
        when(productRepository.findByNameIgnoreCase("Pen")).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void shouldUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findByNameIgnoreCase("Pen")).thenReturn(Optional.of(product));

        ProductRequest updateRequest = new ProductRequest("Pen", BigDecimal.valueOf(12.5), 120);

        ProductResponse response = productService.updateProduct(1L, updateRequest);

        assertThat(response.price()).isEqualByComparingTo("12.5");
        assertThat(response.stockQty()).isEqualTo(120);
    }

    @Test
    void shouldDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }
}
