package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.LoanRequestDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/")
    public ResponseEntity<List<LoanDTO>> getClients() {
        List<Loan> clients = loanRepository.findAll();
        List<LoanDTO> clientDTOs = clients.stream()
                .map(LoanDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(clientDTOs, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/request")
    public ResponseEntity<?> createLoan(@RequestBody LoanRequestDTO loanRequestDTO) {
        try {
            Loan loanName = loanRepository.findByName(loanRequestDTO.name());
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientService.getClientByEmail(userEmail);
            Account destinationAccount = accountRepository.findByNumber(loanRequestDTO.destinationAccount());
            double totalAmount = loanRequestDTO.amount() * 1.20;


            if (loanRequestDTO.amount() <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0");
            }
            if (loanRequestDTO.payments() <= 0) {
                throw new IllegalArgumentException("You have to choose a quantity of payments");
            }
            if (loanName == null) {
                return new ResponseEntity<>("The loan does not exist", HttpStatus.NOT_FOUND);
            }
            if (loanRequestDTO.amount() > loanName.getMaxAmount()) {
                return new ResponseEntity<>("The amount is not available", HttpStatus.NOT_FOUND);
            }
            if (!loanName.getPayments().contains(loanRequestDTO.payments())) {
                return new ResponseEntity<>("The quantity of payments is not available", HttpStatus.NOT_FOUND);
            }
            if (destinationAccount == null) {
                return new ResponseEntity<>("The account does not exist", HttpStatus.NOT_FOUND);
            }
            if (!destinationAccount.getOwner().equals(client)) {
                return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
            }



            ClientLoan clientLoan = new ClientLoan(totalAmount, loanRequestDTO.payments());
            clientLoan.setClient(client);
            clientLoan.setLoan(loanName);

            clientLoanRepository.save(clientLoan);

            Transaction transaction1 = new Transaction(TransactionType.CREDIT, loanRequestDTO.amount(), LocalDate.now(), "Loan " + loanRequestDTO.name() + " approved"); // Usamos el monto original del préstamo
            transactionRepository.save(transaction1);
            destinationAccount.addTransaction(transaction1);
            destinationAccount.setBalance(destinationAccount.getBalance() + loanRequestDTO.amount());

            accountRepository.save(destinationAccount );
            client.addAccount(destinationAccount);
            clientService.saveClient(client);

            return new ResponseEntity<>(new ClientLoanDTO(clientLoan), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
