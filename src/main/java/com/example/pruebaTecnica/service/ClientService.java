package com.example.pruebaTecnica.service;

import com.example.pruebaTecnica.dto.ClientDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ClientService {
    ClientDTO createClient(ClientDTO clientDTO);
    List<ClientDTO> ListClient();
    ClientDTO updateClient(Long id, ClientDTO clientDTO);
    void deleteClient(Long id);
}
