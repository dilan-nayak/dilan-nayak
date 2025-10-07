package com.shoplite.catalog.service;

import com.shoplite.catalog.domain.Product;
import com.shoplite.catalog.dto.ProductRequest;
import com.shoplite.catalog.dto.ProductResponse;
import com.shoplite.catalog.exception.DuplicateResourceException;
import com.shoplite.catalog.exception.ResourceNotFoundException;
import com.shoplite.catalog.mapper.ProductMapper;
import com.shoplite.catalog.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll().stream().map(ProductMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        log.info("Fetching product with id={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id=" + id));
        return ProductMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product name={} stockQty={}", request.name(), request.stockQty());
        productRepository.findByNameIgnoreCase(request.name()).ifPresent(product -> {
            throw new DuplicateResourceException("Product already exists with name=" + request.name());
        });
        Product product = ProductMapper.toEntity(request);
        Product saved = productRepository.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product id={}", id);
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id=" + id));
        productRepository.findByNameIgnoreCase(request.name())
                .filter(product -> !product.getId().equals(id))
                .ifPresent(product -> {
                    throw new DuplicateResourceException("Product already exists with name=" + request.name());
                });
        ProductMapper.updateEntity(existing, request);
        return ProductMapper.toResponse(existing);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product id={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id=" + id));
        productRepository.delete(product);
    }
}
