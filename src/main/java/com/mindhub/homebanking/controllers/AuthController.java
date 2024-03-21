package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.securityServices.JwtServices;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtServices jwtServices;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepostiroty;

    @Autowired
    private com.mindhub.homebanking.utils.metodos metodos;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        try {

            if (loginDTO.email().isBlank() || loginDTO.password().isBlank()) {
                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());
            final String jwt = jwtServices.generateToken(userDetails);
            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {

        try {
            Client existingClient = clientRepostiroty.findByEmail(registerDTO.email);
            if (existingClient != null) {
                return new ResponseEntity<>("Email already exists", HttpStatus.FORBIDDEN);
            }

            if (registerDTO.name.isEmpty() || registerDTO.lastName.isEmpty() || registerDTO.email.isEmpty() || registerDTO.password.isEmpty()) {
                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
            }

            if (!metodos.validateEmail(registerDTO.email)) {
                return new ResponseEntity<>("Invalid email", HttpStatus.FORBIDDEN);
            }

            if (registerDTO.password.length() < 8 ) {
                return new ResponseEntity<>("The password must have at least 8 characters", HttpStatus.FORBIDDEN);
            }

            Client client = new Client(registerDTO.name, registerDTO.lastName, registerDTO.email, passwordEncoder.encode(registerDTO.password));
            clientService.saveClient(client);
            String accountNumber = metodos.generateAccountNumber();
            Account account = new Account(accountNumber, LocalDate.now(), 0, client);

            accountRepository.save(account);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Account created success");
            response.put("client", client);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch
        (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }


}


