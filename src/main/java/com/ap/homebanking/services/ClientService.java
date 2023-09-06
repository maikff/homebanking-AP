package com.ap.homebanking.services;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    void saveClient(Client client);

    List<ClientDTO> getClientsDTO();

    ClientDTO getClientDTO(Client client);

    Client getClientByEmail(String email);

    Client getClientById(Long id);

    List<Client> getClientsList();

    Client createClient(String firstName, String lastName, String email, String password);

}
