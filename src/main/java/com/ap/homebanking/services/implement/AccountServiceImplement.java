package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Optional<Account> getOptionalAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public List<AccountDTO> getListAccountDTO() {
        return accountRepository
                .findAll()
                .stream()
                .map(account -> this.getAccountDTO(account))//o -> map(ClientDTO::new)
                .collect(toList());
    }

    @Override
    public Optional<Account> getOptionalAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> getAccountsByClient(Client client) {
        return accountRepository.findByClient(client);
    }

    @Override
    public List<AccountDTO> mapToAccountDTOList(List<Account> accounts) {
        return accounts.stream()
                .map(account -> this.getAccountDTO(account))
                .collect(toList());
    }

    @Override
    public AccountDTO getAccountDTO(Account account) {
        return new AccountDTO(account);
    }

    @Override
    public Account createAccount(String number, LocalDate creationDate, double balance) {
        return new Account(number, creationDate, balance);
    }

}
