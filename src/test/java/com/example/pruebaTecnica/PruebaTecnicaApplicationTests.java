package com.example.pruebaTecnica;

import com.example.pruebaTecnica.Exception.ErrorMessages;
import com.example.pruebaTecnica.Exception.ResourceNotFoundException;
import com.example.pruebaTecnica.dto.ClientDTO;
import com.example.pruebaTecnica.dto.ProductDTO;
import com.example.pruebaTecnica.dto.TransactionDTO;
import com.example.pruebaTecnica.entity.Client;
import com.example.pruebaTecnica.entity.Enums.AccountStatus;
import com.example.pruebaTecnica.entity.Enums.AccountType;
import com.example.pruebaTecnica.entity.Product;
import com.example.pruebaTecnica.entity.Transaction;
import com.example.pruebaTecnica.mapper.ClientMapper;
import com.example.pruebaTecnica.mapper.ProductMapper;
import com.example.pruebaTecnica.mapper.TransactionMapper;
import com.example.pruebaTecnica.repository.ClientRepository;
import com.example.pruebaTecnica.repository.ProductRepository;
import com.example.pruebaTecnica.repository.TransactionRepository;
import com.example.pruebaTecnica.service.impl.ClientImpl;
import com.example.pruebaTecnica.service.impl.ProductImpl;
import com.example.pruebaTecnica.service.impl.TransactionImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PruebaTecnicaApplicationTests {

	@Mock
	private ClientRepository clientRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private ClientMapper clientMapper;

	@Mock
	private ProductMapper productMapper;

	@Mock
	private TransactionMapper transactionMapper;

	@InjectMocks
	private ClientImpl clientImpl;

	@InjectMocks
	private ProductImpl productImpl;

	@InjectMocks
	private TransactionImpl transactionImpl;

	@Test
	public void testCreateClient() {

		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setName("Cristian");
		clientDTO.setLastName("Cadena");
		clientDTO.setIdentificationNumber("12345");
		clientDTO.setEmail("cristian@gmail.com");
		clientDTO.setDateOfBirth(LocalDate.of(1995, 5, 20));

		Client clientEntity = new Client();
		clientEntity.setId(1L);
		clientEntity.setName("Cristian");
		clientEntity.setLastName("Cadena");
		clientEntity.setIdentificationNumber("12345");
		clientEntity.setEmail("cristian@gmail.com");
		clientEntity.setDateOfBirth(LocalDate.of(1995, 5, 20));

		Client saved = new Client();
		saved.setId(1L);
		saved.setName("Cristian");
		saved.setLastName("Cadena");
		saved.setIdentificationNumber("12345");
		saved.setEmail("cristian@gmail.com");
		saved.setDateOfBirth(LocalDate.of(1995, 5, 20));

		when(clientMapper.toEntity(clientDTO)).thenReturn(clientEntity);
		when(clientRepository.save(any(Client.class))).thenReturn(saved);
		when(clientMapper.toDTO(saved)).thenReturn(clientDTO);

		ClientDTO result = clientImpl.createClient(clientDTO);

		assertNotNull(result);
		assertEquals(clientDTO, result);
		verify(clientRepository).save(any(Client.class));
	}

	@Test
	public void testCreateClient_verifyIdentificationNumberAlreadyExist() {
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setName("Cristian");
		clientDTO.setLastName("Cadena");
		clientDTO.setIdentificationNumber("12345");
		clientDTO.setEmail("cristian@gmail.com");
		clientDTO.setDateOfBirth(LocalDate.of(1995, 5, 20));

		when(clientRepository.existsByIdentificationNumber("12345")).thenReturn(true);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,() -> {
			clientImpl.createClient(clientDTO);
		});

		assertEquals(ErrorMessages.IDENTIFICATION_NUMBER_ALREADY_EXISTS, exception.getMessage());
		verify(clientRepository, never()).save(any(Client.class));
	}

	@Test
	public void testListClient() {

		Client client = new Client();
		client.setId(1L);
		client.setName("Cristian");
		client.setIdentificationNumber("12345");

		ClientDTO dto = new ClientDTO();
		dto.setName("Cristian");
		dto.setIdentificationNumber("12345");

		when(clientRepository.findAll()).thenReturn(List.of(client));
		when(clientMapper.toDTO(client)).thenReturn(dto);

		List<ClientDTO> result = clientImpl.ListClient();

		assertNotNull(result);
		assertEquals(1, result.size());

		ClientDTO resultDTO = result.getFirst();

		assertEquals(dto.getName(), resultDTO.getName());
		assertEquals(dto.getIdentificationNumber(), resultDTO.getIdentificationNumber());

		verify(clientRepository).findAll();
		verify(clientMapper).toDTO(client);
	}

	@Test
	public void testUpdateClient_invalidName_shouldThrowException() {
		Long id = 1L;

		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setName("");
		clientDTO.setLastName("Cadena");
		clientDTO.setEmail("cristian@gmail.com");
		clientDTO.setDateOfBirth(LocalDate.of(1990, 3, 15));

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> clientImpl.updateClient(id, clientDTO)
		);

		assertEquals(ErrorMessages.NAME_MIN_LENGTH_REQUIRED, exception.getMessage());
		verify(clientRepository, never()).save(any());
	}

	@Test
	public void testUpdateClient_invalidEmail_shouldThrowException() {
		Long id = 1L;

		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setName("Cristian");
		clientDTO.setLastName("Cadena");
		clientDTO.setEmail("correo_invalido");
		clientDTO.setDateOfBirth(LocalDate.of(1990, 3, 15));

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> clientImpl.updateClient(id, clientDTO)
		);

		assertEquals(ErrorMessages.EMAIL_INVALID_FORMAT, exception.getMessage());
		verify(clientRepository, never()).save(any());
	}

	@Test
	public void testUpdateClient_withValidValues() {
		Long id = 1L;

		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setIdentificationType("CC");
		clientDTO.setIdentificationNumber("12345");
		clientDTO.setName("Cristian");
		clientDTO.setLastName("Cadena");
		clientDTO.setEmail("cristian@gmail.com");
		clientDTO.setDateOfBirth(LocalDate.of(1990, 3, 15));

		Client existingClient = new Client();
		existingClient.setId(id);

		when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
		when(clientRepository.save(any(Client.class))).thenReturn(existingClient);
		when(clientMapper.toDTO(any(Client.class))).thenReturn(new ClientDTO());

		clientImpl.updateClient(id, clientDTO);

		assertEquals("CC", existingClient.getIdentificationType());
		assertEquals("12345", existingClient.getIdentificationNumber());
		verify(clientRepository).save(existingClient);
	}

	@Test
	public void testDeleteClient_Success() {
		Long id = 1L;

		when(clientRepository.existsById(id)).thenReturn(true);
		when(productRepository.existsByClientId(id)).thenReturn(false);

		clientImpl.deleteClient(id);

		verify(clientRepository).existsById(id);
		verify(productRepository).existsByClientId(id);
		verify(clientRepository).deleteById(id);
	}

	@Test
	public void testDeleteClient_NotFound() {
		Long id = 99L;

		when(clientRepository.existsById(id)).thenReturn(false);

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> clientImpl.deleteClient(id));

		assertEquals(ErrorMessages.CLIENT_NOT_FOUND, exception.getMessage());

		verify(clientRepository).existsById(id);
		verify(clientRepository, never()).deleteById(anyLong());
		verify(productRepository, never()).existsByClientId(anyLong());
	}

	@Test
	public void testDeleteClient_HasLinkedProducts() {
		Long id = 2L;

		when(clientRepository.existsById(id)).thenReturn(true);
		when(productRepository.existsByClientId(id)).thenReturn(true);

		IllegalArgumentException exception =
				assertThrows(IllegalArgumentException.class, () -> clientImpl.deleteClient(id));

		assertEquals(ErrorMessages.CLIENT_HAS_LINKED_PRODUCTS, exception.getMessage());

		verify(clientRepository).existsById(id);
		verify(productRepository).existsByClientId(id);
		verify(clientRepository, never()).deleteById(anyLong());
	}

	// TESTS DE PRODUCT

	@Test
	public void testCreateProduct_Success() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setClientId(1L);
		productDTO.setAccountType(AccountType.AHORROS);
		productDTO.setBalance(BigDecimal.valueOf(50000));

		Client client = new Client();
		client.setId(1L);
		client.setName("Cristian");

		Product productEntity = new Product();
		productEntity.setId(1L);
		productEntity.setAccountType(AccountType.AHORROS);
		productEntity.setBalance(BigDecimal.valueOf(50000));
		productEntity.setClient(client);

		Product saved = new Product();
		saved.setId(1L);
		saved.setAccountType(AccountType.AHORROS);
		saved.setBalance(BigDecimal.valueOf(50000));
		saved.setClient(client);

		ProductDTO savedDTO = new ProductDTO();
		savedDTO.setId(1L);
		savedDTO.setAccountType(AccountType.AHORROS);
		savedDTO.setBalance(BigDecimal.valueOf(50000));

		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		when(productMapper.toEntity(productDTO)).thenReturn(productEntity);
		when(productRepository.save(any(Product.class))).thenReturn(saved);
		when(productMapper.toDTO(saved)).thenReturn(savedDTO);

		ProductDTO result = productImpl.createProduct(productDTO);

		assertNotNull(result);
		assertEquals(savedDTO.getAccountType(), result.getAccountType());
		assertEquals(savedDTO.getBalance(), result.getBalance());
		verify(clientRepository).findById(1L);
		verify(productRepository).save(any(Product.class));
	}

	@Test
	public void testCreateProduct_ClientNotFound() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setClientId(99L);
		productDTO.setAccountType(AccountType.AHORROS);
		productDTO.setBalance(BigDecimal.valueOf(10000));

		when(clientRepository.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> productImpl.createProduct(productDTO));

		assertEquals(ErrorMessages.CLIENT_NOT_FOUND, exception.getMessage());
		verify(clientRepository).findById(99L);
		verify(productRepository, never()).save(any());
	}

	@Test
	public void testCreateProduct_NegativeBalanceSavingsAccount() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setClientId(1L);
		productDTO.setAccountType(AccountType.AHORROS);
		productDTO.setBalance(BigDecimal.valueOf(-100));

		Client client = new Client();
		client.setId(1L);

		when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

		IllegalArgumentException exception =
				assertThrows(IllegalArgumentException.class, () -> productImpl.createProduct(productDTO));

		assertEquals(ErrorMessages.SAVINGS_ACCOUNT_MIN_BALANCE, exception.getMessage());
	}

	@Test
	public void testUpdateProduct_Success() {
		Long id = 1L;

		ProductDTO updateDTO = new ProductDTO();
		updateDTO.setAccountType(AccountType.CORRIENTE);
		updateDTO.setBalance(BigDecimal.valueOf(300000));
		updateDTO.setStatus(AccountStatus.ACTIVA);

		Product existing = new Product();
		existing.setId(id);
		existing.setAccountType(AccountType.CORRIENTE);
		existing.setBalance(BigDecimal.valueOf(100000));
		existing.setStatus(AccountStatus.INACTIVA);

		Product updated = new Product();
		updated.setId(id);
		updated.setAccountType(AccountType.CORRIENTE);
		updated.setBalance(BigDecimal.valueOf(300000));
		updated.setStatus(AccountStatus.ACTIVA);

		ProductDTO updatedDTO = new ProductDTO();
		updatedDTO.setId(id);
		updatedDTO.setAccountType(AccountType.CORRIENTE);
		updatedDTO.setBalance(BigDecimal.valueOf(300000));
		updatedDTO.setStatus(AccountStatus.ACTIVA);

		when(productRepository.findById(id)).thenReturn(Optional.of(existing));
		when(productRepository.save(any(Product.class))).thenReturn(updated);
		when(productMapper.toDTO(updated)).thenReturn(updatedDTO);

		ProductDTO result = productImpl.updateProduct(id, updateDTO);

		assertNotNull(result);
		assertEquals(BigDecimal.valueOf(300000), result.getBalance());
		assertEquals(AccountStatus.ACTIVA, result.getStatus());
		verify(productRepository).findById(id);
		verify(productRepository).save(any(Product.class));
	}

	@Test
	public void testListProducts() {
		Product product = new Product();
		product.setId(1L);
		product.setAccountType(AccountType.AHORROS);
		product.setBalance(BigDecimal.valueOf(10000));

		ProductDTO dto = new ProductDTO();
		dto.setId(1L);
		dto.setAccountType(AccountType.AHORROS);
		dto.setBalance(BigDecimal.valueOf(10000));

		when(productRepository.findAll()).thenReturn(List.of(product));
		when(productMapper.toDTO(product)).thenReturn(dto);

		List<ProductDTO> result = productImpl.listProducts();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(dto.getBalance(), result.getFirst().getBalance());
		verify(productRepository).findAll();
		verify(productMapper).toDTO(product);
	}

	@Test
	public void testDeleteProduct_Success() {
		Long id = 1L;

		Product product = new Product();
		product.setId(id);
		product.setBalance(BigDecimal.ZERO);

		when(productRepository.findById(id)).thenReturn(Optional.of(product));

		productImpl.deleteProduct(id);

		verify(productRepository).findById(id);
		verify(productRepository).deleteById(id);
	}

	@Test
	public void testMakeTransaction_Retiro() {
		TransactionDTO dto = new TransactionDTO();
		dto.setTransactionType("retiro");
		dto.setAccountNumber("12345");
		dto.setAmount(BigDecimal.valueOf(200));

		Product origin = new Product();
		origin.setAccountNumber("12345");
		origin.setBalance(BigDecimal.valueOf(500));
		origin.setStatus(AccountStatus.ACTIVA);

		Transaction entity = new Transaction();
		Transaction saved = new Transaction();
		TransactionDTO expected = new TransactionDTO();

		when(productRepository.findByAccountNumber("12345")).thenReturn(Optional.of(origin));
		when(transactionMapper.toEntity(dto)).thenReturn(entity);
		when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);
		when(transactionMapper.toDTO(saved)).thenReturn(expected);

		TransactionDTO result = transactionImpl.makeTransaction(dto);

		assertNotNull(result);
		verify(productRepository).save(origin);
		verify(transactionRepository).save(entity);
	}

	@Test
	public void testMakeTransaction_Consignacion() {
		TransactionDTO dto = new TransactionDTO();
		dto.setTransactionType("consignacion");
		dto.setDestinationAccountNumber("9999");
		dto.setAmount(BigDecimal.valueOf(300));

		Product destination = new Product();
		destination.setAccountNumber("9999");
		destination.setBalance(BigDecimal.valueOf(100));
		destination.setStatus(AccountStatus.ACTIVA);

		Transaction entity = new Transaction();
		Transaction saved = new Transaction();
		TransactionDTO expected = new TransactionDTO();

		when(productRepository.findByAccountNumber("9999")).thenReturn(Optional.of(destination));
		when(transactionMapper.toEntity(dto)).thenReturn(entity);
		when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);
		when(transactionMapper.toDTO(saved)).thenReturn(expected);

		TransactionDTO result = transactionImpl.makeTransaction(dto);

		assertNotNull(result);
		verify(productRepository).save(destination);
		verify(transactionRepository).save(entity);
	}

	@Test
	public void testMakeTransaction_Transferencia() {
		TransactionDTO dto = new TransactionDTO();
		dto.setTransactionType("transferencia");
		dto.setAccountNumber("1111");
		dto.setDestinationAccountNumber("2222");
		dto.setAmount(BigDecimal.valueOf(150));

		Product origin = new Product();
		origin.setAccountNumber("1111");
		origin.setBalance(BigDecimal.valueOf(500));
		origin.setStatus(AccountStatus.ACTIVA);

		Product destination = new Product();
		destination.setAccountNumber("2222");
		destination.setBalance(BigDecimal.valueOf(100));
		destination.setStatus(AccountStatus.ACTIVA);

		Transaction entity = new Transaction();
		Transaction saved = new Transaction();
		TransactionDTO expected = new TransactionDTO();

		when(productRepository.findByAccountNumber("1111")).thenReturn(Optional.of(origin));
		when(productRepository.findByAccountNumber("2222")).thenReturn(Optional.of(destination));
		when(transactionMapper.toEntity(dto)).thenReturn(entity);
		when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);
		when(transactionMapper.toDTO(saved)).thenReturn(expected);

		TransactionDTO result = transactionImpl.makeTransaction(dto);

		assertNotNull(result);
		verify(productRepository, times(1)).save(origin);
		verify(productRepository, times(1)).save(destination);
		verify(transactionRepository).save(entity);
	}

	@Test
	void testCreateTransaction_Transferencia_DestinationAccountNull() {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setTransactionType("transferencia");
		transactionDTO.setAmount(BigDecimal.valueOf(100));
		transactionDTO.setAccountNumber("123");
		transactionDTO.setDestinationAccountNumber("456");

		Product originAccount = new Product();
		originAccount.setAccountNumber("123");
		originAccount.setBalance(BigDecimal.valueOf(500));
		originAccount.setStatus(AccountStatus.ACTIVA);

		when(productRepository.findByAccountNumber("123"))
				.thenReturn(Optional.of(originAccount));
		when(productRepository.findByAccountNumber("456"))
				.thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			transactionImpl.makeTransaction(transactionDTO);
		});
	}

	@Test
	public void testMakeTransaction_InvalidAmount() {
		TransactionDTO dto = new TransactionDTO();
		dto.setTransactionType("retiro");
		dto.setAmount(BigDecimal.ZERO);

		assertThrows(IllegalArgumentException.class, () -> transactionImpl.makeTransaction(dto));
	}

	@Test
	public void testListTransaction() {
		Transaction transaction = new Transaction();
		transaction.setTransactionDate(LocalDateTime.now());

		TransactionDTO dto = new TransactionDTO();

		when(transactionRepository.findAll()).thenReturn(List.of(transaction));
		when(transactionMapper.toDTO(transaction)).thenReturn(dto);

		List<TransactionDTO> result = transactionImpl.listTransaction();

		assertNotNull(result);
		assertEquals(1, result.size());
		verify(transactionRepository).findAll();
		verify(transactionMapper).toDTO(transaction);
	}

	@Test
	public void testDeleteTransaction_Success() {
		Long id = 1L;
		Transaction transaction = new Transaction();
		transaction.setId(id);

		when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

		transactionImpl.deleteTransaction(id);

		verify(transactionRepository).deleteById(id);
	}
}