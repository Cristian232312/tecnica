package com.example.pruebaTecnica.mapper;

import com.example.pruebaTecnica.dto.ClientDTO;
import com.example.pruebaTecnica.entity.Client;

public class ClientMapper {

    public ClientDTO toDTO(Client client) {

        if (client == null) {
            return null;
        }

        ClientDTO dto = new ClientDTO();
        dto.setIdentificationType(client.getIdentificationType());
        dto.setIdentificationNumber(client.getIdentificationNumber());
        dto.setName(client.getName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setDateOfBirth(client.getDateOfBirth());

        return dto;
    }

    public Client toEntity(ClientDTO clientDTO) {

        if (clientDTO == null) {
            return null;
        }

        Client entity = new Client();
        entity.setIdentificationType(clientDTO.getIdentificationType());
        entity.setIdentificationNumber(clientDTO.getIdentificationNumber());
        entity.setName(clientDTO.getName());
        entity.setLastName(clientDTO.getLastName());
        entity.setEmail(clientDTO.getEmail());
        entity.setDateOfBirth(clientDTO.getDateOfBirth());

        return entity;
    }
}
