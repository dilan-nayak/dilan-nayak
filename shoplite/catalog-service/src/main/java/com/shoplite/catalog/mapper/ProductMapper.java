package com.shoplite.catalog.mapper;

import com.shoplite.catalog.domain.Product;
import com.shoplite.catalog.dto.ProductRequest;
import com.shoplite.catalog.dto.ProductResponse;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.name().trim())
                .price(request.price())
                .stockQty(request.stockQty())
                .build();
    }

    public static void updateEntity(Product product, ProductRequest request) {
        product.setName(request.name().trim());
        product.setPrice(request.price());
        product.setStockQty(request.stockQty());
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStockQty());
    }
}
