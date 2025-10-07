package com.example.pruebaTecnica.service.impl;

import com.example.pruebaTecnica.Exception.ErrorMessages;
import com.example.pruebaTecnica.Exception.ResourceNotFoundException;
import com.example.pruebaTecnica.dto.ProductDTO;
import com.example.pruebaTecnica.entity.Client;
import com.example.pruebaTecnica.entity.Enums.AccountStatus;
import com.example.pruebaTecnica.entity.Enums.AccountType;
import com.example.pruebaTecnica.entity.Product;
import com.example.pruebaTecnica.mapper.ProductMapper;
import com.example.pruebaTecnica.repository.ClientRepository;
import com.example.pruebaTecnica.repository.ProductRepository;
import com.example.pruebaTecnica.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final ProductMapper productMapper;

    public ProductImpl(ProductRepository productRepository, ProductMapper productMapper, ClientRepository clientRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        validateAccountType(productDTO.getAccountType());

        Client client = clientRepository.findById(productDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.CLIENT_NOT_FOUND));

        // validar que el saldo de la cuentade ahorros no sea menor a cero
        if (productDTO.getAccountType() == AccountType.AHORROS
                && productDTO.getBalance() != null
                && productDTO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.SAVINGS_ACCOUNT_MIN_BALANCE);
        }

        String accountNumber = generateAccountNumber(productDTO.getAccountType());
        productDTO.setAccountNumber(accountNumber);

        // se establece como activa toda cuenta de ahorro creada
        if (productDTO.getStatus() == null) {
            productDTO.setStatus(productDTO.getAccountType() == AccountType.AHORROS ? AccountStatus.ACTIVA : AccountStatus.INACTIVA);
        }

        productDTO.setCreatedAt(LocalDate.now());

        Product product = productMapper.toEntity(productDTO);

        // se pasa el id del cliente a la entidad product
        product.setClient(client);

        Product saved = productRepository.save(product);

        return productMapper.toDTO(saved);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        validateAccountType(productDTO.getAccountType());

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND));

        // actualiza el tipo de cuenta si el cliente lo decide
        if (productDTO.getAccountType() != null)
            product.setAccountType(productDTO.getAccountType());

        // se valida que el saldo de la cuenta de ahorros no sea menor a cero
        if (productDTO.getBalance() != null) {
            if (product.getAccountType() == AccountType.AHORROS && productDTO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(ErrorMessages.SAVINGS_ACCOUNT_MIN_BALANCE);
            }
            product.setBalance(productDTO.getBalance());
        }

        if (productDTO.getStatus() != null) {
            // Solo se puede cancelar si el saldo es 0
            if (productDTO.getStatus() == AccountStatus.CANCELADA &&
                    product.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new IllegalArgumentException(ErrorMessages.CANNOT_CANCEL_ACCOUNT_WITH_BALANCE);
            }
            product.setStatus(productDTO.getStatus());
        }

        product.setUpdatedAt(LocalDate.now());

        Product updated = productRepository.save(product);

        return productMapper.toDTO(updated);
    }

    @Override
    public List<ProductDTO> listProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> dto = new ArrayList<>();

        for (Product product : products) {
            dto.add(productMapper.toDTO(product));
        }

        return dto;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND));

        // Solo se puede eliminar si está cancelada y saldo 0
        if (product.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException(ErrorMessages.CANNOT_DELETE_ACCOUNT_WITH_BALANCE);
        }

        productRepository.deleteById(id);
    }

    // Generar número de cuenta de 10 dígitos
    private String generateAccountNumber(AccountType accountType) {
        String prefix = accountType == AccountType.AHORROS ? "53" : "33";
        String number;
        Random random = new Random();
        do {
            number = prefix + String.format("%08d", random.nextInt(100_000_000));
        } while (productRepository.existsByAccountNumber(number));
        return number;
    }

    private void validateAccountType(AccountType accountType) {
        if (accountType == null) {
            throw new IllegalArgumentException(ErrorMessages.ACCOUNT_TYPE_REQUIRED);
        }
        if (!(accountType == AccountType.AHORROS  || accountType == AccountType.CORRIENTE)) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_ACCOUNT_TYPE);
        }
    }
}