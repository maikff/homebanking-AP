package com.ap.homebanking.services;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    void saveAccount(Account account);

    Optional<Account> getOptionalAccountByNumber(String number);

    List<AccountDTO> getListAccountDTO();

    Optional<Account> getOptionalAccountById(Long id);

    List<Account> getAccountsByClient(Client client);

    List<AccountDTO> mapToAccountDTOList(List<Account> accounts);

    AccountDTO getAccountDTO(Account account);

    Account createAccount(String number, LocalDate creationDate, double balance);
}
