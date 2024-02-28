package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins ="*" )
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<Client> clients = clientRepository.findAll();
        List<ClientDTO> clientDTOs = clients.stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(clientDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity <ClientDTO> getClientById (@PathVariable Long id){
        Client client = clientRepository.findById(id).orElse(null);

        if (client==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

        }

        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(clientDTO,HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
   String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok("Hello " + email);
    }

    @GetMapping("/current")
    public  ResponseEntity <?> getCLient ( ){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        return ResponseEntity.ok(new ClientDTO(client));
    }


}

