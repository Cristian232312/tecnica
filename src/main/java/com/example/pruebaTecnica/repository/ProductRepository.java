package com.example.pruebaTecnica.repository;

import com.example.pruebaTecnica.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByAccountNumber(String number);
}
