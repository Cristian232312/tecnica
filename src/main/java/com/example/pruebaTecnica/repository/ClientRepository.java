package com.example.pruebaTecnica.repository;

import com.example.pruebaTecnica.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
