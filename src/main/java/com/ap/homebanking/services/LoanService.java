package com.ap.homebanking.services;

import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    void saveLoan(Loan loan);

    List<Loan> getListLoans();//53

    List<LoanDTO> mapToListLoansDTO(List<Loan> loanList); //56

    Optional<Loan> getOptionalLoanById(Long id);//87

}