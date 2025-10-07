package com.shoplite.catalog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoplite.catalog.dto.ProductRequest;
import com.shoplite.catalog.dto.ProductResponse;
import com.shoplite.catalog.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void shouldReturnAllProducts() throws Exception {
        when(productService.getAllProducts())
                .thenReturn(List.of(new ProductResponse(1L, "Pen", BigDecimal.TEN, 100)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pen"));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Pen", BigDecimal.TEN, 100);
        ProductResponse response = new ProductResponse(1L, "Pen", BigDecimal.TEN, 100);
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldValidateCreateProduct() throws Exception {
        ProductRequest invalid = new ProductRequest("", BigDecimal.valueOf(-1), -5);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Pencil", BigDecimal.valueOf(1.5), 50);
        ProductResponse response = new ProductResponse(1L, "Pencil", BigDecimal.valueOf(1.5), 50);
        when(productService.updateProduct(Mockito.eq(1L), any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pencil"));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}
