package com.example.pruebaTecnica.service;

import com.example.pruebaTecnica.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    TransactionDTO makeTransaction(TransactionDTO transactionDTO);
    List<TransactionDTO> listTransaction();
    void deleteTransaction(Long id);
}