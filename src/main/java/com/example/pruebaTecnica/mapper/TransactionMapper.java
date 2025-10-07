package com.example.pruebaTecnica.mapper;

import com.example.pruebaTecnica.dto.TransactionDTO;
import com.example.pruebaTecnica.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setProductId(transaction.getProduct().getId());

        if (transaction.getProduct() != null) {
            dto.setAccountNumber(transaction.getProduct().getAccountNumber());
        }

        if (transaction.getTransactionType().equalsIgnoreCase("transferencia") ||
                transaction.getTransactionType().equalsIgnoreCase("consignacion")) {
            dto.setDestinationAccountNumber(transaction.getProduct().getAccountNumber());
        }

        return dto;
    }

    public Transaction toEntity(TransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setId(transactionDTO.getId());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());

        return transaction;
    }
}