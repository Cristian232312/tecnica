package com.example.pruebaTecnica.mapper;

import com.example.pruebaTecnica.dto.ClientDTO;
import com.example.pruebaTecnica.dto.ProductDTO;
import com.example.pruebaTecnica.entity.Client;
import com.example.pruebaTecnica.entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientMapper {

    private final ProductMapper productMapper;

    public ClientMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ClientDTO toDTO(Client client) {
        if (client == null) {
            return null;
        }

        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setIdentificationType(client.getIdentificationType());
        dto.setIdentificationNumber(client.getIdentificationNumber());
        dto.setName(client.getName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setDateOfBirth(client.getDateOfBirth());
        dto.setDateOfCreate(client.getDateOfCreate());
        dto.setDateOfUpdate(client.getDateOfUpdate());

        if (client.getProducts() != null) {
            List<ProductDTO> productDTOs = new ArrayList<>();
            for (Product product : client.getProducts()) {
                productDTOs.add(productMapper.toDTO(product));
            }
            dto.setProducts(productDTOs);
        }

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
        entity.setDateOfCreate(clientDTO.getDateOfCreate());
        entity.setDateOfUpdate(clientDTO.getDateOfUpdate());

        return entity;
    }
}
