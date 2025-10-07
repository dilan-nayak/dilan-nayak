package com.shoplite.catalog.dto;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, BigDecimal price, Integer stockQty) {
}
