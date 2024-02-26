package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private  ClientRepository clientRepository;

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getClients() {
        List<Account> clients = accountRepository.findAll();
        List<AccountDTO> clientDTOs = clients.stream().map(AccountDTO::new).collect(Collectors.toList());

        return new ResponseEntity<>(clientDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity <AccountDTO> getClientById (@PathVariable Long id){
        Account client = accountRepository.findById(id).orElse(null);

        if (client==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

        }

        AccountDTO clientDTO = new AccountDTO(client);
        return new ResponseEntity<>(clientDTO,HttpStatus.OK);
    }
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountDTO>> getClientAccounts(@PathVariable Long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);

        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Set<Account> accounts = client.getAccounts();
        List<AccountDTO> accountDTOs = accounts.stream().map(AccountDTO::new).collect(Collectors.toList());

        return new ResponseEntity<>(accountDTOs, HttpStatus.OK);
    }
}
