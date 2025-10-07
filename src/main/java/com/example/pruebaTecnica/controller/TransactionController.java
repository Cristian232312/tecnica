package com.example.pruebaTecnica.controller;

import com.example.pruebaTecnica.dto.ProductDTO;
import com.example.pruebaTecnica.dto.TransactionDTO;
import com.example.pruebaTecnica.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionDTO> makeTransaction(@RequestBody TransactionDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.makeTransaction(transactionDTO), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<TransactionDTO>> listTransaction() {
        return ResponseEntity.ok(transactionService.listTransaction());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.ok("Transacción con id "+id+" eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
