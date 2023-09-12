package com.ap.homebanking.services.implement;

import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.ClientLoan;
import com.ap.homebanking.models.Loan;
import com.ap.homebanking.repositories.ClientLoanRepository;
import com.ap.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public List<ClientLoan> getClientLoanByEmailAndLoanName(String email, String loanName) {
        return clientLoanRepository.findByClientEmailAndLoanName(email, loanName);
    }

    @Override
    public ClientLoan createClientLoan(Client client, Loan loan, int payments, long amount) {
        return new ClientLoan(client, loan, payments, amount);
    }
}
