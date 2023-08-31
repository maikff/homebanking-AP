package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/clients/current", method = RequestMethod.GET)
    public ClientDTO getClientCurrent(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())); //map(client -> new ClientDTO(client));
    }
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository
                .findAll()
                .stream()
                .map(ClientDTO::new)
                .collect(toList());
    }

    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable Long id, Authentication authentication) {

        Client authenticadedClient = clientRepository.findByEmail(authentication.getName());

        Optional<Client> optionalClient = clientRepository.findById(id);

        if (authenticadedClient != null && optionalClient.isPresent()) {
            Client clientById = optionalClient.get();
            if (authenticadedClient.getId().equals(clientById.getId())) {
                ClientDTO clientDTO = new ClientDTO(clientById);
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


        if (clientRepository.findByEmail(email) !=  null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("mail already in use");
        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        clientRepository.save(newClient);

        Random random = new Random();
        boolean accountNumberExists;
        String number;

        do {
            int numRandom = random.nextInt(900000) + 100000;
            number = "VIN-" + numRandom;

            String finalNumber = number;
            accountNumberExists = clientRepository.findAll().stream()
                    .anyMatch(client -> client.getAccounts().stream()
                            .anyMatch(account -> account.getNumber().equals(finalNumber)));

            if (!accountNumberExists) {
                //create new client account
                Account newAccount = new Account(finalNumber, LocalDate.now(), 0.0);

                // Associate the account with the client
                newClient.addAccount(newAccount);

                //save account
                accountRepository.save(newAccount);
            }


        } while (accountNumberExists);


        return ResponseEntity.status(HttpStatus.CREATED).body("Successful registration");
    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            throw new ResourceNotFoundException("Client not found");
        }

        return new ClientDTO(client);
    }

}
