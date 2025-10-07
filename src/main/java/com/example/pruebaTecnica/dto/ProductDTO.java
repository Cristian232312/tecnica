package com.example.pruebaTecnica.dto;

import com.example.pruebaTecnica.entity.Enums.AccountStatus;
import com.example.pruebaTecnica.entity.Enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ProductDTO {

    private Long id;
    private AccountType accountType;
    private String accountNumber;
    private AccountStatus status;
    private BigDecimal balance;
    private Boolean exemptGmf;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long clientId;
    private List<TransactionDTO> transactions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getExemptGmf() {
        return exemptGmf;
    }

    public void setExemptGmf(Boolean exemptGmf) {
        this.exemptGmf = exemptGmf;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
