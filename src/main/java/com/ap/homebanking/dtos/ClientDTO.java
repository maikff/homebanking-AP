package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private Long id;
    Set<AccountDTO> accounts = new HashSet<>();
    private String firstName;
    private String lastName;
    private String email;


    public ClientDTO() {
    }

    public ClientDTO(Client client) {

        this.id = client.getId();

        this.accounts = client.getAccounts()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(toSet());

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

    }


    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

}
