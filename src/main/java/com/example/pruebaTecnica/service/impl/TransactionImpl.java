package com.example.pruebaTecnica.service.impl;

import com.example.pruebaTecnica.Exception.ErrorMessages;
import com.example.pruebaTecnica.Exception.ResourceNotFoundException;
import com.example.pruebaTecnica.dto.TransactionDTO;
import com.example.pruebaTecnica.entity.Enums.AccountStatus;
import com.example.pruebaTecnica.entity.Product;
import com.example.pruebaTecnica.entity.Transaction;
import com.example.pruebaTecnica.mapper.TransactionMapper;
import com.example.pruebaTecnica.repository.ProductRepository;
import com.example.pruebaTecnica.repository.TransactionRepository;
import com.example.pruebaTecnica.service.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final ProductRepository productRepository;

    public TransactionImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper,
                           ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.productRepository = productRepository;
    }

    @Override
    public TransactionDTO makeTransaction(TransactionDTO transactionDTO) {

        //valida que el monto de la transaccion sea mayor a cero
        if (transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_AMOUNT);
        }

        String type = transactionDTO.getTransactionType().toLowerCase();

        Product originAccount = null;
        Product destinationAccount = null;

        //valida cuenta de origen solo si no es consignacion
        if (!type.equals("consignacion")) {
            originAccount = productRepository.findByAccountNumber(transactionDTO.getAccountNumber())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ACCOUNT_NUMBER_NOT_FOUND));

            if (!originAccount.getStatus().equals(AccountStatus.ACTIVA)) {
                throw new IllegalArgumentException(ErrorMessages.ACCOUNT_NOT_ACTIVE);
            }
        }

        //valida cuentas de destino para consignacion o transferencia
        if (transactionDTO.getDestinationAccountNumber() != null) {
            Optional<Product> destinationOpt = productRepository.findByAccountNumber(transactionDTO.getDestinationAccountNumber());
            if (destinationOpt.isEmpty()) {
                throw new ResourceNotFoundException(ErrorMessages.DESTINATION_ACCOUNT_NUMBER_NOT_FOUND);
            }

            destinationAccount = destinationOpt.get();

            if (!destinationAccount.getStatus().equals(AccountStatus.ACTIVA)) {
                throw new IllegalArgumentException(ErrorMessages.DESTINATION_ACCOUNT_NOT_ACTIVE);
            }

            if (originAccount != null &&
                    originAccount.getAccountNumber().equals(destinationAccount.getAccountNumber())) {
                throw new IllegalArgumentException(ErrorMessages.CANNOT_TRANSFER_TO_SAME_ACCOUNT);
            }

        }

        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction.setTransactionDate(LocalDateTime.now());

        switch (type) {

            case "retiro":

                if (originAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
                    throw new IllegalArgumentException(ErrorMessages.INSUFFICIENT_FUNDS_IN_ORIGIN_ACCOUNT);
                }

                originAccount.setBalance(originAccount.getBalance().subtract(transactionDTO.getAmount()));
                productRepository.save(originAccount);

                transaction.setProduct(originAccount);
                break;

            case "consignacion":
                if (destinationAccount == null) {
                    throw new ResourceNotFoundException(ErrorMessages.DESTINATION_ACCOUNT_NUMBER_NOT_FOUND);
                }

                destinationAccount.setBalance(destinationAccount.getBalance().add(transactionDTO.getAmount()));
                productRepository.save(destinationAccount);

                transaction.setProduct(destinationAccount);
                break;

            case "transferencia":
                if (destinationAccount == null) {
                    throw new ResourceNotFoundException(ErrorMessages.DESTINATION_ACCOUNT_NUMBER_NOT_FOUND);
                }

                if (originAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
                    throw new IllegalArgumentException(ErrorMessages.INSUFFICIENT_FUNDS_IN_ORIGIN_ACCOUNT);
                }

                originAccount.setBalance(originAccount.getBalance().subtract(transactionDTO.getAmount()));
                destinationAccount.setBalance(destinationAccount.getBalance().add(transactionDTO.getAmount()));
                productRepository.save(originAccount);
                productRepository.save(destinationAccount);

                transaction.setProduct(originAccount);
                break;

            default:
                throw new IllegalArgumentException(ErrorMessages.INVALID_TRANSACTION_TYPE);

        }

        Transaction saved = transactionRepository.save(transaction);

        return transactionMapper.toDTO(saved);
    }

    @Override
    public List<TransactionDTO> listTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDTO> transactionDTOs = new ArrayList<>();

        for (Transaction transaction : transactions) {
            transactionDTOs.add(transactionMapper.toDTO(transaction));
        }

        return transactionDTOs;
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.TRANSACTION_NOT_FOUND));

        transactionRepository.deleteById(id);
    }
}
