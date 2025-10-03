package com.example.pruebaTecnica.service;

import com.example.pruebaTecnica.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    List<ProductDTO> listProducts();
    void deleteProduct(Long id);
}