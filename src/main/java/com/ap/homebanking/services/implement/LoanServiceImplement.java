package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.Loan;
import com.ap.homebanking.repositories.LoanRepository;
import com.ap.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public List<Loan> getListLoans() {
        return loanRepository.findAll();
    }

    @Override
    public List<LoanDTO> mapToListLoansDTO(List<Loan> loanList) {
        return loanList.stream()
                .map(loan -> new LoanDTO(loan))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Loan> getOptionalLoanById(Long id) {
        return loanRepository.findById(id);
    }
}
