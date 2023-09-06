package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.LoanApplicationDTO;
import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.*;
import com.ap.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    ClientLoanService clientLoanService;

    @Autowired
    TransactionService transactionService;


    @RequestMapping(path = "/loans")
    public ResponseEntity<Object> getLoans(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }

        List<Loan> loans = loanService.getListLoans();
        List<LoanDTO> loanDTOs = loanService.mapToListLoansDTO(loans);

        return ResponseEntity.ok(loanDTOs);
    }

    @Transactional
    @RequestMapping(path = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }
        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    "Invalid data: the fields cannot be empty and the amount " +
                            "and fees cannot be less than or equal to zero");
        }

        Optional<Loan> optionalLoan = loanService.getOptionalLoanById(loanApplicationDTO.getLoanId());

        if (optionalLoan.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The loan does not exist");
        }

        Loan loan = optionalLoan.get();

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Loan amount exceeds maximum.");
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid number of payments");
        }

        Optional<Account> optionalAccount = accountService.getOptionalAccountByNumber(loanApplicationDTO.getToAccountNumber());

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Destination account does not exist");
        }

        List<ClientLoan> existingLoans = clientLoanService.getClientLoanByEmailAndLoanName(authentication.getName(), loan.getName());

        if (!existingLoans.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You already have a loan with this name.");
        }

        Account destinationAccount = optionalAccount.get();
        Client authenticadedClient = clientService.getClientByEmail(authentication.getName());
        List<Account> authenticatedClientAccounts = accountService.getAccountsByClient(authenticadedClient);

        if (!authenticatedClientAccounts.contains(destinationAccount)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This destination account does not belong to the logged in client");
        }
        long totalAmount = (long) (loanApplicationDTO.getAmount() * 1.20);

        ClientLoan loanRequest = clientLoanService.createClientLoan(authenticadedClient, loan, loanApplicationDTO.getPayments(), totalAmount);

        authenticadedClient.addClientLoan(loanRequest);

        Transaction creditTransaction = transactionService.createTransaction(TransactionType.CREDIT, totalAmount, loan.getName() + "Loan approved", LocalDateTime.now());

        destinationAccount.setBalance(destinationAccount.getBalance() + loanRequest.getAmount());

        clientLoanService.saveClientLoan(loanRequest);
        transactionService.saveTransaction(creditTransaction);
        accountService.saveAccount(destinationAccount);
        clientService.saveClient(authenticadedClient);

        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully requested loan");
    }
}
