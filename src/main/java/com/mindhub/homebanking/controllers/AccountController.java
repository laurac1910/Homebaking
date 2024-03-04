package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getClients() {
        return new ResponseEntity<>(accountService.getClients(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getClientById(@PathVariable Long id) {
        Account client = accountService.getClientById(id);

        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

        AccountDTO clientDTO = new AccountDTO(client);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }

    @PostMapping("/current")
    public ResponseEntity<?> createAccount() {

        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientService.getClientByEmail(userEmail);

            if (client == null) {
                return new ResponseEntity<>("Client Not Found", HttpStatus.FORBIDDEN);
            }
            if (client.getAccounts().size() >= 3) {
                return new ResponseEntity<>("The client already has the maximum number of accounts", HttpStatus.FORBIDDEN);
            }

            clientService.saveClient(client);
            String accountNumber = generateAccountNumber();
            Account account = new Account(accountNumber, LocalDate.now(), 0, client);
            accountService.saveAccount(account);


            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error creating account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private String generateAccountNumber() {
        String prefix = "VIN-";
        String numbers = String.valueOf((int) (Math.random() * 9000000) + 1000000);
        return prefix + numbers;
    }

}
