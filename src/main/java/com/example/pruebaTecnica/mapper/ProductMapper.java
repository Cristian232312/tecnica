package com.example.pruebaTecnica.mapper;

import com.example.pruebaTecnica.dto.ProductDTO;
import com.example.pruebaTecnica.dto.TransactionDTO;
import com.example.pruebaTecnica.entity.Product;
import com.example.pruebaTecnica.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {

    private final TransactionMapper transactionMapper;

    public ProductMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setAccountType(product.getAccountType());
        dto.setAccountNumber(product.getAccountNumber());
        dto.setStatus(product.getStatus());
        dto.setBalance(product.getBalance());
        dto.setExemptGmf(product.getExemptGmf());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setClientId(product.getClient() != null ? product.getClient().getId() : null);

        if (product.getTransactions() != null) {
            List<TransactionDTO> transactionDTOS = new ArrayList<>();
            for (Transaction transaction : product.getTransactions()) {
                transactionDTOS.add(transactionMapper.toDTO(transaction));
            }
            dto.setTransactions(transactionDTOS);
        }

        return dto;
    }

    public Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setAccountType(productDTO.getAccountType());
        product.setAccountNumber(productDTO.getAccountNumber());
        product.setStatus(productDTO.getStatus());
        product.setBalance(productDTO.getBalance());
        product.setExemptGmf(productDTO.getExemptGmf());
        product.setCreatedAt(productDTO.getCreatedAt());
        product.setUpdatedAt(productDTO.getUpdatedAt());

        return product;
    }
}
