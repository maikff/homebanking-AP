package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public List<ClientDTO> getClientsDTO() {
        return clientRepository
                .findAll()
                .stream()
                .map(client -> new ClientDTO(client))//o -> map(ClientDTO::new)
                .collect(toList());
    }

    @Override
    public ClientDTO getClientDTO(Client client) {
        return new ClientDTO(client);
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findClientById(id);
    }

    @Override
    public List<Client> getClientsList() {
        return clientRepository.findAll();
    }

    @Override
    public Client createClient(String firstName, String lastName, String email, String password) {
        return new Client(firstName, lastName, email, password);
    }
}
