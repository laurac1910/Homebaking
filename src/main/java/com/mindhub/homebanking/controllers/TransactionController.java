package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.dtos.TransferDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Transactional
// cualquier operacion base dentro de la clase se ejecutara en una transaccion, si tiene exito se confirma en la base, si falla se revierte y no persiste
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/create")

    public ResponseEntity<?> newTransaction(@RequestBody TransferDTO transferDTO) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Client clientOrigin = clientRepository.findByEmail(userEmail);
            Account originAccount = accountRepository.findByNumber(transferDTO.accountOrigin());
            Account destinationAccount = accountRepository.findByNumber(transferDTO.accountDestination());

            if (!clientOrigin.getAccounts().stream().anyMatch(a -> a.getNumber().equals(transferDTO.accountOrigin()))) {
                return ResponseEntity.badRequest().body("The account does not belong to the client");
            }

            if (transferDTO.accountOrigin().equals(transferDTO.accountDestination())) {
                return ResponseEntity.badRequest().body("The origin and destination accounts must be different");
            }

            if (!accountRepository.existsAccountByNumber(transferDTO.accountDestination())) {
                return ResponseEntity.badRequest().body("The destination account does not exist");
            }

            if (String.valueOf(transferDTO.amount()).isBlank()) {
                return ResponseEntity.badRequest().body("The amount must be greater than 0");
            }
            if (transferDTO.amount() <= 0) {
                return ResponseEntity.badRequest().body("The amount must be greater than 0");
            }

            if (!originAccount.getOwner().equals(clientOrigin)) {
                return ResponseEntity.badRequest().body("The account does not belong to the client");
            }

            if (originAccount.getBalance() < transferDTO.amount()) {
                return ResponseEntity.badRequest().body("The account does not have enough balance");
            }

            originAccount.setBalance(originAccount.getBalance() - transferDTO.amount());
            destinationAccount.setBalance(destinationAccount.getBalance() + transferDTO.amount());

            accountRepository.save(originAccount);
            accountRepository.save(destinationAccount);

            Transaction transactionDebit = new Transaction(TransactionType.DEBIT, transferDTO.amount(), LocalDate.now(), transferDTO.description());
            transactionRepository.save(transactionDebit);


            Transaction transactionCredit = new Transaction(TransactionType.CREDIT, transferDTO.amount(), LocalDate.now(), transferDTO.description());
            transactionRepository.save(transactionCredit);
            transactionCredit.setAccount(destinationAccount);
            transactionDebit.setAccount(originAccount);
            return ResponseEntity.ok().build();


        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An unexpected error occurred");
        }


    }
}

