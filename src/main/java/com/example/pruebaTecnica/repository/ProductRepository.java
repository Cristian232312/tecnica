package com.example.pruebaTecnica.repository;

import com.example.pruebaTecnica.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByAccountNumber(String number);
    Optional<Product> findByAccountNumber(String accountNumber);
    boolean existsByClientId(Long ClientId);
}