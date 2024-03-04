package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.implServices.ClientServiceImpl;
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
  private ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> getClients() {
        return new ResponseEntity<>(clientService.getClients(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity <ClientDTO> getClientById (@PathVariable Long id){
        Client client = clientService.getClientById(id);

        if (client==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

        }

        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(clientDTO,HttpStatus.OK);
    }


    @GetMapping("/current")
    public  ResponseEntity <?> getCLient ( ){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(userEmail);
        return ResponseEntity.ok(new ClientDTO(client));
    }


}

