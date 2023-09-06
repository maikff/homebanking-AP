package com.ap.homebanking.services;

import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.ClientLoan;
import com.ap.homebanking.models.Loan;

import java.util.List;

public interface ClientLoanService {
    void saveClientLoan(ClientLoan clientLoan);

    List<ClientLoan> getClientLoanByEmailAndLoanName(String email, String loanName);

    ClientLoan createClientLoan(Client client, Loan loan, int payments, long amount);


}
