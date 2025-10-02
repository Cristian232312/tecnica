package com.example.pruebaTecnica.service.impl;

import com.example.pruebaTecnica.Exception.ErrorMessages;
import com.example.pruebaTecnica.Exception.ResourceNotFoundException;
import com.example.pruebaTecnica.dto.ClientDTO;
import com.example.pruebaTecnica.entity.Client;
import com.example.pruebaTecnica.mapper.ClientMapper;
import com.example.pruebaTecnica.repository.ClientRepository;
import com.example.pruebaTecnica.service.ClientService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClientImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        validateClient(clientDTO);

        Client client = clientMapper.toEntity(clientDTO);

        client.setDateOfCreate(LocalDate.now());
        client.setDateOfUpdate(null);

        Client saved = clientRepository.save(client);

        return clientMapper.toDTO(saved);
    }

    @Override
    public List<ClientDTO> ListClient() {
        List<Client> clients = clientRepository.findAll();
        List<ClientDTO> dto = new ArrayList<>();

        for (Client client : clients) {
            dto.add(clientMapper.toDTO(client));
        }

        return dto;
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {

        validateClient(clientDTO);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.CLIENT_NOT_FOUND));

        if (clientDTO.getIdentificationType() != null && !clientDTO.getIdentificationType().isEmpty())
            client.setIdentificationType(clientDTO.getIdentificationType());

        if (clientDTO.getIdentificationNumber() != null && !clientDTO.getIdentificationNumber().isEmpty())
            client.setIdentificationNumber(clientDTO.getIdentificationNumber());

        if (clientDTO.getName() != null && !clientDTO.getName().isEmpty())
            client.setName(clientDTO.getName());

        if (clientDTO.getLastName() != null && !clientDTO.getLastName().isEmpty())
            client.setLastName(clientDTO.getLastName());

        if (clientDTO.getEmail() != null && !clientDTO.getEmail().isEmpty())
            client.setEmail(clientDTO.getEmail());

        if (clientDTO.getDateOfBirth() != null)
            client.setDateOfBirth(clientDTO.getDateOfBirth());

        client.setDateOfUpdate(LocalDate.now());

        Client updated = clientRepository.save(client);

        return clientMapper.toDTO(updated);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    private void validateClient(ClientDTO clientDTO) {
        validateName(clientDTO.getName());
        validateLastName(clientDTO.getLastName());
        validateEmail(clientDTO.getEmail());
        validateAge(clientDTO.getDateOfBirth());
    }

    private void validateName(String name) {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException(ErrorMessages.NAME_MIN_LENGTH_REQUIRED);
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().length() < 2) {
            throw new IllegalArgumentException(ErrorMessages.LASTNAME_MIN_LENGTH_REQUIRED);
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.EMAIL_NOT_EMPTY);
        }
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.compile(regex).matcher(email).matches()) {
            throw new IllegalArgumentException(ErrorMessages.EMAIL_INVALID_FORMAT);
        }
    }

    private void validateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException(ErrorMessages.DATE_OF_BIRTH_REQUIRED);
        }
        LocalDate today = LocalDate.now();
        int age = today.getYear() - dateOfBirth.getYear();
        if (dateOfBirth.plusYears(age).isAfter(today)) {
            age--;
        }
        if (age < 18) {
            throw new IllegalArgumentException(ErrorMessages.CLIENT_MUST_BE_ADULT);
        }
    }
}
