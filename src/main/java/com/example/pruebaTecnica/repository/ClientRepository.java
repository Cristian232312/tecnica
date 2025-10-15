package com.example.pruebaTecnica.repository;

import com.example.pruebaTecnica.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByIdentificationNumber(String identificationNumber);
}