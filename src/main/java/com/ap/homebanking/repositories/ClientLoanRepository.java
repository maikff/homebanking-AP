package com.ap.homebanking.repositories;

import com.ap.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
    List<ClientLoan> findByClientEmailAndLoanName(String clientEmail, String loanName);
}
