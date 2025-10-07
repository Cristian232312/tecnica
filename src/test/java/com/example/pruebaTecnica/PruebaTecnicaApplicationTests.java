package com.example.pruebaTecnica;

import com.example.pruebaTecnica.dto.ClientDTO;
import com.example.pruebaTecnica.dto.ProductDTO;
import com.example.pruebaTecnica.dto.TransactionDTO;
import com.example.pruebaTecnica.entity.Enums.AccountType;
import com.example.pruebaTecnica.service.ClientService;
import com.example.pruebaTecnica.service.ProductService;
import com.example.pruebaTecnica.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PruebaTecnicaApplicationTests {

	@Mock
	private ClientService clientService;

	@Mock
	private ProductService productService;

	@Mock
	private TransactionService transactionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	//  CLIENTE TESTS

	@Test
	@DisplayName("Debería crear un cliente correctamente")
	void testCreateClient() {
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setName("Carlos");
		clientDTO.setLastName("Pérez");
		clientDTO.setIdentificationNumber("12345");

		when(clientService.createClient(any(ClientDTO.class))).thenReturn(clientDTO);

		ClientDTO result = clientService.createClient(clientDTO);

		assertNotNull(result);
		assertEquals("Carlos", result.getName());
		verify(clientService, times(1)).createClient(clientDTO);
	}

	@Test
	@DisplayName("Debería listar todos los clientes")
	void testListClients() {
		List<ClientDTO> clients = Arrays.asList(new ClientDTO(), new ClientDTO());
		when(clientService.ListClient()).thenReturn(clients);

		List<ClientDTO> result = clientService.ListClient();

		assertEquals(2, result.size());
		verify(clientService, times(1)).ListClient();
	}

	@Test
	@DisplayName("Debería actualizar un cliente por ID")
	void testUpdateClient() {
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setName("Juan");

		when(clientService.updateClient(eq(1L), any(ClientDTO.class))).thenReturn(clientDTO);

		ClientDTO updated = clientService.updateClient(1L, clientDTO);

		assertEquals("Juan", updated.getName());
		verify(clientService).updateClient(1L, clientDTO);
	}

	@Test
	@DisplayName("Debería eliminar un cliente por ID")
	void testDeleteClient() {
		doNothing().when(clientService).deleteClient(1L);

		clientService.deleteClient(1L);

		verify(clientService, times(1)).deleteClient(1L);
	}

	// PRODUCTO TESTS

	@Test
	@DisplayName("Debería crear un producto correctamente")
	void testCreateProduct() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setAccountType(AccountType.AHORROS);
		productDTO.setAccountNumber("5312345678");
		productDTO.setBalance(BigDecimal.valueOf(5000));

		when(productService.createProduct(any(ProductDTO.class))).thenReturn(productDTO);

		ProductDTO result = productService.createProduct(productDTO);

		assertNotNull(result);
		assertEquals(AccountType.AHORROS, result.getAccountType());
		verify(productService).createProduct(productDTO);
	}


	@Test
	@DisplayName("Debería listar todos los productos")
	void testListProducts() {
		List<ProductDTO> products = Arrays.asList(new ProductDTO(), new ProductDTO());
		when(productService.listProducts()).thenReturn(products);

		List<ProductDTO> result = productService.listProducts();

		assertEquals(2, result.size());
		verify(productService).listProducts();
	}

	@Test
	@DisplayName("Debería actualizar un producto por ID")
	void testUpdateProduct() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setAccountType(AccountType.CORRIENTE);

		when(productService.updateProduct(eq(2L), any(ProductDTO.class))).thenReturn(productDTO);

		ProductDTO result = productService.updateProduct(2L, productDTO);

		assertNotNull(result);
		assertEquals(AccountType.CORRIENTE.name(), result.getAccountType().name());
		verify(productService).updateProduct(2L, productDTO);
	}

	@Test
	@DisplayName("Debería eliminar un producto por ID")
	void testDeleteProduct() {
		doNothing().when(productService).deleteProduct(2L);

		productService.deleteProduct(2L);

		verify(productService).deleteProduct(2L);
	}

	// TRANSACCIÓN TESTS

	@Test
	@DisplayName("Debería crear una transacción correctamente")
	void testMakeTransaction() {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setTransactionType("CONSIGNACION");
		transactionDTO.setAmount(BigDecimal.valueOf(1000));
		transactionDTO.setTransactionDate(LocalDateTime.now());

		when(transactionService.makeTransaction(any(TransactionDTO.class))).thenReturn(transactionDTO);

		TransactionDTO result = transactionService.makeTransaction(transactionDTO);

		assertNotNull(result);
		assertEquals("CONSIGNACION", result.getTransactionType());
		verify(transactionService).makeTransaction(transactionDTO);
	}

	@Test
	@DisplayName("Debería listar todas las transacciones")
	void testListTransactions() {
		List<TransactionDTO> transactions = Arrays.asList(new TransactionDTO(), new TransactionDTO());
		when(transactionService.listTransaction()).thenReturn(transactions);

		List<TransactionDTO> result = transactionService.listTransaction();

		assertEquals(2, result.size());
		verify(transactionService).listTransaction();
	}

	@Test
	@DisplayName("Debería eliminar una transacción por ID")
	void testDeleteTransaction() {
		doNothing().when(transactionService).deleteTransaction(3L);

		transactionService.deleteTransaction(3L);

		verify(transactionService).deleteTransaction(3L);
	}
}