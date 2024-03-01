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
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/create")

    public ResponseEntity<?> newTransaction(@RequestBody TransferDTO transferDTO) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientRepository.findByEmail(userEmail);
            Account originAccount = accountRepository.findByNumber(transferDTO.accountOrigin());
            Account destinationAccount = accountRepository.findByNumber(transferDTO.accountDestination());


            if (!client.getAccounts().stream().anyMatch(account1 -> account1.getNumber().equals(transferDTO.accountOrigin()))) {
                return ResponseEntity.badRequest().body("The account does not belong to the client");
            }
            if (transferDTO.accountOrigin() == transferDTO.accountDestination()) {
                return ResponseEntity.badRequest().body("The account origin and destination must be different");
            }
            if (!accountRepository.existsAccountByNumber(transferDTO.accountDestination())) {
                return ResponseEntity.badRequest().body("The account destination does not exist");
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
            throw new RuntimeException(e);
        }


    }
}

