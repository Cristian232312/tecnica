package com.example.pruebaTecnica;

import com.example.pruebaTecnica.Exception.ErrorMessages;
import com.example.pruebaTecnica.Exception.ResourceNotFoundException;
import com.example.pruebaTecnica.dto.ClientDTO;
import com.example.pruebaTecnica.entity.Client;
import com.example.pruebaTecnica.mapper.ClientMapper;
import com.example.pruebaTecnica.repository.ClientRepository;
import com.example.pruebaTecnica.repository.ProductRepository;
import com.example.pruebaTecnica.service.impl.ClientImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PruebaTecnicaApplicationTests {

	@Mock
	private ClientRepository clientRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ClientMapper clientMapper;

	@InjectMocks
	private ClientImpl clientImpl;

	@Test
	void testCreateClient() {

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
	void testListClient() {

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
	void testUpdateClient() {
		Long id = 1L;

		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setName("Cristian Actualizado");
		clientDTO.setLastName("Cadena");
		clientDTO.setEmail("cristian.actualizado@gmail.com");
		clientDTO.setDateOfBirth(LocalDate.of(1990, 3, 15));

		Client existingClient = new Client();
		existingClient.setId(id);
		existingClient.setName("Cristian");
		existingClient.setLastName("Cadena");
		existingClient.setIdentificationNumber("12345");
		existingClient.setEmail("cristian@gmail.com");
		existingClient.setDateOfBirth(LocalDate.of(1990, 3, 15));

		Client updatedClient = new Client();
		updatedClient.setId(id);
		updatedClient.setName("Cristian Actualizado");
		updatedClient.setLastName("Cadena");
		updatedClient.setIdentificationNumber("12345");
		updatedClient.setEmail("cristian.actualizado@gmail.com");
		updatedClient.setDateOfBirth(LocalDate.of(1990, 3, 15));

		ClientDTO updatedDTO = new ClientDTO();
		updatedDTO.setName("Cristian Actualizado");
		updatedDTO.setLastName("Cadena");
		updatedDTO.setIdentificationNumber("12345");
		updatedDTO.setEmail("cristian.actualizado@gmail.com");
		updatedDTO.setDateOfBirth(LocalDate.of(1990, 3, 15));

		when(clientRepository.findById(id)).thenReturn(java.util.Optional.of(existingClient));
		when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);
		when(clientMapper.toDTO(updatedClient)).thenReturn(updatedDTO);

		ClientDTO result = clientImpl.updateClient(id, clientDTO);

		assertNotNull(result);
		assertEquals("Cristian Actualizado", result.getName());
		assertEquals("cristian.actualizado@gmail.com", result.getEmail());
		verify(clientRepository).findById(id);
		verify(clientRepository).save(any(Client.class));
		verify(clientMapper).toDTO(updatedClient);
	}

	@Test
	void testDeleteClient_Success() {
		Long id = 1L;

		when(clientRepository.existsById(id)).thenReturn(true);
		when(productRepository.existsByClientId(id)).thenReturn(false);

		clientImpl.deleteClient(id);

		verify(clientRepository).existsById(id);
		verify(productRepository).existsByClientId(id);
		verify(clientRepository).deleteById(id);
	}

	@Test
	void testDeleteClient_NotFound() {
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
	void testDeleteClient_HasLinkedProducts() {
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
}