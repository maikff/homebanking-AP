package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam long amount,
            @RequestParam String description,
            Authentication authentication) {
        if (fromAccountNumber.isBlank() || toAccountNumber.isBlank() || amount <= 0) {
            String missingField = "";
            if (fromAccountNumber.isBlank()) {
                missingField = "From Account Number";
            } else if (toAccountNumber.isBlank()) {
                missingField = "To Account Number";
            } else if (amount <= 0) {
                missingField = "The amount cannot be less than or equal to zero";
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(missingField + " is missing");
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The source account is the same as the destination account");
        }

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());

        Optional<Account> optionalOriginAccount = accountRepository.findByNumber(fromAccountNumber);

        if (!optionalOriginAccount.isPresent() || !optionalOriginAccount.get().getClient().equals(authenticatedClient)) {
            if (optionalOriginAccount == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This account does not exist");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this account");
            }
        }

        Account originAccount = optionalOriginAccount.get();

        Optional<Account> optionalDestinationAccount = accountRepository.findByNumber(toAccountNumber);

        if (!optionalDestinationAccount.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Destination account does not exist");
        }

        Account destinationAccount = optionalDestinationAccount.get();

        if (originAccount.getBalance() < amount) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the funds to complete this transaction");
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, amount, description + " (DEBIT " + fromAccountNumber + ")", LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description + " (CREDIT " + toAccountNumber + ")", LocalDateTime.now());

        originAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);

        originAccount.setBalance(originAccount.getBalance() - amount);

        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully.");
    }
}
