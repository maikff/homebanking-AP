package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClientsDTO();
    }


    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            String missingField = "";
            if (firstName.isBlank()) {
                missingField = "First Name";
            } else if (lastName.isBlank()) {
                missingField = "Last Name";
            } else if (email.isBlank()) {
                missingField = "Email";
            } else if (password.isBlank()) {
                missingField = "Password";
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(missingField + " is missing");
        }


        if (clientService.getClientByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("mail already in use");
        }
        Client newClient = clientService.createClient(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);
        Random random = new Random();
        boolean accountNumberExists;
        String number;

        do {
            int numRandom = random.nextInt(900000) + 100000;
            number = "VIN-" + numRandom;
            String finalNumber = number;
            accountNumberExists = clientService.getClientsList().stream()
                    .anyMatch(client -> client.getAccounts().stream()
                            .anyMatch(account -> account.getNumber().equals(finalNumber)));
            if (!accountNumberExists) {
                Account newAccount = accountService.createAccount(finalNumber, LocalDate.now(), 0.0);
                newClient.addAccount(newAccount);
                accountService.saveAccount(newAccount);
            }
        } while (accountNumberExists);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successful registration");
    }

    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable Long id, Authentication authentication) {
        Client authenticadedClient = clientService.getClientByEmail(authentication.getName());
        Client client = clientService.getClientById(id);
        if (authenticadedClient != null && client != null) {
            if (authenticadedClient.getId().equals(client.getId())) {
                ClientDTO clientDTO = clientService.getClientDTO(client);
                return ResponseEntity.ok(clientDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this information.");
            }
        }
        if (authenticadedClient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Authenticated client not found");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("client with this ID not found");
        }
    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        Client client = clientService.getClientByEmail(authentication.getName());
        if (client == null) {
            throw new ResourceNotFoundException("Client not found");
        }
        return clientService.getClientDTO(client);
    }
}
