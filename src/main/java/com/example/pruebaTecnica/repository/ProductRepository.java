package com.example.pruebaTecnica.repository;

import com.example.pruebaTecnica.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByAccountNumber(String number);
    Optional<Product> findByAccountNumber(String accountNumber);
    boolean existsByClientId(Long ClientId);
}