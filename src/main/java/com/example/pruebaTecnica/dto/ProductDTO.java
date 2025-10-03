package com.example.pruebaTecnica.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductDTO {

    private String accountType;
    private String accountNumber;
    private String status;
    private BigDecimal balance;
    private Boolean exemptGmf;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long clientId;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
}
