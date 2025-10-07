package com.example.pruebaTecnica.repository;

import com.example.pruebaTecnica.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
