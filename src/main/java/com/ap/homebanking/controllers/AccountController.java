package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getListAccountDTO();
    }

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, login required");
        }
        Optional<Account> accountOptional = accountService.getOptionalAccountById(id);
        if (accountOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account with this ID not found");
        }
        Account account = accountOptional.get();
        Client authenticadedClient = clientService.getClientByEmail(authentication.getName());
        if (account.getClient().equals(authenticadedClient)) {
            AccountDTO accountDTO = accountService.getAccountDTO(account);
            return ResponseEntity.ok(accountDTO);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this information.");
        }
    }

    @RequestMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> getAccounts(Authentication authentication) {
        Client authenticatedClient = clientService.getClientByEmail(authentication.getName());
        if (authenticatedClient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, login required");
        }
        List<Account> clientAccounts = accountService.getAccountsByClient(authenticatedClient);
        if (clientAccounts == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No accounts found");
        }
        List<AccountDTO> accountDTOs = accountService.mapToAccountDTOList(clientAccounts);
        return ResponseEntity.ok(accountDTOs);
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client authenticatedClient = clientService.getClientByEmail(authentication.getName());
        if (authenticatedClient != null) {
            List<Account> accounts = accountService.getAccountsByClient(authenticatedClient);
            if (accounts.size() >= 3) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only have up to three accounts.");
            }
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
                    authenticatedClient.addAccount(newAccount);
                    accountService.saveAccount(newAccount);
                }
            } while (accountNumberExists);
            return ResponseEntity.status(HttpStatus.CREATED).body("Account created");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authenticated client.");
        }
    }
}
